package com.jxlg.app.annotation;

import org.springframework.stereotype.Repository;

@Repository
public @interface MybatisRepository {
    String value() default  "";
}
