package servlets;

import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuelServlet extends HttpServlet {
    private final List<HttpServletResponse> polledResponses = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Get in duel servlet");

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.instance().getPage("duel.html"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post in duel servlet");

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        if (polledResponses.isEmpty()) {
            polledResponses.add(resp);
            resp.getWriter().println("Queue is empty!");
        } else {
            HttpServletResponse opponent = polledResponses.remove(0);

            opponent.setContentType("text/html;charset=utf-8");
            opponent.setStatus(HttpServletResponse.SC_OK);
            opponent.getWriter().println("Fight begins!");

            resp.getWriter().println("Fight begins!");
        }
    }

}
