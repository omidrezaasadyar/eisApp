# EIS Services — Canonical Copy

The catalogue is **fixed at nine services** and ordered as listed below.
Do not invent, merge, split, or paraphrase them. Localised copy lives in
`app/src/main/res/values/strings.xml` (English) and
`app/src/main/res/values-ar/strings.xml` (Arabic), keyed by
`ServiceId.key`.

| # | ServiceId | key | English title | Arabic title |
|---|---|---|---|---|
| 1 | `INDUSTRIAL_SOFTWARE` | `industrial_software` | Industrial Software & Digital Solutions | البرمجيات الصناعية والحلول الرقمية |
| 2 | `AUTOMATION` | `automation` | Automation & Process Optimization | الأتمتة وتحسين العمليات |
| 3 | `AI_DATA` | `ai_data` | AI & Data Intelligence for Industry | الذكاء الاصطناعي وذكاء البيانات للصناعة |
| 4 | `INTEGRATION_MIDDLEWARE` | `integration_middleware` | Industrial Integration & Middleware Solutions | حلول التكامل والوسيط البرمجي الصناعي |
| 5 | `ENGINEERING_CONSULTING` | `engineering_consulting` | Engineering Consulting & Technical Services | الاستشارات الهندسية والخدمات الفنية |
| 6 | `MONITORING_KPI` | `monitoring_kpi` | Monitoring, KPI & Decision Support Systems | المراقبة ومؤشرات الأداء وأنظمة دعم القرار |
| 7 | `EQUIPMENT_TRADING` | `equipment_trading` | Industrial Equipment Trading & Supply | تجارة وتوريد المعدات الصناعية |
| 8 | `INSTALLATION_COMMISSIONING` | `installation_commissioning` | Installation, Commissioning & Support | التركيب والتشغيل والدعم |
| 9 | `SMART_FACTORY` | `smart_factory` | Smart Factory & Industry 4.0 Solutions | المصنع الذكي وحلول الصناعة 4.0 |

For every service `<key>` we ship three string resources:

- `service_<key>` — full title (used in lists and headers).
- `service_<key>_short` — one-line summary (used in service cards on the
  Services screen).
- `service_<key>_desc` — full descriptive paragraph (used on the
  Service Detail screen).

## Source descriptions (Persian — internal reference only)

The Persian text below is the source of truth provided by the company.
It is **not** an app locale; the app ships only English and Arabic.
Use it to validate translations.

### 1. Industrial Software & Digital Solutions

> راهکارهای نرم‌افزاری صنعتی و تحول دیجیتال
>
> تمرکز این بخش بر طراحی و توسعه نرم‌افزارهای پیشرفته برای صنایع سنگین
> (به‌ویژه فولاد، انرژی و تولید) است. این راهکارها شامل سیستم‌های Level 2،
> تحلیل داده‌های فرآیندی، داشبوردهای مدیریتی و سیستم‌های تصمیم‌یار می‌باشد.
> هدف اصلی، افزایش بهره‌وری، کاهش مصرف انرژی و ایجاد دید کامل از فرآیندهای
> صنعتی است. این سیستم‌ها به‌صورت اختصاصی طراحی می‌شوند تا با شرایط واقعی
> کارخانه‌ها کاملاً منطبق باشند.

### 2. Automation & Process Optimization

> اتوماسیون صنعتی و بهینه‌سازی فرآیندها
>
> در این حوزه، شرکت خدمات مهندسی برای بهینه‌سازی عملکرد خطوط تولید ارائه
> می‌دهد. این شامل تحلیل فرآیند، تنظیم پارامترهای عملیاتی، کاهش مصرف انرژی
> (Electricity/Gas)، و بهبود شاخص‌های کلیدی مانند Tap-to-Tap Time در
> فولادسازی است. همچنین امکان یکپارچه‌سازی با PLCها و سیستم‌های Level 1
> وجود دارد تا کنترل دقیق‌تری بر عملیات فراهم شود.

### 3. AI & Data Intelligence for Industry

> هوش مصنوعی و تحلیل داده در صنعت
>
> این بخش به استفاده از الگوریتم‌های پیشرفته و یادگیری ماشین برای پیش‌بینی
> و بهینه‌سازی فرآیندها می‌پردازد. کاربردها شامل پیش‌بینی دما، مصرف مواد،
> تشخیص خطا، و نگهداری پیش‌بینانه (Predictive Maintenance) است. هدف،
> تبدیل داده‌های خام صنعتی به بینش‌های قابل اجرا برای مدیران و اپراتورها
> است.

### 4. Industrial Integration & Middleware Solutions

> یکپارچه‌سازی سیستم‌ها و توسعه Middleware
>
> طراحی و پیاده‌سازی لایه‌های ارتباطی بین سیستم‌های مختلف صنعتی (PLC،
> SCADA، ERP، MES) در این حوزه انجام می‌شود. این شامل توسعه Middleware
> اختصاصی (مشابه Kepware) با قابلیت ارائه OPC UA Server و مدیریت تگ‌ها
> است. هدف این است که داده‌ها به‌صورت استاندارد، امن و قابل اعتماد در کل
> سازمان جریان پیدا کنند.

### 5. Engineering Consulting & Technical Services

> مشاوره مهندسی و خدمات تخصصی صنعتی
>
> ارائه خدمات مشاوره برای طراحی، بهینه‌سازی و اجرای پروژه‌های صنعتی. این
> شامل مطالعات امکان‌سنجی، طراحی سیستم‌های کنترلی، تحلیل اقتصادی (ROI)،
> و تهیه اسناد فنی برای مناقصات است. تمرکز بر ارائه راهکارهای عملی و قابل
> اجرا در شرایط واقعی صنعت می‌باشد.

### 6. Monitoring, KPI & Decision Support Systems

> سیستم‌های مانیتورینگ، KPI و تصمیم‌یار مدیریتی
>
> طراحی سیستم‌هایی برای نمایش لحظه‌ای وضعیت تولید، مصرف انرژی، کیفیت و
> عملکرد تجهیزات. این سیستم‌ها با استفاده از داشبوردهای حرفه‌ای، مدیران را
> قادر می‌سازند تصمیم‌های سریع و دقیق بگیرند. همچنین تعریف و پایش KPIهای
> کلیدی (مانند Yield، Energy per Ton، Downtime) از ویژگی‌های اصلی این
> بخش است.

### 7. Industrial Equipment Trading & Supply

> تأمین و فروش تجهیزات و قطعات صنعتی
>
> شرکت در حوزه تأمین تجهیزات صنعتی (مانند IGBT، فیوزهای صنعتی، تجهیزات
> اتوماسیون، سنسورها و قطعات خاص) نیز فعالیت دارد. با استفاده از شبکه
> تأمین در اروپا، چین، امارات و عمان، امکان ارائه محصولات با قیمت رقابتی
> و اصالت تضمین‌شده فراهم می‌شود.

### 8. Installation, Commissioning & Support

> نصب، راه‌اندازی و پشتیبانی سیستم‌ها
>
> ارائه خدمات کامل از مرحله نصب تا راه‌اندازی و پشتیبانی بلندمدت سیستم‌ها.
> این شامل تست، کالیبراسیون، آموزش اپراتورها و نگهداری سیستم‌ها است. هدف،
> تضمین عملکرد پایدار و بدون وقفه سیستم‌ها در محیط صنعتی می‌باشد.

### 9. Smart Factory & Industry 4.0 Solutions

> راهکارهای کارخانه هوشمند و Industry 4.0
>
> پیاده‌سازی مفاهیم دیجیتال‌سازی پیشرفته شامل اتصال تجهیزات، جمع‌آوری
> داده‌های بلادرنگ، تحلیل هوشمند و خودکارسازی تصمیم‌گیری. این حوزه شرکت
> را در جایگاه یک ارائه‌دهنده راهکارهای نسل جدید صنعتی قرار می‌دهد.
