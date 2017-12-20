import { Component, OnInit } from '@angular/core';
import {BackendService} from '../backend.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  show: boolean;

  constructor(private backendService: BackendService) { }

  ngOnInit() {
  }

  typeChanged(value: String) {
    this.backendService.changeDataType(value);
  }

  jahrChanged(value: String) {
    this.backendService.changeYear(value);
  }

  toggleNavbar(): void {
    this.show = !this.show;
  }
}
