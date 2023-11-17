package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.forerunnerintl.xlate.text.SourceTextReader;

import java.io.File;
import java.io.IOException;

public class OsisReader extends SourceTextReader {
    private XmlMapper xmlMapper = new XmlMapper();

    public OsisDocument readFile(File docFile) {
        OsisDocument osisDocument;
        try {
            osisDocument = xmlMapper.readValue(docFile, OsisDocument.class);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return osisDocument;
    }
}
