package com.heavyweightsoftware.io

import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class KeyedSequentialFileImplTest extends Specification {
    public static final String      STRING_VALUES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    public static final char[]      CHARACTER_VALUES = STRING_VALUES.toCharArray()
    public static final String      FILE_NAME = "ksdstest"
    public static final int         DATA_LENGTH = 180
    public static final int         KEY_LENGTH = 8
    public static final String      OTHER_DATA = "This is a replacement line.8901234567890123456789012345678901234567890123456789012345678901234567890" +
                                                            "12345678901234567890123456789012345678901234567890123456789012345678901234567890"

    Path keyedSequentialFile;
    Random random = new Random()

    void setup() {
        String tempDir = System.getProperty("java.io.tmpdir");
        keyedSequentialFile = Paths.get(tempDir, FILE_NAME)
    }

    def "Test CRUD 100 records"() {
        given: "100 Random records in a file"
        final int numRecords = 100;
        String[] data = generateData(numRecords)
        String[] keys = generateKeys(numRecords)

        when: "Inserting records"
        KeyedSequentialFileImpl file = new KeyedSequentialFileImpl(keyedSequentialFile, KEY_LENGTH, DATA_LENGTH)
        for (int ix = 0; ix < numRecords; ++ix) {
            file.insert(keys[ix], data[ix])
        }

        then: "Records should be correct"
        KEY_LENGTH == file.getKeyLength()
        DATA_LENGTH == file.getDataLength()

        numRecords == file.size()
        List<String> failedGets = new ArrayList<>(numRecords)
        List<String> failedUpdates = new ArrayList<>(numRecords)
        for (int ix = 0; ix < numRecords; ++ix) {
            String key = keys[ix];
            String[] answer = file.get(key)
            if (data[ix] != answer[1]) {
                failedGets.add(key);
            }

            file.update(key, OTHER_DATA);
            String value = file.get(key)[1];
            if (OTHER_DATA != value) {
                failedUpdates.add(key)
            }

            file.delete(key)
        }
        0 == failedGets.size()
        0 == failedUpdates.size()
        0 == file.size()
        file.deleteFile()
    }

    def "Test open existing file"() {
        given: "A file with 20 records"
        final int numRecords = 20;
        String[] data = generateData(numRecords)
        String[] keys = generateKeys(numRecords)
        KeyedSequentialFileImpl file = new KeyedSequentialFileImpl(keyedSequentialFile, KEY_LENGTH, DATA_LENGTH)
        for (int ix = 0; ix < numRecords; ++ix) {
            file.insert(keys[ix], data[ix])
        }
        file.close()
        file = null;

        when: "Reopening the file"
        file = new KeyedSequentialFileImpl(keyedSequentialFile)

        then: "Data should be correct"
        KEY_LENGTH == file.getKeyLength()
        DATA_LENGTH == file.getDataLength()

        numRecords == file.size()
        List<String> failedGets = new ArrayList<>(numRecords)
        for (int ix = 0; ix < numRecords; ++ix) {
            String key = keys[ix];
            String[] answer = file.get(key)
            if (data[ix] != answer[1]) {
                failedGets.add(key);
            }
        }
        0 == failedGets.size()
        file.close()
        file.deleteFile()
    }

    def "Test sequential read"() {
        given: "A file with 20 records"
        final int numRecords = 20;
        String[] data = generateData(numRecords)
        String[] keys = generateKeys(numRecords)
        KeyedSequentialFileImpl file = new KeyedSequentialFileImpl(keyedSequentialFile, KEY_LENGTH, DATA_LENGTH)
        for (int ix = 0; ix < numRecords; ++ix) {
            file.insert(keys[ix], data[ix])
        }

        when: "Sequentially reading the file"
        Iterator<String[]> recordsIterator = file.recordsIterator();

        then: "Data should be correct"
        List<String> keyList = keys
        Collections.sort(keyList)
        Iterator<String> keysIterator = keyList.iterator()

        List<String> failures = new ArrayList<>(numRecords)
        while (recordsIterator.hasNext() && keysIterator.hasNext()) {
            String key = keysIterator.next()
            String[] record = recordsIterator.next()
            if (key != record[0]) {
                failures.add(key);
            }
        }

        file.close()
        file.deleteFile()
    }

    private String[] generateData(int num) {
        String[] result = new String[num]

        for (int ix = 0; ix < num; ++ix) {
            String data = randomString(DATA_LENGTH)
            result[ix] = data;
        }

        return result
    }

    private String[] generateKeys(int num) {
        List<String> keys = new ArrayList<>(num)
        StringBuilder sb = new StringBuilder()
        for (int ix = 0; ix < num; ++ix) {
            sb.append("ABCD")
            sb.append(String.format("%04d", ix))
            keys.add(sb.toString())
            sb.setLength(0)
        }

        Collections.shuffle(keys)
        String[] result = new String[num]
        result = keys.toArray(result)
        return result
    }

    private String randomString(int length) {
        StringBuilder sb = new StringBuilder(length)

        for (int ix = 0; ix < length; ++ix) {
            int idx = random.nextInt(CHARACTER_VALUES.length)
            sb.append(CHARACTER_VALUES[idx])
        }

        return sb.toString()
    }
}
