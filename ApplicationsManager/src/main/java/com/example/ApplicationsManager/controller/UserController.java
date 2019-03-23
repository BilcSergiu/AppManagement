package com.example.ApplicationsManager.controller;

import com.example.ApplicationsManager.ModelViewMapper;
import com.example.ApplicationsManager.db.DaoFactory;
import com.example.ApplicationsManager.db.UserDao.UserDaoInterface;
import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;
import com.example.ApplicationsManager.viewmodel.AppViewModel;
import com.example.ApplicationsManager.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    //private AppRepository appRepository;
    //private UserRepository userRepository;
    private ModelViewMapper mapper;
    private DaoFactory factory;
    private UserDaoInterface userDao;

    @Autowired
    public UserController(ModelViewMapper mapper) {
        // this.appRepository=appRepo;
        // this.userRepository = userRepository;
        this.mapper = mapper;
        this.factory = new DaoFactory();
        this.userDao = factory.getUserDao(2);
    }

    // modificat
    @GetMapping(value = "/all")
    public Set<UserViewModel> getAll() {
        Set<User> users = new HashSet<>();

        //userRepository.findAll().forEach(users::add);
        this.userDao.findAll().forEach(users::add);

        return users.stream().map(user -> this.mapper.convertToUserViewModel(user)).collect(Collectors.toSet());
    }

    // modificat
    @GetMapping("/byId/{id}")
    public UserViewModel byId(@PathVariable int id) {
        // User user = this.userRepository.findById(id).get();

        User user = this.userDao.findById(id);

        if (user == null) {
            throw new EntityNotFoundException();
        }

        UserViewModel userView = this.mapper.convertToUserViewModel(user);

        return userView;
    }

    // modificat
    @GetMapping("/userById/{id}")
    public User userById(@PathVariable int id) {
        // User user = this.userRepository.findById(id).get();

        User user = this.userDao.findById(id);

        if (user == null) {
            throw new EntityNotFoundException();
        }

        user.setApps(userDao.computeApps(user.getId()));

        return user;
    }

    // modificat
    @PostMapping("/add")
    public User save(@RequestBody UserViewModel userViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        User user = this.mapper.convertToUser(userViewModel);

        this.userDao.save(user);

        return user;
    }

    // modificat
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable int id) {
        User user = this.userDao.findById(id);
        this.userDao.delete(user);
        //this.userRepository.deleteById(id);
    }

    @PostMapping("/addApp/{userId}")
    public User addApp(@PathVariable int userId, @RequestBody AppViewModel appViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        //User user = this.userRepository.findById(userId).get();
        User user = this.userDao.findById(userId);

        if (user == null) {
            throw new EntityNotFoundException();
        }

        Application app = this.mapper.convertToApp(appViewModel);


        return this.userDao.addApp(user, app);
    }

    /*
        @GetMapping("/byApp/{appId}")
        public Set<UserViewModel> byUser(@PathVariable int appId){
            Set<User> users = new HashSet<>();

            Application app = this.appRepository.findById(appId).get();
            if(app!=null){
                users = this.userRepository.findAllByApps(app);
            }

            Set<UserViewModel> userViews = users.stream().map(user ->this.mapper.convertToUserViewModel(user)).collect(Collectors.toList());

            return userViews;
        }
    */
    @GetMapping("/getApps/{userId}")
    public Set<Application> getUsers(@PathVariable int userId) {
        Set<Application> apps = new HashSet<>();

        apps = this.userDao.computeApps(userId);

        return apps;
    }

    @PutMapping("/update/{userId}")
    public User update(@PathVariable int userId, @RequestBody User user, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        // User user = this.mapper.convertToUser(userViewModel);
        System.out.println(user.toString());
        User user1 = this.userDao.update(user);

        return user1;

    }


}
