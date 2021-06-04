import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registraitionDetails={
    username:'',
    password:'',
    role:''
  }

  constructor(private api:ApiService,private router:Router) { }

  ngOnInit(): void {}

  onSubmit(){
    console.log('username-'+this.registraitionDetails.username);
    console.log('password-'+this.registraitionDetails.password);
    console.log('role-'+this.registraitionDetails.role);

    this.api.register(this.registraitionDetails).subscribe(data=>{
      console.log(data);
      this.router.navigate(['dashboard']);
    },err=>{
      console.log(err);
    })
  }

}
