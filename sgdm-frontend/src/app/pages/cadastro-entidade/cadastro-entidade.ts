import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { GoogleMapsModule } from '@angular/google-maps';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { EntidadeService } from '../../services/entidade';
import { CadastroEntidadeDTO } from '../../models/entidade';

@Component({
  selector: 'app-cadastro-entidade',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, GoogleMapsModule, NgxMaskDirective, FormsModule],
  providers: [provideNgxMask()],
  templateUrl: './cadastro-entidade.html',
  styleUrls: ['./cadastro-entidade.css']
})
export class CadastroEntidadeComponent implements OnInit {
  cadastroForm!: FormGroup;
  isSubmitting = false;

  // Configuração do mapa
  mapCenter: google.maps.LatLngLiteral = { lat: -23.5505, lng: -46.6333 };
  mapZoom = 13;
  mapOptions: google.maps.MapOptions = {
    mapTypeId: 'roadmap',
    disableDefaultUI: false,
    zoomControl: true
  };
  markerPosition: google.maps.LatLngLiteral | null = null;
  enderecoSearch = '';

  constructor(
    private fb: FormBuilder,
    private entidadeService: EntidadeService
  ) {}

  ngOnInit(): void {
    this.cadastroForm = this.fb.group({
      razaoSocial: ['', [Validators.required, Validators.minLength(3)]],
      nomeFantasia: ['', Validators.required],
      cnpj: ['', [Validators.required]],
      nomeFarmaceutico: ['', Validators.required],
      cpfFarmaceutico: ['', [Validators.required]],
      numCrf: ['', Validators.required],
      aceitaValidadeCurta: [false],
      horarioAbertura: ['07:30', Validators.required],
      horarioFechamento: ['19:30', Validators.required],
      latitude: [null, Validators.required],
      longitude: [null, Validators.required]
    });
  }

  onMapClick(event: google.maps.MapMouseEvent): void {
    if (event.latLng) {
      this.markerPosition = event.latLng.toJSON();
      this.cadastroForm.patchValue({
        latitude: this.markerPosition.lat,
        longitude: this.markerPosition.lng
      });

      console.log('📍 Posição capturada:', this.markerPosition);
      console.log('📋 Formulário após click:', this.cadastroForm.value);
    }
  }

  buscarEndereco(): void {
    if (!this.enderecoSearch) return;

    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ address: this.enderecoSearch }, (results, status) => {
      if (status === 'OK' && results && results[0]) {
        const location = results[0].geometry.location;
        this.mapCenter = location.toJSON();
        this.markerPosition = this.mapCenter;
        this.cadastroForm.patchValue({
          latitude: this.mapCenter.lat,
          longitude: this.mapCenter.lng
        });
      } else {
        alert('Endereço não encontrado!');
      }
    });
  }

  onSubmit(): void {
    if (this.cadastroForm.invalid) {
      alert('Preencha todos os campos obrigatórios!');
      this.cadastroForm.markAllAsTouched();
      return;
    }

    const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado') || '{}');
    if (!usuarioLogado.id) {
      alert('Você precisa estar logado para cadastrar uma entidade!');
      return;
    }

    this.isSubmitting = true;
    const formValue = this.cadastroForm.value;

    const entidade: CadastroEntidadeDTO = {
      razaoSocial: formValue.razaoSocial,
      nomeFantasia: formValue.nomeFantasia,
      cnpj: formValue.cnpj,
      nomeFarmaceutico: formValue.nomeFarmaceutico,
      cpfFarmaceutico: formValue.cpfFarmaceutico,
      numCrf: formValue.numCrf,
      horarioFuncionamento: `${formValue.horarioAbertura} - ${formValue.horarioFechamento}`,
      aceitaValidadeCurta: formValue.aceitaValidadeCurta,
      latitude: formValue.latitude,
      longitude: formValue.longitude,
      idDonoEntidade: usuarioLogado.id
    };

    console.log('📤 Enviando para API:', entidade);

    this.entidadeService.cadastrar(entidade).subscribe({
      next: (response) => {
        console.log('✅ Entidade cadastrada com sucesso:', response);
        alert('Entidade cadastrada com sucesso! Aguardando aprovação.');
        this.isSubmitting = false;

        this.entidadeService.atualizar();

        this.cadastroForm.reset({
          aceitaValidadeCurta: false,
          horarioAbertura: '07:30',
          horarioFechamento: '19:30'
        });
        this.markerPosition = null;
      },
      error: (error) => {
        console.error('❌ Erro ao cadastrar entidade:', error);
        alert('Erro ao cadastrar entidade. Verifique os dados e tente novamente.');
        this.isSubmitting = false;
      }
    });
  }
}
