package com_qust.Filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com_qust.config.FilterProperties;
import com_qust.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import qu_Item.common.util.CookieUtils;
import qu_Item.entry.UserInfo;
import qu_Item.utils.JWTUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        //前置过滤类型;
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //过滤器顺序;
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    //是否拦截
    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request;
        HttpServletRequest request = context.getRequest();

        //获得请求url路径
        String url = request.getRequestURI();

        List<String> allowPath = filterProperties.getAllowPaths();
        System.out.println("allowPath = " + allowPath);

        return !isAllowPath(url,allowPath);//是否过滤;
    }

    private boolean isAllowPath(String url, List<String> allowPath) {
        for (String path : allowPath) {
            if (url.startsWith(path)){
                return true;
            }
        }
        return false;
    }

    //确定拦截后的处理;
    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request;
        HttpServletRequest request = context.getRequest();
        //获取cookie中的token值;
        String cookieValue = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        try {
            //解析cookie
            UserInfo info = JWTUtils.getInfoFromToken(cookieValue, jwtProperties.getPublicKey());

            //用户信息是在里面，但是有时是不用的tomack所以无法贡献域;

        } catch (Exception e) {
            //解析token失败，未登录，拦截
            context.setSendZuulResponse(false);
            //返回状态码
            context.setResponseStatusCode(403);

        }

        return null;

    }
}

