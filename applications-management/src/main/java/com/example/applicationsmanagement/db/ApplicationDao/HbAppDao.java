package com.example.applicationsmanagement.db.ApplicationDao;

import com.example.applicationsmanagement.db.DbAccess.HibernateUtil;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HbAppDao extends AppDaoInterface {

    private static SessionFactory factory;

    public HbAppDao() {
        factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Set<Application> findAll() {
        Session session = factory.openSession();
        Transaction tx = null;
        Set<Application> apps = new HashSet<>();

        try {
            tx = session.beginTransaction();
            List<Application> appss = session.createQuery("From Application").list();
            tx.commit();
            return  appss.stream().collect(Collectors.toSet());
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
            app = session.get(Application.class, id);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return app;
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
            app = session.load(Application.class,application.getId());
            app.getUsers().removeAll(app.getUsers());
            session.saveOrUpdate(app);
            session.delete(app);
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
            app = session.load(Application.class, application.getId());
            app.setId(application.getId());
            app.setName(application.getName());
            app.setTechnologies(application.getTechnologies());
            app.setVersion(application.getVersion());

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
    public Application addUserToApp(Application t, User user) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();
        User user1 = new User();

        try {
            tx = session.beginTransaction();
            app = session.get(Application.class, t.getId());
            user1 = session.get(User.class, user.getId());
            user1.addApplication(app);
            app.addUser(user1);

            session.saveOrUpdate(app);
            session.saveOrUpdate(user1);
            tx.commit();

            return app;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return t;
    }

    @Override
    public Set<User> computeUsers(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();
        Set<User> users = new HashSet<>();
        try {
            tx = session.beginTransaction();

            app = session.load(Application.class,id);
            users = app.getUsers();
            tx.commit();

            return users;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }

    @Override
    public Application deleteUserFromApp(Application t, User user) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();
        User userToDelete = new User();
        try {
            tx = session.beginTransaction();

            app = session.load(Application.class,t.getId());
            userToDelete = session.load(User.class,user.getId());
            app.getUsers().remove(userToDelete);
            userToDelete.getApps().remove(app);
            session.saveOrUpdate(app);
            session.saveOrUpdate(userToDelete);
            tx.commit();

            return app;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }
}
