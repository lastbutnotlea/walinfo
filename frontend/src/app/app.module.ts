import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MenuComponent } from './menu/menu.component';
import { HomeComponent } from './home/home.component';
import { BundestagComponent } from './bundestag/bundestag.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatCardModule} from '@angular/material/card';
import { BackendService } from './backend.service';
import {MatListModule} from '@angular/material/list';
import {HttpClientModule} from '@angular/common/http';
import {MatGridListModule} from '@angular/material/grid-list';
import { ChartsModule } from 'ng2-charts';
import { WahlkreiseComponent } from './wahlkreise/wahlkreise.component';
import { WahlkreisDetailsComponent } from './wahlkreis-details/wahlkreis-details.component';
import { WeitereAnalysenComponent } from './weitere-analysen/weitere-analysen.component';
import { StimmabgabeComponent } from './stimmabgabe/stimmabgabe.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { TokensComponent } from './tokens/tokens.component';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import { Stimmabgabe2Component } from './stimmabgabe2/stimmabgabe2.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    HomeComponent,
    BundestagComponent,
    WahlkreiseComponent,
    WahlkreisDetailsComponent,
    WeitereAnalysenComponent,
    StimmabgabeComponent,
    TokensComponent,
    Stimmabgabe2Component
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatSlideToggleModule,
    MatCardModule,
    MatListModule,
    HttpClientModule,
    MatGridListModule,
    ChartsModule,
    NgbModule.forRoot(),
    AngularFontAwesomeModule,
    FormsModule
  ],
  providers: [BackendService],
  bootstrap: [AppComponent]
})
export class AppModule { }
