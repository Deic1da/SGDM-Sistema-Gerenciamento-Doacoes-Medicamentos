import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { UsuarioService } from '../../services/usuario';
import { ViaCepService } from '../../services/viacep';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro.html',
  styleUrls: ['./cadastro.css'],
})
export class CadastroComponent {
  // FORM DE CADASTRO
  cadastroForm: FormGroup;
  mensagemErro = '';
  mensagemSucesso = '';
  carregandoCep = false;
  mostrarSenha = false;
  mostrarConfirmarSenha = false;

  // FORM DE LOGIN
  loginForm: FormGroup;
  mostrarLoginSenha = false;
  loginErro = '';
  loginSucesso = '';

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private viaCepService: ViaCepService,
    private router: Router,
    private authService: AuthService
  ) {
    // FORM CADASTRO
    this.cadastroForm = this.fb.group({
      nomeCompleto: ['', [Validators.required]],
      cpf: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      telefone: [''],
      cep: ['', [Validators.required]],
      logradouro: ['', [Validators.required]],
      numero: ['', [Validators.required]],
      bairro: ['', [Validators.required]],
      municipio: ['', [Validators.required]],
      estado: ['', [Validators.required]],
      senhaHash: ['', [Validators.required]],
      confirmarSenha: ['', [Validators.required]],
    });

    // FORM LOGIN
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]],
    });

    this.observarCep();
  }

  // -------- CEP --------
  observarCep(): void {
    this.cadastroForm.get('cep')?.valueChanges.subscribe((cep) => {
      const somenteNumeros = cep?.replace(/\D/g, '') || '';
      if (somenteNumeros.length === 8) {
        this.buscarEnderecoPorCep(somenteNumeros);
      }
    });
  }

  buscarEnderecoPorCep(cep: string): void {
    this.carregandoCep = true;
    this.viaCepService.buscarEnderecoPorCep(cep).subscribe({
      next: (endereco) => {
        if (!endereco.erro) {
          this.cadastroForm.patchValue({
            logradouro: endereco.logradouro,
            bairro: endereco.bairro,
            municipio: endereco.localidade,
            estado: endereco.uf,
          });
          this.mensagemErro = '';
        } else {
          this.mensagemErro = 'CEP não encontrado.';
        }
        this.carregandoCep = false;
      },
      error: () => {
        this.mensagemErro = 'Erro ao buscar CEP.';
        this.carregandoCep = false;
      },
    });
  }

  // -------- CADASTRO --------
  onSubmit(): void {
    if (this.cadastroForm.invalid) {
      this.mensagemErro = 'Preencha todos os campos obrigatórios.';
      Object.values(this.cadastroForm.controls).forEach((c) =>
        c.markAsTouched()
      );
      return;
    }

    const { confirmarSenha, ...usuario } = this.cadastroForm.value;

    this.usuarioService.cadastrar(usuario).subscribe({
      next: () => {
        this.mensagemSucesso = 'Cadastro realizado com sucesso.';
        this.mensagemErro = '';
        // Se quiser já logar ou redirecionar, você decide depois
      },
      error: () => {
        this.mensagemErro = 'Erro ao cadastrar usuário.';
        this.mensagemSucesso = '';
      },
    });
  }

  // -------- LOGIN --------
  onLogin(): void {
    this.loginErro = '';
    this.loginSucesso = '';

    if (this.loginForm.invalid) {
      this.loginErro = 'Preencha e-mail e senha.';
      return;
    }

    const { email, senha } = this.loginForm.value;

    this.authService.login(email, senha).subscribe({
      next: (usuario) => {
        if (usuario) {
          this.loginSucesso = 'Login realizado com sucesso!';
          localStorage.setItem('usuarioLogado', JSON.stringify(usuario));
          this.router.navigate(['/tela-inicial']);


        } else {
          this.loginErro = 'Usuário ou senha inválidos.';
        }
      },
      error: () => {
        this.loginErro = 'Erro ao tentar autenticar.';
      },
    });
  }

  irParaLogin(): void {
    this.router.navigate(['/login']);
  }
}
