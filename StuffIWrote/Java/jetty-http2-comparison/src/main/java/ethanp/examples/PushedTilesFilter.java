package ethanp.examples;

import org.eclipse.jetty.server.Request;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Ethan Petuchowski 10/23/15
 *
 * Servlet filters can intercept HTTP requests targeted at your web application
 */
public class PushedTilesFilter implements Filter {

    // The servlet container calls the init method exactly once after instantiating the filter
    @Override public void init(FilterConfig filterConfig) throws ServletException {}


    // The doFilter method of the Filter is called by the container each time a
    // request/response pair is passed through the chain due to a client request
    // for a resource at the end of the chain.
    @Override public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        Request baseRequest = Request.getBaseRequest(request);

        // Returns the portion of the request URI that indicates the context of the request.
        // The context path always comes first in a request URI. The path starts with a "/"
        // character but does not end with a "/" character. For servlets in the default (root)
        // context, this method returns "".
        String contextPath = baseRequest.getContextPath();
        System.out.println("context path: "+contextPath);

        // I guess if a response gets pushed, the baseRequest will be isPush
        // to compensate for the fact that there was no _real_ request
        if (baseRequest.isPush() && baseRequest.getRequestURI().contains("tiles")) {

            String uri = baseRequest
                .getRequestURI()
                .replace("tiles", "pushed")
                .substring(contextPath.length());

            request.getRequestDispatcher(uri).forward(request, response);
            return;
        }

        // Causes the next filter in the chain to be invoked, or if the calling filter
        // is the last filter in the chain, causes the resource at the end of the chain
        // to be invoked.
        chain.doFilter(request, response);
    }

    // This method gives the filter an opportunity to clean up any resources that are
    // being held (for example, memory, file handles, threads) and make sure that any
    // persistent state is synchronized with the filter's current state in memory.
    @Override public void destroy() {}
}
