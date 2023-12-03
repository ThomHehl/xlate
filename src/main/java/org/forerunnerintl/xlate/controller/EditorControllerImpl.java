package org.forerunnerintl.xlate.controller;

import com.heavyweightsoftware.util.Pair;
import org.forerunnerintl.xlate.io.ProjectSettings;
import org.forerunnerintl.xlate.io.XlateSettings;
import org.forerunnerintl.xlate.text.DocumentText;
import org.forerunnerintl.xlate.text.SourceTextConverter;
import org.forerunnerintl.xlate.text.TextFormat;
import org.forerunnerintl.xlate.text.osis.OsisDocument;
import org.forerunnerintl.xlate.text.osis.OsisReader;
import org.forerunnerintl.xlate.text.osis.OsisWriter;
import org.forerunnerintl.xlate.ui.MainEditorPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EditorControllerImpl implements EditorController {
    private static final String XML_SUFFIX = ".xml";

    final private MainEditorPane mainEditorPane;
    final private OsisReader osisReader = new OsisReader();
    final private OsisWriter osisWriter = new OsisWriter();
    private Path projectPath;
    private ProjectSettings projectSettings;
    final private XlateSettings xlateSettings;

    public EditorControllerImpl(MainEditorPane pane) {
        mainEditorPane = pane;
        xlateSettings = new XlateSettings();
        Path settingsPath = xlateSettings.getLastProject();
        if (settingsPath.toString().isEmpty()) {
            mainEditorPane.openProject();
        } else {
            Path projectPath = xlateSettings.getLastProject();
            openProjectDirectory(projectPath.toFile());
        }
    }

    @Override
    public void openProjectDirectory(File dir) {
        projectSettings = new ProjectSettings();

        Path dirPath = Paths.get(dir.toURI());
        if (projectSettings.setProjectDirectory(dirPath)) {
            projectPath = dirPath;
            xlateSettings.setLastProject(projectPath);
            xlateSettings.saveSettings();
            loadProject();
        } else {
            mainEditorPane.handleNoProject(dir);
        }
    }

    @Override
    public void convertSource(ProjectSettings projectSettings) {
        if (convertOldTestamentSource(projectSettings)) {
            convertNewTestamentSource(projectSettings);
        }
    }

    @Override
    public void loadBook(String bookCode) {
        Path textPath = buildTextBath(bookCode);
        OsisDocument osisDocument = osisReader.readPath(textPath);
        mainEditorPane.setOsisDocument(osisDocument);
    }

    private Path buildTextBath(String bookCode) {
        String fileName = bookCode + XML_SUFFIX;
        Path result = Paths.get(projectSettings.getTextDirectory().toFile().getAbsolutePath(), fileName);
        return result;
    }

    private boolean convertNewTestamentSource(ProjectSettings projectSettings) {
        boolean result = true;
        return result;
    }

    private boolean convertOldTestamentSource(ProjectSettings projectSettings) {
        boolean result = true;

        Path otSource = projectSettings.getOldTestamentSourceDirectory();
        if (!otSource.toFile().exists()) {
            mainEditorPane.directoryNotFound("Cannot find OT source directory", otSource.toFile().getAbsolutePath());
            result = false;
        }

        Path textDirectory = projectSettings.getTextDirectory();
        if (!textDirectory.toFile().exists()) {
            textDirectory.toFile().mkdirs();
        }

        result = convertFiles(projectSettings.getOldTestamentSourceFormat(), otSource, textDirectory);

        return result;
    }

    private boolean convertFiles(TextFormat textFormat, Path sourcePath, Path destPath) {
        boolean result = false;

        SourceTextConverter converter = SourceTextConverter.getConverter(textFormat, TextFormat.OSIS);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath)) {
            String msg;

            for (Path inputPath : stream) {
                if (!Files.isDirectory(inputPath)) {
                    msg = "Converting: " + inputPath.toAbsolutePath();
                    mainEditorPane.setStatusText(msg);
                    Path fileName = inputPath.getFileName();
                    Path outputPath = Paths.get(destPath.toString(), fileName.toString());
                    converter.convert(inputPath, outputPath);
                }
            }

            msg = "Conversion Completed";
            mainEditorPane.setStatusText(msg);

            result = true;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return result;
    }

    @Override
    public void createProject(File projectDir) {
        mainEditorPane.newProject(projectSettings);
    }

    private void loadProject() {
        List<Pair<String, String>> books = buildBookList();
        mainEditorPane.setBooks(books);

        String ref = projectSettings.getLastReferenceLocation();
        String[] refs = splitReference(ref);
        mainEditorPane.setCurrentReference(refs[0], refs[1]);
    }

    public static String[] splitReference(String reference) {
        String[] result = new String[2];

        int pos = reference.lastIndexOf(' ');
        if (pos > 0) {
            String book = reference.substring(0, pos);
            book = book.trim();
            result[0] = book;

            String num = reference.substring(pos + 1);
            num = num.trim();
            result[1] = num;
        }

        return result;
    }

    public static List<Pair<String, String>> buildBookList() {
        List<Pair<String, String>> result = new ArrayList<>(DocumentText.BOOK_NAMES.length);

        for (int ix = 0; ix < DocumentText.BOOK_NAMES.length; ++ix) {
            Pair<String, String> pair = new Pair<>(DocumentText.BOOK_ABBREVIATIONS[ix], DocumentText.BOOK_NAMES[ix]);
            result.add(pair);
        }

        return result;
    }
}
