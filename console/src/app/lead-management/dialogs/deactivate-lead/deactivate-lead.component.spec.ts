import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeactivateLeadComponent } from './deactivate-lead.component';

describe('DeactivateLeadComponent', () => {
  let component: DeactivateLeadComponent;
  let fixture: ComponentFixture<DeactivateLeadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeactivateLeadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeactivateLeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
