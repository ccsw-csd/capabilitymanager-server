package com.ccsw.capabilitymanager.common;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Constants {
	public static final String XLS_FILE_EXTENSION = ".xls";
	public static final String XLSX_FILE_EXTENSION = ".xlsx";

	public static final String XLS_FILE_FORMAT = "application/vnd.ms-excel";
	public static final String XLSX_FILE_FORMAT = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static final List<String> ALLOWED_FORMATS = Arrays.asList(XLS_FILE_FORMAT, XLSX_FILE_FORMAT);

	public static final String EMPTY = "";

	public static final int FIRST_SHEET = 0;
	public static final int ROW_EVIDENCE_LIST_START = 1;
	public static final int ROW_EVIDENCE_LIST_NEXT = ROW_EVIDENCE_LIST_START + 1;

	public static final String ROLL1_SE  = "Software Engineer";
	public static final String ROLL1EX_SE  = "Software Engineer (Developers, Tech Leads, Team Leads, QA Tester, Consultor Técnico, DevOps, HOST)";
	public static final String ROLL1_BA  = "Business Analyst";
	public static final String ROLL1EX_BA  = "Business Analyst (Functional Analyst, Product Owner)";
	public static final String ROLL1_EM  = "Engagement Managers";
	public static final String ROLL1EX_EM_PMO = "Engagement Managers (EM, Responsable de proyecto, PMO, Scrum Master)";
	public static final String ROLL1EX_EM = "Engagement Managers (EM, Responsable de proyecto)";
	public static final String ROLL1_AR = "Architects";
	public static final String ROLL1EX_AR = "Architects";

	public static final String ERROR_INIT  = ">>> [ERROR][DataImportServiceImpl] (";
	public static final String ERROR_INIT2  = ") ERROR: ";
	public static final String ERROR_EMPTY_ROL_FILE = " Rols List File is null or empty";
	public static final String ERROR_EMPTY_STAFFING_FILE = " Staffing List File is null or empty";
	public static final String ERROR_DOCUMENT_TYPE = "ERROR: 'documentType' param is not valid (select value 1, 2 or 3)";
	public static final String ERROR_FILE_NOT_FOUND  = ">>> [ERROR] Not Found(";

	public static final Date FUNDATIONDAYLESSONE = new Date(1967, 9, 30);

	//Expor_detalle_Excel__Parametros
	public static final String PARAMETROS  = "Parametros";
	public static final String VERSION  = "Versión";
	public static final String ROLES_FILE  = "Archivo Roles";
	public static final String STAFFING_FILE  = "Archivo Staffing";
	public static final String SCREENSHOOT  = "ScreenShot";
	public static final String DATE_GENERACION  = "Fecha de Generación";
	public static final String DESCRIPTION  = "Descripción";
	public static final String USER  = "Usuario";
	public static final String COMENTS  = "Comentarios";
	public static final String YES ="SI";
	public static final String NO ="NO";
	
	
	
	public static enum RolsDatabasePos {
		COL_SAGA(5),
		COL_EMAIL(3),
		COL_NAME(4),
		COL_ROLL1_EXTENDIDO(6),
		COL_ROLL2_EM(7),
		COL_ROLL2_AR(8),
		COL_ROLL2_AN(9),
		COL_ROLL2_SE(10),
		COL_ROLL3(13),
		COL_ROLL4(16),
		COL_ROL_EXPERIENCE_EM(11),
		COL_ROL_EXPERIENCE_AR(12),
		COL_SKILL_CLOUD_NATIVE_EXPERIENCE(14),
		COL_SKILLL_OWCODE_EXPERIENCE(15),
		COL_SECTOR_EXPERIENCE(17),
		COL_SKILL_CLOUD_EXPERIENCE(18);

		private final int excelPosition;
		RolsDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}

	public static enum StaffingDatabasePos {
		COL_SAGA(1),
		COL_GGID(0),
		COL_CENTRO(10),
		COL_NOMBRE(2),
		COL_APELLIDOS(3),
		COL_LOCALIZACION(65),
		COL_PRACTICA(4),
		COL_GRADO(8),
		COL_CATEGORIA(9),
		COL_PERFIL_TECNICO(36),
		COL_PRIMARY_SKILL(42),
		COL_FECHA_INCORPORACION(32),
		COL_PORCENTAJE_ASIGNACION(11),
		COL_STATUS(12),
		COL_CLIENTE_ACTUAL(15),
		COL_FECHA_INICIO_ASIGNACION(22),
		COL_FECHA_FIN_ASIGNACION(23),
		COL_FECHA_DISPONIBILIDAD(17),
		COL_POSICION_PROYECTO_FUTURO(24),
		COL_COLABORACIONES(63),
		COL_PROYECTO_ANTERIOR(34),
		COL_INGLES_ESCRITO(45),
		COL_INGLES_HABLADO(44),
		COL_JORNADA (46),
		COL_MESES_BENCH(54),
		COL_PRACTICE_AREA(68);


		private final int excelPosition;

		StaffingDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}

	public static enum CertificatesDatabasePos {
		COL_SAGA(14),
		COL_PARTNER(6),
		COL_CERTIFICADO(7),
		COL_NAME_GTD(8),
		COL_CERTIFICATION_GTD(9),
		COL_CODE(10),
		COL_SECTOR(11),
		COL_MODULO(12),
		COL_ID_CANDIDATO(19),
		COL_FECHA_CERTIFICADO(15),
		COL_FECHA_EXPIRACION(16),
		COL_ACTIVO(17),
		COL_GGID(18),
		COL_ANEXO(19),
		COL_COMENTARIO_ANEXO(20),
		COL_ROL_FORMULARIO_EM(21),
		COL_ROL_FORMULARIO_ARCHITECT(22),
		COL_ROL_FORMULARIO_BA(23);

		private final int excelPosition;
		CertificatesDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}
	
	
	public static enum ItinerariosDatabasePos {
		COL_GGID(0),
		COL_FIRST_NAME(1),
		COL_LAST_NAME(2),
		COL_EMAIL_ID(3),
		COL_GLOBAL_GRADE(4),
		COL_COUNTRY(5),
		COL_SBU(6),
		COL_BU(7),
		COL_PATHWAY_ID(8),
		COL_PATHWAY_TITLE(9),
		COL_TOTAL_PATH(10),
		COL_COMPLETED_CONTENT(11),
		COL_COMPLETION_PERCENT(12),
		COL_ENROLLMENT_DATE(13),
		COL_RECENT_ACTIVITY(14),
		COL_COMPLETED_DATE(15);

		private final int excelPosition;
		ItinerariosDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}

}
