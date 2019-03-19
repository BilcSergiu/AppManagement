package com.example.ApplicationsManager.db.UserDao;

import com.example.ApplicationsManager.db.DbAccess.HibernateUtil;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HbUserDao extends UserDaoInterface {

    private static SessionFactory factory;

    public HbUserDao() {
        factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<User> findAll() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<User> apps = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            apps = session.createQuery("From User").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return apps;
    }

    @Override
    public User findById(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        User app = new User();

        try {
            tx = session.beginTransaction();
            app =  (User) session.get(User.class,id);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return app;
    }

    @Override
    public List<User> createObjects(ResultSet resultSet) {
        return null;
    }

    @Override
    public User save(User application) {
        Session session = factory.openSession();
        Transaction tx = null;
        User app = new User();

        try {
            tx = session.beginTransaction();
            session.save(application);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return app;
    }

    @Override
    public User delete(User application) {
        Session session = factory.openSession();
        Transaction tx = null;
        User app = new User();

        try {
            tx = session.beginTransaction();
            session.delete(application);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return app;
    }

    @Override
    public User update(User application) {
        return null;
    }

    @Override
    public User addApp(User user, Application app) {
        Session session = factory.openSession();
        Transaction tx = null;
        User us = new User();
        try {
            tx = session.beginTransaction();
            us = session.load(User.class, user.getId());
            us.addApplication(app);
            session.merge(us);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return us;
    }

    @Override
    public List<Application> computeApps(int id) {
        return null;
    }
}
