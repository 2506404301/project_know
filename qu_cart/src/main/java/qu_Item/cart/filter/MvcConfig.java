package qu_Item.cart.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import qu_Item.cart.Interceptor.UserInterceptor;

//@Configuration用于定义配置类
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //这样会所有请求都拦截;
        //这里遇到了一个bug,这new UserInterceptor()是本类，但是里面用了springMvc的properties所以，无法生效。
        //故这个EnableConfigurationProperties来在这里引用;
        registry.addInterceptor(new UserInterceptor(jwtProperties)).addPathPatterns("/**");
    }

}
