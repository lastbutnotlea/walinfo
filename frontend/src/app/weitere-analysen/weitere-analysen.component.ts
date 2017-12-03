import { Component, OnInit } from '@angular/core';
import {BackendService} from '../backend.service';
import {Observable} from 'rxjs/Observable';
import UeberhangMandateBundesland from '../templates/UeberhangMandateBundesland';

@Component({
  selector: 'app-weitere-analysen',
  templateUrl: './weitere-analysen.component.html',
  styleUrls: ['./weitere-analysen.component.css']
})
export class WeitereAnalysenComponent implements OnInit {

  ueberhangsmandate$: Observable<UeberhangMandateBundesland[]>;

  constructor(private backendService: BackendService) {
    backendService.updatePage.subscribe(res => {
      this.reload();
    });
  }

  ngOnInit() {
    this.reload();
  }

  reload() {
    this.ueberhangsmandate$ = this.backendService.getUeberhangsmandate();
  }

}
