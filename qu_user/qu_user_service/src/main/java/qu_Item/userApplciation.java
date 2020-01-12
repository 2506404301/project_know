package qu_Item;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("qu_Item.user.mapper")
public class userApplciation {
    public static void main(String[] args) {
        SpringApplication.run(userApplciation.class);
    }
}
