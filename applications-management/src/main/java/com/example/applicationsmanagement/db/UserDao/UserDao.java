package com.example.applicationsmanagement.db.UserDao;


import com.example.applicationsmanagement.db.ApplicationDao.AppDao;
import com.example.applicationsmanagement.db.DbAccess.ConnectionFactory;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserDao implements UserDaoInterface {

    private String createInsertQuery(User t) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append("user");
        sb.append(" ( name, username, password ");
        sb.append(") values ( ");
        try {
            Object value;

            for (Field field : User.class.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName() != "id") {
                    if (!field.equals(User.class.getDeclaredFields()[User.class.getDeclaredFields().length - 2]) && field.getName() != "apps") {
                        value = field.get(t);
                        sb.append("'" + value + "', ");
                    } else {
                        if (field.getName() != "apps") {
                            value = field.get(t);
                            sb.append("'" + value + "'); ");
                        }
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append("user");
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    private String createSelectUsersByAppId(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  ");
        sb.append(" FROM ");
        sb.append(User.class.getName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    public String insertAppsUsers(Integer id1, Integer id2) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into application_user(application_id, user_id) values(" + id1 + "," + id2 + ");");
        return sb.toString();
    }


    @Override
    public User findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            System.out.println(resultSet.toString());
            return createObjects(resultSet).iterator().next();

        } catch (SQLException e) {
            System.out.println("UserDao:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    @Override
    public User save(User t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery(t);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("UserDao:save " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    public Set<User> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from " + "user" + ";";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            System.out.println("UserDao:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public User insert(User t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery(t);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("UserDao:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    public void delete(int id) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = "delete from " + User.class.getSimpleName() + " where id =" + id + ";";
        //String query = "SET FOREIGN_KEY_CHECKS=0; \ndelete from " + User.class.getSimpleName() + " where id =" + id + ";\nSET FOREIGN_KEY_CHECKS=1;";
        System.out.println(query);
        try {
            con = ConnectionFactory.getConnection();
            stm = con.prepareStatement(query);
            stm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(stm);
            ConnectionFactory.close(con);
        }

    }

    public Set<User> createObjects(ResultSet rs) {

        Set<User> users = new HashSet<User>();

        try {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User addAppToUser(User user, Application app) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = insertAppsUsers(app.getId(), user.getId());
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("UserDao:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return user;
    }

    @Override
    public Set<Application> computeApps(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = getAllAppsQuery(id);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            AppDao d = new AppDao();
            return d.createObjects(resultSet);

        } catch (SQLException e) {
            System.out.println("UserDao:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    @Override
    public User deleteAppFromUser(User t, Application app) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "delete from application_user where user_id="+t.getId()+" and application_id="+app.getId()+";";
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("ApplicationDAO:deleteUser " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        t.getApps().remove(app);
        return t;
    }

    private String getAllAppsQuery(int id) {

        return "select application.* from application, application_user where application.id = application_user.application_id and application_user.user_id=" + id + ";";
    }

    @Override
    public User delete(User user) {
        this.delete(user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = "UPDATE user SET name ='"+ user.getName()+"', username = '"+ user.getUsername()+"', password = '"+user.getPassword()+"' where id =" + user.getId() + ";";

        System.out.println(query);
        try {
            con = ConnectionFactory.getConnection();
            stm = con.prepareStatement(query);
            stm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(stm);
            ConnectionFactory.close(con);
        }

        return user;
    }


}
