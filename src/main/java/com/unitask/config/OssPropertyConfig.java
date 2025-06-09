package com.unitask.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.app.oss")
public class OssPropertyConfig {

    private String endpoint = "https://.com";

    private String region = "";

    private Boolean pathStyleAccess = true;

    private String accessKey="";

    private String secretKey="";

    private Integer maxConnections = 100;

    private String bucket="";

    private Integer expireHour=0;
}
