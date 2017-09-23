package templater;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PageGenerator {
    private static final String HTML_DIR = "templates";

    private static PageGenerator pageGenerator;
    private final Configuration cfg;

    public static PageGenerator instance() {
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public Template getPage(String filename) {
        Template template = null;
        try {
            template = cfg.getTemplate(HTML_DIR + "/" + filename);
        } catch (IOException  e) {
            e.printStackTrace();
        }
        return template;
    }

    private PageGenerator() {
        cfg = new Configuration(Configuration.VERSION_2_3_26);
        try {
            cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
