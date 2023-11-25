package org.forerunnerintl.xlate.text;

import org.forerunnerintl.xlate.text.osis.OsisReader;
import org.forerunnerintl.xlate.text.osis.OsisWriter;
import org.forerunnerintl.xlate.text.sbl.SblReader;

import javax.print.Doc;
import java.nio.file.Path;

public class SourceTextConverter {
    public static final String DEFAULT_BODY_TEXT = "(...)";

    private SourceTextReader reader;
    private SourceTextWriter writer;

    public static SourceTextConverter getConverter(TextFormat input, TextFormat output) {
        SourceTextReader reader = switch (input) {
            case OSIS -> new OsisReader();
            case SBL -> new SblReader();
            default -> throw new IllegalArgumentException("Invalid input type:" + input);
        };

        SourceTextWriter writer = switch (output) {
            case OSIS -> new OsisWriter();
            default -> throw new IllegalArgumentException("Invalid input type:" + input);
        };

        SourceTextConverter result = new SourceTextConverter(reader, writer);
        return result;
    }

    protected SourceTextConverter(SourceTextReader reader, SourceTextWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void convert(Path input, Path output) {
        DocumentText documentText = reader.readPath(input);
        DocumentText outputText = convertText(documentText);
        writer.writePath(output, outputText);
    }

    private DocumentText convertText(DocumentText documentText) {
        SourceText sourceText = documentText.getSourceText();
        sourceText.clear();;
        sourceText.getDocumentBook().getChapters().parallelStream().forEach(chapter ->
                convertChapter(chapter));
        return documentText;
    }

    private void convertChapter(DocumentChapter chapter) {
        chapter.getVerses().parallelStream().forEach(verse ->
                convertVerse(verse));
    }

    private void convertVerse(DocumentVerse verse) {
        verse.clear();
        verse.getWords().parallelStream().forEach(word ->
                convertWord(word));
    }

    private void convertWord(DocumentWord word) {
        String sourceWord = word.getBodyText();
        word.setSourceWord(sourceWord);
        word.setBodyText(DEFAULT_BODY_TEXT);
    }
}
