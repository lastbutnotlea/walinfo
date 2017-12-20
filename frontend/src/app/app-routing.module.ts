import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HomeComponent} from './home/home.component';
import {RouterModule, Routes} from '@angular/router';
import {BundestagComponent} from './bundestag/bundestag.component';
import {WahlkreiseComponent} from './wahlkreise/wahlkreise.component';
import {WahlkreisDetailsComponent} from './wahlkreis-details/wahlkreis-details.component';
import {WeitereAnalysenComponent} from './weitere-analysen/weitere-analysen.component';
import {StimmabgabeComponent} from './stimmabgabe/stimmabgabe.component';
import {TokensComponent} from './tokens/tokens.component';
import {Stimmabgabe2Component} from './stimmabgabe2/stimmabgabe2.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'btg', component: BundestagComponent },
  { path: 'wahlkreise', component: WahlkreiseComponent},
  { path: 'wahlkreis/:nummer', component: WahlkreisDetailsComponent},
  { path: 'sonstiges', component: WeitereAnalysenComponent },
  { path: 'stimmabgabe', component: StimmabgabeComponent },
  { path: 'tokens', component: TokensComponent },
  { path: 'stimmabgabe2/:token', component: Stimmabgabe2Component }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
