package com.example.ApplicationsManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "application")

public class Application {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;

    public String name;
    public String technologies;
    public String version;

    public List<User> users;

    public Application(){
        this.users = new ArrayList<User>();
    }

    public Application(String name){
        this();
        this.name=name;
    }

    public Application(Integer id, String name){
        this();
        this.name = name;
        this.id=id;
    }

    public Application(String name, String tech, String ver){
        this();
        this.name=name;
        this.technologies= tech;
        this.version= ver;
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

    @ManyToMany(cascade = {CascadeType.ALL, CascadeType.MERGE},fetch=FetchType.EAGER, mappedBy = "apps", targetEntity = User.class)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addUser(User u){
        this.users.add(u);
    }
}
