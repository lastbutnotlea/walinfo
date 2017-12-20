import {Component, OnInit} from '@angular/core';
import {BackendService} from '../backend.service';
import {Observable} from 'rxjs/Observable';
import UeberhangMandateBundesland from '../templates/UeberhangMandateBundesland';
import KnappsterSieger from '../templates/KnappsterSieger';
import ParteiMandate from '../templates/ParteiMandate';

@Component({
  selector: 'app-weitere-analysen',
  templateUrl: './weitere-analysen.component.html',
  styleUrls: ['./weitere-analysen.component.css']
})
export class WeitereAnalysenComponent implements OnInit {

  ueberhangsmandate$: Observable<UeberhangMandateBundesland[]>;

  frauenBonus$: Observable<number>;

  year: String;

  knappsterSiegerParteien: string[];
  knappsteSieger: KnappsterSieger[];
  knappsteSiegerFiltered: KnappsterSieger[];

  erstStimmenData: number[] = [];
  zweitStimmenData: number[] = [];

  erstStimmenLabels: string[] = [];
  zweitStimmenLabels: string[] = [];

  erstStimmen$: Observable<ParteiMandate[]>;
  zweitStimmen$: Observable<ParteiMandate[]>;
  gesamtZweit: number;
  gesamtErst: number;

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

    this.frauenBonus$ = this.backendService.getFrauenBonus2017();

    this.year = this.backendService.year;

    this.knappsteSiegerFiltered = [];
    this.knappsterSiegerParteien = [];
    this.backendService.getKnappsteSieger().subscribe(eintraege => {
      let eintrag;
      const parteien = [];
      for (eintrag of eintraege) {
        if (parteien.indexOf(eintrag.abgeordneter.partei.kuerzel) < 0) {
          parteien.push(eintrag.abgeordneter.partei.kuerzel);
        }
      }
      this.knappsterSiegerParteien = parteien;
      this.knappsteSieger = eintraege;
      this.onClickPartei(parteien[0]);
    });

    this.erstStimmenData = undefined;
    this.erstStimmen$ = this.backendService.getErststimmen();
    this.erstStimmen$.subscribe(parteien => {
      this.gesamtErst = 0;
      const erstStimmenData = [];
      const erstStimmenLabels: string[] = [];
      let parteiMandat;
      for (parteiMandat of parteien) {
        erstStimmenLabels.push(parteiMandat.partei.kuerzel);
        this.gesamtErst += parteiMandat.anzahlAbsolut;
        erstStimmenData.push(parteiMandat.anzahlAbsolut);
      }
      this.erstStimmenLabels = erstStimmenLabels;
      this.erstStimmenData = erstStimmenData;
    });

    this.zweitStimmenData = undefined;
    this.zweitStimmen$ = this.backendService.getZweitstimmen();
    this.zweitStimmen$.subscribe(parteien => {
      this.gesamtZweit = 0;
      const zweitStimmenData = [];
      const zweitStimmenLabels = [];
      let parteiMandat;
      for (parteiMandat of parteien) {
        this.gesamtZweit += parteiMandat.anzahlAbsolut;
        zweitStimmenLabels.push(parteiMandat.partei.kuerzel);
        zweitStimmenData.push(parteiMandat.anzahlAbsolut);
      }
      this.zweitStimmenLabels = zweitStimmenLabels;
      this.zweitStimmenData = zweitStimmenData;
    });
  }

  onClickPartei(partei: string): void {
    this.knappsteSiegerFiltered = this.knappsteSieger.filter(kandidat => kandidat.abgeordneter.partei.kuerzel === partei);
  }
}
