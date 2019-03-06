package org.observer.base.annotation;

import java.lang.annotation.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/22 11:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface App {

    String packageName();
    String activity();
    String waitActivity();

}
