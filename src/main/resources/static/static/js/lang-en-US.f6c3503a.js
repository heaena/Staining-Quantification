(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["lang-en-US","lang-en-US-dashboard","lang-en-US-dashboard-analysis","lang-en-US-menu","lang-en-US-setting"],{"0ff2":function(a,e,s){(function(a,e){e(s("c1df"))})(0,(function(a){"use strict";
//! moment.js locale configuration
var e=a.defineLocale("eu",{months:"urtarrila_otsaila_martxoa_apirila_maiatza_ekaina_uztaila_abuztua_iraila_urria_azaroa_abendua".split("_"),monthsShort:"urt._ots._mar._api._mai._eka._uzt._abu._ira._urr._aza._abe.".split("_"),monthsParseExact:!0,weekdays:"igandea_astelehena_asteartea_asteazkena_osteguna_ostirala_larunbata".split("_"),weekdaysShort:"ig._al._ar._az._og._ol._lr.".split("_"),weekdaysMin:"ig_al_ar_az_og_ol_lr".split("_"),weekdaysParseExact:!0,longDateFormat:{LT:"HH:mm",LTS:"HH:mm:ss",L:"YYYY-MM-DD",LL:"YYYY[ko] MMMM[ren] D[a]",LLL:"YYYY[ko] MMMM[ren] D[a] HH:mm",LLLL:"dddd, YYYY[ko] MMMM[ren] D[a] HH:mm",l:"YYYY-M-D",ll:"YYYY[ko] MMM D[a]",lll:"YYYY[ko] MMM D[a] HH:mm",llll:"ddd, YYYY[ko] MMM D[a] HH:mm"},calendar:{sameDay:"[gaur] LT[etan]",nextDay:"[bihar] LT[etan]",nextWeek:"dddd LT[etan]",lastDay:"[atzo] LT[etan]",lastWeek:"[aurreko] dddd LT[etan]",sameElse:"L"},relativeTime:{future:"%s barru",past:"duela %s",s:"segundo batzuk",ss:"%d segundo",m:"minutu bat",mm:"%d minutu",h:"ordu bat",hh:"%d ordu",d:"egun bat",dd:"%d egun",M:"hilabete bat",MM:"%d hilabete",y:"urte bat",yy:"%d urte"},dayOfMonthOrdinalParse:/\d{1,2}\./,ordinal:"%d.",week:{dow:1,doy:7}});return e}))},5030:function(a,e,s){"use strict";s.r(e),e["default"]={"app.setting.pagestyle":"Page style setting","app.setting.pagestyle.light":"Light style","app.setting.pagestyle.dark":"Dark style","app.setting.pagestyle.realdark":"RealDark style","app.setting.themecolor":"Theme Color","app.setting.navigationmode":"Navigation Mode","app.setting.content-width":"Content Width","app.setting.fixedheader":"Fixed Header","app.setting.fixedsidebar":"Fixed Sidebar","app.setting.sidemenu":"Side Menu Layout","app.setting.topmenu":"Top Menu Layout","app.setting.content-width.fixed":"Fixed","app.setting.content-width.fluid":"Fluid","app.setting.othersettings":"Other Settings","app.setting.weakmode":"Weak Mode","app.setting.copy":"Copy Setting","app.setting.loading":"Loading theme","app.setting.copyinfo":"copy success，please replace defaultSettings in src/models/setting.js","app.setting.production.hint":"Setting panel shows in development environment only, please manually modify"}},"743d":function(a,e,s){"use strict";s.r(e);var t=s("5530"),n=s("7320"),i=n["a"],o=i,r=s("0ff2"),l=s.n(r),d=s("771d"),u=s("5030"),m=s("dea1"),c={antLocale:o,momentName:"eu",momentLocale:l.a};e["default"]=Object(t["a"])(Object(t["a"])(Object(t["a"])(Object(t["a"])({message:"-","layouts.usermenu.dialog.title":"Message","layouts.usermenu.dialog.content":"Are you sure you would like to logout?"},c),d["default"]),u["default"]),m["default"])},"771d":function(a,e,s){"use strict";s.r(e),e["default"]={"menu.welcome":"Welcome","menu.home":"Home","menu.dashboard":"Dashboard","menu.dashboard.analysis":"Analysis","menu.dashboard.monitor":"Monitor","menu.dashboard.workplace":"Workplace","menu.form":"Form","menu.form.basic-form":"Basic Form","menu.form.step-form":"Step Form","menu.form.step-form.info":"Step Form(write transfer information)","menu.form.step-form.confirm":"Step Form(confirm transfer information)","menu.form.step-form.result":"Step Form(finished)","menu.form.advanced-form":"Advanced Form","menu.list":"List","menu.list.table-list":"Search Table","menu.list.basic-list":"Basic List","menu.list.card-list":"Card List","menu.list.search-list":"Search List","menu.list.search-list.articles":"Search List(articles)","menu.list.search-list.projects":"Search List(projects)","menu.list.search-list.applications":"Search List(applications)","menu.profile":"Profile","menu.profile.basic":"Basic Profile","menu.profile.advanced":"Advanced Profile","menu.result":"Result","menu.result.success":"Success","menu.result.fail":"Fail","menu.exception":"Exception","menu.exception.not-permission":"403","menu.exception.not-find":"404","menu.exception.server-error":"500","menu.exception.trigger":"Trigger","menu.account":"Account","menu.account.center":"Account Center","menu.account.settings":"Account Settings","menu.account.trigger":"Trigger Error","menu.account.logout":"Logout"}},b781:function(a,e,s){"use strict";s.r(e),e["default"]={"dashboard.analysis.test":"Gongzhuan No.{no} shop","dashboard.analysis.introduce":"Introduce","dashboard.analysis.total-sales":"Total Sales","dashboard.analysis.day-sales":"Daily Sales","dashboard.analysis.visits":"Visits","dashboard.analysis.visits-trend":"Visits Trend","dashboard.analysis.visits-ranking":"Visits Ranking","dashboard.analysis.day-visits":"Daily Visits","dashboard.analysis.week":"WoW Change","dashboard.analysis.day":"DoD Change","dashboard.analysis.payments":"Payments","dashboard.analysis.conversion-rate":"Conversion Rate","dashboard.analysis.operational-effect":"Operational Effect","dashboard.analysis.sales-trend":"Stores Sales Trend","dashboard.analysis.sales-ranking":"Sales Ranking","dashboard.analysis.all-year":"All Year","dashboard.analysis.all-month":"All Month","dashboard.analysis.all-week":"All Week","dashboard.analysis.all-day":"All day","dashboard.analysis.search-users":"Search Users","dashboard.analysis.per-capita-search":"Per Capita Search","dashboard.analysis.online-top-search":"Online Top Search","dashboard.analysis.the-proportion-of-sales":"The Proportion Of Sales","dashboard.analysis.dropdown-option-one":"Operation one","dashboard.analysis.dropdown-option-two":"Operation two","dashboard.analysis.channel.all":"ALL","dashboard.analysis.channel.online":"Online","dashboard.analysis.channel.stores":"Stores","dashboard.analysis.sales":"Sales","dashboard.analysis.traffic":"Traffic","dashboard.analysis.table.rank":"Rank","dashboard.analysis.table.search-keyword":"Keyword","dashboard.analysis.table.users":"Users","dashboard.analysis.table.weekly-range":"Weekly Range"}},dea1:function(a,e,s){"use strict";s.r(e);var t=s("5530"),n=s("b781");e["default"]=Object(t["a"])({},n["default"])}}]);