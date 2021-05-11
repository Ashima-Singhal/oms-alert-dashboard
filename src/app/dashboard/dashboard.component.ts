import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators} from '@angular/forms';

import { ApiService } from '../api.service';
import { Events } from '../events';
import { Pipe, PipeTransform, Injectable} from '@angular/core';
import * as XLSX from 'xlsx';
import * as fs from 'file-saver';

declare var _: any; // lodash, not strictly typed

@Pipe({
    name: 'uniqFilter',
    pure: false
})
@Injectable()
    export class UniquePipe implements PipeTransform {
        transform(items: any[], args: any[]): any {

        // lodash uniqBy function
        return _.uniqBy(items, args);
    }
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private apiService : ApiService) { }
  events:any = [];
  totalLength:any;
  page:number = 1;
  showEditTable:boolean = false;
  editRowId:any = '';
  patchObj = new Events();
  public  model = 1;
  alerts_type: any = ['open','closed'];
  customers:any =[];
  conditions:any= [];

  dateRange = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
    Alerts: new FormControl('', Validators.required),
    Customers: new FormControl('', Validators.required),
    Conditions: new FormControl('', Validators.required)
  });

  ngOnInit(): void {
      this.getAllEvents();      
      this.getCustomers();
      this.getConditions();
  }

  update(event:Events){
      console.log('event selected-'+event.incident_id)
      console.log('slack_url'+event.slack_url)
      this.patchObj.slack_url = event.slack_url;

      this.apiService.update(event.incident_id,this.patchObj).subscribe(data => {
        console.log('data updated'+data);
        this.editRowId = '';
      })
  }

  edit(val){
    this.editRowId = val;
  }


  search(){
    console.log('in search');
    console.log(this.dateRange.value.Alerts+" "+this.dateRange.value.Customers);
    this.apiService.search(this.dateRange.value.Alerts,this.dateRange.value.Customers).subscribe(data=>{
      console.log('searched data- '+data)
      this.events = data;
    })
  }

  get f(){
    return this.dateRange.controls;
  }

  //code to search records according to filters
  searchDate(){
    console.log("start-"+this.dateRange.value.start);
    console.log("end-"+this.dateRange.value.end);
    console.log("alerts status-"+this.dateRange.value.Alerts);
    console.log("customer name-"+this.dateRange.value.Customers);
    console.log("customer name-"+ this.dateRange.value.Conditions);
    this.apiService.search(this.dateRange.value.Alerts,this.dateRange.value.Customers).subscribe(data=>{
      console.log('data-'+data);
      this.events = data;

      if(this.dateRange.value.start !== null){
        this.events = this.events.filter(f=>new Date(f.timestamp)>=new Date(this.dateRange.value.start) && new Date(f.timestamp) <= new Date(this.dateRange.value.end));
        console.log('selected events -' + this.events)
      }

      if(this.dateRange.value.Conditions !== null){
        this.events = this.events.filter(f=> f.condition_name.localeCompare(this.dateRange.value.Conditions))
        console.log('selected events according to condition name-'+this.events);
      }
    })
  }

  //method to get all customers in db
  getCustomers(){
    this.apiService.getCustomers().subscribe(data=>{
      this.customers = data;
    })
  }

  //method to get all conditions in db
  getConditions(){
    this.apiService.getConditions().subscribe(data => {
      this.conditions = data;
    })
  }

  //method to get all events irrespective of the status
  getAllEvents(){
    this.apiService.searchAllEvents().subscribe(data=>{
      this.events = data;
    })
  }
  exportToExcel()
  {
    let element = document.getElementById('excel-table')
    const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(element);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb,ws,'Sheet1');
    /* save to file */
    XLSX.writeFile(wb, 'SheetJS.xlsx');
  }
  exportJsonData()
  {
    let data = this.events;
    let options:XLSX.JSON2SheetOpts  = {header: ['#', 'Alert Id', 'NR URL','Triggered Time','End time','Alert Type','Slack URL','Customer Name','Status','Action']};
    const ws: XLSX.WorkSheet=XLSX.utils.json_to_sheet(data,options);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'All Data Export');
    XLSX.writeFile(wb, 'data.xlsx');
  }

  //method to reset dashboard
  reset(){
    this.ngOnInit();
    //this.dateRange.value.Conditions = '';
  }
}
