package it.sharethecity.mobile.letzgo.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;

import it.sharethecity.mobile.letzgo.application.ApplicationController;

/**
 * Created by lucabellaroba on 18/10/15.
 */
public final class InternalStorage {

    public static final String DIRECTORY = "imageDir";
    public static final ContextWrapper cw;
    static {
         cw = new ContextWrapper(ApplicationController.getInstance().getApplicationContext());
    }
    private InternalStorage() {}

    public static String saveToInternalSorage(Bitmap bitmapImage, String name){
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(DIRECTORY, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(String name, int width)
    {
        Bitmap b = null;
        try {
            File f = new File(cw.getDir(DIRECTORY, Context.MODE_PRIVATE).getAbsolutePath(), name);
            //File f=new File(path, "profile.jpg");

            b = UtilityFunctions.decodeSampledBitmapFromStream(f, width);
            // b = BitmapFactory.decodeStream(new FileInputStream(f));
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return b;
    }

    public static boolean isImageAlreadyExists(String name){
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(DIRECTORY, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);
        return mypath!=null?mypath.exists():false;
    }

    public static File getBitmapPath(String s){
        return  new File(cw.getDir(DIRECTORY, Context.MODE_PRIVATE).getAbsolutePath(), s);
    }


    public static void saveImageInInternalMemoryAsync(final Bitmap bitmap, final String imageName, final InternalStorageListener listener) {
        new AsyncTask<Bitmap, Void, String>() {
            @Override
            protected String doInBackground(Bitmap... params) {


                return  saveToInternalSorage(params[0], imageName);
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(listener == null) return;

                if(result == null){
                    listener.onSaveFailed();
                }else{
                    listener.onSaveSucceed(imageName);
                }

            }
        }.execute(bitmap);
    }

    public interface InternalStorageListener{
        void onSaveSucceed(String savedImageName);
        void onSaveFailed();

    }
}
