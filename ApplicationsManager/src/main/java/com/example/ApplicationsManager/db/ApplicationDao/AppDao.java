package com.example.ApplicationsManager.db.ApplicationDao;

import com.example.ApplicationsManager.db.DbAccess.ConnectionFactory;
import com.example.ApplicationsManager.db.UserDao.UserDao;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AppDao extends AppDaoInterface {

    private String createInsertQuery(Application t) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append("application");
        sb.append(" ( name, technologies, version ) values ( ");
        try {
            Object value;

            for (Field field : Application.class.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName() != "id") {
                    if (!field.equals(Application.class.getDeclaredFields()[Application.class.getDeclaredFields().length - 2]) && field.getName() != "users") {
                        value = field.get(t);
                        sb.append("'" + value + "', ");
                    } else {
                        if (field.getName() != "users") {
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
        sb.append(this.getClass().getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    private String createSelectUsersByAppId(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  ");
        sb.append(" FROM ");
        sb.append(this.getClass().getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    public String insertAppsUsers(Integer id1, Integer id2) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into application_user(application_id, user_id) values(" + id1 + "," + id2 + ");");
        return sb.toString();
    }


    @Override
    public Application findById(int id) {
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
            System.out.println("ApplicationDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    @Override
    public Application save(Application t) {
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
            System.out.println("ApplicationDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    public Set<Application> createObjects(ResultSet rs) {

        Set<Application> apps = new HashSet<>();

        try {
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("application_id"));
                app.setName(rs.getString("name"));
                app.setTechnologies(rs.getString("technologies"));
                app.setVersion(rs.getString("version"));
                apps.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apps;
    }

    @Override
    public Set<Application> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from " + "application" + ";";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            System.out.println("ApplicationDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


    public Application insert(Application t) {
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
            System.out.println("ApplicationDAO:findById " + e.getMessage());
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
        String query = "delete from " + Application.class.getSimpleName() + " where id =" + id + ";";
        //String query = "SET FOREIGN_KEY_CHECKS=0; \ndelete from " + Application.class.getSimpleName() + " where id =" + id + ";\nSET FOREIGN_KEY_CHECKS=1;";
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

    @Override
    public Application addUserToApp(Application t, User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = insertAppsUsers(t.getId(), user.getId());
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("ApplicationDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    @Override
    public Set<User> computeUsers(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = getAllUsersQuery(id);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            UserDao d = new UserDao();
            return d.createObjects(resultSet);

        } catch (SQLException e) {
            System.out.println("ApplicationDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    private String getAllUsersQuery(int id) {
        return "select user.* from user, application_user where user.id = application_user.user_id and application_user.application_id=" + id + ";";
    }


    @Override
    public Application delete(Application application) {
        this.delete(application.getId());
        return application;
    }

    @Override
    public Application update(Application application) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = "UPDATE application SET name ='"+ application.getName()+"', technologies = '"+ application.getTechnologies()+"', version = '"+application.getVersion()+"' where id =" + application.getId() + ";";

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

        return application;
    }
}

