package com.example.applicationsmanagement.db;


import java.util.Set;

public interface AbstractDAO<T> {

    Set<T> findAll();

    T findById(int id);

    T save(T t);

    T delete(T t);

    T update(T t);

}
