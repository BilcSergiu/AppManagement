package com.example.ApplicationsManager.db;

import com.example.ApplicationsManager.db.ApplicationDao.AppDao;
import com.example.ApplicationsManager.db.ApplicationDao.AppDaoInterface;
import com.example.ApplicationsManager.db.ApplicationDao.HbAppDao;
import com.example.ApplicationsManager.db.UserDao.HbUserDao;
import com.example.ApplicationsManager.db.UserDao.UserDao;
import com.example.ApplicationsManager.db.UserDao.UserDaoInterface;

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
