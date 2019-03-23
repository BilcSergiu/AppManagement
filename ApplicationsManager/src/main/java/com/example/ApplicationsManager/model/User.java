package com.example.ApplicationsManager.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private int id;

    private String name;
    private String username;
    private String password;


    //@ManyToMany( mappedBy = "users")
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

    public String toString(){
        return "User: "+this.getId()+","+this.getName()+","+this.getUsername()+","+this.getPassword();
    }
}
