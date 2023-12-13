import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVactionComponent } from './add-vaction.component';

describe('AddVactionComponent', () => {
  let component: AddVactionComponent;
  let fixture: ComponentFixture<AddVactionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddVactionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddVactionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
