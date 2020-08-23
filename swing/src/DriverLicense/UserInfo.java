package DriverLicense;

import utils.DateUtils;
import utils.LicenseUtils;

public class UserInfo {
    private String cardId;
    
    private int cardType;
    
    private String fullName;
    
    private String birthDate;
    
    private String address;
    
    private String releaseDate;
    
    private String expireDate;

    public UserInfo(String cardId, int cardType, String fullName, String birthDate, String address, String releaseDate, String expireDate) {
        this.cardId = cardId;
        this.cardType = cardType;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.releaseDate = releaseDate;
        this.expireDate = expireDate;
    }

    public UserInfo() {
        
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

    public String getReleaseDate() {
        return DateUtils.toDateString(releaseDate);
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

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
    
    
}
