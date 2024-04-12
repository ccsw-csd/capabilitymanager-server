package com.ccsw.dashboard.listadobench.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ccsw.dashboard.listadobench.model.ListadoBench;

@Repository
public class JdbcViewListadoBenchRepositoryImpl implements ViewListadoBenchRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcViewListadoBenchRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public Collection<ListadoBench> getListadoPersonasBench() {
		return jdbcTemplate.query("SELECT DISTINCT " +
		        "stf.Id AS id, " +
		        "stf.vc_Profile_SAGA AS saga, " +
		        "stf.vc_Profile_GGID AS ggid, " +
		        "stf.vc_Profile_Nombre AS nombre, " +
		        "stf.vc_Profile_Apellidos AS apellidos, " +
		        "stf.vc_Profile_Practica AS practica, " +
		        "stf.vc_Profile_Grado AS grado, " +
		        "stf.vc_Profile_Categoria AS categoria, " +
		        "stf.vc_Profile_Perfil_Tecnico AS perfil, " +
		        "stf.vc_Profile_Fecha_Incorporacion AS fIncorporacion, " +
		        "COALESCE(stf.vc_Profile_Asignacion, 0) AS porcentajeAsignacion, " +
		        "stf.vc_Profile_Cliente_Actual AS clienteActual, " +
		        "stf.vc_Profile_Fecha_Inicio_Asignacion AS inicioAsignacion, " +
		        "stf.vc_Profile_Fecha_Fin_Asignacion AS finAsignacion, " +
		        "stf.vc_Profile_Fecha_Disponibilidad AS fDisponibilidad, " +
		        "stf.vc_Profile_Posicion_Proyecto_Futuro AS posicionFuturo, " +
		        "stf.vc_Profile_Colaboraciones AS colaboraciones, " +
		        "stf.vc_Profile_Proyecto_Anterior AS proyectoAnterior, " +
		        "stf.vc_Profile_Meses_Bench AS mesesBench, " +
		        "stf.vc_Profile_Status AS status " +
		        "FROM dashboard.dm_staffing_import stf " +
		        "WHERE stf.num_Import_CodeId COLLATE utf8mb4_general_ci = (select max(id) from dashboard.version_staffing) " +
		        "AND stf.vc_Profile_Status = 'Disponible' " +
		        "ORDER BY saga ASC", this::mapRowToDataFormation);
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
		dataFormation.setPerfilTecnico(row.getString("perfil"));
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
		dataFormation.setMesesBench(row.getString("mesesBench"));

		return dataFormation;
	}

}