package com.ccsw.dashboard.reportversion;

import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.exception.MyBadAdviceException;
import com.ccsw.dashboard.reportversion.model.ReportVersion;
import com.ccsw.dashboard.reportversion.model.ReportVersionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
@Transactional
public class ReportVersionServiceImpl implements ReportVersionService{

    @Autowired
    private ReportVersionRepository reportVersionRepository;
    
    @Override
    public List<ReportVersion> findAll() {
        return (List<ReportVersion>) this.reportVersionRepository.findAll().stream().sorted().toList();
    }
        
    @Override
    public ReportVersion findByIdVersionCapacidades(Long id) {
        return this.reportVersionRepository.findByIdVersionCapacidades(id).orElse(null);
    }

    @Override
    public List<ReportVersion> findByScreenshot(String id, String year) {
    	
    	if (year != null) {
        	String str1 = year + "-01-01 00:00";
        	String str2 = year + "-12-31 23:59";
        	
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        	
        	LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
        	LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
            
        	try {
                int d = Integer.parseInt(id);
            	if (d == 0 || d == 1) {
            		return this.reportVersionRepository.findByScreenshotAndFechaImportacionBetween(id, dateTime1, dateTime2);
            	} else {
            		return this.reportVersionRepository.findByFechaImportacionBetween(dateTime1, dateTime2);
            	}
            } catch (NumberFormatException nfe) {
        		return this.reportVersionRepository.findByFechaImportacionBetween(dateTime1, dateTime2);
            }
    	} else {
    		return this.reportVersionRepository.findByScreenshot(id);
    	}

    }
    
	@Override
	public List<String> findYears(String screenshot) {
		List<String> rvList = new ArrayList<String>();
		Map<String, String> rvMap = new HashMap<String, String>();
		List<ReportVersion> listReportVersion = new ArrayList<ReportVersion>();
		if (screenshot != null) {
			listReportVersion = findByScreenshot(screenshot, null);
		} else {
			listReportVersion = findAll();
		}
		for (ReportVersion reportVersion : listReportVersion) {
			String year = String.valueOf(reportVersion.getFechaImportacion().getYear());
			rvMap.putIfAbsent(year, "");			
		}
		
		for (Entry<String, String> entry : rvMap.entrySet()) {
			rvList.add(entry.getKey());			
		}	
		
		return rvList;
	}

	@Override
	public void save(Long id, ReportVersionDto dto) {
		ReportVersion reportVersion;      
        reportVersion = this.findById(id);       
        if (reportVersion == null && id != 0)
            throw new MyBadAdviceException("reportVersion id doesn't exist");

        if (reportVersion.getScreenshot() != dto.getScreenshot()) {
        	reportVersion.setUsuario(dto.getUsuario());
        	LocalDate ld = LocalDate.now();
            LocalTime lt = LocalTime.now();
        	reportVersion.setFechaModificacion(LocalDateTime.of(ld, lt));
        }
        BeanUtils.copyProperties(dto, reportVersion, "usuario", "fechaModificacion", "idVersionCapacidades", "idVersionStaffing", "fechaImportacion", "descripcion");
        //roleVersion.setFechaimportacion(dto.getFechaImportacion());
        this.reportVersionRepository.save(reportVersion);
		
	}

	@Override
	public ReportVersion findById(Long id) {
		return reportVersionRepository.findById(id).orElse(null);
	}
	    
}
