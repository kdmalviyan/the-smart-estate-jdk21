import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadTypeComponent } from './lead-type.component';

describe('LeadTypeComponent', () => {
  let component: LeadTypeComponent;
  let fixture: ComponentFixture<LeadTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeadTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
