import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntegrationFormDialogComponent } from './integration-form-dialog.component';

describe('IntegrationFormDialogComponent', () => {
  let component: IntegrationFormDialogComponent;
  let fixture: ComponentFixture<IntegrationFormDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntegrationFormDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntegrationFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
