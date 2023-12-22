package demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import demo.model.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterInterceptor  implements HandlerInterceptor {

    @Autowired
    private SessionRequest sessionRequest;

    @java.lang.annotation.Inherited
    @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
    @java.lang.annotation.Retention( java.lang.annotation.RetentionPolicy.RUNTIME )
    public @interface PublicEndpoint {}

    @java.lang.annotation.Inherited
    @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
    @java.lang.annotation.Retention( java.lang.annotation.RetentionPolicy.RUNTIME )
    public @interface RequiresProfile {
        UserProfile value();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod && !handlerMethod.hasMethodAnnotation(PublicEndpoint.class)) {

            var requiredProfile = ExceptionRest.getOrThrowInternalServerIFNull(
                        "All non-public endpoint must contains annotation %s".formatted(RequiresProfile.class),
                        handlerMethod.getMethodAnnotation(RequiresProfile.class)
                    ).value();

            fillSessionRequest(request);

            ExceptionRest.throwForbiddenIF(
                "Profile %s is not allowed on %s".formatted(sessionRequest.getUserProfile().getProfile(), requiredProfile),
                !sessionRequest.getUserProfile().isAllowedFor(requiredProfile)
            );
        }
        return true;
    }

    private void fillSessionRequest(HttpServletRequest request){
        sessionRequest.setAccountId(retrieveSessionHeader(ConfigApi.HEADER_NAME_X_ACCOUNT_ID, request));
        sessionRequest.setUserId(retrieveSessionHeader(ConfigApi.HEADER_NAME_X_USER_ID, request));

        var headerUserProfile = retrieveSessionHeader(ConfigApi.HEADER_NAME_X_PROFILE_ID, request);
        var userProfile = ExceptionRest.getOrThrowForbiddenIFNull("userProfile %s is not valid".formatted(headerUserProfile), UserProfile.profileFromString(headerUserProfile));

        sessionRequest.setUserProfile(userProfile);
    }

    private String retrieveSessionHeader(String headerName, HttpServletRequest request){
        var header = request.getHeader(headerName);
        ExceptionRest.throwForbiddenIF(
            "Header %s has not been found".formatted(headerName),
            header == null || header.trim().isBlank()
        );
        return header;
    }
    
}
