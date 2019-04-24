package com.ssm.demo.controller.handler;

import com.ssm.demo.controller.annotation.TokenToUser;
import com.ssm.demo.entity.AdminUser;
import com.ssm.demo.service.AdminUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private AdminUserService adminUserService;

    public TokenToUserMethodArgumentResolver() {
    }

    /**
     * 配置支持的注解
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if(methodParameter.hasParameterAnnotation(TokenToUser.class)){
            return true;
        }
        return false;
    }

    /**
     * 取出token
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if(methodParameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser){ // 从注解中取出数据
            AdminUser user = null;
            String token = nativeWebRequest.getHeader("token");
            if(null != token && !"".equals(token)){
                user = adminUserService.getAdminUserByToken(token);
            }
            return user;
        }
        return null;
    }
    /**
     * 获取post请求中的数据字节流
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)throws IOException{
        int contentLength = request.getContentLength();
        if(contentLength < 0 ){
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i=0;i<contentLength;){
            int readlen = request.getInputStream().read(buffer,i,contentLength - i);
            if(readlen == -1){
                break;
            }
            i += readlen;
        }
        return buffer;
    }
}
