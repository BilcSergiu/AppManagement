package com.example.ApplicationsManager.db.UserDao;

import com.example.ApplicationsManager.db.AbstractDAO;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;

import java.util.List;

public abstract class UserDaoInterface implements AbstractDAO<User> {

    public UserDaoInterface(){};

    public abstract User addApp(User user, Application app);

    public abstract List<Application> computeApps(int id);
}
