import { Component, OnInit, ViewChild, ElementRef, VERSION} from '@angular/core';
import { FormControl, FormGroup, Validators} from '@angular/forms';

import { ApiService } from '../api.service';
import { Events } from '../events';
import { Pipe, PipeTransform, Injectable} from '@angular/core';
import * as XLSX from 'xlsx';
import * as fs from 'file-saver';
import { IDropdownSettings } from 'ng-multiselect-dropdown';


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
  alerts_type: any = ['all','open','closed'];
  customers:any =[];
  conditions:any= [];
  searchedKeyword: string;
  SelectedConditions: string;
  selected: string;
  data: any;
  settings = {};
  abc:any =[];
  itemList = [];
  dateTime:any;
  dropdownSettings : IDropdownSettings;
  dropdownSet : IDropdownSettings;
  searchCond={
    current_state:'',
    account_name: [],
    condition_name:'',
    timestamp:Number,
    endTimestamp:Number
  }


  dateRange = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
    Alerts: new FormControl('', Validators.required),
    Customers: new FormControl([], Validators.required),
    Conditions: new FormControl([], Validators.required)
  });

  ngOnInit(): void {
      this.getAllEvents();      
      this.getCustomers();
      this.getConditions();
      this.dropdownSettings = {
        singleSelection: false,
        idField: 'item_id',
        textField: 'item_text',
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        itemsShowLimit: 3,
        allowSearchFilter: true
      };

      this.dropdownSet={
        singleSelection: true,
        idField: 'item_id',
        textField: 'item_text',
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        itemsShowLimit: 3,
        allowSearchFilter: true
      };

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


  //method to get all customers in db
  getCustomers(){
    this.apiService.getCustomers().subscribe(data=>{
      this.customers = data;
      //console.log(this.customers);
    })
  }

  //method to get all conditions in db
  getConditions(){
    this.apiService.getConditions().subscribe(data => {
      this.conditions = data;
    })
  }

  //method to get all events according to filters
  getAllEvents(){
    this.searchCond.current_state = this.dateRange.value.Alerts;
    if(this.searchCond.current_state == 'all')
      this.searchCond.current_state = '';
    else if(this.searchCond.current_state == '')
    this.searchCond.current_state = 'open';
    
    this.searchCond.account_name = this.dateRange.value.Customers;
    this.searchCond.condition_name = this.dateRange.value.Conditions;
    if(this.dateRange.value.start != null)
      this.searchCond.timestamp = this.dateRange.value.start.getTime();
    if(this.dateRange.value.end != null)
      this.searchCond.endTimestamp = this.dateRange.value.end.getTime();

    console.log('Current state-'+this.searchCond.current_state);
    console.log('Account name-'+this.searchCond.account_name);
    console.log('Condition name-'+this.searchCond.condition_name);
    console.log('Start date-'+this.searchCond.timestamp);
    console.log('End date-'+this.searchCond.endTimestamp);

    this.apiService.searchAllEvents(this.searchCond).subscribe(data=>{
      this.events = data;

     this.selectedStatic(this.searchCond.condition_name)

    })
    

  }
  exportToExcel()
  {
    let element = document.getElementById('datatable')
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

  public search1 = '';

  selectedStatic(result) {

    this.search1 = result;
    this.events = this.events.filter(f=> f.condition_name.match(result));
    
  
  }


getDateTime(){
  console.log('in method');
  this.apiService.getDateTime().subscribe(data=>{
    this.dateTime = data;
  })
}
onItemSelect(item: any) {
  console.log(item);
}
onSelectAll(items: any) {
  console.log(items);
}

onDeSelectAll(items: any) {
  console.log(items);
}

}
