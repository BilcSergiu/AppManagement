package com.example.applicationsmanagement.viewmodel;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AppViewModel {


    private int id;

    @NotNull
    private String name;
    private String image_url;

    public AppViewModel() {
    }

    public AppViewModel(int id, @NotNull String name, String image_url) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
    }

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

    @Override
    public String toString() {
        return "AppViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppViewModel that = (AppViewModel) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getImage_url(), that.getImage_url());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getImage_url());
    }
}
