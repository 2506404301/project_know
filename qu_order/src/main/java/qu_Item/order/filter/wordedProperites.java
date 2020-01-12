package qu_Item.order.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qu.worked")
public class wordedProperites {

    private Long workerId;

    private Long datacenterId;
}
