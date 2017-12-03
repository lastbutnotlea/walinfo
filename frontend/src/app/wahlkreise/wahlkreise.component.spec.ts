import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WahlkreiseComponent } from './wahlkreise.component';

describe('WahlkreiseComponent', () => {
  let component: WahlkreiseComponent;
  let fixture: ComponentFixture<WahlkreiseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WahlkreiseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WahlkreiseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
