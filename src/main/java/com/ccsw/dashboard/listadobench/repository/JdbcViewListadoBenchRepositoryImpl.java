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
				        "COALESCE(stf.vc_Profile_Meses_Bench, 0) AS mesesBench, " +
				        "COALESCE(cert.vc_partner, '') AS partner, " +
				        "COALESCE(cert.vc_certificado, '') AS certificado, " +
				        "COALESCE(cert.vc_name_gtd, '') AS nameGtd, " +
				        "COALESCE(cert.vc_certification_gtd, '') AS certificacionGtd, " +
				        "COALESCE(cert.vc_code, '') AS code, " +
				        "COALESCE(cert.vc_sector, '') AS sector, " +
				        "COALESCE(cert.vc_modulo, '') AS modulo, " +
				        "COALESCE(cert.vc_id_candidato, '') AS candidato, " +
				        "COALESCE(cert.vc_fecha_certificado, '') AS fCertificado, " +
				        "COALESCE(cert.vc_fecha_expiracion, '') AS expiracion, " +
				        "COALESCE(cert.vc_activo, '') AS activo, " +
				        "COALESCE(cert.vc_anexo, '') AS anexo, " +
				        "COALESCE(cert.vc_comentario_anexo, '') AS comentario, " +
				        "stf.vc_Profile_Status AS status " +
				        "FROM dashboard.dm_staffing_import stf " +
				        "LEFT JOIN dashboard.dm_certificaciones_import cert ON stf.vc_Profile_SAGA COLLATE utf8mb4_general_ci = cert.vc_saga " +
				        "WHERE stf.num_Import_CodeId COLLATE utf8mb4_general_ci = 45 " +
				        "AND stf.vc_Profile_Status = 'Disponible' " +
				        "ORDER BY saga ASC",this::mapRowToDataFormation);
	}
	 

	private ListadoBench mapRowToDataFormation(ResultSet row, int rowNum) throws SQLException {
		ListadoBench dataFormation = new ListadoBench();
		dataFormation.setSaga(row.getString("saga"));
		dataFormation.setGgid(row.getString("ggid"));
		dataFormation.setNombre(row.getString("nombre"));
		dataFormation.setApellidos(row.getString("apellidos"));
		dataFormation.setPractica(row.getString("practica"));
		dataFormation.setGrado(row.getString("grado"));
		dataFormation.setCategoria(row.getString("categoria"));
		dataFormation.setPerfil_Tecnico(row.getString("perfil"));
		dataFormation.setFecha_Incorporacion(row.getDate("fIncorporacion"));
		dataFormation.setAsignacion(row.getInt("porcentajeAsignacion"));
		dataFormation.setStatus(row.getString("status"));
		dataFormation.setCliente_ctual(row.getString("clienteActual"));
		dataFormation.setFecha_Inicio_asignacion(row.getDate("inicioAsignacion"));
		dataFormation.setFecha_Fin_signacion(row.getDate("finAsignacion"));
		dataFormation.setFecha_Disponibilidad(row.getDate("fDisponibilidad"));
		dataFormation.setPosicion_Proyecto_Futuro(row.getString("posicionFuturo"));
		dataFormation.setColaboraciones(row.getString("colaboraciones"));
		dataFormation.setProyecto_anterior(row.getString("proyectoAnterior"));
		dataFormation.setMeses_Bench(row.getString("mesesBench"));
		dataFormation.setPartner(row.getString("partner"));
		dataFormation.setCertificado(row.getString("certificado"));
		dataFormation.setName_gtd(row.getString("nameGtd"));
		dataFormation.setCertification_gtd(row.getString("certificacionGtd"));
		dataFormation.setCode(row.getString("code"));
		dataFormation.setSector(row.getString("sector"));
		dataFormation.setModulo(row.getString("modulo"));
		dataFormation.setId_candidato(row.getString("candidato"));
		dataFormation.setFecha_certificado(row.getDate("fCertificado"));
		dataFormation.setFecha_expiracion(row.getDate("expiracion"));
		dataFormation.setActivo(row.getString("activo"));
		dataFormation.setAnexo(row.getString("anexo"));
		dataFormation.setComentario_anexo(row.getString("comentario"));

		return dataFormation;
	}

}