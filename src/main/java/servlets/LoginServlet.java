package servlets;

import database.pojos.User;
import database.services.CharactersService;
import database.services.SessionsService;
import database.services.UserService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
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
        Instant pageGenStart = Instant.now();
        System.out.println("get in login servlet");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        try {
            if (!sessionsService.isLoggedIn(req.getSession().getId())) {
                Template page = PageGenerator.instance().getPage("index.html");
                Writer stream = new StringWriter();

                Duration pageGenDuration = Duration.between(pageGenStart, Instant.now());
                pageVariables.put("time", pageGenDuration.toMillis());
                pageVariables.put("requests", 1);
                pageVariables.put("requestsTime", 1);

                page.process(pageVariables, stream);

                resp.getWriter().println(stream.toString());
            } else {
                resp.sendRedirect("/main");
            }

        } catch (SQLException | TemplateException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        System.out.println("post in login servlet");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");
        pageVariables.put("requests", 1);
        pageVariables.put("requestsTime", 1);

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
            resp.setStatus(HttpServletResponse.SC_OK);
            Duration time = Duration.between(pageGenStart, Instant.now());
            resp.sendRedirect("/main?time=" + time.toMillis());

        }

        if (user != null && user.getPassword().equals(password)) {
            try {
                sessionsService.add(req.getSession().getId(), user.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            Duration time = Duration.between(pageGenStart, Instant.now());
            //req.setAttribute("time", time.toMillis());
            resp.sendRedirect("/main?time=" + time.toMillis());

        } else {
            pageVariables.put("message", "Wrong password");

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Template page = PageGenerator.instance().getPage("index.html");
            Writer stream = new StringWriter();
            Duration pageGenDuration = Duration.between(pageGenStart, Instant.now());
            pageVariables.put("time", pageGenDuration.toMillis());
            try {
                page.process(pageVariables, stream);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            resp.getWriter().println(stream.toString());
        }
    }

}
