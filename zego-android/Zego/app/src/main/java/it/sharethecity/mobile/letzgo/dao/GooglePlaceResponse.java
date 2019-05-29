package it.sharethecity.mobile.letzgo.dao;

import java.util.List;

/**
 * Created by lucabellaroba on 21/09/16.
 */
public class GooglePlaceResponse {
    private List<GooglePlacePredictions> predictions;

    /**
     * @return the predictions
     */
    public List<GooglePlacePredictions> getPredictions() {
        return predictions;
    }

    /**
     * @param predictions the predictions to set
     */
    public void setPredictions(List<GooglePlacePredictions> predictions) {
        this.predictions = predictions;
    }
}
