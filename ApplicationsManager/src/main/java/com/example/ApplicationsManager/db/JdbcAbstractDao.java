package com.example.ApplicationsManager.db;

import com.example.ApplicationsManager.db.DbAccess.ConnectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JdbcAbstractDao<T> implements AbstractDAO<T> {


    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public JdbcAbstractDao() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    private String createSelectUsersByAppId(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    public String insertAppsUsers(Integer id1,Integer id2) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into application_user(application_id, user_id) values("+id1+","+id2+");");
        return sb.toString();
    }


    private String createInsertQuery(T t) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" (");
        for (int i = 0; i < type.getDeclaredFields().length - 1; i++) {
            if (!type.getDeclaredFields()[i].getName().equals("id")) {
                sb.append(type.getDeclaredFields()[i].getName() + ", ");
            }
        }
        sb.append(type.getDeclaredFields()[type.getDeclaredFields().length - 1].getName());
        sb.append(") values ( ");
        try {
            Object value;

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName() != "id") {
                    if (!field.equals(type.getDeclaredFields()[type.getDeclaredFields().length - 1])) {
                        value = field.get(t);
                        sb.append("'" + value + "', ");
                    } else {
                        value = field.get(t);
                        sb.append("'" + value + "'); ");
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

    public Set<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from " + type.getSimpleName() + ";";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private String getIdFromNameQuery(String name) {
        return "select id from " + type.getSimpleName() + " where name = '" + name + "';";
    }

    public T findById(int id) {
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
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public abstract Set<T> createObjects(ResultSet resultSet);


    public T save(T t) {
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
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }



    public int getIdFromName(String name) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = this.getIdFromNameQuery(name);

        try {
            con = ConnectionFactory.getConnection();
            stm = con.prepareStatement(query);
            rs = stm.executeQuery();
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(stm);
            ConnectionFactory.close(con);
        }
        return 0;
    }


}
