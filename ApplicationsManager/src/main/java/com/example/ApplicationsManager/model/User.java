package com.example.ApplicationsManager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;
    private String username;
    private String password;

    @ManyToMany(targetEntity = Application.class, cascade = {CascadeType.ALL, CascadeType.MERGE},fetch=FetchType.EAGER)
    @JoinTable(name = "application_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "application_id"))
    private List<Application> apps;

    public User(){
        this.apps = new ArrayList<Application>();
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

    public User(Integer id, String name, String username, String pass){
        this();
        this.name = name;
        this.id=id;
        this.username = username;
        this.password = pass;
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

    @ManyToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }  )
    @JoinTable(
            name = "application_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "application_id")}
    )
    public List<Application> getApps() {
        return apps;
    }

    public void setApps(List<Application> apps) {
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
}
