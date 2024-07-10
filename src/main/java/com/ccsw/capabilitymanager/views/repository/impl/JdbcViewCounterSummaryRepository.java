package com.ccsw.capabilitymanager.views.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.profile.model.Profile;
import com.ccsw.capabilitymanager.views.repository.ViewCounterSummaryRepository;

import io.micrometer.common.util.StringUtils;

@Repository
public class JdbcViewCounterSummaryRepository implements ViewCounterSummaryRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcViewCounterSummaryRepository(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<Profile> generateConutersSummaryByRole(int idVersionCapacidades, int idVersionStaffing, String profileId) {
		return jdbcTemplate.query("select dsi.GGID as ggid,\r\n" +
				"	dsi.SAGA as saga,\r\n" +
				"	dsi.practica  as practica,\r\n" +
				"	dsi.grado as grado,\r\n" +
				"	dsi.categoria as categoria,\r\n" +
				"	dsi.centro as centro,\r\n" +
				"	concat(dsi.apellidos , ', ', dsi.nombre) as nombreCompleto,\r\n" +
				"	dfi.email as email,\r\n" +
				"	dsi.localizacion as localizacion,\r\n" +
				"	dsi.status as status,\r\n" +
				"   dsi.perfil_tecnico as perfilStaffing,"
				+ "	   dfi.rol_L1 as role_level_1,\r\n"
				+ "	   dfi.rol_L2_AR as role_level_2_ar,\r\n"
				+ "	   dfi.rol_L2_EM as role_level_2_em,\r\n"
				+ "	   dfi.rol_L2_SE as role_level_2_se,\r\n"
				+ "	   dfi.rol_experience_AR as experience_ar,\r\n"
				+ "	   dfi.rol_experience_EM  as experience_em, \r\n"
				+ "	   dfi.sector_experience as sector_experience, \r\n"
				+ "	   dfi.skill_cloud_native_experience as cloud_native_experience, \r\n"
				+ "	   dfi.skill_low_code_Experience as low_code_experience, \r\n"
				+ "	   dfi.rol_L3 as tecnicoL3, dfi.rol_L4 as tecnicoL4 \r\n"
				+ "from formdata dfi , staffing dsi\r\n"
				+ "where dfi.num_import_code_id = ? and \r\n"
				+ "	  dsi.num_import_code_id = ? and\r\n"
				+ "	  dfi.rol_L1 = ? and\r\n"
				+ "	  dfi.SAGA = dsi.SAGA",
				this::mapRowToInformeRoles,
				idVersionCapacidades,idVersionStaffing,profileId);
	}

	@Override
	public Collection<Profile> generateConutersSummaryByAll(int idVersionCapacidades, int idVersionStaffing) {
		return jdbcTemplate.query("select dsi.GGID as ggid,\r\n" +
				"	dsi.SAGA as saga,\r\n" +
				"	dsi.practica  as practica,\r\n" +
				"	dsi.grado as grado,\r\n" +
				"	dsi.categoria as categoria,\r\n" +
				"	dsi.centro as centro,\r\n" +
				"	concat(dsi.apellidos , ', ', dsi.nombre) as nombreCompleto,\r\n" +
				"	dfi.email as email,\r\n" +
				"	dsi.localizacion as localizacion,\r\n" +
				"	dsi.status as status,\r\n" +
				"   dsi.perfil_tecnico as perfilStaffing,"
				+ "	   dfi.rol_L1 as role_level_1,\r\n"
				+ "	   dfi.rol_L2_AR as role_level_2_ar,\r\n"
				+ "	   dfi.rol_L2_EM as role_level_2_em,\r\n"
				+ "	   dfi.rol_L2_SE as role_level_2_se,\r\n"
				+ "	   dfi.rol_experience_AR as experience_ar,\r\n"
				+ "	   dfi.rol_experience_EM  as experience_em, \r\n"
				+ "	   dfi.sector_experience as sector_experience, \r\n"
				+ "	   dfi.skill_cloud_native_experience as cloud_native_experience, \r\n"
				+ "	   dfi.skill_low_code_Experience as low_code_experience, \r\n"
				+ "	   dfi.rol_L3 as tecnicoL3, dfi.rol_L4 as tecnicoL4 \r\n"
				+ "from formdata dfi , staffing dsi\r\n"
				+ "where dfi.num_import_code_id = ? and \r\n"
				+ "	  dsi.num_import_code_id = ? and\r\n"
				+ "	  dfi.SAGA = dsi.SAGA",
				this::mapRowToInformeRoles,
				idVersionCapacidades,idVersionStaffing);
	}
	
	

	private Profile mapRowToInformeRoles(ResultSet row,int rowNum) throws SQLException{
		Profile profile = new Profile();
		profile.setSaga(row.getString("saga"));
		profile.setGgid(row.getString("ggid"));
		profile.setPractica(row.getString("practica"));
		profile.setGrado(row.getString("grado"));
		profile.setCategoria(row.getString("categoria"));
		profile.setCentro(row.getString("centro"));
		profile.setNombre(row.getString("nombreCompleto"));
		profile.setEmail(row.getString("email"));
		profile.setLocalizacion(row.getString("localizacion"));
		profile.setStatus(row.getString("status"));
		profile.setPerfilStaffing(row.getString("perfilStaffing"));
		profile.setPerfil(StringUtils.isNotBlank(row.getString("role_level_2_ar")) ? row.getString("role_level_2_ar")
				: StringUtils.isNotBlank(row.getString("role_level_2_em")) ? row.getString("role_level_2_em") : row.getString("role_level_2_se"));
		profile.setActual(row.getString("role_level_1"));
		profile.setExperiencia(StringUtils.isNotBlank(row.getString("experience_ar"))? row.getString("experience_ar") : row.getString("experience_em"));
		profile.setSectorExperiencia(row.getString("sector_experience"));
		profile.setCertificaciones(("certifica"));
		profile.setTecnicoSolution(row.getString("tecnicoL3"));
		profile.setTecnicoIntegration(row.getString("tecnicoL4"));
		profile.setSkillCloudNative(row.getString("cloud_native_experience"));
		profile.setSkillLowCode(row.getString("low_code_experience"));
		return profile;
	}

}
