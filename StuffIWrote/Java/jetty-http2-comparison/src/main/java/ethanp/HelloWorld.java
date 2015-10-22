package ethanp;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Ethan Petuchowski 10/21/15
 *
 * This is from the Jetty Docs:
 *  http://www.eclipse.org/jetty/documentation/current/advanced-embedding.html
 */
public class HelloWorld extends AbstractHandler {



    /**
     * Handle a request.
     * @param target      The target of the request - either a URI or a name.
     * @param baseRequest The original unwrapped request object.
     * @param request     The request either as the {@link Request} object or a wrapper of that
     *                    request. The HttpChannel.getCurrentHttpChannel method can be
     *                    used access the Request object if required.
     * @param response    The response as the Response object or a wrapper of that request.
     *                    The HttpChannel.getCurrentHttpChannel() method can be used access
     *                    the Response object if required.
     */
    @Override public void handle(
        String target,
        Request baseRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException, ServletException {
        System.out.println("target: "+target);
        System.out.println("baseRequest:"+baseRequest.toString());
        System.out.println("request: "+request.toString());
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello World</h1>");
        baseRequest.setHandled(true);
        System.out.println("\nresponse:");
        System.out.println("==============");
        System.out.println(response.toString());
        System.out.println("==============\n");
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new HelloWorld());
        server.start();
        server.join();
    }
}
