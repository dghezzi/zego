/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util.google;

import java.util.List;

/**
 *
 * @author Lu
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
