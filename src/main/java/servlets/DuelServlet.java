package servlets;

import database.services.UserService;
import templater.PageGenHelper;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelServlet extends HttpServlet {
    private final List<HttpServletResponse> polledResponses = new ArrayList<>();
    private final UserService userService;

    public DuelServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        System.out.println("Get in duel servlet");
        Map<String, Object> pageVariables = new HashMap<>();
        Instant getRatingStart = Instant.now();
        int rating = userService.getRating(req.getSession().getId());
        Duration dbCallTime = Duration.between(getRatingStart, Instant.now());
        pageVariables.put("rating", rating);
        pageVariables.put("ready", false);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Writer stream = new StringWriter();
        PageGenHelper.getPage("duel.html", stream, pageVariables, pageGenStart, 1, dbCallTime.toMillis());

        resp.getWriter().println(stream.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();

        System.out.println("post in duel servlet");

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        if (polledResponses.isEmpty()) {
            polledResponses.add(resp);
            Instant getRatingStart = Instant.now();
            int rating = userService.getRating(req.getSession().getId());
            Duration dbCallTime = Duration.between(getRatingStart, Instant.now());

            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("rating",rating);
            pageVariables.put("ready", true);

            Writer stream = new StringWriter();
            PageGenHelper.getPage("duel.html", stream, pageVariables, pageGenStart, 1, dbCallTime.toMillis());

            resp.getWriter().println(stream.toString());
        } else {
            HttpServletResponse opponent = polledResponses.remove(0);

            opponent.setContentType("text/html;charset=utf-8");
            opponent.setStatus(HttpServletResponse.SC_OK);
            opponent.getWriter().println("Fight begins!");

            resp.getWriter().println("Fight begins!");
        }
    }

}
