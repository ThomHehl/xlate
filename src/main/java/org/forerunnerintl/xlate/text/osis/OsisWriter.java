package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.forerunnerintl.xlate.text.DocumentText;
import org.forerunnerintl.xlate.text.SourceTextWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class OsisWriter implements SourceTextWriter {
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public void writeFile(File file, DocumentText text) {
        try {
            xmlMapper.writeValue(file, text);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public void writePath(Path path, DocumentText text) {
        writeFile(path.toFile(), text);
    }
}
