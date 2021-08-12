package com.zkml.api.warning.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config")
@Data
@PropertySource(value = "classpath:/config/config.properties")
public class ConfigProperties {
    private String storage;
}
