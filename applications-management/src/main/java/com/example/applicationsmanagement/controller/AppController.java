package com.example.applicationsmanagement.controller;

import com.example.applicationsmanagement.ModelViewMapper;
import com.example.applicationsmanagement.db.ApplicationDao.AppDaoInterface;
import com.example.applicationsmanagement.db.DaoFactory;
import com.example.applicationsmanagement.db.UserDao.UserDao;
import com.example.applicationsmanagement.db.UserDao.UserDaoInterface;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.viewmodel.AppViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
@CrossOrigin
public class AppController {

    // private AppRepository appRepository;
    // private UserRepository userRepository;
    private AppDaoInterface appDao;
    private ModelViewMapper mapper;
    private UserDaoInterface userDao;
    private DaoFactory factory;

    @Autowired
    public AppController(ModelViewMapper mapper) {
        this.mapper = mapper;
        this.factory = new DaoFactory();
        this.appDao = factory.getAppDao(2);
        this.userDao = factory.getUserDao(2);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Set<Application> getAll() {
        Set<Application> apps = new HashSet<>();

        appDao.findAll().forEach(apps::add);

        Set<AppViewModel> appViews = apps.stream().map(app -> this.mapper.convertToAppViewModel(app)).collect(Collectors.toSet());

        return apps;
    }

    // modificat
    @GetMapping("/byId/{id}")
    public AppViewModel byId(@PathVariable int id) {

        Application app = this.appDao.findById(id);

        if (app == null) {
            throw new EntityNotFoundException();
        }

        AppViewModel appView = this.mapper.convertToAppViewModel(app);

        return appView;
    }

    // modificat
    @GetMapping("/appById/{id}")
    public Application getAppById(@PathVariable int id) {
        //Application app = this.appRepository.findById(id).get();
        Application app = this.appDao.findById(id);

        if (app == null) {
            throw new EntityNotFoundException();
        }

        // AppViewModel appView = this.mapper.convertToAppViewModel(app);

        app.setUsers(appDao.computeUsers(app.getId()));
        //System.out.println(app.getUsers().get(0).getName());
        return app;
    }

    // modificat
    @PostMapping("/add")
    public Application save(@RequestBody Application appViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

       // Application app = this.mapper.convertToApp(appViewModel);
        //this.appRepository.save(app);
        this.appDao.save(appViewModel);
        return appViewModel;
    }

    // nou adaugat
    @PutMapping("/update")
    public Application update( @RequestBody Application application, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }


        Application app = this.appDao.update(application);

        return app;

    }


    // modificat
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable int id) {
        Application app = this.appDao.findById(id);
        this.appDao.delete(app);
    }

    // modificat
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUsingBody(@RequestBody int id) {
        Application app = this.appDao.findById(id);
        this.appDao.delete(app);
    }

    @PutMapping(value="deleteUser/{id}")
    public Application deleteUsingBody(@PathVariable int id,@RequestBody User user) {
        Application app = this.appDao.findById(id);
        User user1 = this.userDao.findById(user.getId());

        return this.appDao.deleteUserFromApp(app,user1);
    }

    @PostMapping("/addUser/{appId}")
    public Application addUser(@PathVariable int appId, @RequestBody UserViewModel userViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        Application app = this.appDao.findById(appId);

        if (app == null) {
            throw new EntityNotFoundException();
        }

        User user = this.mapper.convertToUser(userViewModel);
        user.getApps().add(app);

        return this.appDao.addUserToApp(app, user);

    }

    /*

    @GetMapping("/byUser/{userId}")
    public List<AppViewModel> byUser(@PathVariable int userId){
        List<Application> apps = new ArrayList<>();

        User user = this.userRepository.findById(userId).get();

        //User user = this.userDao
        if(user!=null){
            apps = this.appRepository.findAllByUsers(user);
        }

        List<AppViewModel> appView = apps.stream().map(app ->this.mapper.convertToAppViewModel(app)).collect(Collectors.toList());

        return appView;
    }*/

    @GetMapping("/getUsers/{appId}")
    public Set<User> getUsers(@PathVariable int appId) {
        Set<User> users = new HashSet<>();

        users = this.appDao.computeUsers(appId);

        return users;
    }


}
