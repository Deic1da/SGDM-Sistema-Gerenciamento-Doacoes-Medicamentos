import { Routes } from '@angular/router';
import { CadastroComponent } from './pages/cadastro/cadastro';
import { TelaInicialComponent } from './pages/tela-inicial/tela-inicial';
import { CadastroEntidadeComponent } from './pages/cadastro-entidade/cadastro-entidade';
import { EstoqueMedicamentosComponent } from './pages/estoque-medicamentos/estoque-medicamentos';

export const routes: Routes = [
  { path: '', redirectTo: 'cadastro', pathMatch: 'full' },
  { path: 'cadastro', component: CadastroComponent },
  { path: 'tela-inicial', component: TelaInicialComponent },
  { path: 'cadastro-entidade', component: CadastroEntidadeComponent },
  { path: 'estoque-medicamentos/:id', component: EstoqueMedicamentosComponent }
];
