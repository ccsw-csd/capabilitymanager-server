package com.ccsw.dashboard.roleversion;

import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.exception.MyBadAdviceException;
import com.ccsw.dashboard.roleversion.model.RoleVersion;
import com.ccsw.dashboard.roleversion.model.RoleVersionDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
@Transactional
public class RoleVersionServiceImpl implements RoleVersionService{

    @Autowired
    private RoleVersionRepository RoleVersionRepository;
    
    @Override
    public List<RoleVersion> findAll() {
        return (List<RoleVersion>) this.RoleVersionRepository.findAll().stream().sorted().toList();
    }
        
    @Override
    public RoleVersion findById(Long id) {
        return this.RoleVersionRepository.findById(id).orElse(null);
    }

	@Override
	public List<String> findYears() {
		List<String> rvList = new ArrayList<String>();
		Map<String, String> rvMap = new HashMap<String, String>();
		List<RoleVersion> listRoleVersion = findAll();
		for (RoleVersion roleVersion : listRoleVersion) {
			String year = String.valueOf(roleVersion.getFechaImportacion().getYear());
			rvMap.putIfAbsent(year, "");			
		}
		
		for (Entry<String, String> entry : rvMap.entrySet()) {
			rvList.add(entry.getKey());			
		}	
		
		return rvList;
	}

	@Override
	public void save(Long id, RoleVersionDto dto) {
		RoleVersion roleVersion;      
        roleVersion = this.findById(id);       
        if (roleVersion == null)
            throw new MyBadAdviceException("roleVersion id doesn't exist");

        BeanUtils.copyProperties(dto, roleVersion, "id");
        //roleVersion.setFechaimportacion(dto.getFechaImportacion());
        this.RoleVersionRepository.save(roleVersion);
		
	}
	
	
    
}
