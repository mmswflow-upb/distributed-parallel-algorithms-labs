import { TestBed } from '@angular/core/testing';

import { Tutorial2Service } from './tutorial2.service';

describe('Tutorial2Service', () => {
  let service: Tutorial2Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Tutorial2Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
