package qu_Item.cart.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.HandlerInterceptor;
import qu_Item.cart.filter.JwtProperties;
import qu_Item.common.util.CookieUtils;
import qu_Item.entry.UserInfo;
import qu_Item.utils.JWTUtils;
import qu_Item.utils.RsaUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> thread = new ThreadLocal<>();

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }
    //前置拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            try {
                //获取cookie；
                String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
                //进行获取公钥的用户信息;
                UserInfo info = JWTUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
                //存入线程中 由于线程也是共享，且是key-v结构,保证安全性;
                thread.set(info);
                //放行
                return true;
            } catch (Exception e) {
                log.error("|||||您没有权限访问！|||||");
                return false;
            }

    }

    //在controller渲染完后一定要清空数据;
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空线程，释放资源;
        thread.remove();
    }

    public static UserInfo getUser(){
        return thread.get();
    }
}
