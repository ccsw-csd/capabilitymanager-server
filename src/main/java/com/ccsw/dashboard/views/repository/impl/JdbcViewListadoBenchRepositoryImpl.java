package com.ccsw.dashboard.views.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.dataformation.model.DataFormation;
import com.ccsw.dashboard.views.repository.ViewListadoBenchRepository;


@Repository
public class JdbcViewListadoBenchRepositoryImpl implements ViewListadoBenchRepository{
	
	private final JdbcTemplate jdbcTemplate;
	
	public JdbcViewListadoBenchRepositoryImpl (JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public Collection<DataFormation> getDataFormation(int idVersionCapacidades, int idVersionStaffing,int idCertidficados) {
	    return jdbcTemplate.query(
	        "SELECT DISTINCT " +
	        "    stf.vc_Profile_SAGA AS saga, " +
	        "    stf.vc_Profile_GGID AS ggid, " +
	        "    stf.vc_Profile_Nombre AS nombre, " +
	        "    stf.vc_Profile_Apellidos AS apellidos, " +
	        "    stf.vc_Profile_Practica AS practica, " +
	        "    stf.vc_Profile_Grado AS grado, " +
	        "    stf.vc_Profile_Categoria AS categoria, " +
	        "    stf.vc_Profile_Perfil_Tecnico AS perfil, " +
	        "    stf.vc_Profile_Fecha_Incorporacion AS fIncorporacion, " +
	        "    stf.vc_Profile_Asignacion AS porcentajeAsignacion, " +
	        "    stf.vc_Profile_Status AS status, " +
	        "    stf.vc_Profile_Cliente_Actual AS clienteActual, " +
	        "    stf.vc_Profile_Fecha_Inicio_Asignacion AS inicioAsignacion, " +
	        "    stf.vc_Profile_Fecha_Fin_Asignacion AS finAsignacion, " +
	        "    stf.vc_Profile_Fecha_Disponibilidad AS fDisponibilidad, " +
	        "    stf.vc_Profile_Posicion_Proyecto_Futuro AS posicionFuturo, " +
	        "    stf.vc_Profile_Colaboraciones AS colaboraciones, " +
	        "    stf.vc_Profile_Proyecto_Anterior AS proyectoAnterior, " +
	        "    stf.vc_Profile_Meses_Bench AS mesesBench, " +
	        "    cert.vc_partner AS partner, " +
	        "    cert.vc_certificado AS certificado, " +
	        "    cert.vc_name_gtd AS nameGtd, " +
	        "    cert.vc_certification_gtd AS certificacionGtd, " +
	        "    cert.vc_code AS code, " +
	        "    cert.vc_sector AS sector, " +
	        "    cert.vc_modulo AS modulo, " +
	        "    cert.vc_id_candidato AS candidato, " +
	        "    cert.vc_fecha_certificado AS fCertificado, " +
	        "    cert.vc_fecha_expiracion AS expiracion, " +
	        "    cert.vc_activo AS activo, " +
	        "    cert.vc_anexo AS anexo, " +
	        "    cert.vc_comentario_anexo AS comentario, " +
	        "    form.vc_Profile_Rol_L1 AS rolL1 " +
	        "FROM " +
	        "    dashboard.dm_staffing_import stf " +
	        "INNER JOIN " +
	        "    dashboard.dm_formdata_import form ON stf.vc_Profile_SAGA = form.vc_Profile_SAGA " +
	        "INNER JOIN " +
	        "    dashboard.dm_certificaciones_import cert ON stf.vc_Profile_SAGA = cert.vc_saga " +
	        "WHERE " +
	        "    stf.num_Import_CodeId = (SELECT MAX(id) FROM dashboard.version_staffing) " +
	        "AND " +
	        "    cert.num_import_code_id = (SELECT MAX(id) FROM dashboard.version_certificados)",
	        this::mapRowToDataFormation
	    );
	}
	
	private DataFormation mapRowToDataFormation(ResultSet row, int rowNum) throws SQLException {
	    DataFormation dataFormation = new DataFormation();
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