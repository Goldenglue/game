package servlets;

import database.pojos.Character;
import database.pojos.Duel;
import database.pojos.User;
import database.services.CharactersService;
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
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class FightServlet extends HttpServlet {
    private final Map<Integer, Duel> ongoingDuels = new HashMap<>();
    private final CharactersService charactersService;
    private final UserService userService;

    public FightServlet(CharactersService charactersService, UserService userService) {
        this.charactersService = charactersService;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get in fight servlet");
        Instant pageGenStart = Instant.now();

        int duelId = (int) req.getSession().getAttribute("duelId");

        User user = null;
        Character character = null;
        try {
            user = userService.getBySession(req.getSession().getId());
            character = charactersService.get(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!ongoingDuels.containsKey(duelId)) {

            Duel duel = new Duel();
            duel.setUser1(user);
            duel.setCharacter1(character);
            req.getSession().setAttribute("user", 1);
            ongoingDuels.put(duelId, duel);

            AsyncContext context = req.startAsync();
            context.start(() -> {
                while (ongoingDuels.get(duelId).getUser2() == null) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                context.complete();
            });
        } else {
            Duel duel = ongoingDuels.get(duelId);
            duel.setUser2(user);
            duel.setCharacter2(character);
            req.getSession().setAttribute("user", 2);
        }

        Writer stream = new StringWriter();
        PageGenHelper.getPage("fight.html", stream, new HashMap<>(), pageGenStart, 0, 0);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(stream.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getSession().getAttribute("user"));
        Instant pageGenStart = Instant.now();

        Writer stream = new StringWriter();
        PageGenHelper.getPage("fight.html", stream, new HashMap<>(), pageGenStart, 0, 0);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(stream.toString());
    }
}
