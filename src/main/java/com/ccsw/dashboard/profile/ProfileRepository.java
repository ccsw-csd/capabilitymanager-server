package com.ccsw.dashboard.profile;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.profile.model.Profile;




@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long>, JpaRepository<Profile, Long> {

	List <Profile> findAllByActual(String actual);
	List <Profile> findAllByActualAndPerfil(String actual, String perfil);

}
