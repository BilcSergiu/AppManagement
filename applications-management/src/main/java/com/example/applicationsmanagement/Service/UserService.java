package com.example.applicationsmanagement.Service;

import com.example.applicationsmanagement.ModelViewMapper;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private ModelViewMapper mapper;
    private UserDaoInterface userDao;
    private AppDaoInterface appDao;
    private Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    public UserService(ModelViewMapper mapper) {
        this.mapper = mapper;
        this.userDao = DaoFactory.getUserDao(2);
        this.appDao = DaoFactory.getAppDao(2);
    }

    public UserService(ModelViewMapper mapper, UserDaoInterface userDao, AppDaoInterface appDao) {
        this.mapper = mapper;
        this.userDao = userDao;
        this.appDao = appDao;
    }

    public Set<User> getAll() {
        Set<User> users = new HashSet<>();
        logger.info("getAllUsers executed with success");
        this.userDao.findAll().forEach(users::add);

        return users;
    }

    public UserViewModel byId( int id) throws EntityNotFoundException{
        User user = this.userDao.findById(id);

        if (user == null) {
            logger.error("No user found with the ID="+id);
            throw new EntityNotFoundException();
        }

        UserViewModel userView = this.mapper.convertToUserViewModel(user);
        logger.info("getAllUsers executed with success");
        return userView;
    }

    public User userById( int id) {
       User user = this.userDao.findById(id);

        if (user == null) {
            logger.error("No user found with the ID="+id);
            throw new EntityNotFoundException();
        }

        user.setApps(userDao.computeApps(user.getId()));

        return user;
    }

    public User save(User userViewModel) {
        this.userDao.save(userViewModel);

        return userViewModel;
    }


    public User delete(int id) {
        User user = this.userDao.findById(id);
        if (user == null) {
            logger.error("No user found with the ID="+id);
            throw new EntityNotFoundException();
        }
        return this.userDao.delete(user);
    }


    public User deleteAppFromUser(int id, Application appl) {
        User u = this.userDao.findById(id);
        if (u == null) {
            logger.error("No user found with the ID="+id);
            throw new EntityNotFoundException();
        }
        Application app = this.appDao.findById(appl.getId());

        if (app == null) {
            logger.error("No app found with the ID="+appl.getId());
            throw new EntityNotFoundException();
        }

        User us =  this.userDao.deleteAppFromUser(u, app);
        logger.info("Deleted user from app");
        return us;
    }

    public User addApp(int userId, AppViewModel appViewModel) {
        User user = this.userDao.findById(userId);

        if (user == null) {
            logger.error("No user with id="+userId);
            throw new EntityNotFoundException();
        }

        Application app = this.mapper.convertToApp(appViewModel);
        if (app == null) {
            logger.error("No app with id="+appViewModel.getId());
            throw new EntityNotFoundException();
        }
        app.getUsers().add(user);

        User u  = this.userDao.addAppToUser(user, app);
        logger.info("App added to user!");
        return u;
    }

    public Set<Application> getApps(@PathVariable int userId) {
        Set<Application> apps = new HashSet<>();

        apps = this.userDao.computeApps(userId);
        if(apps == null){
            logger.error("This user doesnt exist");
            new EntityNotFoundException("getApps");
        }
        logger.info("GetApps executed");
        return apps;
    }

    public User update(User user) {
        User user1 = this.userDao.update(user);
        if(user == null){
            logger.error("This user doesnt exist");
            new EntityNotFoundException("update user");
        }
        logger.info("update User executed");
        return user1;

    }

    public String exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "users.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(this.getAll().stream().collect(Collectors.toList()));

        return "Users exported";
    }


    public String importUsers(MultipartFile file) throws IOException {
        List<String[]> tempUsersList = new ArrayList<>();
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        String[] line;
        while ((line = csvReader.readNext()) != null) {

            if (line.length < 4) {
                throw new IndexOutOfBoundsException("too short");
            } else {
                if (!NameValidator.isValid(line[1]) || !NameValidator.isValid(line[2]) || !NameValidator.isValid(line[2]) || !NameValidator.isValid(line[3])) {
                    User user = new User();
                    user.setName(line[0]);
                    user.setPassword(line[2]);
                    user.setUsername(line[1]);
                    user.setImage_url(line[3]);
                    System.out.println(line[0] +"  "+line[1]+"  "+ line[2] +"  "+line[3] +"  "+line[4] +"  ");
                    System.out.println(user.toString());
                    save(user);
                }
            }

        }
        reader.close();
        csvReader.close();


        return "Users imported";
    }


}
