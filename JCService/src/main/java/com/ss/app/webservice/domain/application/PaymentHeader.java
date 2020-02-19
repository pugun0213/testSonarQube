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
@Table(name = "WS_PAYMENT_HDR")
@TableGenerator(name="payment_hdr", table="WS_HIBERNATE_SEQUENCES",  initialValue=1, allocationSize=1)
public class PaymentHeader extends BaseEntity{
	
	private @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="payment_hdr")
    Long id;
    
    private @Version
    @JsonIgnore
    Long version;

    private String comCode;

    private String branchCode;

    private String merchantId;

    private String payType;


	private String posTrackNumber;

    private String preOrderNumber;

    private String source;

    private String invoiceNo;

    private String productCode;



    private BigDecimal redemption;

    private String memberGroup;

    private String memberId;



    private String trackNumber;


  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paymentHeader")
  private Set<PaymentDetail> paymentDtls = new HashSet<>();

}