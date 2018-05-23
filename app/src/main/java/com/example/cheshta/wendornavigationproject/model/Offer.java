package com.example.cheshta.wendornavigationproject.model;

public class Offer {
    public String offerTitle, offerDescription;

    public Offer(String offerTitle, String offerDescription) {
        this.offerTitle = offerTitle;
        this.offerDescription = offerDescription;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }
}
