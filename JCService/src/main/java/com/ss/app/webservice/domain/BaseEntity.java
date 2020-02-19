package com.ss.app.webservice.domain;


import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import java.util.Date;
import javax.persistence.Column;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    private String source;

    private String errorCode;

    @Column(name = "ERROR_MSG")
    private String errorMsg;

    private Date createdDate;

    private Date updateDate;

    private Integer infStatus;

}
