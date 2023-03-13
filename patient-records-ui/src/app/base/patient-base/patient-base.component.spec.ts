import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientBaseComponent } from './patient-base.component';

describe('PatientBaseComponent', () => {
  let component: PatientBaseComponent;
  let fixture: ComponentFixture<PatientBaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PatientBaseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientBaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
