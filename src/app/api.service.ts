import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Events } from './events';
import {switchMap, map} from 'rxjs/operators';



@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  private url:string = 'http://localhost:8080/';


  search(state:any,account_name:any)
  {
    console.log('state in servcie-'+state);
    console.log('account name in service-'+account_name)
    return this.http.get(this.url+"events-list?status="+state+"&account_name="+account_name);
  }

  //method to get all events irrespective of the status
  searchAllEvents(searchCond:any):Observable<any>{
      return this.http.post(this.url+"events",searchCond);
  }

  update(incident_id:any, patchObj:Events):Observable<any>{
      return this.http.patch(this.url+"update-event?alert_id="+incident_id,patchObj);
  }

  //service method to get all customers stored in db
  getCustomers(){
      return this.http.get(this.url+"get-customers");
  }

  //service method to get all conditions stored in db
  getConditions(){
    return this.http.get(this.url+"get-conditions");
  }

  //service method to login
  login(response){
    localStorage.setItem('token',response.token);
    localStorage.setItem('username',response.username);
    localStorage.setItem('role',response.role);
    return true;
  }

  //service method to check if user is logged in
  isLoggedIn(){
    let token = localStorage.getItem('token');
    if(token == undefined || token === '' || token == null) return false;
    return true;
  }

  //service method to logout
  logout(){
    localStorage.removeItem('token');
    return true;
  }

  //service method to get token
  getToken(){
    return localStorage.getItem('token');
  }

  //service method to call server to generate token
  generateToken(credentials:any){
    return this.http.post(this.url+"token",credentials);
  }

  //service method to get username 
  getUsername(){
    return localStorage.getItem('username');
  }

  //service method to register user
  register(registraitionDetails:any){
    return this.http.post(this.url+"register",registraitionDetails);
  }

  //service method to get role
  getRole(){
    return localStorage.getItem('role');
  }

  //service method to get date and time
  getDateTime(){
    return this.http.get(this.url+"getDate");
  }
}
