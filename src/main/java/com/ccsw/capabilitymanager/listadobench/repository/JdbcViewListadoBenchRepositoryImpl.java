package com.ccsw.capabilitymanager.listadobench.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;

@Repository
public class JdbcViewListadoBenchRepositoryImpl implements ViewListadoBenchRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcViewListadoBenchRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<ListadoBench> getListadoPersonasBench() {
		return jdbcTemplate.query("SELECT DISTINCT " +
				"stf.Id AS id, " +
				"stf.SAGA AS saga, " +
				"stf.GGID AS ggid, " +
				"stf.nombre AS nombre, " +
				"stf.apellidos AS apellidos, " +
				"stf.practica AS practica, " +
				"stf.grado AS grado, " +
				"stf.categoria AS categoria,\r\n"
				+ "stf.centro as centro,\r\n"
				+ "form.rol_L1 as rol, "
				+ "stf.perfil_tecnico AS perfil,\r\n"
				+ "stf.primary_skill as primarySkill, " +
				"stf.fecha_incorporacion AS fIncorporacion, " +
				"COALESCE(stf.asignacion, 0) AS porcentajeAsignacion, " +
				"stf.cliente_actual AS clienteActual, " +
				"stf.fecha_inicio_asignacion AS inicioAsignacion, " +
				"stf.fecha_fin_asignacion AS finAsignacion, " +
				"stf.fecha_disponibilidad AS fDisponibilidad, " +
				"stf.posicion_proyecto_futuro AS posicionFuturo, " +
				"stf.colaboraciones AS colaboraciones, " +
				"stf.proyecto_anterior AS proyectoAnterior,\r\n"
				+ "stf.ingles_escrito  as inglesEscrito,\r\n"
				+ "stf.ingles_hablado as inglesHablado,\r\n"
				+ "stf.jornada as jornada, " +
				"stf.meses_bench AS mesesBench, " +
				"stf.status AS status " +
				"FROM staffing stf, formdata form \r\n"
				+ "WHERE stf.num_import_code_id COLLATE utf8mb4_general_ci = (select max(id) from version_staffing)\r\n"
				+ " and form.num_import_code_id  COLLATE utf8mb4_general_ci = (select max(id) from version_capacidades)\r\n"
				+ " and stf.SAGA = form.SAGA\r\n"
				+ " and (status not like 'En Proyecto%' and status not like 'Baja%' and status not like 'TRI%')", this::mapRowToDataFormation);
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