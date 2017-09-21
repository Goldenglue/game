package servlets;

import database.pojos.User;
import database.services.CharactersService;
import database.services.SessionsService;
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
    private final SessionsService sessionsService;

    public LoginServlet(UserService userService, CharactersService charactersService, SessionsService sessionsService) {
        this.userService = userService;
        this.charactersService = charactersService;
        this.sessionsService = sessionsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get in login servlet");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        try {
            if (!sessionsService.isLoggedIn(req.getSession().getId())) {
                resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            } else {
                resp.sendRedirect("/main");
                resp.getWriter().println(PageGenerator.instance().getPage("main.html", pageVariables));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post in login servlet");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        resp.setContentType("text/html;charset=utf-8");

        User user = null;
        try {
            user = userService.getByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null && !password.equals("")) {

            try {
                user = new User(username, password);
                user.setId(userService.addNewUser(username, password));

                charactersService.addNewCharacter(user);
                sessionsService.add(req.getSession().getId(), user.getId());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            resp.setContentType("text/html;charset=utf-8");
            resp.sendRedirect("/main");
            resp.getWriter().println(PageGenerator.instance().getPage("main.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);

            return;
        }

        if (user != null && user.getPassword().equals(password)) {

            try {
                sessionsService.add(req.getSession().getId(), user.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resp.setContentType("text/html;charset=utf-8");
            resp.sendRedirect("/main");
            resp.getWriter().println(PageGenerator.instance().getPage("main.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } else {

            pageVariables.put("message", "Wrong password");

            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
