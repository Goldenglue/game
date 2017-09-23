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

        int userNum = (int) req.getSession().getAttribute("user");
        int duelId = (int) req.getSession().getAttribute("duelId");
        try {
            User user = userService.getBySession(req.getSession().getId());
            Character character = charactersService.get(user.getId());
            Duel duel = ongoingDuels.get(duelId);
            if (userNum == 1) {
                duel.setUser1(user);
                duel.setCharacter1(character);
            } else {
                duel.setUser2(user);
                duel.setCharacter2(character);
            }
            pageVariables.put("username", user.getUsername());
            pageVariables.put("userDmg", character.getMaxDamage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AsyncContext context = req.startAsync();
        context.start(() -> {
            Duel duel = ongoingDuels.get(duelId);
            while (duel.getCharacter1() == null || duel.getCharacter2() == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            PageGenHelper.putFightStats(userNum, duel, pageVariables);

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
        Duel duel = ongoingDuels.get(duelId);

        int userNum = (int) req.getSession().getAttribute("user");
        Character character2 = duel.getCharacter2();
        Character character1 = duel.getCharacter1();
        if (userNum == 1) {
            character2.setCurrentHealth(character2.getCurrentHealth() - character1.getCurrentDamage());
            pageVariables.put("username", duel.getUser1().getUsername());
            pageVariables.put("userDmg", duel.getCharacter1().getMaxDamage());
        } else {
            character1.setCurrentHealth(character1.getCurrentHealth() - character2.getCurrentDamage());
            pageVariables.put("username", duel.getUser2().getUsername());
            pageVariables.put("userDmg", duel.getCharacter2().getMaxDamage());
        }

        PageGenHelper.putFightStats(userNum, duel, pageVariables);

        Writer stream = new StringWriter();
        PageGenHelper.getPage("fight.html", stream,pageVariables, pageGenStart, 0, 0);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(stream.toString());
    }

    public static void createNewDuel(int duelId) {
        ongoingDuels.put(duelId, new Duel());
    }
}
