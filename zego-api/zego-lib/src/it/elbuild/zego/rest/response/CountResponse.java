/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response;

/**
 *
 * @author Lu
 */
public class CountResponse {

        private Long ct;
        private String entity;

        /**
         * @return the ct
         */
        public Long getCt() {
            return ct;
        }

        /**
         * @param ct the ct to set
         */
        public void setCt(Long ct) {
            this.ct = ct;
        }

        /**
         * @return the entity
         */
        public String getEntity() {
            return entity;
        }

        /**
         * @param entity the entity to set
         */
        public void setEntity(String entity) {
            this.entity = entity;
        }
    }
