package com.example.applicationsmanagement;

import com.example.applicationsmanagement.Service.AppService;
import com.example.applicationsmanagement.Service.UserService;
import com.example.applicationsmanagement.db.ApplicationDao.AppDaoInterface;
import com.example.applicationsmanagement.db.UserDao.UserDaoInterface;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.viewmodel.AppViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTests {


    @Mock
    AppDaoInterface appDao;

    @Mock
    UserDaoInterface userDao;

    ModelViewMapper mapper = new ModelViewMapper();

    UserService userService;

    User user;
    UserViewModel userView;

    @Before
    public void setUp() {
        user = new User("sergiu");
        userView = new UserViewModel(1,"sergiu");
        userService = new UserService(mapper, userDao, appDao);

    }

    @Test
    public void testFindByIdWhenUserExists() {
        when(userDao.findById(1)).thenReturn(user);
        UserViewModel actual = this.userService.byId(1);
        assertEquals(actual, userView);
    }

    @Test
    public void testFindByIdWhenUserDoesntExists() {
        when(userDao.findById(any(Integer.class))).thenReturn(null);
        UserViewModel actual = null;
        try {
            actual = this.userService.byId(1);
        }catch(Exception e){

        }
        assertEquals(actual, null);
    }

    @Test
    public void testGetUserById() {
        when(userDao.findById(1)).thenReturn(user);
        User a = this.userService.userById(1);
        assertEquals(a, user);
    }

    @Test
    public void testSaveUser() {
        when(userDao.save(any(User.class))).thenReturn(user);
        User actual = userService.save(user);
        Assert.assertEquals(actual, user);
    }

    @Test
    public void testUpdateSuccesful() {
        User a = new User("Alex");
        when(userDao.update(any(User.class))).thenReturn(a);
        User actual = userService.update(a);
        Assert.assertEquals(actual, a);
    }

    @Test
    public void testAddApp() {
        AppViewModel app = new AppViewModel();
        Application a = new Application();
        User u1 = new User();
        User u2 = new User();
        u2.addApplication(a);
        when(userDao.findById(u1.getId())).thenReturn(u1);
        when(userDao.addAppToUser(u1, a)).thenReturn(u2);
        User actual = userService.addApp(u1.getId(), app);
        Assert.assertEquals(actual, u2);
    }

    @Test
    public void testDeleteById() {
        when(userDao.findById(user.getId())).thenReturn(user);
        when(userDao.delete(user)).thenReturn(user);
        User actual = this.userService.delete(user.getId());

        Assert.assertEquals(actual, user);
    }

    @Test
    public void testDeleteAppFromUser() {
        Application app = new Application("App");
        user.getApps().add(app);
        when(userDao.findById(user.getId())).thenReturn(user);
        when(appDao.findById(app.getId())).thenReturn(app);
        User userss = user;
        userss.getApps().remove(app);
        when(userDao.deleteAppFromUser(user, app)).thenReturn(userss);

        Assert.assertEquals(userss, this.userService.deleteAppFromUser(user.getId(),app));
    }

    @Test
    public void testGetAll(){

        User a1 = new User("a");
        User a2 = new User( "b");
        Set<User> users = new HashSet<>();
        users.add(a1);
        users.add(a2);

        when(this.userDao.findAll()).thenReturn(users);

        Set<User> actualApps = this.userService.getAll();
        Assert.assertEquals(users,actualApps);
    }

    @Test
    public void testComputeUsers(){
        Set<Application> apps = new HashSet<>();
        this.user.setApps(apps);
        when(this.userDao.computeApps(user.getId())).thenReturn(apps);

        Assert.assertEquals(apps,this.userService.getApps(user.getId()));
    }


}
