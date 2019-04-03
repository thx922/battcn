package com.battcn.framework.security.client;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 带负载均衡的请求
 *
 * @author Levin
 * @since 2019-03-30
 */
@AllArgsConstructor
@Import({ResourceAuthExceptionEntryPoint.class, LoadBalancedRestTemplateAutoConfigurer.class})
public class LoadBalancedResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {


    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        http.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                .ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        registry.anyRequest().authenticated()
                .and().csrf().disable();

    }
}