-- Script SQL para criar a tabela tb_cliente
CREATE TABLE tb_cliente (
    id INT PRIMARY KEY,
    nome VARCHAR(100),
    cpf BIGINT,
    tel BIGINT,
    endereco VARCHAR(255),
    numero INT,
    cidade VARCHAR(100),
    estado VARCHAR(2),
    email VARCHAR(100),
    idade INT
);

-- Script SQL para criar a tabela tb_venda
CREATE TABLE tb_venda (
    id INT PRIMARY KEY,
    codigo VARCHAR(100),
    id_cliente_fk INT,
    valor_total DECIMAL(10, 2),
    data_venda DATE,
    status_venda VARCHAR(50),
    FOREIGN KEY (id_cliente_fk) REFERENCES tb_cliente(id)
);

-- Script SQL para criar a tabela tb_produto
CREATE TABLE tb_produto (
    id INT PRIMARY KEY,
    codigo VARCHAR(100),
    nome VARCHAR(100),
    descricao VARCHAR(255),
    valor DECIMAL(10, 2),
    quantidade_estoque INT,
    data_criacao DATE,
    data_atualizacao DATE
);

CREATE TABLE tb_produto_quantidade (
    id INT PRIMARY KEY,
    id_produto_fk INT,
    id_venda_fk INT,
    quantidade INT,
    valor_total DECIMAL(10, 2),
    FOREIGN KEY (id_produto_fk) REFERENCES tb_produto(id),
    FOREIGN KEY (id_venda_fk) REFERENCES tb_venda(id)
);

CREATE TABLE tb_venda (
id INT PRIMARY KEY,
codigo VARCHAR(100) UNIQUE,
id_cliente_fk INT,
valor_total DECIMAL(10, 2),
data_venda DATE,
status_venda VARCHAR(50),
FOREIGN KEY (id_cliente_fk) REFERENCES tb_cliente(id)
);

create sequence seq_cliente
start 1
increment 1
owned by tb_cliente.id;

create sequence sq_produto
start 1
increment 1
owned by tb_produto.id;

create sequence sq_venda
start 1
increment 1
owned by tb_venda.id;

create sequence sq_produto_quantidade
start 1
increment 1
owned by tb_produto_quantidade.id;

ALTER TABLE TB_CLIENTE
ADD CONSTRAINT UK_CPF_CLIENTE UNIQUE (CPF);

ALTER TABLE TB_PRODUTO
ADD CONSTRAINT UK_CODIGO_PRODUTO "CODIGO";

ALTER TABLE TB_VENDA
ADD CONSTRAINT UK_CODIGO_VENDA (CODIGO);

SELECT V.ID AS ID.VENDA, V.CODIGO, V.ID_CLIENTE_FK, V.VALOR_TOTAL, V.DATA_VENDA, V.STATUS_VENDA,
C.ID AS ID_CLIENTE, C.NOME, C.CPF, C.TELL, C.ENDERECO, C.NUMERO, C.CIDADE, C.ESTADO,
P.ID AS ID_PROD_QTD, P.QUANTIDADE, P.VALOR_TOTAL AS PRODUTO_QTD_VALOR_TOTAL
FROM TB_VENDA V
INNER JOIN TB_CLIENTE C ON V.ID_CLIENTE_FK = C.ID
INNER JOIN TB_PRODUTO_QUANTIDADE P ON P.ID_VENDA_FK = V.ID
WHERE V.CODIGO = 'A1';

SELECT PQ.ID, PQ.QUANTIDADE, PQ.VALOR_TOTAL,
P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR
FROM TB_PRODUTO_QUANTIDADE PQINNER JOIN TB_PRODUTO P ON P.ID = PQ.ID_PRODUTO_FK;

CREATE SEQUENCE seq_cliente
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO CYCLE;

CREATE OR REPLACE PROCEDURE sp_cliente (
    p_nome IN VARCHAR2,
    p_cpf IN BIGINT,
    p_tel IN BIGINT,
    p_endereco IN VARCHAR2,
    p_numero IN INT,
    p_cidade IN VARCHAR2,
    p_estado IN VARCHAR2,
    p_email IN VARCHAR2,
    p_idade IN INT
)
IS
BEGIN
    INSERT INTO tb_cliente (id, nome, cpf, tel, endereco, numero, cidade, estado, email, idade)
    VALUES (seq_cliente.NEXTVAL, p_nome, p_cpf, p_tel, p_endereco, p_numero, p_cidade, p_estado, p_email, p_idade);
END;
