CREATE TABLE tbl_tiempo (
  id_fecha NUMBER PRIMARY KEY NOT NULL,
  fecha DATE NOT NULL,
  dia_semana NUMBER NOT NULL,
  mes NUMBER NOT NULL,
  trimestre NUMBER NOT NULL,
  anio NUMBER NOT NULL
);

CREATE TABLE tbl_ciudad (
  id_ciudad NUMBER PRIMARY KEY NOT NULL,
  nombre_ciudad VARCHAR2(50) NOT NULL
);


CREATE TABLE tbl_tienda (
  id_tienda NUMBER PRIMARY KEY NOT NULL,
  nombre_tienda VARCHAR2(20) NOT NULL,
  id_ciudad NUMBER NOT NULL,
  FOREIGN KEY (id_ciudad) REFERENCES tbl_ciudad(id_ciudad)
);

CREATE TABLE tbl_empleado (
  id_empleado NUMBER PRIMARY KEY NOT NULL,
  nombre VARCHAR2(100) NOT NULL,
  id_tienda NUMBER NOT NULL,
  FOREIGN KEY (id_tienda) REFERENCES tbl_tienda(id_tienda)
);


CREATE TABLE tbl_categoria (
  id_categoria NUMBER PRIMARY KEY NOT NULL,
  nombre_categoria VARCHAR2(50) NOT NULL
);

-- Tabla de Películas
CREATE TABLE TBL_PELICULA (
    id_pelicula number PRIMARY KEY,
    titulo VARCHAR2(255 BYTE),
    audiencia VARCHAR2(10 BYTE)
);

CREATE TABLE tbl_peli_cate (
    id_pelicula NUMBER NOT NULL,
    id_categoria NUMBER NOT NULL,
    
    CONSTRAINT fk_peli FOREIGN KEY (id_pelicula) REFERENCES tbl_pelicula(id_pelicula),
    CONSTRAINT fk_cate FOREIGN KEY (id_categoria) REFERENCES tbl_categoria(id_categoria)
);

CREATE TABLE TBL_RENTA (
    id_renta NUMBER PRIMARY KEY,
    fecha_renta DATE, 
    fecha_devolucion DATE
);


CREATE TABLE tbl_hechos_renta (
    id_hechos NUMBER PRIMARY KEY NOT NULL,
    id_renta NUMBER NOT NULL,
    id_fecha NUMBER NOT NULL,
    id_tienda NUMBER NOT NULL,
    id_empleado NUMBER NOT NULL,
    id_pelicula NUMBER NOT NULL,
    
    ingresos NUMBER NOT NULL,          -- monto pagado por renta
    cantidad NUMBER DEFAULT 1 NOT NULL, -- para contar cantidad de rentas
    tiempo_renta NUMBER NOT NULL,      -- duración en días u horas
    FOREIGN KEY (id_fecha) REFERENCES tbl_tiempo(id_fecha),
    FOREIGN KEY (id_tienda) REFERENCES tbl_tienda(id_tienda),
    FOREIGN KEY (id_empleado) REFERENCES tbl_empleado(id_empleado),
    FOREIGN KEY (id_pelicula) REFERENCES tbl_pelicula(id_pelicula)
);


---HECHOS
CREATE SEQUENCE seq_tabla_hechos_renta
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_tabla_hechos
BEFORE INSERT ON tbl_hechos_renta
FOR EACH ROW
BEGIN
    SELECT seq_tabla_hechos_renta.NEXTVAL INTO :NEW.id_hechos FROM dual;
END;
/


--TIEMPO
CREATE SEQUENCE SQ_ID_TBL_TIEMPO 
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER tg_id_tbl_tiempo
BEFORE INSERT ON tbl_tiempo
FOR EACH ROW
BEGIN
    SELECT sq_id_tbl_tiempo.NEXTVAL INTO :NEW.id_fecha FROM DUAL;
END;

/

