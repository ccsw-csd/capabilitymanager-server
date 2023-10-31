package com.ccsw.dashboard.profile;



import java.util.List;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.graderole.model.GradeRole;
import com.ccsw.dashboard.graderole.model.GradeRoleTotal;
import com.ccsw.dashboard.graderole.model.GradeTotal;
import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileTotal;

public interface ProfileService {

	List<Profile> findAll();
	List<ProfileTotal> findAllProfileTotals(String id);
	
}
