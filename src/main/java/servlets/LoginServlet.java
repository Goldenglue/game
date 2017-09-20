package servlets;

import database.pojos.User;
import database.services.CharactersService;
import database.services.UserService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private final UserService userService;
    private final CharactersService charactersService;

    public LoginServlet(UserService userService, CharactersService charactersService) {
        this.userService = userService;
        this.charactersService = charactersService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get request ");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post request");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        User user = userService.getByUsername(username);
        if (user == null && !password.equals("")) {
            System.out.println("Creating new user!");
            try {
                user = new User(username, password);
                user.setId(userService.addNewUser(username, password));
                charactersService.addNewCharacter(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.instance().getPage("main.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("User " + user + " + already exists!");
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.instance().getPage("main.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } else {

            pageVariables.put("message", "Wrong password");
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
