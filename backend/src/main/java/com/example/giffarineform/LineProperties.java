package com.example.giffarineform;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "line")
@Configuration
@Getter
@Setter
public class LineProperties {
private  String uri;
private  String keyChannel;
private String uploadDir;
}
