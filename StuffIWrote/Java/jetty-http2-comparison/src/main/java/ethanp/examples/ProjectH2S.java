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
 * I got this from
 * https://github.com/eclipse/jetty.project/blob/bd27e7d2d480949b32ad6abfbda0c24e6042c1e3/examples/embedded/src/main/java/org/eclipse/jetty/embedded/Http2Server.java
 *
 * Then I trimmed it down to do what I need.
 */
public class ProjectH2S {
    public static void main(String... args) throws Exception {
        Server server = new Server();

        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.setResourceBase("src/main/resources/docroot");
        context.addFilter(PushSessionCacheFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(PushedTilesFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.addServlet(new ServletHolder(servlet), "/test/*");
        context.addServlet(DefaultServlet.class, "/").setInitParameter("maxCacheSize", "81920");
        server.setHandler(context);

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setSendXPoweredBy(true);
        http_config.setSendServerVersion(true);

        // HTTP Connector
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config), new HTTP2CServerConnectionFactory(http_config));
        http.setPort(8080);
        server.addConnector(http);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(HttpsServer.keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword(HttpsServer.PASSWORD);
        sslContextFactory.setKeyManagerPassword(HttpsServer.PASSWORD);
        sslContextFactory.setCipherComparator(new HTTP2Cipher.CipherComparator());

        // HTTPS Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(https_config);

        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol(http.getDefaultProtocol());

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
            new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(https_config));
        http2Connector.setPort(8443);
        server.addConnector(http2Connector);

        ALPN.debug = false;

        server.start();
        //server.dumpStdErr();
        server.join();
    }

    public static class PushedTilesFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            Request baseRequest = Request.getBaseRequest(request);

            if (baseRequest.isPush() && baseRequest.getRequestURI().contains("tiles")) {
                String uri = baseRequest.getRequestURI()
                    .replace("tiles", "pushed")
                    .substring(baseRequest.getContextPath().length());
                request.getRequestDispatcher(uri).forward(request, response);
                return;
            }

            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }

    static Servlet servlet = new HttpServlet() {
        private static final long serialVersionUID = 1L;

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String code = request.getParameter("code");
            if (code != null)
                response.setStatus(Integer.parseInt(code));

            HttpSession session = request.getSession(true);
            if (session.isNew()) {
                response.addCookie(
                    new Cookie(
                        "bigcookie",
                        "This is a test cookies that was created on "
                            +new Date()+
                            " and is used by the jetty http/2 test servlet.")
                );
            }
            response.setHeader("Custom", "Value");
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
