package demo.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import demo.base.ContextAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var accountId = request.getHeader(ConfigApi.HEADER_NAME_X_ACCOUNT_ID);
        ExceptionRest.forbidden(
            "Header %s has not been found".formatted(ConfigApi.HEADER_NAME_X_ACCOUNT_ID), 
            accountId == null || accountId.trim().isBlank()
        );
        ContextAccount.account(accountId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ContextAccount.clear();
    }
    
}
