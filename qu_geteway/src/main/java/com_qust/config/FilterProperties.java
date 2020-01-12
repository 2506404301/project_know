
package com_qust.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Data
@ConfigurationProperties(prefix = "qu.filter")
public class FilterProperties {
    private  List<String>  allowPaths;
}

