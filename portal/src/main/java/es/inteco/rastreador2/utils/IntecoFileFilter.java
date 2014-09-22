package es.inteco.rastreador2.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntecoFileFilter implements FileFilter {

    private String regExp;

    public IntecoFileFilter(String regExp) {
        this.regExp = regExp;
    }

    @Override
    public boolean accept(File pathname) {
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pathname.getName());
        if (matcher.find()) {
            return true;
        }
        return false;
    }

}
