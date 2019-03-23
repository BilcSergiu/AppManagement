package com.example.ApplicationsManager.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue
    @Column(name = "application_id")
    private int id;

    private String name;
    private String technologies;
    private String version;

    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name = "application_user",
            joinColumns = @JoinColumn(name = "application_id", referencedColumnName = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<User> users;

    public Application(){
        this.users = new HashSet<>();
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
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

    public String toString(){
        return "Application: "+this.getId()+","+this.getName()+","+this.getTechnologies()+","+this.getVersion();
    }
}
