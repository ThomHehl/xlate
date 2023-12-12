package com.heavyweightsoftware.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

import static java.nio.file.StandardOpenOption.*;

public class KeyedSequentialFileImpl implements KeyedSequentialFile {
    private static final String     DATA_SUFFIX = ".data";
    private static final char       FILL_CHAR = '.';
    private static final String     KEY_DATA_LENGTH = "DATA_LENGTH";
    private static final String     KEY_KEY_LENGTH = "KEY_LENGTH";

    private Path dataPath;
    private final int dataLength;
    private final int keyLength;
    private final int recordLength;
    private final Path rootPath;
    private FileChannel fileChannel;

    /**
     * Create a new KeyedSequentialFile.
     * @param path the path to the file
     * @param keyLength the length of the key in characters
     * @param dataLength the length of the data in characters
     */
    public KeyedSequentialFileImpl(Path path, int keyLength, int dataLength) {
        rootPath = path;
        this.keyLength = keyLength;
        this.dataLength = dataLength;
        recordLength = keyLength + dataLength;

        Properties properties = new Properties();
        properties.put(KEY_KEY_LENGTH, String.valueOf(keyLength));
        properties.put(KEY_DATA_LENGTH, String.valueOf(dataLength));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rootPath.toFile()));
            properties.store(writer, "Settings for KeyedSequentialFile");
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        openFileChannel();
    }

    /**
     * Open an existing KeyedSequentialFile
     * @param path the existing file path
     */
    public KeyedSequentialFileImpl(Path path) {
        rootPath = path;
        Properties properties = new Properties();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(rootPath.toFile()));
            properties.load(reader);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        dataLength = Integer.parseInt(String.valueOf(properties.get(KEY_DATA_LENGTH)));
        keyLength = Integer.parseInt(String.valueOf(properties.get(KEY_KEY_LENGTH)));
        recordLength = keyLength + dataLength;

        openFileChannel();
    }

    private void openFileChannel() {
        String dataFile = rootPath.toAbsolutePath() + DATA_SUFFIX;
        dataPath = Paths.get(dataFile);
        try  {
            fileChannel = (FileChannel.open(dataPath, CREATE, READ, WRITE));
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create file:" + dataPath.toAbsolutePath(), ioe);
        }
    }

    public void close() {
        try {
            fileChannel.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Delete the row with the specified key
     * @param key the key
     * @return true if the item was delted or false if not found
     */
    @Override
    public boolean delete(String key) {
        boolean result = false;

        try {
            String[] answer = locate(key);
            if (answer != null) {
                previousRecord();
                removeRecord();
                result = true;
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return result;
    }

    private void removeRecord() throws IOException {
        final long startPos = getPosition();
        final long eof = size();
        long fromPos = startPos + 1;
        ByteBuffer buff = ByteBuffer.allocate(recordLength);

        while (fromPos < eof) {
            position(fromPos);
            buff.position(0);
            int num = fileChannel.read(buff);
            buff.position(0);
            position(fromPos - 1);
            num = fileChannel.write(buff);
            ++fromPos;
        }

        fileChannel.truncate(fileChannel.size() - recordLength);
        position(startPos);
    }

    /**
     * Delete the file altogether
     */
    @Override
    public void deleteFile() {
        rootPath.toFile().delete();
        dataPath.toFile().delete();
    }

    /**
     * What is the data length
     * @return the data length in characters
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * What is the key length
     * @return the key length in characters
     */
    public int getKeyLength() {
        return keyLength;
    }

    /**
     * Insert a record into the file
     * @param key the key
     * @param data the data
     */
    @Override
    public void insert(String key, String data) {
        try {
            String[] record = locate(key);
            if (record != null) {
                String msg = key + " already exists.";
                throw new KeyTooShortException(msg);
            }
            openGap();
            write(key, data);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private String[] locate(String key) throws IOException {
        String[] result = null;
        long min = 0;
        long max = size() - 1;

        while (max >= min) {
            long pos = (min + max) / 2;
            String[] record = getRecord(pos);

            int cmp = key.compareTo(record[0]);
            if (cmp == 0) {
                result = record;
                max = -1;
            } else if (cmp < 0) {
                max = pos - 1;
                position(pos);
            } else {
                min = pos + 1;
            }
        }

        return result;
    }

    private void openGap() throws IOException {
        final long startPos = getPosition();
        long fromPos = size() - 1;
        ByteBuffer buff = ByteBuffer.allocate(recordLength);

        while (fromPos >= startPos) {
            position(fromPos);
            buff.position(0);
            int num = fileChannel.read(buff);
            buff.position(0);
            num = fileChannel.write(buff);
            --fromPos;
        }

        position(startPos);
    }

    private byte[] prepareRecord(String key, String data) throws KeyTooShortException {
        if (key.length() < keyLength) {
            String msg = "Key length of " + key.length() + " is less than defined length of " + keyLength + ".";
            throw new KeyTooShortException(msg);
        }
        if (data.length() > dataLength) {
            data = data.substring(0, dataLength);
        } else if (data.length() < dataLength) {
            StringBuilder sb = new StringBuilder(dataLength);
            sb.append(dataLength);
            while (sb.length() < dataLength) {
                sb.append(FILL_CHAR);
            }
            data = sb.toString();
        }

        String str = key + data;
        return str.getBytes();
    }

    /**
     * Move the file pointer back to the previous record
     */
    private void previousRecord() throws IOException {
        long newPos = getPosition() - 1;
        position(newPos);
    }

    /**
     * An iterator to the records
     * @return the iterator to a String array where index 0 = key and index 1 = data
     */
    public Iterator<String[]> recordsIterator() {
        Iterator<String[]> result = new RecordIterator();
        return result;
    }

    /**
     * The number of records in the file
     * @return the number of records
     */
    public long size() {
        long result;

        try {
            result = fileChannel.size() / recordLength;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return result;
    }

    /**
     * Update the data for the specified key
     * @param key the key
     * @param data the new data
     */
    @Override
    public String[] update(String key, String data) {
        String[] result = null;

        try {
            result = locate(key);
            if (result == null) {
                throw new KeyNotFoundException("Key not found:" + key);
            }
            previousRecord();
            write(key, data);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return result;
    }

    /**
     * Retrieve a record with the specified key, if it exists
     * @param key the key
     * @return the key (index 0) and the data (index 1) or null if not found
     */
    @Override
    public String[] get(String key) {
        String[] result;
        try {
            result = locate(key);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return result;
    }

    /**
     * Retrieve the specified record number from the file.
     * @param recordNumber the record number
     * @return the key and data in index 0 and 1 respectively, or null, if the record doesn't exist
     */
    protected String[] getRecord(long recordNumber) throws IOException {
        String[] result = null;

        if (size() >= recordNumber) {
            fileChannel.position(recordNumber * recordLength);
            result = readNextRecord();
        }

        return result;
    }

    private String[] readNextRecord() throws IOException {
        String[] result = new String[2];

        ByteBuffer buff = ByteBuffer.allocate(recordLength);
        byte[] dataBuff = new byte[dataLength];
        byte[] keyBuff = new byte[keyLength];

        int nRead = fileChannel.read(buff);

        buff.position(0);
        buff.get(keyBuff, 0, keyLength);
        result[0] = new String(keyBuff);

        buff.get(dataBuff, 0, dataLength);
        result[1] = new String(dataBuff);

        return result;
    }

    protected long getPosition() throws IOException {
        long pos = fileChannel.position();
        long result = pos / recordLength;
        return result;
    }

    protected void position(long recordNumber) throws IOException {
        long pos = recordNumber * recordLength;
        fileChannel.position(pos);
    }

    protected void write(String key, String data) throws IOException {
        byte[] newRecord = prepareRecord(key, data);
        ByteBuffer out = ByteBuffer.wrap(newRecord);
        fileChannel.write(out);
    }

    protected class RecordIterator implements Iterator<String[]> {

        protected RecordIterator() {
            try {
                position(0);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        @Override
        public boolean hasNext() {
            boolean result = false;

            try {
                result = getPosition() < size();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }

            return result;
        }

        @Override
        public String[] next() {
            String[] result = new String[0];
            try {
                result = readNextRecord();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
            return result;
        }
    }
}
