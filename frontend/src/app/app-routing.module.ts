import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HomeComponent} from './home/home.component';
import {RouterModule, Routes} from '@angular/router';
import {BundestagComponent} from './bundestag/bundestag.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'btg', component: BundestagComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
