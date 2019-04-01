package com.example.applicationsmanagement;

import com.example.applicationsmanagement.model.Application;
import com.example.applicationsmanagement.model.User;
import com.example.applicationsmanagement.viewmodel.AppViewModel;
import com.example.applicationsmanagement.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelViewMapper {

    @Autowired
    public ModelViewMapper(){
        super();
    }

    public AppViewModel convertToAppViewModel(Application app){
        AppViewModel viewModel = new AppViewModel();
        viewModel.setId(app.getId());
        viewModel.setName(app.getName());
        viewModel.setImage_url(app.getImage_url());
        return viewModel;
    }

    public Application convertToApp(AppViewModel viewModel){
        return new Application(viewModel.getId(),viewModel.getName());
    }

    public UserViewModel convertToUserViewModel(User entity) {
        UserViewModel viewModel = new UserViewModel();
        viewModel.setId(entity.getId());
        viewModel.setName(entity.getName());

        return viewModel;
    }

    public User convertToUser(UserViewModel viewModel) {
       return new User(viewModel.getId(),viewModel.getName());
    }
}
