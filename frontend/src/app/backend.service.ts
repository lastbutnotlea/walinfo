import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Subject} from 'rxjs/Subject';
import ParteiMandate from './templates/ParteiMandate';
import Kandidat from './templates/Kandidat';
import Wahlkreis from './templates/Wahlkreis';

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

  constructor(private http: HttpClient) { }

}
