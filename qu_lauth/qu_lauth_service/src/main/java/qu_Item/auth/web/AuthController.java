package qu_Item.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.auth.config.JwtProperties;
import qu_Item.auth.service.AuthService;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.util.CookieUtils;
import qu_Item.entry.UserInfo;
import qu_Item.utils.JWTUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 校验用户登录
     * @param username
     * @param password
     * @param response
     * @param request
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response,
                                      HttpServletRequest request) {
        //登录;
        String token = authService.login(username, password);
        //写入cookie中
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(),
                token, jwtProperties.getExpire(), true);
        //返会NO_CONTENT;
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户登录状态
     * @param cookie
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("QU_TOKEN_NAME") String cookie,
                                           HttpServletResponse response,
                                           HttpServletRequest request){
        try {
            //解析token,用户着公钥来解密。
            UserInfo info = JWTUtils.getInfoFromToken(cookie, jwtProperties.getPublicKey());

            //避免用户处于使用状态时cookie失效；
            //重新创建token;
            String token = JWTUtils.generateToken(info, jwtProperties.getPrivateKey(),
                    jwtProperties.getExpire());
            //写入cookie中；
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),
                    token,jwtProperties.getExpire(),true);
            //返回给用户;
            return ResponseEntity.ok(info);
        }catch (Exception e){
            System.out.println("e = " + e);
            throw new quException(ExceptionEnum.UNKNOWVERIFY_NOT);
        }


    }

}
