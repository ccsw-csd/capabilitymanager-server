package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.activitydataimport.ActivityDataImportRepository;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataEnCursoImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataEnCursoImport;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataEnCursoImportDto;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.certificatesversion.CertificatesVersionRepository;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.exception.FileProcessException;
import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.reportversion.ReportVersionRepository;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.utils.UtilsService;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapacidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import com.ccsw.capabilitymanager.websocket.WebSocketService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FileProcessServiceImpl implements FileProcessService{

    private static final String ESTADO_PROCESANDO = "PROCESANDO";
    private static final String ESTADO_PROCESADO = "PROCESADO";

    private static final int BATCH_SIZE = 700;

    @Autowired
    private FileProcessRepository fileProcessRepository;

    @Autowired
    private VersionCapacidadesRepository versionCapacidadesRepository;

    @Autowired
    private VersionCertificacionesRepository versionCertificacionesRepository;

    @Autowired
    private CertificatesVersionRepository certificatesVersionRepository;
    
    @Autowired
	private CertificatesDataImportRepository certificatesDataImportRepository;

   @Autowired
   private CertificatesDataEnCursoImportRepository certificatesDataEnCursoImportRepository;

    @Autowired
	private ActivityDataImportRepository activityDataImportRepository;

    @Autowired
    private FormDataImportRepository formDataImportRepository;
    
    @Autowired
    private ReportVersionRepository reportVersionRepository;
    
    @Autowired
    private CertificatesDataImportRepository certificateDataImportRepository;

    @Autowired
    private DataserviceS3 dataserviceS3;

    @Autowired
    private UtilsService utilsService;
    
    @Autowired
    private WebSocketService webSocketService;

    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> processPendingFile(FileProcess file) throws Exception {
        return CompletableFuture.runAsync(() -> {
            MinioClient minioClient = dataserviceS3.getMinioClient();
            InputStream fileDownloaded = null;

            try {
                fileDownloaded = minioClient.getObject(GetObjectArgs.builder().bucket(file.getNombreBucket()).object(file.getNombreFichero()).build());
            } catch (Exception e) {
                CapabilityLogger.logError("Error obteniendo el fichero " + file.getNombreFichero() + " de S3.");
                throw new FileProcessException("Error obteniendo el fichero " + file.getNombreFichero() + " de S3.");
            }

            file.setEstado(ESTADO_PROCESANDO);
            fileProcessRepository.save(file);
            fileProcessRepository.flush();

            switch (file.getTipoFichero()) {
    		case "1":
    			//processStaffingDoc();
    			break;
    		case "2":
    			processRolsDoc(file, fileDownloaded);
    			break;
    		case "3":
				processCertificatesDoc(file, fileDownloaded);
    			break;
            case "4":
                //processItinerariosDoc();
                break;

            case "5":
                processCertificatesDocEnCurso(file, fileDownloaded);
                break;
    		default:
    			//setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto,
    			//		Constants.ERROR_DOCUMENT_TYPE, Constants.ERROR_DOCUMENT_TYPE, Constants.EMPTY,
    			//		HttpStatus.BAD_REQUEST);
    		}


        });
    }


  /**
   * Processes the "Certificates Doc En Curso" file.
   *
   * This method reads the provided Excel file, processes each row, and saves the data to the database.
   * It logs the start and end of the process and handles any IOExceptions that occur during file processing.
   *
   * @param file The FileProcess object containing details about the file being processed.
   * @param fileDownloaded The InputStream of the downloaded file from S3.
   */
  private void processCertificatesDocEnCurso(FileProcess file, InputStream fileDownloaded) {
    CapabilityLogger.logDebug("[DataImportServiceImpl] >>>> processCertificatesDocEnCurso");

    try (InputStream inputStream = fileDownloaded) {
        Sheet sheet = utilsService.obtainSheetFromInputStream(inputStream);
        int numRegistros = sheet.getPhysicalNumberOfRows() - 1;
        List<CertificatesDataEnCursoImportDto> listCertificacionesDataImport = new ArrayList<>();

        VersionCertificaciones verCertificaciones = createVersionCertificaciones(file, numRegistros);

        IntStream.range(Constants.ROW_EVIDENCE_LIST_NEXT_CERT_ENCURSO, sheet.getPhysicalNumberOfRows())
                .mapToObj(sheet::getRow)
                .filter(this::shouldProcessRow)
                .forEach(currentRow -> processRow(currentRow, verCertificaciones.getId(), listCertificacionesDataImport));

        saveData(file, listCertificacionesDataImport);
    } catch (IOException e) {
        CapabilityLogger.logError("Error closing InputStream: " + e.getMessage());
    }

    CapabilityLogger.logDebug("[DataImportServiceImpl] processCertificatesDocEnCurso >>>>");
}

  /**
   * Processes a single row from the Excel sheet and maps it to a CertificatesDataEnCursoImportDto object.
   *
   * This method attempts to map the data from the provided row to a CertificatesDataEnCursoImportDto object.
   * If the 'SAGA' field is not empty, the mapped object is added to the provided list.
   * Any ParseException encountered during the mapping process is logged.
   *
   * @param currentRow The current row being processed from the Excel sheet.
   * @param versionId The version ID of the import process.
   * @param listCertificacionesDataImport The list to which the mapped CertificatesDataEnCursoImportDto object will be added.
   */
private void processRow(Row currentRow, int versionId, List<CertificatesDataEnCursoImportDto> listCertificacionesDataImport) {
    try {
        CertificatesDataEnCursoImportDto data = mapRowToCertificatesDataImport(currentRow, versionId, currentRow.getRowNum());
        if (!data.getSaga().isEmpty()) {
            listCertificacionesDataImport.add(data);
        }
    } catch (ParseException e) {
        CapabilityLogger.logError("Error parsing date in row " + currentRow.getRowNum() + ": " + e.getMessage());
    }
}

  /**
   * Determines if the current row should be processed based on the 'Request State' column value.
   *
   * @param currentRow The current row being processed from the Excel sheet.
   * @return true if the 'Request State' column value is "En curso", false otherwise.
   */
  //Obtiene el nombre de la columna  para comprobar si existe
  private boolean shouldProcessRow(Row currentRow) {
    String requestState = utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_REQUEST_STATE.getPosition());
    return "En curso".equals(requestState);
  }


  /**
 * Creates a new version of certifications based on the provided file and number of records.
 *
 * @param file The file process object containing details about the file.
 * @param numRegistros The number of records to be processed.
 * @return A new instance of {@link VersionCertificaciones} if successful, or {@code null} if an error occurs.
 */
  private VersionCertificaciones createVersionCertificaciones(FileProcess file, int numRegistros) {
    try {
      return createCertificationesVersion(numRegistros, file.getTipoFichero(), file.getNombreFichero(), file.getNombreFichero(), file.getUsuario());
    } catch (Exception e) {
      CapabilityLogger.logError("Error creando la version del fichero de certificaciones . Error :" + e.getMessage());
      return null;
    }
  }


/**
 * Maps a row from an Excel sheet to a CertificatesDataEnCursoImportDto object.
 *
 * @param currentRow The current row being processed from the Excel sheet.
 * @param versionId The version ID of the import process.
 * @param rowIndex The index of the current row being processed.
 * @return A CertificatesDataEnCursoImportDto object populated with data from the current row.
 * @throws IllegalArgumentException if mandatory fields are missing or invalid.
 */
  private CertificatesDataEnCursoImportDto mapRowToCertificatesDataImport(Row currentRow, int versionId, int rowIndex)
      throws ParseException {
    CertificatesDataEnCursoImportDto data = new CertificatesDataEnCursoImportDto();


    data.setGgid(Integer.parseInt(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_ID.getPosition())));
    data.setFechaSolicitud(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FECHA_SOLICITUD.getPosition()));
    data.setAnoSolicitud(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_ANO_SOLICITUD.getPosition()));
    data.setqSolicitud(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_Q_SOLICITUD.getPosition()));
    data.setProveedor(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_PROVEEDOR.getPosition()));
    data.setFactura(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FACTURA.getPosition()));
    data.setFechaFactura(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FECHA_FACTURA.getPosition()));
    data.setImporte(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_IMPORTE.getPosition()));
    data.setMoneda(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_MONEDA.getPosition()));
    data.setFechaContabilidad(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FECHA_CONTABILIDAD.getPosition()));
    data.setVoucher(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_VOUCHER.getPosition()));
    data.setCaducidadVoucher(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_CADUCIDAD_VOUCHER.getPosition()));
    data.setEnviadoAlProveedor(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_ENVIADO_AL_PROVEEDOR.getPosition()));
    data.setBu(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_BU.getPosition()));
    data.setBla(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_BLA.getPosition()));
    data.setUne(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_UNE.getPosition()));
    data.setGrado(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_GRADO.getPosition()));
    data.setCodProyecto(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_COD_PROYECTO.getPosition()));
    data.setCoordinador(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_COORDINADOR.getPosition()));
    data.setResponsable(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_RESPONSABLE.getPosition()));
    data.setAutorizadoPorElResponsable(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_AUTORIZADO_POR_EL_RESPONSABLE.getPosition()));
    data.setGestion(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_GESTION.getPosition()));
    data.setConocimiento(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_CONOCIMIENTO.getPosition()));
    data.setOwner(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_OWNER.getPosition()));
    data.setAccionYDetalle(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_ACCION_Y_DETALLE.getPosition()));
    data.setSaga(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_SAGA.getPosition()));
    data.setFechaSc(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FECHA_SC.getPosition()));
    data.setSc(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_SC.getPosition()));
    data.setPo(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_PO.getPosition()));
    data.setSolicitante(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_SOLICITANTE.getPosition()));
    data.setApellidos(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_APELLIDOS.getPosition()));
    data.setNombre(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_NOMBRE.getPosition()));
    data.setEmail(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_EMAIL.getPosition()));
    data.setTelefonoContacto(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_TELEFONO_CONTACTO.getPosition()));
    data.setPartner(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_PARTNER.getPosition()));
    data.setCodigoYDescripcionDelExamen(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_CODIGO_Y_DESCRIPCION_DEL_EXAMEN.getPosition()));
    data.setModalidad(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_MODALIDAD.getPosition()));
    data.setFechaExamen(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FECHA_EXAMEN.getPosition()));
    data.setHora(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_HORA.getPosition()));
    data.setIdioma(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_IDIOMA.getPosition()));
    data.setCentroDeTrabajo(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_CENTRO_DE_TRABAJO.getPosition()));
    data.setNumOportunidadesMismoCodigoDeExamen(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_NUM_OPORTUNIDADES_MISMO_CODIGO_DE_EXAMEN.getPosition()));
    data.setRequestState(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_REQUEST_STATE.getPosition()));
    data.setObservaciones(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_OBSERVACIONES.getPosition()));
    data.setFechaBajaCia(utilsService.getDateValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_FECHA_BAJA_CIA.getPosition()));
    data.setLinkRenovacion(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_LINK_RENOVACION.getPosition()));
    data.setLinkOferta(utilsService.getStringValue(currentRow, Constants.CertificatesDataEnCursoPos.COL_LINK_OFERTA.getPosition()));



    //Como identificamos los campos obligatorios. Los que sean obligatorios se validan
    validateMandatoryFields(data.getSaga(),data.getGgid(), data.getFechaExamen(), versionId, rowIndex);

    return data;
  }


  /**
   * Validates mandatory fields in the provided data.
   * If any mandatory field is missing or empty, it rolls back the certificates and throws a FileProcessException.
   *
   * @param vcSAGA The SAGA value to be validated.
   * @param vcFechaCertificado The certification date to be validated.
   * @param versionId The version ID of the import process.
   * @param rowIndex The index of the current row being processed.
   * @throws FileProcessException if any mandatory field is missing or empty.
   */
  private void validateMandatoryFields(String vcSAGA,int vcGgid,  Date vcFechaCertificado, int versionId, int rowIndex) {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String vcFechaCertificadoStr = (vcFechaCertificado != null) ? dateFormat.format(vcFechaCertificado) : "";

    List<String> missingFields = new ArrayList<>();
    if (vcFechaCertificadoStr.isEmpty()) {
      missingFields.add("Fecha Examen");
    }
    if (vcSAGA == null || vcSAGA.isEmpty()) {
      missingFields.add("SAGA");
    }

    if (vcGgid == 0) {
      missingFields.add("GGID");
    }

    if (!missingFields.isEmpty()) {
      rollBackCertificates(versionId);
      throw new FileProcessException(
          "La fila " + rowIndex + " no contiene el campo obligatorio: " + String.join(", ", missingFields));
    }
  }

  /**
   * Saves the provided list of CertificatesDataEnCursoImportDto objects to the database.
   * If the list is not empty, it saves the data and updates the file status to "PROCESADO".
   * If the list is empty, it throws an UnprocessableEntityException.
   *
   * @param file The FileProcess object representing the file being processed.
   * @param listCertificacionesDataImport The list of CertificatesDataEnCursoImportDto objects to be saved.
   * @throws UnprocessableEntityException if the list is empty or contains no valid rows with 'Request State = En curso'.
   */
  private void saveData(FileProcess file, List<CertificatesDataEnCursoImportDto> listCertificacionesDataImport) {
    if (!listCertificacionesDataImport.isEmpty()) {

      saveAllCertificatesDataEnCursoImport(listCertificacionesDataImport);

      file.setEstado(ESTADO_PROCESADO);
      fileProcessRepository.save(file);
      fileProcessRepository.flush();

    } else {
      throw new UnprocessableEntityException("El fichero no contiene filas válidas con 'Request State = En curso'");
    }
  }

  /**
   * Saves a list of CertificatesDataEnCursoImportDto objects to the database in batches.
   *
   * @param certificatesDataImportList The list of CertificatesDataEnCursoImportDto objects to be saved.
   * @throws UnprocessableEntityException if an error occurs while processing the data.
   */
  @Transactional
  private void saveAllCertificatesDataEnCursoImport(List<CertificatesDataEnCursoImportDto> certificatesDataImportList) {
    try {
      //Mapeamos al objeto CertificatesDataEnCursoImport
      List<CertificatesDataEnCursoImport> entities = certificatesDataImportList.stream()
          .map(this::mapDtoToEntity)
          .collect(Collectors.toList());

      IntStream.range(0, (entities.size() + BATCH_SIZE - 1) / BATCH_SIZE)
          .mapToObj(i -> entities.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, entities.size())))
          .forEach(subList -> {
            CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
            certificatesDataEnCursoImportRepository.saveAll(subList);
            certificatesDataImportRepository.flush();
          });
    } catch (Exception e) {
      StringBuilder errorData = new StringBuilder();
      errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
          .append(Constants.ERROR_INIT2);
      CapabilityLogger.logError(errorData.toString() + e.getMessage());
      String respuestaEx = e.getMessage();
      String  respuesta = respuestaEx.substring(respuestaEx.indexOf('[')+1, respuestaEx.indexOf(']'));
      String respuestaEr = respuesta + ". Error procesando el excel. Comprueba los datos correctos";
      throw new UnprocessableEntityException(respuestaEr);
    }
  }

  /**
   * Mapea un objeto CertificatesDataEnCursoImportDto a CertificatesDataEnCursoImport.
   *
   * @param dto El objeto CertificatesDataEnCursoImportDto a mapear.
   * @return El objeto CertificatesDataEnCursoImport mapeado.
   */
  private CertificatesDataEnCursoImport mapDtoToEntity(CertificatesDataEnCursoImportDto dto) {
    CertificatesDataEnCursoImport entity = new CertificatesDataEnCursoImport();

    entity.setGgid(dto.getGgid());
    entity.setFechaSolicitud(dto.getFechaSolicitud());
    entity.setAnoSolicitud(dto.getAnoSolicitud());
    entity.setqSolicitud(dto.getqSolicitud());
    entity.setProveedor(dto.getProveedor());
    entity.setFactura(dto.getFactura());
    entity.setFechaFactura(dto.getFechaFactura());
    entity.setImporte(dto.getImporte());
    entity.setMoneda(dto.getMoneda());
    entity.setFechaContabilidad(dto.getFechaContabilidad());
    entity.setVoucher(dto.getVoucher());
    entity.setCaducidadVoucher(dto.getCaducidadVoucher());
    entity.setEnviadoAlProveedor(dto.getEnviadoAlProveedor());
    entity.setBu(dto.getBu());
    entity.setBla(dto.getBla());
    entity.setUne(dto.getUne());
    entity.setGrado(dto.getGrado());
    entity.setCodProyecto(dto.getCodProyecto());
    entity.setCoordinador(dto.getCoordinador());
    entity.setResponsable(dto.getResponsable());
    entity.setAutorizadoPorElResponsable(dto.getAutorizadoPorElResponsable());
    entity.setGestion(dto.getGestion());
    entity.setConocimiento(dto.getConocimiento());
    entity.setOwner(dto.getOwner());
    entity.setAccionYDetalle(dto.getAccionYDetalle());
    entity.setSaga(dto.getSaga());
    entity.setFechaSc(dto.getFechaSc());
    entity.setSc(dto.getSc());
    entity.setPo(dto.getPo());
    entity.setSolicitante(dto.getSolicitante());
    entity.setApellidos(dto.getApellidos());
    entity.setNombre(dto.getNombre());
    entity.setEmail(dto.getEmail());
    entity.setTelefonoContacto(dto.getTelefonoContacto());
    entity.setPartner(dto.getPartner());
    entity.setCodigoYDescripcionDelExamen(dto.getCodigoYDescripcionDelExamen());
    entity.setModalidad(dto.getModalidad());
    entity.setFechaExamen(dto.getFechaExamen());
    entity.setHora(dto.getHora());
    entity.setIdioma(dto.getIdioma());
    entity.setCentroDeTrabajo(dto.getCentroDeTrabajo());
    entity.setNumOportunidadesMismoCodigoDeExamen(dto.getNumOportunidadesMismoCodigoDeExamen());
    entity.setRequestState(dto.getRequestState());
    entity.setObservaciones(dto.getObservaciones());
    entity.setFechaBajaCia(dto.getFechaBajaCia());
    entity.setLinkRenovacion(dto.getLinkRenovacion());
    entity.setLinkOferta(dto.getLinkOferta());
    return entity;
  }




  /*------------------------------------------------------*/


  /**
   * procesado de certificaciones finales
    */

    private void processCertificatesDoc(FileProcess file, InputStream fileDownloaded) {
    	CapabilityLogger.logDebug("[FileProcessServiceImpl]  >>>> processCertificatesDoc ");

		Sheet sheet = utilsService.obtainSheetFromInputStream(fileDownloaded);
        int numRegistros = sheet.getPhysicalNumberOfRows() - 1;

		VersionCertificaciones verCertificaciones = null;
		try {
			verCertificaciones = createCertificationesVersion(numRegistros, file.getTipoFichero(), file.getNombreFichero(), file.getNombreFichero(), file.getUsuario());
		} catch (Exception e) {
			CapabilityLogger.logError("Error creando la version del fichero de certificaciones . Error -> " + e.getMessage());
		}

		borrarAntiguos();
		
		List<CertificatesDataImport> listCertificacionesDataImport = new ArrayList<>();
		List<ActivityDataImport> listActividadDataImport = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		ActivityDataImport certificateActivity = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
			certificateActivity = new ActivityDataImport();
			String vcSAGA = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_SAGA.getPosition());
			String vcPartner = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_PARTNER.getPosition());
			String vcCertificado = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CERTIFICADO.getPosition());
			String vcNameGTD = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_NAME_GTD.getPosition());
			String vcCertificationGDT = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CERTIFICATION_GTD.getPosition());
			String vcCode = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CODE.getPosition());
			String vcSector = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_SECTOR.getPosition());
			String vcModulo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_MODULO.getPosition());
			String vcIdCandidato = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ID_CANDIDATO.getPosition());
			Date vcFechaCertificado = utilsService.getDateValue(currentRow,
					Constants.CertificatesDatabasePos.COL_FECHA_CERTIFICADO.getPosition());
			Date vcFechaExpiracion = utilsService.getDateValue(currentRow,
					Constants.CertificatesDatabasePos.COL_FECHA_EXPIRACION.getPosition());
			String vcActivo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ACTIVO.getPosition());
			String vcAnexo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ANEXO.getPosition());
			String vcComentarioAnexo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_COMENTARIO_ANEXO.getPosition());
			String vcGgid = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_GGID.getPosition());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String vcFechaCertificadoStr = (vcFechaCertificado != null) ? dateFormat.format(vcFechaCertificado) : "";

			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(vcFechaCertificadoStr, "Fecha Certificado");
	        noNulables.put(vcSAGA, "SAGA");

		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	rollBackCertificates(verCertificaciones.getId());
                	throw new FileProcessException("La fila " + i + " no contiene el campo obligatorio: " + entry.getValue());
                }
            }

			data.setSAGA(vcSAGA);
			data.setPartner(vcPartner);
			data.setCertificado(vcCertificado);
			data.setNameGTD(vcNameGTD);
			data.setCertificationGTD(vcCertificationGDT);
			data.setCode(vcCode);
			data.setSecto(vcSector);
			data.setModulo(vcModulo);
			data.setIdCandidato(vcIdCandidato);
			data.setFechaCertificado(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			data.setFechaExpiracion(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			data.setActivo(vcActivo);
			data.setAnexo(vcAnexo);
			data.setComentarioAnexo(vcComentarioAnexo);
			data.setNumImportCodeId(verCertificaciones.getId());
			data.setGgid(vcGgid);


			certificateActivity.setgGID(vcGgid);
			certificateActivity.setsAGA(vcSAGA);
			certificateActivity.setPathwayId(vcCode);
			certificateActivity.setPathwayTitle(vcCertificado);
			certificateActivity.setCompletionPercent(100.00);
			certificateActivity.setEnrollmentDate(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			certificateActivity.setCompletedDate(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			certificateActivity.setRecentActivityDate(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			certificateActivity.setObservaciones(vcComentarioAnexo);
			certificateActivity.setEstado("Finalizado");
			certificateActivity.setTypeActivity(7);

			if (!data.getSAGA().isEmpty() &&
				!(data.getCertificado().startsWith(".") || data.getCertificado().startsWith("*"))
				) {
				listCertificacionesDataImport.add(data);
				listActividadDataImport.add(certificateActivity);
			}
			currentRow = sheet.getRow(i);
		}

		if (listCertificacionesDataImport != null && !listCertificacionesDataImport.isEmpty()) {
			saveAllCertificatesDataImport(listCertificacionesDataImport);
			saveActividadDataImport(listActividadDataImport);

			file.setEstado(ESTADO_PROCESADO);
            fileProcessRepository.save(file);
            fileProcessRepository.flush();

		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_CERTIFICATION_FILE);
			CapabilityLogger.logError(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_CERTIFICATION_FILE);
		}

		CapabilityLogger.logDebug("[FileProcessServiceImpl]       processCertificatesDoc >>>>");
		
	}

	private void borrarAntiguos() {
		List<CertificatesVersion> certificacionesVersion = (List<CertificatesVersion>) certificatesVersionRepository
				.findAll().stream().sorted().toList();
		int antiguos = 0, i = 0;
		
		while (i < certificacionesVersion.size() && antiguos <= Constants.KEEP_HISTORICAL) {
			CertificatesVersion vc = certificacionesVersion.get(i);
			if (!utilizadaModeloCapacidad(vc)) {
				antiguos++;
				if (antiguos == Constants.KEEP_HISTORICAL + 1) {
					eliminarCertificacion(vc);
				}
			}
			i++;
		}		
	}

	private boolean utilizadaModeloCapacidad(CertificatesVersion cv) {
		List<ReportVersion> reportVersions = reportVersionRepository.findAll().stream()
				.filter(reportVersion -> reportVersion.getIdVersionCertificaciones() == cv.getId())
				.toList();

		if (reportVersions.isEmpty()) {
			return false;
		}
		else {
			return true;
		}
	}

	private void eliminarCertificacion(CertificatesVersion cv) {
		CapabilityLogger.logInfo("eliminado version certificacion: " + cv.getId());
		//eliminar fichero de S3
		// TODO: eliminar una version, sino borra todo el fichero?
		// TODO: informar que cuando se suben fichero no se guardan versiones sino que machaca el fichero que hay
//		MinioClient minioClient = dataserviceS3.getMinioClient();
//		try {
//            minioClient.removeObject(RemoveObjectArgs.builder()
//                    .bucket(dataserviceS3.getBucketName())
//                    .object(cv.getNombreFichero())
//                    .build());
//        } catch (Exception e) {
//            CapabilityLogger.logError("Error borrando el fichero " + cv.getNombreFichero() + " de S3.");
//            throw new FileProcessException("Error borrando el fichero " + cv.getNombreFichero() + " de S3.");
//        }
		
		//eliminar Certificaciones
		List<CertificatesDataImport> certificacionesBorrar =
				certificatesDataImportRepository.findByNumImportCodeId(cv.getId());
		
		for (CertificatesDataImport cdi : certificacionesBorrar) {
			certificateDataImportRepository.delete(cdi);
		}
		
		//eliminar Certificacion
		certificatesVersionRepository.delete(cv);

	}

	private void processRolsDoc(FileProcess file, InputStream fileDownloaded) {

    	//Aqui la gestion propia del fichero
        //Con el fichero descargado de S3, obtenemos la hoja de Excel
        Sheet sheet = utilsService.obtainSheetFromInputStream(fileDownloaded);
        int numRegistros = sheet.getPhysicalNumberOfRows() - 1;

        //Se crea la version en la tabla de capacidades
        //TODO: Gestion de versiones de los ficheros. Si tenemos ya X versiones guardadas, habrá que eliminar la primera disponible que no se este utilizando en ningun
        // informe
        VersionCapacidades version = null;
        try {
            version = createCapacityVersion(numRegistros, file.getNombreFichero(), file.getNombreFichero(), file.getUsuario(), file.getTipoFichero());
        } catch (Exception e) {
            CapabilityLogger.logError("Error creando la version del fichero de capacidades. Error -> " + e.getMessage());
            throw e;
        }

        List<FormDataImport> roleDataList = null;

        try {
            roleDataList = generateRoleDataList(sheet, version);
        } catch (Exception e) {
            CapabilityLogger.logError("Error obteniendo los datos del fichero. Error -> " + e.getMessage());
            throw e;
        }

        if (roleDataList != null && !roleDataList.isEmpty()) {
            saveAllFormDataImport(roleDataList);

            file.setEstado(ESTADO_PROCESADO);
            fileProcessRepository.save(file);
            fileProcessRepository.flush();
        } else {
            StringBuilder errorData = new StringBuilder();
            errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
                    .append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_ROL_FILE);

            CapabilityLogger.logError(errorData.toString());

            throw new UnprocessableEntityException(Constants.ERROR_EMPTY_ROL_FILE);
        }

	}

	/**
     * Create an save on database CapacityVersion Object
     *
     * @param numReg         num registers on Excel
     * @param fileName       Excell File name
     * @param description    Description
     * @param user           User who uploads data
     * @param idTipointerfaz idTipointerfaz value
     * @return CapacityVersion Object inserted on database
     */
    private VersionCapacidades createCapacityVersion(int numReg, String fileName, String description, String user,
            String idTipointerfaz) {
        VersionCapacidades versionCap = new VersionCapacidades();

        versionCap.setNumRegistros(numReg);
        versionCap.setFechaImportacion(LocalDateTime.now());
        versionCap.setNombreFichero(fileName);
        versionCap.setDescripcion(description);
        versionCap.setUsuario(user);
        if (idTipointerfaz.equals("2")) {
            idTipointerfaz = "Roles";
        }
        versionCap.setIdTipointerfaz(idTipointerfaz);
        versionCap.setFichero(dataserviceS3.getS3Endpoint());

        return versionCapacidadesRepository.save(versionCap);
    }

    private List<FormDataImport> generateRoleDataList(Sheet sheet, VersionCapacidades version) throws FileProcessException {
        List<FormDataImport> rolDataList = new ArrayList<>();
        Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);

        for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
            FormDataImport data = new FormDataImport();

            String vcProfileSAGA = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SAGA.getPosition());
            String vcProfileEmail = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_EMAIL.getPosition());
            String vcProfileName = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_NAME.getPosition());
            String vcProfileRoll1Extendido = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL1_EXTENDIDO.getPosition());
            String vcProfileRoll2EM = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_EM.getPosition());
            String vcProfileRoll2AR = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_AR.getPosition());
            String vcProfileRoll2AN = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_AN.getPosition());
            String vcProfileRoll2SE = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_SE.getPosition());
            String vcProfileRolExperienceEM = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_EM.getPosition());
            String vcProfileRolExperienceAR = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_AR.getPosition());
            String vcProfileRoll3 = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL3.getPosition());
            String vcProfileSkillCloudNativeExperience = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_CLOUD_NATIVE_EXPERIENCE.getPosition());
            String vcProfileSkillLowCodeExperience = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_LOWCODE_EXPERIENCE.getPosition());
            String vcProfileRoll4 = utilsService.getStringValue(currentRow,	Constants.RolsDatabasePos.COL_ROLL4.getPosition());
            String vcProfileSectorExperience = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SECTOR_EXPERIENCE.getPosition());
            String vcProfileSkillCloudExp = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_CLOUD_EXPERIENCE.getPosition());

            Map<String, String> noNulables = new HashMap<>();
            noNulables.put("Saga", vcProfileSAGA);

            Map.Entry<String, String> nullField = noNulables.entrySet().stream()
                    .filter(entry -> entry.getValue() == null || entry.getValue().isEmpty())
                    .findFirst().orElse(null);
            if(nullField != null)
                throw new FileProcessException("La fila " + i + " no contiene el campo obligatorio: " + nullField.getKey());

            data.setSAGA(vcProfileSAGA);
            data.setEmail(vcProfileEmail);
            data.setName(vcProfileName);
            data.setRolL1Extendido(vcProfileRoll1Extendido);
            data.setRolL2EM(vcProfileRoll2EM);
            data.setRolL2AR(vcProfileRoll2AR);
            data.setRolL2AN(vcProfileRoll2AN);
            data.setRolL2SE(vcProfileRoll2SE);
            data.setRolExperienceEM(vcProfileRolExperienceEM);
            data.setRolExperienceAR(vcProfileRolExperienceAR);
            data.setRolL3(vcProfileRoll3);
            data.setRolL4(vcProfileRoll4);
            data.setSkillCloudNativeExperience(vcProfileSkillCloudNativeExperience);
            data.setSkillLowCodeExperience(vcProfileSkillLowCodeExperience);
            data.setSectorExperience(vcProfileSectorExperience);
            data.setSkillCloudExp(vcProfileSkillCloudExp);
            data.setNumImportCodeId(version.getId());
            setRolL1(data);

            rolDataList.add(data);
            currentRow = sheet.getRow(i);
        }

        return rolDataList;
    }

    /**
     * Sets the RolL1 profile value in the {@link FormDataImport} object based on the extended role description.
     *
     * <p>This method evaluates the extended role description from the {@link FormDataImport} object and sets
     * the appropriate RolL1 profile value based on the role description. If the description starts with a specific
     * role title, it sets the corresponding RolL1 profile constant. If the role title does not match any predefined
     * roles, it sets an empty value.</p>
     *
     * @param formDataImport The {@link FormDataImport} object containing the role description and where the RolL1
     *                       profile value will be set.
     */
    private void setRolL1(FormDataImport formDataImport) {
        String rolL1extendido = formDataImport.getRolL1extendido();

        if (rolL1extendido.startsWith("Software Engineer")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_SE);
        } else if (rolL1extendido.startsWith("Business Analyst")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_BA);
        } else if (rolL1extendido.startsWith("Engagement Manager")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_EM);
        } else if (rolL1extendido.startsWith("Architect")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_AR);
        } else {
            formDataImport.setVcProfileRolL1(Constants.EMPTY);
        }
    }

    private void saveAllFormDataImport(List<FormDataImport> formDataImportList) {
        try {
            //return formDataImportRepository.saveAll(formDataImportList);
            IntStream.range(0, (formDataImportList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                    .mapToObj(i -> formDataImportList.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, formDataImportList.size())))
                    .forEach(subList -> {
                        CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
                        formDataImportRepository.saveAll(subList);
                        formDataImportRepository.flush();
                    });
        } catch (Exception e) {
            StringBuilder errorData = new StringBuilder();
            errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
                    .append(Constants.ERROR_INIT2);
            CapabilityLogger.logError(errorData.toString() + e.getMessage());
            String respuestaEx = e.getMessage();
            String  respuesta = respuestaEx.substring(respuestaEx.indexOf('[')+1, respuestaEx.indexOf(']'));
            String respuestaEr = respuesta + ". Error procesando el excel. Comprueba los datos correctos";
            throw new UnprocessableEntityException(respuestaEr);
        }
    }

    /**
	 * Create an save on database CertificationsVersion Object (with
	 * CertificatesDataImport relations)
	 *
	 * @param numReg         num registers on Excel
	 * @param fileName       Excell File name
	 * @param description    Description
	 * @param user           User who uploads data
	 * @param idTipointerfaz idTipointerfaz value
	 * @return CapacityVersion Object inserted on database
	 * @throws IOException
	 */
	private VersionCertificaciones createCertificationesVersion(int numReg, String idTipointerfaz, String fileName, String description, String user) throws IOException {


		VersionCertificaciones versionCer = new VersionCertificaciones();
		if (idTipointerfaz.equals("3")) {
			idTipointerfaz = "Certifications";
		}
		versionCer.setIdTipointerfaz(idTipointerfaz);
		versionCer.setFechaImportacion(LocalDateTime.now());
		versionCer.setNumRegistros(numReg);
		versionCer.setNombreFichero(fileName);
		versionCer.setDescription(description);
		versionCer.setUsuario(user);
		versionCer.setFichero(dataserviceS3.getS3Endpoint());
		// versionCer.setFichero(dto.getFileData().getBytes());
		//		versionCer.setCertificates(setCertificacionesDataImport(versionCer, sheet));

		return versionCertificacionesRepository.save(versionCer);
	}

	private void rollBackCertificates(int id) {

		versionCertificacionesRepository.deleteById((long) id);
	}

	/**
	 * Save list CertificatesDataImport on database
	 *
	 * @param certificatesDataImportList List of objects to save
	 * @return List<CertificatesDataImport>
	 */
	@Transactional
	private void saveAllCertificatesDataImport(List<CertificatesDataImport> certificatesDataImportList) {
		try {
			//return certificatesDataImportRepository.saveAll(certificatesDataImportList);
			IntStream.range(0, (certificatesDataImportList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
            .mapToObj(i -> certificatesDataImportList.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, certificatesDataImportList.size())))
            .forEach(subList -> {
                CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
                certificatesDataImportRepository.saveAll(subList);
                certificatesDataImportRepository.flush();
            });
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEx = e.getMessage();
            String  respuesta = respuestaEx.substring(respuestaEx.indexOf('[')+1, respuestaEx.indexOf(']'));
            String respuestaEr = respuesta + ". Error procesando el excel. Comprueba los datos correctos";
			throw new UnprocessableEntityException(respuestaEr);
		}
	}

	/**
	 * Save list ActivityImport on database
	 *
	 * @param actividadDataImportList List of objects to save
	 * @return List<ActividadDataImport>
	 */
	@Transactional
	private void saveActividadDataImport(List<ActivityDataImport> actividadDataImportList) {
		try {
			IntStream.range(0, (actividadDataImportList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
            .mapToObj(i -> actividadDataImportList.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, actividadDataImportList.size())))
            .forEach(subList -> {
                CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
                
                // nueva forma de procesado: si ya existe una actividad igual (ggid y nombre certificacion)
                // se actualiza (update) la actividad y no se añade duplicada.
                for (ActivityDataImport activity : subList) {
                	List<ActivityDataImport> encontradas = activityDataImportRepository.findIgual(activity.getgGID(), activity.getPathwayTitle());
                	if (!encontradas.isEmpty()) {
                		// ponemos el id de la encontrada en la nueva actividad para forzar un update en bbdd
                		activity.setId(encontradas.get(0).getId());
                	}	
                }
                activityDataImportRepository.saveAll(subList);
                activityDataImportRepository.flush();
            });
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
					.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEx = e.getMessage();
            String  respuesta = respuestaEx.substring(respuestaEx.indexOf('[')+1, respuestaEx.indexOf(']'));
            String respuestaEr = respuesta + ". Error procesando el excel. Comprueba los datos correctos";
			CapabilityLogger.logError(respuestaEr);
			throw new UnprocessableEntityException(respuestaEr);
		}
	}
}
