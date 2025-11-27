import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroEntidade } from './cadastro-entidade';

describe('CadastroEntidade', () => {
  let component: CadastroEntidade;
  let fixture: ComponentFixture<CadastroEntidade>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastroEntidade]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CadastroEntidade);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
