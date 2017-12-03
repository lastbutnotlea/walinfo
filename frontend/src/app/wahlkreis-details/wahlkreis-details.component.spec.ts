import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WahlkreisDetailsComponent } from './wahlkreis-details.component';

describe('WahlkreisDetailsComponent', () => {
  let component: WahlkreisDetailsComponent;
  let fixture: ComponentFixture<WahlkreisDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WahlkreisDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WahlkreisDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
