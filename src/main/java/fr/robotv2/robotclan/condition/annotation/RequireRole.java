package fr.robotv2.robotclan.condition.annotation;

import fr.robotv2.robotclan.flag.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequireRole {
    Role role() default Role.MEMBER;
}
