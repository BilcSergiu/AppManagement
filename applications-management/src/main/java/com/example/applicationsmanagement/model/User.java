package com.example.applicationsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    //@CsvBindByPosition(position = 0)
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String username;
    @CsvBindByPosition(position = 2)
    private String password;
    @CsvBindByPosition(position = 3)
    private String image_url;

    @CsvBindByPosition(position = 4)
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Application> apps;


    public User(){
        this.apps = new HashSet<Application>();
    }

    public User(String name){
        this();
        this.name = name;
    }

    public User(Integer id, String name){
        this();
        this.name = name;
        this.id=id;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Application> getApps() {
        return apps;
    }

    public void setApps(Set<Application> apps) {
        this.apps = apps;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id= id;
    }

    public void addApplication(Application a){
        this.apps.add(a);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUsername(), getPassword());
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                "; name='" + name + '\'' +
                "; username='" + username + '\'' +
                '}';
    }
}
