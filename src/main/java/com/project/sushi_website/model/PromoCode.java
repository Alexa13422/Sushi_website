package com.project.sushi_website.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;
@Entity(name = "promo_code")
public class PromoCode {
    @Id
    private String code;
    private Integer percentage;
    private Date expirationDate;

    public PromoCode(String code, Integer percentage, Date expirationDate) {
        this.code = code;
        this.percentage = percentage;
        this.expirationDate = expirationDate;
    }

    public PromoCode() {

    }

    public String getCode() {
        return code;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
