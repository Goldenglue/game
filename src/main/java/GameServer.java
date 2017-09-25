import database.DataSource;
import database.services.CharactersServiceImpl;
import database.services.SessionsServiceImpl;
import database.services.UserServiceImpl;
import org.eclipse.jetty.server.Server;
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
        DataSource source = new DataSource();
        UserServiceImpl userServiceImpl = new UserServiceImpl(source);
        CharactersServiceImpl charactersServiceImpl = new CharactersServiceImpl(source);
        SessionsServiceImpl sessionsServiceImpl = new SessionsServiceImpl(source);

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addFilter(new FilterHolder(new AuthenticationFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

        context.addServlet(new ServletHolder(new LoginServlet(userServiceImpl, charactersServiceImpl, sessionsServiceImpl)), "/login");
        context.addServlet(new ServletHolder(new MainMenuServlet(sessionsServiceImpl)), "/main");
        context.addServlet(new ServletHolder(new DuelServlet(userServiceImpl)), "/duel");
        context.addServlet(new ServletHolder(new FightServlet(charactersServiceImpl, userServiceImpl)), "/duel/fight");

        context.setContextPath("/");
        context.setBaseResource(Resource.newResource("src/main/resources"));

        server.setHandler(context);

        ServletHolder defaultHolder = new ServletHolder("default", new DefaultServlet());
        defaultHolder.setInitParameter("dirAllowed", "true");
        context.addServlet(defaultHolder, "/");

        server.start();
        server.join();
    }
}
