package com.ccsw.dashboard.graderole.model;

import java.util.ArrayList;

public class GradeTotal {
	String grade;
	ArrayList<Long> totals;
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public ArrayList<Long> getTotals() {
		return totals;
	}
	public void setTotals(ArrayList<Long> totals) {
		this.totals = totals;
	}

	
}
