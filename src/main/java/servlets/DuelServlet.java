package servlets;

import database.services.UserService;
import database.services.implementations.UserServiceImpl;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DuelServlet extends HttpServlet {
    private AtomicBoolean alone = new AtomicBoolean(false);
    private final UserService userServiceImpl;
    private AtomicInteger duelId = new AtomicInteger(0);

    public DuelServlet(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant pageGenStart = Instant.now();
        Map<String, Object> pageVariables = new HashMap<>();
        Instant getRatingStart = Instant.now();

        int rating = userServiceImpl.getRating(req.getSession().getId());

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
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        alone.set(!alone.get());
        if (alone.get()) {
            duelId.incrementAndGet();
            FightServlet.createNewDuel(duelId.get());
            req.getSession().setAttribute("user", 1);
            req.getSession().setAttribute("opponent", 2);
        } else {
            req.getSession().setAttribute("user", 2);
            req.getSession().setAttribute("opponent", 1);
        }
        req.getSession().setAttribute("duelId", duelId.get());

        AsyncContext context = req.startAsync();
        context.setTimeout(0);
        context.start(() -> {
            while (alone.get()) {
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
    }

}
