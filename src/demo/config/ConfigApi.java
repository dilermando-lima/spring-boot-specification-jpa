package demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import demo.filter.FilterRequestResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class ConfigApi {

    public static final String HEADER_NAME_X_ACCOUNT_ID = "X-ACCOUNT-ID";
    public static final String HEADER_NAME_X_USER_ID = "X-USER-ID";
    public static final String HEADER_NAME_X_PROFILE_ID = "X-PROFILE-ID";

    @Bean
    public OpenAPI setGlobalAuthorizationHeaderIntoSwagger() {
      return new OpenAPI().components(
                    new Components()
                        .addSecuritySchemes(
                            HEADER_NAME_X_ACCOUNT_ID, 
                            new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(HEADER_NAME_X_ACCOUNT_ID)
                        )
                        .addSecuritySchemes(
                            HEADER_NAME_X_USER_ID, 
                            new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(HEADER_NAME_X_USER_ID)
                        )
                        .addSecuritySchemes(
                            HEADER_NAME_X_PROFILE_ID, 
                            new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(HEADER_NAME_X_PROFILE_ID)
                        )
                )
                .addSecurityItem(
                    new SecurityRequirement()
                        .addList(HEADER_NAME_X_ACCOUNT_ID)
                        .addList(HEADER_NAME_X_USER_ID)
                        .addList(HEADER_NAME_X_PROFILE_ID)
                );
    }
    
    @Bean
    public WebMvcConfigurer webMvcConfigurer(@Autowired FilterInterceptor filterIntercept){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST","PUT","DELETE","OPTIONS","PATCH")
                        .allowedHeaders("Access-Control-Allow-Headers","X-Requested-With","Authorization","Content-Type","X-File-Name", HEADER_NAME_X_ACCOUNT_ID, HEADER_NAME_X_USER_ID, HEADER_NAME_X_PROFILE_ID)
                        .exposedHeaders("Access-Control-Expose-Headers", "X-File-Name", "Content-Disposition")
                        .allowCredentials(true)
                        .maxAge(3600); 
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry
                    .addInterceptor(filterIntercept)
                    .excludePathPatterns("/swagger*/**");
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new FilterRequestResolver());
            }

        };


    }
}
