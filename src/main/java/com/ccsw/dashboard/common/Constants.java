package com.ccsw.dashboard.common;

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

	public static final String VCPROFILEROLL1_SE  = "Software Engineer";
	public static final String VCPROFILEROLL1EX_SE  = "Software Engineer (Developers, Tech Leads, Team Leads, QA Tester, Consultor Técnico, DevOps, HOST)";
	public static final String VCPROFILEROLL1_BA  = "Business Analyst";
	public static final String VCPROFILEROLL1EX_BA  = "Business Analyst (Functional Analyst, Product Owner)";
	public static final String VCPROFILEROLL1_EM  = "Engagement Managers";
	public static final String VCPROFILEROLL1EX_EM_PMO = "Engagement Managers (EM, Responsable de proyecto, PMO, Scrum Master)";
	public static final String VCPROFILEROLL1EX_EM = "Engagement Managers (EM, Responsable de proyecto)";
	public static final String VCPROFILEROLL1_AR = "Architects";
	public static final String VCPROFILEROLL1EX_AR = "Architects";

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
		COL_VCPROFILEEMAIL(3),
		COL_VCPROFILENAME(4),
		COL_VCPROFILESAGA(5),
		COL_VCPROFILEROLL1EXTENDIDO(6),
		COL_VCPROFILEROLL2EM(7),
		COL_VCPROFILEROLL2AR(8),
		COL_VCPROFILEROLL2AN(9),
		COL_VCPROFILEROLL2SE(10),
		COL_VCPROFILEROLEXPERIENCEEM(11),
		COL_VCPROFILEROLEXPERIENCEAR(12),
		COL_VCPROFILEROLL3(13),
		COL_VCPROFILESKILLCLOUDNATIVEEXPERIENCE(14),
		COL_VCPROFILESKILLLOWCODEEXPERIENCE(15),
		COL_VCPROFILEROLL4(16),
		COL_VCPROFILESECTOREXPERIENCE(17),
		COL_VCPROFILESKILLCLOUDEXP(18);

		private final int excelPosition;
		RolsDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}

	public static enum StaffingDatabasePos {
		COL_VCPROFILESAGA(1),
		COL_VCPROFILEGGID(0),
		COL_VCPROFILENOMBRE(2),
		COL_VCPROFILEAPELLIDOS(3),
		COL_VCPROFILECATEGORIA(9),
		COL_VCPROFILEPRACTICA(4),
		COL_VCPROFILEGRADO(8),
		COL_VCPROFILECENTRO(10),
		COL_VCPROFILELOCALIZACION(65),
		COL_VCPROFILEPERFILTECNICO(36),
		COL_VCPROFILEFECHAINCORPORACION(32),
		COL_VCPROFILEPORCENTAJEASIGNACION(11),
		COL_VCPROFILESTATUS(12),
		COL_VCPROFILECLIENTEACTUAL(15),
		COL_VCPROFILEFECHAINICIOASIGNACION(22),
		COL_VCPROFILEFECHAFINASIGNACION(23),
		COL_VCPROFILEFECHADISPONIBILIDAD(17),
		COL_VCPROFILEPOSICIONPROYECTOFUTURO(24),
		COL_VCPROFILECOLABORACIONES(63),
		COLVCPROFILEPROYECTOANTERIOR(34),
		COL_VCPROFILEMESESBENCH(54);


		private final int excelPosition;

		StaffingDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}

	public static enum CertificatesDatabasePos {
		COL_VCSAGA(14),
		COL_VCPARTNER(6),
		COL_VCCERTIFICADO(7),
		COL_VCNAMEGTD(8),
		COL_VCCERTIFICATIONGTD(9),
		COL_VCCODE(10),
		COL_VCSECTOR(11),
		COL_VCMODULO(12),
		COL_VCIDCANDIDATO(19),
		COL_VCFECHACERTIFICADO(15),
		COL_VCFECHAEXPIRACION(16),
		COL_VCACTIVO(17),
		COL_VCANEXO(19),
		COL_VCCOMENTARIOANEXO(20);

		private final int excelPosition;
		CertificatesDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}

}
