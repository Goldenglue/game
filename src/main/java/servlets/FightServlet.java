package servlets;

import pojos.Character;
import pojos.Duel;
import pojos.User;
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
    private static final Map<Integer, Duel> ongoingDuels = new HashMap<>();
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
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        int userId = (int) req.getSession().getAttribute("user");
        int opponentId = (int) req.getSession().getAttribute("opponent");
        int duelId = (int) req.getSession().getAttribute("duelId");
        try {
            User user = userService.getBySession(req.getSession().getId());
            Character character = charactersService.get(user.getId());
            Duel duel = ongoingDuels.get(duelId);
            duel.addUser(userId, user);
            duel.addCharacter(userId, character);

            pageVariables.put("username", user.getUsername());
            pageVariables.put("userDmg", character.getMaxDamage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AsyncContext context = req.startAsync();
        context.start(() -> {
            Duel duel = ongoingDuels.get(duelId);
            while (duel.getStatus()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            pageVariables.put("going", true);
            PageGenHelper.putFightStats(userId, opponentId,  duel, pageVariables);

            Writer stream = new StringWriter();
            PageGenHelper.getPage("fight.html", stream, pageVariables, pageGenStart, 2, 0);
            try {
                resp.getWriter().println(stream.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            context.complete();
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post in fight servlet");
        Instant pageGenStart = Instant.now();

        Map<String, Object> pageVariables = new HashMap<>();
        int duelId = (int) req.getSession().getAttribute("duelId");
        int opponentId = (int) req.getSession().getAttribute("opponent");
        Duel duel = ongoingDuels.get(duelId);

        int userNum = (int) req.getSession().getAttribute("user");

        PageGenHelper.putFightStats(userNum, opponentId, duel, pageVariables);

        Writer stream = new StringWriter();
        PageGenHelper.getPage("fight.html", stream, pageVariables, pageGenStart, 0, 0);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(stream.toString());
    }

    public static void createNewDuel(int duelId) {
        ongoingDuels.put(duelId, new Duel());
    }
}
