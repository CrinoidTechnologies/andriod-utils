package com.crinoidtechnologies.general.utils;

import android.graphics.Paint;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

public class TextViewUtils {

    public static void createStrikeThrough(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void addLinksNoUnderline(TextView textView, int linkifyMode) {
        Linkify.addLinks(textView, linkifyMode);
        if (!(textView.getText() instanceof Spannable)) return;

        Spannable s = (Spannable) textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public static void RemoveEmptySpaces(StringBuilder text) {
//    text.repl
    }

    public static void RemoveParanthesisText(StringBuilder text) {

//        String dummy = "abcdef (sdjf) ..) (lkasjdfkjdfkj .. ";
//        text=new StringBuilder((dummy));

        int index = text.lastIndexOf("(");
        int index2 = -1;
        while (index >= 0) {
            index2 = text.lastIndexOf(")");
//            Log.d("asdf",index+" "+index2+" ");
            if (index2 > 0) {
                if (index <= index2)
                    text.replace(index, index2 + 1, "");
                else
                    text.replace(index, index + 1, "");
            } else {
//                Log.d("Asdf",text.toString());
                break;
            }
            index = text.lastIndexOf("(");

        }

//        Log.d("asdf",text.toString());

    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
//    public static void Remove

}
