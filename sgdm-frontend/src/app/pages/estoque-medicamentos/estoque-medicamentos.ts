import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MedicamentoEstoque, RegistroEntrega, RegistroDescarte } from '../../models/medicamento-estoque';
import { EstoqueService } from '../../services/estoque';

@Component({
  selector: 'app-estoque-medicamentos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estoque-medicamentos.html',
  styleUrls: ['./estoque-medicamentos.css']
})
export class EstoqueMedicamentosComponent implements OnInit {
  medicamentos: MedicamentoEstoque[] = [];
  medicamentosFiltrados: MedicamentoEstoque[] = [];
  medicamentoSelecionado: MedicamentoEstoque | null = null;

  get itemFinal(): number {
    return Math.min(this.paginaAtual * this.itensPorPagina, this.totalItens);
  }

  // Paginação
  paginaAtual = 1;
  itensPorPagina = 8;
  totalPaginas = 0;
  totalItens = 0;

  // Filtro de busca
  termoBusca = '';

  // Formulários
  cpfDestinatario = '';
  motivoDescarte = '';
  foraValidade = false;

  entidadeId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private estoqueService: EstoqueService
  ) {}

  ngOnInit(): void {
    // Pega o ID da entidade da rota
    this.route.params.subscribe(params => {
      this.entidadeId = +params['id'];
      this.carregarMedicamentos();
    });
  }

  carregarMedicamentos(): void {
    if (this.entidadeId) {
      this.estoqueService.listarMedicamentos(this.entidadeId).subscribe({
        next: (medicamentos: any[]) => {
          // Converte os dados do backend para o formato esperado
          this.medicamentos = medicamentos.map(m => ({
            id: m.id,
            nomeMedicamento: m.nome_medicamento,
            forma: m.forma_farmaceutica,
            dataValidade: m.data_validade,
            status: this.converterStatus(m.status_estoque)
          }));
          this.aplicarFiltro();
          console.log('📦 Medicamentos carregados:', this.medicamentos.length);
        },
        error: (error) => {
          console.error('❌ Erro ao carregar medicamentos:', error);
          alert('Erro ao carregar medicamentos do estoque!');
          // Fallback para mock se necessário
          this.medicamentos = [];
          this.aplicarFiltro();
        }
      });
    }
  }

  private converterStatus(status: string): 'DISPONIVEL' | 'RESERVADO' | 'ENTREGUE' | 'DESCARTADO' {
    const mapa: any = {
      'Disponível': 'DISPONIVEL',
      'Reservado': 'RESERVADO',
      'Entregue': 'ENTREGUE',
      'Descartado': 'DESCARTADO'
    };
    return mapa[status] || 'DISPONIVEL';
  }

  aplicarFiltro(): void {
    if (this.termoBusca.trim() === '') {
      this.medicamentosFiltrados = [...this.medicamentos];
    } else {
      const termo = this.termoBusca.toLowerCase();
      this.medicamentosFiltrados = this.medicamentos.filter(med =>
        med.nomeMedicamento.toLowerCase().includes(termo) ||
        med.forma.toLowerCase().includes(termo)
      );
    }

    this.totalItens = this.medicamentosFiltrados.length;
    this.totalPaginas = Math.ceil(this.totalItens / this.itensPorPagina);
    this.paginaAtual = 1;
  }

  get medicamentosPaginados(): MedicamentoEstoque[] {
    const inicio = (this.paginaAtual - 1) * this.itensPorPagina;
    const fim = inicio + this.itensPorPagina;
    return this.medicamentosFiltrados.slice(inicio, fim);
  }

  get paginasVisiveis(): number[] {
    const paginas: number[] = [];
    const maxPaginas = 5;

    let inicio = Math.max(1, this.paginaAtual - 2);
    let fim = Math.min(this.totalPaginas, inicio + maxPaginas - 1);

    if (fim - inicio < maxPaginas - 1) {
      inicio = Math.max(1, fim - maxPaginas + 1);
    }

    for (let i = inicio; i <= fim; i++) {
      paginas.push(i);
    }
    return paginas;
  }

  irParaPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaAtual = pagina;
    }
  }

  selecionarMedicamento(medicamento: MedicamentoEstoque): void {
    if (medicamento.status === 'DISPONIVEL' || medicamento.status === 'RESERVADO') {
      this.medicamentoSelecionado = medicamento;
    }
  }

  registrarEntrega(): void {
    if (!this.medicamentoSelecionado || !this.cpfDestinatario) {
      alert('Selecione um medicamento e preencha o CPF do destinatário!');
      return;
    }

    const entrega: RegistroEntrega = {
      medicamentoId: this.medicamentoSelecionado.id,
      cpfDestinatario: this.cpfDestinatario
    };

    console.log('📦 Registrando entrega:', entrega);

    // TODO: Chamar API
    // this.estoqueService.registrarEntrega(entrega).subscribe(...)

    alert(`Entrega registrada para ${this.medicamentoSelecionado.nomeMedicamento}`);
    this.limparFormularios();
  }

  registrarDescarte(): void {
    if (!this.medicamentoSelecionado) {
      alert('Selecione um medicamento!');
      return;
    }

    if (!this.foraValidade && !this.motivoDescarte.trim()) {
      alert('Informe o motivo do descarte ou marque "Fora da Validade"!');
      return;
    }

    const descarte: RegistroDescarte = {
      medicamentoId: this.medicamentoSelecionado.id,
      motivoDescarte: this.foraValidade ? 'Fora da Validade' : this.motivoDescarte,
      foraValidade: this.foraValidade
    };

    console.log('🗑️ Registrando descarte:', descarte);

    // TODO: Chamar API
    // this.estoqueService.registrarDescarte(descarte).subscribe(...)

    alert(`Descarte registrado para ${this.medicamentoSelecionado.nomeMedicamento}`);
    this.limparFormularios();
  }

  limparFormularios(): void {
    this.medicamentoSelecionado = null;
    this.cpfDestinatario = '';
    this.motivoDescarte = '';
    this.foraValidade = false;
  }

  getStatusClass(status: string): string {
    const classes: any = {
      'DISPONIVEL': 'status-disponivel',
      'RESERVADO': 'status-reservado',
      'ENTREGUE': 'status-entregue',
      'DESCARTADO': 'status-descartado'
    };
    return classes[status] || '';
  }

  getStatusTexto(status: string): string {
    const textos: any = {
      'DISPONIVEL': 'Disponível',
      'RESERVADO': 'Reservado',
      'ENTREGUE': 'Entregue',
      'DESCARTADO': 'Descartado'
    };
    return textos[status] || status;
  }
}
