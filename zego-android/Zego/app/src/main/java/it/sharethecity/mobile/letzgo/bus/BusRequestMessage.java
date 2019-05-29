package it.sharethecity.mobile.letzgo.bus;

/**
 * Created by lucabellaroba on 30/06/16.
 */
public class BusRequestMessage {
    private int code;
    private Object payLoad;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(Object payLoad) {
        this.payLoad = payLoad;
    }
}
