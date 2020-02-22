package com.example.applicationsmanagement.db.UserDao;

import com.example.applicationsmanagement.db.DbAccess.HibernateUtil;
import com.example.applicationsmanagement.db.UserDao.UserDaoInterface;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.applet.Applet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HbUserDao implements UserDaoInterface {

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
            List<User> appss = session.createQuery("From User").list();
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
    public User findById(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();

        try {
            tx = session.beginTransaction();
            user = session.get(User.class, id);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return user;
    }

    @Override
    public User save(User us) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();

        try {
            tx = session.beginTransaction();
            session.save(us);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return user;
    }

    @Override
    public User delete(User us) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();

        try {
            tx = session.beginTransaction();
            user = session.load(User.class,us.getId());
            user.getApps().removeAll(user.getApps());
            session.saveOrUpdate(user);
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return user;
    }

    @Override
    public User update(User us) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();
        try {
            tx = session.beginTransaction();
            user = session.load(User.class, us.getId());
            user.setId(us.getId());
            user.setName(us.getName());
            user.setUsername(us.getUsername());
            user.setPassword(us.getPassword());
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
    public User addAppToUser(User t, Application app) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();
        Application app1 = new Application();

        try {
            tx = session.beginTransaction();
            user = session.get(User.class, t.getId());
            app1 = session.get(Application.class, app.getId());
            app1.addUser(user);
            user.addApplication(app1);

            session.saveOrUpdate(user);
            session.saveOrUpdate(app1);
            tx.commit();

            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return t;
    }

    @Override
    public Set<Application> computeApps(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();
        Set<Application> apps = new HashSet<>();
        try {
            tx = session.beginTransaction();
            user = session.get(User.class,id);
            apps = user.getApps();
            tx.commit();

            return apps;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }

    @Override
    public User deleteAppFromUser(User t, Application a) {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = new User();
        Application app = new Application();
        try {
            tx = session.beginTransaction();

            user = session.load(User.class,t.getId());
            app = session.load(Application.class,a.getId());
            user.getApps().remove(app);
            app.getUsers().remove(user);
            session.saveOrUpdate(user);
            session.saveOrUpdate(app);
            tx.commit();

            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }
}
