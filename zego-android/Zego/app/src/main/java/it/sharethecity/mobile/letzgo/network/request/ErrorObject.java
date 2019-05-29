package it.sharethecity.mobile.letzgo.network.request;

/**
 * Created by lucabellaroba on 24/06/16.
 */
public class ErrorObject {

    public static final int ERROR_CODE_MISSING_PAYMENT_METHOD = 135;
    public static final int ERROR_CODE_RIDE_NOT_PAID = 156;
    public static final int ERROR_CODE_MISSING_RIDE_TOO_SHORT = 121;
    public static final int ERROR_CODE_DEVICE_BLACKLISTED = 101;
    public static final int ERROR_CODE_MISSING_RIDE_TOO_FAR_FROM_PICKUP = 139;
    public static final int NEW_APP_AVAILABLE = 105;

    private Integer code;
    private String msg;
    private String title;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
