package com.ss.app.webservice.domain.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.HashSet;

import com.ss.app.webservice.domain.BaseEntity;



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "WS_ORDER_HDR")
@TableGenerator(name="order_hdr", table="HIBERNATE_SEQUENCES",  initialValue=1, allocationSize=1)
public class OrderHeader extends BaseEntity {
	
	private @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="order_hdr")
    Long id;
    
    private @Version
    @JsonIgnore
    Long version;

    private String comCode;

    private String branchCode;

    private String orderType;

    private String salePerson;

    private String customerName;

    private String customerGroup;

    private String addressLine1; 

    private String addressLine2;

    private String subDistrict; 
    
    private String district; 

    private String province; 

    private String zipCode;

    private String telephoneNumber;

    private String taxId;

    private String memberCode;

    private String memberId;

    private String deliveryAddress;

    private String vatCode;

    private String preOrderNumber;

	private String posTrackNumber;

    private BigDecimal grandTotal;

	private BigDecimal paymentTotal;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderHeader")
    private Set<OrderDetail> orderDetails = new HashSet<>();

}