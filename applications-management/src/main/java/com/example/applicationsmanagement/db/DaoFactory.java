package com.example.applicationsmanagement.db;

import com.example.applicationsmanagement.db.ApplicationDao.AppDao;
import com.example.applicationsmanagement.db.ApplicationDao.AppDaoInterface;
import com.example.applicationsmanagement.db.ApplicationDao.HbAppDao;
import com.example.applicationsmanagement.db.UserDao.HbUserDao;
import com.example.applicationsmanagement.db.UserDao.UserDao;
import com.example.applicationsmanagement.db.UserDao.UserDaoInterface;

public class DaoFactory {

    public AppDaoInterface getAppDao(int type){
        if(type == 1){
            return new AppDao();
        }else{
            return new HbAppDao();
        }
    }

    public UserDaoInterface getUserDao(int type){
        if(type == 1){
            return new UserDao();
        }else{
            return new HbUserDao();
        }
    }
}
