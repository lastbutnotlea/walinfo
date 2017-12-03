import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import Wahlkreis from '../templates/Wahlkreis';
import {BackendService} from '../backend.service';

@Component({
  selector: 'app-wahlkreise',
  templateUrl: './wahlkreise.component.html',
  styleUrls: ['./wahlkreise.component.css']
})
export class WahlkreiseComponent implements OnInit {

  wahlkreise$: Observable<Wahlkreis[]>;

  constructor(private backendService: BackendService) {
    backendService.updatePage.subscribe(res => {
      this.loadWahlkreise();
    });
  }

  ngOnInit() {
    this.loadWahlkreise();
  }

  loadWahlkreise() {
    this.wahlkreise$ = this.backendService.getWahlkreise();
  }
}
