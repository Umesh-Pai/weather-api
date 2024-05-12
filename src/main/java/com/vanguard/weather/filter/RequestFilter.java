package com.vanguard.weather.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RequestFilter implements Filter {

    private static final String X_CORRELATION_ID_HEADER = "X-CorrelationID";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
        final FilterChain chain) throws ServletException, IOException {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        String correlationId = httpRequest.getHeader(X_CORRELATION_ID_HEADER);

        if (StringUtils.isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        final HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader(X_CORRELATION_ID_HEADER, correlationId);
        chain.doFilter(request, response);
    }
}
