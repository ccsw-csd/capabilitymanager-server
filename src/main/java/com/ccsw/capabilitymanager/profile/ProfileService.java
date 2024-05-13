package com.ccsw.capabilitymanager.profile;



import java.util.List;

import com.ccsw.capabilitymanager.profile.model.Profile;
import com.ccsw.capabilitymanager.profile.model.ProfileGroup;
import com.ccsw.capabilitymanager.profile.model.ProfileTotal;
import com.ccsw.capabilitymanager.profile.model.InformeRoles;

public interface ProfileService {

	List<Profile> findAll(int idImport);
	List<ProfileTotal> findAllProfileTotals(String id, int idImport);
	InformeRoles findAllInformeRoles(int idImport);
	List<ProfileGroup> findAllProfile(String id, int idImport);	
	
}
