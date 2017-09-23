package templater;

import database.pojos.Duel;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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

    public static void putFightStats(int userNum, Duel duel, Map<String, Object> pageVariables) {
        if (userNum == 1) {
            pageVariables.put("opponent", duel.getUser2().getUsername());
            pageVariables.put("opponentDmg", duel.getCharacter2().getMaxDamage());
            pageVariables.put("userCurrHealth", duel.getCharacter1().getCurrentHealth());
            pageVariables.put("userMaxHealth", duel.getCharacter1().getMaxHealth());
            pageVariables.put("opponentCurrHealth", duel.getCharacter2().getCurrentHealth());
            pageVariables.put("opponentMaxHealth", duel.getCharacter2().getMaxHealth());
        } else {
            pageVariables.put("opponent", duel.getUser1().getUsername());
            pageVariables.put("opponentDmg", duel.getCharacter1().getMaxDamage());
            pageVariables.put("userCurrHealth", duel.getCharacter2().getCurrentHealth());
            pageVariables.put("userMaxHealth", duel.getCharacter2().getMaxHealth());
            pageVariables.put("opponentCurrHealth", duel.getCharacter1().getCurrentHealth());
            pageVariables.put("opponentMaxHealth", duel.getCharacter1().getMaxHealth());
        }
    }
}
