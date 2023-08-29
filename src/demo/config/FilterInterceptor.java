package demo.config;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import demo.base.ContextAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterInterceptor  implements HandlerInterceptor {

    @java.lang.annotation.Inherited
    @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
    @java.lang.annotation.Retention( java.lang.annotation.RetentionPolicy.RUNTIME )
    public @interface PublicEndpoint {}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod && !handlerMethod.hasMethodAnnotation(PublicEndpoint.class)) {

            var accountId = request.getHeader(ConfigApi.HEADER_NAME_X_ACCOUNT_ID);
            ExceptionRest.throwForbiddenIF(
                    "Header %s has not been found".formatted(ConfigApi.HEADER_NAME_X_ACCOUNT_ID),
                    accountId == null || accountId.trim().isBlank()
            );

            ContextAccount.accountId(accountId);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ContextAccount.clear();
    }
    
}
