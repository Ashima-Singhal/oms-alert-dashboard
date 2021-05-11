import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private service:ApiService, private router:Router) { }
  credentials={
    username:'',
    password:''
  }
  ngOnInit(): void {
  }

  onSubmit(){
    
    if((this.credentials.username!='' && this.credentials.password!='') && (this.credentials.username!=null && this.credentials.password!=null)){
      console.log('form is submitted');
      this.service.generateToken(this.credentials).subscribe((data:any)=>{
        console.log(data);
        this.service.login(data);
        //window.location.href = "/dashboard";
        this.router.navigate(['dashboard']);
      },err=>{
        console.log(err);
      })
    }
    else
      console.log('fields are empty');
  }

}
