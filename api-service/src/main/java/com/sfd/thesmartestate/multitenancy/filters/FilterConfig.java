package com.sfd.thesmartestate.multitenancy.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kuldeep
 */
@Configuration
public class FilterConfig {

    @Autowired
    private TenantFilter tenantFilter;

    @Bean
    public FilterRegistrationBean<TenantFilter> configureTenantFilter() {
        FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tenantFilter);
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}

