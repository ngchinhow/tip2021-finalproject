import { TestBed } from '@angular/core/testing';

import { DayResolver } from './day.resolver';

describe('DayResolver', () => {
  let resolver: DayResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(DayResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
