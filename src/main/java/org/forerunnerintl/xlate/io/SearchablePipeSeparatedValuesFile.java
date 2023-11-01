package org.forerunnerintl.xlate.io;

import java.io.File;

public abstract class SearchablePipeSeparatedValuesFile {
    public static final String      ID = "ID";

    private int         idIndex = -1;
    private String[]    fields;
    private File        psvFile;

    /**
     Create a PipeSeperatedValuesFile
     @param psvFile the file
     @param fields A list of field names. At least one of the fields must be @ID and the length must be at least two

     */
    protected SearchablePipeSeparatedValuesFile(File psvFile, String[] fields) {
        if (psvFile == null) {
            throw new NullPointerException("psvFile must be a valid file.");
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

        this.psvFile = psvFile;
        this.psvFile = psvFile;
    }

}
