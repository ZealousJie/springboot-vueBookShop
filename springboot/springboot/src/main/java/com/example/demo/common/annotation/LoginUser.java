package com.example.demo.common.annotation;

import java.lang.annotation.*;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
