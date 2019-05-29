/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

/**
 *
 * @author Lu
 */
public class CashPaymentRequest {
    
    private String id;
    private String amount;
    private String date;
    private String cro;

    public String getId() {
        return id;
    }
    
    public Integer getIdAsInt() {
        return Integer.parseInt(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }
    
    public Double getAmountAsDouble() {
        return Double.parseDouble(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }
    
        
    public boolean isValid() {
        return id != null && amount != null && date != null;
    }
}
