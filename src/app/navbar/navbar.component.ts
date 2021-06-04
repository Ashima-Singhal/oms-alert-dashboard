import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  public isCollapsed = true;
  public isLoggedIn = false;
  public username = '';
  public isManager = false;

  constructor(private service: ApiService,private router:Router){}
  ngOnInit(): void {
    this.isLoggedIn = this.service.isLoggedIn();
    this.username = this.service.getUsername();
    if(this.service.getRole() == 'Manager')
      this.isManager = true;
  }

  logoutUser(){
    this.service.logout();
    console.log("user successfully logged out");
    this.router.navigate(['login']);
  }
}
