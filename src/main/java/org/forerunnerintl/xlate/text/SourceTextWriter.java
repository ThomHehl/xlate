package org.forerunnerintl.xlate.text;

import java.io.File;
import java.nio.file.Path;

public interface SourceTextWriter {
    void writeFile(File file, DocumentText text);
    void writePath(Path path, DocumentText text);
}
