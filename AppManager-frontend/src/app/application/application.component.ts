import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { ApiService } from '../shared/api.service';
import { Application } from './application';
import { User } from '../user/users';
import { RequestOptions } from '@angular/http';

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.css']
})
export class ApplicationComponent implements OnInit {

  model: AppViewModel = { name: '', image_url:"" };
  apps: Application[] = [];
  users: User[] = [];
  allUsers: User[] = [];
  appToUpdate: Application = { id: 0, name: "", technologies: "", version: "" ,imageUrl:""};
  selectedApp: Application = { id: 0, name: "", technologies: "", version: "", imageUrl:"" };
  searchText: string;
  userToDelete: User = { id: 0, name: "", username: "", password: "" };
  usersToAdd: User[] = [];
  selectedFile: File;


  constructor(private apiService: ApiService) { }

  ngOnInit() {
    this.getAllApps();

  }

  getAllApps(): void {
    this.apiService.getAllApps().subscribe(
      res => { this.apps = res; },
      err => { alert("An error has occurred while getting the apps") }
    );
  }

  getAllUsers(): void {
    this.apiService.getAllUsers().subscribe(
      res => { this.allUsers = res; },
      err => { alert("An error has occurred while getting the users for apps") }
    );
  }

  addApp(): void {

    this.uploadFile();
    this.apiService.addApp(this.model).subscribe(
      res => { this.getAllApps(); alert(this.model.name + " ( "+ this.model.image_url+" ) was added with success!") },
      err => { alert("An error has occurred while adding an app") }
    );
  }


  deleteApp(): void {
    if (confirm("Are you sure to delete " + this.selectedApp.name)) {
      console.log(
        this.apiService.deleteApp(this.selectedApp.id).subscribe(
          res => {
            alert(this.selectedApp.name + " was deleted with success!")
            window.location.reload()
          },
          err => { alert("An error has occurred while deleting an app") }
        ))
    }
  }

  deleteUserFromApp(user: User): void {
    if (confirm("Are you sure to delete " + user.name)) {
      console.log(
        this.apiService.deleteUserFromApp(this.selectedApp.id, user).subscribe(
          res => {
            alert(user.name + " was deleted with success!");
            this.getUsersByApp();
          },
          err => { alert("An error has occurred while deleting an app") }
        ))
    }
  }

  getUsersByApp(): void {
    this.apiService.getUsersByApp(this.selectedApp.id).subscribe(
      res => { this.users = res; },
      err => { }
    );
  }

  addUserToList(user: User): void {
    if (this.usersToAdd.includes(user) === false) {
      this.usersToAdd.push(user);
      alert(user.name + " added to list");
    } else {
      alert(user.name + " already in the list");

    }
  }

  selectFile(event) {
    let fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      this.selectedFile = fileList[0];
    }

  }

  uploadFile() {
    if (this.selectedFile != null) {
      let file: File = this.selectedFile;
      let formData: FormData = new FormData();
      formData.append('file', file, file.name);

      this.apiService.uploadFile(formData).subscribe(
        res=>{this.model.image_url = res.fileDownloadUri,
        alert( this.model.image_url+" este url-ul!")},
        error => {alert("Error at uploading the file")
        
        });
    }
  }

  removeUserFromList(user: User) {
    if (this.usersToAdd.includes(user) === true) {
      this.usersToAdd.forEach((user1, index) => {
        if (user1 === user) {
          this.usersToAdd.splice(index, 1);
          alert(user.name + " removed from list");
        }
      });
    } else {
      alert(user.name + " not in the list");
    }
  }

  addTheUsers() {
    for (var i = 0; i < this.usersToAdd.length; i++) {
      this.apiService.addUserToApp(this.selectedApp.id, this.usersToAdd[i]).subscribe(
        res => { },
        err => { alert("An error has occurred while adding an user") }
      );
    }
  }


  selectApp(app: Application): void {
    this.apiService.getApp(app.id).subscribe(
      res => {
        this.selectedApp = res;
        this.usersToAdd = [];
      },
      err => { alert("An error has occurred while selecting the app;") }
    );

    this.apiService.getUsersByApp(app.id).subscribe(
      res => {
        this.users = res;;

      },
      err => { alert("An error has occurred while selecting the app;") }
    );
  }

  updateApp(): void {
    this.appToUpdate.id = this.selectedApp.id;
    if (this.appToUpdate.name == "") {
      this.appToUpdate.name = this.selectedApp.name;
    }
    if (this.appToUpdate.technologies == "") {
      this.appToUpdate.technologies = this.selectedApp.technologies;
    }
    if (this.appToUpdate.version == "") {
      this.appToUpdate.version = this.selectedApp.version;
    }
    this.apiService.updateApp(this.appToUpdate).subscribe(
      res => {
        alert(this.appToUpdate.name + " was update with success!");
        this.appToUpdate = { id: 0, name: "", technologies: "", version: "", imageUrl:"" };
        this.selectedApp = res;
        window.location.reload();
      },
      err => { alert("An error has occurred while deleting an app") }
    );
  }



}
export interface AppViewModel {
  name: string;
  image_url: string;
}
