package com.example.ApplicationsManager.viewmodel;

import javax.validation.constraints.NotNull;

public class AppViewModel {


    private int id;

    @NotNull
    private String name;


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

}
