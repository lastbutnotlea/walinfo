import {Component, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {BackendService} from '../backend.service';
import {Observable} from 'rxjs/Observable';
import Wahlkreis from '../templates/Wahlkreis';
import Wahlbeteiligung from '../templates/Wahlbeteiligung';
import Kandidat from '../templates/Kandidat';
import StimmenPartei from '../templates/StimmenPartei';
import Partei from '../templates/Partei';

@Component({
  selector: 'app-wahlkreis-details',
  templateUrl: './wahlkreis-details.component.html',
  styleUrls: ['./wahlkreis-details.component.css']
})
export class WahlkreisDetailsComponent implements OnInit {

  wahlkreisNummer: string;
  year: string;

  wahlkreis: Wahlkreis;
  direktkandidat: Kandidat;
  wahlbeteiligung: Wahlbeteiligung;
  stimmenPartei$: Observable<StimmenPartei[]>;
  parteiSieger: Partei;

  vergleichLabels: string[] = [];
  vergleichChartType = 'bar';
  vergleichLegend = true;

  vergleichData: any[];

  constructor(private route: ActivatedRoute,
    private backendService: BackendService) { }

  ngOnInit() {
    this.wahlkreisNummer =  this.route.snapshot.paramMap.get('nummer');
    this.year = this.route.snapshot.paramMap.get('year');

    this.backendService.getWahlkreis(this.wahlkreisNummer)
      .subscribe(res => {
        this.wahlkreis = res;
      });

    this.backendService.getWahlbeteiligung(this.wahlkreisNummer)
      .subscribe(res => {
        this.wahlbeteiligung = res;
      });

    this.backendService.getDirektkandidat(this.wahlkreisNummer)
      .subscribe(res => {
        this.direktkandidat = res;
      });

    this.stimmenPartei$ = this.backendService.getStimmenProPartei(this.wahlkreisNummer);

    this.backendService.getWahlkreisSieger(this.wahlkreisNummer)
      .subscribe(res => {
        this.parteiSieger = res;
      });

    if (this.year === '2017') {
      this.backendService.getVergleichVorjahr(this.wahlkreisNummer)
        .subscribe(res => {
          const data2013 = [];
          const data2017 = [];
          const labels: string[] = [];
          let parteiVergleich;
          for (parteiVergleich of res) {
            labels.push(parteiVergleich.partei.kuerzel);
            data2013.push(parteiVergleich.stimmen2013);
            data2017.push(parteiVergleich.stimmen2017);
          }
          this.vergleichData = [
            {data: data2013, label: '2013'},
            {data: data2017, label: '2017'}
          ];
          this.vergleichLabels = labels;
        });
    }
  }

}
