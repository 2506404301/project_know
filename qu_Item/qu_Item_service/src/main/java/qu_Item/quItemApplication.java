package qu_Item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("qu_Item.Item.mapper")
public class quItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(quItemApplication.class);
    }
}
