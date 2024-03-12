package com.ccsw.dashboard.common;

import java.util.Arrays;
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
	
	public static final String VCPROFILEROLL1_OP1  = "Software Engineer";
	public static final String VCPROFILEROLL1EX_OP1  = "Software Engineer (Developers, Tech Leads, Team Leads, QA Tester, Consultor Técnico, DevOps, HOST)";
	public static final String VCPROFILEROLL1_OP2  = "Business Analyst";
	public static final String VCPROFILEROLL1EX_OP2  = "Business Analyst (Functional Analyst, Product Owner)";
	public static final String VCPROFILEROLL1_OP3  = "Engagement Manager";
	public static final String VCPROFILEROLL1EX_OP3  = "Engagement Managers (EM, Responsable de proyecto, PMO, Scrum Master)";
	public static final String VCPROFILEROLL1_OP4  = "Architects";
	public static final String VCPROFILEROLL1EX_OP4  = "Architects";

	public static final String ERROR_INIT  = ">>> [ERROR][DataImportServiceImpl] (";
	public static final String ERROR_INIT2  = ") ERROR: ";
	public static final String ERROR_EMPTY_ROL_FILE = " Rols List File is null or empty";
	public static final String ERROR_EMPTY_STAFFING_FILE = " Staffing List File is null or empty";
	public static final String ERROR_DOCUMENT_TYPE = "ERROR: 'documentType' param is not valid (select value 1, 2 or 3)";
	public static final String ERROR_FILE_NOT_FOUND  = ">>> [ERROR] Not Found(";

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
		COL_VCPROFILESAGA(0),
		COL_VCPROFILEGGID(1),
		COL_VCPROFILENOMBRE(2),
		COL_VCPROFILEAPELLIDOS(3),
		COL_VCPROFILECATEGORIA(4),
		COL_VCPROFILEPRACTICA(5),
		COL_VCPROFILEGRADO(6),
		COL_VCPROFILECENTRO(7),
		COL_VCPROFILELOCALIZACION(8),
		COL_VCPROFILEPERFILTECNICO(9),
		COL_VCPROFILESTATUS(15);

		private final int excelPosition;

		StaffingDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}
//TODO: Add Enum CertificatesDatabasePos 
	
}
