import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowupCommentComponent } from './followup-comment.component';

describe('FollowupCommentComponent', () => {
  let component: FollowupCommentComponent;
  let fixture: ComponentFixture<FollowupCommentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FollowupCommentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FollowupCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
