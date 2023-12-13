import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UIPermissionsComponent } from './uipermissions.component';

describe('UIPermissionsComponent', () => {
  let component: UIPermissionsComponent;
  let fixture: ComponentFixture<UIPermissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UIPermissionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UIPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
