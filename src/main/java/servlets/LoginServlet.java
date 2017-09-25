package servlets;

import database.services.CharacterService;
import database.services.SessionService;
import database.services.UserService;
import database.services.implementations.CharactersServiceImpl;
import database.services.implementations.SessionsServiceImpl;
import database.services.implementations.UserServiceImpl;
import pojos.User;
import templater.PageGenHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private final UserService userServiceImpl;
    private final CharacterService charactersServiceImpl;
    private final SessionService sessionsServiceImpl;

    public LoginServlet(UserService userServiceImpl, CharacterService charactersServiceImpl, SessionService sessionsServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.charactersServiceImpl = charactersServiceImpl;
        this.sessionsServiceImpl = sessionsServiceImpl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        System.out.println("get in login servlet");

        String time = req.getParameter("time");
        String db = req.getParameter("db");
        String dbTime = req.getParameter("dbTime");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        Writer stream = new StringWriter();
        if (time != null && db != null && dbTime != null) {
            PageGenHelper.getPage("index.html", stream, pageVariables, pageGenStart.minusMillis(Long.parseLong(time)), Integer.parseInt(db), Long.parseLong(dbTime));
        } else {
            PageGenHelper.getPage("index.html", stream, pageVariables, pageGenStart, 0, 0);
        }
        resp.getWriter().println(stream.toString());


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        System.out.println("post in login servlet");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        resp.setContentType("text/html;charset=utf-8");


        Duration dbCallsTime = Duration.ZERO;

        Instant userCallStart = Instant.now();
        User user = userServiceImpl.getByUsername(username);
        dbCallsTime = dbCallsTime.plus(Duration.between(userCallStart, Instant.now()));

        StringBuilder redirect = new StringBuilder().append("/main?");
        if (user == null && !password.equals("")) {
            user = new User(username, password);
            Instant userAddStart = Instant.now();
            int id = userServiceImpl.addNewUser(username, password);
            dbCallsTime = dbCallsTime.plus(Duration.between(userAddStart, Instant.now()));
            user.setId(id);

            Instant charAddStart = Instant.now();
            charactersServiceImpl.addNewCharacter(user);
            dbCallsTime = dbCallsTime.plus(Duration.between(charAddStart, Instant.now()));

            Instant sessionAddStart = Instant.now();
            sessionsServiceImpl.add(req.getSession(true).getId(), user.getId());
            dbCallsTime = dbCallsTime.plus(Duration.between(sessionAddStart, Instant.now()));

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            Duration time = Duration.between(pageGenStart, Instant.now());

            redirect.append("time=").append(time.toMillis())
                    .append("&db=").append(4)
                    .append("&dbTime=").append(dbCallsTime.toMillis());
            resp.sendRedirect(redirect.toString());
            return;
        }

        if (user != null && user.getPassword().equals(password)) {

            Instant sessionAddStart = Instant.now();
            sessionsServiceImpl.add(req.getSession().getId(), user.getId());
            dbCallsTime = dbCallsTime.plus(Duration.between(sessionAddStart, Instant.now()));

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);

            Duration time = Duration.between(pageGenStart, Instant.now());
            redirect.append("time=").append(time.toMillis())
                    .append("&db=").append(2)
                    .append("&dbTime=").append(dbCallsTime.toMillis());

            resp.sendRedirect(redirect.toString());

        } else {
            pageVariables.put("message", "Неверный пароль");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Writer stream = new StringWriter();
            PageGenHelper.getPage("index.html", stream, pageVariables, pageGenStart, 1, dbCallsTime.toMillis());
            resp.getWriter().println(stream.toString());
        }
    }


}
