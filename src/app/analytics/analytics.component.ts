import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Events } from '../events';

import { ViewChild, ElementRef, VERSION} from '@angular/core';
import { FormControl, FormGroup, Validators} from '@angular/forms';
import { Pipe, PipeTransform, Injectable} from '@angular/core';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import * as XLSX from 'xlsx';
import * as fs from 'file-saver';
import * as  Highcharts from 'highcharts';
import Exporting from 'highcharts/modules/exporting';
import { Chart } from 'chart.js';
import { ChartOptions, ChartType } from 'chart.js';
import { Label, SingleDataSet } from 'ng2-charts';

// Initialize exporting module.
Exporting(Highcharts);
import Data from 'highcharts/modules/data';
// Initialize Data module.
Data(Highcharts);
import ExportData from 'highcharts/modules/export-data';


// Initialize ExportData module.
ExportData(Highcharts);

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
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
export class AnalyticsComponent implements OnInit {

  name = `Angular! v${VERSION.full}`;
  @ViewChild("container", { read: ElementRef }) container: ElementRef;
  @ViewChild("datatable", { read: ElementRef }) datatable: ElementRef;

  public pieChartOptions: ChartOptions = {
    responsive: true,
    legend: {
      position: 'top',
    },
    tooltips: {
      enabled: true,
      mode: 'single',
      callbacks: {
        label: function (tooltipItems, data) {
          return data.datasets[0].data[tooltipItems.index] + ' %';
        }
      }
    },
  };
 // public pieChartLabels: Label[] = ['VFC_INT_RETURN_SEND_INVOICE_Q Depth > 100', 'Critical MQ connection issue','VFC_INT_ORDER_CLOSE_Q Depth > 100',
  //'VFC_INB_WCS_WMB_ITEM_UPDATE_Q Depth > 100',  'VFC_INT_SHIPMENT_CLOSE_Q Depth > 100',  'VFC_PAYMENT_EXECUTION_AGENT_Q Depth > 100',
  //'VFC_CAPACITY_CHANGE_INT_Q Depth > 50','VFC_PAYMENT_COLLECTION_Q Depth > 100',  'VFC_INB_DOM_STR_ORDER_UPDATE_Q Depth > 100',
  //'VFC_RESEND_EMAILS_PURGE_AGENT_Q Depth > 100','VFC_INB_MDM_STR_ITEM_Q Depth > 100', 'VFC_INT_VENDOR_FEED_AGENT_Q Depth > 100',
  //'VFC_INB_WC_STR_ORDER_CREATE_Q Depth > 200',  'Data Extract Failure', 'Deadlock Detected','Critical - Excessive Queries in Lock-Wait','OMSStatistics query result is 0 units for at least 30 mins',
  //'Excessive Database Query Timeouts (YFC0006)','VFC_INB_DOM_STR_ORDER_UPDATE_Q Depth > 100','Default Queue Depth Alert - Custom Queues','Queue Depth exceeds 50000',
  //'Container CPU Usage % is too high','Container Memory Usage % is too high','Crashloop Detection','Connectivity Issue (Various Connection Failure Exceptions)',
  //'rl_ReceivePIXFeed_Q depth > 1 for 1 hour','STERLING_EFA_TO_OMS_DEMANDS_Q Queue Depth','Host CPU % above 90 for 5 minutes','TB_LOAD_INV_MISMATCH_Q queue depth > 100 for 15 minutes',
   //'OMSStatistics query result is 0 units for at least 120 mins','IV integration failure','Connectivity Issue (ConnectException)','Excessive Errors from Agent/Integration Servers - critical',
  //'Excessive Errors - Critical','TB_CREATE_ORDER_Q queue depth > 100 for 15 minutes'];
  public pieChartData: SingleDataSet=[];
  public pieChartType: ChartType = 'pie';
 
  public pieChartPlugins = [];
  public chartColors: Array<any> = [
    { // all colors in order
      backgroundColor: ['#9C27B0','#69F0AE','#6A1B9A', '#d13537', '#b000b5', '#c0ffee', '#000000', '#007E33', '#0099CC', '#FF8800', '#CC0000', '#ff4444', '#00695c','#0d47a1','#3E4551', '#ff80ab','#69F0AE','#6A1B9A',
         '#607D8B','#795548','#EEFF41','#689F38','#FF4081','#F44336','#C62828','#FF4081','#448AFF','#3F51B5','#448AFF',
        '#9E9D24','#E040FB','#CDDC39','#37474F','ffe4c4']
     
    }
]
    public pieChartLabels: Label[]=['VFC_PAYMENT_EXECUTION_AGENT_Q Depth > 100','VFC_INB_WCS_WMB_ITEM_UPDATE_Q Depth > 100','VFC_PAYMENT_EXECUTION_Q Depth > 100','VFC_PAYMENT_EXECUTION_Q Depth > 100'];
 
  constructor(private service:ApiService, private router:Router, private route: ActivatedRoute) { }

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
  selectedItems = [];
  dropdownSettings : IDropdownSettings;
  ConditiondropdownSettings: IDropdownSettings;
  public chartdata: any = [];
  acc:any=[];
  cond:any=[];
  myType = 'PieChart';
  width = 550;
  height = 400;
  piedata:any=[];
  acc2:any=[];
  cond2:any=[];

  public show:boolean = false;
  public buttonName:any = 'Show';


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
   
    this.ConditiondropdownSettings={
      singleSelection: true,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    }

  
}


update(event:Events){
    console.log('event selected-'+event.incident_id)
    console.log('slack_url'+event.slack_url)
    this.patchObj.slack_url = event.slack_url;

    this.service.update(event.incident_id,this.patchObj).subscribe(data => {
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
  this.service.search(this.dateRange.value.Alerts,this.dateRange.value.Customers).subscribe(data=>{
    console.log('searched data- '+data)
    this.events = data;
  })
  
}



get f(){
  return this.dateRange.controls;
}


//method to get all customers in db
getCustomers(){
  this.service.getCustomers().subscribe(data=>{
    this.customers = data;
    //console.log(this.customers);
  })
}

//method to get all conditions in db
getConditions(){
  this.service.getConditions().subscribe(data => {
    this.conditions = data;
  })
}

//method to get all events according to filters
getAllEvents(){
  this.searchCond.current_state = this.dateRange.value.Alerts;
  if(this.searchCond.current_state == 'all')
    this.searchCond.current_state = '';
 
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

 

  this.service.searchAllEvents(this.searchCond).subscribe(data=>{

    this.events = data;
    var totalLength = data.length;
   // var condit1 = data.filter(data=>(data.condition_name=='VFC_INT_RETURN_SEND_INVOICE_Q Depth > 100')).length;
    //var condit2 = data.filter(data=>(data.condition_name == 'Critical MQ connection issue')).length;
    //var condit3 = data.filter(data=>(data.condition_name == 'VFC_INT_ORDER_CLOSE_Q Depth > 100')).length;
    //var condit4 = data.filter(data=>(data.condition_name == 'VFC_INB_WCS_WMB_ITEM_UPDATE_Q Depth > 100')).length;
    //var condit5 = data.filter(data=>(data.condition_name == 'VFC_INT_SHIPMENT_CLOSE_Q Depth > 100')).length;
    //var condit6 = data.filter(data=>(data.condition_name == 'VFC_PAYMENT_EXECUTION_AGENT_Q Depth > 100')).length;
   // var condit7 = data.filter(data=>(data.condition_name == 'VFC_CAPACITY_CHANGE_INT_Q Depth > 50' )).length;
    //var condit8 = data.filter(data=>(data.condition_name == 'VFC_PAYMENT_COLLECTION_Q Depth > 100')).length;
    //var condit9 = data.filter(data=>(data.condition_name == 'VFC_INB_DOM_STR_ORDER_UPDATE_Q Depth > 100')).length;
    //var condit10 = data.filter(data=>(data.condition_name == 'VFC_RESEND_EMAILS_PURGE_AGENT_Q Depth > 100')).length;
    //var condit11 = data.filter(data=>(data.condition_name == 'VFC_INB_MDM_STR_ITEM_Q Depth > 100')).length;
    //var condit12= data.filter(data=>(data.condition_name == 'VFC_INT_VENDOR_FEED_AGENT_Q Depth > 100')).length;
    var condit13= data.filter(data=>(data.condition_name == 'VFC_INB_WC_STR_ORDER_CREATE_Q Depth > 200')).length;
    var condit14 = data.filter(data=>(data.condition_name ==  'Data Extract Failure')).length;
    var condit15 = data.filter(data=>(data.condition_name ==  'Deadlock Detected')).length;
    var condit16 = data.filter(data=>(data.condition_name == 'Critical - Excessive Queries in Lock-Wait')).length;
    var condit17 = data.filter(data=>(data.condition_name == 'OMSStatistics query result is 0 units for at least 30 mins')).length;
    var condit18 = data.filter(data=>(data.condition_name == 'Excessive Database Query Timeouts (YFC0006)')).length;
    var condit19 = data.filter(data=>(data.condition_name == 'VFC_INB_DOM_STR_ORDER_UPDATE_Q Depth > 100')).length;
    var condit20 = data.filter(data=>(data.condition_name == 'Default Queue Depth Alert - Custom Queues')).length;
    var condit21 = data.filter(data=>(data.condition_name == 'Queue Depth exceeds 50000')).length;
    var condit22 = data.filter(data=>(data.condition_name == 'Container CPU Usage % is too high')).length;
    var condit23 = data.filter(data=>(data.condition_name == 'Container Memory Usage % is too high')).length;
    var condit24 = data.filter(data=>(data.condition_name == 'Crashloop Detection')).length;
    var condit25 = data.filter(data=>(data.condition_name == 'Connectivity Issue (Various Connection Failure Exceptions)')).length;
    var condit26 = data.filter(data=>(data.condition_name == 'rl_ReceivePIXFeed_Q depth > 1 for 1 hour')).length;
    var condit27 = data.filter(data=>(data.condition_name == 'STERLING_EFA_TO_OMS_DEMANDS_Q Queue Depth')).length;
    var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    var condit29 = data.filter(data=>(data.condition_name == 'TB_LOAD_INV_MISMATCH_Q queue depth > 100 for 15 minutes')).length;
    var condit30= data.filter(data=>(data.condition_name == 'GC (Global) Overhead (High) - Application Server - IKS')).length;
    var condit31 = data.filter(data=>(data.condition_name == 'VFC_PAYMENT_EXECUTION_AGENT_Q Depth > 100')).length;
    var condit32 = data.filter(data=>(data.condition_name == 'VFC_INB_WCS_WMB_ITEM_UPDATE_Q Depth > 100')).length;
    var condit33 = data.filter(data=>(data.condition_name == 'VFC_PAYMENT_EXECUTION_Q Depth > 100')).length;
    var condit34 = data.filter(data=>(data.condition_name == 'VFC_PAYMENT_EXECUTION_Q Depth > 100')).length;
   // var condit36 = data.filter(data=>(data.condition_name == 'VFC_PAYMENT_EXECUTION_AGENT_Q Depth > 100')).length;
   // var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit2 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
   // var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;
    //var condit28 = data.filter(data=>(data.condition_name == 'Host CPU % above 90 for 5 minutes')).length;

    this.pieChartData=[condit31,condit32,condit33,condit34];

   
   
    this.selectedStatic(this.searchCond.condition_name)

    console.log(this.datatable.nativeElement);
    console.log(this.container.nativeElement);
   
    
    
  Highcharts.chart(this.container.nativeElement, {
    data: {
      table: document.getElementById('datatable'),
      switchRowsAndColumns: true, // use rows as points
      startColumn: 0, 
      endColumn: 1
    
    },
    chart: {
      type: 'column'
    },
    title: {
      text: this.dateRange.value.Customers
    },

    subtitle: {
      text: this.searchCond.condition_name
   },

    yAxis: {
      allowDecimals: false,
       //categories: ['this.dateRange.value.Conditions'],
      title: {
        text: 'Count'
      }
     
    },

    xAxis: {
      allowDecimals: false,
      title: {
        text: 'Alert Type'
      }
     
    },
   
    tooltip: {
      formatter: function () {
        return '<b>' + this.series.name + '</b><br/>' +
          this.point.y + ' ' + this.point.name;
      }
    }

   
  })


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

onItemSelect(item: any) {
  console.log(item);
}
onSelectAll(items: any) {
  console.log(items);
}


toggle() {
  this.show = !this.show;

  // CHANGE THE NAME OF THE BUTTON.
  if(this.show)  
    this.buttonName = "Hide";
  else
    this.buttonName = "Show";
}


}



