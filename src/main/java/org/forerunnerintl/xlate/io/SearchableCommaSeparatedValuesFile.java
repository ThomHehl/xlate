package org.forerunnerintl.xlate.io;

import java.io.File;

public class SearchableCommaSeparatedValuesFile {
    public static final String      ID = "ID";

    private int         idIndex = -1;
    private String[]    fields;
    private File        csvFile;

    /**
     Create a PipeSeperatedValuesFile
     @param csvFile the file
     @param fields A list of field names. At least one of the fields must be @ID and the length must be at least two

     */
    public SearchableCommaSeparatedValuesFile(File csvFile, String[] fields) {
        if (csvFile == null) {
            throw new NullPointerException("csvFile must be a valid file.");
        }

        if (fields == null || fields.length < 2) {
            throw new NullPointerException("fields must be a valid field name list.");
        }

        for (int ix = 0; ix < fields.length; ++ix) {
            if (ID.equals(fields[ix])) {
                idIndex = ix;
                break;
            }
        }

        if (idIndex < 0) {
            throw new IllegalArgumentException("One field name must e ID");
        }

        this.csvFile = csvFile;
        this.fields = fields;
    }

}
