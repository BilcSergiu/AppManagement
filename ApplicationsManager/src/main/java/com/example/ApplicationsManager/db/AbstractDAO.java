package com.example.ApplicationsManager.db;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface AbstractDAO<T> {

    public abstract List<T> findAll();

    public abstract T findById(int id);

    public abstract List<T> createObjects(ResultSet resultSet);

    public abstract T save(T t);

    public abstract T delete(T t);

    public abstract T update(T t);



}
