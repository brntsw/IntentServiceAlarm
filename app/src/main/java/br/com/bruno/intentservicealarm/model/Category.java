package br.com.bruno.intentservicealarm.model;

/**
 * Created by Bruno on 10/04/2016.
 */
public class Category {

    private int id;
    private int place_id;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
