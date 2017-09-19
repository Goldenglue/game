import database.pojos.User;
import database.services.UserService;

public class GameServer {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        User user = new User("Ivan","kekkok");
        int id = userService.addNewUser(user);
        user.setId(id);
        user.setCharacterId(id);

        System.out.println(userService.getByUsername("Ivan"));

        /*ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new LoginServlet(userService)), "/");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("resources");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        server.join();*/
    }
}
