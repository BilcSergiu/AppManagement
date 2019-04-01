package com.example.applicationsmanagement.controller;

import com.example.applicationsmanagement.ModelViewMapper;
import com.example.applicationsmanagement.db.AbstractDAO;
import com.example.applicationsmanagement.db.ApplicationDao.AppDaoInterface;
import com.example.applicationsmanagement.db.DaoFactory;
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
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private ModelViewMapper mapper;
    private DaoFactory factory;
    private UserDaoInterface userDao;
    private AppDaoInterface appDao;

    @Autowired
    public UserController(ModelViewMapper mapper) {

        this.mapper = mapper;
        this.factory = new DaoFactory();
        this.userDao = factory.getUserDao(2);
        this.appDao = factory.getAppDao(2);
    }

    // modificat
    @GetMapping(value = "/all")
    public Set<User> getAll() {
        Set<User> users = new HashSet<>();

        this.userDao.findAll().forEach(users::add);

        return users;// users.stream().map(user -> this.mapper.convertToUserViewModel(user)).collect(Collectors.toSet());
    }

    // modificat
    @GetMapping("/byId/{id}")
    public UserViewModel byId(@PathVariable int id) {
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
    public User save(@RequestBody User userViewModel, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

       // User user = this.mapper.convertToUser(userViewModel);

        this.userDao.save(userViewModel);

        return userViewModel;
    }

    // modificat
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable int id) {
        User user = this.userDao.findById(id);
        this.userDao.delete(user);
        //this.userRepository.deleteById(id);
    }

    // modificat
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUsingBody(@RequestBody int id) {
        User app = this.userDao.findById(id);
        this.userDao.delete(app);
    }

    @PutMapping(value="deleteUser/{id}")
    public User deleteUsingBody(@PathVariable int id,@RequestBody Application appl) {
        User u = this.userDao.findById(id);
        Application app = this.appDao.findById(appl.getId());

        return this.userDao.deleteAppFromUser(u,app);
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

        app.getUsers().add(user);

        return this.userDao.addAppToUser(user, app);
    }


    @GetMapping("/getApps/{userId}")
    public Set<Application> getApps(@PathVariable int userId) {
        Set<Application> apps = new HashSet<>();

        apps = this.userDao.computeApps(userId);

        return apps;
    }

    @PutMapping("/update")
    public User update( @RequestBody User user, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Eroare la legare");
        }

        System.out.println(user.toString());
        User user1 = this.userDao.update(user);

        return user1;

    }


}
