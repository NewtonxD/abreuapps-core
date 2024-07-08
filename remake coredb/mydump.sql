--
-- PostgreSQL database dump
--

-- Dumped from database version 15.6
-- Dumped by pg_dump version 15.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: payroll; Type: SCHEMA; Schema: -; Owner: cabreu
--

CREATE SCHEMA payroll;


ALTER SCHEMA payroll OWNER TO cabreu;

--
-- Name: transport; Type: SCHEMA; Schema: -; Owner: cabreu
--

CREATE SCHEMA transport;


ALTER SCHEMA transport OWNER TO cabreu;

--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


--
-- Name: notify_trigger(); Type: FUNCTION; Schema: public; Owner: cabreu
--

CREATE FUNCTION public.notify_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $_$
	DECLARE 
		rec RECORD;
		dat RECORD;
		payload TEXT;
		  column_name TEXT;
		  column_value TEXT;
		  payload_items json;
	BEGIN
		-- Identify operation
		case TG_OP
		when 'UPDATE' then
			rec :=new;
			dat:=old;
		when 'INSERT' then
			rec:=new;
		when 'DELETE' then
			rec:=old;
		else
			raise exception 'No es posible notificar esto : "%"',TG_OP;
		end case;
	
		 -- Get required fields
		  IF TG_ARGV[0] IS NOT NULL THEN
		    FOREACH column_name IN ARRAY TG_ARGV LOOP
		      EXECUTE format('SELECT $1.%I::TEXT', column_name)
		      INTO column_value
		      USING rec;
		      payload_items := array_to_json(array_append(payload_items, '"' || replace(column_name, '"', '\"') || '":"' || replace(column_value, '"', '\"') || '"'));
		    END LOOP;
		  ELSE
		    payload_items := row_to_json(rec);
		  END IF;
		
		  -- Build the payload
		  payload := json_build_object('timestamp',CURRENT_TIMESTAMP,'operation',TG_OP,'schema',TG_TABLE_SCHEMA,'table',TG_TABLE_NAME,'data',payload_items);

		  -- Notify the channel
		  PERFORM pg_notify('core_db_event',payload);
		  
		  RETURN rec;
	
	END;
$_$;


ALTER FUNCTION public.notify_trigger() OWNER TO cabreu;

--
-- Name: calculate_beariest(double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.calculate_beariest(lon1 double precision, lat1 double precision, lon2 double precision, lat2 double precision) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
DECLARE
    y NUMERIC;
    x NUMERIC;
    bearing DOUBLE PRECISION;
BEGIN
    
    y := sin(radians(lon2 - lon1)) * cos(radians(lat2));
    x := cos(radians(lat1)) * sin(radians(lat2)) - 
         sin(radians(lat1)) * cos(radians(lat2)) * cos(radians(lon2 - lon1));
    bearing := degrees(atan2(y, x));
    RETURN (bearing + 360)::numeric % 360;
END;
$$;


ALTER FUNCTION transport.calculate_beariest(lon1 double precision, lat1 double precision, lon2 double precision, lat2 double precision) OWNER TO cabreu;

--
-- Name: calculate_total_route_distance(character varying); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.calculate_total_route_distance(route_id character varying) RETURNS double precision
    LANGUAGE plpgsql
    AS $_$
DECLARE
    total_distance DOUBLE PRECISION := 0;
    current_point GEOGRAPHY;
    next_point GEOGRAPHY;
    route_cursor CURSOR FOR 
        SELECT geom
        FROM transport.rta_loc
        WHERE ruta_rta = $1
        ORDER BY id; -- Assuming you have a column to maintain the order of points
BEGIN
    OPEN route_cursor;
    FETCH route_cursor INTO current_point;

    LOOP
        FETCH route_cursor INTO next_point;
        EXIT WHEN NOT FOUND;

        total_distance := total_distance + ST_Distance(current_point, next_point);
        current_point := next_point;
    END LOOP;

    CLOSE route_cursor;
    RETURN total_distance;
END;
$_$;


ALTER FUNCTION transport.calculate_total_route_distance(route_id character varying) OWNER TO cabreu;

--
-- Name: get_lastloc_vhl_data(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.get_lastloc_vhl_data() RETURNS TABLE(placa character varying, ruta character varying, lon double precision, lat double precision, orientation double precision, velocidad integer)
    LANGUAGE plpgsql
    AS $$
	
	begin
		
		RETURN QUERY
		with ids as (
		select
			i.id
		from
			(
			select
				max(tl.id) over(partition by tl.placa_pl , tl.ruta_rta ) as id
			from
				transport.trp_loc tl) i
		group by i.id )
		select
			tl.placa_pl,
			tl.ruta_rta,
			tl.lon,
			tl.lat,
			tl.orientation,
			coalesce(transport.get_vhl_velocity(tl.placa_pl,tl.ruta_rta),0)::int as velocidad
		from transport.trp_loc tl 
			inner join ids i on tl.id = i.id
			inner join transport.vhl v on v.estado_dat = 'En Camino' and tl.placa_pl = v.pl and tl.ruta_rta = v.ruta_rta;
	END;
$$;


ALTER FUNCTION transport.get_lastloc_vhl_data() OWNER TO cabreu;

--
-- Name: get_nearest_pda(double precision, double precision); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.get_nearest_pda(x_lon double precision, x_lat double precision) RETURNS TABLE(id integer, dsc character varying, lon real, lat real, distance integer)
    LANGUAGE plpgsql
    AS $$
	
	begin
		
		RETURN QUERY
	    SELECT
	        p.id,
	        p.dsc,
	        p.lon,
	        p.lat,
	        ST_Distance(
	            p.geom,
	            ST_SetSRID(ST_MakePoint(x_lon, x_lat), 4326)::geography
	        )::int AS distance
	    FROM transport.pda p
	    ORDER BY distance
	    LIMIT 1;
   
	END;

$$;


ALTER FUNCTION transport.get_nearest_pda(x_lon double precision, x_lat double precision) OWNER TO cabreu;

--
-- Name: get_pda_data(integer); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.get_pda_data(parada integer) RETURNS TABLE(ruta character varying, vehiculo character varying, velocidad double precision, distancia_faltante double precision, vhl_lat double precision, vhl_lon double precision)
    LANGUAGE plpgsql
    AS $_$
	begin
		
		return QUERY with nearest_points AS (
		    SELECT 
		        r.ruta_rta as ruta,
		        px.placa_pl as vehiculo,
		        ST_LineLocatePoint(r.geom, px.geom) AS loc_x, -- loc vehiculo
		        pr.intersect AS loc_y, -- loc parada
		        px.lat,px.lon
		    FROM 
		        transport.rta_ln r 
		        inner join transport.pda_rta pr on r.ruta_rta=pr.ruta_rta and pr.id = $1
		        left join (
		          with maxDate as ( 
		          	select tl.placa_pl,max(tl.reg_dt) as reg_dt from transport.trp_loc tl group by 1
		          )select tl.placa_pl,tl.ruta_rta,tl.geom,tl.lat, tl.lon
		          from transport.trp_loc tl 
		          inner join maxDate d on d.placa_pl=tl.placa_pl and d.reg_dt=tl.reg_dt
		        )px on px.ruta_rta=r.ruta_rta and px.placa_pl in (select v.pl from transport.vhl v where v.act and v.estado_dat='En Camino')
		), distances AS (
		    SELECT 
		        r.ruta_rta,
		        np.vehiculo,
		        CASE
		            WHEN loc_x >= loc_y THEN 
		                ST_Length(ST_LineSubstring(r.geom, np.loc_y, np.loc_x)::geography)
		            ELSE 
		                ST_Length(ST_LineSubstring(r.geom, np.loc_y, 1)::geography) + 
		                ST_Length(ST_LineSubstring(r.geom, 0, np.loc_x)::geography)
		        END AS distance,
		        np.lat, np.lon
		    FROM 
		        transport.rta_ln r
		    JOIN 
		        nearest_points np ON r.ruta_rta = np.ruta
		)
		select d.ruta_rta as ruta, 
			d.vehiculo,
			coalesce(transport.get_vhl_velocity(d.vehiculo,d.ruta_rta),0) as velocidad,
			MIN(d.distance) as distance,
			d.lat,d.lon
		FROM  distances d group by 1,2,3,5,6  ORDER BY d.ruta_rta;
	
	END;
$_$;


ALTER FUNCTION transport.get_pda_data(parada integer) OWNER TO cabreu;

--
-- Name: get_rta_data(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.get_rta_data() RETURNS TABLE(ruta character varying, loc_ini character varying, loc_fin character varying, distancia integer, total_vehiculos integer)
    LANGUAGE plpgsql
    AS $$
	BEGIN
	return query select r.rta,r.loc_ini,r.loc_fin,transport.calculate_total_route_distance(r.rta)::int as distancia_total,count(v.*)::int as total_vehiculos
		from 
			transport.rta r
			left join transport.vhl v on r.act and v.ruta_rta=r.rta and v.estado_dat ='En Camino'
	group by 1,2,3,4;
	END;
$$;


ALTER FUNCTION transport.get_rta_data() OWNER TO cabreu;

--
-- Name: get_vhl_velocity(character varying, character varying); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.get_vhl_velocity(vehiculo character varying, ruta character varying) RETURNS double precision
    LANGUAGE plpgsql
    AS $_$
	declare 
		last_velocity DOUBLE PRECISION;
	BEGIN
	WITH latest_points AS (
	    SELECT
	        tl.placa_pl,
	        tl.reg_dt,
	        tl.geom,
	        tl.prev_geom,
	        tl.prev_reg_dt
	    FROM transport.trp_loc tl
	    WHERE tl.placa_pl = $1 and tl.ruta_rta=$2 
	    ORDER BY tl.reg_dt DESC
	    LIMIT 5
	)
	SELECT 
	    SUM(ST_Distance(geom::geography, prev_geom::geography)) / SUM(EXTRACT(EPOCH FROM (reg_dt - prev_reg_dt))) into last_velocity
	FROM latest_points
	WHERE prev_geom IS NOT null
	group by placa_pl;
	
	return last_velocity;
	END;
$_$;


ALTER FUNCTION transport.get_vhl_velocity(vehiculo character varying, ruta character varying) OWNER TO cabreu;

--
-- Name: loc_notify_trigger(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.loc_notify_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	begin
		PERFORM pg_notify('loc_db_event','');
		return new;
	END;
$$;


ALTER FUNCTION transport.loc_notify_trigger() OWNER TO cabreu;

--
-- Name: refresh_pda_rtaxpda(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.refresh_pda_rtaxpda() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	BEGIN
		DELETE FROM transport.pda_rta  WHERE id = NEW.id;
		
		insert into transport.pda_rta  
		SELECT p.id,rl.ruta_rta,ST_LineLocatePoint(rl.geom, p.geom)
		FROM transport.pda p
		JOIN transport.rta_ln rl ON ST_DWithin(p.geom,rl.geom::geography, 5)
		where p.act and p.id = new.id order by 1,2;	
	
		return new;
	END;
$$;


ALTER FUNCTION transport.refresh_pda_rtaxpda() OWNER TO cabreu;

--
-- Name: refresh_route_lines(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.refresh_route_lines() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Delete existing linestring for the affected route
    DELETE FROM transport.rta_ln WHERE ruta_rta = NEW.ruta_rta;
    
    -- Insert new linestring for the affected route
    INSERT INTO transport.rta_ln (ruta_rta, geom)
    SELECT 
        ruta_rta,
        ST_MakeLine(geom ORDER BY id)
    FROM transport.rta_loc
    WHERE ruta_rta = NEW.ruta_rta
    GROUP BY ruta_rta;
    
    RETURN NEW;
END;
$$;


ALTER FUNCTION transport.refresh_route_lines() OWNER TO cabreu;

--
-- Name: refresh_rta_rtaxpda(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.refresh_rta_rtaxpda() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	BEGIN
	
	
		DELETE FROM transport.pda_rta  WHERE ruta_rta = NEW.ruta_rta;
		
		insert into transport.pda_rta  
		SELECT p.id,rl.ruta_rta,ST_LineLocatePoint(rl.geom, p.geom)
		FROM transport.pda p
		JOIN transport.rta_ln rl ON ST_DWithin(p.geom,rl.geom::geography, 5)
		where p.act and rl.ruta_rta = new.ruta_rta order by 1,2;	
    
    RETURN NEW;	
		
	END;
$$;


ALTER FUNCTION transport.refresh_rta_rtaxpda() OWNER TO cabreu;

--
-- Name: update_geom(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.update_geom() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.geom := ST_SetSRID(ST_MakePoint(NEW.lon, NEW.lat), 4326);
    RETURN NEW;
END;
$$;


ALTER FUNCTION transport.update_geom() OWNER TO cabreu;

--
-- Name: update_prev_geom(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.update_prev_geom() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	begin
		
		select geom,reg_dt,transport.calculate_beariest(new.lon,new.lat,lon,lat) into new.prev_geom,new.prev_reg_dt,new.orientation
		FROM transport.trp_loc 
	    WHERE placa_pl = new.placa_pl and ruta_rta=new.ruta_rta
	    order by reg_dt desc limit 1;
	   
		return new;
	END;
$$;


ALTER FUNCTION transport.update_prev_geom() OWNER TO cabreu;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: emp; Type: TABLE; Schema: payroll; Owner: cabreu
--

CREATE TABLE payroll.emp (
    id integer NOT NULL,
    act boolean,
    ap character varying(255),
    ced character varying(255),
    dt_ini date,
    dt_fin date,
    nam character varying(255),
    pst character varying(255),
    sex character varying(255)
);


ALTER TABLE payroll.emp OWNER TO cabreu;

--
-- Name: acc; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.acc (
    id integer NOT NULL,
    act boolean,
    fat_scr character varying(255),
    pantalla_dat character varying(255),
    tipo_dato_dat character varying(255)
);


ALTER TABLE public.acc OWNER TO cabreu;

--
-- Name: acc_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.acc_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.acc_seq OWNER TO cabreu;

--
-- Name: emp_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.emp_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.emp_seq OWNER TO cabreu;

--
-- Name: gnr_dat; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.gnr_dat (
    dat character varying(255) NOT NULL,
    act boolean,
    dsc character varying(255),
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    actualizado_por_id integer,
    hecho_por_id integer,
    fat_dat character varying(255)
);


ALTER TABLE public.gnr_dat OWNER TO cabreu;

--
-- Name: pda_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.pda_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pda_seq OWNER TO cabreu;

--
-- Name: ppl_inf; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.ppl_inf (
    id integer NOT NULL,
    apl character varying(255),
    nic character varying(255),
    ced character varying(255),
    dir character varying(255),
    upd_at timestamp(6) without time zone,
    brt_at date,
    mde_at timestamp(6) without time zone,
    nam character varying(255),
    emg_nam character varying(255),
    num_cel character varying(255),
    emg_tel character varying(255),
    actualizado_por_id integer,
    hecho_por_id integer,
    sexo_dat character varying(255),
    tipo_sangre_dat character varying(255)
);


ALTER TABLE public.ppl_inf OWNER TO cabreu;

--
-- Name: ppl_inf_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.ppl_inf_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ppl_inf_seq OWNER TO cabreu;

--
-- Name: rta_asg_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.rta_asg_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rta_asg_seq OWNER TO cabreu;

--
-- Name: rta_loc_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.rta_loc_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rta_loc_seq OWNER TO cabreu;

--
-- Name: rta_pda_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.rta_pda_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rta_pda_seq OWNER TO cabreu;

--
-- Name: sys_cnf; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.sys_cnf (
    cod character varying(255) NOT NULL,
    dsc character varying(255),
    upd_at timestamp(6) without time zone,
    val character varying(255),
    actualizado_por_id integer
);


ALTER TABLE public.sys_cnf OWNER TO cabreu;

--
-- Name: trp_loc_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.trp_loc_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trp_loc_seq OWNER TO cabreu;

--
-- Name: usr; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.usr (
    id integer NOT NULL,
    act boolean,
    actualizado_por_id integer,
    pwd_chg boolean,
    mail character varying(255),
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    hecho_por_id integer,
    pwd character varying(255),
    usr character varying(255),
    persona_id integer NOT NULL
);


ALTER TABLE public.usr OWNER TO cabreu;

--
-- Name: usr_acc; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.usr_acc (
    id integer NOT NULL,
    act boolean,
    val character varying(255),
    acceso_id integer,
    usuario_id integer
);


ALTER TABLE public.usr_acc OWNER TO cabreu;

--
-- Name: usr_acc_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.usr_acc_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usr_acc_seq OWNER TO cabreu;

--
-- Name: usr_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.usr_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usr_seq OWNER TO cabreu;

--
-- Name: pda; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.pda (
    id integer NOT NULL,
    act boolean,
    dsc character varying(255),
    dsc_ptr character varying(255),
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    lat real,
    lat_ptr real,
    lon real,
    lon_ptr real,
    actualizado_por_id integer,
    hecho_por_id integer,
    geom public.geometry(Point,4326)
);


ALTER TABLE transport.pda OWNER TO cabreu;

--
-- Name: pda_rta; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.pda_rta (
    id integer,
    ruta_rta character varying(255),
    "intersect" double precision
);


ALTER TABLE transport.pda_rta OWNER TO cabreu;

--
-- Name: rta; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.rta (
    rta character varying(255) NOT NULL,
    act boolean,
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    loc_fin character varying(255),
    loc_ini character varying(255),
    actualizado_por_id integer,
    hecho_por_id integer
);


ALTER TABLE transport.rta OWNER TO cabreu;

--
-- Name: rta_ln; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.rta_ln (
    ruta_rta character varying(255),
    geom public.geometry
);


ALTER TABLE transport.rta_ln OWNER TO cabreu;

--
-- Name: rta_loc; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.rta_loc (
    id bigint NOT NULL,
    lat double precision,
    lon double precision,
    ruta_rta character varying(255),
    geom public.geometry(Point,4326)
);


ALTER TABLE transport.rta_loc OWNER TO cabreu;

--
-- Name: trp_loc; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.trp_loc (
    id bigint NOT NULL,
    reg_dt timestamp(6) without time zone,
    lat double precision,
    lon double precision,
    placa_pl character varying(255),
    geom public.geometry(Point,4326),
    ruta_rta character varying(255),
    prev_geom public.geometry(Point,4326),
    prev_reg_dt timestamp(6) without time zone,
    orientation double precision
);


ALTER TABLE transport.trp_loc OWNER TO cabreu;

--
-- Name: vhl; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.vhl (
    pl character varying(255) NOT NULL,
    mke_at smallint,
    cap_pax smallint,
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    actualizado_por_id integer,
    estado_dat character varying(255),
    hecho_por_id integer,
    marca_dat character varying(255),
    modelo_dat character varying(255),
    tipo_vehiculo_dat character varying(255),
    color_dat character varying(255),
    act boolean,
    tkn character varying(255),
    ruta_rta character varying(255)
);


ALTER TABLE transport.vhl OWNER TO cabreu;

--
-- Data for Name: emp; Type: TABLE DATA; Schema: payroll; Owner: cabreu
--

COPY payroll.emp (id, act, ap, ced, dt_ini, dt_fin, nam, pst, sex) FROM stdin;
\.


--
-- Data for Name: acc; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.acc (id, act, fat_scr, pantalla_dat, tipo_dato_dat) FROM stdin;
8	t	dat_gen_consulta_datos	dat_gen_registro_datos	Booleano
12	t	sys_principal	usr_mgr_principal	Booleano
13	t	usr_mgr_principal	usr_mgr_registro	Booleano
15	t	\N	sys_principal	Booleano
17	t	\N	con_principal	Booleano
16	t	sys_principal	sys_configuracion	Booleano
18	t	\N	cxp_principal	Booleano
19	t	cxp_principal	cxp_registro_facturas	Booleano
20	t	cxp_principal	cxp_control_pagos	Booleano
21	t	cxp_principal	cxp_informes	Booleano
22	t	cxp_principal	cxp_comunicacion_proveedores	Booleano
23	t	cxp_principal	cxp_autorizaciones_aprobaciones	Booleano
24	t	con_principal	con_registro_transacciones	Booleano
25	t	con_principal	con_conciliacion_bancaria	Booleano
26	t	con_principal	con_cuentas_bancarias	Booleano
27	t	con_principal	con_presupuesto	Booleano
28	t	con_principal	con_impuestos	Booleano
29	t	con_principal	con_informes	Booleano
30	t	\N	trp_principal	Booleano
31	t	trp_principal	trp_rutas_consulta	Booleano
32	t	trp_principal	trp_paradas_consulta	Booleano
33	t	trp_principal	trp_vehiculo_consulta	Booleano
34	t	trp_vehiculo_consulta	trp_vehiculo_registro	Booleano
35	t	trp_paradas_consulta	trp_paradas_registro	Booleano
36	t	trp_rutas_consulta	trp_rutas_registro	Booleano
6	t	sys_principal	dat_gen_consulta_datos	Booleano
\.


--
-- Data for Name: gnr_dat; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.gnr_dat (dat, act, dsc, upd_at, mde_at, actualizado_por_id, hecho_por_id, fat_dat) FROM stdin;
Bicicleta	t	Bicicleta.	2024-06-13 09:52:54.255	2024-06-13 09:52:54.255	\N	1	\N
Menu	t	Categorias para los menus del sistema	\N	2024-03-10 14:54:10	\N	1	\N
Modulo	t	Id de los modulos que se usan en el sistema	\N	2024-03-10 14:54:10	\N	1	\N
Pantallas	t	Id de las pantallas donde se utilizan los permisos	2023-08-23 12:28:22	2023-08-23 12:28:22	\N	1	\N
Permisos	f	Permisos del sistema.	2023-08-29 10:36:15	2023-07-14 17:23:10	1	1	\N
Sexo	t	Categoria Sexo para la inf. personal	\N	2023-09-23 15:23:24	\N	1	\N
Tipos Sanguineos	t	Categoría para los grupos de sangre.	2023-07-14 17:17:05	2023-07-04 10:22:49	1	1	\N
Estados Vehiculo	t	Representa los diferentes estados que puede tener un vehiculo de transporte.	2024-03-31 11:01:03.257	2024-03-31 11:01:03.257	\N	1	\N
Tipo Vehiculo	t	Representa los diferentes tipos de vehiculos que se pueden utilizar en el modulo de transporte.	2024-03-31 11:02:05.302	2024-03-31 11:02:05.302	\N	1	\N
Marca	t	Marca del vehiculo de transporte.	2024-03-31 11:07:26.999	2024-03-31 11:02:31.345	1	1	\N
Modelo	f	Modelo del vehiculo, asociado a la Marca.	2024-03-31 11:21:57.253	2024-03-31 11:04:02.763	1	1	\N
Tipo de Datos	t	Describe los Tipo de Datos manejados en el sistema	2023-08-23 12:25:52	2023-08-23 12:25:52	\N	1	\N
Tipos de Permisos	f	Los diferentes tipos de permisos del sistema.	2023-08-29 10:36:32	2023-08-29 10:36:32	\N	1	\N
Colores	t	Colores para el sistema.	2024-05-17 07:59:12.936	2023-07-14 17:23:26	2	1	\N
Interlink	t	Modelo de autobuses Scania.	2024-03-31 11:49:56.014	2024-03-31 11:49:56.014	\N	1	Scania
Touring	t	Modelo de autobuses Scania.	2024-03-31 11:50:06.798	2024-03-31 11:50:06.798	\N	1	Scania
OmniExpress	t	Modelo de autobuses Scania.	2024-03-31 11:50:18.201	2024-03-31 11:50:18.201	\N	1	Scania
OmniCity	t	Modelo de autobuses Scania.	2024-03-31 11:50:30.145	2024-03-31 11:50:30.145	\N	1	Scania
Metrolink	t	Modelo de autobuses Scania.	2024-03-31 11:50:40.694	2024-03-31 11:50:40.694	\N	1	Scania
Higer A30	t	Modelo de autobuses Scania.	2024-03-31 11:50:53.402	2024-03-31 11:50:53.402	\N	1	Scania
K series	t	Modelo de autobuses Scania.	2024-03-31 11:51:25.358	2024-03-31 11:51:25.358	\N	1	Scania
F series	t	Modelo de autobuses Scania.	2024-03-31 11:51:33.755	2024-03-31 11:51:33.755	\N	1	Scania
N series	t	Modelo de autobuses Scania.	2024-03-31 11:51:46.73	2024-03-31 11:51:46.73	\N	1	Scania
S 417	t	Modelo de autobuses Setra.	2024-03-31 11:52:39.716	2024-03-31 11:52:39.716	\N	1	Setra
S 415	t	Modelo de autobuses Setra.	2024-03-31 11:52:49.668	2024-03-31 11:52:49.668	\N	1	Setra
S 416	t	Modelo de autobuses Setra.	2024-03-31 11:52:59.988	2024-03-31 11:52:59.988	\N	1	Setra
S 417 HDH	t	Modelo de autobuses Setra.	2024-03-31 11:53:12.1	2024-03-31 11:53:12.1	\N	1	Setra
S 419 UL	t	Modelo de autobuses Setra.	2024-03-31 11:53:23.772	2024-03-31 11:53:23.772	\N	1	Setra
S 431 DT	t	Modelo de autobuses Setra.	2024-03-31 11:53:42.052	2024-03-31 11:53:42.052	\N	1	Setra
S 415 UL	t	Modelo de autobuses Setra.	2024-03-31 11:53:59.879	2024-03-31 11:53:59.879	\N	1	Setra
S 515 HD	t	Modelo de autobuses Setra.	2024-03-31 11:54:59.859	2024-03-31 11:54:59.859	\N	1	Setra
S 511 HD	t	Modelo de autobuses Setra.	2024-03-31 11:55:13.696	2024-03-31 11:55:13.696	\N	1	Setra
S 416 GT-HD	t	Modelo de autobuses Setra.	2024-03-31 11:55:39.412	2024-03-31 11:55:39.412	\N	1	Setra
Coaster	t	Modelo de autobuses Toyota.	2024-03-31 11:56:42.427	2024-03-31 11:56:42.427	\N	1	Toyota
HiAce Commuter	t	Modelo de autobuses Toyota.	2024-03-31 11:56:56.241	2024-03-31 11:56:56.241	\N	1	Toyota
Granvia	t	Modelo de autobuses Toyota.	2024-03-31 11:57:06.972	2024-03-31 11:57:06.972	\N	1	Toyota
Regius Ace	t	Modelo de autobuses Toyota.	2024-03-31 11:57:16.717	2024-03-31 11:57:16.717	\N	1	Toyota
8900	t	Modelo de autobuses Volvo.	2024-03-31 11:58:15.262	2024-03-31 11:58:15.262	\N	1	Volvo
9700	t	Modelo de autobuses Volvo.	2024-03-31 11:58:25.145	2024-03-31 11:58:25.145	\N	1	Volvo
9900	t	Modelo de autobuses Volvo.	2024-03-31 11:58:35.48	2024-03-31 11:58:35.48	\N	1	Volvo
B7R	t	Modelo de autobuses Volvo.	2024-03-31 11:58:43.6	2024-03-31 11:58:43.6	\N	1	Volvo
B8R	t	Modelo de autobuses Volvo.	2024-03-31 11:58:53.364	2024-03-31 11:58:53.364	\N	1	Volvo
B11R	t	Modelo de autobuses Volvo.	2024-03-31 11:59:01.857	2024-03-31 11:59:01.857	\N	1	Volvo
B12B	t	Modelo de autobuses Volvo.	2024-03-31 11:59:11.117	2024-03-31 11:59:11.117	\N	1	Volvo
B12M	t	Modelo de autobuses Volvo.	2024-03-31 11:59:19.574	2024-03-31 11:59:19.574	\N	1	Volvo
B13R	t	Modelo de autobuses Volvo.	2024-03-31 11:59:28.517	2024-03-31 11:59:28.517	\N	1	Volvo
ZK6126HGC	t	Modelo de la marca Yutong Group.	2024-03-31 12:00:38.251	2024-03-31 12:00:38.251	\N	1	Yutong Group
Citaro	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
ZK6116HGA	t	Modelo de la marca Yutong Group.	2024-03-31 12:00:50.299	2024-03-31 12:00:50.299	\N	1	Yutong Group
ZK6120BEVG	t	Modelo de la marca Yutong Group.	2024-03-31 12:01:02.931	2024-03-31 12:01:02.931	\N	1	Yutong Group
ZK6120RAC	t	Modelo de la marca Yutong Group.	2024-03-31 12:01:20.762	2024-03-31 12:01:20.762	\N	1	Yutong Group
ZK6122H9	t	Modelo de la marca Yutong Group.	2024-03-31 12:01:40.021	2024-03-31 12:01:40.021	\N	1	Yutong Group
ZK6127HA	t	Modelo de la marca Yutong Group.	2024-03-31 12:01:51.479	2024-03-31 12:01:51.479	\N	1	Yutong Group
ZK6125CHEVG1	t	Modelo de la marca Yutong Group.	2024-03-31 12:02:04.79	2024-03-31 12:02:04.79	\N	1	Yutong Group
ZK6852HG	t	Modelo de la marca Yutong Group.	2024-03-31 12:02:23.212	2024-03-31 12:02:23.212	\N	1	Yutong Group
ZK6125CHEVGC1	t	Modelo de la marca Yutong Group.	2024-03-31 12:02:45.183	2024-03-31 12:02:45.183	\N	1	Yutong Group
ZK6119HA	t	Modelo de la marca Yutong Group.	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Yutong Group
Autobus	t	Tipos de vehiculos del sistema.	2024-03-31 12:03:35.752	2024-03-31 12:03:35.752	\N	1	Tipo Vehiculo
Camión	t	Tipo de vehiculo.	2024-03-31 12:04:07.448	2024-03-31 12:04:07.448	\N	1	Tipo Vehiculo
trp_principal	t	Transporte	2024-03-31 12:02:23.212	2024-03-31 12:02:23.212	\N	1	Modulo
con_principal	t	Contabilidad	2024-03-31 12:02:45.183	2024-03-31 12:02:45.183	\N	1	Modulo
cxp_principal	t	Cuentas por Pagar	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Modulo
sys_principal	t	Sistemas	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Modulo
con_conciliacion_bancaria	t	Conciliación Bancaría	2024-03-31 12:02:23.212	2024-03-31 12:02:23.212	\N	1	Menu
con_cuentas_bancarias	t	Cuentas Bancarías	2024-03-31 12:02:45.183	2024-03-31 12:02:45.183	\N	1	Menu
con_impuestos	t	Impuestos	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
con_informes	t	Informes y Reportes	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
con_presupuesto	t	Presupuesto	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
con_registro_transacciones	t	Transacciones	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
sys_configuracion	t	Configuraciones	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
trp_vehiculo_consulta	t	Vehículos	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
usr_mgr_principal	t	Usuarios	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
usr_mgr_registro	t	Registro	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Pantallas
usr_mgr_permisos	t	Informes	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Pantallas
Masculino	t	Sexo Masculino	2024-03-31 12:02:23.212	2024-03-31 12:02:23.212	\N	1	Sexo
Femenino	t	Sexo Femenino	2024-03-31 12:02:45.183	2024-03-31 12:02:45.183	\N	1	Sexo
Booleano	t	Dato true o false	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipo de Datos
Cadena de Texto	t	Dato texto	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipo de Datos
Enteros	t	Dato entero	2024-03-31 12:02:23.212	2024-03-31 12:02:23.212	\N	1	Tipo de Datos
Decimales	t	Dato decimal	2024-03-31 12:02:45.183	2024-03-31 12:02:45.183	\N	1	Tipo de Datos
O+	t	Sangre O+	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
O-	t	Sangre O-	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
A+	t	Sangre A+	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
A-	t	Sangre A-	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
B+	t	Sangre B+	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
B-	t	Sangre B-	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
AB+	t	Sangre AB+	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
AB-	t	Sangre AB-	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Tipos Sanguineos
Rojo	t	Color Rojo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Azul	t	Color Azul	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Amarillo	t	Color Amarillo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Verde	t	Color Verde	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Morado	t	Color Morado	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Naranja	t	Color Naranja	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Gris	t	Color Gris	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Negro	t	Color Negro	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Blanco	t	Color Blanco	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Rosado	t	Color Rosado	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Colores
Estacionado	t	Estado estacionado	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Estados Vehiculo
En Parada	t	Estado En Parada	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Estados Vehiculo
Averiado	t	Estado Averiado	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Estados Vehiculo
En Camino	t	Estado En Camino	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Estados Vehiculo
Mantenimiento	t	Estado Mantenimiento	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Estados Vehiculo
Mercedes Benz	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Isuzu	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Volvo	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
KYC	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Scania	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Setra	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
MAN Truck & Bus	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Toyota	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Honda	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Iveco Bus	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
Yutong Group	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Marca
dat_gen_consulta_datos	t	Mantenimiento de Datos	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
Tourismo	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
Intouro	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
Conecto	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
Sprinter City	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
CapaCity	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
eCitaro	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
Travego	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
OC 500 RF	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
OC 500 LE	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Mercedes Benz
MAN Lion"s City	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s Coach	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s Intercity	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s Regio	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s Star	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s Explorer	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s Intercity CNG	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s City Hybrid	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s City Electric	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
MAN Lion"s City CNG	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	MAN Truck & Bus
Crossway	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Urbanway	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Daily Minibus	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Magelys	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Evadys	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Crealis	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Irisbus Citelis	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Daily Tourys	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Ares	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
EuroRider	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
EuroCargo Bus	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Turbocity	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
CityClass	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Midys	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Eurorider Cursor	t	Marca de Vehículo	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Iveco Bus
Indefinido	t	Sexo Indefinido	2024-04-09 15:10:11.605	2024-04-09 15:10:11.605	\N	1	Sexo
dat_gen_registro_datos	t	Registro	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Pantallas
cxp_registro_facturas	t	Facturas	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
cxp_control_pagos	t	Control de Pagos	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
cxp_informes	t	Informes y Reportes	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
cxp_comunicacion_proveedores	t	Comunicación Proveedores	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
cxp_autorizaciones_aprobaciones	t	Autorizaciones	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
trp_rutas_consulta	t	Rutas	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
trp_paradas_consulta	t	Paradas	2024-03-31 12:02:56.006	2024-03-31 12:02:56.006	\N	1	Menu
trp_vehiculo_registro	t	Registro	2024-04-10 15:57:37.69	2024-04-10 15:57:37.69	\N	1	Pantallas
trp_paradas_registro	t	Registro	2024-05-01 19:35:36.085	2024-05-01 19:35:36.085	\N	1	Pantallas
trp_rutas_registro	t	Registro	2024-05-11 08:17:42.6281	2024-05-11 08:17:42.6281	\N	1	Pantallas
7900	t	Modelo de autobuses Volvo.	2024-05-14 14:57:16.33	2024-03-31 11:58:06.558	2	1	Volvo
\.


--
-- Data for Name: ppl_inf; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.ppl_inf (id, apl, nic, ced, dir, upd_at, brt_at, mde_at, nam, emg_nam, num_cel, emg_tel, actualizado_por_id, hecho_por_id, sexo_dat, tipo_sangre_dat) FROM stdin;
3	Abreu Pérez		402-1017321-1	Pekin	2024-03-17 15:06:02	2005-01-13	2023-09-23 15:28:22	Esther	Yocasta Pérez	809-958-6826	809-913-3633	1	1	Femenino	O+
5	Abreu	NEwton	402-1017327-1	Los alamos	2024-03-18 00:20:48	2004-01-31	2024-02-01 00:07:25	CArlos		809-958-6826		1	1	Femenino	AB-
2	Abreu Pérez	newton	402-1017327-0	Los ciruelitos calle 1 con 14 diamela... excelente!!!	2024-03-21 17:01:19.204	2001-10-30	2023-09-23 15:25:30	Carlos Isaac	Yocasta Pérez	809-958-6826	809-913-3633	1	1	Masculino	AB-
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: sys_cnf; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.sys_cnf (cod, dsc, upd_at, val, actualizado_por_id) FROM stdin;
appnombre	Nombre del sistema	2024-05-30 15:30:28.566	STP OMSA	1
\.


--
-- Data for Name: usr; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.usr (id, act, actualizado_por_id, pwd_chg, mail, upd_at, mde_at, hecho_por_id, pwd, usr, persona_id) FROM stdin;
4	t	1	f	qqqqq@gmail.com	2024-03-18 00:20:48	2024-02-01 00:07:26	1	$2a$10$kOUT0mBy17tLYSd6ucIZAeBl71bBBl6tT7L4kc1ij5ahgA2Egb.Uy	newton300	5
1	t	1	f	carlosiabreup@gmail.com	2024-03-21 17:01:19.429	2023-09-20 11:02:18	1	$2a$10$FD.HVab6z8H3Tba.hw.SvukdeJDfZ5aIIzCN87AL7T2SSAJqoi8Bq	newton30	2
2	t	1	f	carlosiap30@gmail.com	2024-03-17 15:06:02	2023-09-20 11:02:18	1	$2a$10$eu4bYQCJFUXSmKiIEu3/COg6.hX8TYOEgY9foRYi2BxP6idl5qMw.	esther	3
\.


--
-- Data for Name: usr_acc; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.usr_acc (id, act, val, acceso_id, usuario_id) FROM stdin;
2	t	true	6	1
5	t	true	12	1
9	t	true	13	1
11	t	true	6	2
12	t	true	8	2
16	t	true	15	1
17	t	true	15	2
20	t	true	16	1
52	t	true	16	2
14	t	true	12	2
15	t	true	13	2
53	t	true	18	2
54	t	true	23	2
55	t	true	19	2
56	t	true	20	2
57	t	true	21	2
58	t	true	22	2
59	t	true	17	2
60	t	true	24	2
61	t	true	29	2
62	t	true	28	2
63	t	true	27	2
64	t	true	26	2
65	t	true	25	2
108	t	true	17	1
109	t	true	24	1
110	t	true	29	1
111	t	true	28	1
112	t	true	27	1
113	t	true	26	1
114	t	true	25	1
102	t	true	18	1
103	t	true	23	1
104	t	true	19	1
105	t	true	20	1
106	t	true	21	1
107	t	true	22	1
3	t	true	8	1
252	t	true	30	2
253	t	true	31	2
254	t	true	33	2
255	t	true	34	2
256	t	true	32	2
302	t	false	35	2
152	t	true	30	1
153	t	true	32	1
303	t	true	35	1
154	t	true	33	1
202	t	true	34	1
155	t	true	31	1
352	t	true	36	1
\.


--
-- Data for Name: pda; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.pda (id, act, dsc, dsc_ptr, upd_at, mde_at, lat, lat_ptr, lon, lon_ptr, actualizado_por_id, hecho_por_id, geom) FROM stdin;
2	t	Parada la cienaga 2	\N	2024-05-08 18:41:19.304	2024-05-08 07:31:53.566	19.490173	\N	-70.71864	\N	1	1	0101000020E610000000000040FEAD51C0000000007C7D3340
102	t	parada curva 3	\N	2024-06-06 09:37:01.255	2024-06-06 09:03:48.894	19.487822	\N	-70.71605	\N	1	1	0101000020E6100000000000C0D3AD51C0000000E0E17C3340
52	t	parada en la curva	\N	2024-06-06 09:37:13.547	2024-06-06 08:52:09.845	19.487614	\N	-70.71695	\N	1	1	0101000020E610000000000080E2AD51C000000040D47C3340
53	t	parada en la curva2	\N	2024-06-06 09:37:28.232	2024-06-06 08:57:28.159	19.487902	\N	-70.71468	\N	1	1	0101000020E610000000000060BDAD51C000000020E77C3340
152	t	parada 88 roostece	\N	2024-06-06 09:37:56.792	2024-06-06 09:37:56.792	19.48684	\N	-70.7186	\N	\N	1	0101000020E610000000000080FDAD51C000000080A17C3340
1	t	Parada La cienega 1	\N	2024-06-06 09:47:16.326	2024-05-07 07:31:01.71	19.490456	\N	-70.71826	\N	1	1	0101000020E610000000000000F8AD51C0000000808E7D3340
153	t	la2777	\N	2024-06-06 09:48:09.286	2024-06-06 09:48:09.286	19.486132	\N	-70.71865	\N	\N	1	0101000020E610000000000060FEAD51C000000020737C3340
204	t	Safiro Club	\N	2024-07-02 17:53:36.887	2024-07-02 17:53:36.887	19.484968	\N	-70.71108	\N	\N	1	0101000020E61000000000006082AD51C0000000E0267C3340
203	t	Lovera	\N	2024-07-02 17:53:53.729	2024-07-02 17:52:32.843	19.485191	\N	-70.711624	\N	1	1	0101000020E6100000000000408BAD51C000000080357C3340
205	t	Infotep	\N	2024-07-02 17:54:33.876	2024-07-02 17:54:33.876	19.481514	\N	-70.70609	\N	\N	1	0101000020E6100000000000A030AD51C000000080447B3340
206	t	Unev	\N	2024-07-02 17:54:56.673	2024-07-02 17:54:56.673	19.479946	\N	-70.70372	\N	\N	1	0101000020E6100000000000C009AD51C0000000C0DD7A3340
207	t	DGII Miraflores	\N	2024-07-02 17:55:29.604	2024-07-02 17:55:29.604	19.474358	\N	-70.69458	\N	\N	1	0101000020E61000000000000074AC51C0000000806F793340
208	t	Alquiler de vehiculos	\N	2024-07-02 17:57:58.52	2024-07-02 17:57:58.52	19.473392	\N	-70.69351	\N	\N	1	0101000020E61000000000008062AC51C00000004030793340
209	t	Holas Pollo	\N	2024-07-02 17:58:36.103	2024-07-02 17:58:36.103	19.471304	\N	-70.69184	\N	\N	1	0101000020E61000000000002047AC51C000000060A7783340
210	t	Hache	\N	2024-07-02 17:59:06.5	2024-07-02 17:59:06.5	19.468916	\N	-70.6871	\N	\N	1	0101000020E610000000000080F9AB51C0000000E00A783340
211	t	Femenino	\N	2024-07-02 17:59:47.909	2024-07-02 17:59:47.909	19.463802	\N	-70.686584	\N	\N	1	0101000020E610000000000000F1AB51C0000000C0BB763340
212	t	Plaza	\N	2024-07-02 18:00:37.019	2024-07-02 18:00:37.019	19.461658	\N	-70.68603	\N	\N	1	0101000020E6100000000000E0E7AB51C0000000402F763340
213	t	Huacalito	\N	2024-07-02 18:01:29.226	2024-07-02 18:01:29.226	19.459959	\N	-70.68612	\N	\N	1	0101000020E610000000000060E9AB51C0000000E0BF753340
214	t	UTESA	\N	2024-07-02 18:02:06.871	2024-07-02 18:02:06.871	19.43421	\N	-70.69166	\N	\N	1	0101000020E61000000000002044AC51C000000060286F3340
215	t	Doble via Olimpica	\N	2024-07-02 18:03:14.464	2024-07-02 18:03:14.464	19.43661	\N	-70.68987	\N	\N	1	0101000020E6100000000000E026AC51C0000000A0C56F3340
\.


--
-- Data for Name: pda_rta; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.pda_rta (id, ruta_rta, "intersect") FROM stdin;
1	mariela	0.11717016393395936
152	mariela	0.7067829017075067
153	mariela	0.8185771527509593
\.


--
-- Data for Name: rta; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.rta (rta, act, upd_at, mde_at, loc_fin, loc_ini, actualizado_por_id, hecho_por_id) FROM stdin;
mariela2	t	2024-05-24 08:21:56.579	2024-05-24 08:21:56.579	adentro	afuera	\N	1
mariela3	t	2024-05-24 08:36:44.559	2024-05-24 08:36:44.559	sacalo	metelo	\N	1
mariela	t	2024-06-06 09:46:30.31	2024-05-24 08:12:09.097	preso saco	preso meto	1	1
mariela4	t	2024-07-02 11:55:15.551	2024-05-24 08:40:20.422	metelo metelooo	sacalo sacalo	1	1
,#3	t	2024-07-02 17:34:20.713	2024-07-02 17:34:20.713	La 27	La cienaga	\N	1
#2	t	2024-07-02 18:05:33.469	2024-07-02 11:47:51.721	Utesa	Lovera	1	1
\.


--
-- Data for Name: rta_ln; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.rta_ln (ruta_rta, geom) FROM stdin;
,#3	0102000020E61000000A000000B515FBCBEEAD51C09677D503E67D334059315C1D00AE51C027F911BF627D3340D50451F701AE51C0395E81E8497D33405EBA490C02AE51C08C4AEA04347D3340535A7F4B00AE51C01B81785DBF7C33405F0839EFFFAD51C0D2730B5D897C3340535A7F4B00AE51C01D3BA8C4757C33406EA301BC05AE51C010786000E17B334062F5471806AE51C0902FA182C37B33408813984EEBAD51C05CE509849D7A3340
#2	0102000020E610000062000000A8DF85ADD9AB51C0D960E124CD73334092054CE0D6AB51C0F01307D0EF7333408C48145AD6AB51C02C8029030774334081B4FF01D6AB51C0FCE4284014743340921FF12BD6AB51C0C2D9AD65327433402CD8463CD9AB51C097A949F0867433402A560DC2DCAB51C0A2B437F8C2743340A75D4C33DDAB51C072193735D074334030134548DDAB51C04303B16CE674334039F1D58EE2AB51C0CDE7DCED7A75334059C345EEE9AB51C0E46A64575A763340E010AAD4ECAB51C079793A5794763340E4654D2CF0AB51C0D6011077F57633407156444DF4AB51C092CCEA1D6E773340F17EDC7EF9AB51C0185C7347FF773340F607CA6DFBAB51C07CF31B261A7833407898F6CDFDAB51C07383A10E2B7833408E3EE60302AC51C06A1327F73B7833402575029A08AC51C0F3CB608C48783340B5E0455F41AC51C02F4E7CB5A3783340A7CAF78C44AC51C0E9263108AC7833409BE8F35146AC51C0465F419AB17833402827DA5548AC51C0B98B3045B9783340B0027CB779AC51C0CBB9145795793340CA3159DC7FAC51C03221E692AA793340931CB0ABC9AC51C0ECF7C43A557A3340FAB31F2922AD51C0EF39B01C217B33407CD2890453AD51C0CE55F31C917B334048E2E5E95CAD51C027DBC01DA87B334048E2E5E95CAD51C027DBC01DA87B33403259DC7F64AD51C0287D21E4BC7B33407A8D5DA27AAD51C0664AEB6F097C3340290989B48DAD51C087E123624A7C3340BD38F1D58EAD51C0124DA088457C3340C554FA0967AD51C0F92CCF83BB7B3340CEC7B5A162AD51C0717495EEAE7B3340B95510035DAD51C02B137EA99F7B3340176536C824AD51C05567B5C01E7B3340F5D72B2CB8AC51C0C3F01131257A3340598AE42B81AC51C058C7F143A579334049BBD1C77CAC51C07DE882FA967933404069A85148AC51C029AE2AFBAE783340B492567C43AC51C0C07630629F783340F73E558506AC51C0B1BFEC9E3C783340F9C08EFF02AC51C00261A75835783340EE940ED6FFAB51C033FCA71B287833406C04E275FDAB51C0E675C4211B783340EA5910CAFBAB51C0618DB3E908783340562AA8A8FAAB51C0BD6E1118EB7733404A5F0839EFAB51C0E3FBE25295763340BBECD79DEEAB51C0B284B5317676334053EC681CEAAB51C0B77D8FFAEB753340B1A371A8DFAB51C02E3D9AEAC9743340B1A371A8DFAB51C02E3D9AEAC9743340A2D45E44DBAB51C0022CF2EB877433402635B401D8AB51C0C879FF1F27743340205ED72FD8AB51C02C802903077433409D6516A1D8AB51C05B96AFCBF07333402A70B20DDCAB51C02B6A300DC3733340CB9E0436E7AB51C051FA42C87973334086E28E37F9AB51C0213D450E11733340AAEFFCA204AC51C0CF85915ED472334036785F950BAC51C0E335AFEAAC723340C3B645990DAC51C03352EFA99C723340408A3A730FAC51C0853E58C686723340B6BA9C1210AC51C072FDBB3E73723340C83F33880FAC51C07A6D3656627233403B014D840DAC51C017D68D77477233400725CCB4FDAB51C0D93D7958A8713340029CDEC5FBAB51C0A911FA997A713340FCDEA63FFBAB51C0BF4692205C7133407429AE2AFBAB51C02F698CD651713340FCAA5CA8FCAB51C0C07B478D097133408A1D8D43FDAB51C01F2DCE18E670334023BF7E880DAC51C0EB1A2D077A7033405AD6FD6321AC51C003D0285DFA6F3340A8E15B5837AC51C02F6F0ED76A6F3340E8A221E351AC51C0889FFF1EBC6E33408A39083A5AAC51C04FC939B1876E3340732B84D558AC51C057EE0566856E33405C001AA54BAC51C09D6516A1D86E33407ADFF8DA33AC51C005F9D9C8756F33404AED45B41DAC51C0B68311FB0470334097361C9606AC51C0ABEAE5779A70334079E6E5B0FBAB51C04303B16CE67033406FA0C03BF9AB51C0E8BCC62E51713340745DF8C1F9AB51C040DAFF006B713340FDF84B8BFAAB51C0CB9C2E8B89713340F6ED2422FCAB51C0689604A8A9713340240D6E6B0BAC51C0ECDADE6E49723340A0E062450DAC51C09E4319AA62723340287CB60E0EAC51C04E27D9EA72723340A69D9ACB0DAC51C0F7E5CC76857233409566F3380CAC51C0ECA529029C7233401893FE5E0AAC51C078B306EFAB723340D769A4A5F2AB51C0EF5696E82C73334018EB1B98DCAB51C034DAAA24B2733340919DB7B1D9AB51C097715303CD733340
mariela4	0102000020E61000001800000009DFFB1BB4AD51C0EC4D0CC9C97C33409CA6CF0EB8AD51C0A35698BED77C334018601F9DBAAD51C0E10D6954E07C3340287D21E4BCAD51C0E292E34EE97C334041446ADAC5AD51C004E44BA8E07C3340CAE2FE23D3AD51C072361D01DC7C334028EE7893DFAD51C0444E5FCFD77C3340D192C7D3F2AD51C0AD4B8DD0CF7C3340E02D90A0F8AD51C0946A9F8EC77C334055F65D11FCAD51C0DD611399B97C334031D28BDAFDAD51C0505436ACA97C3340EFAEB321FFAD51C0C26A2C616D7C3340715985CD00AE51C031B1F9B8367C3340715985CD00AE51C031B1F9B8367C3340C365153603AE51C0B6662B2FF97B33404BE7C3B304AE51C05B3FFD67CD7B3340C808A87004AE51C0D0B69A75C67B3340368FC360FEAD51C0641EF983817B33401AA721AAF0AD51C00C5A48C0E87A33401AA721AAF0AD51C00C5A48C0E87A33401BF5108DEEAD51C0179AEB34D27A334009DFFB1BB4AD51C0EC4D0CC9C97C3340A5164A26A7AD51C0575C1C959B7C334097FDBAD39DAD51C0D3BEB9BF7A7C3340
mariela	0102000020E610000008000000195932C7F2AD51C03C50A73CBA7D3340B1C398F4F7AD51C05B7A34D5937D3340B9533A58FFAD51C0C93B8732547D3340BF1072DEFFAD51C0810A47904A7D3340433D7D04FEAD51C0001B1021AE7C3340C51B9947FEAD51C064213A048E7C33404223D8B8FEAD51C041118B18767C33404792205C01AE51C0BB97FBE4287C3340
,Ruta 2	0102000020E610000062000000C30FCEA78EAD51C089B14CBF447C3340C554FA0967AD51C0F92CCF83BB7B3340CEC7B5A162AD51C0717495EEAE7B3340B95510035DAD51C02B137EA99F7B3340176536C824AD51C05567B5C01E7B3340F5D72B2CB8AC51C0C3F01131257A3340598AE42B81AC51C058C7F143A579334049BBD1C77CAC51C07DE882FA967933404069A85148AC51C029AE2AFBAE783340B492567C43AC51C0C07630629F783340F73E558506AC51C0B1BFEC9E3C783340F9C08EFF02AC51C00261A75835783340EE940ED6FFAB51C033FCA71B287833406C04E275FDAB51C0E675C4211B783340EA5910CAFBAB51C0618DB3E908783340562AA8A8FAAB51C0BD6E1118EB7733404A5F0839EFAB51C0E3FBE25295763340BBECD79DEEAB51C0B284B5317676334053EC681CEAAB51C0B77D8FFAEB753340B1A371A8DFAB51C02E3D9AEAC9743340B1A371A8DFAB51C02E3D9AEAC9743340A2D45E44DBAB51C0022CF2EB877433402635B401D8AB51C0C879FF1F27743340205ED72FD8AB51C02C802903077433409D6516A1D8AB51C05B96AFCBF07333402A70B20DDCAB51C02B6A300DC3733340CB9E0436E7AB51C051FA42C87973334086E28E37F9AB51C0213D450E11733340AAEFFCA204AC51C0CF85915ED472334036785F950BAC51C0E335AFEAAC723340C3B645990DAC51C03352EFA99C723340408A3A730FAC51C0853E58C686723340B6BA9C1210AC51C072FDBB3E73723340C83F33880FAC51C07A6D3656627233403B014D840DAC51C017D68D77477233400725CCB4FDAB51C0D93D7958A8713340029CDEC5FBAB51C0A911FA997A713340FCDEA63FFBAB51C0BF4692205C7133407429AE2AFBAB51C02F698CD651713340FCAA5CA8FCAB51C0C07B478D097133408A1D8D43FDAB51C01F2DCE18E670334023BF7E880DAC51C0EB1A2D077A7033405AD6FD6321AC51C003D0285DFA6F3340A8E15B5837AC51C02F6F0ED76A6F3340E8A221E351AC51C0889FFF1EBC6E33408A39083A5AAC51C04FC939B1876E3340732B84D558AC51C057EE0566856E33405C001AA54BAC51C09D6516A1D86E33407ADFF8DA33AC51C005F9D9C8756F33404AED45B41DAC51C0B68311FB0470334097361C9606AC51C0ABEAE5779A70334079E6E5B0FBAB51C04303B16CE67033406FA0C03BF9AB51C0E8BCC62E51713340745DF8C1F9AB51C040DAFF006B713340FDF84B8BFAAB51C0CB9C2E8B89713340F6ED2422FCAB51C0689604A8A9713340240D6E6B0BAC51C0ECDADE6E49723340A0E062450DAC51C09E4319AA62723340287CB60E0EAC51C04E27D9EA72723340A69D9ACB0DAC51C0F7E5CC76857233409566F3380CAC51C0ECA529029C7233401893FE5E0AAC51C078B306EFAB723340D769A4A5F2AB51C0EF5696E82C73334018EB1B98DCAB51C034DAAA24B2733340919DB7B1D9AB51C097715303CD73334092054CE0D6AB51C0F01307D0EF7333408C48145AD6AB51C02C8029030774334081B4FF01D6AB51C0FCE4284014743340921FF12BD6AB51C0C2D9AD65327433402CD8463CD9AB51C097A949F0867433402A560DC2DCAB51C0A2B437F8C2743340A75D4C33DDAB51C072193735D074334030134548DDAB51C04303B16CE674334039F1D58EE2AB51C0CDE7DCED7A75334059C345EEE9AB51C0E46A64575A763340E010AAD4ECAB51C079793A5794763340E4654D2CF0AB51C0D6011077F57633407156444DF4AB51C092CCEA1D6E773340F17EDC7EF9AB51C0185C7347FF773340F607CA6DFBAB51C07CF31B261A7833407898F6CDFDAB51C07383A10E2B7833408E3EE60302AC51C06A1327F73B7833402575029A08AC51C0F3CB608C48783340B5E0455F41AC51C02F4E7CB5A3783340A7CAF78C44AC51C0E9263108AC7833409BE8F35146AC51C0465F419AB17833402827DA5548AC51C0B98B3045B9783340B0027CB779AC51C0CBB9145795793340CA3159DC7FAC51C03221E692AA793340931CB0ABC9AC51C0ECF7C43A557A3340FAB31F2922AD51C0EF39B01C217B33407CD2890453AD51C0CE55F31C917B334048E2E5E95CAD51C027DBC01DA87B334048E2E5E95CAD51C027DBC01DA87B33403259DC7F64AD51C0287D21E4BC7B33407A8D5DA27AAD51C0664AEB6F097C3340290989B48DAD51C087E123624A7C3340C30FCEA78EAD51C089B14CBF447C3340
\.


--
-- Data for Name: rta_loc; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.rta_loc (id, lat, lon, ruta_rta, geom) FROM stdin;
450	19.487454	-70.714118	mariela4	0101000020E610000009DFFB1BB4AD51C0EC4D0CC9C97C3340
451	19.487667	-70.714359	mariela4	0101000020E61000009CA6CF0EB8AD51C0A35698BED77C3340
452	19.487798	-70.714515	mariela4	0101000020E610000018601F9DBAAD51C0E10D6954E07C3340
453	19.487935	-70.714654	mariela4	0101000020E6100000287D21E4BCAD51C0E292E34EE97C3340
454	19.487803	-70.715201	mariela4	0101000020E610000041446ADAC5AD51C004E44BA8E07C3340
455	19.487732	-70.716012	mariela4	0101000020E6100000CAE2FE23D3AD51C072361D01DC7C3340
456	19.487668	-70.716771	mariela4	0101000020E610000028EE7893DFAD51C0444E5FCFD77C3340
457	19.487546	-70.717946	mariela4	0101000020E6100000D192C7D3F2AD51C0AD4B8DD0CF7C3340
458	19.48742	-70.7183	mariela4	0101000020E6100000E02D90A0F8AD51C0946A9F8EC77C3340
459	19.487207	-70.71851	mariela4	0101000020E610000055F65D11FCAD51C0DD611399B97C3340
460	19.486964	-70.718619	mariela4	0101000020E610000031D28BDAFDAD51C0505436ACA97C3340
461	19.486044	-70.718697	mariela4	0101000020E6100000EFAEB321FFAD51C0C26A2C616D7C3340
462	19.48521	-70.718799	mariela4	0101000020E6100000715985CD00AE51C031B1F9B8367C3340
463	19.48521	-70.718799	mariela4	0101000020E6100000715985CD00AE51C031B1F9B8367C3340
464	19.484271	-70.718946	mariela4	0101000020E6100000C365153603AE51C0B6662B2FF97B3340
465	19.483603	-70.719037	mariela4	0101000020E61000004BE7C3B304AE51C05B3FFD67CD7B3340
466	19.483497	-70.719021	mariela4	0101000020E6100000C808A87004AE51C0D0B69A75C67B3340
467	19.482445	-70.718651	mariela4	0101000020E6100000368FC360FEAD51C0641EF983817B3340
468	19.480114	-70.717814	mariela4	0101000020E61000001AA721AAF0AD51C00C5A48C0E87A3340
469	19.480114	-70.717814	mariela4	0101000020E61000001AA721AAF0AD51C00C5A48C0E87A3340
470	19.47977	-70.717685	mariela4	0101000020E61000001BF5108DEEAD51C0179AEB34D27A3340
471	19.487454	-70.714118	mariela4	0101000020E610000009DFFB1BB4AD51C0EC4D0CC9C97C3340
472	19.486749	-70.713327	mariela4	0101000020E6100000A5164A26A7AD51C0575C1C959B7C3340
473	19.486248	-70.712758	mariela4	0101000020E610000097FDBAD39DAD51C0D3BEB9BF7A7C3340
602	19.491791	-70.7177	,#3	0101000020E6100000B515FBCBEEAD51C09677D503E67D3340
603	19.489788	-70.718757	,#3	0101000020E610000059315C1D00AE51C027F911BF627D3340
604	19.489409	-70.71887	,#3	0101000020E6100000D50451F701AE51C0395E81E8497D3340
605	19.489075	-70.718875	,#3	0101000020E61000005EBA490C02AE51C08C4AEA04347D3340
606	19.487295	-70.718768	,#3	0101000020E6100000535A7F4B00AE51C01B81785DBF7C3340
607	19.486471	-70.718746	,#3	0101000020E61000005F0839EFFFAD51C0D2730B5D897C3340
608	19.486172	-70.718768	,#3	0101000020E6100000535A7F4B00AE51C01D3BA8C4757C3340
609	19.483902	-70.7191	,#3	0101000020E61000006EA301BC05AE51C010786000E17B3340
610	19.483452	-70.719122	,#3	0101000020E610000062F5471806AE51C0902FA182C37B3340
611	19.478966	-70.717487	,#3	0101000020E61000008813984EEBAD51C05CE509849D7A3340
748	19.451937	-70.685339	#2	0101000020E610000018EB1B98DCAB51C034DAAA24B2733340
749	19.452347	-70.685162	#2	0101000020E6100000919DB7B1D9AB51C097715303CD733340
326	19.491123	-70.717943	mariela	0101000020E6100000195932C7F2AD51C03C50A73CBA7D3340
327	19.490537	-70.718259	mariela	0101000020E6100000B1C398F4F7AD51C05B7A34D5937D3340
328	19.489566	-70.71871	mariela	0101000020E6100000B9533A58FFAD51C0C93B8732547D3340
329	19.489419	-70.718742	mariela	0101000020E6100000BF1072DEFFAD51C0810A47904A7D3340
330	19.487032	-70.718629	mariela	0101000020E6100000433D7D04FEAD51C0001B1021AE7C3340
331	19.486542	-70.718645	mariela	0101000020E6100000C51B9947FEAD51C064213A048E7C3340
332	19.486177	-70.718672	mariela	0101000020E61000004223D8B8FEAD51C041118B18767C3340
333	19.484999	-70.718833	mariela	0101000020E61000004792205C01AE51C0BB97FBE4287C3340
652	19.452349	-70.685161	#2	0101000020E6100000A8DF85ADD9AB51C0D960E124CD733340
653	19.452878	-70.68499	#2	0101000020E610000092054CE0D6AB51C0F01307D0EF733340
654	19.453232	-70.684958	#2	0101000020E61000008C48145AD6AB51C02C80290307743340
655	19.453434	-70.684937	#2	0101000020E610000081B4FF01D6AB51C0FCE4284014743340
656	19.453894	-70.684947	#2	0101000020E6100000921FF12BD6AB51C0C2D9AD6532743340
657	19.455184	-70.685134	#2	0101000020E61000002CD8463CD9AB51C097A949F086743340
658	19.4561	-70.685349	#2	0101000020E61000002A560DC2DCAB51C0A2B437F8C2743340
659	19.456302	-70.685376	#2	0101000020E6100000A75D4C33DDAB51C072193735D0743340
660	19.456641	-70.685381	#2	0101000020E610000030134548DDAB51C04303B16CE6743340
661	19.458907	-70.685703	#2	0101000020E610000039F1D58EE2AB51C0CDE7DCED7A753340
662	19.462316	-70.686153	#2	0101000020E610000059C345EEE9AB51C0E46A64575A763340
663	19.463201	-70.68633	#2	0101000020E6100000E010AAD4ECAB51C079793A5794763340
664	19.464683	-70.686534	#2	0101000020E6100000E4654D2CF0AB51C0D6011077F5763340
665	19.466524	-70.686786	#2	0101000020E61000007156444DF4AB51C092CCEA1D6E773340
666	19.468739	-70.687103	#2	0101000020E6100000F17EDC7EF9AB51C0185C7347FF773340
667	19.469149	-70.687221	#2	0101000020E6100000F607CA6DFBAB51C07CF31B261A783340
668	19.469407	-70.687366	#2	0101000020E61000007898F6CDFDAB51C07383A10E2B783340
669	19.469665	-70.687623	#2	0101000020E61000008E3EE60302AC51C06A1327F73B783340
670	19.469857	-70.688025	#2	0101000020E61000002575029A08AC51C0F3CB608C48783340
671	19.471248	-70.69149	#2	0101000020E6100000B5E0455F41AC51C02F4E7CB5A3783340
672	19.471375	-70.691684	#2	0101000020E6100000A7CAF78C44AC51C0E9263108AC783340
673	19.47146	-70.691792	#2	0101000020E61000009BE8F35146AC51C0465F419AB1783340
674	19.471577	-70.691915	#2	0101000020E61000002827DA5548AC51C0B98B3045B9783340
675	19.474935	-70.694929	#2	0101000020E6100000B0027CB779AC51C0CBB9145795793340
676	19.475259	-70.695304	#2	0101000020E6100000CA3159DC7FAC51C03221E692AA793340
677	19.477863	-70.699809	#2	0101000020E6100000931CB0ABC9AC51C0ECF7C43A557A3340
678	19.480974	-70.70521	#2	0101000020E6100000FAB31F2922AD51C0EF39B01C217B3340
679	19.482683	-70.708192	#2	0101000020E61000007CD2890453AD51C0CE55F31C917B3340
680	19.483034	-70.708796	#2	0101000020E610000048E2E5E95CAD51C027DBC01DA87B3340
681	19.483034	-70.708796	#2	0101000020E610000048E2E5E95CAD51C027DBC01DA87B3340
682	19.483351	-70.709259	#2	0101000020E61000003259DC7F64AD51C0287D21E4BC7B3340
683	19.484519	-70.71061	#2	0101000020E61000007A8D5DA27AAD51C0664AEB6F097C3340
684	19.48551	-70.711774	#2	0101000020E6100000290989B48DAD51C087E123624A7C3340
685	19.485436	-70.711843	#2	0101000020E6100000BD38F1D58EAD51C0124DA088457C3340
686	19.48333	-70.709414	#2	0101000020E6100000C554FA0967AD51C0F92CCF83BB7B3340
687	19.483138	-70.709145	#2	0101000020E6100000CEC7B5A162AD51C0717495EEAE7B3340
688	19.482905	-70.708802	#2	0101000020E6100000B95510035DAD51C02B137EA99F7B3340
689	19.480938	-70.70537	#2	0101000020E6100000176536C824AD51C05567B5C01E7B3340
690	19.47713	-70.698741	#2	0101000020E6100000F5D72B2CB8AC51C0C3F01131257A3340
691	19.475178	-70.695384	#2	0101000020E6100000598AE42B81AC51C058C7F143A5793340
692	19.47496	-70.695116	#2	0101000020E610000049BBD1C77CAC51C07DE882FA96793340
693	19.47142	-70.691914	#2	0101000020E61000004069A85148AC51C029AE2AFBAE783340
694	19.471182	-70.691619	#2	0101000020E6100000B492567C43AC51C0C07630629F783340
695	19.469675	-70.687898	#2	0101000020E6100000F73E558506AC51C0B1BFEC9E3C783340
696	19.469564	-70.687683	#2	0101000020E6100000F9C08EFF02AC51C00261A75835783340
697	19.469362	-70.68749	#2	0101000020E6100000EE940ED6FFAB51C033FCA71B28783340
698	19.469164	-70.687345	#2	0101000020E61000006C04E275FDAB51C0E675C4211B783340
699	19.468886	-70.687243	#2	0101000020E6100000EA5910CAFBAB51C0618DB3E908783340
700	19.468431	-70.687174	#2	0101000020E6100000562AA8A8FAAB51C0BD6E1118EB773340
701	19.463216	-70.686476	#2	0101000020E61000004A5F0839EFAB51C0E3FBE25295763340
702	19.462741	-70.686439	#2	0101000020E6100000BBECD79DEEAB51C0B284B53176763340
703	19.460632	-70.686164	#2	0101000020E610000053EC681CEAAB51C0B77D8FFAEB753340
704	19.456206	-70.685526	#2	0101000020E6100000B1A371A8DFAB51C02E3D9AEAC9743340
705	19.456206	-70.685526	#2	0101000020E6100000B1A371A8DFAB51C02E3D9AEAC9743340
706	19.455199	-70.685258	#2	0101000020E6100000A2D45E44DBAB51C0022CF2EB87743340
707	19.453722	-70.685059	#2	0101000020E61000002635B401D8AB51C0C879FF1F27743340
708	19.453232	-70.68507	#2	0101000020E6100000205ED72FD8AB51C02C80290307743340
709	19.452893	-70.685097	#2	0101000020E61000009D6516A1D8AB51C05B96AFCBF0733340
710	19.452195	-70.685306	#2	0101000020E61000002A70B20DDCAB51C02B6A300DC3733340
711	19.451077	-70.685987	#2	0101000020E6100000CB9E0436E7AB51C051FA42C879733340
712	19.449479	-70.687086	#2	0101000020E610000086E28E37F9AB51C0213D450E11733340
713	19.448553	-70.687783	#2	0101000020E6100000AAEFFCA204AC51C0CF85915ED4723340
714	19.447951	-70.688207	#2	0101000020E610000036785F950BAC51C0E335AFEAAC723340
715	19.447703	-70.68833	#2	0101000020E6100000C3B645990DAC51C03352EFA99C723340
716	19.447369	-70.688443	#2	0101000020E6100000408A3A730FAC51C0853E58C686723340
717	19.447071	-70.688481	#2	0101000020E6100000B6BA9C1210AC51C072FDBB3E73723340
718	19.446813	-70.688448	#2	0101000020E6100000C83F33880FAC51C07A6D365662723340
719	19.446403	-70.688325	#2	0101000020E61000003B014D840DAC51C017D68D7747723340
720	19.443975	-70.68736	#2	0101000020E61000000725CCB4FDAB51C0D93D7958A8713340
721	19.443277	-70.687242	#2	0101000020E6100000029CDEC5FBAB51C0A911FA997A713340
722	19.442812	-70.68721	#2	0101000020E6100000FCDEA63FFBAB51C0BF4692205C713340
723	19.442655	-70.687205	#2	0101000020E61000007429AE2AFBAB51C02F698CD651713340
724	19.441552	-70.687296	#2	0101000020E6100000FCAA5CA8FCAB51C0C07B478D09713340
725	19.441011	-70.687333	#2	0101000020E61000008A1D8D43FDAB51C01F2DCE18E6703340
726	19.439362	-70.688326	#2	0101000020E610000023BF7E880DAC51C0EB1A2D077A703340
727	19.437414	-70.689538	#2	0101000020E61000005AD6FD6321AC51C003D0285DFA6F3340
728	19.435224	-70.690878	#2	0101000020E6100000A8E15B5837AC51C02F6F0ED76A6F3340
729	19.432558	-70.692498	#2	0101000020E6100000E8A221E351AC51C0889FFF1EBC6E3340
730	19.431758	-70.693007	#2	0101000020E61000008A39083A5AAC51C04FC939B1876E3340
731	19.431723	-70.692922	#2	0101000020E6100000732B84D558AC51C057EE0566856E3340
732	19.432993	-70.692117	#2	0101000020E61000005C001AA54BAC51C09D6516A1D86E3340
733	19.435391	-70.690665	#2	0101000020E61000007ADFF8DA33AC51C005F9D9C8756F3340
734	19.437576	-70.689313	#2	0101000020E61000004AED45B41DAC51C0B68311FB04703340
735	19.439857	-70.687902	#2	0101000020E610000097361C9606AC51C0ABEAE5779A703340
736	19.441016	-70.687237	#2	0101000020E610000079E6E5B0FBAB51C04303B16CE6703340
737	19.442645	-70.687087	#2	0101000020E61000006FA0C03BF9AB51C0E8BCC62E51713340
738	19.443039	-70.687119	#2	0101000020E6100000745DF8C1F9AB51C040DAFF006B713340
739	19.443505	-70.687167	#2	0101000020E6100000FDF84B8BFAAB51C0CB9C2E8B89713340
740	19.443995	-70.687264	#2	0101000020E6100000F6ED2422FCAB51C0689604A8A9713340
741	19.446433	-70.688197	#2	0101000020E6100000240D6E6B0BAC51C0ECDADE6E49723340
742	19.446818	-70.68831	#2	0101000020E6100000A0E062450DAC51C09E4319AA62723340
743	19.447066	-70.688358	#2	0101000020E6100000287CB60E0EAC51C04E27D9EA72723340
744	19.447349	-70.688342	#2	0101000020E6100000A69D9ACB0DAC51C0F7E5CC7685723340
745	19.447693	-70.688246	#2	0101000020E61000009566F3380CAC51C0ECA529029C723340
746	19.447936	-70.688133	#2	0101000020E61000001893FE5E0AAC51C078B306EFAB723340
747	19.449904	-70.686685	#2	0101000020E6100000D769A4A5F2AB51C0EF5696E82C733340
\.


--
-- Data for Name: trp_loc; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.trp_loc (id, reg_dt, lat, lon, placa_pl, geom, ruta_rta, prev_geom, prev_reg_dt, orientation) FROM stdin;
363	2024-04-19 19:52:21.579	19.481797930117573	-70.7184201383622	B9288949	0101000020E61000006CC37598FAAD51C0AE22F11B577B3340	mariela4	\N	\N	\N
362	2024-04-19 19:52:24.575	19.486986628206214	-70.7186291835761	A1547858	0101000020E6100000465A4205FEAD51C029BCD927AB7C3340	mariela	0101000020E61000006CC37598FAAD51C0AE22F11B577B3340	2024-04-19 19:52:21.579	177.824840324155
361	2024-04-19 19:52:21.579	19.481797930117573	-70.7184201383622	A1547858	0101000020E61000006CC37598FAAD51C0AE22F11B577B3340	mariela	\N	\N	\N
\.


--
-- Data for Name: vhl; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.vhl (pl, mke_at, cap_pax, upd_at, mde_at, actualizado_por_id, estado_dat, hecho_por_id, marca_dat, modelo_dat, tipo_vehiculo_dat, color_dat, act, tkn, ruta_rta) FROM stdin;
B9288949	0	0	2024-04-26 08:52:47.909	2024-04-12 19:13:36.646	1	Estacionado	1	Scania	OmniExpress	Autobus	Amarillo	t	\N	\N
A1547858	0	0	2024-04-26 16:44:07.754	2024-04-12 18:27:58.831	1	En Camino	1	Mercedes Benz	Citaro	Autobus	Verde	t	\N	mariela
\.


--
-- Name: acc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.acc_seq', 1, false);


--
-- Name: emp_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.emp_seq', 1, false);


--
-- Name: pda_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.pda_seq', 251, true);


--
-- Name: ppl_inf_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.ppl_inf_seq', 1, false);


--
-- Name: rta_asg_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.rta_asg_seq', 1, false);


--
-- Name: rta_loc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.rta_loc_seq', 751, true);


--
-- Name: rta_pda_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.rta_pda_seq', 1, false);


--
-- Name: trp_loc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.trp_loc_seq', 451, true);


--
-- Name: usr_acc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.usr_acc_seq', 401, true);


--
-- Name: usr_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.usr_seq', 1, false);


--
-- Name: emp emp_pkey; Type: CONSTRAINT; Schema: payroll; Owner: cabreu
--

ALTER TABLE ONLY payroll.emp
    ADD CONSTRAINT emp_pkey PRIMARY KEY (id);


--
-- Name: acc acc_pkey; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.acc
    ADD CONSTRAINT acc_pkey PRIMARY KEY (id);


--
-- Name: gnr_dat gnr_dat_pkey; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.gnr_dat
    ADD CONSTRAINT gnr_dat_pkey PRIMARY KEY (dat);


--
-- Name: ppl_inf ppl_inf_pkey; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.ppl_inf
    ADD CONSTRAINT ppl_inf_pkey PRIMARY KEY (id);


--
-- Name: sys_cnf sys_cnf_pkey; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.sys_cnf
    ADD CONSTRAINT sys_cnf_pkey PRIMARY KEY (cod);


--
-- Name: usr uk_bu1non5n4nocuy3e7tgi3to3k; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.usr
    ADD CONSTRAINT uk_bu1non5n4nocuy3e7tgi3to3k UNIQUE (persona_id);


--
-- Name: usr_acc usr_acc_pkey; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.usr_acc
    ADD CONSTRAINT usr_acc_pkey PRIMARY KEY (id);


--
-- Name: usr usr_pkey; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.usr
    ADD CONSTRAINT usr_pkey PRIMARY KEY (id);


--
-- Name: pda pda_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.pda
    ADD CONSTRAINT pda_pkey PRIMARY KEY (id);


--
-- Name: rta_loc rta_loc_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.rta_loc
    ADD CONSTRAINT rta_loc_pkey PRIMARY KEY (id);


--
-- Name: rta rta_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.rta
    ADD CONSTRAINT rta_pkey PRIMARY KEY (rta);


--
-- Name: trp_loc trp_loc_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.trp_loc
    ADD CONSTRAINT trp_loc_pkey PRIMARY KEY (id);


--
-- Name: vhl vhl_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT vhl_pkey PRIMARY KEY (pl);


--
-- Name: trp_loc_placa_pl_idx; Type: INDEX; Schema: transport; Owner: cabreu
--

CREATE INDEX trp_loc_placa_pl_idx ON transport.trp_loc USING btree (placa_pl, ruta_rta);


--
-- Name: vhl_pl_idx; Type: INDEX; Schema: transport; Owner: cabreu
--

CREATE INDEX vhl_pl_idx ON transport.vhl USING btree (pl, ruta_rta);


--
-- Name: gnr_dat gnr_dat_notify; Type: TRIGGER; Schema: public; Owner: cabreu
--

CREATE TRIGGER gnr_dat_notify AFTER INSERT OR DELETE OR UPDATE ON public.gnr_dat FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: usr usr_notify; Type: TRIGGER; Schema: public; Owner: cabreu
--

CREATE TRIGGER usr_notify AFTER INSERT OR DELETE OR UPDATE ON public.usr FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: pda parades_before_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER parades_before_insert BEFORE INSERT ON transport.pda FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


--
-- Name: pda parades_before_update; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER parades_before_update BEFORE UPDATE ON transport.pda FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


--
-- Name: pda pda_notify; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER pda_notify AFTER INSERT OR DELETE OR UPDATE ON transport.pda FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: pda refresh_pda_rtaxpda_after_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER refresh_pda_rtaxpda_after_insert BEFORE INSERT ON transport.pda FOR EACH ROW EXECUTE FUNCTION transport.refresh_pda_rtaxpda();


--
-- Name: pda refresh_pda_rtaxpda_after_update; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER refresh_pda_rtaxpda_after_update BEFORE UPDATE ON transport.pda FOR EACH ROW EXECUTE FUNCTION transport.refresh_pda_rtaxpda();


--
-- Name: rta_ln refresh_rta_rtaxpda_after_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER refresh_rta_rtaxpda_after_insert BEFORE INSERT ON transport.rta_ln FOR EACH ROW EXECUTE FUNCTION transport.refresh_rta_rtaxpda();


--
-- Name: rta_loc route_points_after_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER route_points_after_insert AFTER INSERT ON transport.rta_loc FOR EACH ROW EXECUTE FUNCTION transport.refresh_route_lines();


--
-- Name: rta_loc route_points_after_update; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER route_points_after_update AFTER UPDATE ON transport.rta_loc FOR EACH ROW EXECUTE FUNCTION transport.refresh_route_lines();


--
-- Name: rta_loc route_points_before_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER route_points_before_insert BEFORE INSERT ON transport.rta_loc FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


--
-- Name: rta_loc route_points_before_update; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER route_points_before_update BEFORE UPDATE ON transport.rta_loc FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


--
-- Name: rta rta_notify; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER rta_notify AFTER INSERT OR DELETE OR UPDATE ON transport.rta FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: trp_loc transport_loc_before_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER transport_loc_before_insert BEFORE INSERT ON transport.trp_loc FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


--
-- Name: trp_loc transport_loc_before_update; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER transport_loc_before_update BEFORE UPDATE ON transport.trp_loc FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


--
-- Name: trp_loc transport_loc_notify; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER transport_loc_notify BEFORE INSERT ON transport.trp_loc FOR EACH ROW EXECUTE FUNCTION transport.loc_notify_trigger();


--
-- Name: trp_loc transport_trp_loc_before_insert_prev; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER transport_trp_loc_before_insert_prev BEFORE INSERT ON transport.trp_loc FOR EACH ROW EXECUTE FUNCTION transport.update_prev_geom();


--
-- Name: vhl vhl_notify; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER vhl_notify AFTER INSERT OR DELETE OR UPDATE ON transport.vhl FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: ppl_inf fk2wgx4om2tperqiov1krlu8flr; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.ppl_inf
    ADD CONSTRAINT fk2wgx4om2tperqiov1krlu8flr FOREIGN KEY (sexo_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: gnr_dat fkgg156xffcc3fb7l66rkr56nss; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.gnr_dat
    ADD CONSTRAINT fkgg156xffcc3fb7l66rkr56nss FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: gnr_dat fkhjsoh9nfyg0og3n9h6xv5s6d6; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.gnr_dat
    ADD CONSTRAINT fkhjsoh9nfyg0og3n9h6xv5s6d6 FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- Name: sys_cnf fkhx87nwu6hrsx1tabgxndxjsua; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.sys_cnf
    ADD CONSTRAINT fkhx87nwu6hrsx1tabgxndxjsua FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: ppl_inf fkj19xjx5ftuy08uyasc71mkls0; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.ppl_inf
    ADD CONSTRAINT fkj19xjx5ftuy08uyasc71mkls0 FOREIGN KEY (tipo_sangre_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: ppl_inf fkjlka0cks52oio4s94bcwht1ot; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.ppl_inf
    ADD CONSTRAINT fkjlka0cks52oio4s94bcwht1ot FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: acc fkkbdi7wwysm80veeq490cf4ig9; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.acc
    ADD CONSTRAINT fkkbdi7wwysm80veeq490cf4ig9 FOREIGN KEY (pantalla_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: usr_acc fkkn2juqrv9ylqa7rhv26f5lepa; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.usr_acc
    ADD CONSTRAINT fkkn2juqrv9ylqa7rhv26f5lepa FOREIGN KEY (usuario_id) REFERENCES public.usr(id);


--
-- Name: acc fkldrwe1a3etyfpprlc7n8d451r; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.acc
    ADD CONSTRAINT fkldrwe1a3etyfpprlc7n8d451r FOREIGN KEY (tipo_dato_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: usr fknbrkutuan2c34quc2hefe6diq; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.usr
    ADD CONSTRAINT fknbrkutuan2c34quc2hefe6diq FOREIGN KEY (persona_id) REFERENCES public.ppl_inf(id);


--
-- Name: usr_acc fkqkt7pwqtra3vxkiikh76ug40l; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.usr_acc
    ADD CONSTRAINT fkqkt7pwqtra3vxkiikh76ug40l FOREIGN KEY (acceso_id) REFERENCES public.acc(id);


--
-- Name: ppl_inf fks02ppaa1i6n8bnj7n29b1t67p; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.ppl_inf
    ADD CONSTRAINT fks02ppaa1i6n8bnj7n29b1t67p FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- Name: trp_loc fk4cpcko6xlm0mvao9qe9ixpyj9; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.trp_loc
    ADD CONSTRAINT fk4cpcko6xlm0mvao9qe9ixpyj9 FOREIGN KEY (placa_pl) REFERENCES transport.vhl(pl);


--
-- Name: vhl fk6vvoptl7hcva4598m6ftm81jk; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fk6vvoptl7hcva4598m6ftm81jk FOREIGN KEY (tipo_vehiculo_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: vhl fk8432h7m2rmhn6ho9h6ufr5es4; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fk8432h7m2rmhn6ho9h6ufr5es4 FOREIGN KEY (modelo_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: pda fka0u7kvoh1gbif92tdkkum452s; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.pda
    ADD CONSTRAINT fka0u7kvoh1gbif92tdkkum452s FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- Name: pda fkcqa9qqy7mdrauue4jgnt35o4g; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.pda
    ADD CONSTRAINT fkcqa9qqy7mdrauue4jgnt35o4g FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: vhl fkcwq6460vys74frl69ief674g3; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fkcwq6460vys74frl69ief674g3 FOREIGN KEY (ruta_rta) REFERENCES transport.rta(rta);


--
-- Name: vhl fkglhvyx5ti0tuik3aikrnkt0k9; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fkglhvyx5ti0tuik3aikrnkt0k9 FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: vhl fkh2j9fqynak97tvc9r9814isfb; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fkh2j9fqynak97tvc9r9814isfb FOREIGN KEY (marca_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: vhl fkia7hodmttwiioyfbvx79aahgs; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fkia7hodmttwiioyfbvx79aahgs FOREIGN KEY (estado_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: rta fkirtythaaktexhak4mrtdqiqi7; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.rta
    ADD CONSTRAINT fkirtythaaktexhak4mrtdqiqi7 FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: trp_loc fkk70qxdx68odi7g0tq2fl8a0qv; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.trp_loc
    ADD CONSTRAINT fkk70qxdx68odi7g0tq2fl8a0qv FOREIGN KEY (ruta_rta) REFERENCES transport.rta(rta);


--
-- Name: vhl fklag4uwr49g3uyyi9gil66nfyj; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fklag4uwr49g3uyyi9gil66nfyj FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- Name: rta_loc fklduq37v82b6hgpsfmarj072j; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.rta_loc
    ADD CONSTRAINT fklduq37v82b6hgpsfmarj072j FOREIGN KEY (ruta_rta) REFERENCES transport.rta(rta);


--
-- Name: vhl fkst2tx0x2qjfglenosm28c0wh7; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT fkst2tx0x2qjfglenosm28c0wh7 FOREIGN KEY (color_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: rta fktmjgu0wy1r45g2phqblxfmalv; Type: FK CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.rta
    ADD CONSTRAINT fktmjgu0wy1r45g2phqblxfmalv FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- PostgreSQL database dump complete
--

