package com.ccsw.dashboard.reportversion;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ccsw.dashboard.reportversion.model.ReportVersion;
import com.ccsw.dashboard.reportversion.model.ReportVersionDto;
import com.ccsw.dashboard.roleversion.RoleVersionService;
import com.ccsw.dashboard.roleversion.model.RoleVersion;
import com.ccsw.dashboard.roleversion.model.RoleVersionDto;
import com.ccsw.dashboard.staffingversion.StaffingVersionService;
import com.ccsw.dashboard.staffingversion.model.StaffingVersion;
import com.ccsw.dashboard.staffingversion.model.StaffingVersionDto;

@RequestMapping(value = "/reportimports")
@RestController
public class ReportVersionController {

    @Autowired
    private ReportVersionService reportVersionService;
    
    @Autowired
    private StaffingVersionService staffingVersionService;
    
    @Autowired
    private RoleVersionService roleVersionService;
    
    @Autowired
    DozerBeanMapper mapper;

    @GetMapping("/all")
    public List<ReportVersionDto> findAll(){        
        return this.reportVersionService.findAll().stream()
        		.map(rv-> { 
				ReportVersionDto rvdto = new ReportVersionDto();	    			
	            RoleVersion roleVersion = roleVersionService.findById(Long.valueOf(rv.getIdVersionCapacidades()));
				rvdto.setRoleVersion(roleVersion == null ? null : new RoleVersionDto(roleVersion.getId(), 
																  											   roleVersion.getIdTipoInterfaz(),
																  											   roleVersion.getFechaImportacion(),
																  											   roleVersion.getNumRegistros(),
																  											   roleVersion.getNombreFichero(),
																  											   roleVersion.getDescripcion(),
																  											   roleVersion.getUsuario()));
				StaffingVersion staffingVersion = staffingVersionService.findById(Long.valueOf(rv.getIdVersionStaffing()));
				rvdto.setStaffingVersion(staffingVersion == null ? null : new StaffingVersionDto(staffingVersion.getId(),
																  									staffingVersion.getIdTipoInterfaz(),
																  									staffingVersion.getFechaImportacion(),
																  									staffingVersion.getNumRegistros(),
																  									staffingVersion.getNombreFichero(),
																  									staffingVersion.getDescripcion(),
																  									staffingVersion.getUsuario()));  
				rvdto.setId(rv.getId());
				rvdto.setUsuario(rv.getUsuario());
				rvdto.setDescripcion(rv.getDescripcion());
				rvdto.setScreenshot(rv.getScreenshot());
				rvdto.setComentarios(rv.getComentarios());
				rvdto.setFechaImportacion(rv.getFechaImportacion());
				rvdto.setFechaModificacion(rv.getFechaModificacion());
				return rvdto;
			})
			.toList(); 
    }
    
    @GetMapping("/all/{year}")
    public List<ReportVersionDto> findAllYear(@PathVariable String year){       
    	return this.reportVersionService.findAll().stream().filter(rv->String.valueOf(rv.getFechaImportacion().getYear()).equals(year))
    			//.map(rv->mapper.map(rv, ReportVersionDto.class))
    			.map(rv-> { 
	    			ReportVersionDto rvdto = new ReportVersionDto();	    			
                    RoleVersion roleVersion = roleVersionService.findById(Long.valueOf(rv.getIdVersionCapacidades()));
	    			rvdto.setRoleVersion(roleVersion == null ? null : new RoleVersionDto(roleVersion.getId(), 
	    															  											   roleVersion.getIdTipoInterfaz(),
	    															  											   roleVersion.getFechaImportacion(),
	    															  											   roleVersion.getNumRegistros(),
	    															  											   roleVersion.getNombreFichero(),
	    															  											   roleVersion.getDescripcion(),
	    															  											   roleVersion.getUsuario()));
	    			StaffingVersion staffingVersion = staffingVersionService.findById(Long.valueOf(rv.getIdVersionStaffing()));
	    			rvdto.setStaffingVersion(staffingVersion == null ? null : new StaffingVersionDto(staffingVersion.getId(),
	    															  									staffingVersion.getIdTipoInterfaz(),
	    															  									staffingVersion.getFechaImportacion(),
	    															  									staffingVersion.getNumRegistros(),
	    															  									staffingVersion.getNombreFichero(),
	    															  									staffingVersion.getDescripcion(),
	    															  									staffingVersion.getUsuario()));  
	    			rvdto.setId(rv.getId());
	    			rvdto.setUsuario(rv.getUsuario());
	    			rvdto.setDescripcion(rv.getDescripcion());
	    			rvdto.setScreenshot(rv.getScreenshot());
	    			rvdto.setComentarios(rv.getComentarios());
	    			rvdto.setFechaImportacion(rv.getFechaImportacion());
	    			rvdto.setFechaModificacion(rv.getFechaModificacion());
	    			return rvdto;
    			})
    			.toList();
    }
    
    @GetMapping("/{id}")
    public ReportVersion findById(@PathVariable String id){
        return this.reportVersionService.findByIdVersionCapacidades(Long.valueOf(id));     
    }
    
    @GetMapping("/screenshot/{screenshot}")
    public List<ReportVersion> findByScreenshotNum(@PathVariable(name = "screenshot", required = true) String screenshot, @RequestParam(value = "year", required = false) String year){
        return this.reportVersionService.findByScreenshot(screenshot, year);     
    }
    
    @GetMapping("/years")
    public List<String> findYears(@RequestParam(name = "screenshot", required = false) String screenshot){
       return this.reportVersionService.findYears(screenshot);
    }
    
    @PutMapping({ "/{id}" })
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ReportVersionDto dto) {
        this.reportVersionService.save(id, dto);
    }
}
