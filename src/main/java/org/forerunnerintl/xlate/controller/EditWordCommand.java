package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.note.TranslationEntry;
import org.forerunnerintl.xlate.text.VerseReference;
import org.forerunnerintl.xlate.text.osis.OsisWord;

import java.util.concurrent.Future;

public class EditWordCommand {
    private CommandType commandType;
    private String altDefinition;
    private int count;
    private String primaryDefinition;
    private String text;
    private Future<TranslationEntry> translationEntryFuture;
    private VerseReference verseReference;
    private OsisWord word;

    public EditWordCommand() {}

    public EditWordCommand(CommandType commandType) {
        this.commandType = commandType;
    }

    @Override
    public String toString() {
        return "EditWordCommand{" +
                "commandType=" + commandType +
                ", altDefinition='" + altDefinition + '\'' +
                ", count='" + count + '\'' +
                ", primaryDefinition='" + primaryDefinition + '\'' +
                ", text='" + text + '\'' +
                ", word=" + word +
                '}';
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getAltDefinition() {
        return altDefinition;
    }

    public void setAltDefinition(String altDefinition) {
        this.altDefinition = altDefinition;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrimaryDefinition() {
        return primaryDefinition;
    }

    public void setPrimaryDefinition(String primaryDefinition) {
        this.primaryDefinition = primaryDefinition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Future<TranslationEntry> getTranslationEntryFuture() {
        return translationEntryFuture;
    }

    public void setTranslationEntryFuture(Future<TranslationEntry> translationEntryFuture) {
        this.translationEntryFuture = translationEntryFuture;
    }

    public VerseReference getVerseReference() {
        return verseReference;
    }

    public void setVerseReference(VerseReference verseReference) {
        this.verseReference = verseReference;
    }

    public OsisWord getWord() {
        return word;
    }

    public void setWord(OsisWord word) {
        this.word = word;
    }

    public enum CommandType {
        AddNote,
        EditText,
        InsertAfter,
        InsertBefore,
        Move,
    }
}
