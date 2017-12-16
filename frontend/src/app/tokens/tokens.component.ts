import { Component, OnInit } from '@angular/core';
import Wahlkreis from '../templates/Wahlkreis';
import {Observable} from 'rxjs/Observable';
import {BackendService} from '../backend.service';
import Token from '../templates/Token';

@Component({
  selector: 'app-tokens',
  templateUrl: './tokens.component.html',
  styleUrls: ['./tokens.component.css']
})
export class TokensComponent implements OnInit {

  private wahlkreise$: Observable<Wahlkreis[]>;

  private wahlkreis: Wahlkreis;

  private tokens$: Observable<Token[]>;

  constructor(private backendService: BackendService) { }

  ngOnInit() {
    this.wahlkreise$ = this.backendService.getWahlkreiseYear('2017');
  }

  wahlkreiseSelected(): void {
    if (typeof this.wahlkreis === 'object') {
      this.tokens$ = this.backendService.getTokens2017(this.wahlkreis.nummer);
    } else {
      this.wahlkreis = undefined;
    }
  }

  handleGenerate(): void {
    this.backendService.generateTokens(this.wahlkreis.nummer, 10)
      .subscribe(res => this.wahlkreiseSelected(), res => this.wahlkreiseSelected());
  }
}
