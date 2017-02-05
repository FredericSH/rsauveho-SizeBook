package com.example.rfsh.rsauveho_sizebook;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rfsh on 2017-02-04.
 */

public class DateFilter implements InputFilter{
    Pattern mPattern;

    public DateFilter() {
        /**
         * Regex found on http://regexlib.com/REDetails.aspx?regexp_id=763
         * credit to Michael Ash
         * This regex handles all dates for AD years, including leap years and handles
         * month lengths
         */
        mPattern = Pattern.compile("^(?=\\d)(?:(?!(?:1582(?:\\.|-|\\/)10(?:\\.|-|\\/)" +
                "(?:0?[5-9]|1[0-4]))|(?:1752(?:\\.|-|\\/)0?9(?:\\.|-|\\/)(?:0?[3-9]|1[0-3])))" +
                "(?=(?:(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:\\d\\d)" +
                "(?:[02468][048]|[13579][26]))\\D0?2\\D29)|(?:\\d{4}\\D(?!(?:0?[2469]|11)\\D31)" +
                "(?!0?2(?:\\.|-|\\/)(?:29|30))))(\\d{4})([-\\/.])(0?\\d|1[012])\\2((?!00)[012]?\\d|3[01])" +
                "(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])" +
                "(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$\n");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);
//        if(!matcher.matches())
//            return "";

        return null;
    }
}
