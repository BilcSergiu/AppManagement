package com.example.applicationsmanagement.Service;

import com.example.applicationsmanagement.ModelViewMapper;
import com.example.applicationsmanagement.controller.AppController;
import com.example.applicationsmanagement.db.ApplicationDao.AppDaoInterface;
import com.example.applicationsmanagement.db.DaoFactory;
import com.example.applicationsmanagement.db.UserDao.UserDaoInterface;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppService {

    private static final Logger logger = Logger.getLogger(AppService.class);


    private AppDaoInterface appDao;
    private ModelViewMapper mapper;
    private UserDaoInterface userDao;


    @Autowired
    public AppService(ModelViewMapper mapper) {
        this.mapper = mapper;
        this.appDao = DaoFactory.getAppDao(2);
        this.userDao = DaoFactory.getUserDao(2);
    }


    public AppService(AppDaoInterface appDao, ModelViewMapper mapper, UserDaoInterface userDao) {
        this.appDao = appDao;
        this.mapper = mapper;
        this.userDao = userDao;
    }

    public Set<Application> getAll() throws Exception {
        Set<Application> apps = null;
        //logs debug message
        if (logger.isDebugEnabled()) {
            logger.debug("getAll is executed!");
        }
        try {
            logger.info("All adds method accessed!");
            apps = new HashSet<>();
            appDao.findAll().forEach(apps::add);
            Set<AppViewModel> appViews = apps.stream().map(app -> this.mapper.convertToAppViewModel(app)).collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error while getting allApps", e);
            throw new Exception("Exception getAll");
        }

        return apps;
    }


    public AppViewModel byId(int id) throws Exception {

        Application app = this.appDao.findById(id);
        AppViewModel appView = null;
        if (app == null) {
            logger.error("There is no app with id=" + id, new EntityNotFoundException());
            throw new Exception("Fail byId");
        } else {
            appView = this.mapper.convertToAppViewModel(app);
        }

        logger.info("AppById executed with success");
        return appView;
    }


    public Application getAppById(int id) throws Exception {
        Application app = this.appDao.findById(id);

        if (app == null) {
            logger.error("Couldn't get an app with id= " + id, new EntityNotFoundException());
            throw new Exception("Fail getAPpById");
        }

        app.setUsers(appDao.computeUsers(app.getId()));

        logger.info("AppById executed with success");
        return app;
    }


    public Application save(Application app) throws Exception {
        if (app == null) {
            logger.error("Application received is null");
            throw new Exception("Fail save");
        }
        try {
            this.appDao.save(app);
            logger.info("Added a new app with success");
        } catch (Exception e) {
            throw new Exception("Fail save");
        }
        return app;
    }

    public Application update(Application application) throws Exception {
        if (application == null) {
            logger.error("Application received is null");
            throw new Exception("Fail save");
        }
        try {
            Application app = this.appDao.update(application);
            logger.info("App update executed");
            return app;
        } catch (Exception e) {
            throw new Exception("Fail save");
        }

    }

    public Application deleteById(int id) {
        Application app = this.appDao.findById(id);
        if (app == null) {
            logger.error("Application received is null");
        }

        logger.info("App " + id + " deleted with success!");
        return this.appDao.delete(app);
    }

    public Application deleteUserFromApp(int id, User user) {
        Application app = this.appDao.findById(id);
        User user1 = this.userDao.findById(user.getId());
        if (app == null) {
            logger.error("Application received is null");
        }
        if (user1 == null) {
            logger.error("There is not user with the id " + user.getId());
        }

        Application a = this.appDao.deleteUserFromApp(app, user1);
        logger.info("User " + id + " deleted from " + id + " app");
        return a;
    }


    public Application addUser(int appId, UserViewModel userViewModel) {

        Application app = this.appDao.findById(appId);
        if (app == null) {
            logger.error("Application received is null");
        }
        User user = this.mapper.convertToUser(userViewModel);
        if (user == null) {
            logger.error("There is not user with the id " + user.getId());
        }
        user.getApps().add(app);

        Application a = this.appDao.addUserToApp(app, user);
        logger.info("User " + user.getId() + " added to " + appId + " app");
        return a;

    }

    public Set<User> getUsers(int appId) {
        Set<User> users = new HashSet<>();

        users = this.appDao.computeUsers(appId);

        return users;
    }


    public String exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "apps.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Application> writer = new StatefulBeanToCsvBuilder<Application>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(this.getAll().stream().collect(Collectors.toList()));

        return "Apps exported";
    }


    public String importCSV(MultipartFile file) throws Exception {
        List<String[]> tempUsersList = new ArrayList<>();
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        String[] line;
        while ((line = csvReader.readNext()) != null) {

            if (line.length < 4) {
                throw new IndexOutOfBoundsException("too short");
            } else {
                if (!NameValidator.isValid(line[1]) || !NameValidator.isValid(line[2]) || !NameValidator.isValid(line[2]) || !NameValidator.isValid(line[3])) {
                    Application app = new Application();
                    app.setName(line[0]);
                    app.setTechnologies(line[2]);
                    app.setVersion(line[1]);
                    app.setImage_url(line[3]);
                    System.out.println(line[0] +"  "+line[1]+"  "+ line[2] +"  "+line[3] +"  "+line[4] +"  ");
                    System.out.println(app.toString());
                    save(app);
                }
            }

        }
        reader.close();
        csvReader.close();

        return "Apps imported";
    }


}
