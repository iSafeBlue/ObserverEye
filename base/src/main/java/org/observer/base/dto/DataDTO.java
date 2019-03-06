package org.observer.base.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Entity(name = "Data")
@Data
public class DataDTO implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String groupName;

    private Integer tid;

    private String funDesc;

    private String funVal;

    private String result;

    private Date date;

}