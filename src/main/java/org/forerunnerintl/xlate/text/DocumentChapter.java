package org.forerunnerintl.xlate.text;

import java.util.List;

public abstract class DocumentChapter {

    public abstract String getUniqueId();

    public abstract void setUniqueId(String uniqueId);

    public abstract List<DocumentVerse> getVerses();

    public abstract void setVerses(List<DocumentVerse> verses);
}
