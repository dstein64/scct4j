package scct4j.filters;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scct4j.MyLogger;

public class MyFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            
            // No caching unless explicitly turned on
            // (IE was using cache for requests to, e.g., /items, which was causing problems
            response.setHeader("Cache-Control", "no-store, must-revalidate, private, no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            if ("/".equals(request.getRequestURI())) {
                response.sendRedirect("/app");
                return;
            }
            
            chain.doFilter(request, response);
        } else {
            // ERROR. Don't pass along chain
            MyLogger.log.log(Level.SEVERE, "Unsupported request");
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }

}
