import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Stimmabgabe2Component } from './stimmabgabe2.component';

describe('Stimmabgabe2Component', () => {
  let component: Stimmabgabe2Component;
  let fixture: ComponentFixture<Stimmabgabe2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Stimmabgabe2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Stimmabgabe2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
