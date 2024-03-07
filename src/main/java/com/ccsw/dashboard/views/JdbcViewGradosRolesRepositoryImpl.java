package com.ccsw.dashboard.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.graderole.model.GradeRole;

@Repository
public class JdbcViewGradosRolesRepositoryImpl implements ViewGradosRolesRepository {
	
	private final JdbcTemplate jdbcTemplate;		

	public JdbcViewGradosRolesRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}



	@Override
	public Collection<GradeRole> findAll(int idVersionCapacidades, int idVersionStaffing) {
		return jdbcTemplate.query("select\r\n"
				+ "    fr.id as id,\r\n"
				+ "    sg.vc_Profile_Grado as grado,\r\n"
				+ "    fr.vc_Profile_Rol_L1 as role_level_1,\r\n"
				+ "    fr.vc_Profile_SAGA as saga,\r\n"
				+ "    fr.num_Import_CodeId as idCapacidades,\r\n"
				+ "	   sg.num_Import_CodeId as idStaffing\r\n"
				+ "from\r\n"
				+ "    dashboard.dm_staffing_import sg,\r\n"
				+ "    dashboard.dm_formdata_import fr\r\n"
				+ "where\r\n"
				+ "    fr.num_Import_CodeId = ? and \r\n"
				+ "	sg.num_Import_CodeId = ? and\r\n"
				+ "    sg.vc_Profile_SAGA = fr.vc_Profile_SAGA",
				this::mapRowToGradeRoles,
				idVersionCapacidades,idVersionStaffing);
	}
	
	private GradeRole mapRowToGradeRoles(ResultSet row,int rowNum) throws SQLException{
		GradeRole gradeRole = new GradeRole();
		gradeRole.setId(row.getLong("id"));
		gradeRole.setGrade(row.getString("grado"));
		gradeRole.setRole(row.getString("role_level_1"));
		gradeRole.setIdImportCapacidades(row.getLong("idCapacidades"));
		gradeRole.setIdImportStaffing(row.getLong("idStaffing"));
		return gradeRole;
	}

}
