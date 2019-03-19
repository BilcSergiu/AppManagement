package com.example.ApplicationsManager.db.ApplicationDao;

import com.example.ApplicationsManager.db.AbstractDAO;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;

import java.util.List;

public abstract class AppDaoInterface implements AbstractDAO<Application> {

    public AppDaoInterface(){

    }

    public abstract Application addUser(Application app, User user);

    public abstract List<User> computeUsers(int id);
}
