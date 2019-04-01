package com.example.applicationsmanagement.viewmodel;

import javax.validation.constraints.NotNull;

public class AppViewModel {


    private int id;

    @NotNull
    private String name;
    private String image_url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
