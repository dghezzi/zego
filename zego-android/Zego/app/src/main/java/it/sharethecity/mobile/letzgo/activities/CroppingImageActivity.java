package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.albinmathew.photocrop.cropoverlay.edge.Edge;
import com.albinmathew.photocrop.cropoverlay.utils.ImageViewUtil;
import com.albinmathew.photocrop.photoview.PhotoView;
import com.albinmathew.photocrop.photoview.PhotoViewAttacher;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.BuildConfig;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.MyCropOverlayView;
import it.sharethecity.mobile.letzgo.utilities.CapturePhotoUtils;
import it.sharethecity.mobile.letzgo.utilities.InternalStorage;
import it.sharethecity.mobile.letzgo.utilities.InternalStorageContentProvider;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;


public class CroppingImageActivity extends ZegoBaseActivity {

    public static final String TAG = "ImageCropActivity";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROPPED_PICTURE = 0x3;

    public static final String ACTION_CAMERA = "action-camera";
    public static final String ACTION_GALLERY = "action-gallery";
    public static final String IMAGE_PATH = "image-path";
    public static final String TYPE_OF_PHOTO = "type_of_photo";


    public static final String ERROR_MSG = "error_msg";
    public static final String ERROR = "error";

    public static final String DEAFULT_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_NAME = "profile_name";


    private final int IMAGE_MAX_SIZE = 1024;
    private final Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;


    private ContentResolver mContentResolver;
    private float minScale = 1f;
    //Temp file to save cropped image
    private String mImagePath;
    private Uri mSaveUri = null;
    private Uri mImageUri = null;




    @Nullable
    @BindView(R.id.progress_bar)
    ProgressWheel progressBar;

    @Nullable
    @BindView(R.id.iv_photo)
    PhotoView  mImageView;

    @Nullable
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @Nullable
    @BindView(R.id.sendBtn)
    Button btnSend;

    @Nullable
    @BindView(R.id.cancelBtn)
    Button btnCancel;

    @Nullable
    @BindView(R.id.crop_overlay)
    MyCropOverlayView mCropOverlayView;

    Bundle savedInstanceState;

    //File for capturing camera images
    private File mFileTemp;
    private View.OnClickListener btnCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userCancelled();
        }
    };
    private View.OnClickListener btnSendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            btnSend.setEnabled(false);

            cropImageTask();
//            saveUploadCroppedImage();
        }
    };


    private AsyncTask<Bitmap, Void, Bitmap> cropTask;
    private boolean isFromCAmera;

    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

    }

    public static int getCameraPhotoOrientation(@NonNull Context context, Uri imageUri) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            ExifInterface exif = new ExifInterface(
                    imageUri.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropping_image);
        ButterKnife.bind(this);

        user = ApplicationController.getInstance().getUserLogged();
       // bind();
        setListeners();
        mContentResolver = getContentResolver();

        proceedToCrop(savedInstanceState);

    }



    private void bind() {
        mCropOverlayView = new MyCropOverlayView(this,0,false);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCropOverlayView.setLayoutParams(p);

        rootLayout.addView(mCropOverlayView);
    }



    private void setListeners() {
        btnSend.setOnClickListener(btnSendListener);

        btnCancel.setOnClickListener(btnCancelListener);


        mImageView.addListener(new PhotoViewAttacher.IGetImageBounds() {
            @Override
            public Rect getImageBounds() {
                return new Rect((int) Edge.LEFT.getCoordinate(), (int) Edge.TOP.getCoordinate(), (int) Edge.RIGHT.getCoordinate(), (int) Edge.BOTTOM.getCoordinate());
            }
        });
    }

    private void proceedToCrop(Bundle savedInstanceState) {
        createTempFile();
        if(savedInstanceState != null){
            isFromCAmera = savedInstanceState.getBoolean("isFromCamera");
        }
        if (savedInstanceState == null || !savedInstanceState.getBoolean("restoreState")) {
            String action = getIntent().getStringExtra("ACTION");
            if (null != action) {
                switch (action) {
                    case ACTION_CAMERA:
                        getIntent().removeExtra("ACTION");
                        isFromCAmera = true;
                        takePic();
                        return;
                    case ACTION_GALLERY:
                        getIntent().removeExtra("ACTION");
                        isFromCAmera = false;
                        pickImage();
                        return;
                }
            }
        }
        mImagePath = mFileTemp.getPath();
        mSaveUri = Uri.fromFile(new File(mImagePath));
        mImageUri = Uri.fromFile(new File(mImagePath));
        init(isFromCAmera);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRunningTask();
    }

    private void init(boolean isFromCamera) {
        Bitmap b = getBitmap(mImageUri);

        Drawable bitmap = new BitmapDrawable(getResources(), b);
        int h = bitmap.getIntrinsicHeight();
        int w = bitmap.getIntrinsicWidth();
        final float cropWindowWidth = Edge.getWidth();
        final float cropWindowHeight = Edge.getHeight();
        if (h <= w) {
            minScale = (cropWindowHeight + 1f) / h;
        } else if (w < h) {
            minScale = (cropWindowWidth + 1f) / w;
        }

        mImageView.setMaximumScale(minScale * 3);
        mImageView.setMediumScale(minScale * 2);
        mImageView.setMinimumScale(minScale);
        mImageView.setImageDrawable(bitmap);
        mImageView.setScale(minScale);

    }

    private void saveUploadCroppedImage() {
        boolean saved = true;
        if (saved) {
            //USUALLY Upload image to server here

           // Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
            Picasso.with(getBaseContext())
                    .load(mSaveUri)
                    .resize(200,200)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            final String fileName = user.getId() + "_" + Calendar.getInstance(TimeZone.getTimeZone("CET")).getTimeInMillis() + ".jpg";

                            InternalStorage.saveImageInInternalMemoryAsync(bitmap, DEAFULT_NAME, new InternalStorage.InternalStorageListener() {
                                @Override
                                public void onSaveSucceed(String savedImageName) {
                                    File file = InternalStorage.getBitmapPath(DEAFULT_NAME);


                                    TransferUtility transferUtility = new TransferUtility(getS3Client(), getApplicationContext());
                                    TransferObserver observer = transferUtility.upload(
                                            ZegoConstants.AWS3.BUCKET,     /* The bucket to upload to */
                                            fileName,
                                            file        /* The file where the data to upload exists */
                                    );



                                    observer.setTransferListener(new TransferListener() {
                                        @Override
                                        public void onStateChanged(int id, TransferState state) {
                                            if(state == TransferState.COMPLETED){
                                                Intent i = new Intent();
                                                i.putExtra(PROFILE_IMAGE_NAME,fileName);
                                                setResult(RESULT_OK,i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                                        }

                                        @Override
                                        public void onError(int id, Exception ex) {
                                            showInfoDialog(getString(R.string.warning),getString(R.string.profile_saving_ko));
                                            resetUiState();
                                        }
                                    });


                                }

                                @Override
                                public void onSaveFailed() {
                                    showInfoDialog(getString(R.string.warning),getString(R.string.profile_saving_ko));
                                    resetUiState();
                                }
                            });

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            showInfoDialog(getString(R.string.warning),getString(R.string.profile_saving_ko));
                            resetUiState();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });


        }

        else {
            resetUiState();
        }

    }

    private void resetUiState() {
        progressBar.setVisibility(View.GONE);
        btnSend.setEnabled(true);
    }

    private void createTempFile() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

    private void takePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    mImageCaptureUri = FileProvider.getUriForFile(CroppingImageActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            mFileTemp);
                }else{
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
                }

            } else {
                /*
                 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    mImageCaptureUri = FileProvider.getUriForFile(CroppingImageActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            mFileTemp);
                }else {
                    mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                }
//

            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't take picture", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("restoreState", true);
        savedInstanceState.putBoolean("isFromCamera", isFromCAmera);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image source available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
      //  super.onActivityResult(requestCode, resultCode, result);
        createTempFile();
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mImagePath = mFileTemp.getPath();
                mSaveUri =  Uri.fromFile(new File(mImagePath));
                mImageUri =  Uri.fromFile(new File(mImagePath));
                isFromCAmera = true;
                init(isFromCAmera);
            } else if (resultCode == RESULT_CANCELED) {
                userCancelled();
            } else {
                errored();
            }

        } else if (requestCode == REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                userCancelled();
                return;
            } else if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(result.getData()); // Got the bitmap .. Copy it to the temp file for cropping
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    mImagePath = mFileTemp.getPath();

                    mSaveUri =  Uri.fromFile(new File(mImagePath));
                    mImageUri =  Uri.fromFile(new File(mImagePath));
                    isFromCAmera = false;
                    init(isFromCAmera);
                } catch (Exception e) {
                    errored();
                }
            } else {
                errored();
            }

        }
    }

    private Bitmap getBitmap(Uri uri) {
        InputStream in = null;
        Bitmap returnedBitmap = null;
        try {
            in = mContentResolver.openInputStream(uri);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int scale = 1;

  //          scale = UtilityFunctions.calculateInSampleSize(o,getWithImageByType(),getHeightImageByType());
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, o2);
            in.close();
            returnedBitmap = fixOrientationBugOfProcessedBitmap(bitmap);
            return returnedBitmap;
        } catch (FileNotFoundException e) {
           // Log.d(TAG, "FileNotFoundException");
        } catch (IOException e) {
           // Log.d(TAG, "IOException");
        }
        return null;
    }

    private Bitmap fixOrientationBugOfProcessedBitmap(Bitmap bitmap) {
        try {
            if (getCameraPhotoOrientation(this, Uri.parse(mFileTemp.getPath())) == 0) {
                return bitmap;
            } else {
                Matrix matrix = new Matrix();
                matrix.postRotate(getCameraPhotoOrientation(this, Uri.parse(mFileTemp.getPath())));
                // recreate the new Bitmap and set it back
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Bitmap getCurrentDisplayedImage() {
        Bitmap result = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);
        mImageView.draw(c);
        return result;
    }

    public Bitmap getCroppedImage() {

        Bitmap mCurrentDisplayedBitmap = getCurrentDisplayedImage();
        Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(mCurrentDisplayedBitmap, mImageView);

        // Get the scale factor between the actual Bitmap dimensions and the
        // displayed dimensions for width.
        float actualImageWidth = mCurrentDisplayedBitmap.getWidth();
        float displayedImageWidth = displayedImageRect.width();
        float scaleFactorWidth = actualImageWidth / displayedImageWidth;

        // Get the scale factor between the actual Bitmap dimensions and the
        // displayed dimensions for height.
        float actualImageHeight = mCurrentDisplayedBitmap.getHeight();
        float displayedImageHeight = displayedImageRect.height();
        float scaleFactorHeight = actualImageHeight / displayedImageHeight;

        // Get crop window position relative to the displayed image.
        float cropWindowX = Edge.LEFT.getCoordinate() - displayedImageRect.left;
        float cropWindowY = Edge.TOP.getCoordinate() - displayedImageRect.top;
        float cropWindowWidth = Edge.getWidth();
        float cropWindowHeight = Edge.getHeight();

        // Scale the crop window position to the actual size of the Bitmap.
        float actualCropX = cropWindowX * scaleFactorWidth;
        float actualCropY = cropWindowY * scaleFactorHeight;
        float actualCropWidth = cropWindowWidth * scaleFactorWidth;
        float actualCropHeight = cropWindowHeight * scaleFactorHeight;

        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(mCurrentDisplayedBitmap, (int) actualCropX, (int) actualCropY, (int) actualCropWidth, (int) actualCropHeight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean saveOutput() {
        Bitmap croppedImage = getCroppedImage();
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 100, outputStream);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            } finally {
                closeSilently(outputStream);
            }
        } else {
           // Log.e(TAG, "not defined image url");
            return false;
        }
        croppedImage.recycle();
        return true;
    }


    public void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }

    private void stopRunningTask(){
        if(cropTask != null && cropTask.getStatus() == AsyncTask.Status.RUNNING){
            cropTask.cancel(true);
        }

    }

    public void userCancelled() {
        btnSend.setEnabled(true);
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void errored() {
        Intent intent = new Intent();
        intent.putExtra(ERROR, true);
        if ("Error while opening the image file. Please try again." != null) {
            intent.putExtra(ERROR_MSG, "Error while opening the image file. Please try again.");
        }
        finish();
    }





    private void cropImageTask(){

        int widthImageToCrop = mImageView.getWidth();
        int heigthImageToCrop = mImageView.getHeight();
        Bitmap result = Bitmap.createBitmap(widthImageToCrop, heigthImageToCrop, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);
        mImageView.draw(c);
        final Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(result, mImageView);

        cropTask = new AsyncTask<Bitmap,Void,Bitmap>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Bitmap doInBackground(Bitmap... param) {
                // Get the scale factor between the actual Bitmap dimensions and the
                // displayed dimensions for width.
                float actualImageWidth = param[0].getWidth();
                float displayedImageWidth = displayedImageRect.width();
                float scaleFactorWidth = actualImageWidth / displayedImageWidth;

                // Get the scale factor between the actual Bitmap dimensions and the
                // displayed dimensions for height.
                float actualImageHeight = param[0].getHeight();
                float displayedImageHeight = displayedImageRect.height();
                float scaleFactorHeight = actualImageHeight / displayedImageHeight;

                // Get crop window position relative to the displayed image.
                float cropWindowX = Edge.LEFT.getCoordinate() - displayedImageRect.left;
                float cropWindowY = Edge.TOP.getCoordinate() - displayedImageRect.top;
                float cropWindowWidth = Edge.getWidth();
                float cropWindowHeight = Edge.getHeight();

                // Scale the crop window position to the actual size of the Bitmap.
                float actualCropX = cropWindowX * scaleFactorWidth;
                float actualCropY = cropWindowY * scaleFactorHeight;
                float actualCropWidth = cropWindowWidth * scaleFactorWidth;
                float actualCropHeight = cropWindowHeight * scaleFactorHeight;

                // Crop the subset from the original Bitmap.
                Bitmap bitmap = Bitmap.createBitmap(param[0], (int) actualCropX, (int) actualCropY, (int) actualCropWidth, (int) actualCropHeight);
                if(bitmap != null){
                    if (mSaveUri != null) {
                        OutputStream outputStream = null;
                        try {
                            outputStream = mContentResolver.openOutputStream(mSaveUri);
                            if (outputStream != null) {
                                bitmap.compress(mOutputFormat, 100, outputStream);
                            }
                        } catch (IOException ex) {
                            bitmap = null;
                        } finally {
                            closeSilently(outputStream);
                        }
                    } else {
                        bitmap = null;
                    }

                }

                return bitmap;
            }



            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(bitmap != null){
                    saveUploadCroppedImage();
                }else{

                }

            }
        }.execute(result);
    }





}

