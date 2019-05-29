package it.elbuild.zego.util.google;

/**
 * Created by Lu on 24/01/16.
 */
public class DistanceMatrixResponse {


    private String status;

    private DistanceMatrixRow[] rows;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DistanceMatrixRow[] getRows() {
        return rows;
    }

    public void setRows(DistanceMatrixRow[] rows) {
        this.rows = rows;
    }
}
