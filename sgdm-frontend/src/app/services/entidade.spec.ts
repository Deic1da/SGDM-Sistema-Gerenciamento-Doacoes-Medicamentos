import { TestBed } from '@angular/core/testing';

import { Entidade } from './entidade';

describe('Entidade', () => {
  let service: Entidade;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Entidade);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
