package com.example.ApplicationsManager.db;

import com.example.ApplicationsManager.model.Application;
import org.springframework.data.repository.CrudRepository;

public interface AppRepository extends CrudRepository<Application,Integer> {
}
