package com.ccsw.dashboard.dataimport.model;




public class Asset {

	private byte[] content;
	private String contentType;
	public Asset(byte[] content, String contentType) {
		super();
		this.content = content;
		this.contentType = contentType;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
}
