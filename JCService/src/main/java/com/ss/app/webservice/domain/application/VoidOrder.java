package com.ss.app.webservice.domain.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



import com.ss.app.webservice.domain.BaseEntity;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "WS_VOID_ORDER")
@TableGenerator(name="void_order", table="WS_HIBERNATE_SEQUENCES",  initialValue=1, allocationSize=1)
public class VoidOrder extends BaseEntity{
	
	private @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="void_order")
    Long id;
    
    private @Version
    @JsonIgnore
    Long version;

	private String posTrackNumber;

	private String reason;

	private String comCode;

	private String branchCode;

	private String customerName;

	private String customerGroup;
}