package org.forerunnerintl.xlate.text;

import java.io.File;
import java.nio.file.Path;

public interface SourceTextReader {
    DocumentText readPath(Path path);

    DocumentText readPath(File docFile);
}
