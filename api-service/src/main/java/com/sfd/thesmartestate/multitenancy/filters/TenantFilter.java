package com.sfd.thesmartestate.multitenancy.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfd.thesmartestate.common.dto.ExceptionDto;
import com.sfd.thesmartestate.multitenancy.tenants.TenantClientService;
import com.sfd.thesmartestate.multitenancy.tenants.TenantContext;
import com.sfd.thesmartestate.multitenancy.tenants.TenantException;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kuldeep
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class TenantFilter implements Filter {
    private static final String TENANT_ID_HEADER = "X-Tenant";
    private final TenantClientService tenantService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {

            HttpServletRequest request = (HttpServletRequest) servletRequest;

            if (!"error".equalsIgnoreCase(request.getRequestURI())
                    && !request.getRequestURI().startsWith("/actuator")) {
                log.info("Do filter before processing: Filtering request before it reaches the controller.");
                final String tenantId = request.getHeader(TENANT_ID_HEADER);
                log.info("Executing Tenant Filter");
                if (StringUtils.hasText(tenantId)) {
                    log.info("Validating Tenant " + tenantId);
                    if (!tenantService.isTenantValid(tenantId)) {
                        throw new TenantException("Your tenant id does not exist in our system or you account is suspended, " +
                                "please check with SFD support team.");
                    }
                    log.info("Setting tenant to context");
                    TenantContext.setTenantId(tenantId);
                } else {
                    throw new TenantException("Tenant code is missing, Consider adding X-Tenant in your request.");
                }
                filterChain.doFilter(request, response);
                log.info("Do filter after processing: Intercepting request after the controller has been invoked.");
                log.info("Request complete. Clearing tenant from context");
                TenantContext.clear();
            }
        } catch (TenantException e) {
            // Handle the exception, you can log it or perform other actions
            ObjectMapper mapper = new ObjectMapper();
            log.error("Authorization header is invalid/not found", e);
            ExceptionDto exceptionDto = ExceptionDto.builder().message(e.getMessage()).build();
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpServletResponse.getWriter().write(mapper.writeValueAsString(exceptionDto));

        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

