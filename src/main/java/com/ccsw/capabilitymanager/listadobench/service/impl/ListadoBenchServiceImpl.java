package com.ccsw.capabilitymanager.listadobench.service.impl;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.repository.ViewListadoBenchRepository;
import com.ccsw.capabilitymanager.listadobench.service.ListadoBenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListadoBenchServiceImpl implements ListadoBenchService {

    @Autowired
    private ViewListadoBenchRepository listadoBenchRepository;

    /**
     * Retrieves a list of all staffing entries from the repository.
     *
     * <p>This method interacts with the {@link ListadoBenchRepository} to fetch all staffing entries. It casts the result
     * to a {@link List} of {@link ListadoBench} objects.</p>
     *
     * @return A {@link List} of {@link ListadoBench} objects representing all staffing entries.
     */
    @Override
    public List<ListadoBench> getListadoPersonasBench() {
        return (List<ListadoBench>) listadoBenchRepository.getListadoPersonasBench();
    }

    /**
     * Retrieves a list of staffing entries filtered by a specific saga from the repository.
     *
     * <p>This method interacts with the {@link ListadoBenchRepository} to fetch staffing entries that match the specified
     * saga. It returns an {@link Optional} containing a {@link List} of {@link ListadoBench} objects. If no entries are found
     * for the given saga, the {@link Optional} will be empty.</p>
     *
     * @param saga The saga identifier used to filter the staffing entries.
     * @return An {@link Optional} containing a {@link List} of {@link ListadoBench} objects that match the specified saga,
     *         or an empty {@link Optional} if no entries are found.
     */
    @Override
    public Optional<List<ListadoBench>> getEmpleadoPorSaga(String saga) {
        return listadoBenchRepository.getEmpleadoPorSaga(saga);
    }
}


