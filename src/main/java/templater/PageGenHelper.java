package templater;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import pojos.Duel;

import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class PageGenHelper {
    public static void getPage(String page, Writer stream, Map<String, Object> pageVariables, Instant pageGenStart, int dbCalls, long dbCallsTime) {
        Template template = PageGenerator.instance().getPage(page);
        pageVariables.put("requests", dbCalls);
        pageVariables.put("requestsTime", dbCallsTime);
        Duration pageGenDuration = Duration.between(pageGenStart, Instant.now());
        pageVariables.put("time", pageGenDuration.toMillis());
        try {
            template.process(pageVariables, stream);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void putFightStats(int userId, int opponentId, Duel duel, Map<String, Object> pageVariables) {
        pageVariables.put("opponent", duel.getUsers().get(opponentId).getUsername());
        pageVariables.put("opponentDmg", duel.getCharacters().get(opponentId).getMaxDamage());
        pageVariables.put("userCurrHealth", duel.getCharacters().get(userId).getCurrentHealth());
        pageVariables.put("userMaxHealth", duel.getCharacters().get(userId).getMaxHealth());
        pageVariables.put("opponentCurrHealth", duel.getCharacters().get(opponentId).getCurrentHealth());
        pageVariables.put("opponentMaxHealth", duel.getCharacters().get(opponentId).getMaxHealth());
        pageVariables.put("logs", duel.getLogs().get(userId));
    }
}
