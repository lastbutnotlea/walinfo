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
import FrauenMaennerQuote from './templates/FrauenMaennerQuote';
import KnappsterSieger from './templates/KnappsterSieger';
import 'rxjs/add/observable/of';
import Token from './templates/Token';
import StimmenKandidat from './templates/StimmenKandidat';

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
    return this.getWahlkreiseYear(this.year);
  }

  getWahlkreiseYear(year: String): Observable<Wahlkreis[]> {
    return this.http.get<Wahlkreis[]>('http://localhost:8080/wahlkreise?jahr='
      + year);
  }

  getWahlkreisYear(nummer: number, year: String): Observable<Wahlkreis> {
    return this.http.get<Wahlkreis[]>('http://localhost:8080/wahlkreise/' + nummer + '?jahr='
      + year )
      .pipe(map(res => res[0]));
  }

  getWahlkreis(nummer: string): Observable<Wahlkreis> {
    return this.getWahlkreisYear(Number.parseInt(nummer), this.year);
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

  getErstStimmenProPartei(wknr: string): Observable<StimmenKandidat[]> {
    return this.http.get<StimmenKandidat[]>('http://localhost:8080/wahlkreise/erststimmenproabgeordneter' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getZweitStimmenProPartei(wknr: string): Observable<StimmenPartei[]> {
    return this.http.get<StimmenPartei[]>('http://localhost:8080/wahlkreise/zweitstimmenpropartei' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getVergleich2017ErstVorjahr(wknr: string): Observable<WahlkreisParteiVergleich[]> {
    return this.http.get<WahlkreisParteiVergleich[]>('http://localhost:8080/wahlkreise/vergleichvorjahr/erststimmen' +
      '?wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getVergleich2017ZweitVorjahr(wknr: string): Observable<WahlkreisParteiVergleich[]> {
    return this.http.get<WahlkreisParteiVergleich[]>('http://localhost:8080/wahlkreise/vergleichvorjahr/zweitstimmen' +
      '?wknr=' + wknr +
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

  getFrauenMaennerQuote(wknr: string): Observable<FrauenMaennerQuote> {
    return this.http.get<FrauenMaennerQuote>('http://localhost:8080/wahlkreise/frauenquote' +
      '?jahr=' + this.year +
      '&wknr=' + wknr +
      '&modus=' + this.dataType);
  }

  getKnappsteSieger(): Observable<KnappsterSieger[]> {
    return this.http.get<KnappsterSieger[]>('http://localhost:8080/knappstesieger' +
      '?jahr=' + this.year +
      '&modus=' + this.dataType);
  }

  getFrauenBonus2017(): Observable<number> {
    return this.http.get<number>('http://localhost:8080/frauenbonus' +
      '?modus=' + this.dataType);
  }

  getErststimmen(): Observable<ParteiMandate[]> {
    return this.http.get<ParteiMandate[]>('http://localhost:8080/erststimmen' +
      '?jahr=' + this.year +
      '&modus=' + this.dataType);
  }

  getZweitstimmen(): Observable<ParteiMandate[]> {
    return this.http.get<ParteiMandate[]>('http://localhost:8080/zweitstimmen' +
      '?jahr=' + this.year +
      '&modus=' + this.dataType);
  }

  getTokens2017(wknr: number): Observable<Token[]> {
    return this.http.get<Token[]>('http://localhost:8080/tokens/show' +
      '?wknr=' + wknr);
  }

  generateTokens(wknr: number, anzahl: number): Observable<Object> {
    return this.http.get('http://localhost:8080/tokens/generate' +
      '?wknr=' + wknr +
      '&anzahl=' + anzahl);
  }

  checkToken(token: string): Observable<Token> {
    return this.http.get<Token>('http://localhost:8080/waehlen/verify' +
      '?token=' + token);
  }

  constructor(private http: HttpClient) { }

}
