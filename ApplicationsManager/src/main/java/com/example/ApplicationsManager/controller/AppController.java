package com.example.ApplicationsManager.controller;

import com.example.ApplicationsManager.ModelViewMapper;
import com.example.ApplicationsManager.db.ApplicationDao.AppDaoInterface;
import com.example.ApplicationsManager.db.DaoFactory;
import com.example.ApplicationsManager.db.UserDao.UserDao;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;
import com.example.ApplicationsManager.viewmodel.AppViewModel;
import com.example.ApplicationsManager.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
@CrossOrigin
public class AppController {

   // private AppRepository appRepository;
   // private UserRepository userRepository;
    private AppDaoInterface appDao;
    private ModelViewMapper mapper;
    private UserDao userDao;
    private DaoFactory factory;

    @Autowired
    public AppController(ModelViewMapper mapper){
        this.mapper = mapper;
        this.factory=new DaoFactory();
        this.appDao = factory.getAppDao(1);
        this.userDao = new UserDao();
    }

    // Modificat pentru jdbc normal querry
    @RequestMapping(value ="/all", method = RequestMethod.POST)
    public List<AppViewModel> getAll(){
        List<Application> apps = new ArrayList<>();

        appDao.findAll().forEach(apps::add);

        List<AppViewModel> appViews = apps.stream().map(app->this.mapper.convertToAppViewModel(app)).collect(Collectors.toList());

        return appViews;
    }

    // modificat
    @GetMapping("/byId/{id}")
    public AppViewModel byId(@PathVariable int id){

        Application app = this.appDao.findById(id);

        if(app == null){
            throw new EntityNotFoundException();
        }

        AppViewModel appView = this.mapper.convertToAppViewModel(app);

        return appView;
    }

    // modificat
    @GetMapping("/appById/{id}")
    public Application getAppById(@PathVariable int id){
        //Application app = this.appRepository.findById(id).get();
        Application app = this.appDao.findById(id);

        if(app == null){
            throw new EntityNotFoundException();
        }

       // AppViewModel appView = this.mapper.convertToAppViewModel(app);

        app.setUsers(appDao.computeUsers(app.getId()));
        //System.out.println(app.getUsers().get(0).getName());
        return app;
    }

    // modificat
    @PostMapping("/add")
    public Application save(@RequestBody AppViewModel appViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        Application app = this.mapper.convertToApp(appViewModel);
        //this.appRepository.save(app);
        this.appDao.save(app);
        return app;
    }

    // modificat
    @RequestMapping(value ="/delete/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable int id){
        Application app = this.appDao.findById(id);
        this.appDao.delete(app);
    }

    // modificat
    @RequestMapping(value ="/delete", method = RequestMethod.DELETE)
    public void deleteUsingBody(@RequestBody int id){
        Application app = this.appDao.findById(id);
        this.appDao.delete(app);
    }

    @PostMapping("/addUser/{appId}")
    public Application byId(@PathVariable int appId, @RequestBody UserViewModel userViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        Application app = this.appDao.findById(appId);

        if(app == null){
            throw new EntityNotFoundException();
        }

        User user = this.mapper.convertToUser(userViewModel);
        user.getApps().add(app);

        return this.appDao.addUser(app,user);

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
    public List<User> getUsers(@PathVariable int appId){
        List<User> users = new ArrayList<>();

        users = this.appDao.computeUsers(appId);

        return users;
    }


    @PostMapping("/update/{appId}")
    public Application update(@PathVariable int appId, @RequestBody Application userViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

       // User user = this.mapper.convertToUser(userViewModel);
       Application app = this.appDao.update(userViewModel);

        return app;

    }


}
