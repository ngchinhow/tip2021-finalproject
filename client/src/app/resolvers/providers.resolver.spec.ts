import { TestBed } from '@angular/core/testing';

import { ProvidersResolver } from './providers.resolver';

describe('ProvidersResolver', () => {
  let resolver: ProvidersResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ProvidersResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
