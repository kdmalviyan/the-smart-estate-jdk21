import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectChangeComponent } from './project-change.component';

describe('ProjectChangeComponent', () => {
  let component: ProjectChangeComponent;
  let fixture: ComponentFixture<ProjectChangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectChangeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
