import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RawLeadManagementComponent } from './raw-lead-management.component';

describe('RawLeadManagementComponent', () => {
  let component: RawLeadManagementComponent;
  let fixture: ComponentFixture<RawLeadManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RawLeadManagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RawLeadManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
