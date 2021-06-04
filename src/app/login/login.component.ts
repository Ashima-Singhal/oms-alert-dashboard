import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';
// import { authCodeFlowConfig } from '../sso.config';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private service:ApiService, private router:Router) {
   // this.configureSingleSignOn();
   }
  credentials={
    username:'',
    password:'',
    role:''
  }
  ngOnInit(): void {
  }

  // configureSingleSignOn(){
  //   this.oauth.configure(authCodeFlowConfig);
  //   this.oauth.tokenValidationHandler = new JwksValidationHandler();
  //   this.oauth.loadDiscoveryDocumentAndTryLogin();
  // }

  onSubmit(){
    
    if((this.credentials.username!='' && this.credentials.password!='' && this.credentials.role!='') && (this.credentials.username!=null && this.credentials.password!=null&&this.credentials.role!=null)){
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

   // this.oauth.initImplicitFlow();
  }

  // logout(){
  //   this.oauth.logOut();
  // }

}