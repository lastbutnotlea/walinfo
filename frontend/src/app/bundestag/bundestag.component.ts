import {Component, OnInit, ViewChild} from '@angular/core';
import {BackendService} from '../backend.service';
import ParteiMandate from '../templates/ParteiMandate';
import {Observable} from 'rxjs/Observable';
import Kandidat from '../templates/Kandidat';

@Component({
  selector: 'app-bundestag',
  templateUrl: './bundestag.component.html',
  styleUrls: ['./bundestag.component.css']
})
export class BundestagComponent implements OnInit {

  sitzverteilung$: Observable<ParteiMandate[]>;
  mitgliederBundestag$: Observable<Kandidat[]>;

  pieChartLabels: string[] = ['12'];
  pieChartData: number[] = [2];
  pieChartOption = {
    rotation: 1 * Math.PI,
    circumference: 1 * Math.PI
  };
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
    this.sitzverteilung$.subscribe(liste => {
      let parteiMandat;
      for (parteiMandat of liste) {
        newLabels.push(parteiMandat.partei.kuerzel + ' (' + parteiMandat.partei.name + ')');
        newData.push(parteiMandat.anzahlMandate);
      }
    });

    this.pieChartLabels = newLabels;
    this.pieChartData = newData;

    this.mitgliederBundestag$ = this.backendService.getBundestagMitglieder();
  }

  onClickPartei(partei: String) {

  }

}
