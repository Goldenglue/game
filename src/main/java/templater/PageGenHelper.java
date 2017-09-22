package templater;

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
}
