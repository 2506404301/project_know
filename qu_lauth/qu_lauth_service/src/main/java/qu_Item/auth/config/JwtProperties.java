package qu_Item.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import qu_Item.utils.RsaUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
/*扫描application.yml中的属性，指定前缀信息。*/
@ConfigurationProperties(prefix = "qu.jwt")
public class JwtProperties {
    private String secret; //密文;
    private String pubKeyPath; //公钥路径;
    private String priKeyPath; //私钥路径;
    private int expire; //过期时长;
    private String CookieName; //cookie的名称;

    private  PublicKey publicKey; //公钥
    private  PrivateKey privateKey; //私钥

    //类实例化后，就去加载公钥和私钥;
    @PostConstruct
    public void init() throws Exception {
        //如果公钥或私钥不存在
        File pubPath = new File(pubKeyPath);
        File priPath = new File(priKeyPath);
        if (!priPath.exists()||!pubPath.exists()){
            //如果不存在，公共和私有文件,生成对应秘钥密文;
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }

        //读取公钥和私钥
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        System.out.println("privateKey = " + privateKey);
    }

}
