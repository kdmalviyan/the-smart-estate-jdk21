import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRawLeadComponent } from './add-raw-lead.component';

describe('AddRawLeadComponent', () => {
  let component: AddRawLeadComponent;
  let fixture: ComponentFixture<AddRawLeadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddRawLeadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRawLeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
