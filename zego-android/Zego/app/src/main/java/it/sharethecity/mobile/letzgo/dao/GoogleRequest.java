package it.sharethecity.mobile.letzgo.dao;

/**
 * Created by lucabellaroba on 21/09/16.
 */
public class GoogleRequest {

        private String term;
        private Integer radius;
        private Double lat;
        private Double lng;

        /**
         * @return the term
         */
        public String getTerm() {
            return term;
        }

        /**
         * @param term the term to set
         */
        public void setTerm(String term) {
            this.term = term;
        }

        /**
         * @return the radius
         */
        public Integer getRadius() {
            return radius;
        }

        /**
         * @param radius the radius to set
         */
        public void setRadius(Integer radius) {
            this.radius = radius;
        }

        /**
         * @return the lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         * @param lat the lat to set
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         * @return the lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         * @param lng the lng to set
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
