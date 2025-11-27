export interface MedicamentoEstoque {
  id: number;
  nomeMedicamento: string;
  forma: string; // Comprimido, Cápsula, Xarope
  dataValidade: string;
  status: 'DISPONIVEL' | 'RESERVADO' | 'ENTREGUE' | 'DESCARTADO';
  cpfDestinatario?: string;
  motivoDescarte?: string;
  foraValidade?: boolean;
}

export interface RegistroEntrega {
  medicamentoId: number;
  cpfDestinatario: string;
}

export interface RegistroDescarte {
  medicamentoId: number;
  motivoDescarte: string;
  foraValidade: boolean;
}
