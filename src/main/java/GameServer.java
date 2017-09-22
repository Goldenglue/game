import database.services.CharactersService;
import database.services.SessionsService;
import database.services.UserService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.DuelServlet;
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


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new LoginServlet(userService, charactersService, sessionsService)), "/login");
        context.addServlet(new ServletHolder(new MainMenuServlet(sessionsService)), "/main");
        context.addServlet(new ServletHolder(new DuelServlet()), "/main/duel");
        context.addFilter(new FilterHolder(new AuthenticationFilter()),"/*", EnumSet.of(DispatcherType.REQUEST));

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/main/resources");
        resourceHandler.setDirectoriesListed(true);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
