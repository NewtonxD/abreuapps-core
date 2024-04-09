phase 1 make user, db(abreuapps_coredb) and schemas

phase 2 configure and db in spring application.properties

phase 2.5 inside app.prop set dll update moode (notes inside this file)

phase 3 restore data using console/terminal

/* run using postgres with superuser permission*/
/* psql -U postgres abreuapps_coredb */

/* TIME OF DATA: 31-03-2024 ONLY CORE DATA*/

ALTER TABLE public.dat_grp 
DISABLE TRIGGER ALL;

INSERT INTO public.dat_grp (grp,act,dsc,upd_at,mde_at,actualizado_por_id,hecho_por_id) VALUES
	 ('Menu',true,'Categorias para los menus del sistema',NULL,'2024-03-10 14:54:10',NULL,1),
	 ('Modulo',true,'Id de los modulos que se usan en el sistema',NULL,'2024-03-10 14:54:10',NULL,1),
	 ('Pantallas',true,'Id de las pantallas donde se utilizan los permisos','2023-08-23 12:28:22','2023-08-23 12:28:22',NULL,1),
	 ('Permisos',false,'Permisos del sistema.','2023-08-29 10:36:15','2023-07-14 17:23:10',1,1),
	 ('Sexo',true,'Categoria Sexo para la inf. personal',NULL,'2023-09-23 15:23:24',NULL,1),
	 ('Tipo de Datos',true,'Describe los Tipo de Datos manejados en el sistema','2023-08-23 12:25:52','2023-08-23 12:25:52',NULL,1),
	 ('Tipos de Permisos',true,'Los diferentes tipos de permisos del sistema.','2023-08-29 10:36:32','2023-08-29 10:36:32',NULL,1),
	 ('Tipos Sanguineos',true,'Categoría para los grupos de sangre.','2023-07-14 17:17:05','2023-07-04 10:22:49',1,1),
	 ('Colores',true,'Colores para el sistema','2024-03-21 18:22:19.862','2023-07-14 17:23:26',1,1),
	 ('Estados Vehiculo',true,'Representa los diferentes estados que puede tener un vehiculo de transporte.','2024-03-31 11:01:03.257','2024-03-31 11:01:03.257',NULL,1),
	 ('Tipo Vehiculo',true,'Representa los diferentes tipos de vehiculos que se pueden utilizar en el modulo de transporte.','2024-03-31 11:02:05.302','2024-03-31 11:02:05.302',NULL,1),
	 ('Marca',true,'Marca del vehiculo de transporte.','2024-03-31 11:07:26.999','2024-03-31 11:02:31.345',1,1),
	 ('Mercedes Benz',true,'Marca de vehiculo.','2024-03-31 11:07:43.714','2024-03-31 11:06:49.639',1,1),
	 ('Isuzu',true,'Marca de vehiculo.','2024-03-31 11:07:47.728','2024-03-31 11:07:19.283',1,1),
	 ('Volvo',true,'Marca de vehiculo.','2024-03-31 11:08:31.399','2024-03-31 11:08:31.399',NULL,1),
	 ('KYC',true,'Marca de vehiculo.','2024-03-31 11:11:01.955','2024-03-31 11:11:01.955',NULL,1),
	 ('Scania',true,'Marca de vehiculo.','2024-03-31 11:11:28.56','2024-03-31 11:11:28.56',NULL,1),
	 ('Setra',true,'Marca de vehiculo.','2024-03-31 11:11:57.152','2024-03-31 11:11:57.152',NULL,1),
	 ('MAN Truck & Bus',true,'Marca de vehiculo.','2024-03-31 11:14:53.457','2024-03-31 11:14:53.457',NULL,1),
	 ('Toyota',true,'Marca de vehiculo.','2024-03-31 11:15:11.092','2024-03-31 11:15:11.092',NULL,1),
	 ('Honda',true,'Marca de vehiculo.','2024-03-31 11:15:31.164','2024-03-31 11:15:31.164',NULL,1),
	 ('Iveco Bus',true,'Marca de vehiculo.','2024-03-31 11:16:06.364','2024-03-31 11:16:06.364',NULL,1),
	 ('Yutong Group',true,'Marca de vehiculo.','2024-03-31 11:16:34.49','2024-03-31 11:16:34.49',NULL,1),
	 ('Modelo',false,'Modelo del vehiculo, asociado a la Marca.','2024-03-31 11:21:57.253','2024-03-31 11:04:02.763',1,1);


ALTER TABLE public.dat_grp 
ENABLE TRIGGER ALL;


ALTER TABLE public.gnr_dat 
DISABLE TRIGGER ALL;

INSERT INTO public.gnr_dat (dat,act,dsc,upd_at,mde_at,actualizado_por_id,grupo_grp,hecho_por_id) VALUES
	 ('Interlink',true,'Modelo de autobuses Scania.','2024-03-31 11:49:56.014','2024-03-31 11:49:56.014',NULL,'Scania',1),
	 ('Touring',true,'Modelo de autobuses Scania.','2024-03-31 11:50:06.798','2024-03-31 11:50:06.798',NULL,'Scania',1),
	 ('OmniExpress',true,'Modelo de autobuses Scania.','2024-03-31 11:50:18.201','2024-03-31 11:50:18.201',NULL,'Scania',1),
	 ('OmniCity',true,'Modelo de autobuses Scania.','2024-03-31 11:50:30.145','2024-03-31 11:50:30.145',NULL,'Scania',1),
	 ('Metrolink',true,'Modelo de autobuses Scania.','2024-03-31 11:50:40.694','2024-03-31 11:50:40.694',NULL,'Scania',1),
	 ('Higer A30',true,'Modelo de autobuses Scania.','2024-03-31 11:50:53.402','2024-03-31 11:50:53.402',NULL,'Scania',1),
	 ('K series',true,'Modelo de autobuses Scania.','2024-03-31 11:51:25.358','2024-03-31 11:51:25.358',NULL,'Scania',1),
	 ('F series',true,'Modelo de autobuses Scania.','2024-03-31 11:51:33.755','2024-03-31 11:51:33.755',NULL,'Scania',1),
	 ('N series',true,'Modelo de autobuses Scania.','2024-03-31 11:51:46.73','2024-03-31 11:51:46.73',NULL,'Scania',1),
	 ('S 417',true,'Modelo de autobuses Setra.','2024-03-31 11:52:39.716','2024-03-31 11:52:39.716',NULL,'Setra',1),
	 ('S 415',true,'Modelo de autobuses Setra.','2024-03-31 11:52:49.668','2024-03-31 11:52:49.668',NULL,'Setra',1),
	 ('S 416',true,'Modelo de autobuses Setra.','2024-03-31 11:52:59.988','2024-03-31 11:52:59.988',NULL,'Setra',1),
	 ('S 417 HDH',true,'Modelo de autobuses Setra.','2024-03-31 11:53:12.1','2024-03-31 11:53:12.1',NULL,'Setra',1),
	 ('S 419 UL',true,'Modelo de autobuses Setra.','2024-03-31 11:53:23.772','2024-03-31 11:53:23.772',NULL,'Setra',1),
	 ('S 431 DT',true,'Modelo de autobuses Setra.','2024-03-31 11:53:42.052','2024-03-31 11:53:42.052',NULL,'Setra',1),
	 ('S 415 UL',true,'Modelo de autobuses Setra.','2024-03-31 11:53:59.879','2024-03-31 11:53:59.879',NULL,'Setra',1),
	 ('S 515 HD',true,'Modelo de autobuses Setra.','2024-03-31 11:54:59.859','2024-03-31 11:54:59.859',NULL,'Setra',1),
	 ('S 511 HD',true,'Modelo de autobuses Setra.','2024-03-31 11:55:13.696','2024-03-31 11:55:13.696',NULL,'Setra',1),
	 ('S 416 GT-HD',true,'Modelo de autobuses Setra.','2024-03-31 11:55:39.412','2024-03-31 11:55:39.412',NULL,'Setra',1),
	 ('Coaster',true,'Modelo de autobuses Toyota.','2024-03-31 11:56:42.427','2024-03-31 11:56:42.427',NULL,'Toyota',1),
	 ('HiAce Commuter',true,'Modelo de autobuses Toyota.','2024-03-31 11:56:56.241','2024-03-31 11:56:56.241',NULL,'Toyota',1),
	 ('Granvia',true,'Modelo de autobuses Toyota.','2024-03-31 11:57:06.972','2024-03-31 11:57:06.972',NULL,'Toyota',1),
	 ('Regius Ace',true,'Modelo de autobuses Toyota.','2024-03-31 11:57:16.717','2024-03-31 11:57:16.717',NULL,'Toyota',1),
	 ('7900',true,'Modelo de autobuses Volvo.','2024-03-31 11:58:06.558','2024-03-31 11:58:06.558',NULL,'Volvo',1),
	 ('8900',true,'Modelo de autobuses Volvo.','2024-03-31 11:58:15.262','2024-03-31 11:58:15.262',NULL,'Volvo',1),
	 ('9700',true,'Modelo de autobuses Volvo.','2024-03-31 11:58:25.145','2024-03-31 11:58:25.145',NULL,'Volvo',1),
	 ('9900',true,'Modelo de autobuses Volvo.','2024-03-31 11:58:35.48','2024-03-31 11:58:35.48',NULL,'Volvo',1),
	 ('B7R',true,'Modelo de autobuses Volvo.','2024-03-31 11:58:43.6','2024-03-31 11:58:43.6',NULL,'Volvo',1),
	 ('B8R',true,'Modelo de autobuses Volvo.','2024-03-31 11:58:53.364','2024-03-31 11:58:53.364',NULL,'Volvo',1),
	 ('B11R',true,'Modelo de autobuses Volvo.','2024-03-31 11:59:01.857','2024-03-31 11:59:01.857',NULL,'Volvo',1),
	 ('B12B',true,'Modelo de autobuses Volvo.','2024-03-31 11:59:11.117','2024-03-31 11:59:11.117',NULL,'Volvo',1),
	 ('B12M',true,'Modelo de autobuses Volvo.','2024-03-31 11:59:19.574','2024-03-31 11:59:19.574',NULL,'Volvo',1),
	 ('B13R',true,'Modelo de autobuses Volvo.','2024-03-31 11:59:28.517','2024-03-31 11:59:28.517',NULL,'Volvo',1),
	 ('ZK6126HGC',true,'Modelo de la marca Yutong Group.','2024-03-31 12:00:38.251','2024-03-31 12:00:38.251',NULL,'Yutong Group',1),
	 ('ZK6116HGA',true,'Modelo de la marca Yutong Group.','2024-03-31 12:00:50.299','2024-03-31 12:00:50.299',NULL,'Yutong Group',1),
	 ('ZK6120BEVG',true,'Modelo de la marca Yutong Group.','2024-03-31 12:01:02.931','2024-03-31 12:01:02.931',NULL,'Yutong Group',1),
	 ('ZK6120RAC',true,'Modelo de la marca Yutong Group.','2024-03-31 12:01:20.762','2024-03-31 12:01:20.762',NULL,'Yutong Group',1),
	 ('ZK6122H9',true,'Modelo de la marca Yutong Group.','2024-03-31 12:01:40.021','2024-03-31 12:01:40.021',NULL,'Yutong Group',1),
	 ('ZK6127HA',true,'Modelo de la marca Yutong Group.','2024-03-31 12:01:51.479','2024-03-31 12:01:51.479',NULL,'Yutong Group',1),
	 ('ZK6125CHEVG1',true,'Modelo de la marca Yutong Group.','2024-03-31 12:02:04.79','2024-03-31 12:02:04.79',NULL,'Yutong Group',1),
	 ('ZK6852HG',true,'Modelo de la marca Yutong Group.','2024-03-31 12:02:23.212','2024-03-31 12:02:23.212',NULL,'Yutong Group',1),
	 ('ZK6125CHEVGC1',true,'Modelo de la marca Yutong Group.','2024-03-31 12:02:45.183','2024-03-31 12:02:45.183',NULL,'Yutong Group',1),
	 ('ZK6119HA',true,'Modelo de la marca Yutong Group.','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Yutong Group',1),
	 ('Autobus',true,'Tipos de vehiculos del sistema.','2024-03-31 12:03:35.752','2024-03-31 12:03:35.752',NULL,'Tipo Vehiculo',1),
	 ('Camión',true,'Tipo de vehiculo.','2024-03-31 12:04:07.448','2024-03-31 12:04:07.448',NULL,'Tipo Vehiculo',1);




INSERT INTO public.gnr_dat (dat,act,dsc,upd_at,mde_at,actualizado_por_id,grupo_grp,hecho_por_id) VALUES
	 ('trp_principal',true,'Transporte','2024-03-31 12:02:23.212','2024-03-31 12:02:23.212',NULL,'Modulo',1),
	 ('con_principal',true,'Contabilidad','2024-03-31 12:02:45.183','2024-03-31 12:02:45.183',NULL,'Modulo',1),
	 ('cxp_principal',true,'Cuentas por Pagar','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Modulo',1),
	 ('sys_principal',true,'Sistemas','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Modulo',1),
	 ('con_conciliacion_bancaria',true,'Conciliación Bancaría','2024-03-31 12:02:23.212','2024-03-31 12:02:23.212',NULL,'Menu',1),
	 ('con_cuentas_bancarias',true,'Cuentas Bancarías','2024-03-31 12:02:45.183','2024-03-31 12:02:45.183',NULL,'Menu',1),
	 ('con_impuestos',true,'Impuestos','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('con_informes',true,'Informes y Reportes','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('con_presupuesto',true,'Presupuesto','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('con_registro_transacciones',true,'Transacciones','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('dat_gen_consulta_datos',true,'Datos','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Pantallas',1),
	 ('dat_gen_consulta_grupos',true,'Grupos de Datos','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Pantallas',1),
	 ('dat_gen_principal',true,'Datos del Sistema','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('sys_configuracion',true,'Configuraciones','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('trp_vehiculo_consulta',true,'Vehículos','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('usr_mgr_principal',true,'Usuarios','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
	 ('usr_mgr_registro',true,'Registro','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Pantallas',1),
	 ('usr_mgr_permisos',true,'Permisos','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Pantallas',1);
	 
	 

INSERT INTO public.gnr_dat (dat,act,dsc,upd_at,mde_at,actualizado_por_id,grupo_grp,hecho_por_id) VALUES
	 ('Masculino',true,'Sexo Masculino','2024-03-31 12:02:23.212','2024-03-31 12:02:23.212',NULL,'Sexo',1),
	 ('Femenino',true,'Sexo Femenino','2024-03-31 12:02:45.183','2024-03-31 12:02:45.183',NULL,'Sexo',1),
	 ('Indefinido',true,'Sexo Indefinido','2024-03-31 12:02:45.183','2024-03-31 12:02:45.183',NULL,'Sexo',1),
	 ('Booleano',true,'Dato true o false','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipo de Datos',1),
	 ('Cadena de Texto',true,'Dato texto','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipo de Datos',1),
	 ('Enteros',true,'Dato entero','2024-03-31 12:02:23.212','2024-03-31 12:02:23.212',NULL,'Tipo de Datos',1),
	 ('Decimales',true,'Dato decimal','2024-03-31 12:02:45.183','2024-03-31 12:02:45.183',NULL,'Tipo de Datos',1),
	 ('O+',true,'Sangre O+','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('O-',true,'Sangre O-','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('A+',true,'Sangre A+','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('A-',true,'Sangre A-','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('B+',true,'Sangre B+','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('B-',true,'Sangre B-','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('AB+',true,'Sangre AB+','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('AB-',true,'Sangre AB-','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Tipos Sanguineos',1),
	 ('Rojo',true,'Color Rojo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Azul',true,'Color Azul','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Amarillo',true,'Color Amarillo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Verde',true,'Color Verde','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Morado',true,'Color Morado','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Naranja',true,'Color Naranja','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Gris',true,'Color Gris','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Negro',true,'Color Negro','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Blanco',true,'Color Blanco','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Rosado',true,'Color Rosado','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Colores',1),
	 ('Estacionado',true,'Estado estacionado','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Estados Vehiculo',1),
	 ('En Parada',true,'Estado En Parada','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Estados Vehiculo',1),
	 ('Averiado',true,'Estado Averiado','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Estados Vehiculo',1),
	 ('En Camino',true,'Estado En Camino','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Estados Vehiculo',1),
	 ('Mantenimiento',true,'Estado Mantenimiento','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Estados Vehiculo',1),
	 ('Mercedes Benz',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Isuzu',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Volvo',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('KYC',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Scania',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Setra',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('MAN Truck & Bus',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Toyota',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Honda',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Iveco Bus',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1),
 	 ('Yutong Group',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Marca',1);
 	 	 	 	 	 	 
INSERT INTO public.gnr_dat (dat,act,dsc,upd_at,mde_at,actualizado_por_id,grupo_grp,hecho_por_id)  values
('Citaro',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('Tourismo',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('Intouro',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('Conecto',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('Sprinter City',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('CapaCity',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('eCitaro',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('Travego',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('OC 500 RF',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('OC 500 LE',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Mercedes Benz',1),
('MAN Lion"s City',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s Coach',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s Intercity',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s Regio',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s Star',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s Explorer',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s Intercity CNG',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s City Hybrid',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s City Electric',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('MAN Lion"s City CNG',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'MAN Truck & Bus',1),
('Crossway',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Urbanway',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Daily Minibus',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Magelys',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Evadys',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Crealis',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Irisbus Citelis',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Daily Tourys',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Ares',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('EuroRider',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('EuroCargo Bus',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Turbocity',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('CityClass',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Midys',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1),
('Eurorider Cursor',true,'Marca de Vehículo','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Iveco Bus',1);
	 	 
	 

INSERT INTO public.gnr_dat (dat,act,dsc,upd_at,mde_at,actualizado_por_id,grupo_grp,hecho_por_id)  values
('dat_gen_registro_datos',true,'Registro','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Pantallas',1),
('dat_gen_registro_grupos',true,'Registro','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Pantallas',1),
('cxp_registro_facturas',true,'Facturas','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
('cxp_control_pagos',true,'Control de Pagos','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
('cxp_informes',true,'Informes y Reportes','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
('cxp_comunicacion_proveedores',true,'Comunicación Proveedores','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
('cxp_autorizaciones_aprobaciones',true,'Autorizaciones','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
('trp_rutas_consulta',true,'Rutas','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1),
('trp_paradas_consulta',true,'Paradas','2024-03-31 12:02:56.006','2024-03-31 12:02:56.006',NULL,'Menu',1)
;	 	 	 	 
	 	 

ALTER TABLE public.gnr_dat 
ENABLE TRIGGER ALL;


ALTER TABLE public.acc 
DISABLE TRIGGER ALL;


INSERT INTO public.acc (id,act,fat_scr,pantalla_dat,tipo_dato_dat) VALUES
	 (1,true,'sys_principal','dat_gen_principal','Booleano'),
	 (6,true,'dat_gen_principal','dat_gen_consulta_datos','Booleano'),
	 (8,true,'dat_gen_consulta_datos','dat_gen_registro_datos','Booleano'),
	 (10,true,'dat_gen_principal','dat_gen_consulta_grupos','Booleano'),
	 (11,true,'dat_gen_consulta_grupos','dat_gen_registro_grupos','Booleano'),
	 (12,true,'sys_principal','usr_mgr_principal','Booleano'),
	 (13,true,'usr_mgr_principal','usr_mgr_registro','Booleano'),
	 (15,true,NULL,'sys_principal','Booleano'),
	 (17,true,NULL,'con_principal','Booleano'),
	 (16,true,'sys_principal','sys_configuracion','Booleano'),
	 (18,true,NULL,'cxp_principal','Booleano'),
	 (19,true,'cxp_principal','cxp_registro_facturas','Booleano'),
	 (20,true,'cxp_principal','cxp_control_pagos','Booleano'),
	 (21,true,'cxp_principal','cxp_informes','Booleano'),
	 (22,true,'cxp_principal','cxp_comunicacion_proveedores','Booleano'),
	 (23,true,'cxp_principal','cxp_autorizaciones_aprobaciones','Booleano'),
	 (24,true,'con_principal','con_registro_transacciones','Booleano'),
	 (25,true,'con_principal','con_conciliacion_bancaria','Booleano'),
	 (26,true,'con_principal','con_cuentas_bancarias','Booleano'),
	 (27,true,'con_principal','con_presupuesto','Booleano'),
	 (28,true,'con_principal','con_impuestos','Booleano'),
	 (29,true,'con_principal','con_informes','Booleano')
	 (30,true,NULL,'trp_principal','Booleano'),
	 (31,true,'trp_principal','trp_rutas_consulta','Booleano'),
	 (32,true,'trp_principal','trp_paradas_consulta','Booleano'),
	 (33,true,'trp_principal','trp_vehiculo_consulta','Booleano');
	 

ALTER TABLE public.acc 
ENABLE TRIGGER ALL;


ALTER TABLE public.ppl_inf 
DISABLE TRIGGER ALL;

INSERT INTO public.ppl_inf (id,apl,nic,ced,dir,upd_at,brt_at,mde_at,nam,emg_nam,num_cel,emg_tel,actualizado_por_id,hecho_por_id,sexo_dat,tipo_sangre_dat) VALUES
	 (3,'Abreu Pérez','','402-1017321-1','Pekin','2024-03-17 15:06:02','2005-01-13','2023-09-23 15:28:22','Esther','Yocasta Pérez','809-958-6826','809-913-3633',1,1,'Femenino','O+'),
	 (5,'Abreu','NEwton','402-1017327-1','Los alamos','2024-03-18 00:20:48','2004-01-31','2024-02-01 00:07:25','CArlos','','809-958-6826','',1,1,'Femenino','AB-'),
	 (2,'Abreu Pérez','newton','402-1017327-0','Los ciruelitos calle 1 con 14 diamela... excelente!!!','2024-03-21 17:01:19.204','2001-10-30','2023-09-23 15:25:30','Carlos Isaac','Yocasta Pérez','809-958-6826','809-913-3633',1,1,'Masculino','AB-');


ALTER TABLE public.ppl_inf 
ENABLE TRIGGER ALL;


ALTER TABLE public.usr
DISABLE TRIGGER ALL;

INSERT INTO public.usr (id,act,pwd_chg,pwd,mail,upd_at,mde_at,usr,actualizado_por_id,hecho_por_id,persona_id) VALUES
	 (2,true,false,'$2a$10$eu4bYQCJFUXSmKiIEu3/COg6.hX8TYOEgY9foRYi2BxP6idl5qMw.','carlosiap30@gmail.com','2024-03-17 15:06:02','2023-09-20 11:02:18','esther',1,1,3),
	 (4,true,false,'$2a$10$kOUT0mBy17tLYSd6ucIZAeBl71bBBl6tT7L4kc1ij5ahgA2Egb.Uy','qqqqq@gmail.com','2024-03-18 00:20:48','2024-02-01 00:07:26','newton300',1,1,5),
	 (1,true,false,'$2a$10$FD.HVab6z8H3Tba.hw.SvukdeJDfZ5aIIzCN87AL7T2SSAJqoi8Bq','carlosiabreup@gmail.com','2024-03-21 17:01:19.429','2023-09-20 11:02:18','newton30',1,1,2);


ALTER TABLE public.usr
ENABLE TRIGGER ALL;


ALTER TABLE public.usr_acc
DISABLE TRIGGER ALL;


INSERT INTO public.usr_acc (id,act,val,acceso_id,usuario_id) VALUES
	 (1,true,'true',1,1),
	 (2,true,'true',6,1),
	 (3,true,'true',8,1),
	 (4,true,'true',10,1),
	 (5,true,'true',12,1),
	 (9,true,'true',13,1),
	 (10,true,'true',1,2),
	 (11,true,'true',6,2),
	 (12,true,'true',8,2),
	 (13,true,'true',10,2),
	 (16,true,'true',15,1),
	 (17,true,'true',15,2),
	 (18,true,'true',11,2),
	 (19,true,'true',11,1),
	 (20,true,'true',16,1),
	 (52,true,'true',16,2),
	 (14,true,'true',12,2),
	 (15,true,'true',13,2),
	 (53,true,'true',18,2),
	 (54,true,'true',23,2),
	 (55,true,'true',19,2),
	 (56,true,'true',20,2),
	 (57,true,'true',21,2),
	 (58,true,'true',22,2),
	 (59,true,'true',17,2),
	 (60,true,'true',24,2),
	 (61,true,'true',29,2),
	 (62,true,'true',28,2),
	 (63,true,'true',27,2),
	 (64,true,'true',26,2),
	 (65,true,'true',25,2),
	 (102,true,'true',18,1),
	 (103,true,'true',23,1),
	 (104,true,'true',19,1),
	 (105,true,'true',20,1),
	 (106,true,'true',21,1),
	 (107,true,'true',22,1),
	 (108,true,'true',17,1),
	 (109,true,'true',24,1),
	 (110,true,'true',29,1),
	 (111,true,'true',28,1),
	 (112,true,'true',27,1),
	 (113,true,'true',26,1),
	 (114,true,'true',25,1);


ALTER TABLE public.usr_acc
ENABLE TRIGGER ALL;

INSERT INTO public.sys_cnf (cod,dsc,val,upd_at,actualizado_por_id) VALUES
	 ('appnombre','Nombre del sistema','STP OMSA','2024-03-23 20:00:38.714',1);



phase 4 enjoy!



