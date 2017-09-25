package servlets;

import database.services.SessionsService;
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

public class MainMenuServlet extends HttpServlet {
    private final SessionsService sessionsService;

    public MainMenuServlet(SessionsService sessionsService) {

        this.sessionsService = sessionsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get in main menu");
        Instant pageGenStart = Instant.now();
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();


        String time = req.getParameter("time");
        String db = req.getParameter("db");
        String dbTime = req.getParameter("dbTime");

        Writer stream = new StringWriter();
        if (time != null && db != null && dbTime != null) {
            PageGenHelper.getPage("main.html", stream, pageVariables, pageGenStart.minusMillis(Long.parseLong(time)), Integer.parseInt(db), Long.parseLong(dbTime));
        } else {
            PageGenHelper.getPage("main.html", stream, pageVariables, pageGenStart, 0, 0);
        }
        resp.getWriter().println(stream.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        Instant sessionEndStart = Instant.now();
        sessionsService.endSession(req.getSession().getId());
        Duration dbTime = Duration.between(sessionEndStart, Instant.now());

        req.getSession().invalidate();

        Duration time = Duration.between(pageGenStart, Instant.now());
        StringBuilder redirect = new StringBuilder();

        redirect.append("/login?")
                .append("time=").append(time.toMillis())
                .append("&db=").append(1)
                .append("&dbTime=").append(dbTime.toMillis());
        resp.sendRedirect(redirect.toString());

    }
}
