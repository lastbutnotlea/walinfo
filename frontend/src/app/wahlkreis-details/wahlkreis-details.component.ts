import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {BackendService} from '../backend.service';

@Component({
  selector: 'app-wahlkreis-details',
  templateUrl: './wahlkreis-details.component.html',
  styleUrls: ['./wahlkreis-details.component.css']
})
export class WahlkreisDetailsComponent implements OnInit {

  wahlkreisId: number;

  constructor(private route: ActivatedRoute,
    private backendService: BackendService) { }

  ngOnInit() {
    this.wahlkreisId =  +this.route.snapshot.paramMap.get('id');
  }

}
