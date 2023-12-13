import { TestBed } from '@angular/core/testing';

import { VacationManagementService } from './vacation-management.service';

describe('VacationManagementService', () => {
  let service: VacationManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VacationManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
