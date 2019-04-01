package com.example.applicationsmanagement.db.ApplicationDao;

import com.example.applicationsmanagement.db.AbstractDAO;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;

import java.util.Set;

public abstract class AppDaoInterface implements AbstractDAO<Application> {

    public AppDaoInterface() {

    }

    public abstract Application addUserToApp(Application app, User user);

    public abstract Set<User> computeUsers(int id);

    public abstract Application deleteUserFromApp(Application t, User user);
}
