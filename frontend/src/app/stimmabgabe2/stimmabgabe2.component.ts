import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BackendService} from '../backend.service';
import Wahlkreis from '../templates/Wahlkreis';
import Kandidat from '../templates/Kandidat';
import Partei from '../templates/Partei';

@Component({
  selector: 'app-stimmabgabe2',
  templateUrl: './stimmabgabe2.component.html',
  styleUrls: ['./stimmabgabe2.component.css']
})
export class Stimmabgabe2Component implements OnInit {

  private token: string;
  private wahlkreis: Wahlkreis;

  private kandidates: Kandidat[];
  private parteien: Partei[];

  private ausgewaehlteErststimme = -1;
  private ausgewaehlteZweitstimme = -1;

  private success = false;
  private error = false;


  constructor(private route: ActivatedRoute, private backendService: BackendService) {
  }

  ngOnInit() {
    this.token = this.route.snapshot.paramMap.get('token');
    this.backendService.checkToken(this.token)
      .subscribe(res => {
        this.reload(res.wknr);
      });
  }

  reload(wahlkreisNummer: number) {
    this.backendService.getWahlkreisYear(wahlkreisNummer, '2017')
      .subscribe(res => {
        this.wahlkreis = res;
      });
    this.backendService.getKandidatenWahlkreis(wahlkreisNummer)
      .subscribe(res => {
        this.kandidates = res;
      });
    this.backendService.getParteienWahlkreis(wahlkreisNummer)
      .subscribe(res => {
        this.parteien = res;
      });
  }

  handleWahl(): void {
    if ((this.ausgewaehlteErststimme > 0 && this.ausgewaehlteZweitstimme > 0)
      || confirm('Sie haben bei der Erst- oder Zweitstimme nichts ausgewählt, ' +
        'wenn sie jetzt abgeben wird die entsprechende Erst- oder Zweitstimme ' +
        'als ungültig erklärt.')) {
      this.backendService.stimmAbgabe(this.token, this.ausgewaehlteErststimme, this.ausgewaehlteZweitstimme)
        .subscribe(success => {
          if (success) {
            this.success = true;
          } else {
            this.error = true;
          }
        });
      console.log(this.ausgewaehlteErststimme + ' ' + this.ausgewaehlteZweitstimme);
    }
  }
}
