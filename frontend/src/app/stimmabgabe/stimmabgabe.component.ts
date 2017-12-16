import { Component, OnInit } from '@angular/core';
import {BackendService} from '../backend.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-stimmabgabe',
  templateUrl: './stimmabgabe.component.html',
  styleUrls: ['./stimmabgabe.component.css']
})
export class StimmabgabeComponent implements OnInit {

  private bereitsVerbraucht: boolean;
  private ungueltig: boolean;

  constructor(
    private backendService: BackendService,
    private router: Router) { }

  ngOnInit() {
  }

  handleTokenSubmit(token: string): void {
    this.backendService.checkToken(token)
      .subscribe(res => {
        this.ungueltig = !res.valid;
        this.bereitsVerbraucht = res.used;
        if (!res.used && res.valid) {
          this.router.navigate(['/stimmabgabe2', token]);
        }
      });
  }

}
