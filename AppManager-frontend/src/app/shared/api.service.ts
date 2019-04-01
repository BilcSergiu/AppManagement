import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../user/users';
import { Application } from '../application/application';
import { AppViewModel } from '../application/application.component';
import { UserViewModel } from '../user/user.component';
import { UploadResponse } from '../application/uploadRespose';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private BASE_URL = "http://localhost:8080" ;
  public ALL_APPS_URL = `${this.BASE_URL}/apps/all`;
  public UPLOAD_FILE = `${this.BASE_URL}/uploadFile`;
  public SAVE_UPDATE_APP = `${this.BASE_URL}/apps/add`;
  public UPDATE_APP = `${this.BASE_URL}/apps/update`;
  public UPDATE_USER = `${this.BASE_URL}/users/update`;
  public GET_APP = `${this.BASE_URL}/apps/appById/`;
  public GET_USER = `${this.BASE_URL}/users/userById/`;
  public DELETE_USER_FROM_APP = `${this.BASE_URL}/apps/deleteUser/`;
  public DELETE_APP_FROM_USER = `${this.BASE_URL}/users/deleteApp/`;
  public ADD_USER_TO_APP = `${this.BASE_URL}/apps/addUser/`;
  public ADD_APP_TO_USER = `${this.BASE_URL}/users/addApp/`;
  private DELETE_APP_URL = `${this.BASE_URL}/apps/delete/`;
  private APP_BY_NAME_URL = `${this.BASE_URL}/apps/byName/`;
  private APPS_BY_USER_URL = `${this.BASE_URL}/users/getApps/`;
  private ALL_USERS_URL = `${this.BASE_URL}/users/all`;
  private USERS_BY_APP_URL = `${this.BASE_URL}/apps/getUsers/`;
  private SAVE_UPDATE_USER_URL = `${this.BASE_URL}/users/add`;
  private DELETE_USER_URL = `${this.BASE_URL}/users/delete/`;

  constructor(private http: HttpClient) {

  }

  getAllApps(): Observable<Application[]> {
    return this.http.get<Application[]>(this.ALL_APPS_URL);
  }

  addApp(app: AppViewModel): Observable<AppViewModel> {
    return this.http.post<AppViewModel>(this.SAVE_UPDATE_APP, app);
  }

  deleteApp(id: number): Observable<any> {
    return this.http.delete(this.DELETE_APP_URL + id);
  }

  getApp(id: number): Observable<any> {
    return this.http.get(this.GET_APP + id);
  }
  
  getUser(id: number): Observable<any> {
    return this.http.get(this.GET_USER + id);
  }

  updateApp(app: Application): Observable<any> {
    return this.http.put(this.UPDATE_APP,app);
  }

  updateUser(user: User): Observable<any> {
    return this.http.put(this.UPDATE_USER,user);
  }
  
  addUserToApp(id: number, user: User): Observable<any> {
    alert(user.name+" was added with success!");
    return this.http.post(this.ADD_USER_TO_APP+id,user);
  }
  
  addAppToUser(id: number, app: Application): Observable<any> {
    return this.http.post(this.ADD_APP_TO_USER+id,app);
  }


  deleteUserFromApp(id: number, user: User): Observable<any> {
    return this.http.put(this.DELETE_USER_FROM_APP+id,user);
  }
  
  deleteAppFromUser(id: number, app: Application): Observable<any> {
    return this.http.put(this.DELETE_USER_FROM_APP+id,app);
  }

  getAppByName(user: string): Observable<Application>{
    return this.http.get<Application>(this.APP_BY_NAME_URL);
  }

  getAppsByUser(userId: number): Observable<Application[]> {
    return this.http.get<Application[]>(this.APPS_BY_USER_URL + userId);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.ALL_USERS_URL);
  }

  getUsersByApp(appId: number): Observable<User[]> {
    return this.http.get<User[]>(this.USERS_BY_APP_URL + appId);
  }

  addUser(user: UserViewModel): Observable<User> {
    return this.http.post<User>(this.SAVE_UPDATE_USER_URL, user);
  }

  deleteUser(userId:number):Observable<any>{
    return this.http.delete(this.DELETE_USER_URL + userId);
  }

  uploadFile(formData:FormData):Observable<UploadResponse>{
    const httpOptions = {
      headers: new HttpHeaders({
 
      })
    };
    let response: UploadResponse;
    return this.http.post<UploadResponse>(this.UPLOAD_FILE, formData,httpOptions);
  }

  downloadFile(url:string):Observable<FormData>{

    return this.http.get<FormData>(url);
  }
}
