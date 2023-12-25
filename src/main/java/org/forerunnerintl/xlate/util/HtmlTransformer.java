package org.forerunnerintl.xlate.util;

import com.heavyweightsoftware.io.RegularExpressionFileFilter;
import org.forerunnerintl.xlate.io.ProjectSettings;
import org.forerunnerintl.xlate.io.ProjectSettingsImpl;
import org.forerunnerintl.xlate.io.XlateSettings;
import org.forerunnerintl.xlate.text.DocumentText;
import org.forerunnerintl.xlate.text.VerseReference;
import org.forerunnerintl.xlate.text.osis.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlTransformer {
    public static final String      CHAPTER_FORMAT_PATTERN = "000";
    public static final String      DIR_NAME_OUTPUT = "html";
    public static final String      HTML_CHAPTER_NEXT = "<a href='$nextChapterNumber$.html'>Chapter $nextChapterNumber$</a>";
    public static final String      HTML_CHAPTER_PREVIOUS = "<a href='$previousChapterNumber$.html'>Chapter $previousChapterNumber$</a>";
    public static final String      HTML_END =
            """
          <p/>
        </body>
        </html>""";
    public static final String      HTML_NA = "&nbsp;";
    public static final String      HTML_NOTE_REF = "<span class=\"noteReference\">$noteReference$</span>";
    public static final String      HTML_NOTE_END = "</div>";
    public static final String      HTML_NOTE_ENTRY = "<p class=\"noteText\"><span class=\"noteId\">$noteId$</span>$noteText$</p>";
    public static final String      HTML_NOTE_START = "<div class=\"noteBox\">";
    public static final String      HTML_PARA = "<p/>";
    public static final String      HTML_START =
            """
        <html>
        <head>
            <title>Forerunner Bible - $bookName$ $chapterNumber$</title>
            <link rel="stylesheet" href="../frb.css">
        <head>
        <body>
            <h1>$bookName$ $chapterNumber$</h1>
            <p/>
             <table style="width:100%">
               <tr>
                 <th style="width:50%;text-align: left;">$previousChapter$</th>
                 <th style="width:50%;text-align: right;">$nextChapter$</th>
               </tr>
             </table>
            <p/>""";
    public static final String      HTML_VERSE_NUM = "<span class=\"verseReference\">$verseNumber$</span>";
    public static final String      VAR_BOOK = "$bookName$";
    public static final String      VAR_CHAPTER = "$chapterNumber$";
    public static final String      VAR_CHAPTER_NEXT = "$nextChapter$";
    public static final String      VAR_CHAPTER_NEXT_NUMBER = "$nextChapterNumber$";
    public static final String      VAR_CHAPTER_PREV = "$previousChapter$";
    public static final String      VAR_CHAPTER_PREV_NUMBER = "$previousChapterNumber$";
    public static final String      VAR_NOTE_ID = "$noteId$";
    public static final String      VAR_NOTE_TEXT = "$noteText$";
    public static final String      VAR_VERSE_NUM = "$verseNumber$";

    private DecimalFormat chapterFormat;
    private OsisReader reader;
    private Path textPath;

    public static void main(String[] args) {
        XlateSettings xlateSettings = new XlateSettings();
        Path projectPath = xlateSettings.getLastProject();
        ProjectSettings projectSettings = new ProjectSettingsImpl();
        projectSettings.setProjectDirectory(projectPath);

        Path outputDir = Paths.get(projectPath.toString(), DIR_NAME_OUTPUT);

        HtmlTransformer htmlTransformer = new HtmlTransformer(projectSettings);
        htmlTransformer.transform(outputDir);
    }

    public HtmlTransformer(ProjectSettings projectSettings) {
        chapterFormat = (DecimalFormat) DecimalFormat.getInstance();
        chapterFormat.applyPattern(CHAPTER_FORMAT_PATTERN);
        reader = new OsisReader();
        textPath = projectSettings.getTextDirectory();
    }

    public void transform(Path outputDir) {
        outputDir.toFile().mkdirs();

        File textDirectory = textPath.toFile();
        RegularExpressionFileFilter filter = new RegularExpressionFileFilter("^Gen.*");
        for (File xmlFile : textDirectory.listFiles(filter)){
            OsisDocument doc = reader.readPath(xmlFile);
            OsisBook osisBook = doc.getOsisText().getOsisBook();
            String osisId = osisBook.getOsisId();
            String bookName = DocumentText.getBookNameForAbbreviation(osisId);
            String dirName = bookName.replace(" ", "-");
            Path bookDir = Paths.get(outputDir.toString(), dirName);
            bookDir.toFile().mkdirs();

            List<OsisChapter> chapters = doc.getOsisText().getOsisBook().getOsisChapters();
            chapters.parallelStream().forEach(osisChapter ->
                    writeChapter(osisChapter, bookDir, bookName, chapters.size()));
        }
    }

    public void writeChapter(OsisChapter chapter, Path outputDir, String bookName, int numChapters) {
        int chapterNum = OsisHelper.getChapterNum(chapter);

        Map<String, String> varMap = buildVarMap(bookName, chapterNum, numChapters);
        String start = replaceVars(HTML_START, varMap);

        BufferedWriter writer = buildWriter(outputDir, chapterNum);
        writeWithNewLine(writer, start);

        List<OsisNote> noteList = new ArrayList<>();
        for (OsisVerse verse: chapter.getOsisVerses()) {
            VerseReference verseRef = OsisHelper.getVerseRef(verse);
            String verseText = OsisHelper.getVerseText(verse, HTML_NOTE_REF);
            String verseNum = HTML_VERSE_NUM.replace(VAR_VERSE_NUM, Integer.toString(verseRef.verse()));
            boolean verseNumberWritten = false;

            int pos;
            while ((pos = verseText.indexOf(HTML_PARA)) >= 0) {
                if (pos == 0) {
                    dumpNotes(writer, noteList);
                    noteList.clear();

                    writeWithNewLine(writer, HTML_PARA);
                    if (!verseNumberWritten) {
                        writeLine(writer, verseNum);
                        verseNumberWritten = true;
                    }

                    verseText = verseText.substring(HTML_PARA.length()).trim();
                } else {
                    String str = verseText.substring(0, pos);
                    writeWithNewLine(writer, str);

                    dumpNotes(writer, noteList);
                    noteList.clear();

                    writeWithNewLine(writer, HTML_PARA);

                    pos += HTML_PARA.length();
                    verseText = verseText.substring(0, pos).trim();
                }
            }
            noteList.addAll(verse.getOsisNotes());

            if (!verseText.isEmpty()) {
                if (!verseNumberWritten) {
                    writeLine(writer, verseNum);
                }

                writeWithNewLine(writer, verseText);
            }
        }

        try {
            writer.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void dumpNotes(BufferedWriter writer, List<OsisNote> noteList) {
        if (noteList.isEmpty()) {
            return;
        }

        writeWithNewLine(writer, HTML_NOTE_START);

        for (OsisNote note : noteList) {
            String noteId = note.getNoteId();
            if (noteId == null || noteId.equals("")) {
                noteId = note.getOsisRef();
            }
            String noteEntry = HTML_NOTE_ENTRY.replace(VAR_NOTE_ID, noteId);
            noteEntry = noteEntry.replace(VAR_NOTE_TEXT, note.getText());
            writeWithNewLine(writer, noteEntry);
        }

        writeWithNewLine(writer, HTML_NOTE_END);
    }

    private void writeLine(BufferedWriter writer, String line) {
        try {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeWithNewLine(BufferedWriter writer, String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedWriter buildWriter(Path outputDir, int chapterNum) {
        String chapterName = chapterFormat.format(chapterNum);
        String filename = chapterName + ".html";
        Path outputPath = Paths.get(outputDir.toString(), filename);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outputPath.toFile()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return writer;
    }

    private Map<String, String> buildVarMap(String bookName, int chapterNum, int numChapters) {
        Map<String, String> result = new HashMap<>();

        result.put(VAR_BOOK, bookName);
        result.put(VAR_CHAPTER, Integer.toString(chapterNum));

        String prev;
        if (chapterNum > 1) {
            int prevNum = chapterNum - 1;
            String str = chapterFormat.format(prevNum);
            prev = HTML_CHAPTER_PREVIOUS.replace(VAR_CHAPTER_PREV_NUMBER, str);
        } else {
            prev = HTML_NA;
        }
        result.put(VAR_CHAPTER_PREV, prev);

        String next;
        if (chapterNum < numChapters) {
            int nextNum = chapterNum + 1;
            String str = chapterFormat.format(nextNum);
            next = HTML_CHAPTER_NEXT.replace(VAR_CHAPTER_NEXT_NUMBER, str);
        } else {
            next = HTML_NA;
        }
        result.put(VAR_CHAPTER_NEXT, next);

        return  result;
    }

    private String replaceVars(String template, Map<String, String> varMap) {
        String result = template;

        for (Map.Entry<String, String>entry : varMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
