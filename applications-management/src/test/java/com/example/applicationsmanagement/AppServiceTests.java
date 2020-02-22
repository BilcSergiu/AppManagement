package com.example.applicationsmanagement;

import com.example.applicationsmanagement.Service.AppService;
import com.example.applicationsmanagement.db.ApplicationDao.AppDao;
import com.example.applicationsmanagement.db.ApplicationDao.AppDaoInterface;
import com.example.applicationsmanagement.db.DaoFactory;
import com.example.applicationsmanagement.db.UserDao.UserDaoInterface;
import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.viewmodel.AppViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AppServiceTests {

    @Mock
    AppDaoInterface appDao;

    @Mock
    UserDaoInterface userDao;

    ModelViewMapper mapper = new ModelViewMapper();

    AppService appService;

    Application app;
    AppViewModel appView;

    @Before
    public void setUp() {
        app = new Application(123, "sergiu", "tec1", "1", "");
        appView = new AppViewModel(123, "sergiu", "");
        appService = new AppService(appDao, mapper, userDao);

    }

    @Test
    public void testFindByIdWhenAppExists() {
        when(appDao.findById(123)).thenReturn(app);
        AppViewModel actual = null;
        try {
            actual = this.appService.byId(123);
        } catch (Exception e) {
            Assert.assertEquals("Test",e.getMessage());
        }
        assertEquals(actual, appView);
    }

    @Test
    public void testFindByIdWhenAppDoesntExists() {
        when(appDao.findById(any(Integer.class))).thenReturn(null);

        AppViewModel actual = null;
        try {
            actual = this.appService.byId(123);
        } catch (Exception e) {
            Assert.assertEquals("Fail byId",e.getMessage());
        }
        assertEquals(actual, null);
    }

    @Test
    public void testGetAppById() {
        when(appDao.findById(123)).thenReturn(app);
        Application a = null;
        try {
            a = this.appService.getAppById(123);
        } catch (Exception e) {

        }
        assertEquals(a, app);
    }

    @Test
    public void testSave() {
        when(appDao.save(any(Application.class))).thenReturn(app);
        Application actual = null;
        try {
            actual = appService.save(app);
        } catch (Exception e) {

        }
        Assert.assertEquals(actual, app);
    }

    @Test
    public void testUpdateSuccesful() {
        Application a = new Application("Alex");
        when(appDao.update(any(Application.class))).thenReturn(a);
        Application actual = null;
        try {
            actual = appService.update(a);
        } catch (Exception e) {

        }
        Assert.assertEquals(actual, a);
    }

    @Test
    public void testAddUser() {
        UserViewModel user = new UserViewModel();
        User u = new User();
        Application a1 = new Application();
        Application a2 = new Application();
        a2.addUser(u);
        when(appDao.findById(a1.getId())).thenReturn(a1);
        when(appDao.addUserToApp(a1, u)).thenReturn(a2);
        Application actual = appService.addUser(a1.getId(), user);
        Assert.assertEquals(actual, a2);
    }

    @Test
    public void testDeleteById() {
        when(appDao.findById(app.getId())).thenReturn(app);
        when(appDao.delete(app)).thenReturn(app);
        Application actual = this.appService.deleteById(app.getId());

        Assert.assertEquals(actual, app);
    }

    @Test
    public void testDeleteUserFromApp() {
        User user = new User("Alex");
        app.getUsers().add(user);
        when(appDao.findById(app.getId())).thenReturn(app);
        when(userDao.findById(user.getId())).thenReturn(user);
        Application appp = app;
        appp.getUsers().remove(user);
        when(appDao.deleteUserFromApp(app, user)).thenReturn(appp);

        Assert.assertEquals(appp, this.appService.deleteUserFromApp(app.getId(),user));
    }

    @Test
    public void testGetAll(){

        Application a1 = new Application("a");
        Application a2 = new Application( "b");
        Set<Application> apps = new HashSet<>();
        apps.add(a1);
        apps.add(a2);

        when(this.appDao.findAll()).thenReturn(apps);

        Set<Application> actualApps = null;
        try {
            actualApps = this.appService.getAll();
        } catch (Exception e) {

        }
        Assert.assertEquals(apps,actualApps);
    }

    @Test
    public void testComputeUsers(){
        Set<User> users = new HashSet<>();
        this.app.setUsers(users);
        when(this.appDao.computeUsers(app.getId())).thenReturn(users);

        Assert.assertEquals(users,this.appService.getUsers(app.getId()));
    }


}
