package com.fkp2.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.InputType;
import android.content.Context;
import android.content.res.Configuration;

public class FastKeyboardService extends InputMethodService {
    private final int NAVY = Color.rgb(23,61,112);
    private final int BROWN = Color.rgb(117,61,18);
    private final int RED = Color.rgb(215,20,20);
    private final int CREAM = Color.rgb(250,249,242);
    private final int YELLOW = Color.rgb(255,224,128);
    private final int BLUE = Color.rgb(205,226,250);
    private final int PINK = Color.rgb(252,220,220);

    @Override public View onCreateInputView() {
        return buildKeyboard();
    }

    private LinearLayout buildKeyboard() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(3,3,3,0);
        root.setBackgroundColor(CREAM);

        LinearLayout tools = row(0.92f);
        String[] toolLabels = {"Copy\nAll","Copy\nScreen","Paste","Cut","Undo","Redo","100\nHistory","امکانات","▶","Resize"};
        for (String s : toolLabels) {
            Button b = key(s, 15, NAVY, CREAM);
            tools.addView(b, weight(1));
            if (s.equals("Paste")) b.setOnClickListener(v -> paste());
            else if (s.equals("Cut")) b.setOnClickListener(v -> cut());
            else if (s.equals("Undo")) b.setOnClickListener(v -> sendKey(67));
            else if (s.equals("Redo")) b.setOnClickListener(v -> sendKey(67));
        }
        root.addView(tools);

        LinearLayout suggestions = row(0.58f);
        String[] words = {"سلام","سلامت","سلامتی","من","مهم","منطقه",""};
        for (String s : words) suggestions.addView(key(s, 15, NAVY, CREAM), weight(1));
        root.addView(suggestions);

        LinearLayout nums = row(0.98f);
        String[] numsA = {"1","2","3","4","5","6","7","8","9","0","⌫"};
        String[] numsS = {"!","@","#","$","%","^","&","*","(",")",""};
        for (int i=0;i<numsA.length;i++) {
            Button b = key(numsA[i], 20, i<10 ? BROWN : NAVY, i==10 ? PINK : CREAM);
            if (i<10) {
                final String n = numsA[i];
                b.setOnClickListener(v -> commit(n));
            } else b.setOnClickListener(v -> backspace());
            nums.addView(b, weight(i==10 ? 1.55f : 1));
        }
        root.addView(nums);

        addLetterRow(root, new String[]{"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"});
        addLetterRow(root, new String[]{"ش","س","ی","ب","ل","ا","ت","ن","م","ک","گ"});

        LinearLayout third = row(0.98f);
        String[] r3 = {"Caps","ظ","ط","ژ","ز","ر","ذ","د","پ","و","چ","=_"}; 
        for (String s:r3) {
            Button b = key(s, s.equals("Caps") ? 18 : 21, NAVY, CREAM);
            if (!s.equals("Caps") && !s.equals("=_")) {
                b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
            }
            third.addView(b, weight(s.equals("Caps") ? 1.2f : 1));
        }
        root.addView(third);

        LinearLayout bottom = row(1.0f);
        Button emoji = key("اموجی", 16, NAVY, CREAM);
        Button symbols = key("123\n!@...", 15, NAVY, CREAM);
        Button globe = key("🌐", 23, Color.BLUE, CREAM);
        Button space = key("Space", 20, NAVY, YELLOW);
        Button comma = key("،", 24, RED, CREAM);
        Button question = key("؟", 24, RED, CREAM);
        Button pm = key("+\n−", 20, RED, CREAM);
        Button left = key("←", 25, Color.BLUE, CREAM);
        Button right = key("→", 25, Color.BLUE, CREAM);
        Button up = key("↑", 25, Color.BLUE, CREAM);
        Button down = key("↓", 25, Color.BLUE, CREAM);
        bottom.addView(emoji, weight(.8f));
        bottom.addView(symbols, weight(1.2f));
        bottom.addView(globe, weight(.9f));
        bottom.addView(space, weight(2.45f));
        bottom.addView(comma, weight(.75f));
        bottom.addView(question, weight(.75f));
        bottom.addView(pm, weight(.75f));
        bottom.addView(left, weight(1.05f));
        bottom.addView(right, weight(1.05f));
        bottom.addView(up, weight(1.05f));
        bottom.addView(down, weight(1.05f));
        space.setOnClickListener(v -> commit(" "));
        comma.setOnClickListener(v -> commit("،"));
        question.setOnClickListener(v -> commit("؟"));
        left.setOnClickListener(v -> sendKey(21));
        right.setOnClickListener(v -> sendKey(22));
        up.setOnClickListener(v -> sendKey(19));
        down.setOnClickListener(v -> sendKey(20));
        root.addView(bottom);

        return root;
    }

    private void addLetterRow(LinearLayout root, String[] letters) {
        LinearLayout r = row(0.98f);
        for (String s:letters) {
            Button b = key(s, 24, NAVY, CREAM);
            b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
            r.addView(b, weight(1));
        }
        root.addView(r);
    }

    private LinearLayout row(float hWeight) {
        LinearLayout r = new LinearLayout(this);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setGravity(Gravity.CENTER);
        r.setPadding(0,0,0,0);
        r.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, hWeight));
        return r;
    }

    private LinearLayout.LayoutParams weight(float w) {
        return new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, w);
    }

    private Button key(String text, float size, int fg, int bg) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(size);
        b.setTextColor(fg);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);
        b.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        b.setPadding(0,0,0,0);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bg);
        gd.setCornerRadius(12);
        gd.setStroke(1, Color.rgb(210,208,200));
        b.setBackground(gd);
        b.setMinHeight(0);
        b.setMinWidth(0);
        return b;
    }

    private void commit(String s) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.commitText(s, 1);
    }

    private void backspace() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.deleteSurroundingText(1,0);
    }

    private void sendKey(int code) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, code));
    }

    private void paste() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.performContextMenuAction(android.R.id.paste);
    }

    private void cut() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.performContextMenuAction(android.R.id.cut);
    }
}
