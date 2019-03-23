package com.example.ApplicationsManager.db.UserDao;

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

public class HbUserDao extends UserDaoInterface {

    private static SessionFactory factory;

    public HbUserDao() {
        factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Set<User> findAll() {
        Session session = factory.openSession();
        Transaction tx = null;
        Set<User> apps = new HashSet<>();

        try {
            tx = session.beginTransaction();
            List<User> userss = session.createQuery("From User").list();
            tx.commit();
            return  userss.stream().collect(Collectors.toSet());
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
            app = session.get(User.class, id);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return app;
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
    public User update(User u) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();
        try {
            tx = session.beginTransaction();
            user = (User)session.load(User.class, u.getId());
            System.out.println(u.toString());
//            user.setId(u.getId());
            user.setName(u.getName());
            user.setUsername(u.getUsername());
            user.setPassword(u.getPassword());
            user.setApps(u.getApps());
            session.update(user);
            tx.commit();
            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

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
    public Set<Application> computeApps(int id) {
        return null;
    }
}
