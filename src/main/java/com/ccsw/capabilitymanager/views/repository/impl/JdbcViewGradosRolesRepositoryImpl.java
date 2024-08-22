package com.ccsw.capabilitymanager.views.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.views.repository.ViewGradosRolesRepository;

@Repository
public class JdbcViewGradosRolesRepositoryImpl implements ViewGradosRolesRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcViewGradosRolesRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * Retrieves a collection of GradeRole objects based on the provided version IDs.
	 * 
	 * @param idVersionCapacidades the ID of the version from the capacidades table.
	 * @param idVersionStaffing the ID of the version from the staffing table.
	 * @return a collection of GradeRole objects matching the provided IDs.
	 */
	@Override
	public Collection<GradeRole> findAll(int idVersionCapacidades, int idVersionStaffing) {
		return jdbcTemplate.query("select\r\n"
				+ "    fr.id as id,\r\n"
				+ "    sg.grado as grado,\r\n"
				+ "    fr.rol_L1 as role_level_1,\r\n"
				+ "    fr.SAGA as saga,\r\n"
				+ "    fr.num_import_code_id as idCapacidades,\r\n"
				+ "	   sg.num_import_code_id as idStaffing\r\n"
				+ "from staffing sg, formdata fr\r\n"
				+ "where\r\n"
				+ "    fr.num_import_code_id = ? and \r\n"
				+ "	   sg.num_import_code_id = ? and\r\n"
				+ "    (fr.rol_L2_EM like '%" + Constants.ROLL1_EM + "%' or fr.rol_L2_EM = '') and\r\n"
				+ "    sg.SAGA = fr.SAGA",
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
