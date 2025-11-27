export interface Usuario {
  id?: number;
  nomeCompleto: string;
  cpf: string;
  email: string;
  telefone?: string;
  senhaHash: string;
  dataCadastro?: Date;
  ultimoAcesso?: Date;
  cep: string;
  logradouro: string;
  numero: string;
  bairro: string;
  municipio: string;
  estado: string;
  status?: string;
}

export interface EnderecoViaCep {
  cep: string;
  logradouro: string;
  complemento: string;
  bairro: string;
  localidade: string;
  uf: string;
  erro?: boolean;
}
