package com.example.ApplicationsManager;

import com.example.ApplicationsManager.model.Application;
import com.example.ApplicationsManager.model.User;
import com.example.ApplicationsManager.viewmodel.AppViewModel;
import com.example.ApplicationsManager.viewmodel.UserViewModel;
import org.springframework.stereotype.Component;

@Component
public class ModelViewMapper {


    public ModelViewMapper(){
        super();
    }

    public AppViewModel convertToAppViewModel(Application app){
        AppViewModel viewModel = new AppViewModel();
        viewModel.setId(app.getId());
        viewModel.setName(app.getName());

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
