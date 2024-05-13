package com.ccsw.capabilitymanager.profile;

import java.io.IOException;
import java.util.List;

import com.ccsw.capabilitymanager.profile.model.ProfileGroup;
import com.ccsw.capabilitymanager.profile.model.ProfileTotal;

import jakarta.servlet.http.HttpServletResponse;

public interface ExportService {

	void writeProfileTotalsToCsv(String id, HttpServletResponse servletResponse);

	void writeProfileTotalsToExcel(String id, HttpServletResponse servletResponse) throws IOException;

	void writeProfileToExcel(String id, HttpServletResponse servletResponse, Long idReport) throws IOException;

	void setProfileGroup(List<ProfileGroup> profileGroup);

	void setProfileTotals(List<ProfileTotal> profileTotals);

	void writeProfileToTemplateExcel(String id, HttpServletResponse servletResponse) throws IOException;

}
