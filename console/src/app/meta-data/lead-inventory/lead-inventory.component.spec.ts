import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadInventoryComponent } from './lead-inventory.component';

describe('LeadInventoryComponent', () => {
  let component: LeadInventoryComponent;
  let fixture: ComponentFixture<LeadInventoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeadInventoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadInventoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
