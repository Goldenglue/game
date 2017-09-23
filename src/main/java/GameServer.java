import database.services.CharactersService;
import database.services.SessionsService;
import database.services.UserService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import servlets.DuelServlet;
import servlets.FightServlet;
import servlets.LoginServlet;
import servlets.MainMenuServlet;
import servlets.filter.AuthenticationFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class GameServer {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        CharactersService charactersService = new CharactersService();
        SessionsService sessionsService = new SessionsService();

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new LoginServlet(userService, charactersService, sessionsService)), "/login");
        context.addServlet(new ServletHolder(new MainMenuServlet(sessionsService)), "/main");
        context.addServlet(new ServletHolder(new DuelServlet(userService)), "/duel");
        context.addServlet(new ServletHolder(new FightServlet()),"/duel/fight");

        context.setContextPath("/");
        context.setBaseResource(Resource.newResource("src/main/resources"));

        server.setHandler(context);

        ServletHolder defaultHolder = new ServletHolder("default", new DefaultServlet());
        defaultHolder.setInitParameter("dirAllowed", "true");
        context.addServlet(defaultHolder, "/");

        context.addFilter(new FilterHolder(new AuthenticationFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

        server.start();
        server.join();
    }

}
