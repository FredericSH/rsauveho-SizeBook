package com.example.rfsh.rsauveho_sizebook;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to ensure numbers have one decimal digits.
 * http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
 */

public class DecimalInputFilter implements InputFilter{
    Pattern mPattern;

    /**
     * Takes two arguments, filter applied to an edittext to limit allowed decimal places
     * @param digitsBeforeZero
     * @param digitsAfterZero
     */
    public DecimalInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0,"
                + (digitsBeforeZero-1) + "}" +
                "+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);
        if(!matcher.matches())
            return "";
        return null;
    }
}
