package servlets;

import database.services.UserService;
import templater.PageGenHelper;

import javax.servlet.AsyncContext;
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
import java.util.Objects;

public class DuelServlet extends HttpServlet {
    private volatile boolean alone = false;
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
        System.out.println("post in duel servlet");
        Instant pageGenStart = Instant.now();
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        alone = !alone;

        AsyncContext context = req.startAsync();
        context.setTimeout(0);
        context.start(() -> {
            while (alone) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                HttpServletResponse response = (HttpServletResponse) context.getResponse();
                response.sendRedirect("/duel/fight");
                context.complete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        /*if (polledResponses.isEmpty()) {
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

            opponent.sendRedirect("/duel/fight");

            resp.sendRedirect("/duel/fight");
        }*/
    }

}
