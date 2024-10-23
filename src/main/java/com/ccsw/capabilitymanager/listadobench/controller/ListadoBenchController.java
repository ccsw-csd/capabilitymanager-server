package com.ccsw.capabilitymanager.listadobench.controller;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.service.ListadoBenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listadobench")
public class ListadoBenchController {

    @Autowired
    private ListadoBenchService service;

    /**
     * Retrieves a list of all staffing entries.
     *
     * <p>This method handles HTTP GET requests to the root path of the controller. It returns a list of {@link ListadoBench}
     * objects representing the staffing entries obtained from the service layer.</p>
     *
     * @return A {@link List} of {@link ListadoBench} objects containing all staffing entries.
     */
    @GetMapping
    public List<ListadoBench> getAllStaffing() {
        return service.getListadoPersonasBench();
    }

    /**
     * Retrieves a list of staffing entries filtered by a specific saga.
     *
     * <p>This method handles HTTP GET requests to the path with a saga parameter. It returns an {@link Optional} containing
     * a list of {@link ListadoBench} objects that match the specified saga. If no entries are found for the given saga, the
     * {@link Optional} will be empty.</p>
     *
     * @param saga The saga identifier used to filter the staffing entries.
     * @return An {@link Optional} containing a {@link List} of {@link ListadoBench} objects matching the specified saga,
     *         or an empty {@link Optional} if no entries are found.
     */
    @GetMapping("/{saga}")
    public Optional<List<ListadoBench>> getEmpleadoPorSaga(@PathVariable String saga) {
        return service.getEmpleadoPorSaga(saga);
    }

    /**
     * Retrieves a list of staffing entries that belong to the same department as the person with the specified ggid.
     *
     * <p>This method handles HTTP GET requests to the path with a ggid parameter. It returns a list of {@link ListadoBench}
     * objects representing all the staff that belong to the same department as the person associated with the provided ggid.</p>
     *
     * @param ggid The ggid of the person whose department will be used to filter the staffing entries.
     * @return A {@link List} of {@link ListadoBench} objects containing all staff in the same department.
     */
    @GetMapping("/ggid/{ggid}")
    public List<ListadoBench> getStaffingByDepartment(@PathVariable String ggid) {
        return service.getStaffingByDepartment(ggid);
    }
}
