package com.ccsw.dashboard.profile;



import java.util.List;

import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileGroup;
import com.ccsw.dashboard.profile.model.ProfileTotal;
import com.ccsw.dashboard.profile.model.ProfileTotalsCombined;

public interface ProfileService {

	List<Profile> findAll(int idImport);
	List<ProfileTotal> findAllProfileTotals(String id, int idImport);
	ProfileTotalsCombined findAllProfileTotalsTogether(List<String> id, int idImport);
	List<ProfileGroup> findAllProfile(String id, int idImport);	
	
}
