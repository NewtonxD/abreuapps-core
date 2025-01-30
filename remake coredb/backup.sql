--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

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
-- Name: inventory; Type: SCHEMA; Schema: -; Owner: cabreu
--

CREATE SCHEMA inventory;


ALTER SCHEMA inventory OWNER TO cabreu;

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
-- Name: get_conf_valor(character varying); Type: FUNCTION; Schema: public; Owner: cabreu
--

CREATE FUNCTION public.get_conf_valor(codigo character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$
declare a varchar;
	BEGIN
		select val into a from public.sys_cnf where cod=$1;
		return a;
	END;
$_$;


ALTER FUNCTION public.get_conf_valor(codigo character varying) OWNER TO cabreu;

--
-- Name: get_pub_reporte(date, date, character varying, boolean, boolean); Type: FUNCTION; Schema: public; Owner: cabreu
--

CREATE FUNCTION public.get_pub_reporte(fecha_desde date, fecha_hasta date, empresa_filtro character varying, incluir_completadas boolean, incluir_en_curso boolean) RETURNS TABLE(empresa character varying, titulo character varying, descripcion character varying, total_views integer, total_clicks integer, conversion_clicks numeric, periodo character varying, duracion_dias integer, estado character varying, fecha_inicio date, fecha_fin date)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT
        p.empresa_dat AS empresa,
        p.tit AS titulo,
        p.dsc AS descripcion,
        p.cnt_view AS total_views,
        p.cnt_clk AS total_clicks,
        CASE WHEN p.cnt_view > 0 THEN (p.cnt_clk::NUMERIC / p.cnt_view::NUMERIC) * 100 ELSE 0 END AS conversion_clicks,
        (CONCAT(p.dt_str, ' - ', p.dt_fin))::varchar AS periodo,
        CASE
            WHEN p.dt_fin <= fecha_hasta THEN  (p.dt_fin - p.dt_str)
            ELSE  (fecha_hasta - p.dt_str)
        END AS duracion_dias,
        (CASE
            WHEN p.act = TRUE AND p.dt_fin >= CURRENT_DATE THEN 'En Curso'
            WHEN p.act = FALSE OR p.dt_fin < CURRENT_DATE THEN 'Completada'
            ELSE 'En Curso'
        end)::varchar AS estado,
        p.dt_str AS fecha_inicio,
        CASE
            WHEN p.dt_fin <= fecha_hasta THEN p.dt_fin
            ELSE NULL
        END AS fecha_fin
    FROM
        public.pub p
    WHERE
        p.dt_str >= fecha_desde AND
        p.dt_str <= fecha_hasta AND
        (empresa_filtro IS NULL OR p.empresa_dat = empresa_filtro) AND
        ((incluir_completadas = TRUE AND p.act = FALSE) OR 
        (incluir_en_curso = TRUE AND p.act = TRUE))
    ORDER BY
        p.empresa_dat, p.dt_str;

END;
$$;


ALTER FUNCTION public.get_pub_reporte(fecha_desde date, fecha_hasta date, empresa_filtro character varying, incluir_completadas boolean, incluir_en_curso boolean) OWNER TO cabreu;

--
-- Name: get_reporte_encabezado(character varying, date, date); Type: FUNCTION; Schema: public; Owner: cabreu
--

CREATE FUNCTION public.get_reporte_encabezado(titulo_reporte character varying, fecha_desde date, fecha_hasta date) RETURNS TABLE(titulo character varying, desde date, hasta date, server_ip character varying, empresa character varying, direccion character varying)
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN QUERY
	select 
		$1 as titulo, 
		$2 as desde, 
		$3 as hasta,
		get_conf_valor('serverip') as server_ip,
		get_conf_valor('empresanombre') as empresa, 
		get_conf_valor('centraldir') as direccion;

END;
$_$;


ALTER FUNCTION public.get_reporte_encabezado(titulo_reporte character varying, fecha_desde date, fecha_hasta date) OWNER TO cabreu;

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
		            WHEN loc_x <= loc_y THEN 
		                ST_Length(ST_LineSubstring(r.geom, np.loc_x,np.loc_y)::geography)
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
	
	if last_velocity<=3 then
		last_velocity:=3;
	end if;

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
-- Name: log_vhl_trigger(); Type: FUNCTION; Schema: transport; Owner: cabreu
--

CREATE FUNCTION transport.log_vhl_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare 
		dlat double precision;
		dlon double precision;
	begin
		
		if old.estado_dat != new.estado_dat then
			select tl.lat, tl.lon into dlat, dlon 
			from transport.trp_loc tl 
			where tl.ruta_rta=old.ruta_rta and tl.placa_pl=old.pl
			order by tl.reg_dt desc limit 1;
		
			insert into transport.vhl_log(est_new,est_old,lat,lon,pl,rta_old,rta_new,sys) values
			(new.estado_dat,old.estado_dat,dlat,dlon,new.pl,old.ruta_rta,new.ruta_rta,new.tkn='n/a');
			
		end if;
	
		if new.tkn='n/a' then 
			new.tkn:='';
		end if;
	
		return new;
	END;
$$;


ALTER FUNCTION transport.log_vhl_trigger() OWNER TO cabreu;

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
		JOIN transport.rta_ln rl ON ST_DWithin(p.geom,rl.geom::geography, 7)
		join transport.rta r on r.rta=rl.ruta_rta and r.act
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
   
   DELETE FROM transport.pda_rta  WHERE ruta_rta = NEW.ruta_rta;
		
	insert into transport.pda_rta  
	SELECT p.id,rl.ruta_rta,ST_LineLocatePoint(rl.geom, p.geom)
	FROM transport.pda p
	JOIN transport.rta_ln rl ON ST_DWithin(p.geom,rl.geom::geography, 7)
	join transport.rta r on r.rta=rl.ruta_rta and r.act
	where p.act and rl.ruta_rta = new.ruta_rta order by 1,2;	
    
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
		JOIN transport.rta_ln rl ON ST_DWithin(p.geom,rl.geom::geography, 7)
		join transport.rta r on r.rta=rl.ruta_rta and r.act
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
-- Name: prd; Type: TABLE; Schema: inventory; Owner: cabreu
--

CREATE TABLE inventory.prd (
    id integer NOT NULL,
    act boolean,
    dsc character varying(255),
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    pho character varying(255),
    nom character varying(255),
    prc_sel real,
    actualizado_por_id integer,
    categoria_dat character varying(255),
    hecho_por_id integer
);


ALTER TABLE inventory.prd OWNER TO cabreu;

--
-- Name: sell; Type: TABLE; Schema: inventory; Owner: cabreu
--

CREATE TABLE inventory.sell (
    id bigint NOT NULL,
    act boolean,
    cnt_art integer,
    mde_at timestamp(6) without time zone,
    amo_dsc real,
    amo_imp real,
    amo_pay real,
    amo_tot real,
    hecho_por_id integer
);


ALTER TABLE inventory.sell OWNER TO cabreu;

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
    sex character varying(255),
    puesto_dat character varying(255),
    sexo_dat character varying(255)
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


ALTER SEQUENCE public.acc_seq OWNER TO cabreu;

--
-- Name: emp_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.emp_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.emp_seq OWNER TO cabreu;

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


ALTER SEQUENCE public.pda_seq OWNER TO cabreu;

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


ALTER SEQUENCE public.ppl_inf_seq OWNER TO cabreu;

--
-- Name: prd_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.prd_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.prd_seq OWNER TO cabreu;

--
-- Name: pub; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.pub (
    id bigint NOT NULL,
    tit character varying(255) NOT NULL,
    dsc character varying(255),
    img_vid character varying(255),
    lnk_dst character varying(255) NOT NULL,
    dt_str date NOT NULL,
    dt_fin date NOT NULL,
    cnt_clk integer DEFAULT 0,
    cnt_view integer DEFAULT 0,
    act boolean DEFAULT true NOT NULL,
    upd_at timestamp(6) without time zone,
    mde_at timestamp(6) without time zone,
    actualizado_por_id integer,
    hecho_por_id integer,
    empresa_dat character varying(255)
);


ALTER TABLE public.pub OWNER TO cabreu;

--
-- Name: pub_id_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.pub_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pub_id_seq OWNER TO cabreu;

--
-- Name: pub_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabreu
--

ALTER SEQUENCE public.pub_id_seq OWNED BY public.pub.id;


--
-- Name: pub_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.pub_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pub_seq OWNER TO cabreu;

--
-- Name: rta_asg_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.rta_asg_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rta_asg_seq OWNER TO cabreu;

--
-- Name: rta_loc_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.rta_loc_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rta_loc_seq OWNER TO cabreu;

--
-- Name: rta_pda_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.rta_pda_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rta_pda_seq OWNER TO cabreu;

--
-- Name: sell_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.sell_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sell_seq OWNER TO cabreu;

--
-- Name: sta; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.sta (
    id bigint NOT NULL,
    dt date NOT NULL,
    cnt_viw integer DEFAULT 0 NOT NULL,
    reg_at timestamp(6) without time zone DEFAULT now()
);


ALTER TABLE public.sta OWNER TO cabreu;

--
-- Name: sta_id_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.sta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sta_id_seq OWNER TO cabreu;

--
-- Name: sta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabreu
--

ALTER SEQUENCE public.sta_id_seq OWNED BY public.sta.id;


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


ALTER SEQUENCE public.trp_loc_seq OWNER TO cabreu;

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


ALTER SEQUENCE public.usr_acc_seq OWNER TO cabreu;

--
-- Name: usr_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.usr_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usr_seq OWNER TO cabreu;

--
-- Name: vhl_log_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.vhl_log_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vhl_log_seq OWNER TO cabreu;

--
-- Name: vis_log; Type: TABLE; Schema: public; Owner: cabreu
--

CREATE TABLE public.vis_log (
    id bigint NOT NULL,
    dt timestamp(6) without time zone DEFAULT now()
);


ALTER TABLE public.vis_log OWNER TO cabreu;

--
-- Name: vis_log_id_seq; Type: SEQUENCE; Schema: public; Owner: cabreu
--

CREATE SEQUENCE public.vis_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vis_log_id_seq OWNER TO cabreu;

--
-- Name: vis_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabreu
--

ALTER SEQUENCE public.vis_log_id_seq OWNED BY public.vis_log.id;


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
    geom public.geometry
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
    geom public.geometry
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
    ruta_rta character varying(255),
    prev_reg_dt timestamp(6) without time zone,
    orientation double precision,
    geom public.geometry,
    prev_geom public.geometry
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
-- Name: vhl_log; Type: TABLE; Schema: transport; Owner: cabreu
--

CREATE TABLE transport.vhl_log (
    id bigint NOT NULL,
    est_new character varying(255),
    est_old character varying(255),
    reg_dt timestamp(6) without time zone DEFAULT now(),
    lat double precision,
    lon double precision,
    pl character varying(255),
    rta_old character varying(255),
    rta_new character varying(255),
    sys boolean
);


ALTER TABLE transport.vhl_log OWNER TO cabreu;

--
-- Name: vhl_log_id_seq; Type: SEQUENCE; Schema: transport; Owner: cabreu
--

ALTER TABLE transport.vhl_log ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME transport.vhl_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: pub id; Type: DEFAULT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.pub ALTER COLUMN id SET DEFAULT nextval('public.pub_id_seq'::regclass);


--
-- Name: sta id; Type: DEFAULT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.sta ALTER COLUMN id SET DEFAULT nextval('public.sta_id_seq'::regclass);


--
-- Name: vis_log id; Type: DEFAULT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.vis_log ALTER COLUMN id SET DEFAULT nextval('public.vis_log_id_seq'::regclass);


--
-- Data for Name: prd; Type: TABLE DATA; Schema: inventory; Owner: cabreu
--

COPY inventory.prd (id, act, dsc, upd_at, mde_at, pho, nom, prc_sel, actualizado_por_id, categoria_dat, hecho_por_id) FROM stdin;
2	t		2024-10-12 08:05:21.4	2024-10-12 08:05:21.4	343pxLatexrealnumbers.svg120241012080521.png	Mero	100	\N	Pescado	1
1	t		2024-10-12 08:36:01.622	2024-10-12 07:57:56.95	Pitagoras220241012075756.jpg	Mero con Frito	250	1	Platillos	1
\.


--
-- Data for Name: sell; Type: TABLE DATA; Schema: inventory; Owner: cabreu
--

COPY inventory.sell (id, act, cnt_art, mde_at, amo_dsc, amo_imp, amo_pay, amo_tot, hecho_por_id) FROM stdin;
\.


--
-- Data for Name: emp; Type: TABLE DATA; Schema: payroll; Owner: cabreu
--

COPY payroll.emp (id, act, ap, ced, dt_ini, dt_fin, nam, pst, sex, puesto_dat, sexo_dat) FROM stdin;
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
37	t	\N	pub_principal	Booleano
38	t	pub_principal	pub_publicidad_consulta	Booleano
39	t	pub_principal	pub_publicidad_reportes	Booleano
40	t	pub_publicidad_consulta	pub_publicidad_registro	Booleano
41	t	pub_principal	dat_gen_consulta_empresa	Booleano
42	t	dat_gen_consulta_empresa	dat_gen_registro_empresa	Booleano
43	t	\N	inv_principal	Booleano
44	t	inv_principal	inv_producto_consulta	Booleano
45	t	inv_producto_consulta	inv_producto_registro	Booleano
\.


--
-- Data for Name: gnr_dat; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.gnr_dat (dat, act, dsc, upd_at, mde_at, actualizado_por_id, hecho_por_id, fat_dat) FROM stdin;
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
pub_publicidad_reportes	t	Reportes	2024-08-08 07:26:05.63	2024-08-08 07:26:05.63	\N	1	Menu
pub_principal	t	Publicidad	2024-08-08 07:22:31.387	2024-08-08 07:22:31.387	\N	1	Modulo
pub_publicidad_consulta	t	Consulta	2024-08-08 07:24:11.05	2024-08-08 07:24:11.05	\N	1	Menu
Bicicleta	t	Bicicleta.	2024-06-13 09:52:54.255	2024-06-13 09:52:54.255	\N	1	Tipo Vehiculo
Tipo de Datos	t	Describe los Tipo de Datos manejados en el sistema	2023-08-23 12:25:52	2023-08-23 12:25:52	\N	1	\N
pub_publicidad_registro	t	Registro	2024-08-08 07:27:06.913	2024-08-08 07:27:06.913	\N	1	Pantallas
Empresas	t	Categoria para las empresas listadas	\N	2024-08-10 15:19:35.295343	\N	1	\N
Leche Rica	t	Leche Rica Empresa	\N	2024-08-10 15:20:26.749641	\N	1	Empresas
dat_gen_consulta_empresa	t	Consulta Empresas	2024-08-13 09:33:32.091	2024-08-13 09:33:32.091	\N	1	Menu
dat_gen_registro_empresa	t	Registro	2024-08-13 09:33:53.485	2024-08-13 09:33:53.485	\N	1	Pantallas
United Tobacco Company	t	Tabaco zona	2024-08-13 09:37:59.053	2024-08-13 09:37:59.053	\N	1	Empresas
inv_principal	t	Inventario	2024-10-08 12:00:01.941	2024-10-08 12:00:01.941	\N	1	Modulo
inv_producto_consulta	t	Producto	2024-10-08 12:00:01.941	2024-10-08 12:00:01.941	\N	1	Menu
inv_producto_registro	t	Registro	2024-10-08 12:00:01.941	2024-10-08 12:00:01.941	\N	1	Pantalla
Categoria Producto	t	Categoria Producto	2024-10-12 07:36:21.734	2024-10-12 07:36:21.734	\N	1	\N
Plasticos	t	Plasticos y etc	2024-10-12 07:36:47.431	2024-10-12 07:36:47.431	\N	1	Categoria Producto
Platillos	t	Platillos de comida	2024-10-12 07:37:04.888	2024-10-12 07:37:04.888	\N	1	Categoria Producto
Mariscos	t	Mariscos	2024-10-12 07:37:23.982	2024-10-12 07:37:23.982	\N	1	Categoria Producto
Pescado	t	Pescado	2024-10-12 07:37:55.085	2024-10-12 07:37:55.085	\N	1	Categoria Producto
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
-- Data for Name: pub; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.pub (id, tit, dsc, img_vid, lnk_dst, dt_str, dt_fin, cnt_clk, cnt_view, act, upd_at, mde_at, actualizado_por_id, hecho_por_id, empresa_dat) FROM stdin;
52	La Cabra Lola	Guardando la Cabra Lola	GrabLifeByTheHornsIsJustAFigureOfSpeech120240812134146.mp4	https://www.google.com	2024-08-14	2024-08-17	1	29	f	2024-08-14 17:03:33.698	2024-08-12 13:41:46.467	1	1	Leche Rica
2	La Vaca Lola	Para saber algo mas...	Screenshotfrom2024080508172820240810181757.png	https://www.google.com	2024-08-12	2024-08-13	7	12	f	2024-08-12 14:03:57.542	2024-08-10 18:17:57.402	1	1	Leche Rica
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: sta; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.sta (id, dt, cnt_viw, reg_at) FROM stdin;
115	2024-08-13	2	2024-08-17 21:08:32.85065
116	2024-08-14	17	2024-08-17 21:08:32.85065
117	2024-08-16	2	2024-08-17 21:08:32.85065
149	2024-10-04	4	2024-10-12 08:34:30.004478
150	2024-10-08	1	2024-10-12 08:34:30.004478
158	2024-12-04	1	2024-12-05 17:48:29.184275
\.


--
-- Data for Name: sys_cnf; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.sys_cnf (cod, dsc, upd_at, val, actualizado_por_id) FROM stdin;
maptilesdir	Tiles directory	2024-08-17 10:34:35.843641	./../tiles/TilesRD	1
filesdir	Files directory	2024-08-17 10:35:13.736776	./../files	1
centrallatlng	Latitud,Longitud Central	2024-08-17 17:35:12.88378	19.488365437890657,-70.71529535723246	1
appprovincia	Provincia de la app	2024-08-17 17:47:07.249	Santiago	1
centraldir	Dirección Central	2024-08-17 17:33:21.239978	Av. Estrella Sadhala, Santiago de los Caballeros, República Dominicana.	1
serverip	Server IP	2024-10-04 15:28:34.117	http://localhost:8090	1
appnombre	Nombre del sistema	2024-07-26 07:22:37.453	El Mago	1
empresanombre	Nombre Empresa (reportes)	2024-08-17 18:02:18.779302	El Mago ERP System	1
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
402	t	true	37	1
403	t	true	38	1
404	t	true	40	1
405	t	true	39	1
452	t	true	41	1
453	t	true	42	1
502	t	true	43	1
503	t	true	44	1
504	t	true	45	1
\.


--
-- Data for Name: vis_log; Type: TABLE DATA; Schema: public; Owner: cabreu
--

COPY public.vis_log (id, dt) FROM stdin;
43	2024-12-04 10:26:17.329067
\.


--
-- Data for Name: pda; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.pda (id, act, dsc, dsc_ptr, upd_at, mde_at, lat, lat_ptr, lon, lon_ptr, actualizado_por_id, hecho_por_id, geom) FROM stdin;
210	t	Hache	\N	2024-07-03 10:26:42.57	2024-07-02 17:59:06.5	19.468927	\N	-70.68712	\N	1	1	0101000020E6100000000000C0F9AB51C0000000A00B783340
2	t	Parada la cienaga 2	\N	2024-05-08 18:41:19.304	2024-05-08 07:31:53.566	19.490173	\N	-70.71864	\N	1	1	0101000020E610000000000040FEAD51C0000000007C7D3340
102	t	parada curva 3	\N	2024-06-06 09:37:01.255	2024-06-06 09:03:48.894	19.487822	\N	-70.71605	\N	1	1	0101000020E6100000000000C0D3AD51C0000000E0E17C3340
52	t	parada en la curva	\N	2024-06-06 09:37:13.547	2024-06-06 08:52:09.845	19.487614	\N	-70.71695	\N	1	1	0101000020E610000000000080E2AD51C000000040D47C3340
53	t	parada en la curva2	\N	2024-06-06 09:37:28.232	2024-06-06 08:57:28.159	19.487902	\N	-70.71468	\N	1	1	0101000020E610000000000060BDAD51C000000020E77C3340
152	t	parada 88 roostece	\N	2024-06-06 09:37:56.792	2024-06-06 09:37:56.792	19.48684	\N	-70.7186	\N	\N	1	0101000020E610000000000080FDAD51C000000080A17C3340
1	t	Parada La cienega 1	\N	2024-06-06 09:47:16.326	2024-05-07 07:31:01.71	19.490456	\N	-70.71826	\N	1	1	0101000020E610000000000000F8AD51C0000000808E7D3340
153	t	la2777	\N	2024-06-06 09:48:09.286	2024-06-06 09:48:09.286	19.486132	\N	-70.71865	\N	\N	1	0101000020E610000000000060FEAD51C000000020737C3340
204	t	Safiro Club	\N	2024-07-02 17:53:36.887	2024-07-02 17:53:36.887	19.484968	\N	-70.71108	\N	\N	1	0101000020E61000000000006082AD51C0000000E0267C3340
203	t	Lovera	\N	2024-07-02 17:53:53.729	2024-07-02 17:52:32.843	19.485191	\N	-70.711624	\N	1	1	0101000020E6100000000000408BAD51C000000080357C3340
206	t	Unev	\N	2024-07-02 17:54:56.673	2024-07-02 17:54:56.673	19.479946	\N	-70.70372	\N	\N	1	0101000020E6100000000000C009AD51C0000000C0DD7A3340
207	t	DGII Miraflores	\N	2024-07-02 17:55:29.604	2024-07-02 17:55:29.604	19.474358	\N	-70.69458	\N	\N	1	0101000020E61000000000000074AC51C0000000806F793340
208	t	Alquiler de vehiculos	\N	2024-07-02 17:57:58.52	2024-07-02 17:57:58.52	19.473392	\N	-70.69351	\N	\N	1	0101000020E61000000000008062AC51C00000004030793340
209	t	Holas Pollo	\N	2024-07-02 17:58:36.103	2024-07-02 17:58:36.103	19.471304	\N	-70.69184	\N	\N	1	0101000020E61000000000002047AC51C000000060A7783340
211	t	Femenino	\N	2024-07-02 17:59:47.909	2024-07-02 17:59:47.909	19.463802	\N	-70.686584	\N	\N	1	0101000020E610000000000000F1AB51C0000000C0BB763340
212	t	Plaza	\N	2024-07-02 18:00:37.019	2024-07-02 18:00:37.019	19.461658	\N	-70.68603	\N	\N	1	0101000020E6100000000000E0E7AB51C0000000402F763340
213	t	Huacalito	\N	2024-07-02 18:01:29.226	2024-07-02 18:01:29.226	19.459959	\N	-70.68612	\N	\N	1	0101000020E610000000000060E9AB51C0000000E0BF753340
214	t	UTESA	\N	2024-07-02 18:02:06.871	2024-07-02 18:02:06.871	19.43421	\N	-70.69166	\N	\N	1	0101000020E61000000000002044AC51C000000060286F3340
215	t	Doble via Olimpica	\N	2024-07-02 18:03:14.464	2024-07-02 18:03:14.464	19.43661	\N	-70.68987	\N	\N	1	0101000020E6100000000000E026AC51C0000000A0C56F3340
205	t	Infotep	\N	2024-07-03 10:04:57.337	2024-07-02 17:54:33.876	19.481516	\N	-70.706085	\N	1	1	0101000020E61000000000008030AD51C0000000A0447B3340
\.


--
-- Data for Name: pda_rta; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.pda_rta (id, ruta_rta, "intersect") FROM stdin;
52	mariela4	0.13386465799280925
53	mariela4	0.033312265726148114
152	mariela4	0.22459267427733964
153	mariela4	0.25581802857062486
1	mariela	0.11717016393395936
152	mariela	0.7067829017075067
153	mariela	0.8185771527509593
210	,corta	0.9645085846328765
212	,corta	0.6025299956535299
203	#2	0.34551342128305096
204	#2	0.3360675158718588
205	#2	0.29186613140581896
206	#2	0.41440531174485856
207	#2	0.49227808682285984
208	#2	0.18285922084191733
209	#2	0.5219397681063633
210	#2	0.12466331909700652
211	#2	0.5976251563529692
212	#2	0.0714612232802936
213	#2	0.6256555104410605
215	#2	0.8932005569334924
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
#2	t	2024-07-03 10:16:38.488	2024-07-02 11:47:51.721	Utesa	Lovera	1	1
,corta	t	2024-07-03 10:24:21.935	2024-07-03 10:24:21.935	pucmm	utesa	\N	1
,#4	t	2024-07-09 07:25:35.207	2024-07-09 07:25:35.207	Yapur Dumit	Pekin	\N	1
,#5	t	2024-07-09 07:29:12.511	2024-07-09 07:29:12.511	Las carreras	La junta	\N	1
#6	t	2024-07-09 07:35:23.159	2024-07-09 07:35:23.159	colinas mall	aprezio 	\N	1
\.


--
-- Data for Name: rta_ln; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.rta_ln (ruta_rta, geom) FROM stdin;
,Ruta 2	\N
mariela4	0102000020E61000001800000009DFFB1BB4AD51C0EC4D0CC9C97C33409CA6CF0EB8AD51C0A35698BED77C334018601F9DBAAD51C0E10D6954E07C3340287D21E4BCAD51C0E292E34EE97C334041446ADAC5AD51C004E44BA8E07C3340CAE2FE23D3AD51C072361D01DC7C334028EE7893DFAD51C0444E5FCFD77C3340D192C7D3F2AD51C0AD4B8DD0CF7C3340E02D90A0F8AD51C0946A9F8EC77C334055F65D11FCAD51C0DD611399B97C334031D28BDAFDAD51C0505436ACA97C3340EFAEB321FFAD51C0C26A2C616D7C3340715985CD00AE51C031B1F9B8367C3340715985CD00AE51C031B1F9B8367C3340C365153603AE51C0B6662B2FF97B33404BE7C3B304AE51C05B3FFD67CD7B3340C808A87004AE51C0D0B69A75C67B3340368FC360FEAD51C0641EF983817B33401AA721AAF0AD51C00C5A48C0E87A33401AA721AAF0AD51C00C5A48C0E87A33401BF5108DEEAD51C0179AEB34D27A334009DFFB1BB4AD51C0EC4D0CC9C97C3340A5164A26A7AD51C0575C1C959B7C334097FDBAD39DAD51C0D3BEB9BF7A7C3340
,#3	0102000020E61000000A000000B515FBCBEEAD51C09677D503E67D334059315C1D00AE51C027F911BF627D3340D50451F701AE51C0395E81E8497D33405EBA490C02AE51C08C4AEA04347D3340535A7F4B00AE51C01B81785DBF7C33405F0839EFFFAD51C0D2730B5D897C3340535A7F4B00AE51C01D3BA8C4757C33406EA301BC05AE51C010786000E17B334062F5471806AE51C0902FA182C37B33408813984EEBAD51C05CE509849D7A3340
mariela	0102000020E610000008000000195932C7F2AD51C03C50A73CBA7D3340B1C398F4F7AD51C05B7A34D5937D3340B9533A58FFAD51C0C93B8732547D3340BF1072DEFFAD51C0810A47904A7D3340433D7D04FEAD51C0001B1021AE7C3340C51B9947FEAD51C064213A048E7C33404223D8B8FEAD51C041118B18767C33404792205C01AE51C0BB97FBE4287C3340
,corta	0102000020E61000000F000000A8FFACF9F1AB51C03A58FFE730733340EE3D5C72DCAB51C0632AFD84B37333406DE179A9D8AB51C032C9C859D873334068588CBAD6AB51C06E35EB8CEF73334069723106D6AB51C08750A5660F7433405DC47762D6AB51C06C7BBB2539743340F0BF95ECD8AB51C046EBA86A8274334077D9AF3BDDAB51C0AFB321FFCC7433407102D369DDAB51C03621AD31E8743340DCBB067DE9AB51C03387A4164A763340D00D4DD9E9AB51C0E19A3BFA5F763340DA39CD02EDAB51C0C47AA35698763340F17EDC7EF9AB51C04A7CEE04FB773340FCDEA63FFBAB51C03447567E197833400CAEB9A3FFAB51C0BBB4E1B034783340
,#4	0102000020E61000000200000015C5ABAC6DAC51C0EA3F6B7EFC6D3340C58EC6A17EAC51C0AE8383BD896D3340
,#5	0102000020E610000006000000AAB706B64AAD51C0D1E7A38CB874334083893F8A3AAD51C01E34BBEEAD7433406687F8872DAD51C0E71C3C139A74334032AB77B81DAD51C03E5E48878774334041102043C7AC51C0D55B035B2574334009A9DBD957AC51C04F5DF92CCF733340
#2	0102000020E61000006200000018EB1B98DCAB51C034DAAA24B2733340919DB7B1D9AB51C097715303CD733340A8DF85ADD9AB51C0D960E124CD73334092054CE0D6AB51C0F01307D0EF7333408C48145AD6AB51C02C8029030774334081B4FF01D6AB51C0FCE4284014743340921FF12BD6AB51C0C2D9AD65327433402CD8463CD9AB51C097A949F0867433402A560DC2DCAB51C0A2B437F8C2743340A75D4C33DDAB51C072193735D074334030134548DDAB51C04303B16CE674334039F1D58EE2AB51C0CDE7DCED7A75334059C345EEE9AB51C0E46A64575A763340E010AAD4ECAB51C079793A5794763340E4654D2CF0AB51C0D6011077F57633407156444DF4AB51C092CCEA1D6E773340F17EDC7EF9AB51C0185C7347FF773340F607CA6DFBAB51C07CF31B261A7833407898F6CDFDAB51C07383A10E2B7833408E3EE60302AC51C06A1327F73B7833402575029A08AC51C0F3CB608C48783340B5E0455F41AC51C02F4E7CB5A3783340A7CAF78C44AC51C0E9263108AC7833409BE8F35146AC51C0465F419AB17833402827DA5548AC51C0B98B3045B9783340B0027CB779AC51C0CBB9145795793340CA3159DC7FAC51C03221E692AA793340931CB0ABC9AC51C0ECF7C43A557A3340FAB31F2922AD51C0EF39B01C217B33407CD2890453AD51C0CE55F31C917B334048E2E5E95CAD51C027DBC01DA87B334048E2E5E95CAD51C027DBC01DA87B33403259DC7F64AD51C0287D21E4BC7B33407A8D5DA27AAD51C0664AEB6F097C3340290989B48DAD51C087E123624A7C3340BD38F1D58EAD51C0124DA088457C3340C554FA0967AD51C0F92CCF83BB7B3340CEC7B5A162AD51C0717495EEAE7B3340B95510035DAD51C02B137EA99F7B3340176536C824AD51C05567B5C01E7B3340F5D72B2CB8AC51C0C3F01131257A3340598AE42B81AC51C058C7F143A579334049BBD1C77CAC51C07DE882FA967933404069A85148AC51C029AE2AFBAE783340B492567C43AC51C0C07630629F783340F73E558506AC51C0B1BFEC9E3C783340F9C08EFF02AC51C00261A75835783340EE940ED6FFAB51C033FCA71B287833406C04E275FDAB51C0E675C4211B783340EA5910CAFBAB51C0618DB3E908783340562AA8A8FAAB51C0BD6E1118EB7733404A5F0839EFAB51C0E3FBE25295763340BBECD79DEEAB51C0B284B5317676334053EC681CEAAB51C0B77D8FFAEB753340B1A371A8DFAB51C02E3D9AEAC9743340B1A371A8DFAB51C02E3D9AEAC9743340A2D45E44DBAB51C0022CF2EB877433402635B401D8AB51C0C879FF1F27743340205ED72FD8AB51C02C802903077433409D6516A1D8AB51C05B96AFCBF07333402A70B20DDCAB51C02B6A300DC3733340CB9E0436E7AB51C051FA42C87973334086E28E37F9AB51C0213D450E11733340AAEFFCA204AC51C0CF85915ED472334036785F950BAC51C0E335AFEAAC723340C3B645990DAC51C03352EFA99C723340408A3A730FAC51C0853E58C686723340B6BA9C1210AC51C072FDBB3E73723340C83F33880FAC51C07A6D3656627233403B014D840DAC51C017D68D77477233400725CCB4FDAB51C0D93D7958A8713340029CDEC5FBAB51C0A911FA997A713340FCDEA63FFBAB51C0BF4692205C7133407429AE2AFBAB51C02F698CD651713340FCAA5CA8FCAB51C0C07B478D097133408A1D8D43FDAB51C01F2DCE18E670334023BF7E880DAC51C0EB1A2D077A7033405AD6FD6321AC51C003D0285DFA6F3340A8E15B5837AC51C02F6F0ED76A6F3340E8A221E351AC51C0889FFF1EBC6E33408A39083A5AAC51C04FC939B1876E3340732B84D558AC51C057EE0566856E3340C2DF2F664BAC51C0B459F5B9DA6E33407ADFF8DA33AC51C005F9D9C8756F33404AED45B41DAC51C0B68311FB0470334097361C9606AC51C0ABEAE5779A70334079E6E5B0FBAB51C04303B16CE67033406FA0C03BF9AB51C0E8BCC62E51713340745DF8C1F9AB51C040DAFF006B713340FDF84B8BFAAB51C0CB9C2E8B89713340F6ED2422FCAB51C0689604A8A9713340240D6E6B0BAC51C0ECDADE6E49723340A0E062450DAC51C09E4319AA62723340287CB60E0EAC51C04E27D9EA72723340A69D9ACB0DAC51C0F7E5CC76857233409566F3380CAC51C0ECA529029C7233401893FE5E0AAC51C078B306EFAB723340D769A4A5F2AB51C0EF5696E82C733340
#6	0102000020E6100000070000004E0AF31E67AE51C0FE4465C39A7A334030D461855BAE51C037A79201A07A334011363CBD52AE51C0D9E90775917A334000E2AE5E45AE51C0581CCEFC6A7A3340EF74E789E7AD51C033E202D028793340F110C64FE3AD51C02026E1421E7933406D567DAEB6AD51C0BB09BE69FA783340
\.


--
-- Data for Name: rta_loc; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.rta_loc (id, lat, lon, ruta_rta, geom) FROM stdin;
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
1242	19.449965	-70.686644	,corta	0101000020E6100000A8FFACF9F1AB51C03A58FFE730733340
1243	19.451958	-70.68533	,corta	0101000020E6100000EE3D5C72DCAB51C0632AFD84B3733340
1244	19.45252	-70.685099	,corta	0101000020E61000006DE179A9D8AB51C032C9C859D8733340
1245	19.452874	-70.684981	,corta	0101000020E610000068588CBAD6AB51C06E35EB8CEF733340
1246	19.45336	-70.684938	,corta	0101000020E610000069723106D6AB51C08750A5660F743340
1247	19.453997	-70.68496	,corta	0101000020E61000005DC47762D6AB51C06C7BBB2539743340
1248	19.455115	-70.685115	,corta	0101000020E6100000F0BF95ECD8AB51C046EBA86A82743340
1249	19.456253	-70.685378	,corta	0101000020E610000077D9AF3BDDAB51C0AFB321FFCC743340
1250	19.456668	-70.685389	,corta	0101000020E61000007102D369DDAB51C03621AD31E8743340
1251	19.462068	-70.686126	,corta	0101000020E6100000DCBB067DE9AB51C03387A4164A763340
1252	19.462402	-70.686148	,corta	0101000020E6100000D00D4DD9E9AB51C0E19A3BFA5F763340
1253	19.463262	-70.686341	,corta	0101000020E6100000DA39CD02EDAB51C0C47AA35698763340
1254	19.468674	-70.687103	,corta	0101000020E6100000F17EDC7EF9AB51C04A7CEE04FB773340
1255	19.469139	-70.68721	,corta	0101000020E6100000FCDEA63FFBAB51C03447567E19783340
326	19.491123	-70.717943	mariela	0101000020E6100000195932C7F2AD51C03C50A73CBA7D3340
327	19.490537	-70.718259	mariela	0101000020E6100000B1C398F4F7AD51C05B7A34D5937D3340
328	19.489566	-70.71871	mariela	0101000020E6100000B9533A58FFAD51C0C93B8732547D3340
329	19.489419	-70.718742	mariela	0101000020E6100000BF1072DEFFAD51C0810A47904A7D3340
330	19.487032	-70.718629	mariela	0101000020E6100000433D7D04FEAD51C0001B1021AE7C3340
331	19.486542	-70.718645	mariela	0101000020E6100000C51B9947FEAD51C064213A048E7C3340
332	19.486177	-70.718672	mariela	0101000020E61000004223D8B8FEAD51C041118B18767C3340
333	19.484999	-70.718833	mariela	0101000020E61000004792205C01AE51C0BB97FBE4287C3340
1256	19.469554	-70.687478	,corta	0101000020E61000000CAEB9A3FFAB51C0BBB4E1B034783340
1302	19.429634	-70.694194	,#4	0101000020E610000015C5ABAC6DAC51C0EA3F6B7EFC6D3340
1303	19.427883	-70.695229	,#4	0101000020E6100000C58EC6A17EAC51C0AE8383BD896D3340
1352	19.455941	-70.707685	,#5	0101000020E6100000AAB706B64AAD51C0D1E7A38CB8743340
1353	19.455779	-70.706698	,#5	0101000020E610000083893F8A3AAD51C01E34BBEEAD743340
1354	19.455476	-70.705904	,#5	0101000020E61000006687F8872DAD51C0E71C3C139A743340
1355	19.455193	-70.704939	,#5	0101000020E610000032AB77B81DAD51C03E5E488787743340
1356	19.453695	-70.699662	,#5	0101000020E610000041102043C7AC51C0D55B035B25743340
1357	19.45238	-70.692862	,#5	0101000020E610000009A9DBD957AC51C04F5DF92CCF733340
1144	19.451937	-70.685339	#2	0101000020E610000018EB1B98DCAB51C034DAAA24B2733340
1145	19.452347	-70.685162	#2	0101000020E6100000919DB7B1D9AB51C097715303CD733340
1146	19.452349	-70.685161	#2	0101000020E6100000A8DF85ADD9AB51C0D960E124CD733340
1147	19.452878	-70.68499	#2	0101000020E610000092054CE0D6AB51C0F01307D0EF733340
1148	19.453232	-70.684958	#2	0101000020E61000008C48145AD6AB51C02C80290307743340
1149	19.453434	-70.684937	#2	0101000020E610000081B4FF01D6AB51C0FCE4284014743340
1150	19.453894	-70.684947	#2	0101000020E6100000921FF12BD6AB51C0C2D9AD6532743340
1151	19.455184	-70.685134	#2	0101000020E61000002CD8463CD9AB51C097A949F086743340
1152	19.4561	-70.685349	#2	0101000020E61000002A560DC2DCAB51C0A2B437F8C2743340
1153	19.456302	-70.685376	#2	0101000020E6100000A75D4C33DDAB51C072193735D0743340
1154	19.456641	-70.685381	#2	0101000020E610000030134548DDAB51C04303B16CE6743340
1155	19.458907	-70.685703	#2	0101000020E610000039F1D58EE2AB51C0CDE7DCED7A753340
1156	19.462316	-70.686153	#2	0101000020E610000059C345EEE9AB51C0E46A64575A763340
1157	19.463201	-70.68633	#2	0101000020E6100000E010AAD4ECAB51C079793A5794763340
1158	19.464683	-70.686534	#2	0101000020E6100000E4654D2CF0AB51C0D6011077F5763340
1159	19.466524	-70.686786	#2	0101000020E61000007156444DF4AB51C092CCEA1D6E773340
1160	19.468739	-70.687103	#2	0101000020E6100000F17EDC7EF9AB51C0185C7347FF773340
1161	19.469149	-70.687221	#2	0101000020E6100000F607CA6DFBAB51C07CF31B261A783340
1162	19.469407	-70.687366	#2	0101000020E61000007898F6CDFDAB51C07383A10E2B783340
1163	19.469665	-70.687623	#2	0101000020E61000008E3EE60302AC51C06A1327F73B783340
1164	19.469857	-70.688025	#2	0101000020E61000002575029A08AC51C0F3CB608C48783340
1165	19.471248	-70.69149	#2	0101000020E6100000B5E0455F41AC51C02F4E7CB5A3783340
1166	19.471375	-70.691684	#2	0101000020E6100000A7CAF78C44AC51C0E9263108AC783340
1167	19.47146	-70.691792	#2	0101000020E61000009BE8F35146AC51C0465F419AB1783340
1168	19.471577	-70.691915	#2	0101000020E61000002827DA5548AC51C0B98B3045B9783340
1169	19.474935	-70.694929	#2	0101000020E6100000B0027CB779AC51C0CBB9145795793340
1170	19.475259	-70.695304	#2	0101000020E6100000CA3159DC7FAC51C03221E692AA793340
1171	19.477863	-70.699809	#2	0101000020E6100000931CB0ABC9AC51C0ECF7C43A557A3340
1172	19.480974	-70.70521	#2	0101000020E6100000FAB31F2922AD51C0EF39B01C217B3340
1173	19.482683	-70.708192	#2	0101000020E61000007CD2890453AD51C0CE55F31C917B3340
1174	19.483034	-70.708796	#2	0101000020E610000048E2E5E95CAD51C027DBC01DA87B3340
1175	19.483034	-70.708796	#2	0101000020E610000048E2E5E95CAD51C027DBC01DA87B3340
1176	19.483351	-70.709259	#2	0101000020E61000003259DC7F64AD51C0287D21E4BC7B3340
1177	19.484519	-70.71061	#2	0101000020E61000007A8D5DA27AAD51C0664AEB6F097C3340
1178	19.48551	-70.711774	#2	0101000020E6100000290989B48DAD51C087E123624A7C3340
1179	19.485436	-70.711843	#2	0101000020E6100000BD38F1D58EAD51C0124DA088457C3340
1180	19.48333	-70.709414	#2	0101000020E6100000C554FA0967AD51C0F92CCF83BB7B3340
1181	19.483138	-70.709145	#2	0101000020E6100000CEC7B5A162AD51C0717495EEAE7B3340
1182	19.482905	-70.708802	#2	0101000020E6100000B95510035DAD51C02B137EA99F7B3340
1183	19.480938	-70.70537	#2	0101000020E6100000176536C824AD51C05567B5C01E7B3340
1184	19.47713	-70.698741	#2	0101000020E6100000F5D72B2CB8AC51C0C3F01131257A3340
1185	19.475178	-70.695384	#2	0101000020E6100000598AE42B81AC51C058C7F143A5793340
1186	19.47496	-70.695116	#2	0101000020E610000049BBD1C77CAC51C07DE882FA96793340
1187	19.47142	-70.691914	#2	0101000020E61000004069A85148AC51C029AE2AFBAE783340
1188	19.471182	-70.691619	#2	0101000020E6100000B492567C43AC51C0C07630629F783340
1189	19.469675	-70.687898	#2	0101000020E6100000F73E558506AC51C0B1BFEC9E3C783340
1190	19.469564	-70.687683	#2	0101000020E6100000F9C08EFF02AC51C00261A75835783340
1191	19.469362	-70.68749	#2	0101000020E6100000EE940ED6FFAB51C033FCA71B28783340
1192	19.469164	-70.687345	#2	0101000020E61000006C04E275FDAB51C0E675C4211B783340
1193	19.468886	-70.687243	#2	0101000020E6100000EA5910CAFBAB51C0618DB3E908783340
1194	19.468431	-70.687174	#2	0101000020E6100000562AA8A8FAAB51C0BD6E1118EB773340
1195	19.463216	-70.686476	#2	0101000020E61000004A5F0839EFAB51C0E3FBE25295763340
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
1196	19.462741	-70.686439	#2	0101000020E6100000BBECD79DEEAB51C0B284B53176763340
1197	19.460632	-70.686164	#2	0101000020E610000053EC681CEAAB51C0B77D8FFAEB753340
1198	19.456206	-70.685526	#2	0101000020E6100000B1A371A8DFAB51C02E3D9AEAC9743340
1199	19.456206	-70.685526	#2	0101000020E6100000B1A371A8DFAB51C02E3D9AEAC9743340
1200	19.455199	-70.685258	#2	0101000020E6100000A2D45E44DBAB51C0022CF2EB87743340
1201	19.453722	-70.685059	#2	0101000020E61000002635B401D8AB51C0C879FF1F27743340
1202	19.453232	-70.68507	#2	0101000020E6100000205ED72FD8AB51C02C80290307743340
1203	19.452893	-70.685097	#2	0101000020E61000009D6516A1D8AB51C05B96AFCBF0733340
1204	19.452195	-70.685306	#2	0101000020E61000002A70B20DDCAB51C02B6A300DC3733340
1205	19.451077	-70.685987	#2	0101000020E6100000CB9E0436E7AB51C051FA42C879733340
1206	19.449479	-70.687086	#2	0101000020E610000086E28E37F9AB51C0213D450E11733340
1207	19.448553	-70.687783	#2	0101000020E6100000AAEFFCA204AC51C0CF85915ED4723340
1208	19.447951	-70.688207	#2	0101000020E610000036785F950BAC51C0E335AFEAAC723340
1209	19.447703	-70.68833	#2	0101000020E6100000C3B645990DAC51C03352EFA99C723340
1210	19.447369	-70.688443	#2	0101000020E6100000408A3A730FAC51C0853E58C686723340
1211	19.447071	-70.688481	#2	0101000020E6100000B6BA9C1210AC51C072FDBB3E73723340
1212	19.446813	-70.688448	#2	0101000020E6100000C83F33880FAC51C07A6D365662723340
1213	19.446403	-70.688325	#2	0101000020E61000003B014D840DAC51C017D68D7747723340
1214	19.443975	-70.68736	#2	0101000020E61000000725CCB4FDAB51C0D93D7958A8713340
1215	19.443277	-70.687242	#2	0101000020E6100000029CDEC5FBAB51C0A911FA997A713340
1216	19.442812	-70.68721	#2	0101000020E6100000FCDEA63FFBAB51C0BF4692205C713340
1217	19.442655	-70.687205	#2	0101000020E61000007429AE2AFBAB51C02F698CD651713340
1218	19.441552	-70.687296	#2	0101000020E6100000FCAA5CA8FCAB51C0C07B478D09713340
1219	19.441011	-70.687333	#2	0101000020E61000008A1D8D43FDAB51C01F2DCE18E6703340
1220	19.439362	-70.688326	#2	0101000020E610000023BF7E880DAC51C0EB1A2D077A703340
1221	19.437414	-70.689538	#2	0101000020E61000005AD6FD6321AC51C003D0285DFA6F3340
1222	19.435224	-70.690878	#2	0101000020E6100000A8E15B5837AC51C02F6F0ED76A6F3340
1223	19.432558	-70.692498	#2	0101000020E6100000E8A221E351AC51C0889FFF1EBC6E3340
1224	19.431758	-70.693007	#2	0101000020E61000008A39083A5AAC51C04FC939B1876E3340
1225	19.431723	-70.692922	#2	0101000020E6100000732B84D558AC51C057EE0566856E3340
1226	19.433025	-70.692102	#2	0101000020E6100000C2DF2F664BAC51C0B459F5B9DA6E3340
1227	19.435391	-70.690665	#2	0101000020E61000007ADFF8DA33AC51C005F9D9C8756F3340
1228	19.437576	-70.689313	#2	0101000020E61000004AED45B41DAC51C0B68311FB04703340
1229	19.439857	-70.687902	#2	0101000020E610000097361C9606AC51C0ABEAE5779A703340
1230	19.441016	-70.687237	#2	0101000020E610000079E6E5B0FBAB51C04303B16CE6703340
1231	19.442645	-70.687087	#2	0101000020E61000006FA0C03BF9AB51C0E8BCC62E51713340
1232	19.443039	-70.687119	#2	0101000020E6100000745DF8C1F9AB51C040DAFF006B713340
1233	19.443505	-70.687167	#2	0101000020E6100000FDF84B8BFAAB51C0CB9C2E8B89713340
1234	19.443995	-70.687264	#2	0101000020E6100000F6ED2422FCAB51C0689604A8A9713340
1235	19.446433	-70.688197	#2	0101000020E6100000240D6E6B0BAC51C0ECDADE6E49723340
1236	19.446818	-70.68831	#2	0101000020E6100000A0E062450DAC51C09E4319AA62723340
1237	19.447066	-70.688358	#2	0101000020E6100000287CB60E0EAC51C04E27D9EA72723340
1238	19.447349	-70.688342	#2	0101000020E6100000A69D9ACB0DAC51C0F7E5CC7685723340
1239	19.447693	-70.688246	#2	0101000020E61000009566F3380CAC51C0ECA529029C723340
1240	19.447936	-70.688133	#2	0101000020E61000001893FE5E0AAC51C078B306EFAB723340
1241	19.449904	-70.686685	#2	0101000020E6100000D769A4A5F2AB51C0EF5696E82C733340
1402	19.478924	-70.725044	#6	0101000020E61000004E0AF31E67AE51C0FE4465C39A7A3340
1403	19.479004	-70.724336	#6	0101000020E610000030D461855BAE51C037A79201A07A3340
1404	19.478782	-70.7238	#6	0101000020E610000011363CBD52AE51C0D9E90775917A3340
1405	19.478195	-70.722984	#6	0101000020E610000000E2AE5E45AE51C0581CCEFC6A7A3340
1406	19.473279	-70.717257	#6	0101000020E6100000EF74E789E7AD51C033E202D028793340
1407	19.473118	-70.716999	#6	0101000020E6100000F110C64FE3AD51C02026E1421E793340
1408	19.472571	-70.714275	#6	0101000020E61000006D567DAEB6AD51C0BB09BE69FA783340
\.


--
-- Data for Name: trp_loc; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.trp_loc (id, reg_dt, lat, lon, placa_pl, ruta_rta, prev_reg_dt, orientation, geom, prev_geom) FROM stdin;
361	2024-04-19 19:52:21.579	19.481797930117573	-70.7184201383622	B9288949	mariela	\N	\N	0101000020E61000006CC37598FAAD51C0AE22F11B577B3340	\N
362	2024-04-19 19:52:24.575	19.486986628206214	-70.7186291835761	B9288949	mariela	2024-04-19 19:52:21.579	177.824840324155	0101000020E6100000465A4205FEAD51C029BCD927AB7C3340	0101000020E61000006CC37598FAAD51C0AE22F11B577B3340
363	2024-04-19 19:52:21.579	19.481797930117573	-70.7184201383622	B9288949	mariela4	\N	\N	0101000020E61000006CC37598FAAD51C0AE22F11B577B3340	\N
452	2024-07-02 19:46:26.949	19.4767566	-70.7099605	A1547858	#2	\N	\N	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340	\N
453	2024-07-02 19:46:31.836	19.4767566	-70.7099605	A1547858	#2	2024-07-02 19:46:26.949	0	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340
454	2024-07-02 19:46:36.818	19.4767566	-70.7099605	A1547858	#2	2024-07-02 19:46:31.836	0	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340
455	2024-07-02 19:46:41.884	19.4767566	-70.7099605	A1547858	#2	2024-07-02 19:46:36.818	0	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340
503	2024-07-02 19:59:46.17	19.4766293	-70.7098892	A1547858	#2	2024-07-02 19:46:41.884	332.163981923069	0101000020E610000027721CD36EAD51C0BCD0B760047A3340	0101000020E6100000EA3C2AFE6FAD51C0F42675B80C7A3340
504	2024-07-02 19:59:51.073	19.4766293	-70.7098892	A1547858	#2	2024-07-02 19:59:46.17	0	0101000020E610000027721CD36EAD51C0BCD0B760047A3340	0101000020E610000027721CD36EAD51C0BCD0B760047A3340
505	2024-07-02 19:59:56.05	19.4766293	-70.7098892	A1547858	#2	2024-07-02 19:59:51.073	0	0101000020E610000027721CD36EAD51C0BCD0B760047A3340	0101000020E610000027721CD36EAD51C0BCD0B760047A3340
506	2024-07-02 20:00:01.088	19.4766293	-70.7098892	A1547858	#2	2024-07-02 19:59:56.05	0	0101000020E610000027721CD36EAD51C0BCD0B760047A3340	0101000020E610000027721CD36EAD51C0BCD0B760047A3340
507	2024-07-02 20:00:06.123	19.4766293	-70.7098892	A1547858	#2	2024-07-02 20:00:01.088	0	0101000020E610000027721CD36EAD51C0BCD0B760047A3340	0101000020E610000027721CD36EAD51C0BCD0B760047A3340
508	2024-07-02 20:11:13.115	19.4834073	-70.7093871	A1547858	#2	2024-07-02 20:00:06.123	183.994992573704	0101000020E610000013AD269966AD51C0CD25B094C07B3340	0101000020E610000027721CD36EAD51C0BCD0B760047A3340
509	2024-07-02 20:11:18.235	19.4832557	-70.7093924	A1547858	#2	2024-07-02 20:11:13.115	1.887700932105	0101000020E6100000FB8161AF66AD51C0081B43A5B67B3340	0101000020E610000013AD269966AD51C0CD25B094C07B3340
510	2024-07-02 20:11:22.866	19.4832723	-70.7094038	A1547858	#2	2024-07-02 20:11:18.235	147.080044204328	0101000020E61000001F2A32DF66AD51C00D90C3BBB77B3340	0101000020E6100000FB8161AF66AD51C0081B43A5B67B3340
511	2024-07-02 20:11:28.163	19.4833427	-70.7093448	A1547858	#2	2024-07-02 20:11:22.866	218.311543235665	0101000020E61000009C65BBE765AD51C0A642E158BC7B3340	0101000020E61000001F2A32DF66AD51C00D90C3BBB77B3340
512	2024-07-02 20:11:33.256	19.4833916	-70.7094127	A1547858	#2	2024-07-02 20:11:28.163	127.376923609505	0101000020E61000007E77860467AD51C03F29498DBF7B3340	0101000020E61000009C65BBE765AD51C0A642E158BC7B3340
513	2024-07-02 20:11:42.73	19.4833331	-70.7094414	A1547858	#2	2024-07-02 20:11:33.256	24.820812148561	0101000020E61000006CDBE67C67AD51C00593D1B7BB7B3340	0101000020E61000007E77860467AD51C03F29498DBF7B3340
514	2024-07-02 20:11:49.176	19.4833636	-70.7093442	A1547858	#2	2024-07-02 20:11:42.73	251.590240310867	0101000020E6100000DE2637E565AD51C0AB1386B7BD7B3340	0101000020E61000006CDBE67C67AD51C00593D1B7BB7B3340
515	2024-07-02 20:11:53.95	19.4833632	-70.7093108	A1547858	#2	2024-07-02 20:11:49.176	270.727821582482	0101000020E6100000C62C205965AD51C00417D0B0BD7B3340	0101000020E6100000DE2637E565AD51C0AB1386B7BD7B3340
516	2024-07-02 20:11:58.951	19.4834678	-70.7091706	A1547858	#2	2024-07-02 20:11:53.95	231.642151246908	0101000020E61000003892150D63AD51C048ABB58BC47B3340	0101000020E6100000C62C205965AD51C00417D0B0BD7B3340
517	2024-07-02 20:12:04.285	19.4834547	-70.7092394	A1547858	#2	2024-07-02 20:11:58.951	78.581436113829	0101000020E61000003802A72D64AD51C0F598EDAFC37B3340	0101000020E61000003892150D63AD51C048ABB58BC47B3340
518	2024-07-02 20:12:09.262	19.4834593	-70.7092704	A1547858	#2	2024-07-02 20:12:04.285	98.944972124291	0101000020E61000005501ADAF64AD51C072721AFDC37B3340	0101000020E61000003802A72D64AD51C0F598EDAFC37B3340
519	2024-07-02 20:12:14.037	19.4834087	-70.7093597	A1547858	#2	2024-07-02 20:12:09.262	58.992161353346	0101000020E61000006C263A2666AD51C0141A2DACC07B3340	0101000020E61000005501ADAF64AD51C072721AFDC37B3340
520	2024-07-02 20:12:19.303	19.4833453	-70.7094292	A1547858	#2	2024-07-02 20:12:14.037	45.942229067051	0101000020E6100000F534BB4967AD51C0E12C8084BC7B3340	0101000020E61000006C263A2666AD51C0141A2DACC07B3340
521	2024-07-02 20:12:24.315	19.4833325	-70.7094528	A1547858	#2	2024-07-02 20:12:19.303	60.087474386806	0101000020E61000008F83B7AC67AD51C00B98C0ADBB7B3340	0101000020E6100000F534BB4967AD51C0E12C8084BC7B3340
522	2024-07-02 20:12:29.258	19.4833427	-70.7094658	A1547858	#2	2024-07-02 20:12:24.315	129.769680506521	0101000020E61000005A283EE367AD51C0A642E158BC7B3340	0101000020E61000008F83B7AC67AD51C00B98C0ADBB7B3340
523	2024-07-02 20:12:34.068	19.4831133	-70.7094393	A1547858	#2	2024-07-02 20:12:29.258	353.784756186229	0101000020E6100000D1FF177467AD51C03CC32F50AD7B3340	0101000020E61000005A283EE367AD51C0A642E158BC7B3340
524	2024-07-02 20:12:39.496	19.4831475	-70.7094529	A1547858	#2	2024-07-02 20:12:34.068	159.449445363138	0101000020E61000005AE322AD67AD51C0E8A4F78DAF7B3340	0101000020E6100000D1FF177467AD51C03CC32F50AD7B3340
525	2024-07-02 20:12:44.637	19.4832562	-70.7094332	A1547858	#2	2024-07-02 20:12:39.496	189.695667110562	0101000020E6100000962C825A67AD51C0D896A6ADB67B3340	0101000020E61000005AE322AD67AD51C0E8A4F78DAF7B3340
526	2024-07-02 20:12:49.407	19.4833023	-70.709446	A1547858	#2	2024-07-02 20:12:44.637	165.331458159014	0101000020E6100000CB11329067AD51C0E29414B3B97B3340	0101000020E6100000962C825A67AD51C0D896A6ADB67B3340
527	2024-07-02 20:12:58.58	19.4834756	-70.7092755	A1547858	#2	2024-07-02 20:12:49.407	222.846148396958	0101000020E6100000A91611C564AD51C0FA69920EC57B3340	0101000020E6100000CB11329067AD51C0E29414B3B97B3340
528	2024-07-02 20:13:05.133	19.4834809	-70.7092032	A1547858	#2	2024-07-02 20:12:58.58	265.55373714266	0101000020E6100000FD8DD19563AD51C09BBD7D67C57B3340	0101000020E6100000A91611C564AD51C0FA69920EC57B3340
529	2024-07-02 20:13:10.025	19.4834897	-70.7092471	A1547858	#2	2024-07-02 20:13:05.133	102.00409232848	0101000020E61000001AD2F24D64AD51C0EE7321FBC57B3340	0101000020E6100000FD8DD19563AD51C09BBD7D67C57B3340
530	2024-07-02 20:13:15.067	19.4834278	-70.7093333	A1547858	#2	2024-07-02 20:13:10.025	52.702920704295	0101000020E6100000AE5D7FB765AD51C02CFA9EECC17B3340	0101000020E61000001AD2F24D64AD51C0EE7321FBC57B3340
531	2024-07-02 20:13:19.849	19.4834222	-70.7093164	A1547858	#2	2024-07-02 20:13:15.067	289.365957460107	0101000020E61000000E219D7065AD51C00E29AB8EC17B3340	0101000020E6100000AE5D7FB765AD51C02CFA9EECC17B3340
532	2024-07-02 20:13:26.484	19.4833663	-70.7092674	A1547858	#2	2024-07-02 20:13:19.849	320.430648331002	0101000020E61000009DC717A364AD51C0107DD2E4BD7B3340	0101000020E61000000E219D7065AD51C00E29AB8EC17B3340
533	2024-07-02 20:13:30.065	19.4833505	-70.7092907	A1547858	#2	2024-07-02 20:13:26.484	54.272537535848	0101000020E6100000D8F6D10465AD51C05801BEDBBC7B3340	0101000020E61000009DC717A364AD51C0107DD2E4BD7B3340
534	2024-07-02 20:13:34.872	19.483423	-70.7092146	A1547858	#2	2024-07-02 20:13:30.065	224.699074629527	0101000020E61000002036A2C563AD51C05B22179CC17B3340	0101000020E6100000D8F6D10465AD51C05801BEDBBC7B3340
535	2024-07-02 20:13:39.961	19.4834146	-70.7091711	A1547858	#2	2024-07-02 20:13:34.872	281.575926438761	0101000020E61000002D712E0F63AD51C0AF68290FC17B3340	0101000020E61000002036A2C563AD51C05B22179CC17B3340
536	2024-07-02 20:13:45.095	19.483416	-70.7092601	A1547858	#2	2024-07-02 20:13:39.961	90.955922252153	0101000020E6100000E476798464AD51C0F65CA626C17B3340	0101000020E61000002D712E0F63AD51C0AF68290FC17B3340
537	2024-07-02 20:13:49.811	19.4833963	-70.709226	A1547858	#2	2024-07-02 20:13:45.095	301.500091501851	0101000020E610000044DE72F563AD51C0E58123DCBF7B3340	0101000020E6100000E476798464AD51C0F65CA626C17B3340
538	2024-07-02 20:13:55.248	19.4833698	-70.7093889	A1547858	#2	2024-07-02 20:13:49.811	80.209573966465	0101000020E61000004E69B3A066AD51C0C2DF8A1FBE7B3340	0101000020E610000044DE72F563AD51C0E58123DCBF7B3340
539	2024-07-02 20:14:00.297	19.4833349	-70.7094538	A1547858	#2	2024-07-02 20:13:55.248	60.298986206515	0101000020E61000007841E9B067AD51C0F38304D6BB7B3340	0101000020E61000004E69B3A066AD51C0C2DF8A1FBE7B3340
540	2024-07-02 20:14:05.295	19.4831688	-70.7093941	A1547858	#2	2024-07-02 20:14:00.297	341.281513283624	0101000020E61000006CDE82B666AD51C0947252F3B07B3340	0101000020E61000007841E9B067AD51C0F38304D6BB7B3340
541	2024-07-02 20:14:10.605	19.483057	-70.7092755	A1547858	#2	2024-07-02 20:14:05.295	314.997727934995	0101000020E6100000A91611C564AD51C0971AA19FA97B3340	0101000020E61000006CDE82B666AD51C0947252F3B07B3340
542	2024-07-02 20:14:15.323	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:10.605	294.03117745345	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000A91611C564AD51C0971AA19FA97B3340
543	2024-07-02 20:14:22.849	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:15.323	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
544	2024-07-02 20:14:27.677	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:22.849	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
545	2024-07-02 20:14:32.796	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:27.677	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
546	2024-07-02 20:14:37.926	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:32.796	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
547	2024-07-02 20:14:42.674	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:37.926	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
548	2024-07-02 20:14:47.8	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:42.674	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
549	2024-07-02 20:14:52.626	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:47.8	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
550	2024-07-02 20:14:57.757	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:52.626	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
551	2024-07-02 20:15:02.879	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:14:57.757	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
552	2024-07-02 20:15:07.603	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:02.879	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
553	2024-07-02 20:15:12.796	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:07.603	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
554	2024-07-02 20:15:18.164	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:12.796	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
555	2024-07-02 20:15:22.696	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:18.164	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
556	2024-07-02 20:15:27.842	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:22.696	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
557	2024-07-02 20:15:32.598	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:27.842	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
558	2024-07-02 20:15:37.723	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:32.598	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
559	2024-07-02 20:15:42.593	19.4829438	-70.7090062	A1547858	#2	2024-07-02 20:15:37.723	0	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
560	2024-07-02 20:15:51.011	19.4821069	-70.7072983	A1547858	#2	2024-07-02 20:15:42.593	297.464692489842	0101000020E61000000DC1166044AD51C00BAD985B6B7B3340	0101000020E6100000526A8A5B60AD51C054CE7234A27B3340
561	2024-07-02 20:15:56.46	19.4816355	-70.7065387	A1547858	#2	2024-07-02 20:15:51.011	303.356162873193	0101000020E6100000EF7618EE37AD51C0B517D1764C7B3340	0101000020E61000000DC1166044AD51C00BAD985B6B7B3340
562	2024-07-02 20:16:00.801	19.4813597	-70.7060552	A1547858	#2	2024-07-02 20:15:56.46	301.176719459392	0101000020E6100000ED4A260230AD51C0C41CA9633A7B3340	0101000020E6100000EF7618EE37AD51C0B517D1764C7B3340
563	2024-07-02 20:16:08.553	19.4810118	-70.7055669	A1547858	#2	2024-07-02 20:16:00.801	307.079771743848	0101000020E6100000F728120228AD51C077FDDD96237B3340	0101000020E6100000ED4A260230AD51C0C41CA9633A7B3340
564	2024-07-02 20:16:14.41	19.4809726	-70.7054863	A1547858	#2	2024-07-02 20:16:08.553	297.288574870899	0101000020E6100000AB9102B026AD51C0A8453305217B3340	0101000020E6100000F728120228AD51C077FDDD96237B3340
565	2024-07-02 20:16:19.375	19.4809845	-70.7055183	A1547858	#2	2024-07-02 20:16:14.41	111.527085054453	0101000020E6100000B04E3A3627AD51C00762D9CC217B3340	0101000020E6100000AB9102B026AD51C0A8453305217B3340
566	2024-07-02 20:16:24.024	19.4810058	-70.7055147	A1547858	#2	2024-07-02 20:16:19.375	189.053312656772	0101000020E610000039D6202727AD51C0B22F3432237B3340	0101000020E6100000B04E3A3627AD51C00762D9CC217B3340
567	2024-07-02 20:16:29.071	19.4809774	-70.7055365	A1547858	#2	2024-07-02 20:16:24.024	35.891822605974	0101000020E61000009868908227AD51C0781DBB55217B3340	0101000020E610000039D6202727AD51C0B22F3432237B3340
568	2024-07-02 20:16:34.375	19.480988	-70.7055135	A1547858	#2	2024-07-02 20:16:29.071	243.948054985596	0101000020E6100000BC58182227AD51C0B9C49107227B3340	0101000020E61000009868908227AD51C0781DBB55217B3340
569	2024-07-02 20:16:39.281	19.4810087	-70.7055412	A1547858	#2	2024-07-02 20:16:34.375	128.402842429567	0101000020E6100000C2FE469627AD51C06B97DB62237B3340	0101000020E6100000BC58182227AD51C0B9C49107227B3340
570	2024-07-02 20:16:44.366	19.4809907	-70.7055211	A1547858	#2	2024-07-02 20:16:39.281	313.528259073736	0101000020E6100000D4C8F84127AD51C01E2EDE34227B3340	0101000020E6100000C2FE469627AD51C06B97DB62237B3340
571	2024-07-02 20:16:49.091	19.4809914	-70.7055213	A1547858	#2	2024-07-02 20:16:44.366	164.924727204408	0101000020E61000006988CF4227AD51C042A89C40227B3340	0101000020E6100000D4C8F84127AD51C01E2EDE34227B3340
572	2024-07-02 20:16:54.505	19.4809945	-70.7055477	A1547858	#2	2024-07-02 20:16:49.091	97.099890966612	0101000020E610000027518AB127AD51C04E0E9F74227B3340	0101000020E61000006988CF4227AD51C042A89C40227B3340
573	2024-07-02 20:16:59.33	19.4809902	-70.7054772	A1547858	#2	2024-07-02 20:16:54.505	273.701697848445	0101000020E6100000B784D78926AD51C04EB27A2C227B3340	0101000020E610000027518AB127AD51C04E0E9F74227B3340
574	2024-07-02 20:17:04.176	19.4809961	-70.7055378	A1547858	#2	2024-07-02 20:16:59.33	95.896127890128	0101000020E6100000E045048827AD51C0E800778F227B3340	0101000020E6100000B784D78926AD51C04EB27A2C227B3340
575	2024-07-02 20:17:09.249	19.4808189	-70.7053368	A1547858	#2	2024-07-02 20:17:04.176	313.079928090406	0101000020E6100000942AF63C24AD51C077CC8AF2167B3340	0101000020E6100000E045048827AD51C0E800778F227B3340
576	2024-07-02 20:17:14.494	19.4809052	-70.7047535	A1547858	#2	2024-07-02 20:17:09.249	261.081107366826	0101000020E6100000758F6CAE1AAD51C0F1796A9A1C7B3340	0101000020E6100000942AF63C24AD51C077CC8AF2167B3340
577	2024-07-02 20:17:19.587	19.480634	-70.7041465	A1547858	#2	2024-07-02 20:17:14.494	295.357198589358	0101000020E6100000F1457BBC10AD51C07D586FD40A7B3340	0101000020E6100000758F6CAE1AAD51C0F1796A9A1C7B3340
578	2024-07-02 20:17:24.307	19.4799066	-70.7035862	A1547858	#2	2024-07-02 20:17:19.587	324.013532540419	0101000020E6100000AFBA698E07AD51C07222B028DB7A3340	0101000020E6100000F1457BBC10AD51C07D586FD40A7B3340
579	2024-07-02 20:17:31.302	19.479471	-70.7026612	A1547858	#2	2024-07-02 20:17:24.307	296.542789137185	0101000020E6100000C28AAE66F8AC51C06361889CBE7A3340	0101000020E6100000AFBA698E07AD51C07222B028DB7A3340
580	2024-07-02 20:17:38.101	19.4787199	-70.7018093	A1547858	#2	2024-07-02 20:17:31.302	313.082447143551	0101000020E6100000D5E18D71EAAC51C0C4712A638D7A3340	0101000020E6100000C28AAE66F8AC51C06361889CBE7A3340
581	2024-07-02 20:17:42.521	19.4785484	-70.7010316	A1547858	#2	2024-07-02 20:17:38.101	283.165464085248	0101000020E61000009ADDA4B3DDAC51C09A8DDF25827A3340	0101000020E6100000D5E18D71EAAC51C0C4712A638D7A3340
582	2024-07-02 20:17:47.64	19.4783635	-70.6997149	A1547858	#2	2024-07-02 20:17:42.521	278.472239761349	0101000020E610000088010121C8AC51C0A019C407767A3340	0101000020E61000009ADDA4B3DDAC51C09A8DDF25827A3340
583	2024-07-02 20:17:53.013	19.4783322	-70.6993742	A1547858	#2	2024-07-02 20:17:47.64	275.565774268235	0101000020E6100000A22A018CC2AC51C0AD9FA3FA737A3340	0101000020E610000088010121C8AC51C0A019C407767A3340
584	2024-07-02 20:17:57.698	19.477996	-70.6995127	A1547858	#2	2024-07-02 20:17:53.013	21.22516606595	0101000020E6100000BF68EAD0C4AC51C06B9E23F25D7A3340	0101000020E6100000A22A018CC2AC51C0AD9FA3FA737A3340
585	2024-07-02 20:18:02.538	19.4776419	-70.6993024	A1547858	#2	2024-07-02 20:17:57.698	330.755068379334	0101000020E6100000EA80DA5EC1AC51C005B353BD467A3340	0101000020E6100000BF68EAD0C4AC51C06B9E23F25D7A3340
586	2024-07-02 20:18:07.894	19.4773738	-70.6988254	A1547858	#2	2024-07-02 20:18:02.538	300.802291342174	0101000020E61000004EA72B8EB9AC51C09DF75A2B357A3340	0101000020E6100000EA80DA5EC1AC51C005B353BD467A3340
587	2024-07-02 20:18:14.429	19.4766235	-70.6980043	A1547858	#2	2024-07-02 20:18:07.894	314.105190620404	0101000020E6100000E93D3A1AACAC51C04B0169FF037A3340	0101000020E61000004EA72B8EB9AC51C09DF75A2B357A3340
588	2024-07-02 20:18:19.996	19.4762526	-70.6973196	A1547858	#2	2024-07-02 20:18:14.429	299.880713850141	0101000020E6100000073763E2A0AC51C08DA2BDB0EB793340	0101000020E6100000E93D3A1AACAC51C04B0169FF037A3340
589	2024-07-02 20:18:23.942	19.476015	-70.6971578	A1547858	#2	2024-07-02 20:18:19.996	327.299106818985	0101000020E6100000AFC9BF3B9EAC51C0CB67791EDC793340	0101000020E6100000073763E2A0AC51C08DA2BDB0EB793340
590	2024-07-02 20:18:29.384	19.475419	-70.6963498	A1547858	#2	2024-07-02 20:18:23.942	308.039400446979	0101000020E6100000DF64C0FE90AC51C0A3E5400FB5793340	0101000020E6100000AFC9BF3B9EAC51C0CB67791EDC793340
591	2024-07-02 20:18:34.546	19.475147	-70.6959717	A1547858	#2	2024-07-02 20:18:29.384	307.34527485406	0101000020E6100000429CE2CC8AAC51C0E2CAD93BA3793340	0101000020E6100000DF64C0FE90AC51C0A3E5400FB5793340
592	2024-07-02 20:18:39.109	19.4758964	-70.6970147	A1547858	#2	2024-07-02 20:18:34.546	127.311182676772	0101000020E610000033558BE39BAC51C0BD48B258D4793340	0101000020E6100000429CE2CC8AAC51C0E2CAD93BA3793340
593	2024-07-02 20:18:44.164	19.4756246	-70.6953569	A1547858	#2	2024-07-02 20:18:39.109	279.865495177321	0101000020E610000011233ABA80AC51C04F2CA688C2793340	0101000020E610000033558BE39BAC51C0BD48B258D4793340
594	2024-07-02 20:18:49.16	19.4755881	-70.695039	A1547858	#2	2024-07-02 20:18:44.164	276.943571569913	0101000020E6100000739CDB847BAC51C0E5DD4724C0793340	0101000020E610000011233ABA80AC51C04F2CA688C2793340
595	2024-07-02 20:18:54.219	19.47545	-70.6950908	A1547858	#2	2024-07-02 20:18:49.16	19.475080346259	0101000020E610000008701F5E7CAC51C019E25817B7793340	0101000020E6100000739CDB847BAC51C0E5DD4724C0793340
596	2024-07-02 20:18:59.152	19.4754949	-70.6948407	A1547858	#2	2024-07-02 20:18:54.219	259.218662844365	0101000020E6100000819B204578AC51C030EAA408BA793340	0101000020E610000008701F5E7CAC51C019E25817B7793340
597	2024-07-02 20:19:09.566	19.4749264	-70.6946122	A1547858	#2	2024-07-02 20:18:59.152	339.246408970366	0101000020E6100000C499BA8674AC51C0CC01CCC694793340	0101000020E6100000819B204578AC51C030EAA408BA793340
598	2024-07-02 20:19:09.602	19.4755073	-70.6947354	A1547858	#2	2024-07-02 20:19:09.566	168.69277749574	0101000020E6100000E797778B76AC51C05F82AED8BA793340	0101000020E6100000C499BA8674AC51C0CC01CCC694793340
599	2024-07-02 20:19:22.121	19.473645	-70.6938531	A1547858	#2	2024-07-02 20:19:09.602	335.931529990431	0101000020E61000009B2ED51668AC51C0F7E978CC40793340	0101000020E6100000E797778B76AC51C05F82AED8BA793340
600	2024-07-02 20:19:24.313	19.4730976	-70.6934072	A1547858	#2	2024-07-02 20:19:22.121	322.476485080615	0101000020E6100000E6B397C860AC51C0EBD09FEC1C793340	0101000020E61000009B2ED51668AC51C0F7E978CC40793340
601	2024-07-02 20:19:29.401	19.4733452	-70.6935876	A1547858	#2	2024-07-02 20:19:24.313	145.514135251733	0101000020E610000050BA3EBD63AC51C0F5B7A9262D793340	0101000020E6100000E6B397C860AC51C0EBD09FEC1C793340
602	2024-07-02 20:19:37.065	19.4720547	-70.6925465	A1547858	#2	2024-07-02 20:19:29.401	322.743688937397	0101000020E6100000651D8EAE52AC51C0506CAA93D8783340	0101000020E610000050BA3EBD63AC51C0F5B7A9262D793340
1102	2024-07-17 16:05:08.329	19.4766722	-70.7099474	B9288949	mariela	2024-04-19 19:52:24.575	321.568665841455	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000465A4205FEAD51C029BCD927AB7C3340
1103	2024-07-17 16:05:13.317	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:08.329	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1104	2024-07-17 16:05:18.196	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:13.317	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1105	2024-07-17 16:05:23.17	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:18.196	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1106	2024-07-17 16:05:28.383	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:23.17	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1107	2024-07-17 16:05:33.224	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:28.383	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1108	2024-07-17 16:05:38.2	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:33.224	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1109	2024-07-17 16:05:43.143	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:38.2	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1110	2024-07-17 16:05:48.231	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:43.143	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1111	2024-07-17 16:05:53.273	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:48.231	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1112	2024-07-17 16:05:58.253	19.4766722	-70.7099474	B9288949	mariela	2024-07-17 16:05:53.273	0	0101000020E6100000563838C76FAD51C091E97530077A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1152	2024-07-17 16:29:33.687	19.476645	-70.7099271	B9288949	mariela	2024-07-17 16:05:58.253	324.869134577723	0101000020E6100000D34213726FAD51C04BCD1E68057A3340	0101000020E6100000563838C76FAD51C091E97530077A3340
1153	2024-07-17 16:29:48.694	19.476645	-70.7099271	B9288949	mariela	2024-07-17 16:29:33.687	0	0101000020E6100000D34213726FAD51C04BCD1E68057A3340	0101000020E6100000D34213726FAD51C04BCD1E68057A3340
1154	2024-07-17 16:29:53.671	19.476645	-70.7099271	B9288949	mariela	2024-07-17 16:29:48.694	0	0101000020E6100000D34213726FAD51C04BCD1E68057A3340	0101000020E6100000D34213726FAD51C04BCD1E68057A3340
1155	2024-07-17 16:29:58.573	19.476645	-70.7099271	B9288949	mariela	2024-07-17 16:29:53.671	0	0101000020E6100000D34213726FAD51C04BCD1E68057A3340	0101000020E6100000D34213726FAD51C04BCD1E68057A3340
1156	2024-07-17 16:30:03.646	19.476645	-70.7099271	B9288949	mariela	2024-07-17 16:29:58.573	0	0101000020E6100000D34213726FAD51C04BCD1E68057A3340	0101000020E6100000D34213726FAD51C04BCD1E68057A3340
1202	2024-07-30 08:03:17.147	19.4766455	-70.7098886	B9288949	mariela	2024-07-17 16:30:03.646	269.210791578574	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000D34213726FAD51C04BCD1E68057A3340
1203	2024-07-30 08:03:22.108	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:17.147	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1204	2024-07-30 08:03:27.258	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:22.108	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1205	2024-07-30 08:03:32.217	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:27.258	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1206	2024-07-30 08:03:37.059	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:32.217	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1207	2024-07-30 08:03:42.16	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:37.059	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1208	2024-07-30 08:03:47.175	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:42.16	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1209	2024-07-30 08:03:52.153	19.4766455	-70.7098886	B9288949	mariela	2024-07-30 08:03:47.175	0	0101000020E6100000683398D06EAD51C01B498270057A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1252	2024-07-31 07:58:03.668	19.4766819	-70.7099304	B9288949	mariela	2024-07-30 08:03:52.153	132.727666750117	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000683398D06EAD51C01B498270057A3340
1253	2024-07-31 07:58:08.5	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:03.668	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1254	2024-07-31 07:58:13.609	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:08.5	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1255	2024-07-31 07:58:18.562	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:13.609	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1256	2024-07-31 07:58:23.577	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:18.562	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1257	2024-07-31 07:58:28.514	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:23.577	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1258	2024-07-31 07:58:33.492	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:28.514	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1259	2024-07-31 07:58:38.585	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:33.492	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1302	2024-07-31 08:06:51.364	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 07:58:38.585	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1303	2024-07-31 08:06:56.331	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 08:06:51.364	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1304	2024-07-31 08:07:01.421	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 08:06:56.331	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1305	2024-07-31 08:07:06.451	19.4766819	-70.7099304	B9288949	mariela	2024-07-31 08:07:01.421	0	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1352	2024-07-31 08:13:45.939	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:07:06.451	306.860155943758	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E6100000EB9BEA7F6FAD51C05B1833D3077A3340
1353	2024-07-31 08:13:50.825	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:13:45.939	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1354	2024-07-31 08:13:55.842	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:13:50.825	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1355	2024-07-31 08:14:00.927	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:13:55.842	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1356	2024-07-31 08:14:05.791	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:14:00.927	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1357	2024-07-31 08:14:46.614	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:14:05.791	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1358	2024-07-31 08:14:51.397	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:14:46.614	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1359	2024-07-31 08:14:56.37	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:14:51.397	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1360	2024-07-31 08:15:16.65	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:14:56.37	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1361	2024-07-31 08:15:21.611	19.476625	-70.7098499	B9288949	mariela	2024-07-31 08:15:16.65	0	0101000020E61000006864462E6EAD51C0BC749318047A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1362	2024-07-31 09:56:21.507	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 08:15:21.611	10.372046929633	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E61000006864462E6EAD51C0BC749318047A3340
1363	2024-07-31 09:56:26.476	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:56:21.507	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1364	2024-07-31 09:56:31.479	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:56:26.476	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1365	2024-07-31 09:56:36.495	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:56:31.479	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1366	2024-07-31 09:56:51.688	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:56:36.495	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1367	2024-07-31 09:56:56.5	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:56:51.688	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1368	2024-07-31 09:57:01.658	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:56:56.5	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1369	2024-07-31 09:57:06.464	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:01.658	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1370	2024-07-31 09:57:11.464	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:06.464	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1371	2024-07-31 09:57:16.465	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:11.464	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1372	2024-07-31 09:57:21.588	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:16.465	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1373	2024-07-31 09:57:26.509	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:21.588	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1374	2024-07-31 09:57:33.567	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:26.509	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1375	2024-07-31 09:57:36.527	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:33.567	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1376	2024-07-31 09:57:42.733	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:36.527	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1377	2024-07-31 09:57:46.54	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:42.733	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1378	2024-07-31 09:57:51.456	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:46.54	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1379	2024-07-31 09:57:56.45	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:51.456	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1380	2024-07-31 09:58:01.738	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:57:56.45	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1381	2024-07-31 09:58:06.488	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:01.738	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1382	2024-07-31 09:58:11.453	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:06.488	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1383	2024-07-31 09:58:16.483	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:11.453	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1384	2024-07-31 09:58:21.59	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:16.483	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1385	2024-07-31 09:58:26.48	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:21.59	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1386	2024-07-31 09:58:31.449	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:26.48	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1387	2024-07-31 09:58:36.506	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:31.449	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1388	2024-07-31 09:58:41.556	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:36.506	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1389	2024-07-31 09:58:46.496	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:41.556	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1390	2024-07-31 09:58:51.465	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:46.496	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1391	2024-07-31 09:58:56.521	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:51.465	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1392	2024-07-31 09:59:01.75	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:58:56.521	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1393	2024-07-31 09:59:06.503	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:59:01.75	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1394	2024-07-31 09:59:11.44	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:59:06.503	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1395	2024-07-31 09:59:16.478	19.4765977	-70.7098552	B9288949	mariela	2024-07-31 09:59:11.44	0	0101000020E6100000513981446EAD51C04CD98E4E027A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1396	2024-07-31 10:03:20.95	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 09:59:16.478	139.842585637708	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E6100000513981446EAD51C04CD98E4E027A3340
1397	2024-07-31 10:03:25.887	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 10:03:20.95	0	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1398	2024-07-31 10:03:31.088	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 10:03:25.887	0	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1399	2024-07-31 10:03:35.964	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 10:03:31.088	0	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1400	2024-07-31 10:03:40.953	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 10:03:35.964	0	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1401	2024-07-31 10:11:51.292	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 10:03:40.953	0	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1402	2024-07-31 10:11:56.436	19.4766758	-70.7099251	B9288949	mariela	2024-07-31 10:11:51.292	0	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1403	2024-07-31 10:12:01.278	19.4766792	-70.7099248	B9288949	mariela	2024-07-31 10:11:56.436	184.755271619045	0101000020E6100000A3A76D686FAD51C0F6AEE6A5077A3340	0101000020E610000003C7AF696FAD51C06DCBDB6C077A3340
1404	2024-07-31 10:12:06.285	19.4766792	-70.7099248	B9288949	mariela	2024-07-31 10:12:01.278	0	0101000020E6100000A3A76D686FAD51C0F6AEE6A5077A3340	0101000020E6100000A3A76D686FAD51C0F6AEE6A5077A3340
1452	2024-07-31 10:33:31.816	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:12:06.285	328.83720935845	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000A3A76D686FAD51C0F6AEE6A5077A3340
1453	2024-07-31 10:33:36.751	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:33:31.816	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1454	2024-07-31 10:33:41.76	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:33:36.751	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1455	2024-07-31 10:33:46.838	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:33:41.76	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1456	2024-07-31 10:33:51.851	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:33:46.838	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1457	2024-07-31 10:33:56.777	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:33:51.851	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1458	2024-07-31 10:34:01.746	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:33:56.777	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1459	2024-07-31 10:34:06.973	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:01.746	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1460	2024-07-31 10:34:11.889	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:06.973	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1461	2024-07-31 10:34:16.83	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:11.889	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1462	2024-07-31 10:34:21.89	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:16.83	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1463	2024-07-31 10:34:26.811	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:21.89	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1464	2024-07-31 10:34:31.92	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:26.811	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1465	2024-07-31 10:34:36.749	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:31.92	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1466	2024-07-31 10:34:41.754	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:36.749	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1467	2024-07-31 10:34:46.92	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:41.754	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1468	2024-07-31 10:34:51.783	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:46.92	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1469	2024-07-31 10:34:56.795	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:51.783	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1470	2024-07-31 10:35:01.811	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:34:56.795	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1471	2024-07-31 10:35:06.977	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:01.811	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1472	2024-07-31 10:35:11.819	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:06.977	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1473	2024-07-31 10:35:16.807	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:11.819	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1474	2024-07-31 10:35:21.879	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:16.807	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1475	2024-07-31 10:35:26.854	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:21.879	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1476	2024-07-31 10:35:31.811	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:26.854	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1477	2024-07-31 10:35:36.834	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:31.811	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1478	2024-07-31 10:35:41.805	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:36.834	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1479	2024-07-31 10:35:46.862	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:41.805	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1480	2024-07-31 10:35:51.804	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:46.862	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1481	2024-07-31 10:35:56.752	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:51.804	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1482	2024-07-31 10:36:01.867	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:35:56.752	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1483	2024-07-31 10:36:06.796	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:01.867	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1484	2024-07-31 10:36:11.811	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:06.796	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1485	2024-07-31 10:36:16.733	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:11.811	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1486	2024-07-31 10:36:21.889	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:16.733	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1487	2024-07-31 10:36:26.816	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:21.889	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1488	2024-07-31 10:36:31.875	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:26.816	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1489	2024-07-31 10:36:36.753	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:31.875	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1490	2024-07-31 10:36:41.728	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:36.753	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1491	2024-07-31 10:36:46.732	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:41.728	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1492	2024-07-31 10:36:51.8	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:46.732	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1493	2024-07-31 10:36:56.768	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:51.8	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1494	2024-07-31 10:37:01.99	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:36:56.768	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1495	2024-07-31 10:37:06.787	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:01.99	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1496	2024-07-31 10:37:11.817	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:06.787	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1497	2024-07-31 10:37:16.734	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:11.817	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1498	2024-07-31 10:37:21.794	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:16.734	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1499	2024-07-31 10:37:26.813	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:21.794	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1500	2024-07-31 10:37:31.864	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:26.813	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1501	2024-07-31 10:37:36.765	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:31.864	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1502	2024-07-31 10:37:41.812	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:36.765	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1503	2024-07-31 10:37:46.78	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:41.812	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1504	2024-07-31 10:37:51.791	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:46.78	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1505	2024-07-31 10:37:56.778	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:51.791	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1506	2024-07-31 10:38:01.763	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:37:56.778	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1507	2024-07-31 10:38:06.799	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:01.763	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1508	2024-07-31 10:38:11.824	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:06.799	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1509	2024-07-31 10:38:16.761	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:11.824	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1510	2024-07-31 10:38:21.819	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:16.761	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1511	2024-07-31 10:38:26.858	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:21.819	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1512	2024-07-31 10:38:31.785	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:26.858	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1513	2024-07-31 10:38:36.79	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:31.785	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1514	2024-07-31 10:38:41.765	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:36.79	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1515	2024-07-31 10:38:46.834	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:41.765	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1516	2024-07-31 10:38:51.814	19.4765986	-70.7098731	B9288949	mariela	2024-07-31 10:38:46.834	0	0101000020E6100000D933958F6EAD51C0C351A85D027A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1517	2024-07-31 10:41:21.625	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:38:51.814	117.551500274221	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000D933958F6EAD51C0C351A85D027A3340
1518	2024-07-31 10:41:26.58	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:41:21.625	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1519	2024-07-31 10:41:31.682	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:41:26.58	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1520	2024-07-31 10:41:36.659	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:41:31.682	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1521	2024-07-31 10:58:51.156	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:41:36.659	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1522	2024-07-31 10:58:56.141	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:58:51.156	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1523	2024-07-31 10:59:01.193	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:58:56.141	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1524	2024-07-31 10:59:06.092	19.4766137	-70.7099038	B9288949	mariela	2024-07-31 10:59:01.193	0	0101000020E6100000981359106FAD51C05853FE5A037A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1552	2024-07-31 11:50:31.701	19.4766338	-70.7098597	B9288949	mariela	2024-07-31 10:59:06.092	244.198735828403	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340	0101000020E6100000981359106FAD51C05853FE5A037A3340
1553	2024-07-31 11:50:36.607	19.4766338	-70.7098597	B9288949	mariela	2024-07-31 11:50:31.701	0	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340
1554	2024-07-31 11:50:51.699	19.4766338	-70.7098597	B9288949	mariela	2024-07-31 11:50:36.607	0	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340
1555	2024-07-31 11:50:56.704	19.4766338	-70.7098597	B9288949	mariela	2024-07-31 11:50:51.699	0	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340
1556	2024-07-31 11:51:01.663	19.4766338	-70.7098597	B9288949	mariela	2024-07-31 11:50:56.704	0	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340
1557	2024-07-31 12:57:21.739	19.4766202	-70.7098762	B9288949	mariela	2024-07-31 11:51:01.663	48.837760532271	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340	0101000020E6100000E50F61576EAD51C00F2B37AC047A3340
1558	2024-07-31 12:57:26.703	19.4766202	-70.7098762	B9288949	mariela	2024-07-31 12:57:21.739	0	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340
1559	2024-07-31 12:57:31.722	19.4766202	-70.7098762	B9288949	mariela	2024-07-31 12:57:26.703	0	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340
1560	2024-07-31 12:57:36.636	19.4766202	-70.7098762	B9288949	mariela	2024-07-31 12:57:31.722	0	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340
1561	2024-07-31 13:16:46.235	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 12:57:36.636	152.209937101434	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E61000005CCD959C6EAD51C0EC9C0BC8037A3340
1562	2024-07-31 13:16:51.246	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 13:16:46.235	0	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1563	2024-07-31 13:16:56.276	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 13:16:51.246	0	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1564	2024-07-31 13:17:01.152	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 13:16:56.276	0	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1565	2024-07-31 13:17:06.15	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 13:17:01.152	0	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1566	2024-07-31 13:17:11.308	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 13:17:06.15	0	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1567	2024-07-31 13:17:16.226	19.4766524	-70.7098942	B9288949	mariela	2024-07-31 13:17:11.308	0	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1568	2024-07-31 13:41:41.223	19.4766766	-70.7099265	B9288949	mariela	2024-07-31 13:17:16.226	128.474191412429	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340	0101000020E6100000AF2715E86EAD51C0568F45E4057A3340
1569	2024-07-31 13:41:46.158	19.4766766	-70.7099265	B9288949	mariela	2024-07-31 13:41:41.223	0	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340
1570	2024-07-31 13:41:51.14	19.4766766	-70.7099265	B9288949	mariela	2024-07-31 13:41:46.158	0	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340
1571	2024-07-31 13:41:56.123	19.4766766	-70.7099265	B9288949	mariela	2024-07-31 13:41:51.14	0	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340	0101000020E610000014048F6F6FAD51C0BBC4477A077A3340
\.


--
-- Data for Name: vhl; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.vhl (pl, mke_at, cap_pax, upd_at, mde_at, actualizado_por_id, estado_dat, hecho_por_id, marca_dat, modelo_dat, tipo_vehiculo_dat, color_dat, act, tkn, ruta_rta) FROM stdin;
A1547858	0	0	2024-07-02 21:15:04.011	2024-04-12 18:27:58.831	1	Estacionado	1	Mercedes Benz	Citaro	Autobus	Verde	t		\N
B9288949	0	0	2024-07-31 13:41:57.897	2024-04-12 19:13:36.646	1	Estacionado	1	Scania	OmniExpress	Autobus	Amarillo	t		\N
\.


--
-- Data for Name: vhl_log; Type: TABLE DATA; Schema: transport; Owner: cabreu
--

COPY transport.vhl_log (id, est_new, est_old, reg_dt, lat, lon, pl, rta_old, rta_new, sys) FROM stdin;
1	Averiado	Estacionado	2024-07-23 08:08:04.473001	\N	\N	B9288949	\N	\N	f
2	Estacionado	Averiado	2024-07-23 08:20:38.23192	\N	\N	B9288949	\N	\N	f
3	Averiado	Estacionado	2024-07-23 08:21:18.846432	\N	\N	B9288949	\N	\N	f
4	Estacionado	Averiado	2024-07-23 08:28:11.52587	\N	\N	B9288949	\N	\N	f
5	Averiado	Estacionado	2024-07-23 08:30:41.227814	\N	\N	B9288949	\N	\N	f
6	Estacionado	Averiado	2024-07-23 08:32:43.30765	\N	\N	B9288949	\N	\N	f
7	Averiado	Estacionado	2024-07-23 08:33:18.230322	\N	\N	B9288949	\N	\N	f
8	Estacionado	Averiado	2024-07-23 08:35:10.396771	\N	\N	B9288949	\N	\N	f
9	Averiado	Estacionado	2024-07-23 08:35:22.618169	\N	\N	B9288949	\N	\N	f
10	Estacionado	Averiado	2024-07-23 08:35:39.342833	\N	\N	B9288949	\N	\N	f
11	Averiado	Estacionado	2024-07-23 09:21:25.252826	\N	\N	A1547858	\N	\N	f
12	Estacionado	Averiado	2024-07-23 09:27:35.336218	\N	\N	A1547858	\N	\N	f
13	Averiado	Estacionado	2024-07-23 09:30:39.93915	\N	\N	B9288949	\N	\N	f
14	Estacionado	Averiado	2024-07-23 09:30:55.278436	\N	\N	B9288949	\N	\N	f
15	En Camino	Estacionado	2024-07-30 08:03:15.000829	\N	\N	B9288949	\N	mariela	f
16	Estacionado	En Camino	2024-07-30 08:12:18.135031	19.4766455	-70.7098886	B9288949	mariela	\N	f
17	En Camino	Estacionado	2024-07-31 07:58:01.625213	\N	\N	B9288949	\N	mariela	f
18	Estacionado	En Camino	2024-07-31 08:01:06.332847	19.4766819	-70.7099304	B9288949	mariela	\N	f
19	En Camino	Estacionado	2024-07-31 08:06:48.454569	\N	\N	B9288949	\N	mariela	f
20	Estacionado	En Camino	2024-07-31 08:10:02.203957	19.4766819	-70.7099304	B9288949	mariela	\N	t
21	En Camino	Estacionado	2024-07-31 08:13:41.074486	\N	\N	B9288949	\N	mariela	f
22	Averiado	En Camino	2024-07-31 08:14:08.112466	19.476625	-70.7098499	B9288949	mariela	\N	f
23	Estacionado	Averiado	2024-07-31 08:14:34.41954	\N	\N	B9288949	\N	\N	f
24	En Camino	Estacionado	2024-07-31 08:14:41.807493	\N	\N	B9288949	\N	mariela	f
25	Estacionado	En Camino	2024-07-31 08:14:59.450756	19.476625	-70.7098499	B9288949	mariela	\N	f
26	En Camino	Estacionado	2024-07-31 08:15:11.924978	\N	\N	B9288949	\N	mariela	f
27	Estacionado	En Camino	2024-07-31 08:18:03.575995	19.476625	-70.7098499	B9288949	mariela	\N	t
28	En Camino	Estacionado	2024-07-31 09:56:17.678653	\N	\N	B9288949	\N	mariela	f
29	Estacionado	En Camino	2024-07-31 09:56:39.619097	19.4765977	-70.7098552	B9288949	mariela	\N	f
30	En Camino	Estacionado	2024-07-31 09:56:46.861424	\N	\N	B9288949	\N	mariela	f
31	Estacionado	En Camino	2024-07-31 09:59:21.447629	19.4765977	-70.7098552	B9288949	mariela	\N	f
32	En Camino	Estacionado	2024-07-31 10:03:18.664907	\N	\N	B9288949	\N	mariela	f
33	Estacionado	En Camino	2024-07-31 10:06:03.573946	19.4766758	-70.7099251	B9288949	mariela	\N	t
34	En Camino	Estacionado	2024-07-31 10:11:47.243053	\N	\N	B9288949	\N	mariela	f
35	Estacionado	En Camino	2024-07-31 10:14:49.383518	19.4766792	-70.7099248	B9288949	mariela	\N	f
36	En Camino	Estacionado	2024-07-31 10:33:29.913284	\N	\N	B9288949	\N	mariela	f
37	Estacionado	En Camino	2024-07-31 10:38:54.472068	19.4765986	-70.7098731	B9288949	mariela	\N	f
38	En Camino	Estacionado	2024-07-31 10:41:18.725832	\N	\N	B9288949	\N	mariela	f
39	Estacionado	En Camino	2024-07-31 10:42:58.611258	19.4766137	-70.7099038	B9288949	mariela	\N	t
40	En Camino	Estacionado	2024-07-31 10:58:46.074801	\N	\N	B9288949	\N	mariela	f
41	Estacionado	En Camino	2024-07-31 11:00:58.611384	19.4766137	-70.7099038	B9288949	mariela	\N	t
42	En Camino	Estacionado	2024-07-31 11:50:30.568949	\N	\N	B9288949	\N	mariela	f
43	Estacionado	En Camino	2024-07-31 11:50:40.118616	19.4766338	-70.7098597	B9288949	mariela	\N	f
44	En Camino	Estacionado	2024-07-31 11:50:46.907354	\N	\N	B9288949	\N	mariela	f
45	Estacionado	En Camino	2024-07-31 11:52:39.073051	19.4766338	-70.7098597	B9288949	mariela	\N	t
46	En Camino	Estacionado	2024-07-31 12:57:16.790014	\N	\N	B9288949	\N	mariela	f
47	Estacionado	En Camino	2024-07-31 12:58:39.070599	19.4766202	-70.7098762	B9288949	mariela	\N	t
48	En Camino	Estacionado	2024-07-31 13:16:42.273513	\N	\N	B9288949	\N	mariela	f
49	Estacionado	En Camino	2024-07-31 13:18:39.070404	19.4766524	-70.7098942	B9288949	mariela	\N	t
50	En Camino	Estacionado	2024-07-31 13:41:36.173551	\N	\N	B9288949	\N	mariela	f
51	Estacionado	En Camino	2024-07-31 13:41:57.898597	19.4766766	-70.7099265	B9288949	mariela	\N	f
52	En Camino	Estacionado	2024-08-14 10:54:03.487811	\N	\N	B9288949	\N	\N	f
53	Estacionado	En Camino	2024-08-14 11:05:04.670226	\N	\N	B9288949	\N	\N	f
54	En Camino	Estacionado	2024-08-14 11:07:11.173472	\N	\N	B9288949	\N	\N	f
55	Estacionado	En Camino	2024-08-14 11:07:17.496141	\N	\N	B9288949	\N	\N	f
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
-- Name: prd_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.prd_seq', 51, true);


--
-- Name: pub_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.pub_id_seq', 1, false);


--
-- Name: pub_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.pub_seq', 101, true);


--
-- Name: rta_asg_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.rta_asg_seq', 1, false);


--
-- Name: rta_loc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.rta_loc_seq', 1451, true);


--
-- Name: rta_pda_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.rta_pda_seq', 1, false);


--
-- Name: sell_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.sell_seq', 1, false);


--
-- Name: sta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.sta_id_seq', 158, true);


--
-- Name: trp_loc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.trp_loc_seq', 1601, true);


--
-- Name: usr_acc_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.usr_acc_seq', 551, true);


--
-- Name: usr_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.usr_seq', 1, false);


--
-- Name: vhl_log_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.vhl_log_seq', 1, false);


--
-- Name: vis_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabreu
--

SELECT pg_catalog.setval('public.vis_log_id_seq', 43, true);


--
-- Name: vhl_log_id_seq; Type: SEQUENCE SET; Schema: transport; Owner: cabreu
--

SELECT pg_catalog.setval('transport.vhl_log_id_seq', 55, true);


--
-- Name: prd prd_pkey; Type: CONSTRAINT; Schema: inventory; Owner: cabreu
--

ALTER TABLE ONLY inventory.prd
    ADD CONSTRAINT prd_pkey PRIMARY KEY (id);


--
-- Name: sell sell_pkey; Type: CONSTRAINT; Schema: inventory; Owner: cabreu
--

ALTER TABLE ONLY inventory.sell
    ADD CONSTRAINT sell_pkey PRIMARY KEY (id);


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
-- Name: pub pub_pk; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.pub
    ADD CONSTRAINT pub_pk PRIMARY KEY (id);


--
-- Name: sta sta_pk; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.sta
    ADD CONSTRAINT sta_pk PRIMARY KEY (id);


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
-- Name: vis_log vis_log_pk; Type: CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.vis_log
    ADD CONSTRAINT vis_log_pk PRIMARY KEY (id);


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
-- Name: vhl_log vhl_log_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl_log
    ADD CONSTRAINT vhl_log_pkey PRIMARY KEY (id);


--
-- Name: vhl vhl_pkey; Type: CONSTRAINT; Schema: transport; Owner: cabreu
--

ALTER TABLE ONLY transport.vhl
    ADD CONSTRAINT vhl_pkey PRIMARY KEY (pl);


--
-- Name: vis_log_dt_idx; Type: INDEX; Schema: public; Owner: cabreu
--

CREATE INDEX vis_log_dt_idx ON public.vis_log USING btree (dt);


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
-- Name: vis_log vis_log_notify_trigger; Type: TRIGGER; Schema: public; Owner: cabreu
--

CREATE TRIGGER vis_log_notify_trigger AFTER INSERT ON public.vis_log FOR EACH STATEMENT EXECUTE FUNCTION public.notify_trigger();


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
-- Name: rta_loc route_points_after_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER route_points_after_insert AFTER INSERT OR UPDATE ON transport.rta_loc FOR EACH ROW EXECUTE FUNCTION transport.refresh_route_lines();


--
-- Name: rta_loc route_points_before_insert; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER route_points_before_insert BEFORE INSERT OR UPDATE ON transport.rta_loc FOR EACH ROW EXECUTE FUNCTION transport.update_geom();


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
-- Name: vhl vhl_log_creation; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER vhl_log_creation AFTER UPDATE ON transport.vhl FOR EACH ROW EXECUTE FUNCTION transport.log_vhl_trigger();


--
-- Name: vhl_log vhl_log_notify; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER vhl_log_notify AFTER INSERT OR DELETE OR UPDATE ON transport.vhl_log FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: vhl vhl_notify; Type: TRIGGER; Schema: transport; Owner: cabreu
--

CREATE TRIGGER vhl_notify AFTER INSERT OR DELETE OR UPDATE ON transport.vhl FOR EACH ROW EXECUTE FUNCTION public.notify_trigger();


--
-- Name: prd fk1fako6tousb4huns0swqjuno4; Type: FK CONSTRAINT; Schema: inventory; Owner: cabreu
--

ALTER TABLE ONLY inventory.prd
    ADD CONSTRAINT fk1fako6tousb4huns0swqjuno4 FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: prd fk3844ocp8s0x7aj7ou5wb23xfs; Type: FK CONSTRAINT; Schema: inventory; Owner: cabreu
--

ALTER TABLE ONLY inventory.prd
    ADD CONSTRAINT fk3844ocp8s0x7aj7ou5wb23xfs FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- Name: sell fk44evbxbvqsadhjh9d07lwr1nf; Type: FK CONSTRAINT; Schema: inventory; Owner: cabreu
--

ALTER TABLE ONLY inventory.sell
    ADD CONSTRAINT fk44evbxbvqsadhjh9d07lwr1nf FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


--
-- Name: prd fkd375jfy3c3l6ftx1x3swkg11r; Type: FK CONSTRAINT; Schema: inventory; Owner: cabreu
--

ALTER TABLE ONLY inventory.prd
    ADD CONSTRAINT fkd375jfy3c3l6ftx1x3swkg11r FOREIGN KEY (categoria_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: emp fkaah00j9itntebt33e65xjs9mi; Type: FK CONSTRAINT; Schema: payroll; Owner: cabreu
--

ALTER TABLE ONLY payroll.emp
    ADD CONSTRAINT fkaah00j9itntebt33e65xjs9mi FOREIGN KEY (puesto_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: emp fkgwbl17vueiiijn46gw5i93exr; Type: FK CONSTRAINT; Schema: payroll; Owner: cabreu
--

ALTER TABLE ONLY payroll.emp
    ADD CONSTRAINT fkgwbl17vueiiijn46gw5i93exr FOREIGN KEY (sexo_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: ppl_inf fk2wgx4om2tperqiov1krlu8flr; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.ppl_inf
    ADD CONSTRAINT fk2wgx4om2tperqiov1krlu8flr FOREIGN KEY (sexo_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: pub fka1vv5hpk6yb6chmg46qml8yyc; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.pub
    ADD CONSTRAINT fka1vv5hpk6yb6chmg46qml8yyc FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: pub fkakhhucrb3ekboda0h1tjh4xt1; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.pub
    ADD CONSTRAINT fkakhhucrb3ekboda0h1tjh4xt1 FOREIGN KEY (empresa_dat) REFERENCES public.gnr_dat(dat);


--
-- Name: gnr_dat fkgg156xffcc3fb7l66rkr56nss; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.gnr_dat
    ADD CONSTRAINT fkgg156xffcc3fb7l66rkr56nss FOREIGN KEY (actualizado_por_id) REFERENCES public.usr(id);


--
-- Name: pub fkhh9id1276jktxopibdghgah5v; Type: FK CONSTRAINT; Schema: public; Owner: cabreu
--

ALTER TABLE ONLY public.pub
    ADD CONSTRAINT fkhh9id1276jktxopibdghgah5v FOREIGN KEY (hecho_por_id) REFERENCES public.usr(id);


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

