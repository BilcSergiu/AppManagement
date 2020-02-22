package com.example.applicationsmanagement.controller;

import com.example.applicationsmanagement.Service.UserService;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.validator.NameValidator;
import com.example.applicationsmanagement.viewmodel.AppViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;

    // modificat
    @GetMapping(value = "/all")
    public Set<User> getAll() {
       return service.getAll();
     }

    // modificat
    @GetMapping("/byId/{id}")
    public UserViewModel byId(@PathVariable int id) {
        return this.service.byId(id);
    }

    // modificat
    @GetMapping("/userById/{id}")
    public User userById(@PathVariable int id) {
       return this.service.userById(id);
    }

    // modificat
    @PostMapping("/add")
    public User save(@RequestBody User userViewModel, BindingResult bindingResult) throws ValidationException {
       return this.service.save(userViewModel);
    }

    // modificat
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable int id) {
       this.service.delete(id);
    }

    // modificat
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUsingBody(@RequestBody int id) {
        this.service.delete(id);
    }

    @PutMapping(value="deleteUser/{id}")
    public User deleteUsingBody(@PathVariable int id,@RequestBody Application appl) {
       return this.service.deleteAppFromUser(id,appl);
    }

    @PostMapping("/addApp/{userId}")
    public User addApp(@PathVariable int userId, @RequestBody AppViewModel appViewModel, BindingResult bindingResult) throws ValidationException {
        return this.service.addApp(userId,appViewModel);
    }

    @GetMapping("/getApps/{userId}")
    public Set<Application> getApps(@PathVariable int userId) {
       return this.service.getApps(userId);
    }

    @PutMapping("/update")
    public User update( @RequestBody User user, BindingResult bindingResult) throws ValidationException {
       return this.service.update(user);
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
            this.service.importUsers(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
