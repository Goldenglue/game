package servlets;

import database.services.CharacterService;
import database.services.UserService;
import database.services.implementations.CharactersServiceImpl;
import database.services.implementations.UserServiceImpl;
import managers.DuelManager;
import pojos.Character;
import pojos.Duel;
import pojos.User;
import templater.PageGenHelper;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FightServlet extends HttpServlet {
    private static final Map<Integer, Duel> ongoingDuels = new HashMap<>();
    private final CharacterService charactersServiceImpl;
    private final UserService userServiceImpl;

    public FightServlet(CharacterService charactersServiceImpl, UserService userServiceImpl) {
        this.charactersServiceImpl = charactersServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get in fight servlet");
        Instant pageGenStart = Instant.now();

        Map<String, Object> pageVariables = new HashMap<>();

        int userId = (int) req.getSession().getAttribute("user");
        int opponentId = (int) req.getSession().getAttribute("opponent");
        int duelId = (int) req.getSession().getAttribute("duelId");
        Duel duel = ongoingDuels.get(duelId);
        if (duel.getStatus()) {
            long secondsAfterStart = duel.secondsAfterStart();
            if (secondsAfterStart <= 5) {
                resp.setIntHeader("Refresh", 1);
                pageVariables.put("waiting", true);
                pageVariables.put("timeBeforeStart", 60 - secondsAfterStart);
            } else {
                pageVariables.put("waiting", false);
            }
            Writer stream = new StringWriter();

            PageGenHelper.putFightStats(userId, opponentId, duel, pageVariables);
            PageGenHelper.getPage("fight.html", stream, pageVariables, pageGenStart, 0, 0);

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(stream.toString());
            return;
        }

        User user = userServiceImpl.getBySession(req.getSession().getId());
        Character character = charactersServiceImpl.get(user.getId());
        duel.addUser(userId, user);
        duel.addCharacter(userId, character);
        duel.addLog(userId, new ArrayList<>());


        //TODO create a somewhat smart way to check if everyone is ready
        AsyncContext context = req.startAsync();
        context.start(() -> {
            while (duel.getCharacters().size() < 2) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            duel.start();
            duel.setReady();
            pageVariables.put("waiting", true);
            pageVariables.put("timeBeforeStart", 60 - duel.secondsAfterStart());
            PageGenHelper.putFightStats(userId, opponentId, duel, pageVariables);

            Writer stream = new StringWriter();
            PageGenHelper.getPage("fight.html", stream, pageVariables, pageGenStart, 2, 0);
            try {
                resp.setIntHeader("Refresh", 1);
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
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
        int userId = (int) req.getSession().getAttribute("user");

        Duel duel = ongoingDuels.get(duelId);
        DuelManager duelManager = duel.getDuelManager();

        if (duelManager.process(userId, opponentId)) {
            pageVariables.put("going", true);
        } else {
            pageVariables.put("going", false);
            boolean isWinner = duelManager.didIWin(userId);
            if (isWinner) {
                userServiceImpl.updateRatingOnWin(duel.getUsers().get(userId).getId());
            } else {
                userServiceImpl.updateRatingOnLose(duel.getUsers().get(userId).getId());
            }
            charactersServiceImpl.updateAfterMatch(duel.getUsers().get(userId).getId());
            pageVariables.put("didIWin", isWinner);


            req.getSession().removeAttribute("duelId");
            req.getSession().removeAttribute("user");
            req.getSession().removeAttribute("opponent");
        }
        pageVariables.put("username", duel.getUsers().get(userId).getUsername());
        PageGenHelper.putFightStats(userId, opponentId, duel, pageVariables);

        Writer stream = new StringWriter();
        PageGenHelper.getPage("fight.html", stream, pageVariables, pageGenStart, 0, 0);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(stream.toString());
    }

    static void createNewDuel(int duelId) {
        ongoingDuels.put(duelId, new Duel());
    }
}
