package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.forerunnerintl.xlate.text.SourceTextReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class OsisReader implements SourceTextReader {
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public OsisDocument readPath(Path path) {
        return readPath(path.toFile());
    }

    @Override
    public OsisDocument readPath(File docFile) {
        OsisDocument osisDocument;
        try {
            osisDocument = xmlMapper.readValue(docFile, OsisDocument.class);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return osisDocument;
    }
}
