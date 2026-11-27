-- =====================================================================
--  ArenaBatallaPOO_MVC  -  DML (datos y consultas)  -  ORACLE
-- =====================================================================

INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Orco',      'Furia de combate (WAAAGH)');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Saiyan',    'Ki y bono Zenkai');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Ninfa',     'Elemental y seduccion');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Golem',     'Resistencia elemental');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Arquero',   'Ataque a distancia');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Bardo',     'Inspiracion y velocidad');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Caballero', 'Armadura, escudo y criatura de apoyo');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Barbaro',   'Estamina y arma adicional');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Stand',     'Espiritu con rango y usuario');
INSERT INTO tipos_clase (tipo_clase, descripcion) VALUES ('Polinene',  'Polimonedas y aura');
COMMIT;



-- 2.1 CREATE: alta de personaje
INSERT INTO personajes
    (id, tipo_clase, nombre, vida, vida_maxima, fuerza, poder, defensa, arma, ruta_imagen)
VALUES
    (:id, :tipo_clase, :nombre, :vida, :vida_maxima, :fuerza, :poder, :defensa, :arma, :ruta_imagen);



-- 2.2 READ: todos los personajes activos
SELECT id, tipo_clase, nombre, vida, vida_maxima, fuerza, poder, defensa, arma, ruta_imagen
FROM   personajes
WHERE  activo = 1
ORDER  BY nombre;



-- 2.3 READ: un personaje por id (sin importar si esta activo)
SELECT id, tipo_clase, nombre, vida, vida_maxima, fuerza, poder, defensa, arma, ruta_imagen, activo
FROM   personajes
WHERE  id = :id;



-- 2.4 READ: filtrar por clase
SELECT id, nombre, vida, vida_maxima, fuerza, poder, defensa
FROM   personajes
WHERE  tipo_clase = :tipo_clase AND activo = 1
ORDER  BY nombre;



-- 2.5 READ: buscar por nombre (coincidencia parcial, sin distinguir mayusculas)
SELECT id, tipo_clase, nombre, vida, vida_maxima
FROM   personajes
WHERE  activo = 1
  AND  UPPER(nombre) LIKE '%' || UPPER(:texto) || '%'
ORDER  BY nombre;



-- 2.6 READ: validar que el nombre no exista antes de insertar
SELECT COUNT(*) AS existe FROM personajes WHERE UPPER(nombre) = UPPER(:nombre);



-- 2.7 UPDATE: edicion completa
UPDATE personajes
SET    nombre = :nombre, vida = :vida, vida_maxima = :vida_maxima,
       fuerza = :fuerza, poder = :poder, defensa = :defensa,
       arma = :arma, ruta_imagen = :ruta_imagen
WHERE  id = :id;



-- 2.8 UPDATE: solo la vida (p. ej. curar o guardar tras un combate)
UPDATE personajes SET vida = :vida WHERE id = :id;



-- 2.9 UPDATE: restaurar vida al maximo
UPDATE personajes SET vida = vida_maxima WHERE id = :id;



-- 2.10 DELETE logico (recomendado: conserva el historial de peleas)
UPDATE personajes SET activo = 0 WHERE id = :id;



-- 2.11 Reactivar personaje
UPDATE personajes SET activo = 1 WHERE id = :id;



-- 2.12 DELETE fisico (falla si el personaje ya participo en combates;
--      usar solo para personajes sin historial)
DELETE FROM personajes WHERE id = :id;



-- 3.1 CREATE/UPDATE en una sola sentencia (upsert)
MERGE INTO personaje_atributo a
USING (SELECT :personaje_id AS personaje_id,
              :clave        AS clave,
              :valor        AS valor,
              :tipo_dato    AS tipo_dato
       FROM dual) s
ON (a.personaje_id = s.personaje_id AND a.clave = s.clave)
WHEN MATCHED THEN
    UPDATE SET a.valor = s.valor, a.tipo_dato = s.tipo_dato
WHEN NOT MATCHED THEN
    INSERT (personaje_id, clave, valor, tipo_dato)
    VALUES (s.personaje_id, s.clave, s.valor, s.tipo_dato);



-- 3.2 READ: atributos de un personaje
SELECT clave, valor, tipo_dato
FROM   personaje_atributo
WHERE  personaje_id = :personaje_id
ORDER  BY clave;



-- 3.3 READ: personaje + atributos en una sola consulta (para armar el objeto en el DAO)
SELECT p.id, p.tipo_clase, p.nombre, p.vida, p.vida_maxima, p.fuerza, p.poder,
       p.defensa, p.arma, p.ruta_imagen,
       a.clave, a.valor, a.tipo_dato
FROM   personajes p
LEFT JOIN personaje_atributo a ON a.personaje_id = p.id
WHERE  p.activo = 1
ORDER  BY p.nombre, a.clave;



-- 3.4 DELETE: quitar un atributo
DELETE FROM personaje_atributo WHERE personaje_id = :personaje_id AND clave = :clave;



-- 3.5 DELETE: quitar todos los atributos (antes de reescribirlos)
DELETE FROM personaje_atributo WHERE personaje_id = :personaje_id;



-- 4.1 Abrir un combate y recuperar su id
--     JDBC: prepareStatement(sql, new String[]{"ID"}) y getGeneratedKeys()
INSERT INTO combates (modo) VALUES (:modo) RETURNING id INTO :id_combate;



-- 4.2 Registrar a cada participante con la foto de sus stats iniciales
INSERT INTO combate_participante
    (combate_id, personaje_id, equipo, vida_inicial, fuerza_inicial, poder_inicial, defensa_inicial)
SELECT :combate_id, p.id, :equipo, p.vida, p.fuerza, p.poder, p.defensa
FROM   personajes p
WHERE  p.id = :personaje_id;



-- 4.3 Registrar una accion (turno)
INSERT INTO combate_turno
    (combate_id, numero_turno, atacante_id, objetivo_id, accion,
     dano_bruto, dano_neto, vida_objetivo_despues, descripcion)
VALUES
    (:combate_id, :numero_turno, :atacante_id, :objetivo_id, :accion,
     :dano_bruto, :dano_neto, :vida_objetivo_despues, :descripcion);



-- 4.4 Cerrar el combate: guardar los stats finales de cada participante
UPDATE combate_participante
SET    vida_final   = :vida_final,
       fuerza_final = :fuerza_final,
       poder_final  = :poder_final,
       sobrevivio   = CASE WHEN :vida_final > 0 THEN 1 ELSE 0 END
WHERE  combate_id = :combate_id AND personaje_id = :personaje_id;



-- 4.5 Cerrar el combate: ganador, fecha y total de turnos
--     (:ganador_id = NULL para un empate)
UPDATE combates
SET    estado        = 'FINALIZADO',
       finalizado_en = SYSTIMESTAMP,
       ganador_id    = :ganador_id,
       total_turnos  = NVL((SELECT MAX(numero_turno) FROM combate_turno
                            WHERE combate_id = :combate_id), 0)
WHERE  id = :combate_id;



-- 4.6 Marcar un combate como abandonado
UPDATE combates
SET    estado = 'ABANDONADO', finalizado_en = SYSTIMESTAMP
WHERE  id = :combate_id;



-- 4.7 Persistir el estado final del personaje tras la pelea
--     (vida actual; los stats base no se tocan)
UPDATE personajes SET vida = :vida WHERE id = :id;



-- 4.8 Siguiente numero de turno de un combate
SELECT NVL(MAX(numero_turno), 0) + 1 AS siguiente
FROM   combate_turno
WHERE  combate_id = :combate_id;



-- 4.9 Eliminar un combate (borra en cascada participantes y turnos)
DELETE FROM combates WHERE id = :combate_id;



-- 5.1 Historial de combates, mas recientes primero
SELECT * FROM v_historial_combates ORDER BY iniciado_en DESC;



-- 5.2 Participantes de un combate con su resultado
SELECT p.nombre, p.tipo_clase, cp.equipo,
       cp.vida_inicial, cp.vida_final,
       cp.fuerza_inicial, cp.fuerza_final,
       cp.poder_inicial,  cp.poder_final,
       cp.sobrevivio
FROM   combate_participante cp
JOIN   personajes p ON p.id = cp.personaje_id
WHERE  cp.combate_id = :combate_id
ORDER  BY cp.equipo, p.nombre;



-- 5.3 Bitacora completa de un combate (para reproducir la pelea en la vista)
SELECT t.numero_turno,
       a.nombre AS atacante,
       o.nombre AS objetivo,
       t.accion, t.dano_bruto, t.dano_neto, t.vida_objetivo_despues,
       t.descripcion
FROM   combate_turno t
JOIN   personajes a ON a.id = t.atacante_id
LEFT JOIN personajes o ON o.id = t.objetivo_id
WHERE  t.combate_id = :combate_id
ORDER  BY t.numero_turno, t.id;



-- 5.4 Combates en los que participo un personaje
SELECT c.id AS combate_id, c.modo, c.estado, c.iniciado_en,
       CASE WHEN c.ganador_id = :personaje_id THEN 'VICTORIA'
            WHEN c.ganador_id IS NULL AND c.estado = 'FINALIZADO' THEN 'EMPATE'
            WHEN c.estado = 'FINALIZADO' THEN 'DERROTA'
            ELSE c.estado END AS resultado
FROM   combates c
JOIN   combate_participante cp ON cp.combate_id = c.id
WHERE  cp.personaje_id = :personaje_id
ORDER  BY c.iniciado_en DESC;



-- 5.5 Combates en curso
SELECT id, modo, iniciado_en FROM combates WHERE estado = 'EN_CURSO' ORDER BY iniciado_en;



-- 6.1 Ranking general
SELECT nombre, tipo_clase, combates, victorias, no_victorias,
       ROUND(victorias / NULLIF(combates, 0) * 100, 1) AS porcentaje_victorias,
       dano_total_causado
FROM   v_ranking
ORDER  BY victorias DESC, dano_total_causado DESC;



-- 6.2 Top 10 por victorias
SELECT nombre, tipo_clase, victorias
FROM   v_ranking
ORDER  BY victorias DESC, nombre
FETCH FIRST 10 ROWS ONLY;



-- 6.3 Rendimiento por clase
SELECT tipo_clase,
       COUNT(*)           AS personajes,
       SUM(combates)      AS combates,
       SUM(victorias)     AS victorias,
       SUM(dano_total_causado) AS dano_total
FROM   v_ranking
GROUP  BY tipo_clase
ORDER  BY victorias DESC;



-- 6.4 Dano causado y recibido por personaje en un combate
SELECT p.nombre,
       NVL(SUM(CASE WHEN t.atacante_id = p.id THEN t.dano_neto END), 0) AS dano_causado,
       NVL(SUM(CASE WHEN t.objetivo_id = p.id THEN t.dano_neto END), 0) AS dano_recibido
FROM   combate_participante cp
JOIN   personajes p ON p.id = cp.personaje_id
LEFT JOIN combate_turno t ON t.combate_id = cp.combate_id
WHERE  cp.combate_id = :combate_id
GROUP  BY p.nombre
ORDER  BY dano_causado DESC;



-- 6.5 Uso de habilidades especiales por personaje
SELECT a.nombre, COUNT(*) AS veces_habilidad_especial
FROM   combate_turno t
JOIN   personajes a ON a.id = t.atacante_id
WHERE  t.accion = 'HABILIDAD_ESPECIAL'
GROUP  BY a.nombre
ORDER  BY veces_habilidad_especial DESC;
