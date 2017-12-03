import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WeitereAnalysenComponent } from './weitere-analysen.component';

describe('WeitereAnalysenComponent', () => {
  let component: WeitereAnalysenComponent;
  let fixture: ComponentFixture<WeitereAnalysenComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WeitereAnalysenComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WeitereAnalysenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
