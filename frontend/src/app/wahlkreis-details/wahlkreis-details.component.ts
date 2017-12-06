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

  zweitStimmenPartei$: Observable<StimmenPartei[]>;
  zweitStimmenParteiLabels: string[] = ['12'];
  zweitStimmenParteiData: number[] = [2];

  erstStimmenPartei$: Observable<StimmenPartei[]>;
  erstStimmenParteiLabels: string[] = ['12'];
  erstStimmenParteiData: number[] = [2];

  parteiSieger: Partei;

  vergleichErstLabels: string[] = [];
  vergleichZweitLabels: string[] = [];
  vergleichChartType = 'bar';
  vergleichLegend = true;

  vergleichErstData: any[];
  vergleichZweitData: any[];

  anzahlMaenner: number;
  anzahlKandidaten: number;

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

    this.backendService.getFrauenMaennerQuote(this.wahlkreisNummer)
      .subscribe(res => {
        this.anzahlMaenner = res.anzahlMaenner;
        this.anzahlKandidaten = res.gesamtAnzahl;
      });

    this.erstStimmenPartei$ = this.backendService.getErstStimmenProPartei(this.wahlkreisNummer);
    this.erstStimmenPartei$.subscribe(res => {
      let parteiMandat;
      this.erstStimmenParteiData = [];
      this.erstStimmenParteiLabels = [];
      for (parteiMandat of res) {
        this.erstStimmenParteiLabels.push(parteiMandat.partei.kuerzel);
        this.erstStimmenParteiData.push(parteiMandat.anzahlAbsolut);
      }
    });

    this.zweitStimmenPartei$ = this.backendService.getZweitStimmenProPartei(this.wahlkreisNummer);
    this.zweitStimmenPartei$.subscribe(res => {
      let parteiMandat;
      this.zweitStimmenParteiData = [];
      this.zweitStimmenParteiLabels = [];
      for (parteiMandat of res) {
        this.zweitStimmenParteiLabels.push(parteiMandat.partei.kuerzel);
        this.zweitStimmenParteiData.push(parteiMandat.anzahlAbsolut);
      }
    });

    this.backendService.getWahlkreisSieger(this.wahlkreisNummer)
      .subscribe(res => {
        this.parteiSieger = res;
      });

    if (this.year === '2017') {
      this.backendService.getVergleich2017ErstVorjahr(this.wahlkreisNummer)
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
          this.vergleichErstData = [
            {data: data2013, label: '2013'},
            {data: data2017, label: '2017'}
          ];
          this.vergleichErstLabels = labels;
        });
      this.backendService.getVergleich2017ZweitVorjahr(this.wahlkreisNummer)
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
          this.vergleichZweitData = [
            {data: data2013, label: '2013'},
            {data: data2017, label: '2017'}
          ];
          this.vergleichZweitLabels = labels;
        });
    }
  }

}
