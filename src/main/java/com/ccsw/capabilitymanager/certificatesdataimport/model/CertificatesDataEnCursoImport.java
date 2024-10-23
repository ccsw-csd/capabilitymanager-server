package com.ccsw.capabilitymanager.certificatesdataimport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "certificaciones_curso")
public class CertificatesDataEnCursoImport implements Comparable<CertificatesDataEnCursoImport> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ggid", nullable = false)
  private int ggid;
  
  @Column(name = "num_import_code_id")
  private int numImportCodeId;

  @Column(name = "fechaSolicitud", nullable = true)
  private Date fechaSolicitud;

  @Column(name = "anoSolicitud", nullable = true)
  private String anoSolicitud;

  @Column(name = "qSolicitud", nullable = true)
  private String qSolicitud;

  @Column(name = "proveedor", nullable = true)
  private String proveedor;

  @Column(name = "factura", nullable = true)
  private String factura;

  @Column(name = "fechaFactura", nullable = true)
  private Date fechaFactura;

  @Column(name = "importe", nullable = true)
  private String importe;

  @Column(name = "moneda", nullable = true)
  private String moneda;

  @Column(name = "fechaContabilidad", nullable = true)
  private Date fechaContabilidad;

  @Column(name = "voucher", nullable = true)
  private String voucher;

  @Column(name = "caducidadVoucher", nullable = true)
  private Date caducidadVoucher;

  @Column(name = "enviadoAlProveedor", nullable = true)
  private String enviadoAlProveedor;

  @Column(name = "bu", nullable = true)
  private String bu;

  @Column(name = "bla", nullable = true)
  private String bla;

  @Column(name = "une", nullable = true)
  private String une;

  @Column(name = "grado", nullable = true)
  private String grado;

  @Column(name = "codProyecto", nullable = true)
  private String codProyecto;

  @Column(name = "coordinador", nullable = true)
  private String coordinador;

  @Column(name = "responsable", nullable = true)
  private String responsable;

  @Column(name = "autorizadoPorElResponsable", nullable = true)
  private String autorizadoPorElResponsable;

  @Column(name = "gestion", nullable = true)
  private String gestion;

  @Column(name = "conocimiento", nullable = true)
  private String conocimiento;

  @Column(name = "owner", nullable = true)
  private String owner;

  @Column(name = "accionYDetalle", nullable = true)
  private String accionYDetalle;

  @Column(name = "saga", nullable = false)
  private String saga;

  @Column(name = "fechaSC", nullable = true)
  private Date fechaSC;

  @Column(name = "sc", nullable = true)
  private String sc;

  @Column(name = "po", nullable = true)
  private String po;

  @Column(name = "solicitante", nullable = true)
  private String solicitante;

  @Column(name = "apellidos", nullable = true)
  private String apellidos;

  @Column(name = "nombre", nullable = true)
  private String nombre;

  @Column(name = "email", nullable = true)
  private String email;

  @Column(name = "telefonoContacto", nullable = true)
  private String telefonoContacto;

  @Column(name = "partner", nullable = true)
  private String partner;

  @Column(name = "codigoYDescripcionDelExamen", nullable = true)
  private String codigoYDescripcionDelExamen;

  @Column(name = "modalidad", nullable = true)
  private String modalidad;

  @Column(name = "fechaExamen", nullable = false)
  private Date fechaExamen;

  @Column(name = "hora", nullable = true)
  private String hora;

  @Column(name = "idioma", nullable = true)
  private String idioma;

  @Column(name = "centroDeTrabajo", nullable = true)
  private String centroDeTrabajo;

  @Column(name = "numOportunidadesMismoCodigoDeExamen", nullable = true)
  private String numOportunidadesMismoCodigoDeExamen;

  @Column(name = "requestState", nullable = true)
  private String requestState;

  @Column(name = "observaciones", nullable = true)
  private String observaciones;

  @Column(name = "fechaBajaCia", nullable = true)
  private Date fechaBajaCia;

  @Column(name = "linkRenovacion", nullable = true)
  private String linkRenovacion;

  @Column(name = "linkOferta", nullable = true)
  private String linkOferta;
  
  

  // Getters and setters

  public int getId() {

    return id;
  }

  public void setId(int id) {

    this.id = id;
  }

  public int getGgid() {

    return ggid;
  }

  public void setGgid(int ggid) {

    this.ggid = ggid;
  }

  public Date getFechaSolicitud() {

    return fechaSolicitud;
  }

  public void setFechaSolicitud(Date fechaSolicitud) {

    this.fechaSolicitud = fechaSolicitud;
  }

  public String getAnoSolicitud() {

    return anoSolicitud;
  }

  public void setAnoSolicitud(String anoSolicitud) {

    this.anoSolicitud = anoSolicitud;
  }

  public String getqSolicitud() {

    return qSolicitud;
  }

  public void setqSolicitud(String qSolicitud) {

    this.qSolicitud = qSolicitud;
  }

  public String getProveedor() {

    return proveedor;
  }

  public void setProveedor(String proveedor) {

    this.proveedor = proveedor;
  }

  public String getFactura() {

    return factura;
  }

  public void setFactura(String factura) {

    this.factura = factura;
  }

  public Date getFechaFactura() {

    return fechaFactura;
  }

  public void setFechaFactura(Date fechaFactura) {

    this.fechaFactura = fechaFactura;
  }

  public String getImporte() {

    return importe;
  }

  public void setImporte(String importe) {

    this.importe = importe;
  }

  public String getMoneda() {

    return moneda;
  }

  public void setMoneda(String moneda) {

    this.moneda = moneda;
  }

  public Date getFechaContabilidad() {

    return fechaContabilidad;
  }

  public void setFechaContabilidad(Date fechaContabilidad) {

    this.fechaContabilidad = fechaContabilidad;
  }

  public String getVoucher() {

    return voucher;
  }

  public void setVoucher(String voucher) {

    this.voucher = voucher;
  }

  public Date getCaducidadVoucher() {

    return caducidadVoucher;
  }

  public void setCaducidadVoucher(Date caducidadVoucher) {

    this.caducidadVoucher = caducidadVoucher;
  }

  public String getEnviadoAlProveedor() {

    return enviadoAlProveedor;
  }

  public void setEnviadoAlProveedor(String enviadoAlProveedor) {

    this.enviadoAlProveedor = enviadoAlProveedor;
  }

  public String getBu() {

    return bu;
  }

  public void setBu(String bu) {

    this.bu = bu;
  }

  public String getBla() {

    return bla;
  }

  public void setBla(String bla) {

    this.bla = bla;
  }

  public String getUne() {

    return une;
  }

  public void setUne(String une) {

    this.une = une;
  }

  public String getGrado() {

    return grado;
  }

  public void setGrado(String grado) {

    this.grado = grado;
  }

  public String getCodProyecto() {

    return codProyecto;
  }

  public void setCodProyecto(String codProyecto) {

    this.codProyecto = codProyecto;
  }

  public String getCoordinador() {

    return coordinador;
  }

  public void setCoordinador(String coordinador) {

    this.coordinador = coordinador;
  }

  public String getResponsable() {

    return responsable;
  }

  public void setResponsable(String responsable) {

    this.responsable = responsable;
  }

  public String getAutorizadoPorElResponsable() {

    return autorizadoPorElResponsable;
  }

  public void setAutorizadoPorElResponsable(String autorizadoPorElResponsable) {

    this.autorizadoPorElResponsable = autorizadoPorElResponsable;
  }

  public String getGestion() {

    return gestion;
  }

  public void setGestion(String gestion) {

    this.gestion = gestion;
  }

  public String getConocimiento() {

    return conocimiento;
  }

  public void setConocimiento(String conocimiento) {

    this.conocimiento = conocimiento;
  }

  public String getOwner() {

    return owner;
  }

  public void setOwner(String owner) {

    this.owner = owner;
  }

  public String getAccionYDetalle() {

    return accionYDetalle;
  }

  public void setAccionYDetalle(String accionYDetalle) {

    this.accionYDetalle = accionYDetalle;
  }

  public String getSaga() {

    return saga;
  }

  public void setSaga(String saga) {

    this.saga = saga;
  }

  public Date getFechaSc() {

    return fechaSC;
  }

  public void setFechaSc(Date fechaSc) {

    this.fechaSC = fechaSc;
  }

  public String getSc() {

    return sc;
  }

  public void setSc(String sc) {

    this.sc = sc;
  }

  public String getPo() {

    return po;
  }

  public void setPo(String po) {

    this.po = po;
  }

  public String getSolicitante() {

    return solicitante;
  }

  public void setSolicitante(String solicitante) {

    this.solicitante = solicitante;
  }

  public String getApellidos() {

    return apellidos;
  }

  public void setApellidos(String apellidos) {

    this.apellidos = apellidos;
  }

  public String getNombre() {

    return nombre;
  }

  public void setNombre(String nombre) {

    this.nombre = nombre;
  }

  public String getEmail() {

    return email;
  }

  public void setEmail(String email) {

    this.email = email;
  }

  public String getTelefonoContacto() {

    return telefonoContacto;
  }

  public void setTelefonoContacto(String telefonoContacto) {

    this.telefonoContacto = telefonoContacto;
  }

  public String getPartner() {

    return partner;
  }

  public void setPartner(String partner) {

    this.partner = partner;
  }

  public String getCodigoYDescripcionDelExamen() {

    return codigoYDescripcionDelExamen;
  }

  public void setCodigoYDescripcionDelExamen(String codigoYDescripcionDelExamen) {

    this.codigoYDescripcionDelExamen = codigoYDescripcionDelExamen;
  }

  public String getModalidad() {

    return modalidad;
  }

  public void setModalidad(String modalidad) {

    this.modalidad = modalidad;
  }

  public Date getFechaExamen() {

    return fechaExamen;
  }

  public void setFechaExamen(Date fechaExamen) {

    this.fechaExamen = fechaExamen;
  }

  public String getHora() {

    return hora;
  }

  public void setHora(String hora) {

    this.hora = hora;
  }

  public String getIdioma() {

    return idioma;
  }

  public void setIdioma(String idioma) {

    this.idioma = idioma;
  }

  public String getCentroDeTrabajo() {

    return centroDeTrabajo;
  }

  public void setCentroDeTrabajo(String centroDeTrabajo) {

    this.centroDeTrabajo = centroDeTrabajo;
  }

  public String getNumOportunidadesMismoCodigoDeExamen() {

    return numOportunidadesMismoCodigoDeExamen;
  }

  public void setNumOportunidadesMismoCodigoDeExamen(String numOportunidadesMismoCodigoDeExamen) {

    this.numOportunidadesMismoCodigoDeExamen = numOportunidadesMismoCodigoDeExamen;
  }

  public String getRequestState() {

    return requestState;
  }

  public void setRequestState(String requestState) {

    this.requestState = requestState;
  }

  public String getObservaciones() {

    return observaciones;
  }

  public void setObservaciones(String observaciones) {

    this.observaciones = observaciones;
  }

  public Date getFechaBajaCia() {

    return fechaBajaCia;
  }

  public void setFechaBajaCia(Date fechaBajaCia) {

    this.fechaBajaCia = fechaBajaCia;
  }

  public String getLinkRenovacion() {

    return linkRenovacion;
  }

  public void setLinkRenovacion(String linkRenovacion) {

    this.linkRenovacion = linkRenovacion;
  }

  public String getLinkOferta() {

    return linkOferta;
  }

  public void setLinkOferta(String linkOferta) {

    this.linkOferta = linkOferta;
  }

  @Override
  public int compareTo(CertificatesDataEnCursoImport o) {

    return this.id - o.id;
  }
}