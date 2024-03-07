package com.ccsw.dashboard.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.profile.model.Profile;

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
		return jdbcTemplate.query("select dfi.vc_Profile_Rol_L1 as role_level_1,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_AR as role_level_2_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_EM as role_level_2_em,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_SE as role_level_2_se,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_AR as experience_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_EM  as experience_em, \r\n"
				+ "	   dfi.vc_Profile_Sector_Experience as sector_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Cloud_Native_Experience as cloud_native_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Low_Code_Experience as low_code_experience, \r\n"
				+ "	   dfi.vc_Profile_Rol_L3 as tecnico1, \r\n"
				+ "	   dfi.vc_Profile_Rol_L4 as tecnico2 \r\n"
				+ "from dashboard.dm_formdata_import dfi , dashboard.dm_staffing_import dsi\r\n"
				+ "where dfi.num_Import_CodeId = ? and \r\n"
				+ "	  dsi.num_Import_CodeId = ? and\r\n"
				+ "	  dfi.vc_Profile_Rol_L1 = ? and\r\n"
				+ "	  dfi.vc_Profile_SAGA = dsi.vc_Profile_SAGA",
				this::mapRowToInformeRoles,
				idVersionCapacidades,idVersionStaffing,profileId);
	}
	
	public Collection<Profile> generateConutersSummaryByAll(int idVersionCapacidades, int idVersionStaffing) {
		return jdbcTemplate.query("select dfi.vc_Profile_Rol_L1 as role_level_1,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_AR as role_level_2_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_EM as role_level_2_em,\r\n"
				+ "	   dfi.vc_Profile_Rol_L2_SE as role_level_2_se,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_AR as experience_ar,\r\n"
				+ "	   dfi.vc_Profile_Rol_Experience_EM  as experience_em, \r\n"
				+ "	   dfi.vc_Profile_Sector_Experience as sector_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Cloud_Native_Experience as cloud_native_experience, \r\n"
				+ "	   dfi.vc_Profile_Skill_Low_Code_Experience as low_code_experience, \r\n"
				+ "	   dfi.vc_Profile_Rol_L3 as tecnico1, \r\n"
				+ "	   dfi.vc_Profile_Rol_L4 as tecnico2 \r\n"
				+ "from dashboard.dm_formdata_import dfi , dashboard.dm_staffing_import dsi\r\n"
				+ "where dfi.num_Import_CodeId = ? and \r\n"
				+ "	  dsi.num_Import_CodeId = ? and\r\n"				
				+ "	  dfi.vc_Profile_SAGA = dsi.vc_Profile_SAGA",
				this::mapRowToInformeRoles,
				idVersionCapacidades,idVersionStaffing);
	}
	
	private Profile mapRowToInformeRoles(ResultSet row,int rowNum) throws SQLException{
		Profile profile = new Profile();	
		profile.setPerfil(StringUtils.isNotBlank(row.getString("role_level_2_ar")) ? row.getString("role_level_2_ar"): 
																					StringUtils.isNotBlank(row.getString("role_level_2_em")) ? row.getString("role_level_2_em") : 
																																				row.getString("role_level_2_se"));
		profile.setActual(row.getString("role_level_1"));
		profile.setExperiencia(StringUtils.isNotBlank(row.getString("experience_ar"))? row.getString("experience_ar") : row.getString("experience_em"));
		profile.setSectorExperiencia(row.getString("sector_experience"));
		StringBuilder tecnico = new StringBuilder(row.getString("tecnico1"));
		profile.setTecnico(tecnico.append(";").append(row.getString("tecnico2")).toString());
		profile.setSkillCloudNative(row.getString("cloud_native_experience"));
		profile.setSkillLowCode(row.getString("low_code_experience"));
		return profile;
	}

}
