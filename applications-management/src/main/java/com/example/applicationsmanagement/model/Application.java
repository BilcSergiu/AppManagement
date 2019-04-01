package com.example.applicationsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "APPLICATION")
public class Application {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String name;
    private String technologies;
    private String version;
    private String image_url;

    @JsonIgnore
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

    public Application(String name, String tech, String ver, String url){
        this();
        this.name=name;
        this.technologies= tech;
        this.version= ver;
        this.image_url= url;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getTechnologies(), that.getTechnologies()) &&
                Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTechnologies(), getVersion());
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
