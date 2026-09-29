package com.fkp2.keyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
    private boolean english = false;
    private final ArrayList<String> history = new ArrayList<>();
    private final ArrayList<Button> allKeys = new ArrayList<>();
    private SharedPreferences prefs;
    private LinearLayout currentRoot;
    private int keyboardColor = CREAM;

    private static final String[] PERSIAN_NUMBERS = {"۱","۲","۳","۴","۵","۶","۷","۸","۹","۰"};
    private static final String[] PERSIAN_R1 = {"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"};
    private static final String[] PERSIAN_R2 = {"ش","س","ی","ب","ل","ا","ت","ن","م","ک","گ"};
    private static final String[] PERSIAN_R3 = {"ظ","ط","ژ","ز","ر","ذ","د","پ","و","چ"};
    private static final String[] EN_R1 = {"Q","W","E","R","T","Y","U","I","O","P","["};
    private static final String[] EN_R2 = {"A","S","D","F","G","H","J","K","L",";","'"};
    private static final String[] EN_R3 = {"Z","X","C","V","B","N","M",",",".","/"};

    private static final String[] EMOJIS = {
        "😀","😃","😄","😁","😆","😅","😂","🤣","😊","😇","🙂","🙃","😉","😌","😍","🥰","😘","😗","😙","😚","😋","😛","😝","😜","🤪","🤨","🧐","🤓","😎","🤩","🥳","😏","😒","😞","😔","😟","😕","🙁","☹️","😣","😖","😫","😩","🥺","😢","😭","😤","😠","😡","🤬","🤯","😳","🥵","🥶","😱","😨","😰","😥","😓","🤗","🤔","🤭","🤫","🤥","😶","😐","😑","😬","🙄","😯","😦","😧","😮","😲","🥱","😴","🤤","😪","😵","🤐","🥴","🤢","🤮","🤧","😷","🤒","🤕","🤑","🤠","😈","👿","👹","👺","🤡","💩","👻","💀","☠️","👽","👾","🤖","🎃","😺","😸","😹","😻","😼","😽","🙀","😿","😾","🙈","🙉","🙊","💋","💌","💘","💝","💖","💗","💓","💞","💕","💟","❣️","💔","❤️","🧡","💛","💚","💙","💜","🖤","🤍","🤎","💯","💢","💥","💫","💦","💨","🕳️","💣","💬","👋","🤚","🖐️","✋","🖖","👌","🤏","✌️","🤞","🤟","🤘","🤙","👈","👉","👆","👇","☝️","👍","👎","✊","👊","🤝","🙏","👏","🙌","👐","🤲","💪","🦾","🦿","👀","👁️","🧠","👄","👅","👂","👃","🫀","🫁","🦷","🦴","👶","🧒","👦","👧","🧑","👨","👩","🧓","👴","👵","🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼","🐨","🐯","🦁","🐮","🐷","🐸","🐵","🙈","🙉","🙊","🐔","🐧","🐦","🐤","🦄","🐝","🦋","🐌","🐞","🐜","🐢","🐍","🦎","🦂","🐙","🦀","🐠","🐟","🐬","🐳","🐊","🐘","🦏","🦒","🦓","🐎","🐕","🐈","🐓","🦜","🦢","🌹","🌷","🌻","🌞","🌝","🌈","☀️","⭐","🌟","✨","⚡","❄️","🔥","🌊","🍎","🍊","🍋","🍉","🍇","🍓","🍒","🍑","🍍","🥝","🍅","🥑","🍞","🧀","🍔","🍕","🍟","🌭","🍿","🍩","🍪","🎂","🍰","🍫","🍬","☕","🍵","⚽","🏀","🏈","⚾","🎾","🏐","🏆","🥇","🚗","🚕","🚌","🚓","🚑","🚒","✈️","🚁","🚀","🚲","🏠","🏢","🏥","🏫","⛪","🕌","🛒","📱","💻","⌚","📷","📺","🎧","🎵","🎶","🎸","🎹","🎮","🎲","🎯","🎁","🎈","🎉","🎊","📌","📍","🔑","🔒","🔓","⚙️","🔔","🔍","🔎","💡","📁","📂","🗂️","🗃️","🗄️","📦","🗑️","📝","📄","📋","📎","🖇️","📚","📖","📕","📗","📘","📙","📒","📓","📔","📁","📂","🗂️","🗃️","🗄️","🗑️"
    };

    private static final String[] FLAGS = {
        "🇮🇷","🇺🇸","🇬🇧","🇨🇦","🇦🇺","🇩🇪","🇫🇷","🇮🇹","🇪🇸","🇵🇹","🇹🇷","🇷🇺","🇺🇦","🇨🇳","🇯🇵","🇰🇷","🇮🇳","🇵🇰","🇦🇫","🇮🇶","🇸🇦","🇦🇪","🇶🇦","🇰🇼","🇧🇭","🇴🇲","🇪🇬","🇯🇴","🇱🇧","🇸🇾","🇵🇸","🇮🇱","🇬🇷","🇳🇱","🇧🇪","🇨🇭","🇦🇹","🇸🇪","🇳🇴","🇩🇰","🇫🇮","🇵🇱","🇨🇿","🇭🇺","🇷🇴","🇧🇬","🇷🇸","🇭🇷","🇦🇱","🇧🇦","🇬🇪","🇦🇲","🇦🇿","🇰🇿","🇺🇿","🇹🇯","🇹🇲","🇰🇬","🇦🇺","🇳🇿","🇿🇦","🇳🇬","🇰🇪","🇲🇦","🇩🇿","🇹🇳","🇧🇷","🇦🇷","🇨🇱","🇨🇴","🇲🇽","🇺🇾","🇻🇪","🇵🇪","🇺🇸","🇨🇺","🇯🇲","🇰🇷","🇸🇬","🇲🇾","🇮🇩","🇹🇭","🇻🇳","🇵🇭"
    };

    private static final String[] SYMBOLS = {
        "!","@","#","$","%","^","&","*","(",")","-","_","+","=","[","]","{","}","\\","|",";",":","'","\"",",",".","<",">","/","?","~","`","§","¶","©","®","™","€","£","¥","₽","₹","₺","₩","₴","₦","₱","₲","₵","₡","₫","฿","₭","₮","₸","₺","∞","≈","≠","≤","≥","±","×","÷","√","∑","∏","∆","∇","∂","∫","∮","π","µ","Ω","α","β","γ","δ","θ","λ","σ","φ","ψ","ω","←","↑","→","↓","↔","↕","↖","↗","↘","↙","⇐","⇑","⇒","⇓","↻","↺","✓","✔","✕","✖","✗","✘","★","☆","●","○","■","□","◆","◇","▲","△","▼","▽","♥","♡","♦","♢","♣","♤","♧","☀","☁","☂","☃","☄","☎","☑","☒","☐","⚠","⚡","⚙","⚓","⚽","♠","♣","♥","♦","♪","♫","†","‡","‰","′","″","←","→","↑","↓","↪","↩","⌂","⌘","⌫","⏎","␣","◀","▶","⏪","⏩","⏮","⏭","⏸","⏹","⏺","🔒","🔓","🔑","🔔","🔕","🔗","🔒","🗝️"
    };

    private static final String[] ARABIC_MARKS = {"َ","ِ","ُ","ً","ٍ","ٌ","ْ","ّ","ٰ","ٔ","ٕ","ٖ","ٗ","٘","ٙ","ٚ","ٛ","ٜ","ٝ","ٞ","ٟ","ٖ","ـ","ء","آ","أ","ؤ","إ","ئ","ة","ى","لا"};

    @Override public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("fkp2", Context.MODE_PRIVATE);
        loadHistory();
        keyboardColor = prefs.getInt("keyboardColor", CREAM);
    }

    @Override public View onCreateInputView() { return buildKeyboard(); }

    @Override public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        if (restarting) rebuild();
    }

    private LinearLayout buildKeyboard() {
        allKeys.clear();
        LinearLayout root = new LinearLayout(this);
        currentRoot = root;
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.FILL);
        root.setPadding(1,1,1,1);
        root.setBackgroundColor(keyboardColor);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout tools = row(1.05f);
        String[] toolLabels = {"📋 Copy\nAll","🖼 Copy\nScreen","📥 Paste","✂ Cut","↶ Undo","↷ Redo","🕘 100\nHistory","⚙ امکانات","▶","↕ Resize"};
        for (String s : toolLabels) {
            Button b = key(s,12,NAVY,CREAM); tools.addView(b,weight(1));
            if (s.startsWith("📋 Copy\nAll")) b.setOnClickListener(v -> copyAll());
            else if (s.startsWith("🖼 Copy\nScreen")) b.setOnClickListener(v -> copyAll());
            else if (s.startsWith("📥 Paste")) b.setOnClickListener(v -> paste());
            else if (s.startsWith("✂ Cut")) b.setOnClickListener(v -> cut());
            else if (s.startsWith("↶ Undo")) b.setOnClickListener(v -> ctrlKey(KeyEvent.KEYCODE_Z));
            else if (s.startsWith("↷ Redo")) b.setOnClickListener(v -> ctrlKey(KeyEvent.KEYCODE_Y));
            else if (s.contains("100") && s.contains("History")) b.setOnClickListener(v -> showHistory(v));
            else if (s.contains("امکانات")) b.setOnClickListener(v -> showTools(v));
            else if (s.equals("▶")) b.setOnClickListener(v -> sendKey(KeyEvent.KEYCODE_DPAD_RIGHT));
            else if (s.contains("Resize")) b.setOnClickListener(v -> toggleResize());
        }
        root.addView(tools);

        LinearLayout suggestions = row(0.62f);
        for (String s : new String[]{"سلام","سلامت","سلامتی","من","مهم","منطقه",""}) {
            Button b = key(s,14,NAVY,CREAM);
            if (!TextUtils.isEmpty(s)) b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
            suggestions.addView(b,weight(1));
        }
        root.addView(suggestions);

        LinearLayout nums = row(1.0f);
        String[] numberLabels = symbols ? firstTenSymbols() : (english ? new String[]{"1","2","3","4","5","6","7","8","9","0"} : PERSIAN_NUMBERS);
        for (int i=0;i<numberLabels.length;i++) {
            final int index=i; Button b=key(numberLabels[i],19,BROWN,CREAM); nums.addView(b,weight(1));
            b.setOnClickListener(v -> commit(((Button)v).getText().toString()));
        }
        Button back = key("⌫",19,NAVY,PINK); nums.addView(back,weight(1.45f)); back.setOnClickListener(v -> backspace());
        root.addView(nums);

        if (english) {
            addLetterRow(root, EN_R1); addLetterRow(root, EN_R2);
        } else {
            addLetterRow(root, PERSIAN_R1); addLetterRow(root, PERSIAN_R2);
        }

        LinearLayout third=row(1.0f);
        Button capsB=key("Caps",16,NAVY,caps?YELLOW:CREAM); third.addView(capsB,weight(1.2f)); capsB.setOnClickListener(v->{caps=!caps; rebuild();});
        String[] r3=english?EN_R3:PERSIAN_R3;
        for(String s:r3){Button b=key(caps? s.toUpperCase():s,20,NAVY,CREAM); third.addView(b,weight(1)); b.setOnClickListener(v -> commit(((Button)v).getText().toString()));}
        Button eq=key("=_",18,NAVY,CREAM); third.addView(eq,weight(1)); eq.setOnClickListener(v->commit("="));
        root.addView(third);

        LinearLayout bottom=row(1.08f);
        Button emoji=key("😀 اموجی",14,NAVY,CREAM); Button sym=key("# 123\n!@...",13,NAVY,symbols?YELLOW:CREAM); Button globe=key(english?"🌐 EN":"🌐 FA",18,BLUE,CREAM); Button space=key("Space",19,NAVY,CREAM);
        Button comma=key(english?",":"،",23,RED,CREAM); Button question=key(english?"?":"؟",23,RED,CREAM); Button pm=key("+\n−",18,RED,CREAM); Button left=key("←",23,BLUE,CREAM); Button right=key("→",23,BLUE,CREAM); Button up=key("↑",23,BLUE,CREAM); Button down=key("↓",23,BLUE,CREAM); Button enter=key("↵ Enter",16,NAVY,CREAM);
        bottom.addView(emoji,weight(.82f)); bottom.addView(sym,weight(1.15f)); bottom.addView(globe,weight(.9f)); bottom.addView(space,weight(2.35f)); bottom.addView(comma,weight(.72f)); bottom.addView(question,weight(.72f)); bottom.addView(pm,weight(.72f)); bottom.addView(left,weight(1)); bottom.addView(right,weight(1)); bottom.addView(up,weight(1)); bottom.addView(down,weight(1)); bottom.addView(enter,weight(1.35f));
        emoji.setOnClickListener(v->showEmoji(v)); sym.setOnClickListener(v->{symbols=!symbols; rebuild();}); globe.setOnClickListener(v->{english=!english; symbols=false; rebuild();}); space.setOnClickListener(v->commit(" ")); comma.setOnClickListener(v->commit(comma.getText().toString())); question.setOnClickListener(v->commit(question.getText().toString())); pm.setOnClickListener(v->commit("±")); left.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_LEFT)); right.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_RIGHT)); up.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_UP)); down.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_DOWN)); enter.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_ENTER));
        root.addView(bottom);
        return root;
    }

    private String[] firstTenSymbols(){ return Arrays.copyOf(SYMBOLS,10); }

    private void addLetterRow(LinearLayout root,String[] letters){
        LinearLayout r=row(1.0f);
        for(String s:letters){
            String shown=caps?s.toUpperCase():s;
            Button b=key(shown,22,NAVY,CREAM);
            r.addView(b,weight(1));
            b.setOnClickListener(v->commit(((Button)v).getText().toString()));
            if(!english && "ا".equals(s)){
                b.setOnLongClickListener(v->{showAlefVariants(v);return true;});
            }
        }
        root.addView(r);
    }

    private void showAlefVariants(View anchor){
        showGridPopup(anchor,new String[]{"ا","آ","أ","إ","ٱ","ؤ","ئ"},52,100);
    }

    private LinearLayout row(float w){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);r.setGravity(Gravity.FILL);r.setPadding(0,0,0,0);r.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,w));return r;}
    private LinearLayout.LayoutParams weight(float w){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,w);p.setMargins(0,0,0,0);return p;}

    private Button key(String text,float size,int fg,int bg){
        Button b=new Button(this);
        b.setText(text); b.setTextSize(size); b.setTextColor(fg); b.setGravity(Gravity.CENTER);
        b.setAllCaps(false); b.setTypeface(Typeface.create("sans",Typeface.NORMAL));
        b.setPadding(0,0,0,0); b.setMinHeight(0); b.setMinWidth(0); b.setIncludeFontPadding(true);
        b.setTag(bg); b.setBackground(makeBg(bg));
        b.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){
                v.setBackground(makeBg(YELLOW));
            } else if(e.getAction()==MotionEvent.ACTION_UP || e.getAction()==MotionEvent.ACTION_CANCEL){
                Object t=v.getTag();
                if(t instanceof Integer) v.setBackground(makeBg((Integer)t));
            }
            return false;
        });
        allKeys.add(b); return b;
    }

    private GradientDrawable makeBg(int color){GradientDrawable gd=new GradientDrawable();gd.setColor(color);gd.setCornerRadius(8);gd.setStroke(1,Color.rgb(210,208,200));return gd;}

    private void rebuild(){setInputView(buildKeyboard());}
    private void commit(String s){
        try{InputConnection ic=getCurrentInputConnection();if(ic!=null && s!=null){ic.commitText(caps&&s.length()==1?s.toUpperCase():s,1);if(!s.equals(" "))addHistory(s);}}catch(Exception ignored){}
    }
    private void backspace(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.deleteSurroundingText(1,0);}
    private void sendKey(int code){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,code));ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,code));}}
    private void ctrlKey(int code){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.sendKeyEvent(new KeyEvent(0,0,KeyEvent.ACTION_DOWN,code,0,KeyEvent.META_CTRL_ON));ic.sendKeyEvent(new KeyEvent(0,0,KeyEvent.ACTION_UP,code,0,KeyEvent.META_CTRL_ON));}}
    private void copyAll(){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.performContextMenuAction(android.R.id.selectAll);ic.performContextMenuAction(android.R.id.copy);}}
    private void paste(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.performContextMenuAction(android.R.id.paste);}
    private void cut(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.performContextMenuAction(android.R.id.cut);}
    private void toggleResize(){getWindow().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,dp(260));}

    private void showEmoji(View anchor){
        LinearLayout box=scrollBox();
        addGrid(box,EMOJIS,38);
        addGrid(box,FLAGS,38);
        PopupWindow pw=new PopupWindow(box,dp(330),dp(420),true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(420));
    }

    private void showTools(View anchor){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(6,6,6,6);box.setBackgroundColor(CREAM);
        Button arabic=key("🔤 اعراب و علائم عربی",14,NAVY,CREAM); box.addView(arabic,new LinearLayout.LayoutParams(-1,dp(48))); arabic.setOnClickListener(v->showGridPopup(anchor,ARABIC_MARKS,44,220));
        Button calc=key("🧮 ماشین حساب",14,NAVY,CREAM); box.addView(calc,new LinearLayout.LayoutParams(-1,dp(48))); calc.setOnClickListener(v->showCalculator(anchor));
        Button colors=key("🎨 رنگ نمای کیبورد",14,NAVY,CREAM); box.addView(colors,new LinearLayout.LayoutParams(-1,dp(48))); colors.setOnClickListener(v->showColorTablet(anchor));
        Button emoji=key("😀 اموجی و پرچم‌ها",14,NAVY,CREAM); box.addView(emoji,new LinearLayout.LayoutParams(-1,dp(48))); emoji.setOnClickListener(v->showEmoji(anchor));
        Button syms=key("#️⃣ بیش از 100 علامت",14,NAVY,CREAM); box.addView(syms,new LinearLayout.LayoutParams(-1,dp(48))); syms.setOnClickListener(v->showGridPopup(anchor,SYMBOLS,42,300));
        Button historyB=key("🕘 100 History",14,NAVY,CREAM); box.addView(historyB,new LinearLayout.LayoutParams(-1,dp(48))); historyB.setOnClickListener(v->showHistory(v));
        PopupWindow pw=new PopupWindow(box,dp(250),ViewGroup.LayoutParams.WRAP_CONTENT,true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(300));
    }

    private LinearLayout scrollBox(){android.widget.ScrollView sv=new android.widget.ScrollView(this); LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(6,6,6,6);box.setBackgroundColor(CREAM);sv.addView(box);return box;}
    private void addGrid(LinearLayout box,String[] items,int cell){LinearLayout row=null;int count=0;for(String item:items){if(count%7==0){row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);box.addView(row,new LinearLayout.LayoutParams(-1,dp(cell)));}Button b=key(item,20,NAVY,CREAM);row.addView(b,weight(1));b.setOnClickListener(v->commit(((Button)v).getText().toString()));count++;}}
    private void showGridPopup(View anchor,String[] items,int cell,int height){LinearLayout box=scrollBox();addGrid(box,items,cell);PopupWindow pw=new PopupWindow(box,dp(330),dp(height),true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(height));}
    private void stylePopup(PopupWindow pw){pw.setBackgroundDrawable(new ColorDrawable(CREAM));pw.setOutsideTouchable(true);pw.setElevation(dp(8));}

    private void showCalculator(View anchor){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(8,8,8,8);box.setBackgroundColor(CREAM);
        TextView display=new TextView(this);display.setText("0");display.setTextSize(24);display.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);box.addView(display,new LinearLayout.LayoutParams(-1,dp(55)));
        String[] keys={"7","8","9","÷","4","5","6","×","1","2","3","−","0",".","=","+","C"};
        for(int i=0;i<keys.length;i+=4){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);for(int j=i;j<Math.min(i+4,keys.length);j++){String k=keys[j];Button b=key(k,18,NAVY,CREAM);r.addView(b,weight(1));b.setOnClickListener(v->{String old=display.getText().toString();String kx=((Button)v).getText().toString();if(kx.equals("C"))display.setText("0");else if(kx.equals("="))display.setText(calculate(old));else display.setText(old.equals("0")?kx:old+kx);});}box.addView(r,new LinearLayout.LayoutParams(-1,dp(48)));}
        PopupWindow pw=new PopupWindow(box,dp(280),dp(360),true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(360));
    }
    private String calculate(String s){try{String e=s.replace("×","*").replace("÷","/").replace("−","-");double v=new SimpleExpression(e).parse();if(v==(long)v)return Long.toString((long)v);return Double.toString(v);}catch(Exception ex){return "Error";}}

    private void showColorTablet(View anchor){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(8,8,8,8);box.setBackgroundColor(CREAM);
        TextView title=new TextView(this);title.setText("انتخاب رنگ نمای کیبورد");title.setGravity(Gravity.CENTER);title.setTextSize(16);box.addView(title,new LinearLayout.LayoutParams(-1,dp(42)));
        int[] colors={0xFFFFFBF0,0xFFFFFFFF,0xFFFFF2CC,0xFFFFE4C4,0xFFFFD6D6,0xFFFFE0F0,0xFFE8D9FF,0xFFD9E8FF,0xFFD8F0FF,0xFFD8F5E5,0xFFE7F5D8,0xFFF5F5DC,0xFFE0E0E0,0xFFC8C8C8,0xFFB0BEC5,0xFF263238,0xFF102A43,0xFF1B4965,0xFF5C3D2E,0xFF6D597A,0xFF8D6E63,0xFF455A64,0xFF2E7D32,0xFF1565C0,0xFF6A1B9A,0xFFC62828,0xFFEF6C00,0xFFFFC107,0xFF00838F,0xFF00695C,0xFFAD1457,0xFF4E342E,0xFF37474F,0xFF1A237E,0xFF311B92,0xFF004D40,0xFF33691E,0xFF827717,0xFF3E2723,0xFF000000,0xFFFFFFFF};
        for(int i=0;i<colors.length;i+=5){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);for(int j=i;j<Math.min(i+5,colors.length);j++){final int c=colors[j];Button sw=new Button(this);sw.setBackground(makeBg(c));sw.setText("");r.addView(sw,weight(1));sw.setOnClickListener(v->{keyboardColor=c;prefs.edit().putInt("keyboardColor",c).apply();if(currentRoot!=null)currentRoot.setBackgroundColor(c);});}box.addView(r,new LinearLayout.LayoutParams(-1,dp(48)));}
        PopupWindow pw=new PopupWindow(box,dp(320),dp(430),true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(430));
    }

    private void showHistory(View anchor){if(history.isEmpty()){showGridPopup(anchor,new String[]{"تاریخچه خالی است"},42,120);return;}List<String> items=new ArrayList<>(history);Collections.reverse(items);if(items.size()>100)items=items.subList(0,100);showGridPopup(anchor,items.toArray(new String[0]),42,360);}
    private void addHistory(String s){if(TextUtils.isEmpty(s))return;history.add(s);while(history.size()>100)history.remove(0);prefs.edit().putString("history",TextUtils.join("\u0001",history)).apply();}
    private void loadHistory(){String all=prefs.getString("history","");if(!TextUtils.isEmpty(all))history.addAll(Arrays.asList(all.split("\u0001",-1)));while(history.size()>100)history.remove(0);}
    private int dp(int v){return Math.round(v*getResources().getDisplayMetrics().density);}

    private static class SimpleExpression{
        private final String s;private int p=0;SimpleExpression(String s){this.s=s.replace(" ","");}
        double parse(){double v=expr();if(p<s.length())throw new RuntimeException();return v;}
        double expr(){double v=term();while(p<s.length()){char c=s.charAt(p);if(c=='+'){p++;v+=term();}else if(c=='-'){p++;v-=term();}else break;}return v;}
        double term(){double v=factor();while(p<s.length()){char c=s.charAt(p);if(c=='*'){p++;v*=factor();}else if(c=='/'){p++;v/=factor();}else break;}return v;}
        double factor(){if(p<s.length()&&s.charAt(p)=='-'){p++;return -factor();}int st=p;while(p<s.length()&&(Character.isDigit(s.charAt(p))||s.charAt(p)=='.'))p++;if(st==p)throw new RuntimeException();return Double.parseDouble(s.substring(st,p));}
    }
}
