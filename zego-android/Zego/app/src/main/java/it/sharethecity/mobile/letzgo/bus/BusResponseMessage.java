package it.sharethecity.mobile.letzgo.bus;

/**
 * Created by lucabellaroba on 30/06/16.
 */
public class BusResponseMessage {
    private int code;
    private Object message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
