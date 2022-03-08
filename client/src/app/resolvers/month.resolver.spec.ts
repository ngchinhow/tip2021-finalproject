import { TestBed } from '@angular/core/testing';

import { MonthResolver } from './month.resolver';

describe('MonthResolver', () => {
  let resolver: MonthResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(MonthResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
