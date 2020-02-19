package com.ss.app.webservice.domain.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WS_ORDER_DTL")
// @EqualsAndHashCode(of = {"id"})
@TableGenerator(name="order_dtl", table="WS_HIBERNATE_SEQUENCES", initialValue=1, allocationSize=1)
public class OrderDetail {

	private @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="order_dtl")
    Long id;

    private @Version
    @JsonIgnore
    Long version;

    private String itemCode;

    private String itemName;

    @Column(name = "UOM")
    private String uom;

    private String priceList;

    private Integer orderQuantities;

    private Integer shipQuantities;

    private BigDecimal pricePerUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderHeader")
    OrderHeader orderHeader;


    private String promotionLv1;

    private String promotionLv2;

    private String promotionLv3;

    private String discountLv1;

    private String discountLv2;

    private String discountLv3;

}