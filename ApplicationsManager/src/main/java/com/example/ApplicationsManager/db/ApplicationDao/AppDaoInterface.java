package com.example.ApplicationsManager.db.ApplicationDao;

import com.example.ApplicationsManager.db.AbstractDAO;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;

import java.util.Set;

public abstract class AppDaoInterface implements AbstractDAO<Application> {

    public AppDaoInterface() {

    }

    public abstract Application addUserToApp(Application app, User user);

    public abstract Set<User> computeUsers(int id);
}
