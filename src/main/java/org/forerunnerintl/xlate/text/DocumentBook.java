package org.forerunnerintl.xlate.text;

import java.util.List;

public abstract class DocumentBook {

    public abstract List<DocumentChapter> getChapters();

    public abstract void setChapters(List<DocumentChapter> chapters);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract String getUniqueID();

    public abstract void setUniqueID(String uniqueID);
}
