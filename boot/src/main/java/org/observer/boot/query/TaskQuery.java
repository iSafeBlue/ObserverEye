package org.observer.boot.query;

import lombok.Data;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/23 19:39
 */
@Data
public class TaskQuery {

    private String pluginname;

    private String jobname;

    private Integer minutes;

    private Integer repeat;

    private String email;
}
