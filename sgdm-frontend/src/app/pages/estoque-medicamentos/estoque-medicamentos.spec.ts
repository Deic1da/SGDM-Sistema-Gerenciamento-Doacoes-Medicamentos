import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstoqueMedicamentos } from './estoque-medicamentos';

describe('EstoqueMedicamentos', () => {
  let component: EstoqueMedicamentos;
  let fixture: ComponentFixture<EstoqueMedicamentos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EstoqueMedicamentos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstoqueMedicamentos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
