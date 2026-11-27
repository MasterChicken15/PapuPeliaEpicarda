-- =====================================================================
--  ArenaBatallaPOO_MVC  -  Esquema de base de datos (dialecto MySQL/MariaDB)
--
--  Diseno:
--   1) personajes            -> campos comunes de PersonajeBase
--   2) personaje_atributo    -> atributos propios de cada subclase (clave/valor),
--                               asi agregar una clase nueva NO requiere ALTER TABLE
--   3) combates              -> cabecera de cada pelea
--   4) combate_participante  -> quien pelea, en que equipo y SNAPSHOT de sus stats
--   5) combate_turno         -> bitacora de cada accion (permite reproducir la pelea)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS arena_batalla
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE arena_batalla;

-- ---------------------------------------------------------------------
-- Catalogo de clases (lo que devuelve getTipoClase())
-- ---------------------------------------------------------------------
CREATE TABLE tipos_clase (
    tipo_clase  VARCHAR(30)  NOT NULL,
    descripcion VARCHAR(120) NULL,
    PRIMARY KEY (tipo_clase)
) ENGINE=InnoDB;

INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES
 ('Orco',      'Furia de combate (WAAAGH)'),
 ('Saiyan',    'Ki y bono Zenkai'),
 ('Ninfa',     'Elemental y seduccion'),
 ('Golem',     'Resistencia elemental'),
 ('Arquero',   'Ataque a distancia'),
 ('Bardo',     'Inspiracion y velocidad'),
 ('Caballero', 'Armadura, escudo y criatura de apoyo'),
 ('Barbaro',   'Estamina y arma adicional'),
 ('Stand',     'Espiritu con rango y usuario'),
 ('Polinene',  'Polimonedas y aura');

-- ---------------------------------------------------------------------
-- Personajes (campos de PersonajeBase)
-- ---------------------------------------------------------------------
CREATE TABLE personajes (
    id            VARCHAR(36)  NOT NULL,              -- PersonajeBase.id (String)
    tipo_clase    VARCHAR(30)  NOT NULL,
    nombre        VARCHAR(60)  NOT NULL,
    vida          INT          NOT NULL,
    vida_maxima   INT          NOT NULL,
    fuerza        INT          NOT NULL,
    poder         INT          NOT NULL,
    defensa       INT          NOT NULL,
    arma          VARCHAR(60)  NULL,
    ruta_imagen   VARCHAR(255) NULL,                  -- "Columna VARCHAR" de la Unidad V
    activo        TINYINT(1)   NOT NULL DEFAULT 1,    -- baja logica: conserva historial
    creado_en     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_personajes_nombre (nombre),
    CONSTRAINT fk_personajes_clase FOREIGN KEY (tipo_clase)
        REFERENCES tipos_clase (tipo_clase),
    CONSTRAINT ck_personajes_vida CHECK (vida >= 0 AND vida <= vida_maxima)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Atributos especificos por subclase (clave/valor tipado)
--   Orco:      colmillos, tamano, color, aditamento
--   Saiyan:    cola, celulasZ, zenkai, ki
--   Ninfa:     elemento, nivelSeduccion, experiencia
--   Golem:     resistencia, elemento
--   Arquero:   rango, municion
--   Bardo:     inspiracion, velocidadAtaque
--   Caballero: armadura, resistencia, escudo, criaturaApoyo
--   Barbaro:   estamina, resistencia, armaAdicional
--   Stand:     rango, tipo, frase, usuario
--   Polinene:  cantidadPolimonedas, inteligencia, tamear, nivelAura
-- ---------------------------------------------------------------------
CREATE TABLE personaje_atributo (
    personaje_id VARCHAR(36)  NOT NULL,
    clave        VARCHAR(40)  NOT NULL,
    valor        VARCHAR(255) NOT NULL,
    tipo_dato    ENUM('INT','STRING','BOOL') NOT NULL DEFAULT 'STRING',
    PRIMARY KEY (personaje_id, clave),
    CONSTRAINT fk_atributo_personaje FOREIGN KEY (personaje_id)
        REFERENCES personajes (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Combates (cabecera)
--   modo: 1v1, equipos o todos contra todos; lo define cuantos participantes
--         y que 'equipo' tengan en combate_participante.
--   ganador_id NULL + estado FINALIZADO = empate.
-- ---------------------------------------------------------------------
CREATE TABLE combates (
    id          INT AUTO_INCREMENT NOT NULL,
    modo        ENUM('1V1','EQUIPOS','TODOS_CONTRA_TODOS') NOT NULL DEFAULT '1V1',
    estado      ENUM('EN_CURSO','FINALIZADO','ABANDONADO') NOT NULL DEFAULT 'EN_CURSO',
    iniciado_en TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finalizado_en TIMESTAMP NULL,
    ganador_id  VARCHAR(36) NULL,
    total_turnos INT        NOT NULL DEFAULT 0,
    notas       VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_combate_ganador FOREIGN KEY (ganador_id)
        REFERENCES personajes (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Participantes: foto de los stats al iniciar y al terminar.
--   Necesaria porque las habilidades especiales modifican fuerza/poder
--   y recibirDano modifica la vida del objeto en memoria.
-- ---------------------------------------------------------------------
CREATE TABLE combate_participante (
    combate_id     INT         NOT NULL,
    personaje_id   VARCHAR(36) NOT NULL,
    equipo         TINYINT     NOT NULL DEFAULT 0,   -- 0 = sin equipo / individual
    vida_inicial   INT         NOT NULL,
    fuerza_inicial INT         NOT NULL,
    poder_inicial  INT         NOT NULL,
    defensa_inicial INT        NOT NULL,
    vida_final     INT         NULL,
    fuerza_final   INT         NULL,
    poder_final    INT         NULL,
    sobrevivio     TINYINT(1)  NULL,
    PRIMARY KEY (combate_id, personaje_id),
    CONSTRAINT fk_part_combate FOREIGN KEY (combate_id)
        REFERENCES combates (id) ON DELETE CASCADE,
    CONSTRAINT fk_part_personaje FOREIGN KEY (personaje_id)
        REFERENCES personajes (id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Bitacora de turnos / acciones
--   descripcion guarda el texto que ya devuelven recibirDano() y
--   ejecutarHabilidadEspecial(), para mostrarlo tal cual en la vista.
-- ---------------------------------------------------------------------
CREATE TABLE combate_turno (
    id            BIGINT AUTO_INCREMENT NOT NULL,
    combate_id    INT         NOT NULL,
    numero_turno  INT         NOT NULL,
    atacante_id   VARCHAR(36) NOT NULL,
    objetivo_id   VARCHAR(36) NULL,                  -- NULL si la accion no tiene objetivo
    accion        ENUM('ATAQUE_BASICO','HABILIDAD_ESPECIAL','DEFENSA','OTRA')
                  NOT NULL DEFAULT 'ATAQUE_BASICO',
    dano_bruto    INT         NOT NULL DEFAULT 0,
    dano_neto     INT         NOT NULL DEFAULT 0,
    vida_objetivo_despues INT NULL,
    descripcion   VARCHAR(500) NULL,
    ocurrido_en   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_turno (combate_id, numero_turno, atacante_id),
    CONSTRAINT fk_turno_combate FOREIGN KEY (combate_id)
        REFERENCES combates (id) ON DELETE CASCADE,
    CONSTRAINT fk_turno_atacante FOREIGN KEY (atacante_id)
        REFERENCES personajes (id),
    CONSTRAINT fk_turno_objetivo FOREIGN KEY (objetivo_id)
        REFERENCES personajes (id)
) ENGINE=InnoDB;

CREATE INDEX idx_turno_combate ON combate_turno (combate_id, numero_turno);
CREATE INDEX idx_part_personaje ON combate_participante (personaje_id);

-- ---------------------------------------------------------------------
-- Vistas utiles para la pestana de historial / ranking
-- ---------------------------------------------------------------------
CREATE OR REPLACE VIEW v_ranking AS
SELECT p.id,
       p.nombre,
       p.tipo_clase,
       COUNT(DISTINCT cp.combate_id)                                   AS combates,
       COUNT(DISTINCT CASE WHEN c.ganador_id = p.id THEN c.id END)     AS victorias,
       COUNT(DISTINCT cp.combate_id)
         - COUNT(DISTINCT CASE WHEN c.ganador_id = p.id THEN c.id END) AS no_victorias,
       COALESCE((SELECT SUM(t.dano_neto) FROM combate_turno t
                 WHERE t.atacante_id = p.id), 0)                       AS dano_total_causado
FROM personajes p
LEFT JOIN combate_participante cp ON cp.personaje_id = p.id
LEFT JOIN combates c ON c.id = cp.combate_id AND c.estado = 'FINALIZADO'
GROUP BY p.id, p.nombre, p.tipo_clase;

CREATE OR REPLACE VIEW v_historial_combates AS
SELECT c.id AS combate_id, c.modo, c.estado, c.iniciado_en, c.finalizado_en,
       c.total_turnos, g.nombre AS ganador
FROM combates c
LEFT JOIN personajes g ON g.id = c.ganador_id
ORDER BY c.iniciado_en DESC;

-- ---------------------------------------------------------------------
-- Ejemplo de uso (guardar un Orco)
-- ---------------------------------------------------------------------
-- INSERT INTO personajes (id,tipo_clase,nombre,vida,vida_maxima,fuerza,poder,defensa,arma,ruta_imagen)
-- VALUES ('p-001','Orco','Grommash',200,200,80,30,40,'Hacha de guerra','img/orco.png');
-- INSERT INTO personaje_atributo (personaje_id,clave,valor,tipo_dato) VALUES
--  ('p-001','colmillos','2','INT'),
--  ('p-001','tamano','Corpulento','STRING'),
--  ('p-001','color','Verde','STRING'),
--  ('p-001','aditamento','Cicatrices de batalla','STRING');
