package com.fkp2.keyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastKeyboardService extends InputMethodService {
    private static final int NAVY = Color.rgb(23,61,112);
    private static final int BROWN = Color.rgb(117,61,18);
    private static final int RED = Color.rgb(215,20,20);
    private static final int CREAM = Color.rgb(250,249,242);
    private static final int YELLOW = Color.rgb(255,224,128);
    private static final int BLUE = Color.rgb(25,95,170);
    private static final int PINK = Color.rgb(252,220,220);

    private boolean caps=false, capsLocked=false, symbols=false, english=false;
    private long lastCapsTap=0;
    private int resizeLevel=0;
    private int normalWindowHeight=0;
    private final ArrayList<String> history=new ArrayList<>();
    private SharedPreferences prefs;
    private LinearLayout currentRoot;
    private int keyboardColor=CREAM;
    private final Handler handler=new Handler();

    private static final String[] PERSIAN_NUMBERS={"۱","۲","۳","۴","۵","۶","۷","۸","۹","۰"};
    private static final String[] NUMBER_MARKS={"!","@","#","$","%","^","&","*","(",")"};
    private static final String[] PERSIAN_R1={"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"};
    private static final String[] PERSIAN_R2={"ش","س","ی","ب","ل","ا","ت","ن","م","ک","گ"};
    private static final String[] PERSIAN_R3={"ظ","ط","ژ","ز","ر","ذ","د","پ","و","چ"};
    private static final String[] PERSIAN_MARKS_R1={"!","@","#","$","%","^","&","*","(",")","["};
    private static final String[] PERSIAN_MARKS_R2={"]","{","}","<",">","=","+","-","_","/","\\"};
    private static final String[] EN_R1={"q","w","e","r","t","y","u","i","o","p","["};
    private static final String[] EN_R2={"a","s","d","f","g","h","j","k","l",";","'"};
    private static final String[] EN_R3={"z","x","c","v","b","n","m",",",".","/"};
    private static final String[] EN_MARKS_R1={"!","@","#","$","%","^","&","*","(",")","["};
    private static final String[] EN_MARKS_R2={"@","#","$","%","&","*","-","_",";",":","'"};
    private static final String[] EN_MARKS_R3={"<",">","{","}","[","]","\\","|","?","/"};

    private static final String[] ALIF_VARIANTS={"ا","آ","أ","إ","ٱ","ؤ","ئ"};
    private static final String[] ARABIC_MARKS={"َ","ِ","ُ","ً","ٍ","ٌ","ْ","ّ","ٰ","ٔ","ٕ","ٖ","ٗ","٘","ٙ","ٚ","ٛ","ٜ","ٝ","ٞ","ٟ","ـ","ء","آ","أ","ؤ","إ","ئ","ة","ى","لا"};

    private static final String[] EMOJIS={"😀","😃","😄","😁","😆","😅","😂","🤣","😊","😇","🙂","🙃","😉","😌","😍","🥰","😘","😗","😙","😚","😋","😛","😝","😜","🤪","🤨","🧐","🤓","😎","🤩","🥳","😏","😒","😞","😔","😟","😕","🙁","☹️","😣","😖","😫","😩","🥺","😢","😭","😤","😠","😡","🤬","🤯","😳","🥵","🥶","😱","😨","😰","😥","😓","🤗","🤔","🤭","🤫","🤥","😶","😐","😑","😬","🙄","😯","😦","😧","😮","😲","🥱","😴","🤤","😪","😵","🤐","🥴","🤢","🤮","🤧","😷","🤒","🤕","🤑","🤠","😈","👿","👹","👺","🤡","💩","👻","💀","☠️","👽","👾","🤖","🎃","😺","😸","😹","😻","😼","😽","🙀","😿","😾","🙈","🙉","🙊","💋","💘","💝","💖","💗","💓","💞","💕","💟","❣️","💔","❤️","🧡","💛","💚","💙","💜","🖤","🤍","🤎","💯","💥","💫","💦","💨","💣","💬","👋","🤚","🖐️","✋","🖖","👌","🤏","✌️","🤞","🤟","🤘","🤙","👈","👉","👆","👇","☝️","👍","👎","✊","👊","🤝","🙏","👏","🙌","💪","👀","🧠","👄","👅","👂","👃","👶","🧒","👦","👧","🧑","👨","👩","🧓","👴","👵","🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼","🐨","🐯","🦁","🐮","🐷","🐸","🐵","🐔","🐧","🐦","🐤","🦄","🐝","🦋","🐌","🐞","🐜","🐢","🐍","🦎","🦂","🐙","🦀","🐠","🐟","🐬","🐳","🐊","🐘","🦏","🦒","🦓","🐎","🐕","🐈","🐓","🦜","🦢","🌹","🌷","🌻","🌞","🌈","☀️","⭐","🌟","✨","⚡","❄️","🔥","🌊","🍎","🍊","🍋","🍉","🍇","🍓","🍒","🍑","🍍","🥝","🍅","🥑","🍞","🧀","🍔","🍕","🍟","🌭","🍿","🍩","🍪","🎂","🍰","🍫","🍬","☕","🍵","⚽","🏀","🏈","⚾","🎾","🏐","🏆","🥇","🚗","🚕","🚌","🚓","🚑","🚒","✈️","🚁","🚀","🚲","🏠","🏢","🏥","🏫","⛪","🕌","🛒","📱","💻","⌚","📷","📺","🎧","🎵","🎶","🎸","🎹","🎮","🎲","🎯","🎁","🎈","🎉","🎊","📌","📍","🔑","🔒","🔓","⚙️","🔔","🔍","🔎","💡","📁","📂","🗂️","🗃️","🗄️","📦","🗑️","📝","📄","📋","📎","📚","📖"};
    private static final String[] FLAGS={"🇮🇷","🇺🇸","🇬🇧","🇨🇦","🇦🇺","🇩🇪","🇫🇷","🇮🇹","🇪🇸","🇵🇹","🇹🇷","🇷🇺","🇺🇦","🇨🇳","🇯🇵","🇰🇷","🇮🇳","🇵🇰","🇦🇫","🇮🇶","🇸🇦","🇦🇪","🇶🇦","🇰🇼","🇧🇭","🇴🇲","🇪🇬","🇯🇴","🇱🇧","🇸🇾","🇵🇸","🇬🇷","🇳🇱","🇧🇪","🇨🇭","🇦🇹","🇸🇪","🇳🇴","🇩🇰","🇫🇮","🇵🇱","🇨🇿","🇭🇺","🇷🇴","🇧🇬","🇷🇸","🇭🇷","🇦🇱","🇧🇦","🇬🇪","🇦🇲","🇦🇿","🇰🇿","🇺🇿","🇹🇯","🇹🇲","🇰🇬","🇳🇿","🇿🇦","🇳🇬","🇰🇪","🇲🇦","🇩🇿","🇹🇳","🇧🇷","🇦🇷","🇨🇱","🇨🇴","🇲🇽","🇺🇾","🇻🇪","🇵🇪","🇨🇺","🇯🇲","🇸🇬","🇲🇾","🇮🇩","🇹🇭","🇻🇳","🇵🇭"};
    private static final String[] SYMBOLS={"!","@","#","$","%","^","&","*","(",")","-","_","+","=","[","]","{","}","\\","|",";",":","'","\"",",",".","<",">","/","?","~","`","§","¶","©","®","™","€","£","¥","₽","₹","₺","₩","₴","₦","₱","₲","₵","₡","₫","฿","∞","≈","≠","≤","≥","±","×","÷","√","∑","∏","∆","∇","∂","∫","∮","π","µ","Ω","α","β","γ","δ","θ","λ","σ","φ","ψ","ω","←","↑","→","↓","↔","↕","↖","↗","↘","↙","⇐","⇑","⇒","⇓","↻","↺","✓","✔","✕","✖","✗","✘","★","☆","●","○","■","□","◆","◇","▲","△","▼","▽","♥","♡","♦","♢","♣","♤","♧","☀","☁","☂","☃","☄","☎","☑","☒","☐","⚠","⚡","⚙","⚓","⚽","♠","♣","♥","♦","♪","♫","†","‡","‰","′","″","↪","↩","⌂","⌘","⌫","⏎","␣","◀","▶","⏪","⏩","⏮","⏭","⏸","⏹","⏺","🔒","🔓","🔑","🔔","🔕","🔗","🗝️"};

    @Override public void onCreate(){super.onCreate();prefs=getSharedPreferences("fkp2",Context.MODE_PRIVATE);loadHistory();keyboardColor=prefs.getInt("keyboardColor",CREAM);}
    @Override public View onCreateInputView(){return buildKeyboard();}
    @Override public void onStartInputView(EditorInfo info,boolean restarting){super.onStartInputView(info,restarting);if(restarting)rebuild();}

    private LinearLayout buildKeyboard(){
        LinearLayout root=new LinearLayout(this);currentRoot=root;root.setOrientation(LinearLayout.VERTICAL);root.setPadding(1,1,1,1);root.setBackgroundColor(keyboardColor);root.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        root.post(() -> { if(normalWindowHeight<=0 && root.getHeight()>0) normalWindowHeight=root.getHeight(); });
        LinearLayout tools=row(1.05f);
        String[] labels={"Copy\nAll","Copy\nScreen","Paste","Cut","Undo","Redo","100\nHistory","امکانات","➤","Resize"};
        String[] icons={"⧉","▣","▣","✂","↶","↷","▤","⚙","➤","↕"};
        for(int i=0;i<labels.length;i++){Button b=keyWithIcon(labels[i],icons[i],12,NAVY,CREAM);tools.addView(b,weight(1));final int n=i;switch(n){case 0:b.setOnClickListener(v->copyAll());break;case 1:b.setOnClickListener(v->copyAll());break;case 2:b.setOnClickListener(v->paste());break;case 3:b.setOnClickListener(v->cut());break;case 4:b.setOnClickListener(v->ctrlKey(KeyEvent.KEYCODE_Z));break;case 5:b.setOnClickListener(v->ctrlKey(KeyEvent.KEYCODE_Y));break;case 6:b.setOnClickListener(v->showHistory(v));break;case 7:b.setOnClickListener(v->showTools(v));break;case 8:b.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_RIGHT));break;default:b.setOnClickListener(v->toggleResize());}}
        root.addView(tools);
        LinearLayout suggestions=row(.62f);for(String s:new String[]{"سلام","سلامت","سلامتی","من","مهم","منطقه",""}){Button b=key(s,14,NAVY,CREAM);suggestions.addView(b,weight(1));if(!s.isEmpty())b.setOnClickListener(v->commit(((Button)v).getText().toString()));}root.addView(suggestions);
        LinearLayout nums=row(1f);String[] numsText=english?new String[]{"1","2","3","4","5","6","7","8","9","0"}:PERSIAN_NUMBERS;for(int i=0;i<10;i++){Button b=dualKey(numsText[i],NUMBER_MARKS[i],19,BROWN,RED,CREAM);nums.addView(b,weight(1));b.setOnClickListener(v->commit(((Button)v).getTag().toString()));addMarkRepeat(b,NUMBER_MARKS[i]);}Button back=key("⌫",22,NAVY,PINK);nums.addView(back,weight(1.45f));addBackspaceRepeat(back);root.addView(nums);
        LinearLayout letters=new LinearLayout(this);letters.setOrientation(LinearLayout.HORIZONTAL);letters.setLayoutParams(new LinearLayout.LayoutParams(-1,0,2f));
        LinearLayout letterRows=new LinearLayout(this);letterRows.setOrientation(LinearLayout.VERTICAL);letterRows.setLayoutParams(new LinearLayout.LayoutParams(0,-1,11f));
        addLetterRow(letterRows,english?EN_R1:PERSIAN_R1,english?EN_MARKS_R1:PERSIAN_MARKS_R1);addLetterRow(letterRows,english?EN_R2:PERSIAN_R2,english?EN_MARKS_R2:PERSIAN_MARKS_R2);
        letters.addView(letterRows);Button enter=key("Enter",16,NAVY,Color.rgb(214,232,255));letters.addView(enter,new LinearLayout.LayoutParams(0,-1,1.2f));enter.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_ENTER));root.addView(letters);
        LinearLayout third=row(1f);Button capsB=key(capsLocked?"Caps 🔒":"Caps",16,NAVY,caps?YELLOW:CREAM);third.addView(capsB,weight(1.2f));capsB.setOnClickListener(v->{long now=android.os.SystemClock.uptimeMillis();if(now-lastCapsTap<320){capsLocked=!capsLocked;caps=capsLocked;lastCapsTap=0;}else{caps=!caps;lastCapsTap=now;}rebuild();});String[] r3=english?EN_R3:PERSIAN_R3;for(int i=0;i<r3.length;i++){String s=caps?r3[i].toUpperCase():r3[i];String mark=(english?EN_MARKS_R3:PERSIAN_MARKS_R1)[i%11];Button b=dualKey(s,mark,20,NAVY,RED,CREAM);b.setOnClickListener(v->commit(((Button)v).getTag().toString()));addMarkRepeat(b,mark);third.addView(b,weight(1));}Button qmark=key("؟",20,RED,CREAM);third.addView(qmark,weight(1));qmark.setOnClickListener(v->commit("؟"));root.addView(third);
        LinearLayout bottom=row(1.08f);Button emoji=keyWithIcon("اموجی","☺",14,NAVY,CREAM);Button sym=keyWithIcon("123\n!@...","⌘",13,NAVY,symbols?YELLOW:CREAM);Button globe=key(english?"🌐 EN":"🌐 FA",18,BLUE,CREAM);Button space=key("Space",19,NAVY,CREAM);Button comma=key(english?",":"،",23,RED,CREAM);Button question=key(".",23,RED,CREAM);Button pm=key("+\n−",18,RED,CREAM);Button left=key("←",23,BLUE,CREAM);Button right=key("→",23,BLUE,CREAM);Button up=key("↑",23,BLUE,CREAM);Button down=key("↓",23,BLUE,CREAM);bottom.addView(emoji,weight(.82f));bottom.addView(sym,weight(1.15f));bottom.addView(globe,weight(.9f));bottom.addView(space,weight(2.35f));bottom.addView(comma,weight(.72f));bottom.addView(question,weight(.72f));bottom.addView(pm,weight(.72f));bottom.addView(left,weight(.95f));bottom.addView(right,weight(.95f));bottom.addView(up,weight(.82f));bottom.addView(down,weight(.82f));emoji.setOnClickListener(v->showEmoji(v));sym.setOnClickListener(v->showSymbols(v));globe.setOnClickListener(v->{english=!english;symbols=false;rebuild();});space.setOnClickListener(v->commit(" "));comma.setOnClickListener(v->commit(((Button)v).getText().toString()));question.setOnClickListener(v->commit("."));pm.setOnClickListener(v->commit("±"));left.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_LEFT));right.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_RIGHT));up.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_UP));down.setOnClickListener(v->sendKey(KeyEvent.KEYCODE_DPAD_DOWN));root.addView(bottom);return root;
    }

    private void addLetterRow(LinearLayout parent,String[] letters,String[] marks){LinearLayout r=row(1f);for(int i=0;i<letters.length;i++){String s=caps?letters[i].toUpperCase():letters[i];Button b=dualKey(s,marks[i],22,NAVY,RED,CREAM);r.addView(b,weight(1));final String out=s;b.setOnClickListener(v->{commit(out);if(!capsLocked&&caps){caps=false;rebuild();}});if(!english&&s.equals("ا")){addAlifLongPress(b);}else{addMarkRepeat(b,marks[i]);}}parent.addView(r);}
    private Button dualKey(String main,String mark,float size,int fg,int markColor,int bg){DualButton b=new DualButton(this);b.setMainMark(main,mark,size,fg,markColor);b.setAllCaps(false);b.setTypeface(Typeface.create("sans",Typeface.NORMAL));b.setPadding(0,0,0,0);b.setMinHeight(0);b.setMinWidth(0);b.setTag(main);b.setBackground(makeBg(bg));installHighlight(b);return b;}
    private static class DualButton extends Button {
        private String main="", mark=""; private float mainSize=20; private int mainColor=Color.BLACK, markColor=Color.RED;
        DualButton(Context c){super(c);setWillNotDraw(false);setText("");}
        void setMainMark(String m,String k,float size,int mc,int kc){main=m;mark=k;mainSize=size;mainColor=mc;markColor=kc;invalidate();}
        @Override protected void onDraw(android.graphics.Canvas c){super.onDraw(c);android.graphics.Paint p=new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);p.setTypeface(Typeface.create("sans",Typeface.NORMAL));p.setTextAlign(android.graphics.Paint.Align.CENTER);p.setTextSize(mainSize*getResources().getDisplayMetrics().scaledDensity);p.setColor(mainColor);float cy=getHeight()/2f-(p.ascent()+p.descent())/2f;c.drawText(main,getWidth()/2f,cy,p);p.setTextAlign(android.graphics.Paint.Align.LEFT);p.setTextSize(mainSize*.58f*getResources().getDisplayMetrics().scaledDensity);p.setColor(markColor);c.drawText(mark,dpStatic(getResources(),6),getHeight()-dpStatic(getResources(),5),p);}
        private static int dpStatic(android.content.res.Resources r,int v){return Math.round(v*r.getDisplayMetrics().density);}
    }
    private Button key(String text,float size,int fg,int bg){Button b=new Button(this);b.setText(text);b.setTextSize(size);b.setTextColor(fg);b.setGravity(Gravity.CENTER);b.setAllCaps(false);b.setTypeface(Typeface.create("sans",Typeface.NORMAL));b.setPadding(0,0,0,0);b.setMinHeight(0);b.setMinWidth(0);b.setIncludeFontPadding(true);b.setTag(bg);b.setBackground(makeBg(bg));installHighlight(b);return b;}
    private Button keyWithIcon(String text,String icon,float size,int fg,int bg){Button b=key(text,size,fg,bg);b.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);b.setContentDescription(text+" "+icon);return b;}
    private void installHighlight(Button b){b.setOnTouchListener((v,e)->{if(e.getAction()==MotionEvent.ACTION_DOWN){b.setBackground(makeBg(YELLOW));}else if(e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL){v.postDelayed(()->{Object t=b.getTag();b.setBackground(makeBg(t instanceof Integer?(Integer)t:CREAM));},80);}return false;});}
    private void addBackspaceRepeat(Button b){final boolean[] repeating={false};final Runnable[] repeat={null};repeat[0]=()->{repeating[0]=true;backspace();handler.postDelayed(repeat[0],90);};b.setOnTouchListener((v,e)->{if(e.getAction()==MotionEvent.ACTION_DOWN){b.setBackground(makeBg(YELLOW));repeating[0]=false;backspace();handler.postDelayed(repeat[0],420);return true;}if(e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL){handler.removeCallbacks(repeat[0]);b.setBackground(makeBg(PINK));return true;}return true;});}
    private void addMarkRepeat(Button b,String mark){
        final boolean[] repeating={false}; final Runnable[] repeat={null};
        repeat[0]=()->{repeating[0]=true;commit(mark);handler.postDelayed(repeat[0],110);};
        b.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){b.setBackground(makeBg(YELLOW));repeating[0]=false;handler.postDelayed(repeat[0],350);return true;}
            if(e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL){handler.removeCallbacks(repeat[0]);if(!repeating[0]){commit(((Button)v).getTag().toString());}Object t=b.getTag();b.setBackground(makeBg(t instanceof Integer?(Integer)t:CREAM));return true;}
            return true;
        });
    }
    private void showSymbols(View anchor){symbols=true;showRepeatGridPopup(anchor,SYMBOLS,42,300);}

    private void addAlifLongPress(Button b){final boolean[] shown={false};final Runnable[] r={null};r[0]=()->{shown[0]=true;showAlifVariants(b);};b.setOnTouchListener((v,e)->{if(e.getAction()==MotionEvent.ACTION_DOWN){shown[0]=false;handler.postDelayed(r[0],450);b.setBackground(makeBg(YELLOW));return true;}if(e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL){handler.removeCallbacks(r[0]);Object t=b.getTag();b.setBackground(makeBg(t instanceof Integer?(Integer)t:CREAM));if(!shown[0]){b.performClick();}return true;}return true;});}
    private void showAlifVariants(View anchor){showGridPopup(anchor,ALIF_VARIANTS,52,150);}
    private LinearLayout row(float w){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);r.setGravity(Gravity.FILL);r.setPadding(0,0,0,0);r.setLayoutParams(new LinearLayout.LayoutParams(-1,0,w));return r;}
    private LinearLayout.LayoutParams weight(float w){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-1,w);p.setMargins(0,0,0,0);return p;}
    private GradientDrawable makeBg(int color){GradientDrawable gd=new GradientDrawable();gd.setColor(color);gd.setCornerRadius(8);gd.setStroke(1,Color.rgb(210,208,200));return gd;}
    private void rebuild(){setInputView(buildKeyboard());}
    private void commit(String s){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.commitText(s,1);if(!s.equals(" "))addHistory(s);}}
    private void backspace(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.deleteSurroundingText(1,0);}
    private void sendKey(int code){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,code));ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,code));}}
    private void ctrlKey(int code){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.sendKeyEvent(new KeyEvent(0,0,KeyEvent.ACTION_DOWN,code,0,KeyEvent.META_CTRL_ON));ic.sendKeyEvent(new KeyEvent(0,0,KeyEvent.ACTION_UP,code,0,KeyEvent.META_CTRL_ON));}}
    private void copyAll(){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.performContextMenuAction(android.R.id.selectAll);ic.performContextMenuAction(android.R.id.copy);}}
    private void paste(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.performContextMenuAction(android.R.id.paste);}
    private void cut(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.performContextMenuAction(android.R.id.cut);}
    private void toggleResize(){resizeLevel++;if(resizeLevel>3)resizeLevel=0;if(resizeLevel==0){int h=normalWindowHeight>0?normalWindowHeight:dp(360);getWindow().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,h);return;}int h;switch(resizeLevel){case 1:h=dp(280);break;case 2:h=dp(220);break;default:h=dp(160);break;}getWindow().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,h);}

    private void showEmoji(View anchor){ScrollView sv=scrollBox();LinearLayout box=gridContainer(sv);addGrid(box,EMOJIS,40);addGrid(box,FLAGS,40);PopupWindow pw=new PopupWindow(sv,dp(330),dp(420),true);stylePopup(pw);sv.post(()->pw.showAsDropDown(anchor,0,-dp(420)));}
    private void showTools(View anchor){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(6,6,6,6);box.setBackgroundColor(CREAM);String[][] tools={{"⚙","اعراب و علائم عربی"},{"▦","ماشین حساب"},{"🎨","رنگ نمای کیبورد"},{"☺","اموجی و پرچم‌ها"},{"#","بیش از 100 علامت"},{"▤","100 History"}};for(String[] t:tools){Button b=keyWithIcon(t[1],t[0],14,NAVY,CREAM);box.addView(b,new LinearLayout.LayoutParams(-1,dp(48)));if(t[1].startsWith("اعراب"))b.setOnClickListener(v->showGridPopup(v,ARABIC_MARKS,44,220));else if(t[1].equals("ماشین حساب"))b.setOnClickListener(v->showCalculator(v));else if(t[1].startsWith("رنگ"))b.setOnClickListener(v->showColorTablet(v));else if(t[1].startsWith("اموجی"))b.setOnClickListener(v->showEmoji(v));else if(t[1].startsWith("بیش"))b.setOnClickListener(v->showRepeatGridPopup(v,SYMBOLS,42,300));else b.setOnClickListener(v->showHistory(v));}PopupWindow pw=new PopupWindow(box,dp(260),-2,true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(300));}
    private ScrollView scrollBox(){ScrollView sv=new ScrollView(this);LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(6,6,6,6);box.setBackgroundColor(CREAM);sv.addView(box);return sv;}
    private LinearLayout gridContainer(ScrollView sv){return (LinearLayout)sv.getChildAt(0);}
    private void addGrid(LinearLayout box,String[] items,int cell){LinearLayout r=null;int count=0;for(String item:items){if(count%7==0){r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);box.addView(r,new LinearLayout.LayoutParams(-1,dp(cell)));}Button b=key(item,20,NAVY,CREAM);r.addView(b,weight(1));b.setOnClickListener(v->commit(((Button)v).getText().toString()));count++;}}
    private void showGridPopup(View anchor,String[] items,int cell,int height){ScrollView sv=scrollBox();addGrid(gridContainer(sv),items,cell);PopupWindow pw=new PopupWindow(sv,dp(330),dp(height),true);stylePopup(pw);sv.post(()->pw.showAsDropDown(anchor,0,-dp(height)));}
private void showRepeatGridPopup(View anchor,String[] items,int cell,int height){ScrollView sv=scrollBox();addRepeatGrid(gridContainer(sv),items,cell);PopupWindow pw=new PopupWindow(sv,dp(330),dp(height),true);stylePopup(pw);sv.post(()->pw.showAsDropDown(anchor,0,-dp(height)));}
    private void addRepeatGrid(LinearLayout box,String[] items,int cell){LinearLayout r=null;int count=0;for(String item:items){if(count%7==0){r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);box.addView(r,new LinearLayout.LayoutParams(-1,dp(cell)));}Button b=key(item,20,NAVY,CREAM);r.addView(b,weight(1));b.setOnClickListener(v->commit(((Button)v).getText().toString()));addMarkRepeat(b,item);count++;}}
    private void stylePopup(PopupWindow pw){pw.setBackgroundDrawable(new ColorDrawable(CREAM));pw.setOutsideTouchable(true);pw.setElevation(dp(8));}
    private void showCalculator(View anchor){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(8,8,8,8);box.setBackgroundColor(CREAM);TextView display=new TextView(this);display.setText("0");display.setTextSize(24);display.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);box.addView(display,new LinearLayout.LayoutParams(-1,dp(55)));String[] ks={"7","8","9","÷","4","5","6","×","1","2","3","−","0",".","=","+","C"};for(int i=0;i<ks.length;i+=4){LinearLayout r=new LinearLayout(this);for(int j=i;j<Math.min(i+4,ks.length);j++){String k=ks[j];Button b=key(k,18,NAVY,CREAM);r.addView(b,weight(1));b.setOnClickListener(v->{String old=display.getText().toString();String x=((Button)v).getText().toString();if(x.equals("C"))display.setText("0");else if(x.equals("="))display.setText(calculate(old));else display.setText(old.equals("0")?x:old+x);});}box.addView(r,new LinearLayout.LayoutParams(-1,dp(48)));}PopupWindow pw=new PopupWindow(box,dp(280),dp(360),true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(360));}
    private String calculate(String s){try{String e=s.replace("×","*").replace("÷","/").replace("−","-");double v=new SimpleExpression(e).parse();return v==(long)v?Long.toString((long)v):Double.toString(v);}catch(Exception e){return "Error";}}
    private void showColorTablet(View anchor){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(8,8,8,8);box.setBackgroundColor(CREAM);TextView title=new TextView(this);title.setText("انتخاب رنگ نمای کیبورد");title.setGravity(Gravity.CENTER);title.setTextSize(16);box.addView(title,new LinearLayout.LayoutParams(-1,dp(42)));int[] colors={0xFFFFFBF0,0xFFFFFFFF,0xFFFFF2CC,0xFFFFE4C4,0xFFFFD6D6,0xFFFFE0F0,0xFFE8D9FF,0xFFD9E8FF,0xFFD8F0FF,0xFFD8F5E5,0xFFE7F5D8,0xFFF5F5DC,0xFFE0E0E0,0xFFC8C8C8,0xFFB0BEC5,0xFF263238,0xFF102A43,0xFF1B4965,0xFF5C3D2E,0xFF6D597A,0xFF8D6E63,0xFF455A64,0xFF2E7D32,0xFF1565C0,0xFF6A1B9A,0xFFC62828,0xFFEF6C00,0xFFFFC107,0xFF00838F,0xFF00695C,0xFFAD1457,0xFF4E342E,0xFF37474F,0xFF1A237E,0xFF311B92,0xFF004D40,0xFF33691E,0xFF827717,0xFF3E2723,0xFF000000};for(int i=0;i<colors.length;i+=5){LinearLayout r=new LinearLayout(this);for(int j=i;j<Math.min(i+5,colors.length);j++){final int c=colors[j];Button sw=key("",1,NAVY,c);r.addView(sw,weight(1));sw.setOnClickListener(v->{keyboardColor=c;prefs.edit().putInt("keyboardColor",c).apply();if(currentRoot!=null)currentRoot.setBackgroundColor(c);});}box.addView(r,new LinearLayout.LayoutParams(-1,dp(48)));}PopupWindow pw=new PopupWindow(box,dp(320),dp(430),true);stylePopup(pw);pw.showAsDropDown(anchor,0,-dp(430));}
    private void showHistory(View anchor){if(history.isEmpty()){showGridPopup(anchor,new String[]{"تاریخچه خالی است"},42,120);return;}List<String> items=new ArrayList<>(history);Collections.reverse(items);if(items.size()>100)items=items.subList(0,100);showGridPopup(anchor,items.toArray(new String[0]),42,360);}
    private void addHistory(String s){if(TextUtils.isEmpty(s))return;history.add(s);while(history.size()>100)history.remove(0);prefs.edit().putString("history",TextUtils.join("\u0001",history)).apply();}
    private void loadHistory(){String all=prefs.getString("history","");if(!TextUtils.isEmpty(all))history.addAll(Arrays.asList(all.split("\u0001",-1)));while(history.size()>100)history.remove(0);}
    private int dp(int v){return Math.round(v*getResources().getDisplayMetrics().density);}
    private static class SimpleExpression{private final String s;private int p=0;SimpleExpression(String s){this.s=s.replace(" ","");}double parse(){double v=expr();if(p<s.length())throw new RuntimeException();return v;}double expr(){double v=term();while(p<s.length()){char c=s.charAt(p);if(c=='+'){p++;v+=term();}else if(c=='-'){p++;v-=term();}else break;}return v;}double term(){double v=factor();while(p<s.length()){char c=s.charAt(p);if(c=='*'){p++;v*=factor();}else if(c=='/'){p++;v/=factor();}else break;}return v;}double factor(){if(p<s.length()&&s.charAt(p)=='-'){p++;return -factor();}int st=p;while(p<s.length()&&(Character.isDigit(s.charAt(p))||s.charAt(p)=='.'))p++;if(st==p)throw new RuntimeException();return Double.parseDouble(s.substring(st,p));}}
}
