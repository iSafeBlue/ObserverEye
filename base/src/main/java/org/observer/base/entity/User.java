package org.observer.base.entity;

import lombok.Data;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/23 20:31
 */
@Data
public class User {

    private Integer id = 1;
    private String username = "admin";
    private String password = "admin";

}
