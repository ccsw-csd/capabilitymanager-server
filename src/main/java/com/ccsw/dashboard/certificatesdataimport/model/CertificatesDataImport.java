package com.ccsw.dashboard.certificatesdataimport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dm_certificatedata_import")
public class CertificatesDataImport implements Comparable<CertificatesDataImport> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	
	@Override
	public int compareTo(CertificatesDataImport o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
