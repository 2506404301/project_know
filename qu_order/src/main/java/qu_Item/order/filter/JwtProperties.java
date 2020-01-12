
package qu_Item.order.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import qu_Item.utils.RsaUtils;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "qu.jwt")
public class JwtProperties {

    private String pubKeyPath;
    private String CookieName;

    private  PublicKey publicKey; //公钥


    //类实例化后，就去加载公钥和私钥;
    @PostConstruct
    public void init() throws Exception {
        //读取公钥和私钥
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);

    }

}

