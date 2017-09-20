package servlets;

import database.services.UserService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class MainMenuServlet extends HttpServlet {
    public MainMenuServlet(UserService userService) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.instance().getPage("main.html", new HashMap<>()));
        resp.setStatus(HttpServletResponse.SC_OK);
    }


}
