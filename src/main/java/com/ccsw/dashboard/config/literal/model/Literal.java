package com.ccsw.dashboard.config.literal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aux_literales")
public class Literal  implements Comparable<Literal> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @Column(name="vc_type", nullable = false)
    private String type;
    
    @Column(name="vc_subtype", nullable = false)
    private String subtype;
    
    @Column(name="vc_ord", nullable = false)
    private int ord;
    
    @Column(name="vc_desc", nullable = false)
	public String desc;	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override	
	public int compareTo(Literal o) {
	    if (this.getSubtype() != o.getSubtype()) {
	        return this.getSubtype().compareTo(o.getSubtype());
	    }
	    return Integer.valueOf(this.ord).compareTo(o.getOrd());
	}	
		
}
