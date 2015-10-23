package ethanp.examples;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NegotiatingServerConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.PushSessionCacheFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.EnumSet;

/**
 * Ethan Petuchowski 10/23/15
 * <p>
 * I got this from https://github.com/eclipse/jetty.project/blob/bd27e7d2d480949b32ad6abfbda0c24e6042c1e3/examples/embedded/src/main/java/org/eclipse/jetty/embedded/Http2Server.java
 * <p>
 * Then I trimmed it down to do what I need.
 */
public class ProjectH2S {
    public static void main(String... args) throws Exception {
        Server server = new Server();

        /* An MBean is a managed Java object, similar to a JavaBeans component, that follows
         * the design patterns set forth in the JMX specification.
         *
         * An MBean can represent a device, an application, or any resource that needs to be managed.
         *
         * MBeans can also emit notifications when certain predefined events occur.
         */
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);

        // for all routes on SESSIONs, use this handler
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        // serve static content from this path (works!)
        context.setResourceBase("src/main/resources/docroot");

        // create an EnumSet only containing REQUEST
        // but not containing FORWARD, INCLUDE, ASYNC, or ERROR
        EnumSet<DispatcherType> requestEnumSet = EnumSet.of(DispatcherType.REQUEST);

        // add the "optimized server-push from cache" filter to all REQUESTs
        context.addFilter(PushSessionCacheFilter.class, "/*", requestEnumSet);

        // Servlet filters can intercept HTTP requests targeted at your web application
        context.addFilter(PushedTilesFilter.class, "/*", requestEnumSet);

        // This object will organize the loading of the servlet when needed or requested.
        ServletHolder servlet = new ServletHolder(ProjectH2S.servlet);

        context.addServlet(servlet, "/test/*");

        // This servlet, normally mapped to /,
        // provides the handling for static content,
        // as well as the OPTION and TRACE methods,
        // for the context.
        Class<DefaultServlet> defaultServlet = DefaultServlet.class;

        context.addServlet(defaultServlet, "/").setInitParameter("maxCacheSize", "81920");

        // make this ContextHandler the only handler for this server
        server.setHandler(context);

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecurePort(8443);

        // send the X-Powered-By header in responses
        http_config.setSendXPoweredBy(true);

        // send the Server header in responses
        http_config.setSendServerVersion(true);

        // HTTP Connector
        ServerConnector http = new ServerConnector(
            server,
            new HttpConnectionFactory(http_config),
            new HTTP2CServerConnectionFactory(http_config)
        );
        http.setPort(8080);
        server.addConnector(http);

        // A Connection factory is responsible for instantiating and configuring
        // a Connection instance to handle an EndPoint accepted by a Connector.
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(HttpsServer.keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword(HttpsServer.PASSWORD);
        sslContextFactory.setKeyManagerPassword(HttpsServer.PASSWORD);
        sslContextFactory.setCipherComparator(new HTTP2Cipher.CipherComparator());

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(http_config);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConfig);

        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol(http.getDefaultProtocol());

        SslConnectionFactory ssl = new SslConnectionFactory(
            sslContextFactory,
            alpn.getProtocol()
        );

        ServerConnector http2Connector = new ServerConnector(server,
            ssl,
            alpn,
            h2,
            new HttpConnectionFactory(httpsConfig)
        );
        http2Connector.setPort(8443);
        server.addConnector(http2Connector);

        ALPN.debug = false;

        server.start();
        //server.dumpStdErr();
        server.join();
    }

    // Servlet filters can intercept HTTP requests targeted at your web application
    public static class PushedTilesFilter implements Filter {

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

    // This is what gets hit when you go to path "/test/*"
    //
    // HttpServlet should be subclassed to create an HTTP servlet suitable for a Web site.
    //
    // Servlets handle concurrent requests, so synchronize access to shared resources.
    //
    static Servlet servlet = new HttpServlet() {
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
    };

}
