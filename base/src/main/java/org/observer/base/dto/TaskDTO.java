package org.observer.base.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Entity(name = "Task")
@Data
public class TaskDTO implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer uid;

    private String jobname;

    private String groupname;

    private String noticer;

    private Date date;

}