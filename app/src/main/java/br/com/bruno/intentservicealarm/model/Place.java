package br.com.bruno.intentservicealarm.model;

/**
 * Created by Bruno on 10/04/2016.
 */
public class Place {

    private int id;
    private int place_id;
    private String title;
    private String address;
    private String city;
    private String state;
    private String phone;
    private String latitude;
    private String longitude;
    private String avarageRating;
    private String lastReviewIntro;
    private String businessUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAvarageRating() {
        return avarageRating;
    }

    public void setAvarageRating(String avarageRating) {
        this.avarageRating = avarageRating;
    }

    public String getLastReviewIntro() {
        return lastReviewIntro;
    }

    public void setLastReviewIntro(String lastReviewIntro) {
        this.lastReviewIntro = lastReviewIntro;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }
}
