package qu_Item.auth.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import qu_Item.auth.client.AuthClient;
import qu_Item.auth.config.JwtProperties;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.entry.UserInfo;

import qu_Item.unity.User;
import qu_Item.utils.JWTUtils;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录接口实现;
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        try {
                User user = authClient.queryUserByUserNameAndPassword(username, password);
                if (user == null) {
                    throw new quException(ExceptionEnum.ERROR_USERNAME_PASSWORD);
                }
                //生成对应的token;进行私钥加密,(公钥和私钥都可以解密)
                System.out.println("jwtProperties = " + jwtProperties.getExpire());
                 String token = JWTUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                 System.out.println("token = " + token);
               return token;
          } catch (Exception e) {
                log.error("生成token失败",e);
                throw new quException(ExceptionEnum.CREATED_TOKEN_ERROR);
         }

    }
}
