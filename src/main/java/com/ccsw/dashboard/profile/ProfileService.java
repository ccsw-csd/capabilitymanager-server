package com.ccsw.dashboard.profile;



import java.util.List;

import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileGroup;
import com.ccsw.dashboard.profile.model.ProfileTotal;

public interface ProfileService {

	List<Profile> findAll();
	List<ProfileTotal> findAllProfileTotals(String id);
	List<ProfileGroup> findAllProfile(String id);
	
}
