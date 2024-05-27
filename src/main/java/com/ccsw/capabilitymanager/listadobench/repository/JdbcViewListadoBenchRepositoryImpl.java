package com.ccsw.capabilitymanager.listadobench.repository;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcViewListadoBenchRepositoryImpl implements ViewListadoBenchRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcViewListadoBenchRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<ListadoBench> getListadoPersonasBench() {
        String query = "SELECT DISTINCT " +
                "stf.Id AS id, " +
                "stf.SAGA AS saga, " +
                "stf.GGID AS ggid, " +
                "stf.nombre AS nombre, " +
                "stf.apellidos AS apellidos, " +
                "stf.practica AS practica, " +
                "stf.grado AS grado, " +
                "stf.categoria AS categoria, " +
                "stf.centro AS centro, " +
                "form.rol_L1 AS rol, " +
                "stf.perfil_tecnico AS perfil, " +
                "stf.primary_skill AS primarySkill, " +
                "stf.fecha_incorporacion AS fIncorporacion, " +
                "COALESCE(stf.asignacion, 0) AS porcentajeAsignacion, " +
                "stf.cliente_actual AS clienteActual, " +
                "stf.fecha_inicio_asignacion AS inicioAsignacion, " +
                "stf.fecha_fin_asignacion AS finAsignacion, " +
                "stf.fecha_disponibilidad AS fDisponibilidad, " +
                "stf.posicion_proyecto_futuro AS posicionFuturo, " +
                "stf.colaboraciones AS colaboraciones, " +
                "stf.proyecto_anterior AS proyectoAnterior, " +
                "stf.ingles_escrito AS inglesEscrito, " +
                "stf.ingles_hablado AS inglesHablado, " +
                "stf.jornada AS jornada, " +
                "stf.meses_bench AS mesesBench, " +
                "stf.status AS status " +
                "FROM staffing stf " +
                "LEFT JOIN formdata form ON stf.SAGA = form.SAGA " +
                "WHERE stf.num_import_code_id COLLATE utf8mb4_general_ci = (SELECT MAX(id) FROM version_staffing) " +
                "AND stf.status IN ('Disponible', 'TRI+BDCS', 'BDCS')";


        return jdbcTemplate.query(query, this::mapRowToDataFormation);
    }

    public Optional<List<ListadoBench>> getEmpleadoPorSaga(String saga) {
        String query = "SELECT DISTINCT " +
                "stf.Id AS id, " +
                "stf.SAGA AS saga, " +
                "stf.GGID AS ggid, " +
                "stf.nombre AS nombre, " +
                "stf.apellidos AS apellidos, " +
                "stf.practica AS practica, " +
                "stf.grado AS grado, " +
                "stf.categoria AS categoria, " +
                "stf.centro AS centro, " +
                "form.rol_L1 AS rol, " +
                "stf.perfil_tecnico AS perfil, " +
                "stf.primary_skill AS primarySkill, " +
                "stf.fecha_incorporacion AS fIncorporacion, " +
                "COALESCE(stf.asignacion, 0) AS porcentajeAsignacion, " +
                "stf.cliente_actual AS clienteActual, " +
                "stf.fecha_inicio_asignacion AS inicioAsignacion, " +
                "stf.fecha_fin_asignacion AS finAsignacion, " +
                "stf.fecha_disponibilidad AS fDisponibilidad, " +
                "stf.posicion_proyecto_futuro AS posicionFuturo, " +
                "stf.colaboraciones AS colaboraciones, " +
                "stf.proyecto_anterior AS proyectoAnterior, " +
                "stf.ingles_escrito AS inglesEscrito, " +
                "stf.ingles_hablado AS inglesHablado, " +
                "stf.jornada AS jornada, " +
                "stf.meses_bench AS mesesBench, " +
                "stf.status AS status " +
                "FROM staffing stf " +
                "LEFT JOIN formdata form ON stf.SAGA = form.SAGA " +
                "WHERE stf.num_import_code_id = (SELECT MAX(id) FROM version_staffing) AND stf.SAGA = ?";

        List<ListadoBench> empleados = jdbcTemplate.query(query, new Object[]{saga}, this::mapRowToDataFormation);
        return Optional.ofNullable(empleados.isEmpty() ? null : empleados);
    }

    private ListadoBench mapRowToDataFormation(ResultSet row, int rowNum) throws SQLException {
        ListadoBench dataFormation = new ListadoBench();
        dataFormation.setId(row.getLong("id"));
        dataFormation.setSaga(row.getString("saga"));
        dataFormation.setGgid(row.getString("ggid"));
        dataFormation.setNombre(row.getString("nombre"));
        dataFormation.setApellidos(row.getString("apellidos"));
        dataFormation.setPractica(row.getString("practica"));
        dataFormation.setGrado(row.getString("grado"));
        dataFormation.setCategoria(row.getString("categoria"));
        dataFormation.setCentro(row.getString("centro"));
        dataFormation.setRol(row.getString("rol"));
        dataFormation.setPerfilTecnico(row.getString("perfil"));
        dataFormation.setPrimarySkill(row.getString("primarySkill"));
        dataFormation.setFechaIncorporacion(row.getDate("fIncorporacion"));
        dataFormation.setAsignacion(row.getInt("porcentajeAsignacion"));
        dataFormation.setStatus(row.getString("status"));
        dataFormation.setClienteActual(row.getString("clienteActual"));
        dataFormation.setFechaInicioAsignacion(row.getDate("inicioAsignacion"));
        dataFormation.setFechaFinAsignacion(row.getDate("finAsignacion"));
        dataFormation.setFechaDisponibilidad(row.getDate("fDisponibilidad"));
        dataFormation.setPosicionProyectoFuturo(row.getString("posicionFuturo"));
        dataFormation.setColaboraciones(row.getString("colaboraciones"));
        dataFormation.setProyectoAnterior(row.getString("proyectoAnterior"));
        dataFormation.setInglesEscrito(row.getString("inglesEscrito"));
        dataFormation.setInglesHablado(row.getString("inglesHablado"));
        dataFormation.setJornada(row.getString("jornada"));
        dataFormation.setMesesBench(row.getString("mesesBench"));

        return dataFormation;
    }
}
