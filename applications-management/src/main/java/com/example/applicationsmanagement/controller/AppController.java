package com.example.applicationsmanagement.controller;


import com.example.applicationsmanagement.Service.AppService;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.viewmodel.AppViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/apps")
@CrossOrigin
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppService service;


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Set<Application> getAll() {
        try {
            return service.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // modificat
    @GetMapping("/byId/{id}")
    public AppViewModel byId(@PathVariable int id) {
        try {
            return this.service.byId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // modificat
    @GetMapping("/appById/{id}")
    public Application getAppById(@PathVariable int id) {
        try {
            return this.service.getAppById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // modificat
    @PostMapping("/add")
    public Application save(@RequestBody Application app, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error("Application save: Error at binding result", new EntityNotFoundException());
        }

        try {
            return this.service.save(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // nou adaugat
    @PutMapping("/update")
    public Application update(@RequestBody Application application, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Application save: ValidationError", new ValidationException("Update app"));
        }
        try {
            return this.service.update(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // modificat
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable int id) {
        this.service.deleteById(id);
    }

    // modificat
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUsingBody(@RequestBody int id) {
        this.service.deleteById(id);
    }

    @PutMapping(value = "deleteUser/{id}")
    public Application deleteUser(@PathVariable int id, @RequestBody User user) {
        return this.service.deleteUserFromApp(id, user);
    }

    @PostMapping("/addUser/{appId}")
    public Application addUser(@PathVariable int appId, @RequestBody UserViewModel userViewModel) {
        return this.service.addUser(appId, userViewModel);

    }

    @GetMapping("/getUsers/{appId}")
    public Set<User> getUsers(@PathVariable int appId) {
        return this.service.getUsers(appId);
    }


    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) {

        try {
            this.service.exportCSV(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/import")
    public void importUsers(MultipartFile file)  {
        try {
            this.service.importCSV(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
