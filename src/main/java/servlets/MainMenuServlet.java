package servlets;

import database.services.SessionsService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MainMenuServlet extends HttpServlet {
    private final SessionsService sessionsService;

    public MainMenuServlet(SessionsService sessionsService) {

        this.sessionsService = sessionsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        Map<String, Object> pageVariables = new HashMap<>();

        try {
            if (!sessionsService.isLoggedIn(req.getSession().getId())) {
                pageVariables.put("message", "");

                resp.sendRedirect("/login");
                resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resp.getWriter().println(PageGenerator.instance().getPage("main.html", pageVariables));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (SQLException e) {
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
        resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
