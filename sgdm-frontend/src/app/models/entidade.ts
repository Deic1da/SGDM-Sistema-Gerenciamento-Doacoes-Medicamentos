export interface Entidade {
  id: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  latitude: number;
  longitude: number;
  idDonoEntidade?: number;
  horarioFuncionamento: string;
  aceitaValidadeCurta: boolean;
  distanciaMetros?: number;
  duracaoSegundos?: number;
  distanciaTexto?: string;
  duracaoTexto?: string;
  status?: string;
  dataCadastro?: Date;
}

export interface CadastroEntidadeDTO {
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  nomeFarmaceutico: string;
  cpfFarmaceutico: string;
  numCrf: string;
  horarioFuncionamento: string;
  aceitaValidadeCurta: boolean;
  latitude: number;
  longitude: number;
  idDonoEntidade?: number;
}

export interface Farmaceutico {
  id?: number;
  nomeFarmaceutico: string;
  cpfFarmaceutico: string;
  numCrf: string;
  ufCrf?: string;
  statusProfissional?: string;
}
