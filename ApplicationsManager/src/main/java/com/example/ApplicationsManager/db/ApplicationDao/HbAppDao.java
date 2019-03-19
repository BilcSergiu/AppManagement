package com.example.ApplicationsManager.db.ApplicationDao;

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

public class HbAppDao extends AppDaoInterface {

    private static SessionFactory factory;

    public HbAppDao() {
        factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<Application> findAll() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Application> apps = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            apps = session.createQuery("From Application").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }


        return apps;
    }

    @Override
    public Application findById(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();

        try {
            tx = session.beginTransaction();
            app =  (Application) session.get(Application.class,id);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return app;
    }

    @Override
    public List<Application> createObjects(ResultSet resultSet) {
        return null;
    }

    @Override
    public Application save(Application application) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();

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
    public Application delete(Application application) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();

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
    public Application update(Application application) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();
        try {
            tx = session.beginTransaction();
           app = session.load(Application.class,application.getId());
            app.setId(application.getId());
            app.setName(application.getName());
            app.setTechnologies(application.getTechnologies());
            app.setVersion(application.getVersion());
            app.setUsers(application.getUsers());
            session.update(app);
            tx.commit();
            return app;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }

    @Override
    public Application addUser(Application t, User user) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Application app = session.load(Application.class,t.getId());
            app.addUser(user);
            session.update(app);
            User user1 = session.load(User.class,user.getId());
            user1.addApplication(t);
            session.merge(user1);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return t;
    }

    @Override
    public List<User> computeUsers(int id) {
        return null;
    }
}
