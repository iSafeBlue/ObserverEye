package org.observer.base.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope("prototype")
public @interface Rule {
    String value() default "";

    String name() ;

    Param[] param();
}
