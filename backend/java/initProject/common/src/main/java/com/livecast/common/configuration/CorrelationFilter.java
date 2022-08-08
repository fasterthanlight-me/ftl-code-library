package com.livecast.common.configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(0)
public class CorrelationFilter implements Filter {
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    public static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String correlationId = getCorrelationIdFromHeader((HttpServletRequest) servletRequest);
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
                httpServletResponse
        );
        responseWrapper.setHeader(CORRELATION_ID_HEADER_NAME, correlationId);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public static String getCorrelationIdFromHeader(final HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
        if (StringUtils.isBlank(correlationId)) {
            correlationId = generateUniqueCorrelationId();
        }
        return correlationId;
    }

    private static String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
