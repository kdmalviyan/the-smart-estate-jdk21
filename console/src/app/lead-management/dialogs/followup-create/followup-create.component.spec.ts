import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowupCreateComponent } from './followup-create.component';

describe('FollowupCreateComponent', () => {
  let component: FollowupCreateComponent;
  let fixture: ComponentFixture<FollowupCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FollowupCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FollowupCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
