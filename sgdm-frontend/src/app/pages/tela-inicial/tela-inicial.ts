import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GoogleMapsModule, GoogleMap } from '@angular/google-maps';
import { Router } from '@angular/router';
import { EntidadeService } from '../../services/entidade';
import { Entidade } from '../../models/entidade';
import { MapaService } from '../../services/mapa';
import { AreaRTService } from '../../services/area-rt.service';

@Component({
  selector: 'app-tela-inicial',
  standalone: true,
  imports: [CommonModule, GoogleMapsModule],
  templateUrl: './tela-inicial.html',
  styleUrls: ['./tela-inicial.css'],
  providers: [EntidadeService]
})
export class TelaInicialComponent implements OnInit {
  center = { lat: -23.5505, lng: -46.6333 };
  zoom = 12;
  meuMarcador: { lat: number, lng: number } | null = null;
  usuarioLogado: any = null;
  pontosColeta: Entidade[] = [];
  selectedIndex: number | null = null;
  mapaCarregado = false;
  rotas: google.maps.LatLngLiteral[] = [];

  usuarioTemEntidade = false;
  usuarioEhRT = false;
  usuarioId: number | null = null;

  @ViewChild(GoogleMap, { static: false }) map!: GoogleMap;

  constructor(
    private entidadeService: EntidadeService,
    private cdr: ChangeDetectorRef,
    private mapaService: MapaService,
    private router: Router,
    public areaRTService: AreaRTService // Service que gerencia a lista RT
  ) {}

  fecharSeClicarFora() {
    if (this.areaRTService.getMostrarLista()) {
      this.areaRTService.fecharLista();
    }
  }


  onMapReady() {
    this.mapaCarregado = true;
  }

  ngOnInit(): void {
    // Pega o user logado do localStorage
    if (typeof window !== 'undefined' && window.localStorage) {
      const usuario = localStorage.getItem('usuarioLogado');
      if (usuario) {
        this.usuarioLogado = JSON.parse(usuario);
        this.usuarioId = this.usuarioLogado?.id || null;
      }
    }

    this.verificarEntidadeDoUsuario();
    this.verificarSeUsuarioEhRT();

    // Captura geolocalização
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          this.center = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          this.meuMarcador = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          this.zoom = 15;
          this.iniciarListaReativa();
        },
        (error) => {
          this.iniciarListaReativa();
        },
        { enableHighAccuracy: true }
      );
    } else {
      this.iniciarListaReativa();
    }
  }

  gerenciarPontoDeColeta(): void {
    if (!this.usuarioId) {
      alert('Você precisa estar logado!');
      return;
    }

    if (this.usuarioTemEntidade) {
      alert('Página de gerenciamento em desenvolvimento!');
      // this.router.navigate(['/gerenciar-entidade']);
    } else {
      this.router.navigate(['/cadastro-entidade']);
    }
  }

  verificarEntidadeDoUsuario(): void {
    if (this.usuarioId) {
      this.entidadeService.verificarSeUsuarioTemEntidade(this.usuarioId).subscribe({
        next: (temEntidade: boolean) => {
          setTimeout(() => {
            this.usuarioTemEntidade = temEntidade;
            console.log('👤 Usuário tem entidade?', temEntidade);
          });
        },
        error: (error: any) => {
          console.error('❌ Erro ao verificar entidade:', error);
        }
      });
    }
  }

  verificarSeUsuarioEhRT(): void {
    if (this.usuarioId) {
      this.entidadeService.verificarSeUsuarioEhRT(this.usuarioId).subscribe({
        next: (ehRT: boolean) => {
          setTimeout(() => {
            this.usuarioEhRT = ehRT;
            console.log('👨‍⚕️ Usuário é RT?', ehRT);
          });
        },
        error: (error: any) => {
          console.error('❌ Erro ao verificar RT:', error);
        }
      });
    }
  }

  areaDoFarmaceutico(): void {
    if (!this.usuarioId) {
      alert('Você precisa estar logado!');
      return;
    }
    if (this.usuarioEhRT) {
      if (this.areaRTService.getMostrarLista()) {
        this.areaRTService.fecharLista();
      } else {
        this.carregarEntidadesRT();
      }
    } else {
      alert('Cadastro de farmacêutico em desenvolvimento!');
    }
  }

  carregarEntidadesRT(): void {
    if (this.usuarioId) {
      this.entidadeService.listarEntidadesDoRT(this.usuarioId).subscribe({
        next: (entidades: Entidade[]) => {
          this.areaRTService.setEntidadesRT(entidades);
          this.areaRTService.setMostrarLista(true);
          console.log('📋 Entidades do RT:', entidades);
        },
        error: (error: any) => {
          console.error('❌ Erro ao carregar entidades do RT:', error);
          alert('Erro ao carregar suas entidades.');
        }
      });
    }
  }

  selecionarEntidadeRT(entidade: Entidade): void {
    console.log('🏥 Entidade selecionada:', entidade);
    this.areaRTService.fecharLista();

    // Navega para a página de estoque passando o ID da entidade
    this.router.navigate(['/estoque-medicamentos', entidade.id]);
  }


  iniciarListaReativa() {
    this.entidadeService.entidades$.subscribe(entidades => {
      if (!this.meuMarcador) {
        this.pontosColeta = entidades;
        this.selectedIndex = null;
        this.cdr.detectChanges();
        return;
      }

      const promises = entidades.map(ponto => {
        return new Promise<Entidade>((resolve) => {
          const service = new google.maps.DistanceMatrixService();
          service.getDistanceMatrix({
            origins: [this.meuMarcador as any],
            destinations: [{ lat: ponto.latitude, lng: ponto.longitude }],
            travelMode: google.maps.TravelMode.DRIVING,
            unitSystem: google.maps.UnitSystem.METRIC,
          }, (response, status) => {
            if (status === 'OK' && response?.rows?.[0]?.elements?.[0]?.status === 'OK') {
              ponto.distanciaMetros = response.rows[0].elements[0].distance.value;
              ponto.duracaoSegundos = response.rows[0].elements[0].duration.value;
              ponto.distanciaTexto = response.rows[0].elements[0].distance.text;
              ponto.duracaoTexto = response.rows[0].elements[0].duration.text;
            } else {
              ponto.distanciaMetros = Infinity;
              ponto.duracaoSegundos = Infinity;
              ponto.distanciaTexto = '';
              ponto.duracaoTexto = '';
            }
            resolve(ponto);
          });
        });
      });

      Promise.all(promises).then(pontosComDistancia => {
        this.pontosColeta = pontosComDistancia
          .sort((a: Entidade, b: Entidade) => (a.duracaoSegundos ?? Infinity) - (b.duracaoSegundos ?? Infinity));
        this.selectedIndex = null;
        this.cdr.detectChanges();
      });
    });
  }

  selecionarPonto(index: number) {
    this.selectedIndex = index;
    const ponto = this.pontosColeta[index];

    if (this.meuMarcador && this.map) {
      const bounds = new google.maps.LatLngBounds();
      bounds.extend(new google.maps.LatLng(this.meuMarcador.lat, this.meuMarcador.lng));
      bounds.extend(new google.maps.LatLng(ponto.latitude, ponto.longitude));
      this.map.fitBounds(bounds);
    }
  }

  abrirNoGoogleMaps(ponto: Entidade) {
    const origem = this.meuMarcador
      ? `${this.meuMarcador.lat},${this.meuMarcador.lng}`
      : "";
    const destino = `${ponto.latitude},${ponto.longitude}`;
    const url = `https://www.google.com/maps/dir/?api=1&origin=${origem}&destination=${destino}`;
    window.open(url, "_blank");
  }

  tracarRota(index: number) {
    if (!this.meuMarcador) return;
    const ponto = this.pontosColeta[index];
    this.mapaService.getRoute(
      this.meuMarcador,
      { lat: ponto.latitude, lng: ponto.longitude }
    ).then(result => {
      if (result) {
        this.rotas = result.routes[0].overview_path.map(
          ll => ({ lat: ll.lat(), lng: ll.lng() })
        );
        this.cdr.detectChanges();
      } else {
        this.rotas = [];
      }
    });
  }
}
