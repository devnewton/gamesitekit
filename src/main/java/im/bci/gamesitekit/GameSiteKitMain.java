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
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
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

    private static final List<Locale> locales = Arrays.asList(Locale.FRENCH, Locale.ENGLISH);

    private Configuration freemakerConfiguration;
    private Path screenshotsInputDir;
    private Path screenshotThumbnailsInputDir;
    private Path screenshotThumbnailsOutputDir;
    private Path screenshotsOutputDir;
    private Path styleInputDir;
    private Path styleOutputDir;
    private Path scriptsInputDir;
    private Path scriptsOutputDir;

    public static final String IMAGE_GLOB = "*.{jpg,jpeg,png,JPEG,JPG,PNG}";

    public GameSiteKitMain() {
    }

    public boolean init(String[] args) throws IOException {
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
            screenshotsOutputDir = outputDir.resolve("screenshots");
            screenshotThumbnailsOutputDir = screenshotsOutputDir.resolve("thumbnails");
            screenshotsInputDir = inputDir.resolve("screenshots");
            screenshotThumbnailsInputDir = screenshotsInputDir.resolve("thumbnails");
            styleInputDir = templateDir.resolve("style");
            styleOutputDir = outputDir.resolve("style");
            scriptsInputDir = templateDir.resolve("scripts");
            scriptsOutputDir = outputDir.resolve("scripts");
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("gamesitekit [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println(" Example: gamesitekit " + parser.printExample(OptionHandlerFilter.ALL));
            return false;
        }
        initFreemarker();
        return true;
    }

    public static void main(String[] args) throws IOException, TemplateException, SAXException, ParserConfigurationException {
        GameSiteKitMain main = new GameSiteKitMain();
        if (main.init(args)) {
            main.cleanUp();
            main.buildResources();
            for (Locale locale : locales) {
                main.buildHtml(locale);
            }
        }
    }

    private void initFreemarker() throws IOException {
        freemakerConfiguration = new Configuration();
        freemakerConfiguration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{new FileTemplateLoader(inputDir.toFile()), new FileTemplateLoader(templateDir.toFile())}));
        freemakerConfiguration.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        freemakerConfiguration.setDefaultEncoding("UTF-8");
        freemakerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        freemakerConfiguration.setIncompatibleImprovements(new Version(2, 3, 20));
    }

    private void buildResources() throws IOException {
        copyPhp();
        copyMedias();
        copyStyle();
        copyScripts();
        copyScreenshots();
    }

    private void copyScreenshots() throws IOException {
        Files.createDirectories(screenshotsOutputDir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(screenshotsInputDir, IMAGE_GLOB)) {
            for (Path image : stream) {
                Files.copy(image, screenshotsOutputDir.resolve(screenshotsInputDir.relativize(image)));
            }
        }
        Files.createDirectories(screenshotThumbnailsOutputDir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(screenshotThumbnailsInputDir, IMAGE_GLOB)) {
            for (Path image : stream) {
                Files.copy(image, screenshotThumbnailsOutputDir.resolve(screenshotThumbnailsInputDir.relativize(image)));
            }
        }
    }

    private void buildHtml(Locale locale) throws SAXException, IOException, TemplateException, ParserConfigurationException {
        Path localeOutputDir = outputDir.resolve(locale.getLanguage());
        Files.createDirectories(localeOutputDir);
        HashMap<String, Object> model = new HashMap<>();
        model.put("screenshots", createScreenshotsMV(localeOutputDir));
        model.put("lastUpdate", new Date());
        freemakerConfiguration.setLocale(locale);
        freemakerConfiguration.addAutoImport("manifest", "manifest.ftl");
        for(String file : Arrays.asList("index", "support", "giftware")) {
            try (BufferedWriter w = Files.newBufferedWriter(localeOutputDir.resolve(file + ".html"), Charset.forName("UTF-8"))) {
                freemakerConfiguration.getTemplate(file + ".ftl").process(model, w);
            }
        }
    }

    private List<ScreenshotMV> createScreenshotsMV(Path localeOutputDir) throws IOException {
        List<ScreenshotMV> screenshots = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(screenshotThumbnailsOutputDir, IMAGE_GLOB)) {
            for (Path thumbnail : stream) {
                Path screenshot = screenshotsOutputDir.resolve(thumbnail.getFileName());
                if (Files.exists(screenshot)) {
                    ScreenshotMV mv = new ScreenshotMV();
                    mv.setFull(localeOutputDir.relativize(screenshot).toString());
                    mv.setThumbnail(localeOutputDir.relativize(thumbnail).toString());
                    screenshots.add(mv);
                }
            }
        }
        Collections.sort(screenshots, new Comparator<ScreenshotMV>() {

            @Override
            public int compare(ScreenshotMV o1, ScreenshotMV o2) {
                return o1.getFull().compareTo(o2.getFull());
            }

        });
        return screenshots;
    }

    private void copyMedias() throws IOException {
        Path mediaOutputDir = outputDir.resolve("media");
        FileUtils.copyDirectory(templateDir.resolve("media").toFile(), mediaOutputDir.toFile());
        FileUtils.copyDirectory(inputDir.resolve("media").toFile(), mediaOutputDir.toFile());
    }

    private void copyStyle() throws IOException {
        FileUtils.copyDirectory(styleInputDir.toFile(), styleOutputDir.toFile());
    }

    private void copyScripts() throws IOException {
        FileUtils.copyDirectory(scriptsInputDir.toFile(), scriptsOutputDir.toFile());
    }

    private void cleanUp() throws IOException {
        FileUtils.deleteDirectory(outputDir.toFile());
        Files.createDirectories(outputDir);
    }

    private void copyPhp() throws IOException {
        Files.copy(templateDir.resolve("lang_selector.php"), outputDir.resolve("index.php"));
    }
}
