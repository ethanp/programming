package ethanp.examples;

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

        /* since we're not deciding what to do based on the `target`, we're going to do
         * the exact same thing for every page regardless of which path parameter they sent
         * in their HTTP request.
         */

        System.out.println("target: "+target);
        System.out.println("baseRequest:"+baseRequest.toString());
        System.out.println("request: "+request.toString());

        /* this sets the `Content-Type` HEADER in the HTTP response */
        response.setContentType("text/html; charset=utf-8");

        /* this sets the STATUS CODE in the HTTP response */
        response.setStatus(HttpServletResponse.SC_OK);

        /* this adds the text to the BODY of the HTTP response */
        response.getWriter().println("<h1>Hello World</h1>");

        /* I think this means that if we have this handler in a HandlerList, it will stop
         * passing the request to sequential handlers in that list because this one did
         * already handle it.
         */
        baseRequest.setHandled(true);

        /* The `Date` is automatically added as a header because the default `HttpConfiguration`
         * does that. And the default `Server` we created, creates a default `ServerConnector`
         * which creates a default `HttpConnectionFactory`, which creates a default
         * `HttpConfiguration`.
         */

        System.out.println("\nresponse:");
        System.out.println("==============");
        System.out.println(response.toString());
        System.out.println("==============\n");
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        // if we set no handler, it'll just 404 for everything
        server.setHandler(new HelloWorld());

        server.start();
        server.join();
    }
}
