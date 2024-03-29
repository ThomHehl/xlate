package org.forerunnerintl.xlate.controller;

import com.heavyweightsoftware.util.Pair;
import org.forerunnerintl.xlate.io.ProjectSettingsImpl;
import org.forerunnerintl.xlate.io.XlateSettings;
import org.forerunnerintl.xlate.note.PreferredTranslationFile;
import org.forerunnerintl.xlate.note.TranslationEntry;
import org.forerunnerintl.xlate.text.DocumentText;
import org.forerunnerintl.xlate.text.DocumentWord;
import org.forerunnerintl.xlate.text.SourceTextConverter;
import org.forerunnerintl.xlate.text.TextFormat;
import org.forerunnerintl.xlate.text.osis.*;
import org.forerunnerintl.xlate.ui.MainEditorPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EditorControllerImpl implements EditorController {
    private static final String XML_SUFFIX = ".xml";

    final private MainEditorPane mainEditorPane;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    final private OsisReader osisReader = new OsisReader();
    final private OsisWriter osisWriter = new OsisWriter();
    final private PreferredTranslationFile preferredTranslationFile;
    private Path projectPath;
    private ProjectSettingsImpl projectSettings;
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

        preferredTranslationFile = new PreferredTranslationFile(projectSettings);
    }

    @Override
    public void openProjectDirectory(File dir) {
        projectSettings = new ProjectSettingsImpl();

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
    public void convertSource(ProjectSettingsImpl projectSettings) {
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

    @Override
    public Future<TranslationEntry> getPreferredTranslation(final OsisDocument document, final String key) {
        return executor.submit(() -> {
            String refKey = TranslationEntry.buildKey(document, key);
            TranslationEntry entry = preferredTranslationFile.get(refKey);
            return entry;
        });
    }

    @Override
    public void editDocument(OsisDocument document, EditWordCommand cmd) {
        switch (cmd.getCommandType()) {
            case AddNote:
                break;

            case EditText:
                editText(document, cmd);
                break;

            case InsertAfter:
                insertNextTo(document, cmd, 1);
                break;

            case InsertBefore:
                insertNextTo(document, cmd, 0);
                break;

            case Move:
                moveWord(document, cmd);
                break;
        }

    }

    protected void editText(OsisDocument document, EditWordCommand cmd) {
        OsisWord word = cmd.getWord();
        word.setBodyText(cmd.getText());

        boolean preferredChanged = handlePreferredTranslation(document, cmd);
        if (preferredChanged) {
            updateTranslations(document);
        } else {
            storeDocument(document);
        }
    }

    private void insertNextTo(OsisDocument document, EditWordCommand cmd, int adjustment) {
        OsisWord word = cmd.getWord();
        OsisWord newWord = createAddendum(cmd.getText());
        OsisVerse verse = OsisHelper.getVerse(document, cmd.getVerseReference());
        List<OsisWord> wordList = verse.getOsisWords();
        int index = wordList.indexOf(word) + adjustment;
        wordList.add(index, newWord);

        storeDocument(document);
    }

    private OsisWord createAddendum(String text) {
        OsisWord result = new OsisWord();

        result.setId(DocumentWord.ADDENDUM_ID);
        result.setBodyText(text);

        return result;
    }

    private void moveWord(OsisDocument document, EditWordCommand cmd) {
        OsisWord word = cmd.getWord();
        OsisVerse verse = OsisHelper.getVerse(document, cmd.getVerseReference());
        List<OsisWord> wordList = verse.getOsisWords();
        int index = wordList.indexOf(word);
        wordList.remove(index);

        index += cmd.getCount();
        wordList.add(index, word);

        storeDocument(document);
    }

    private boolean handlePreferredTranslation(OsisDocument document, EditWordCommand cmd) {
        boolean result = false;
        String refKey = cmd.getWord().getLemma();
        refKey = TranslationEntry.buildKey(document, refKey);
        String newPrimary = cmd.getPrimaryDefinition();
        String newAlternate = cmd.getAltDefinition();

        TranslationEntry entry = preferredTranslationFile.get(refKey);
        if (entry == null) {
            if (newPrimary != null && !newPrimary.isEmpty()) {
                entry = new TranslationEntry(refKey, cmd.getPrimaryDefinition(), cmd.getAltDefinition());
                preferredTranslationFile.store(entry);
                result = true;
            }
        } else if (!(newPrimary.equals(entry.getPrimary()) && newAlternate.equals(entry.getAlternatesAsString()))) {
            if (newPrimary != null && !newPrimary.isEmpty()) {
                entry.setPrimary(newPrimary);
                entry.setAlternatesAsString(cmd.getAltDefinition());
                preferredTranslationFile.store(entry);
                result = true;
            }
        }

        return result;
    }

    @Override
    public void storeDocument(OsisDocument document) {
        String bookCode = document.getOsisText().getOsisBook().getOsisId();
        Path textPath = buildTextBath(bookCode);
        osisWriter.writePath(textPath, document);
        mainEditorPane.setOsisDocument(document);
    }

    private void updateTranslations(OsisDocument document) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                for (OsisChapter chapter : document.getOsisText().getOsisBook().getOsisChapters()) {
                    updateTranslations(document, chapter);
                }

                storeDocument(document);
                mainEditorPane.setOsisDocument(document);
            }
        };
        thread.start();
    }

    private void updateTranslations(OsisDocument document, OsisChapter chapter) {
        for (OsisVerse verse : chapter.getOsisVerses()) {
            for (OsisWord osisWord : verse.getOsisWords()) {
                if (OsisWord.DEFAULT_BODY_TEXT.equals(osisWord.getBodyText())) {
                    usePreferredTranslation(document, osisWord);
                }
            }
        }
    }

    private void usePreferredTranslation(OsisDocument document, OsisWord osisWord) {
        String ref = TranslationEntry.buildKey(document, osisWord.getLemma());
        TranslationEntry entry = preferredTranslationFile.get(ref);
        if (entry != null) {
            osisWord.setBodyText(entry.getPrimary());
        }
    }

    private Path buildTextBath(String bookCode) {
        String fileName = bookCode + XML_SUFFIX;
        Path result = Paths.get(projectSettings.getTextDirectory().toFile().getAbsolutePath(), fileName);
        return result;
    }

    private boolean convertNewTestamentSource(ProjectSettingsImpl projectSettings) {
        boolean result = true;
        return result;
    }

    private boolean convertOldTestamentSource(ProjectSettingsImpl projectSettings) {
        boolean result = true;

        Path otSource = projectSettings.getOldTestamentSourceDirectory();
        if (!otSource.toFile().exists()) {
            mainEditorPane.directoryNotFound("Cannot find OT source directory", otSource.toFile().getAbsolutePath());
            result = false;
        }

        if (result) {
            Path textDirectory = projectSettings.getTextDirectory();
            if (!textDirectory.toFile().exists()) {
                textDirectory.toFile().mkdirs();
            }

            result = convertFiles(projectSettings.getOldTestamentSourceFormat(), otSource, textDirectory);
        }

        return result;
    }

    private boolean convertFiles(TextFormat textFormat, Path sourcePath, Path destPath) {
        boolean result;

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

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                mainEditorPane.setCurrentReference(refs[0], refs[1]);
            }
        };
        thread.start();
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
