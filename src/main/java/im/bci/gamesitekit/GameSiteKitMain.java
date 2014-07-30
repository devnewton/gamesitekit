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

    private Locale locale = Locale.FRENCH;

    @Option(name = "-l")
    public void setLocale(String language) {
        this.locale = new Locale(language);
    }

    private Configuration freemakerConfiguration;
    private Path screenshotsInputDir;
    private Path screenshotThumbnailsInputDir;
    private Path screenshotThumbnailsOutputDir;
    private Path screenshotsOutputDir;
    private Path downloadsInputDir;
    private Path downloadsOutputDir;
    private Path styleInputDir;
    private Path styleOutputDir;

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
            downloadsInputDir = inputDir.resolve("downloads");
            downloadsOutputDir = outputDir.resolve("downloads");
            screenshotsOutputDir = outputDir.resolve("screenshots");
            screenshotThumbnailsOutputDir = screenshotsOutputDir.resolve("thumbnails");
            screenshotsInputDir = inputDir.resolve("screenshots");
            screenshotThumbnailsInputDir = screenshotsInputDir.resolve("thumbnails");
            styleInputDir = templateDir.resolve("style");
            styleOutputDir = outputDir.resolve("style");
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
            main.build();
        }
    }

    private void initFreemarker() throws IOException {
        freemakerConfiguration = new Configuration();
        freemakerConfiguration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{new FileTemplateLoader(inputDir.toFile()), new FileTemplateLoader(templateDir.toFile())}));
        freemakerConfiguration.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        freemakerConfiguration.setDefaultEncoding("UTF-8");
        freemakerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        freemakerConfiguration.setIncompatibleImprovements(new Version(2, 3, 20));
        freemakerConfiguration.setLocale(locale);
    }

    private void build() throws IOException, TemplateException, SAXException, ParserConfigurationException {
        FileUtils.deleteDirectory(outputDir.toFile());
        Files.createDirectories(outputDir);
        copyMedias();
        copyStyle();
        copyScreenshots();
        copyDownloads();
        buildHtml();
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

    private void buildHtml() throws SAXException, IOException, TemplateException, ParserConfigurationException {
        HashMap<String, Object> model = new HashMap<>();
        model.put("manifest", freemarker.ext.dom.NodeModel.parse(resolveManifest().toFile()));
        model.put("screenshots", createScreenshotsMV());
        model.put("downloads", createDownloadsMV());
        try (BufferedWriter w = Files.newBufferedWriter(outputDir.resolve("index.html"), Charset.forName("UTF-8"))) {
            freemakerConfiguration.getTemplate("index.ftl").process(model, w);
        }
    }

    private Path resolveManifest() {
        Path manifest = inputDir.resolve("manifest_" + locale.getLanguage() + ".xml");
        if (Files.exists(manifest)) {
            return manifest;
        } else {
            return inputDir.resolve("manifest.xml");
        }
    }

    private List<ScreenshotMV> createScreenshotsMV() throws IOException {
        List<ScreenshotMV> screenshots = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(screenshotThumbnailsOutputDir, IMAGE_GLOB)) {
            for (Path thumbnail : stream) {
                Path screenshot = screenshotsOutputDir.resolve(thumbnail.getFileName());
                if (Files.exists(screenshot)) {
                    ScreenshotMV mv = new ScreenshotMV();
                    mv.setFull(outputDir.relativize(screenshot).toString());
                    mv.setThumbnail(outputDir.relativize(thumbnail).toString());
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

    private void copyDownloads() throws IOException {
        Files.createDirectories(downloadsOutputDir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(downloadsInputDir)) {
            for (Path image : stream) {
                Files.copy(image, downloadsOutputDir.resolve(downloadsInputDir.relativize(image)));
            }
        }
    }

    private List<DownloadsMV> createDownloadsMV() throws IOException {
        List<DownloadsMV> downloads = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(downloadsOutputDir)) {
            for (Path download : stream) {
                DownloadsMV mv = new DownloadsMV();
                mv.setUrl(downloadsOutputDir.relativize(download).toString());
                mv.setFileName(download.getFileName().toString());
                downloads.add(mv);
            }
        }
        Collections.sort(downloads, new Comparator<DownloadsMV>() {

            @Override
            public int compare(DownloadsMV o1, DownloadsMV o2) {
                String ext1 = o1.getFileName().substring(o1.getFileName().indexOf("."));
                String ext2 = o2.getFileName().substring(o2.getFileName().indexOf("."));
                return ext2.compareTo(ext1);
            }

        });
        return downloads;
    }

    private void copyMedias() throws IOException {
        Path mediaOutputDir = outputDir.resolve("media");
        FileUtils.copyDirectory(templateDir.resolve("media").toFile(), mediaOutputDir.toFile());
        FileUtils.copyDirectory(inputDir.resolve("media").toFile(), mediaOutputDir.toFile());
    }

    private void copyStyle() throws IOException {
        FileUtils.copyDirectory(styleInputDir.toFile(), styleOutputDir.toFile());
    }

}
