import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from './users';
import { Application } from '../application/application';
import { ApiService } from '../shared/api.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  model:UserViewModel={name:'',image_url:''};
  apps: Application[] = [];
  users: User[] = [];
  allApps: Application[] = [];
  userToUpdate: User = { id: 0, name: "", username: "", password: "" };
  selectedUser: User = { id: 0, name: "", username: "", password: "" };
  searchText: string;
  appToDelete: Application = {id: 0, name: "", technologies: "", version: "", imageUrl:""};
  appsToAdd: Application[]=[];
  selectedFile: File;

  constructor(private apiService: ApiService) { }


  ngOnInit() {
    this.getAllUsers();
  }

  getAllUsers():void{
    this.apiService.getAllUsers().subscribe(
      res => {this.users=res;},
      err => {alert("An error has occurred while getting the users()")}
    );
  }

  getAllApps(): void {
    this.apiService.getAllApps().subscribe(
      res => { this.allApps = res; },
      err => { alert("An error has occurred while getting the users") }
    );
  }

  addUser():void{
    this.uploadFile();
    this.apiService.addUser(this.model).subscribe(
      res => {this.getAllUsers();
              alert(this.model.name+" ("+this.model.image_url+" )was added with success!")},
      err => {alert("An error has occurred while adding an user")}
    );
  }

  deleteUser():void{
    if (confirm("Are you sure to delete " + this.selectedUser.name)) {
      console.log(
        this.apiService.deleteUser(this.selectedUser.id).subscribe(
          res => { alert(this.selectedUser.name + " was deleted with success!")
                  window.location.reload() },
          err => { alert("An error has occurred while deleting an app") }
        ))
    }
  }
  
  deleteAppFromUser(app:Application): void {
    if (confirm("Are you sure to delete " + app.name+"?")) {
      console.log(
        this.apiService.deleteAppFromUser(this.selectedUser.id,app).subscribe(
          res => { alert(app.name + " was deleted with success!");
                  this.getAppsByUser(); },
          err => { alert("An error has occurred while deleting an app") }
        ))
    }
  }

  getAppsByUser():void{
    this.apiService.getAppsByUser(this.selectedUser.id).subscribe(
      res => {this.apps = res;},
      err => {}
    );
  }

  selectUser(user: User) {
    this.apiService.getUser(user.id).subscribe(
      res => {
        this.selectedUser = res;
        this.appsToAdd = [];
      },
      err => { alert("An error has occurred while selecting the user;") }
    );

    this.apiService.getAppsByUser(user.id).subscribe(
      res => {
        this.apps = res;;

      },
      err => { alert("An error has occurred while selecting the user;") }
    );
  }

  
  addAppToList(app: Application): void {
    if(this.appsToAdd.includes(app)===false){
     this.appsToAdd.push(app);
     alert(app.name+" added to list");
    }else{
      alert(app.name+" already in the list");
      
    }
  }

  removeAppFromList(app: Application){
    if(this.appsToAdd.includes(app)===true){
       this.appsToAdd.forEach( (user1, index) => {
         if(user1 === app){
           this.appsToAdd.splice(index,1);
           alert(app.name+" removed from list");
         }
       });
      }else{
        alert(app.name+" not in the list");
      }
  }

  addTheApps(){
    for(var i = 0;i<this.appsToAdd.length;i++) { 
      this.apiService.addAppToUser(this.selectedUser.id,this.appsToAdd[i]).subscribe(
        res => { },
          err => { alert("An error has occurred while adding an user") }
      );
   }
  }

  updateUser(): void {
    this.userToUpdate.id = this.selectedUser.id;
    if (this.userToUpdate.name == "") {
      this.userToUpdate.name = this.selectedUser.name;
    }
    if (this.userToUpdate.username == "") {
      this.userToUpdate.username = this.selectedUser.username;
    }
    if (this.userToUpdate.password == "") {
      this.userToUpdate.password = this.selectedUser.password;
    }
    this.apiService.updateUser(this.userToUpdate).subscribe(
      res => {
        alert(this.userToUpdate.name + " was update with success!");
        this.userToUpdate = { id: 0, name: "", username: "", password: "" };
        this.selectedUser = res;
        window.location.reload();
      },
      err => { alert("An error has occurred while deleting an app") }
    );
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
}

export interface UserViewModel{
  name:string;
  image_url:string;
}