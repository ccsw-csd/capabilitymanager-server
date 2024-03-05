package com.ccsw.dashboard.staffingversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.exception.MyBadAdviceException;
import com.ccsw.dashboard.staffingversion.model.StaffingVersion;
import com.ccsw.dashboard.staffingversion.model.StaffingVersionDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StaffingVersionServiceImpl implements StaffingVersionService{

    @Autowired
    private StaffingVersionRepository StaffingVersionRepository;
    
    @Override
    public List<StaffingVersion> findAll() {
        return (List<StaffingVersion>) this.StaffingVersionRepository.findAll().stream().sorted().toList();
    }
        
    @Override
    public StaffingVersion findById(Long id) {
        return this.StaffingVersionRepository.findById(id).orElse(null);
    }

	@Override
	public List<String> findYears() {
		List<String> rvList = new ArrayList<String>();
		Map<String, String> rvMap = new HashMap<String, String>();
		List<StaffingVersion> listStaffingVersion = findAll();
		for (StaffingVersion staffingVersion : listStaffingVersion) {
			String year = String.valueOf(staffingVersion.getFechaImportacion().getYear());
			rvMap.putIfAbsent(year, "");			
		}
		
		for (Entry<String, String> entry : rvMap.entrySet()) {
			rvList.add(entry.getKey());			
		}	
		
		return rvList;
	}

	@Override
	public void save(Long id, StaffingVersionDto dto) {
		StaffingVersion staffingVersion;      
        staffingVersion = this.findById(id);       
        if (staffingVersion == null)
            throw new MyBadAdviceException("roleVersion id doesn't exist");

        BeanUtils.copyProperties(dto, staffingVersion, "id");
        //roleVersion.setFechaimportacion(dto.getFechaImportacion());
        this.StaffingVersionRepository.save(staffingVersion);
		
	}

	    
}
