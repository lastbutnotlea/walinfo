import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class BackendService {

  getStuff (): Observable<String> {
    return of('asd');
  }

  constructor(private http: HttpClient) { }

}
