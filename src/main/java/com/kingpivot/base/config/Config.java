package com.kingpivot.base.config;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class Config {
    public static final String ENCODE_KEY = "kpbase";
}
