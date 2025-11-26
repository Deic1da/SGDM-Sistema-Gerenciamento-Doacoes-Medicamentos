CREATE TYPE ativo_inativo AS ENUM ('Ativo', 'Inativo');


--Tabela Usuarios (Pessoa Física)--
create table usuarios(                   
--id: Integer ou UUID, PK (Chave Primária), auto-incremento ou identificador único.
	id serial primary key, 
--nome_completo: String (255), obrigatório (NOT NULL).
	nome_completo varchar(255) not null,
--cpf: String (11), obrigatório (NOT NULL), único. Validado no sistema.
	cpf char(11) not null unique,
--email: String (255), obrigatório (NOT NULL), único. Validado no sistema.
	email varchar(255) not null unique,
--telefone: String (15), opcional. Formato padrão telefone Brasileiro.
	telefone char(15),
--senha_hash: String (255), obrigatório (NOT NULL). Senha criptografada!
	senha_hash varchar(255) not null,
--status: Enum ('Ativo', 'Inativo'), obrigatório. Para exclusão lógica/histórico.
	status ativo_inativo default 'Ativo',
--data_cadastro: Timestamp, obrigatório.
	data_cadastro TIMESTAMPTZ default now() not null,
--ultimo_acesso: Timestamp, obrigatório.
	ultimo_acesso TIMESTAMPTZ,
--cep: String (9), obrigatório.
	cep char(9) not null,
--logradouro: String (255), obrigatório.
	logradouro varchar(255) not null,
--numero: String (20), obrigatório.
	numero varchar(20) not null,
--bairro: String (100), obrigatório.
	bairro varchar(100) not null,
--municipio: String (100), obrigatório.
	municipio varchar(100) not null,
--estado: String (2), obrigatório.
	estado char(2) not null
);

create index email on usuarios using hash(email);


CREATE TYPE estado_crf AS ENUM ('AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO');
CREATE TYPE status_farmaceutico AS ENUM ('Ativo', 'Suspenso', 'Inativo');

create table farmaceuticos (                
--id: Integer ou UUID, PK (Chave Primária), auto-incremento ou identificador único.
	id serial primary key,
--id_usuario_pf: Integer ou UUID, FK obrigatória referenciando Usuarios.id.
	id_usuario_pf INTEGER REFERENCES usuarios(id) unique,
--num_crf: String (20), obrigatório (NOT NULL), único. Número do CRF registrado no Conselho Federal de Farmácia.
	num_crf char (20) not null unique,
--uf_crf: Enum, obrigatório. (Exemplo: 'AC', 'AL', ... 'SP', 'TO'). Unidade Federativa do CRF.
	uf_crf estado_crf not null,
--status_profissional: Enum ('Ativo', 'Suspenso', 'Inativo'), obrigatório.
	status_profissional status_farmaceutico not null
);
           

CREATE TYPE status_entidade AS ENUM ('Pendente', 'Aprovado', 'Reprovado', 'Inativo');

--Tabela Entidades (Pontos de Coleta)--
create table entidades(                                        
--id: Integer ou UUID, PK (Chave Primária), auto-incremento ou identificador único.
	id serial primary key,
--razao_social: String (255), obrigatório (NOT NULL).
	razao_social Varchar(255) not null,
--nome_fantasia: String (255), opcional.
	nome_fantasia Varchar (255),
--cnpj: String (18), obrigatório (NOT NULL), único. (Formato: xx.xxx.xxx/xxxx-xx). Validado no sistema.
	cnpj char(18) not null unique,
--id_dono_entidade: Integer ou UUID, FK obrigatória referenciando Usuarios.id (dono do ponto de coleta).
	id_dono_entidade integer REFERENCES usuarios(id),
--horario_funcionamento: String (100), obrigatório (NOT NULL).
	horario_funcionamento Varchar(100) not null,
--aceita_validade_curta: Boolean, obrigatório (NOT NULL). True se aceitar medicamentos com validade até 7 dias.
	aceita_validade_curta BOOLEAN not null,
--status: Enum ("Pendente", "Aprovado", "Reprovado", "Inativo"), obrigatório (NOT NULL). A entidade só aparece no mapa se o status for "Aprovado".
	status status_entidade default 'Pendente' not null,
--data_cadastro: Timestamp, obrigatório (NOT NULL).
	data_cadastro TIMESTAMPTZ default now() not null,
-- latitude/longitude
	latitude DOUBLE PRECISION not null ,
	longitude DOUBLE PRECISION not null,
--faramaceutico responsavel
	farmaceutico_rt INTEGER  REFERENCES farmaceuticos(id) unique
);


CREATE TYPE vinculo AS ENUM ('Responsavel_Tecnico', 'Farmaceutico_Normal');
CREATE TYPE status_vinculo AS ENUM ('Pendente', 'Aceito', 'Removido');

--Tabela VinculosFarmaceutico (RBAC)--
create table vinculos_farmaceutico(                         
--id: Integer ou UUID, PK (Chave Primária).
	id_vinculo_farmaceutico serial primary key,
--id_entidade: Integer ou UUID, FK obrigatória referenciando Entidades.id.
	id_entidade integer references entidades(id),
--id_farmaceutico: Integer ou UUID, FK obrigatória referenciando Farmaceuticos.id.
	id_farmaceutico integer references farmaceuticos(id),
--tipo_vinculo: Enum ('Responsável Técnico', 'Farmacêutico Normal'), obrigatório (NOT NULL). Um RT é obrigatório para cada entidade.
	tipo_vinculo vinculo default 'Responsavel_Tecnico' not null,
--status_vinculo: Enum ('Pendente', 'Aceito', 'Removido'), obrigatório (NOT NULL).
	status_vinculo status_vinculo default 'Pendente' not null,
--data_inicio: Date, obrigatório (NOT NULL).
	date_inicio date not null,
--data_fim: Date, opcional. Preenchido apenas em caso de remoção ou substituição.
	data_fim date
);


CREATE TYPE tipo_operacao AS ENUM ('Validacao', 'Entrega', 'Alteracao', 'Cadastro');

--Tabela LogAuditoria (Rastreabilidade)--
create table log_auditoria(            
--id: Integer ou UUID, PK (Chave Primária).
	id serial primary key,
--id_ator_responsavel: Integer ou UUID, FK obrigatória referenciando Usuarios.id (quem realizou a operação).
	id_ator_responsavel integer references usuarios(id),
--tipo_operacao: Enum (exemplo: 'Validação', 'Entrega', 'Alteração', 'Cadastro_Medicamento', etc.), obrigatório (NOT NULL).
	tipo_operacao tipo_operacao default 'Validacao' not null,
--registro_afetado: String (50), obrigatório (NOT NULL). O ID do item ou registro afetado (ex: ID do Medicamento, ID da Entidade).
	registro_afetado varchar(50) not null, 
--tabela_afetada: String (50), obrigatório (NOT NULL). Nome da tabela afetada (ex: Medicamentos, Entidades).
	tabela_afetada varchar (50) not null,
--descricao_alteracao: Text, opcional. Detalhes sobre o que foi modificado.
	descricao_alteracao text,
--data_hora: Timestamp, obrigatório (NOT NULL). Deve ser imutável, para garantir a trilha de auditoria.
	data_hora TIMESTAMPTZ default now() not null
);


CREATE TYPE status_doacao AS ENUM ('Cadastrado', 'Aprovado', 'Descartado');

--Tabela de MedicamentosDoação--
create table medicamentos_doacao(                   
--id: Integer ou UUID, PK (Chave Primária).
	id serial primary key,
--id_doador: Integer ou UUID, FK obrigatória referenciando Usuarios.id (quem está doando).
	id_doador integer references usuarios(id),
--nome_medicamento: String (255), obrigatório (NOT NULL).
	nome_medicamento Varchar(255) not null,
--forma_farmaceutica: String (100), obrigatório (NOT NULL).
	forma_farmaceutica Varchar(100) not null,
--condicao_embalagem: String (100), obrigatório (NOT NULL).
	condicao_embalagem Varchar(100) not null,
--data_validade: Date, obrigatório (NOT NULL).
	data_validade Date not null,
--status_doacao: Enum ('Cadastrado', 'Em_validacao', 'Aprovado', 'Rejeitado', 'Descartado'), obrigatório (NOT NULL).
	status_doacao status_doacao default 'Cadastrado' not null,
--data_cadastro: Timestamp, obrigatório (NOT NULL).
	data_cadastro TIMESTAMPTZ default now() not null,
--id_entidade_destino: Integer ou UUID, FK opcional para Entidades.id (entidade que vai receber a doação, preenchido após aprovação).
	id_entidade_destino integer references Entidades(id)
);
    

CREATE TYPE status_validacao AS ENUM ('Aprovado', 'Rejeitado', 'Em Processo');

--Tabela Validacoes--
create table validacoes(
--id: Integer ou UUID, PK (Chave Primária).
	id serial primary key,
--id_medicamento_doacao: Integer ou UUID, FK obrigatória referenciando MedicamentosDoacao.id.
	id_medicamento_doacao integer references medicamentos_doacao(id),
--id_farmaceutico_validante: Integer ou UUID, FK obrigatória referenciando Farmaceuticos.id (quem validou).
	id_farmaceutico_validante integer references Farmaceuticos(id),
--status_validacao: Enum ('Aprovado', 'Rejeitado'), obrigatório (NOT NULL).
	status_validacao status_validacao not null,
--motivo_rejeicao: Text, opcional; preenchido apenas se o status for "Rejeitado".
	motivo_rejeicao varchar(255),
--data_validacao: Timestamp, obrigatório (NOT NULL). Imutável, para trilha de auditoria.
	data_validacao TIMESTAMPTZ default now() not null
);
                       

CREATE TYPE status_estoque AS ENUM ('Disponível', 'Reservado', 'Entregue', 'Descartado');

--Tabela EstoqueEntidade--
create table estoque_entidade(
--id: Integer ou UUID, PK (Chave Primária).
	id serial primary key,
--id_entidade: Integer ou UUID, FK obrigatória referenciando Entidades.id.
	id_entidade integer references Entidades(id),
--id_validacao: Integer ou UUID, FK obrigatória referenciando Validacoes.id (item validado que entrou no estoque).
	id_validacao integer references Validacoes(id),
--quantidade: Integer, obrigatório (NOT NULL), quantidade disponível.
	quantidade integer not null,
--data_entrada: Timestamp, obrigatório (NOT NULL).
	data_entrada TIMESTAMPTZ default now() not null,
--status_estoque: Enum ('Disponível', 'Reservado', 'Entregue', 'Descartado'), obrigatório.
	status_estoque status_estoque not null
);


CREATE TYPE status_entrega AS ENUM ('Realizada', 'Cancelada');

--Tabela Entregas (Registro de Baixa)--
create table entregas(          
--id: Integer ou UUID, PK (Chave Primária).
	id serial primary key,
--id_estoque_entidade: Integer ou UUID, FK obrigatória referenciando EstoqueEntidade.id (que item foi entregue).
	id_estoque_entidade integer references estoque_entidade(id),
--id_farmaceutico_entregador: Integer ou UUID, FK obrigatória referenciando Farmaceuticos.id (quem entregou).
	id_farmaceutico_entregador integer references Farmaceuticos(id),
--id_receptor_pf: Integer ou UUID, FK obrigatória referenciando Usuarios.id (quem recebeu). Permite manter anonimato do doador.
	id_receptor_pf integer references Usuarios(id),
--data_entrega: Timestamp, obrigatório (NOT NULL).
	data_entrega TIMESTAMPTZ default now() not null,
--status_entrega: Enum ('Realizada', 'Cancelada'), obrigatório (NOT NULL).
	status_entrega status_entrega not null
);

CREATE TYPE destinos_finais  as ENUM ('incinerado','residuos_especiais','devolucao_fornecedor','local_autorizado','retorno_posto','autoclave','outro');

--Tabela Descarte--                  
create table descarte(
--id: Integer ou UUID, PK (Chave Primária).
	id serial primary key,
--id_medicamento_doacao: Integer ou UUID, FK obrigatória referenciando MedicamentosDoacao.id (identifica o medicamento descartado).
	id_medicamento_doacao integer references medicamentos_doacao(id),
--id_entidade: Integer ou UUID, FK obrigatória referenciando Entidades.id (quem efetuou o descarte).
	id_entidade integer references Entidades(id),
--id_farmaceutico_responsavel: Integer ou UUID, FK obrigatória referenciando Farmaceuticos.id (farmacêutico responsável pela operação).
	id_farmaceutico_responsavel integer references Farmaceuticos(id),
--motivo_descarte: String (255), obrigatório (NOT NULL). Exemplo: "Vencido", "Embalagem danificada", "Rejeitado na validação" etc.
	motivo_descarte varchar(255) not null,
--data_descarte: Timestamp, obrigatório (NOT NULL).
	data_descarte TIMESTAMPTZ default now() not null,
--destino_final: String (100), obrigatório (NOT NULL). Onde foi descartado (exemplos: incinerador, empresa de resíduos especiais, devolução a fornecedor).
	destino_final destinos_finais NOT NULL
);

-- Triggers --

CREATE OR REPLACE FUNCTION validade_medicamento_menor_data_atual()
RETURNS TRIGGER AS $$

BEGIN
    IF NEW.data_validade < CURRENT_DATE THEN
        RAISE EXCEPTION 'A data de validade, não pode ser menor que o dia atual.';
    END IF;
    RETURN NEW;
END;

$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validade_medicamento_menor_data_atual
BEFORE INSERT OR UPDATE ON medicamentos_doacao
FOR EACH ROW 
EXECUTE FUNCTION validade_medicamento_menor_data_atual();


-----------------------------------------------------------------------------------------------


-- ========================================
-- 1. CRIAR USUÁRIO DOADOR
-- ========================================
INSERT INTO usuarios (nome_completo, cpf, email, telefone, senha_hash, cep, logradouro, numero, bairro, municipio, estado)
VALUES (
    'Carlos Silva Doador',
    '11122233344',
    'carlos.doador@email.com',
    '82987654321',
    '$2a$10$abcdefghijklmnopqrstuvwxyz123456',
    '57000-000',
    'Rua do Comércio',
    '100',
    'Centro',
    'Maceió',
    'AL'
);

-- ========================================
-- 2. CRIAR USUÁRIO (DONO + FARMACÊUTICO)
-- ========================================
INSERT INTO usuarios (nome_completo, cpf, email, telefone, senha_hash, cep, logradouro, numero, bairro, municipio, estado)
VALUES (
    'Dra. Maria Farmacêutica Maceió',
    '99988877766',
    'maria.farm.maceio@email.com',
    '82912345678',
    '$2a$10$abcdefghijklmnopqrstuvwxyz123456',
    '57035-000',
    'Av. Fernandes Lima',
    '500',
    'Farol',
    'Maceió',
    'AL'
);

-- ========================================
-- 3. REGISTRAR COMO FARMACÊUTICO
-- ========================================
INSERT INTO farmaceuticos (id_usuario_pf, num_crf, uf_crf, status_profissional)
SELECT 
    u.id,
    '54321-AL',
    'AL',
    'Ativo'
FROM usuarios u
WHERE u.cpf = '99988877766';

-- ========================================
-- 4. CRIAR PONTO DE COLETA
-- ========================================
INSERT INTO entidades (
    razao_social, 
    nome_fantasia, 
    cnpj, 
    id_dono_entidade, 
    horario_funcionamento, 
    aceita_validade_curta, 
    status, 
    latitude, 
    longitude, 
    farmaceutico_rt
)
SELECT 
    'Farmácia Popular Maceió LTDA',
    'Farmácia Popular Maceió',
    '12.345.678/0001-90',
    u.id,
    'Seg a Sex: 08:00-18:00, Sáb: 08:00-12:00',
    true,
    'Aprovado',
    -9.6658,
    -35.7353,
    f.id
FROM usuarios u
INNER JOIN farmaceuticos f ON f.id_usuario_pf = u.id
WHERE u.cpf = '99988877766';

-- ========================================
-- 5. CRIAR VÍNCULO
-- ========================================
INSERT INTO vinculos_farmaceutico (id_entidade, id_farmaceutico, tipo_vinculo, status_vinculo, date_inicio)
SELECT 
    e.id,
    f.id,
    'Responsavel_Tecnico',
    'Aceito',
    CURRENT_DATE
FROM entidades e
INNER JOIN farmaceuticos f ON e.farmaceutico_rt = f.id
WHERE e.cnpj = '12.345.678/0001-90';

-- ========================================
-- 6. INSERIR 12 MEDICAMENTOS
-- ========================================

-- Med 1 - Validade 28/11/2025
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Dipirona 500mg', 'Comprimido', 'Lacrada', '2025-11-28', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 2 - Validade 28/11/2025
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Paracetamol 750mg', 'Comprimido', 'Lacrada', '2025-11-28', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 3
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Ibuprofeno 600mg', 'Comprimido', 'Lacrada', '2026-03-15', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 4
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Amoxicilina 500mg', 'Cápsula', 'Lacrada', '2026-01-20', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 5
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Loratadina 10mg', 'Comprimido', 'Lacrada', '2025-12-30', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 6
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Omeprazol 20mg', 'Cápsula', 'Lacrada', '2026-06-10', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 7
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Captopril 25mg', 'Comprimido', 'Lacrada', '2026-08-22', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 8
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Losartana 50mg', 'Comprimido', 'Lacrada', '2026-02-14', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 9
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Metformina 850mg', 'Comprimido', 'Lacrada', '2026-04-05', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 10
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Atorvastatina 20mg', 'Comprimido', 'Lacrada', '2025-12-15', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 11
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Azitromicina 500mg', 'Comprimido', 'Lacrada', '2026-07-18', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- Med 12
INSERT INTO medicamentos_doacao (id_doador, nome_medicamento, forma_farmaceutica, condicao_embalagem, data_validade, status_doacao, id_entidade_destino)
SELECT u.id, 'Cetirizina 10mg', 'Comprimido', 'Lacrada', '2026-05-25', 'Aprovado',
       (SELECT id FROM entidades WHERE cnpj = '12.345.678/0001-90')
FROM usuarios u WHERE u.cpf = '11122233344';

-- ========================================
-- 7. CRIAR VALIDAÇÕES APROVADAS
-- ========================================
INSERT INTO validacoes (id_medicamento_doacao, id_farmaceutico_validante, status_validacao)
SELECT 
    md.id,
    f.id,
    'Aprovado'
FROM medicamentos_doacao md
CROSS JOIN farmaceuticos f
INNER JOIN entidades e ON md.id_entidade_destino = e.id
WHERE e.cnpj = '12.345.678/0001-90'
  AND f.num_crf = '54321-AL';

-- ========================================
-- 8. INSERIR DIRETO NO ESTOQUE
-- ========================================
INSERT INTO estoque_entidade (id_entidade, id_validacao, quantidade, status_estoque)
SELECT 
    e.id,
    v.id,
    1,
    'Disponível'
FROM validacoes v
INNER JOIN medicamentos_doacao md ON v.id_medicamento_doacao = md.id
INNER JOIN entidades e ON md.id_entidade_destino = e.id
WHERE e.cnpj = '12.345.678/0001-90';

-- ========================================
-- 9. VERIFICAR RESULTADO
-- ========================================
SELECT 
    e.id as entidade_id,
    e.nome_fantasia,
    COUNT(ee.id) as total_medicamentos_estoque
FROM entidades e
LEFT JOIN estoque_entidade ee ON ee.id_entidade = e.id
WHERE e.cnpj = '12.345.678/0001-90'
GROUP BY e.id, e.nome_fantasia;

-- Listar os medicamentos
SELECT 
    ee.id,
    md.nome_medicamento,
    md.forma_farmaceutica,
    TO_CHAR(md.data_validade, 'DD/MM/YYYY') as validade,
    ee.status_estoque
FROM estoque_entidade ee
INNER JOIN validacoes v ON ee.id_validacao = v.id
INNER JOIN medicamentos_doacao md ON v.id_medicamento_doacao = md.id
INNER JOIN entidades e ON ee.id_entidade = e.id
WHERE e.cnpj = '12.345.678/0001-90'
ORDER BY md.data_validade;
