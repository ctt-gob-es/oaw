package es.inteco.rastreador2.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntecoFileFilter implements FileFilter {

    private final Pattern pattern;

    public IntecoFileFilter(String regExp) {
        pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

    @Override
    public boolean accept(File pathname) {
        Matcher matcher = pattern.matcher(pathname.getName());
        return matcher.find();
    }

}
