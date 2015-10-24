package ethanp.examples;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Ethan Petuchowski 10/23/15
 *
 * This is what gets hit when you go to path "/test/*"
 *
 * HttpServlet should be subclassed to create an HTTP servlet suitable for a Web site.
 *
 * Servlets handle concurrent requests, so synchronize access to shared resources.
 */
public class ProtocolDebugServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Receives standard HTTP requests from the public service method and dispatches them to
    // the doXXX methods defined in this class.
    //
    // There's no need to override this method.
    //  (we are here to simply & easily capture EVERY method of HTTP request coming in)
    //
    @Override protected void service(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {

        // looks for the parameter in the request
        // returns null if the parameter doesn't exist
        String code = request.getParameter("code");
        if (code != null)
            response.setStatus(Integer.parseInt(code));


        // Get or create the current user session.
        // The session IS lasting between requests from the local Chrome.
        HttpSession session = request.getSession(true);
        if (session.isNew()) {

            // if it's a new user, they deserve a big Cookie
            // (I'm not seeing this cookie show up in the browser though ?)
            response.addCookie(
                new Cookie("bigcookie",
                    "This is a test cookies that was created on "
                        +new Date()+
                        " and is used by the jetty http/2 test servlet.")
            );
        }
        response.setHeader("Custom", "Value");

        // send back some plain-text about the server response
        response.setContentType("text/plain");
        String content = "Hello from Jetty using "+request.getProtocol()+"\n";
        content += "uri="+request.getRequestURI()+"\n";
        content += "session="+session.getId()+(session.isNew() ? "(New)\n" : "\n");
        content += "date="+new Date()+"\n";
        response.setContentLength(content.length());
        response.getOutputStream().print(content);
    }
}
