import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Subject} from 'rxjs/Subject';
import ParteiMandate from './templates/ParteiMandate';
import Kandidat from './templates/Kandidat';
import Wahlkreis from './templates/Wahlkreis';
import Wahlbeteiligung from './templates/Wahlbeteiligung';
import { catchError, map, tap } from 'rxjs/operators';
import StimmenPartei from './templates/StimmenPartei';
import WahlkreisParteiVergleich from './templates/WahlkreisParteiVergleich';
import Partei from './templates/Partei';
import UeberhangMandateBundesland from './templates/UeberhangMandateBundesland';

@Injectable()
export class BackendService {

  public updatePage: Subject<boolean> = new Subject();

  dataType: String = 'aggr';
  year: String = '2017';

  changeDataType(value: String) {
    this.dataType = value;
    this.updatePage.next();
  }

  changeYear(value: String) {
    this.year = value;
    this.updatePage.next();
  }

  getBundestagSitzverteilung (): Observable<ParteiMandate[]> {
    return this.http.get<ParteiMandate[]>('http://localhost:8080/bundestag/sitzverteilung?jahr='
      + this.year + '&modus=' + this.dataType);
  }

  getBundestagMitglieder(): Observable<Kandidat[]> {
    return this.http.get<Kandidat[]>('http://localhost:8080/bundestag/mitglieder?jahr='
      + this.year + '&modus=' + this.dataType);
  }

  getWahlkreise(): Observable<Wahlkreis[]> {
    return this.http.get<Wahlkreis[]>('http://localhost:8080/wahlkreise?jahr='
      + this.year);
  }

  getWahlkreis(nummer: string): Observable<Wahlkreis> {
    return this.http.get<Wahlkreis[]>('http://localhost:8080/wahlkreise/' + nummer + '?jahr='
      + this.year )
      .pipe(map(res => res[0]));
  }

  getWahlbeteiligung(wknr: string): Observable<Wahlbeteiligung> {
    return this.http.get<Wahlbeteiligung>('http://localhost:8080/wahlkreise/wahlbeteiligung' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getDirektkandidat(wknr: string): Observable<Kandidat> {
    return this.http.get<Kandidat>('http://localhost:8080/wahlkreise/direktmandat' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getStimmenProPartei(wknr: string): Observable<StimmenPartei[]> {
    return this.http.get<StimmenPartei[]>('http://localhost:8080/wahlkreise/stimmenpropartei' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getVergleichVorjahr(wknr: string): Observable<WahlkreisParteiVergleich[]> {
    return this.http.get<WahlkreisParteiVergleich[]>('http://localhost:8080/wahlkreise/vergleichvorjahr' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getWahlkreisSieger(wknr: string): Observable<Partei> {
    return this.http.get<Partei>('http://localhost:8080/wahlkreise/siegerzweitstimmen' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getUeberhangsmandate(): Observable<UeberhangMandateBundesland[]> {
    return this.http.get<UeberhangMandateBundesland[]>('http://localhost:8080/bundestag/ueberhangmandate' +
      '?jahr=' + this.year +
      '&modus=' + this.dataType);
  }

  constructor(private http: HttpClient) { }

}
