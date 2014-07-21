/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package im.bci.gamesitekit;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.xml.sax.SAXException;

/**
 *
 * @author devnewton
 */
public class GameSiteKitMain {

    @Option(name = "-t")
    private Path templateDir;

    @Option(name = "-i")
    private Path inputDir;

    @Option(name = "-o")
    private Path outputDir;

    private Configuration freemakerConfiguration;

    public GameSiteKitMain() {

    }

    public void init(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (null == inputDir) {
                inputDir = FileSystems.getDefault().getPath("games/newtonadventure");
            }
            if (null == templateDir) {
                templateDir = FileSystems.getDefault().getPath("template");
            }
            if (null == outputDir) {
                outputDir = FileSystems.getDefault().getPath("target/site");
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("gamesitekit [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println(" Example: gamesitekit " + parser.printExample(OptionHandlerFilter.ALL));
        }
        initFreemarker();
    }

    public static void main(String[] args) throws IOException, TemplateException, SAXException, ParserConfigurationException {
        GameSiteKitMain main = new GameSiteKitMain();
        main.init(args);
        main.build();
    }

    private void initFreemarker() throws IOException {
        freemakerConfiguration = new Configuration();
        freemakerConfiguration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{new FileTemplateLoader(inputDir.toFile()), new FileTemplateLoader(templateDir.toFile())}));
        freemakerConfiguration.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        freemakerConfiguration.setDefaultEncoding("UTF-8");
        freemakerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        freemakerConfiguration.setIncompatibleImprovements(new Version(2, 3, 20));
    }

    private void build() throws IOException, TemplateException, SAXException, ParserConfigurationException {
        HashMap<String, Object> model = new HashMap<>();
        model.put("manifest", freemarker.ext.dom.NodeModel.parse(inputDir.resolve("manifest.xml").toFile()));
        Files.createDirectories(outputDir);
        try (BufferedWriter w = Files.newBufferedWriter(outputDir.resolve("index.html"), Charset.forName("UTF-8"))) {
            freemakerConfiguration.getTemplate("index.ftl").process(model, w);
        }
    }

}
