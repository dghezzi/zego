package it.elbuild.zego.util.google;

/**
 * Created by Lu on 24/01/16.
 */
public class DistanceMatrixElement {

    private  DistanceMatrixTuple distance;

    private  DistanceMatrixTuple duration;
    
     private  DistanceMatrixTuple duration_in_traffic;

    private String status;

    public DistanceMatrixTuple getDistance() {
        return distance;
    }

    public void setDistance(DistanceMatrixTuple distance) {
        this.distance = distance;
    }

    public DistanceMatrixTuple getDuration() {
        return duration;
    }

    public void setDuration(DistanceMatrixTuple duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the duration_in_traffic
     */
    public DistanceMatrixTuple getDuration_in_traffic() {
        return duration_in_traffic;
    }

    /**
     * @param duration_in_traffic the duration_in_traffic to set
     */
    public void setDuration_in_traffic(DistanceMatrixTuple duration_in_traffic) {
        this.duration_in_traffic = duration_in_traffic;
    }
}
