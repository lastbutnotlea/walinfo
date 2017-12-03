import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HomeComponent} from './home/home.component';
import {RouterModule, Routes} from '@angular/router';
import {BundestagComponent} from './bundestag/bundestag.component';
import {WahlkreiseComponent} from './wahlkreise/wahlkreise.component';
import {WahlkreisDetailsComponent} from './wahlkreis-details/wahlkreis-details.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'btg', component: BundestagComponent },
  { path: 'wahlkreise', component: WahlkreiseComponent},
  { path: 'wahlkreis/:id', component: WahlkreisDetailsComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
