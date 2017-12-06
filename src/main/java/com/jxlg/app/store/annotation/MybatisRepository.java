package com.jxlg.app.store.annotation;

import org.springframework.stereotype.Repository;

@Repository
public @interface MybatisRepository {
    String value() default  "";
}
