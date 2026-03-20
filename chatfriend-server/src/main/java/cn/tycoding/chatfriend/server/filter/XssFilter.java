package cn.tycoding.chatfriend.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    private static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);
            if (values == null) {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = sanitize(values[i]);
            }
            return encodedValues;
        }

        @Override
        public String getParameter(String parameter) {
            return sanitize(super.getParameter(parameter));
        }

        @Override
        public String getHeader(String name) {
            return sanitize(super.getHeader(name));
        }

        private String sanitize(String value) {
            if (value != null) {
                value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
                value = value.replaceAll("'", "&#39;");
                value = value.replaceAll("eval\\((.*)\\)", "");
                value = value.replaceAll("[\"'][\\s]*javascript:(.*)[\"']", "\"\"");
                value = value.replaceAll("script", "");
            }
            return value;
        }
    }
}
