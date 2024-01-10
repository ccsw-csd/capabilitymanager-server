package com.ccsw.dashboard.profile;

import java.io.IOException;
import java.util.List;

import com.ccsw.dashboard.profile.model.ProfileGroup;
import com.ccsw.dashboard.profile.model.ProfileTotal;

import jakarta.servlet.http.HttpServletResponse;

public interface ExportService {

	void writeProfileTotalsToCsv(String id, HttpServletResponse servletResponse);

	void writeProfileTotalsToExcel(String id, HttpServletResponse servletResponse) throws IOException;

	void writeProfileToExcel(String id, HttpServletResponse servletResponse) throws IOException;

	void setProfileGroup(List<ProfileGroup> profileGroup);

	void setProfileTotals(List<ProfileTotal> profileTotals);

	void writeProfileToTemplateExcel(String id, HttpServletResponse servletResponse) throws IOException;

}
