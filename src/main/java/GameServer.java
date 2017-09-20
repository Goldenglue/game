import database.services.CharactersService;
import database.services.UserService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.DuelServlet;
import servlets.LoginServlet;
import servlets.MainMenuServlet;

public class GameServer {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        CharactersService charactersService = new CharactersService();


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new LoginServlet(userService, charactersService)), "/login");
        context.addServlet(new ServletHolder(new MainMenuServlet(userService)), "/main");
        context.addServlet(new ServletHolder(new DuelServlet()), "/main/duel");

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
