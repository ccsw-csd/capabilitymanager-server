package com.ccsw.capabilitymanager.activitydataimport.model;

import java.util.Date;

public class ActivityDataImportDto {
    private Integer id;
    private String gGID;
    private String sAGA;
    private String estado;
    private String pathwayId;
    private String pathwayTitle;
    private Integer totalPathwayContent;
    private Integer completedContent;
    private Double completionPercent;
    private Date enrollmentDate;
    private Date recentActivityDate;
    private Date completedDate;
    private Integer typeActivity;
    private String observaciones;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getgGID() {
        return gGID;
    }

    public void setgGID(String gGID) {
        this.gGID = gGID;
    }

    public String getsAGA() {
        return sAGA;
    }

    public void setsAGA(String sAGA) {
        this.sAGA = sAGA;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getPathwayId() {
        return pathwayId;
    }
    public void setPathwayId(String pathwayId) {
        this.pathwayId = pathwayId;
    }
    public String getPathwayTitle() {
        return pathwayTitle;
    }
    public void setPathwayTitle(String pathwayTitle) {
        this.pathwayTitle = pathwayTitle;
    }
    public Integer getTotalPathwayContent() {
        return totalPathwayContent;
    }
    public void setTotalPathwayContent(Integer totalPathwayContent) {
        this.totalPathwayContent = totalPathwayContent;
    }
    public Integer getCompletedContent() {
        return completedContent;
    }
    public void setCompletedContent(Integer completedContent) {
        this.completedContent = completedContent;
    }
    public Double getCompletionPercent() {
        return completionPercent;
    }
    public void setCompletionPercent(Double completionPercent) {
        this.completionPercent = completionPercent;
    }
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    public Date getRecentActivityDate() {
        return recentActivityDate;
    }
    public void setRecentActivityDate(Date recentActivityDate) {
        this.recentActivityDate = recentActivityDate;
    }
    public Date getCompletedDate() {
        return completedDate;
    }
    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Integer getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(Integer typeActivity) {
        this.typeActivity = typeActivity;
    }
}
