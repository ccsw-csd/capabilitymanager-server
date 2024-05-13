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
		return jdbcTemplate.query("select dsi.vc_Profile_GGID as ggid,\r\n" +
				"	dsi.vc_Profile_SAGA as saga,\r\n" +
				"	dsi.vc_Profile_Practica  as practica,\r\n" +
				"	dsi.vc_Profile_Grado as grado,\r\n" +
				"	dsi.vc_Profile_Categoria as categoria,\r\n" +
				"	dsi.vc_Profile_Centro as centro,\r\n" +
				"	concat(dsi.vc_Profile_Apellidos , ', ', dsi.vc_Profile_Nombre) as nombreCompleto,\r\n" +
				"	dfi.vc_Profile_Email as email,\r\n" +
				"	dsi.vc_Profile_Localizacion as localizacion,\r\n" +
				"	dsi.vc_Profile_Status as status,\r\n" +
				"   dsi.vc_Profile_Perfil_Tecnico as perfilStaffing,"
				+ "	   dfi.vc_Profile_Rol_L1 as role_level_1,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_AR as role_level_2_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_EM as role_level_2_em,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_SE as role_level_2_se,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_AR as experience_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_EM  as experience_em, \r\n"
				+ "	   dfi.vc_Profile_Sector_Experience as sector_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Cloud_Native_Experience as cloud_native_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Low_Code_Experience as low_code_experience, \r\n"
				+ "	   dfi.vc_Profile_Rol_L3 as tecnicoL3, dfi.vc_Profile_Rol_L4 as tecnicoL4 \r\n"
				+ "from dm_formdata_import dfi , dm_staffing_import dsi\r\n"
				+ "where dfi.num_Import_CodeId = ? and \r\n"
				+ "	  dsi.num_Import_CodeId = ? and\r\n"
				+ "	  dfi.vc_Profile_Rol_L1 = ? and\r\n"
				+ "	  dfi.vc_Profile_SAGA = dsi.vc_Profile_SAGA",
				this::mapRowToInformeRoles,
				idVersionCapacidades,idVersionStaffing,profileId);
	}

	@Override
	public Collection<Profile> generateConutersSummaryByAll(int idVersionCapacidades, int idVersionStaffing) {
		return jdbcTemplate.query("select dsi.vc_Profile_GGID as ggid,\r\n" +
				"	dsi.vc_Profile_SAGA as saga,\r\n" +
				"	dsi.vc_Profile_Practica  as practica,\r\n" +
				"	dsi.vc_Profile_Grado as grado,\r\n" +
				"	dsi.vc_Profile_Categoria as categoria,\r\n" +
				"	dsi.vc_Profile_Centro as centro,\r\n" +
				"	concat(dsi.vc_Profile_Apellidos , ', ', dsi.vc_Profile_Nombre) as nombreCompleto,\r\n" +
				"	dfi.vc_Profile_Email as email,\r\n" +
				"	dsi.vc_Profile_Localizacion as localizacion,\r\n" +
				"	dsi.vc_Profile_Status as status,\r\n" +
				"   dsi.vc_Profile_Perfil_Tecnico as perfilStaffing,"
				+ "	dfi.vc_Profile_Rol_L1 as role_level_1,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_AR as role_level_2_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_EM as role_level_2_em,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_SE as role_level_2_se,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_AR as experience_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_EM  as experience_em, \r\n"
				+ "	   dfi.vc_Profile_Sector_Experience as sector_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Cloud_Native_Experience as cloud_native_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Low_Code_Experience as low_code_experience, \r\n"
				+ "	   dfi.vc_Profile_Rol_L3 as tecnicoL3, \r\n" + "	   dfi.vc_Profile_Rol_L4 as tecnicoL4 \r\n"
				+ "from dm_formdata_import dfi , dm_staffing_import dsi\r\n"
				+ "where dfi.num_Import_CodeId = ? and \r\n"
				+ "	  dsi.num_Import_CodeId = ? and\r\n"
				+ "	  dfi.vc_Profile_SAGA = dsi.vc_Profile_SAGA",
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
		profile.setTecnicoSolution(row.getString("tecnicoL3"));
		profile.setTecnicoIntegration(row.getString("tecnicoL4"));
		profile.setSkillCloudNative(row.getString("cloud_native_experience"));
		profile.setSkillLowCode(row.getString("low_code_experience"));
		return profile;
	}

}
