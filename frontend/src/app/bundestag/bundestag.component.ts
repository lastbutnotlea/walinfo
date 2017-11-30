import { Component, OnInit } from '@angular/core';
import {BackendService} from '../backend.service';

@Component({
  selector: 'app-bundestag',
  templateUrl: './bundestag.component.html',
  styleUrls: ['./bundestag.component.css']
})
export class BundestagComponent implements OnInit {

  kandidaten = ['sfgsdf', 'safsdf', 'sdfsdf'];

  pieChartData = {
    chartType: 'PieChart',
    dataTable: [
      ['Task', 'Hours per Day'],
      ['Work',     11],
      ['Eat',      2],
      ['Commute',  2],
      ['Watch TV', 2],
      ['Sleep',    7]
    ],
    options: {
      // 'title': 'Tasks'
      slices: {
        0: { color: 'yellow' },
        1: { color: 'transparent' }
      }
    },
  };

  constructor(private backendService: BackendService) { }

  ngOnInit() {
  }

}
