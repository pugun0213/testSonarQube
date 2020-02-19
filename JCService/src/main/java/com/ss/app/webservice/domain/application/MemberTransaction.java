package com.ss.app.webservice.domain.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.ss.app.webservice.domain.BaseEntity;
import javax.persistence.Column;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "WS_MEMBER_TRANSACTION")
public class MemberTransaction extends BaseEntity {
	
	private @Id
    @GeneratedValue(strategy = GenerationType.TABLE )
    Long id;

    @Column(length = 3)
	private String comCode;

	@Column(length=5)
	private String memberClub;

	@Column(name="MEMBER_CARD_ID")
	private String memberCardID;

	@Column(length=3)
	private String transactionType;

	private String promoCode;

	private String referenceDoc;

	private Date referenceDate;

	private BigDecimal netPurchaseAmount;

	private BigDecimal point;

	private String createdBy;

    private @Version
    @JsonIgnore
    Long version;


}

