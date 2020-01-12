package qu_Item.order.filter;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import qu_Item.common.util.IdWorker;

@Configuration
@EnableConfigurationProperties(wordedProperites.class)
public class IdwordConfig {

    /**
     * 注入ikwork;
     * @param words
     * @return
     */

    @Bean
    public IdWorker idWorker(wordedProperites words){
        return new IdWorker(words.getWorkerId(),words.getDatacenterId());
    }
}
