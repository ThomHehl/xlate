package com.heavyweightsoftware.io;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionFileFilter implements FileFilter {
    private Pattern pattern;

    public RegularExpressionFileFilter(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean accept(File file) {
        String fileName = file.getName();
        Matcher matcher = pattern.matcher(fileName);
        boolean result = matcher.matches();
        return result;
    }
}
