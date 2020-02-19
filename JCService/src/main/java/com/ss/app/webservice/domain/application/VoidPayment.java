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
@Table(name = "WS_VOID_PAYMENT")
@TableGenerator(name="void_payment", table="WS_HIBERNATE_SEQUENCES",  initialValue=1, allocationSize=1)
public class VoidPayment extends BaseEntity{
	
	private @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="void_payment")
    Long id;
    
    private @Version
    @JsonIgnore
    Long version;

    private String callService;

    private String trackNumber;

    private String merchantId;

    private String reason;

    private String remark;
  
}