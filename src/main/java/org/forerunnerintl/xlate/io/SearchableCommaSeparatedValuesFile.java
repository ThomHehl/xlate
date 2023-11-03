package org.forerunnerintl.xlate.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SearchableCommaSeparatedValuesFile {
    public static final int         BUFFER_SIZE = 80;

    public static final String      ID = "ID";
    public static final String      QUOTE = "\"";
    public static final char        QUOTE_CHAR = QUOTE.charAt(0);
    public static final String      SEP = ",";
    public static final char        SEP_CHAR = SEP.charAt(0);

    private int         idIndex = -1;
    private String[]    fields;
    private Path        csvFile;

    /**
     Create a PipeSeperatedValuesFile
     @param csvFile the file
     @param fields A list of field names. At least one of the fields must be @ID and the length must be at least two
     */
    public SearchableCommaSeparatedValuesFile(Path csvFile, String[] fields) {
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

    /**
     * Search the file for an ID
     * @param idValue the ID being searched for
     * @return the matching record or an array of nulls of the proper length. Too long records will be truncated and too
     *          short will return nulls for the missing fields
     */
    public String[] searchById(String idValue) {
        String[] result = new String[fields.length];

        try {
            FileChannel fileChannel = FileChannel.open(csvFile, StandardOpenOption.READ);
            String[] answer = searchFileForId(fileChannel, idValue);
            int count = result.length < answer.length || answer.length == 0
                    ? answer.length
                    : result.length;
            for (int ix = 0; ix < count; ++ix) {
                result[ix] = answer[ix];
            }
        } catch (IOException ioe) {
            String msg = "Error reading:" + csvFile.toAbsolutePath();
            throw new RuntimeException(msg, ioe);
        }

        return result;
    }

    private String[] searchFileForId(FileChannel fileChannel, String idValue) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int fieldLength = toRow(fields).length();
        long startPos = fieldLength;
        long endPos = fileChannel.size();
        long pos = ((endPos - startPos) / 2) + startPos;

        final int lookingForRecordTerminator = 0;
        final int lookingForId = 1;
        final int parsingFields = 2;

        int state = lookingForRecordTerminator;
        boolean done = false;
        boolean inQuote = false;
        String buffString = "";
        char[] ch = null;
        String idString = "";
        int numRead = -1;
        StringBuilder sb = new StringBuilder();
        int strNdx = 0;
        List<String> fieldList = new ArrayList<>();

        while (!done) {
            switch (state) {
                case lookingForRecordTerminator:
                    fileChannel.position(pos);
                    buffer.clear();
                    numRead = fileChannel.read(buffer);
                    buffString = new String(buffer.array());
                    int eolNdx = buffString.indexOf("\n");
                    if (eolNdx >= 0) {
                        pos += eolNdx;
                        state = lookingForId;
                    } else {
                        pos -= BUFFER_SIZE;
                        if (pos < fieldLength) {
                            done = true;
                        }
                    }
                    break;

                case lookingForId:
                    fileChannel.position(pos);
                    buffer.clear();
                    numRead = fileChannel.read(buffer);
                    buffString = new String(buffer.array());

                    strNdx = buffString.indexOf(',');
                    if (strNdx < 0) {
                        throw new IllegalStateException("Error finding ID string");
                    }
                    idString = buffString.substring(0, strNdx).trim();
                    if (idString.startsWith(QUOTE)) {
                        idString = idString.substring(1, idString.length() - 1);
                    }

                    int cmp = idString.compareTo(idValue);
                    if (cmp == 0) {
                        fieldList.add(idString);
                        strNdx++;
                        buffString = buffString.substring(strNdx);
                        ch = buffString.toCharArray();
                        sb.setLength(0);
                        strNdx = 0;
                        state = parsingFields;
                    } else {
                        if (cmp > 0) {
                            endPos = pos - 1;
                        } else {
                            startPos = pos + 1;
                        }
                        if ((endPos - startPos) < idValue.length()) {
                            done = true;
                        }
                        else {
                            pos = ((endPos - startPos) / 2) + startPos;
                        }
                    }

                    break;

                case parsingFields:
                    char curr = ch[strNdx];
                    if (curr == QUOTE_CHAR) {
                        if (sb.length() == 0 && !inQuote) {
                            inQuote = true;
                        } else if (inQuote) {
                            inQuote = false;
                        } else {
                            sb.append(curr);
                        }

                    } else if (curr == SEP_CHAR) {
                        if (inQuote) {
                            sb.append(curr);
                        }
                        else {
                            fieldList.add(sb.toString());
                            sb.setLength(0);
                        }
                    } else if (curr == '\n') {
                        fieldList.add(sb.toString());
                        sb.setLength(0);
                        done = true;
                    } else {
                        sb.append(curr);
                    }

                    strNdx++;
                    if (!done && strNdx >= ch.length) {
                        numRead = fileChannel.read(buffer);
                        if (numRead < 0) {
                            done = true;
                        } else {
                            buffString = new String(buffer.array());
                            ch = buffString.toCharArray();
                        }
                    }

                    break;
            }
        }

        String[] result = new String[fieldList.size()];
        result = fieldList.toArray(result);
        return result;
    }

    private String toRow(String[] fields) {
        StringBuilder sb = new StringBuilder(64);

        for (String field : fields) {
            sb.append(QUOTE);
            sb.append(field);
            sb.append(QUOTE);
            sb.append(SEP);
        }

        sb.setLength(sb.length() - 1);
        sb.append("\n");

        return sb.toString();
    }
}
