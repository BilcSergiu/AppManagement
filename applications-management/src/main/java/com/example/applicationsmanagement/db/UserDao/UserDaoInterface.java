package com.example.applicationsmanagement.db.UserDao;


import com.example.applicationsmanagement.db.AbstractDAO;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;

import java.util.Set;

public interface UserDaoInterface extends AbstractDAO<User> {

    public abstract User addAppToUser(User user, Application app);

    public abstract Set<Application> computeApps(int id);

    public abstract User deleteAppFromUser(User t, Application app);
}
