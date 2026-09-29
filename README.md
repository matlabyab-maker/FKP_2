# FKP_2 — Fast Keyboard

نسخه 1.1.1 — نسخه اصلاح‌شده پایه متوسط

این نسخه بر پایه نسخه 1.1 ساخته شده و خطای کامپایل `onStartInputView` اصلاح شده است.

## روش کار
1. پروژه را در مخزن FKP_2 قرار دهید.
2. در MGit تغییرات را `All to Stage` کنید.
3. Commit با هر نام دلخواه بزنید.
4. Push کنید.
5. در GitHub، Workflow با نام `FKP_2 Android Build` به‌صورت خودکار اجرا می‌شود.
6. APK ساخته‌شده از بخش Artifacts قابل دریافت است.

نام Commit هیچ وابستگی به نام Workflow ندارد.

## نکته نصب
پس از نصب APK، در تنظیمات Android بخش Keyboard/Input Method، کیبورد FKP_2 را فعال کنید و سپس آن را از انتخابگر کیبورد انتخاب کنید.

## مرجع طراحی
چیدمان و قواعد پروژه باید مطابق سند مرجع Fast Keyboard باقی بماند و تغییرات بعدی فقط بر اساس درخواست صریح کاربر انجام شود.


## Version 1.2
- English/Persian keyboard toggle via Globe.
- Enter key added.
- Yellow flash across all keyboard keys on any key press.
- Emoji collection expanded beyond 100 entries, with country flags and folder/file icons.
- Symbol collection expanded beyond 100 entries.
- Arabic marks moved into the امکانات drawer.
- Simple calculator added to امکانات.
- Keyboard appearance color tablet added to امکانات.


## FKP_2 v1.3 – تغییرات این نسخه
- چیدمان اصلی بر اساس تصویر مرجع کاربر تنظیم شده است.
- هایلایت زرد فقط روی همان کلیدی که لمس می‌شود نمایش داده می‌شود.
- ردیف Enter در سمت راست دو ردیف حروف قرار گرفته و ارتفاع آن دو ردیف را پوشش می‌دهد.
- Backspace با نگه‌داشتن، حذف تکرارشونده انجام می‌دهد.
- اعداد دارای علامت دوم کوچک و قرمز هستند؛ کلیدهای حروف نیز علامت دوم دارند.
- زبان انگلیسی با حروف کوچک شروع می‌شود و Caps برای حروف بزرگ است.
- نگه‌داشتن حرف «ا» انتخابگر گونه‌های ا، آ، أ، إ، ٱ، ؤ، ئ را باز می‌کند.
- ماشین حساب ساده در کشوی «امکانات» قرار دارد.
- برای برنامه آیکون اختصاصی اضافه شده است.
- مجموعه Emoji، پرچم‌ها و بیش از 100 نماد در بخش‌های مربوط قرار دارند.

## FKP_2 v1.6 – Predictive suggestions
- Added a lightweight offline Persian next-word predictor using a small built-in bigram model.
- Example seed: after «شب», suggestions include «و» and «روز».
- Suggestions are refreshed from the current text before the cursor through `onUpdateSelection`.
- The model learns simple word pairs from text when Space is pressed and stores learned pairs locally in SharedPreferences.
- Question and exclamation context can add `؟` or `!` to the suggestion row.
- This is intentionally a lightweight offline predictor; it is not a full neural NLP model and does not claim full semantic understanding.


## FKP_2 v1.7
- وارد کردن فهرست واژگان فارسی به مخزن پیشنهادها از assets/suggestions_fa.txt
- حفظ پیشنهادهای زمینه‌ای N-gram و استفاده از واژگان پایه برای پیشنهاد عمومی

### Suggestion dictionary update
- Added 81 new unique user-provided Persian entries to `app/src/main/assets/suggestions_fa.txt`.
- Existing entries and duplicates were preserved/ignored; the user-provided spellings were kept as supplied.


## FKP_2 v1.8 – رنگ‌ها در واژگان پیشنهادی
- واژه «لازی» طبق درخواست کاربر حذف شد.
- ۱۲ نام رنگ پایه به فهرست پیشنهادها اضافه شد: قرمز، نارنجی، زرد، سبز، آبی، نیلی، بنفش، صورتی، قهوه‌ای، مشکی، سفید، خاکستری؛
- فهرست پیشنهادها پس از حذف موارد تکراری و افزودن رنگ‌ها، 1500 مورد یکتا دارد.


## FKP_2 v1.9 – اشیاء و عناصر رایج بازی‌ها
- 98 واژه/عبارت جدید مرتبط با محیط، اشیاء، وسایل نقلیه، شخصیت‌ها، حیوانات و عناصر رایج بازی‌ها به فرهنگ پیشنهادها اضافه شد.
- تعداد کل موارد یکتا: 2910


## FKP_2 v2.0 – واژه‌نامه گسترده برنامه‌سازی، بازی‌سازی، GitHub و MGit
- واژه‌نامه پیشنهادی `app/src/main/assets/suggestions_fa.txt` گسترش داده شد.
- تعداد مدخل‌های یکتای واژه‌نامه: **2910**.
- اصطلاحات برنامه‌نویسی و توسعه نرم‌افزار: Java، Kotlin، Android، JavaScript، Python، C/C++، Rust، Go، Swift، Dart، PHP، SQL، API، JSON، XML، HTML، CSS و مفاهیم کدنویسی، معماری، دیتابیس، شبکه، تست و خطایابی.
- اصطلاحات Android و ساخت APK/AAB: SDK، JDK، Gradle، Manifest، Activity، Service، Permission، Resource، Build، Debug، Release، Signing، Keystore و موارد مرتبط.
- اصطلاحات بازی‌سازی: Game Development، Game Engine، Unity، Unreal Engine، Godot، Gameplay، Player، NPC، Quest، Level، Map، Inventory، Weapon، Health، Damage، AI، Physics، Collision، Collider، Rigidbody، Raycast، Camera، Shader، Material، Texture، Animation، Particle، Lighting، Audio و موارد دیگر.
- اصطلاحات Git و GitHub: Repository، Branch، Clone، Fork، Commit، Push، Pull، Fetch، Merge، Rebase، Reset، Revert، Diff، Log، Tag، Stash، Cherry-pick، Pull Request، Issue، Review، Actions، Workflow، Runner، Job، Step، Artifact، Release، Projects، Discussions، Pages، Wiki، Security، Secrets، API و موارد دیگر.
- اصطلاحات مربوط به MGit و منوی سه‌خط آن نیز به‌طور ویژه اضافه شده‌اند؛ از جمله Repository، Clone Repository، Init Repository، Commit Changes، Push Changes، Pull Changes، Fetch Changes، Merge، Branches، New Branch، Rename Branch، Delete Branch، Checkout Branch، Tag، Reset، Revert، Cherry Pick، Stash، Log، Diff، Changes، Files، Working Tree، Staging Area، Remote، Add/Edit/Remove Remote، Credentials، Authentication، SSH Key، Settings، Preferences، Help و موارد مرتبط.
- این موارد به خود فایل واژه‌نامه اضافه شده‌اند تا در پیشنهادهای کیبورد قابل استفاده باشند، نه فقط در README.
