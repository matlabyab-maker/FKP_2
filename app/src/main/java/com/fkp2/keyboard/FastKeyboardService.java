package com.fkp2.keyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastKeyboardService extends InputMethodService {
    private static final int NAVY = Color.rgb(23, 61, 112);
    private static final int BROWN = Color.rgb(117, 61, 18);
    private static final int RED = Color.rgb(215, 20, 20);
    private static final int CREAM = Color.rgb(250, 249, 242);
    private static final int YELLOW = Color.rgb(255, 224, 128);
    private static final int BLUE = Color.rgb(25, 95, 170);
    private static final int PINK = Color.rgb(252, 220, 220);

    private boolean caps = false;
    private boolean symbols = false;
    private final ArrayList<String> history = new ArrayList<>();
    private SharedPreferences prefs;

    @Override public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("fkp2", Context.MODE_PRIVATE);
        loadHistory();
    }

    @Override public View onCreateInputView() {
        return buildKeyboard();
    }

    @Override public void onStartInputView(android.view.inputmethod.EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        View view = getInputView();
        if (view != null) view.post(view::requestLayout);
    }

    private LinearLayout buildKeyboard() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.FILL);
        root.setPadding(1, 1, 1, 1);
        root.setBackgroundColor(CREAM);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout tools = row(1.05f);
        String[] toolLabels = {"Copy\nAll", "Copy\nScreen", "Paste", "Cut", "Undo", "Redo", "100\nHistory", "امکانات", "▶", "Resize"};
        for (String s : toolLabels) {
            Button b = key(s, 12, NAVY, CREAM);
            tools.addView(b, weight(1));
            if (s.startsWith("Copy\nAll")) b.setOnClickListener(v -> copyAll());
            else if (s.startsWith("Copy\nScreen")) b.setOnClickListener(v -> copyAll());
            else if (s.equals("Paste")) b.setOnClickListener(v -> paste());
            else if (s.equals("Cut")) b.setOnClickListener(v -> cut());
            else if (s.equals("Undo")) b.setOnClickListener(v -> ctrlKey(KeyEvent.KEYCODE_Z));
            else if (s.equals("Redo")) b.setOnClickListener(v -> ctrlKey(KeyEvent.KEYCODE_Y));
            else if (s.startsWith("100")) b.setOnClickListener(v -> showHistory(v));
            else if (s.equals("امکانات")) b.setOnClickListener(v -> showTools(v));
            else if (s.equals("▶")) b.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_RIGHT));
            else if (s.equals("Resize")) b.setOnClickListener(v -> toggleResize(root));
        }
        root.addView(tools);

        LinearLayout suggestions = row(0.62f);
        for (String s : new String[]{"سلام", "سلامت", "سلامتی", "من", "مهم", "منطقه", ""}) {
            Button b = key(s, 14, NAVY, CREAM);
            if (!TextUtils.isEmpty(s)) b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
            suggestions.addView(b, weight(1));
        }
        root.addView(suggestions);

        LinearLayout nums = row(1.0f);
        String[] numberLabels = symbols ? new String[]{"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "⌫"}
                                       : new String[]{"۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹", "۰", "⌫"};
        for (int i = 0; i < numberLabels.length; i++) {
            final int index = i;
            Button b = key(numberLabels[i], 19, i == 10 ? NAVY : BROWN, i == 10 ? PINK : CREAM);
            b.setOnClickListener(v -> {
                if (index == 10) backspace(); else commit(((Button)v).getText().toString());
            });
            nums.addView(b, weight(i == 10 ? 1.45f : 1));
        }
        root.addView(nums);

        addLetterRow(root, new String[]{"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"});
        addLetterRow(root, new String[]{"ش","س","ی","ب","ل","ا","ت","ن","م","ک","گ"});

        LinearLayout third = row(1.0f);
        String[] r3 = {"Caps","ظ","ط","ژ","ز","ر","ذ","د","پ","و","چ","=_"};
        for (String s : r3) {
            Button b = key(s, s.equals("Caps") ? 16 : 20, NAVY, s.equals("Caps") && caps ? YELLOW : CREAM);
            if (s.equals("Caps")) b.setOnClickListener(v -> { caps = !caps; rebuild(); });
            else if (s.equals("=_")) b.setOnClickListener(v -> commit("="));
            else b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
            third.addView(b, weight(s.equals("Caps") ? 1.2f : 1));
        }
        root.addView(third);

        LinearLayout bottom = row(1.08f);
        Button emoji = key("اموجی", 14, NAVY, CREAM);
        Button sym = key("123\n!@...", 13, NAVY, symbols ? YELLOW : CREAM);
        Button globe = key("🌐", 22, BLUE, CREAM);
        Button space = key("Space", 19, NAVY, YELLOW);
        Button comma = key("،", 23, RED, CREAM);
        Button question = key("؟", 23, RED, CREAM);
        Button pm = key("+\n−", 18, RED, CREAM);
        Button left = key("←", 23, BLUE, CREAM);
        Button right = key("→", 23, BLUE, CREAM);
        Button up = key("↑", 23, BLUE, CREAM);
        Button down = key("↓", 23, BLUE, CREAM);
        bottom.addView(emoji, weight(.82f));
        bottom.addView(sym, weight(1.15f));
        bottom.addView(globe, weight(.82f));
        bottom.addView(space, weight(2.35f));
        bottom.addView(comma, weight(.72f));
        bottom.addView(question, weight(.72f));
        bottom.addView(pm, weight(.72f));
        bottom.addView(left, weight(1.0f));
        bottom.addView(right, weight(1.0f));
        bottom.addView(up, weight(1.0f));
        bottom.addView(down, weight(1.0f));
        emoji.setOnClickListener(v -> showEmoji(v));
        sym.setOnClickListener(v -> { symbols = !symbols; rebuild(); });
        globe.setOnClickListener(v -> ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker());
        space.setOnClickListener(v -> commit(" "));
        comma.setOnClickListener(v -> commit("،"));
        question.setOnClickListener(v -> commit("؟"));
        pm.setOnClickListener(v -> commit("±"));
        left.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_LEFT));
        right.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_RIGHT));
        up.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_UP));
        down.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_DOWN));
        root.addView(bottom);
        return root;
    }

    private void addLetterRow(LinearLayout root, String[] letters) {
        LinearLayout r = row(1.0f);
        for (String s : letters) {
            Button b = key(s, 22, NAVY, CREAM);
            b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
            r.addView(b, weight(1));
        }
        root.addView(r);
    }

    private LinearLayout row(float weight) {
        LinearLayout r = new LinearLayout(this);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setGravity(Gravity.FILL);
        r.setPadding(0, 0, 0, 0);
        r.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight));
        return r;
    }

    private LinearLayout.LayoutParams weight(float w) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, w);
        p.setMargins(0, 0, 0, 0);
        return p;
    }

    private Button key(String text, float size, int fg, int bg) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(size);
        b.setTextColor(fg);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);
        b.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        b.setPadding(0, 0, 0, 0);
        b.setMinHeight(0);
        b.setMinWidth(0);
        b.setIncludeFontPadding(true);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bg);
        gd.setCornerRadius(8);
        gd.setStroke(1, Color.rgb(210, 208, 200));
        b.setBackground(gd);
        return b;
    }

    private void rebuild() {
        View v = buildKeyboard();
        setInputView(v);
    }

    private void commit(String s) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.commitText(s, 1);
            if (!s.equals(" ")) addHistory(s);
        }
    }

    private void backspace() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.deleteSurroundingText(1, 0);
    }

    private void sendKey(int code) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, code));
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, code));
        }
    }

    private void ctrlKey(int code) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.sendKeyEvent(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, code, 0, KeyEvent.META_CTRL_ON));
            ic.sendKeyEvent(new KeyEvent(0, 0, KeyEvent.ACTION_UP, code, 0, KeyEvent.META_CTRL_ON));
        }
    }

    private void copyAll() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.performContextMenuAction(android.R.id.selectAll);
            ic.performContextMenuAction(android.R.id.copy);
        }
    }

    private void paste() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.performContextMenuAction(android.R.id.paste);
    }

    private void cut() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.performContextMenuAction(android.R.id.cut);
    }

    private void toggleResize(LinearLayout root) {
        ViewGroup.LayoutParams p = root.getLayoutParams();
        if (p != null) {
            int current = p.height;
            root.setTag(current == ViewGroup.LayoutParams.WRAP_CONTENT ? "normal" : "compact");
        }
        // Keep the keyboard attached to the bottom; this button provides a safe medium-size toggle.
        getWindow().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dp(260));
    }

    private void showEmoji(View anchor) {
        showPopup(anchor, new String[]{"😀","😂","😍","😎","👍","❤️","🙏","🔥","🎉","🙂"}, 14, true);
    }

    private void showTools(View anchor) {
        showPopup(anchor, new String[]{"َ","ِ","ُ","ّ","ْ","ً","ٌ","ٍ","ٔ","ٰ","☑ Select All","⌫ Delete"}, 13, false);
    }

    private void showPopup(View anchor, String[] items, int size, boolean historyLike) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(6,6,6,6);
        box.setBackgroundColor(CREAM);
        for (String item : items) {
            Button b = key(item, size, NAVY, CREAM);
            box.addView(b, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(42)));
            b.setOnClickListener(v -> {
                String s = ((Button)v).getText().toString();
                if (s.contains("Select All")) {
                    InputConnection ic = getCurrentInputConnection();
                    if (ic != null) ic.performContextMenuAction(android.R.id.selectAll);
                } else if (s.contains("Delete")) backspace();
                else commit(s);
            });
        }
        PopupWindow pw = new PopupWindow(box, dp(180), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(CREAM));
        pw.setOutsideTouchable(true);
        pw.setElevation(dp(8));
        pw.showAsDropDown(anchor, 0, -dp(220));
    }

    private void showHistory(View anchor) {
        if (history.isEmpty()) {
            showPopup(anchor, new String[]{"تاریخچه خالی است"}, 14, true);
            return;
        }
        List<String> items = new ArrayList<>(history);
        Collections.reverse(items);
        if (items.size() > 20) items = items.subList(0, 20);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(6,6,6,6);
        box.setBackgroundColor(CREAM);
        for (String item : items) {
            Button b = key(item, 14, NAVY, CREAM);
            box.addView(b, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(42)));
            b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
        }
        PopupWindow pw = new PopupWindow(box, dp(220), dp(360), true);
        pw.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(CREAM));
        pw.setOutsideTouchable(true);
        pw.setElevation(dp(8));
        pw.showAsDropDown(anchor, 0, -dp(360));
    }

    private void addHistory(String s) {
        if (TextUtils.isEmpty(s)) return;
        history.add(s);
        while (history.size() > 100) history.remove(0);
        prefs.edit().putString("history", TextUtils.join("\u0001", history)).apply();
    }

    private void loadHistory() {
        String all = prefs.getString("history", "");
        if (!TextUtils.isEmpty(all)) history.addAll(Arrays.asList(all.split("\u0001", -1)));
        while (history.size() > 100) history.remove(0);
    }

    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }
}
