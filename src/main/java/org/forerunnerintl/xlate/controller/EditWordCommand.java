package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.text.osis.OsisWord;

public class EditWordCommand {
    private CommandType commandType;
    private String altDefinition;
    private String primaryDefinition;
    private String text;
    private OsisWord word;

    @Override
    public String toString() {
        return "EditWordCommand{" +
                "commandType=" + commandType +
                ", altDefinition='" + altDefinition + '\'' +
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

    public OsisWord getWord() {
        return word;
    }

    public void setWord(OsisWord word) {
        this.word = word;
    }

    public enum CommandType {
        EditText,
        InsertAfter,
        InsertBefore
    }
}
