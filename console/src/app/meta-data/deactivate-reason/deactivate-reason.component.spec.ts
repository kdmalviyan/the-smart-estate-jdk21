import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeactivateReasonComponent } from './deactivate-reason.component';

describe('DeactivateReasonComponent', () => {
  let component: DeactivateReasonComponent;
  let fixture: ComponentFixture<DeactivateReasonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeactivateReasonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeactivateReasonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
