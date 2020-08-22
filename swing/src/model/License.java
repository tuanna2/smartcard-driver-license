/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import utils.DateUtils;
import utils.LicenseUtils;

/**
 *
 * @author superuser
 */
public class License {
    private String cardId;
    
    private int cardType;
    
    private String fullName;
    
    private String birthDate;
    
    private String address;
    
    private String grantedDate;
    
    private String expireDate;

    public License(String cardId, int cardType, String fullName, String birthDate, String address, String grantedDate, String expireDate) {
        this.cardId = cardId;
        this.cardType = cardType;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.grantedDate = grantedDate;
        this.expireDate = expireDate;
    }

    public License() {
        
    }
    

    public String getCardId() {
        return cardId;
    }

    public String getCardType() {
        return LicenseUtils.cardType(cardType);
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthDate() {
        return DateUtils.toDateString(birthDate);
    }

    public String getAddress() {
        return address;
    }

    public String getGrantedDate() {
        return DateUtils.toDateString(grantedDate);
    }

    public String getExpireDate() {
        return DateUtils.toDateString(expireDate);
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGrantedDate(String grantedDate) {
        this.grantedDate = grantedDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
    
    
}
