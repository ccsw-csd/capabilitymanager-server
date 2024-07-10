package com.ccsw.capabilitymanager.activitydataimport.model;

import java.util.Date;

public class ActivityDataImportDto {
    private Integer id;
    private String GGID;
    private String SAGA;
    private String estado;
    private Integer pathwayId;
    private String pathwayTitle;
    private Integer totalPathwayContent;
    private Integer completedContent;
    private String completionPercent;
    private Date enrollmentDate;
    private Date recentActivityDate;
    private Date completedDate;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getGGID() {
        return GGID;
    }
    public void setGGID(String gGID) {
        GGID = gGID;
    }
    public String getSAGA() {
        return SAGA;
    }
    public void setSAGA(String sAGA) {
        SAGA = sAGA;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Integer getPathwayId() {
        return pathwayId;
    }
    public void setPathwayId(Integer pathwayId) {
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
    public String getCompletionPercent() {
        return completionPercent;
    }
    public void setCompletionPercent(String completionPercent) {
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
}
