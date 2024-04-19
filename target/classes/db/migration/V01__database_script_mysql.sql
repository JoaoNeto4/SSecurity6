


CREATE TABLE horas (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    hora_minuto time NOT NULL,
    UNIQUE KEY UK_HORA_MINUTO (hora_minuto)
);

CREATE TABLE especialidades (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao text,
    titulo varchar(255) NOT NULL,
    UNIQUE KEY UK_TITULO (titulo)
);

CREATE TABLE medicos (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    crm int(11) NOT NULL,
    data_inscricao date NOT NULL,
    nome varchar(255) NOT NULL,
    id_usuario bigint(20) DEFAULT NULL,
    UNIQUE KEY UK_CRM (crm),
    UNIQUE KEY UK_NOME (nome),
    UNIQUE KEY UK_USUARIO_ID (id_usuario),
    CONSTRAINT FK_USUARIO_ID FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
);

CREATE TABLE agendamentos (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_consulta date DEFAULT NULL,
    id_especialidade bigint(20) DEFAULT NULL,
    id_horario bigint(20) DEFAULT NULL,
    id_medico bigint(20) DEFAULT NULL,
    id_paciente bigint(20) DEFAULT NULL,
    CONSTRAINT FK_ESPECIALIDADE_ID FOREIGN KEY (id_especialidade) REFERENCES especialidades (id),
    CONSTRAINT FK_HORA_ID FOREIGN KEY (id_horario) REFERENCES horas (id),
    CONSTRAINT FK_MEDICO_ID FOREIGN KEY (id_medico) REFERENCES medicos (id),
    CONSTRAINT FK_PACIENTE_ID FOREIGN KEY (id_paciente) REFERENCES pacientes (id)
);

INSERT INTO horas VALUES (1, '07:00:00'), (2, '07:30:00'), ... (20, '17:30:00');  -- Insert values for all 20 hours

CREATE TABLE medicos_tem_especialidades (
    id_especialidade bigint(20) NOT NULL,
    id_medico bigint(20) NOT NULL,
    UNIQUE KEY MEDICO_UNIQUE_ESPECIALIZACAO (id_especialidade, id_medico),
    CONSTRAINT FK_ESPECIALIDADE_MEDICO_ID FOREIGN KEY (id_medico) REFERENCES medicos (id),
    CONSTRAINT FK_MEDICO_ESPECIALIDADE_ID FOREIGN KEY (id_especialidade) REFERENCES especialidades (id)
);

CREATE TABLE pacientes (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_nascimento date NOT NULL,
    nome varchar(255) NOT NULL,
    id_usuario bigint(20) DEFAULT NULL,
    UNIQUE KEY UK_PACIENTE_NOME (nome),
    CONSTRAINT FK_PACIENTE_USUARIO_ID FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
);

CREATE TABLE perfis (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao varchar(255) NOT NULL,
    UNIQUE KEY UK_PERFIL_DESCRICAO (descricao)
);

INSERT INTO perfis VALUES (1, 'ADMIN'), (2, 'MEDICO'), (3, 'PACIENTE');

CREATE TABLE usuarios (
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    ativo tinyint(1) NOT NULL,
    email varchar(255) NOT NULL,
    senha varchar(255) NOT NULL,
    codigo_verificador varchar(6) DEFAULT NULL,
    UNIQUE KEY UK_USUARIO_EMAIL (email)
);

CREATE TABLE usuarios_tem_perfis (
    usuario_id bigint(20) NOT NULL,
    perfil_id bigint(20) NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT FK_PERFIL_TEM_USUARIO_ID FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT FK_USUARIO_TEM_PERFIL_ID FOREIGN KEY (perfil_id) REFERENCES perfis (id)
);