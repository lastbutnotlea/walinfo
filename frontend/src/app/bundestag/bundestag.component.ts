import {Component, OnInit, ViewChild} from '@angular/core';
import {BackendService} from '../backend.service';
import ParteiMandate from '../templates/ParteiMandate';
import {Observable} from 'rxjs/Observable';
import Kandidat from '../templates/Kandidat';
import {filter} from 'rxjs/operator/filter';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-bundestag',
  templateUrl: './bundestag.component.html',
  styleUrls: ['./bundestag.component.css']
})
export class BundestagComponent implements OnInit {

  sitzverteilung$: Observable<ParteiMandate[]>;
  mitgliederBundestag: Kandidat[];
  mitgliederBundestagFiltered: Kandidat[] = [];
  parteien: string[] = [];

  pieChartLabels: string[] = ['12'];
  pieChartData: number[] = [2];
  pieChartOption = {
    rotation: 1 * Math.PI,
    circumference: 1 * Math.PI
  };
  pieChartColors: any[] = [];
  pieChartType = 'pie';

  constructor(private backendService: BackendService) {
    backendService.updatePage.subscribe(res => {
      this.loadBundestagData();
    });
  }

  ngOnInit() {
    this.loadBundestagData();
  }

  loadBundestagData() {
    this.sitzverteilung$ = this.backendService.getBundestagSitzverteilung();
    const newData = [];
    const newLabels = [];
    const newColors = [];
    this.sitzverteilung$.subscribe(liste => {
      let parteiMandat;
      for (parteiMandat of liste) {
        newLabels.push(parteiMandat.partei.kuerzel + ' (' + parteiMandat.partei.name + ')');
        newData.push(parteiMandat.anzahlMandate);
        newColors.push(parteiMandat.partei.farbe);
      }
    });

    this.pieChartLabels = newLabels;
    this.pieChartData = newData;
    this.pieChartColors = [{backgroundColor: newColors}];

    this.backendService.getBundestagMitglieder()
      .subscribe(kandidaten => {
        let kandidat;
        const parteien = [];
        for (kandidat of kandidaten) {
          if (parteien.indexOf(kandidat.partei.kuerzel) < 0) {
            parteien.push(kandidat.partei.kuerzel);
          }
        }
        this.parteien = parteien;
        this.mitgliederBundestag = kandidaten;
        this.onClickPartei(parteien[0]);
      });

  }

  onClickPartei(partei: string): void {
    this.mitgliederBundestagFiltered = this.mitgliederBundestag.filter(kandidat => kandidat.partei.kuerzel === partei);
  }
}
