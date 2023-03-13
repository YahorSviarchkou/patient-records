import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorBaseComponent } from './doctor-base.component';

describe('DoctorBaseComponent', () => {
  let component: DoctorBaseComponent;
  let fixture: ComponentFixture<DoctorBaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoctorBaseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DoctorBaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
