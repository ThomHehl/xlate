package org.forerunnerintl.xlate.text;

public abstract class DocumentText {
    private SourceText sourceText;

    public SourceText getSourceText() {
        return sourceText;
    }

    public void setSourceText(SourceText sourceText) {
        this.sourceText = sourceText;
    }
}
