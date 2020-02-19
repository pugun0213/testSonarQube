package com.ss.app.webservice.domain.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;






@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
// @EqualsAndHashCode(of = {"id"})
@Table(name = "WS_PAYMENT_DTL")
@TableGenerator(name="payment_dtl", table="WS_HIBERNATE_SEQUENCES",  initialValue=1, allocationSize=1)
public class PaymentDetail {
    
    private @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="payment_dtl")
    Long id;
    
    private @Version
    @JsonIgnore
    Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentHeader")
   private  PaymentHeader paymentHeader;

   private String paymentType;
   private String paymentCode;

   private String cardNo;
   private BigDecimal paidAmount;
   private String currency;
   private BigDecimal exchangeRate;
   private BigDecimal paidCoin;
   private Date exchangeDate;

}