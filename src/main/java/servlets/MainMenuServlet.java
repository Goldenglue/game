package servlets;

import database.services.SessionsService;
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

public class MainMenuServlet extends HttpServlet {
    private final SessionsService sessionsService;

    public MainMenuServlet(SessionsService sessionsService) {

        this.sessionsService = sessionsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        resp.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = new HashMap<>();

        try {
            Instant serviceCheckStart = Instant.now();
            boolean loggedIn = sessionsService.isLoggedIn(req.getSession().getId());
            Duration dbCallDuration = Duration.between(serviceCheckStart, Instant.now());
            if (!loggedIn) {
                pageVariables.put("message", "");

                resp.sendRedirect("/login");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                String time = req.getParameter("time");
                String db = req.getParameter("db");
                String dbCalls = req.getParameter("dbTime");


                Template page = PageGenerator.instance().getPage("main.html");
                Writer stream = new StringWriter();

                Duration pageGenDuration = Duration.between(pageGenStart, Instant.now());
                pageGenDuration = pageGenDuration.plusMillis(Long.parseLong(time));
                pageVariables.put("time", pageGenDuration.toMillis());
                pageVariables.put("requests", 1 + Long.parseLong(db));
                pageVariables.put("requestsTime", dbCallDuration.toMillis() + Long.parseLong(dbCalls));

                page.process(pageVariables, stream);

                resp.getWriter().println(stream.toString());

            }
        } catch (SQLException | TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");

        try {
            sessionsService.endSession(req.getSession().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/login");
        resp.getWriter().println(PageGenerator.instance().getPage("index.html"));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
