phase 1 make user, db(abreuapps_coredb) and schemas

phase 2 configure and db in spring application.properties

phase 2.5 inside app.prop set dll update moode (notes inside this file)

phase 3 restore data using console/terminal

/* run using postgres with superuser permission*/
/* psql -U postgres abreuapps_coredb */

/* TIME OF DATA: 24-03-2024 ONLY CORE DATA*/

ALTER TABLE public.dat_grp 
DISABLE TRIGGER ALL;

INSERT INTO public.dat_grp (grp,act,dsc,hecho_por_id,mde_at,actualizado_por_id,upd_at) VALUES ('Colores',true,'Colores para el sistema.',1,'2023-07-14 17:23:26',1,'2023-08-27 05:47:24'),('Menu',true,'Categorias para los menus del sistema',1,'2024-03-10 14:54:10',NULL,NULL),('Modulo',true,'Id de los modulos que se usan en el sistema',1,'2024-03-10 14:54:10',NULL,NULL),('Pantallas',true,'Id de las pantallas donde se utilizan los permisos',1,'2023-08-23 12:28:22',NULL,'2023-08-23 12:28:22'),('Permisos',false,'Permisos del sistema.',1,'2023-07-14 17:23:10',1,'2023-08-29 10:36:15'),('Sexo',true,'Categoria Sexo para la inf. personal',1,'2023-09-23 15:23:24',NULL,NULL),('Tipo de Datos',true,'Describe los Tipo de Datos manejados en el sistema',1,'2023-08-23 12:25:52',NULL,'2023-08-23 12:25:52'),('Tipos de Permisos',true,'Los diferentes tipos de permisos del sistema.',1,'2023-08-29 10:36:32',NULL,'2023-08-29 10:36:32'),('Tipos Sanguineos',true,'Categoría para los grupos de sangre.',1,'2023-07-04 10:22:49',1,'2023-07-14 17:17:05');

ALTER TABLE public.dat_grp 
ENABLE TRIGGER ALL;


ALTER TABLE public.gnr_dat 
DISABLE TRIGGER ALL;

INSERT INTO public.gnr_dat(dat,act,dsc,grupo_grp,hecho_por_id,mde_at,actualizado_por_id,upd_at) VALUES ('AB-',true,'Sangre AB Negativo','Tipos Sanguineos',1,'2023-09-23 16:15:41',1,'2024-02-06 02:46:55'),('AB+',true,'Sangre AB Positivo','Tipos Sanguineos',1,'2023-09-23 16:15:35',1,'2023-10-05 11:06:43'),('Amarillo',true,'Color primario.','Colores',1,'2023-08-27 12:17:03',2,'2023-10-05 11:06:11'),('Azul',false,'color del cielo\r\n','Colores',1,'2023-12-31 23:48:04',1,'2023-12-31 23:48:11'),('Booleano',true,'Verdadero o Falso, lógica.','Tipo de Datos',1,'2023-08-29 10:26:38',NULL,'2023-10-05 11:06:11'),('dat_gen_consulta_datos',true,'Datos','Pantallas',1,'2023-09-05 21:40:08',1,'2024-03-18 02:23:20'),('dat_gen_consulta_grupos',true,'Grupos','Pantallas',1,'2023-09-09 14:22:59',1,'2024-03-18 02:23:33'),('dat_gen_principal',true,'Datos Gnr.','Menu',1,'2023-08-29 10:39:57',1,'2024-03-18 02:23:04'),('dat_gen_registro_datos',true,'Registro','Pantallas',1,'2023-09-09 13:19:10',1,'2024-03-18 02:25:00'),('dat_gen_registro_grupos',true,'Registro','Pantallas',1,'2023-09-09 14:23:14',1,'2024-03-18 02:25:11'),('Decimal',true,'Representa los números decimales en el sistema.','Tipo de Datos',1,'2023-08-29 10:25:32',NULL,'2023-10-05 11:06:11'),('Entero',true,'Tipo de dato que representa un integer.','Tipo de Datos',1,'2023-08-29 10:24:59',NULL,'2023-10-05 11:06:11'),('Femenino',true,'Sexo Femenino','Sexo',1,'2023-09-23 15:25:20',NULL,'2023-10-05 11:06:11'),('Masculino',true,'Sexo masculino','Sexo',1,'2023-09-23 15:25:08',NULL,'2023-10-05 11:06:11'),('N/A',true,'No aplica.','Tipo de Datos',1,'2023-08-29 10:40:51',NULL,'2023-10-05 11:06:11'),('Negro',true,'Color del todo y de la nada.','Colores',1,'2023-10-19 11:03:51',NULL,'2023-10-19 11:03:51'),('O-',true,'Sangre O Negativo','Tipos Sanguineos',1,'2023-09-23 16:15:29',1,'2023-10-05 11:07:03'),('O+',true,'Sangre O Positivo','Tipos Sanguineos',1,'2023-09-23 16:15:23',NULL,'2023-10-05 11:06:11'),('Rojo',true,'Color Primario','Colores',1,'2023-08-28 22:39:16',NULL,'2023-10-05 11:06:11'),('sys_configuracion',true,'Configuración','Menu',1,'2024-03-17 15:15:53',1,'2024-03-18 02:22:33'),('sys_principal',true,'Sistemas','Modulo',1,'2024-03-04 12:05:13',1,'2024-03-18 02:23:44'),('Texto',true,'Texto, Varchar, Text','Tipo de Datos',1,'2023-08-29 10:25:54',NULL,'2023-10-05 11:06:11'),('usr_mgr_principal',true,'Usuarios','Menu',1,'2023-09-15 13:00:06',1,'2024-03-18 02:22:47'),('usr_mgr_registro',true,'Registro + Permisos','Pantallas',1,'2023-09-22 14:51:07',1,'2024-03-18 02:25:27');

ALTER TABLE public.gnr_dat 
ENABLE TRIGGER ALL;


ALTER TABLE public.acc 
DISABLE TRIGGER ALL;

INSERT INTO public.acc(id,act,tipo_dato_dat,pantalla_dat,fat_scr) VALUES (1,true,'Booleano','dat_gen_principal','sys_principal'),(6,true,'Booleano','dat_gen_consulta_datos','dat_gen_principal'),(8,true,'Booleano','dat_gen_registro_datos','dat_gen_consulta_datos'),(10,true,'Booleano','dat_gen_consulta_grupos','dat_gen_principal'),(11,true,'Booleano','dat_gen_registro_grupos','dat_gen_consulta_grupos'),(12,true,'Booleano','usr_mgr_principal','sys_principal'),(13,true,'Booleano','usr_mgr_registro','usr_mgr_principal'),(15,true,'Booleano','sys_principal',NULL),(16,true,'Booleano','sys_configuracion','sys_principal');

ALTER TABLE public.acc 
ENABLE TRIGGER ALL;


ALTER TABLE public.ppl_inf 
DISABLE TRIGGER ALL;

INSERT INTO ppl_inf(id,nam,apl,ced,sexo_dat,num_cel,hecho_por_id,mde_at,actualizado_por_id,upd_at,emg_tel,emg_nam,dir,brt_at,tipo_sangre_dat,nic) VALUES (2,'Carlos Isaac','Abreu Pérez','402-1017327-0','Masculino','809-958-6826',1,'2023-09-23 15:25:30',1,'2024-03-04 04:18:08','809-913-3633','Yocasta Pérez','Los ciruelitos calle 1 con 14 diamela... excelente!!!','2001-10-30','AB-',''),(3,'Esther','Abreu Pérez','402-1017321-1','Femenino','809-958-6826',1,'2023-09-23 15:28:22',1,'2024-03-17 15:06:02','809-913-3633','Yocasta Pérez','Pekin','2005-01-13','O+',''),(5,'CArlos','Abreu','402-1017327-1','Femenino','809-958-6826',1,'2024-02-01 00:07:25',1,'2024-03-18 00:20:48','','','Los alamos','2004-01-31','AB-','NEwton');

ALTER TABLE public.ppl_inf 
ENABLE TRIGGER ALL;


ALTER TABLE public.usr
DISABLE TRIGGER ALL;

INSERT INTO public.usr(id,act,pwd,mail,usr,hecho_por_id,mde_at,actualizado_por_id,upd_at,persona_id,pwd_chg) VALUES (1,true,'$2a$10$FD.HVab6z8H3Tba.hw.SvukdeJDfZ5aIIzCN87AL7T2SSAJqoi8Bq','carlosiabreup@gmail.com','newton30',1,'2023-09-20 11:02:18',1,'2024-03-04 04:18:08',2,false),(2,true,'$2a$10$eu4bYQCJFUXSmKiIEu3/COg6.hX8TYOEgY9foRYi2BxP6idl5qMw.','carlosiap30@gmail.com','esther',1,'2023-09-20 11:02:18',1,'2024-03-17 15:06:02',3,false),(4,true,'$2a$10$kOUT0mBy17tLYSd6ucIZAeBl71bBBl6tT7L4kc1ij5ahgA2Egb.Uy','qqqqq@gmail.com','newton300',1,'2024-02-01 00:07:26',1,'2024-03-18 00:20:48',5,false);

ALTER TABLE public.usr
ENABLE TRIGGER ALL;


ALTER TABLE public.usr_acc
DISABLE TRIGGER ALL;

INSERT INTO public.usr_acc(id,acceso_id,act,usuario_id,val) VALUES (1,1,true,1,'true'),(2,6,true,1,'true'),(3,8,true,1,'true'),(4,10,true,1,'true'),(5,12,true,1,'true'),(9,13,true,1,'true'),(10,1,true,2,'true'),(11,6,true,2,'true'),(12,8,true,2,'true'),(13,10,true,2,'true'),(14,12,true,2,'false'),(15,13,true,2,'false'),(16,15,true,1,'true'),(17,15,true,2,'true'),(18,11,true,2,'true'),(19,11,true,1,'true'),(20,16,true,1,'true');

ALTER TABLE public.usr_acc
ENABLE TRIGGER ALL;


phase 4 enjoy!



