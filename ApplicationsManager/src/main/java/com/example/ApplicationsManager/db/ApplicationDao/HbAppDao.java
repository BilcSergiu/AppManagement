package com.example.ApplicationsManager.db.ApplicationDao;

import com.example.ApplicationsManager.db.DbAccess.HibernateUtil;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
            app = session.load(Application.class, application.getId());
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
    public Application addUserToApp(Application t, User user) {
        Session session = factory.openSession();
        Transaction tx = null;
        Application app = new Application();
        User user1 = new User();

        try {
            tx = session.beginTransaction();
            app = session.get(Application.class, t.getId());
            app.setName(t.getName());
            app.setVersion(t.getVersion());
            app.setTechnologies(t.getTechnologies());
            System.out.println(app.toString());

            user1 = session.get(User.class, user.getId());
            System.out.println(user1.toString());
            user1.addApplication(t);
            Set<User> uss = new HashSet<>();
            uss.add(user1);
            app.setUsers(uss);

            session.merge(app);
            session.merge(user1);
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
        return null;
    }
}
