(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-e958812a"],{"3f0e":function(t,e,a){"use strict";a.d(e,"b",(function(){return i})),a.d(e,"a",(function(){return o})),a.d(e,"c",(function(){return c}));var n=a("b775"),r={remove:"/task/remove",taskList:"/task/listTask",taskResult:"/task/taskResult"};function i(t){return Object(n["b"])({url:r.taskResult,method:"get",params:t})}function o(t){return Object(n["b"])({url:r.taskList,method:"get",params:t})}function c(t){return Object(n["b"])({url:r.remove,method:"delete",params:t})}},"8ce4":function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=this,a=e.$createElement,n=e._self._c||a;return n("a-card",{attrs:{bordered:!1}},[n("div",[n("a-row",{attrs:{gutter:0}},[n("a-col",{attrs:{md:14,sm:24}},[n("a-form",{attrs:{layout:"inline"}},[n("a-form-item",{attrs:{label:"任务名称"}},[n("a-input",{attrs:{placeholder:"任务名称"},model:{value:e.queryParam.filterName,callback:function(t){e.$set(e.queryParam,"filterName",t)},expression:"queryParam.filterName"}})],1),n("a-form-item",[n("a-button",{on:{click:function(t){return e.loadData()}}},[e._v("查询")]),n("a-button",{staticStyle:{"margin-left":"8px"},on:{click:function(){return t.queryParam={}}}},[e._v("重置")])],1)],1)],1)],1)],1),n("a-table",{attrs:{"data-source":e.dataSource,pagination:!1,rowKey:"name",size:"middle"}},[n("a-table-column",{key:"name",attrs:{title:"任务名称","data-index":"name"},scopedSlots:e._u([{key:"default",fn:function(t,a){return[n("a",{staticStyle:{"padding-right":"50px","font-size":"16px"},on:{click:function(t){return e.onClickAnalysisResult(a.name)}}},[n("a-icon",{staticStyle:{color:"goldenrod",fontSize:"20px","margin-right":"10px"},attrs:{type:"folder",theme:"filled"}}),n("span",{staticStyle:{color:"black"}},[e._v(e._s(a.name))])],1)]}}])}),n("a-table-column",{key:"lastModified",attrs:{title:"修改时间","data-index":"lastModified"},scopedSlots:e._u([{key:"default",fn:function(t,a){return[e._v(" "+e._s(e.formatDate(a.lastModified))+" ")]}}])}),n("a-table-column",{key:"action",attrs:{title:"操作"},scopedSlots:e._u([{key:"default",fn:function(t,a){return[n("span",[n("a-button",{on:{click:function(t){return e.deleteFile(a)}}},[e._v("删除")]),n("a-button",{staticStyle:{"margin-left":"5px"},attrs:{type:"primary"},on:{click:function(t){return e.onClickAnalysisResult(a)}}},[e._v("分析结果")])],1)]}}])})],1)],1)},r=[],i=(a("a434"),a("3f0e")),o=a("c1df"),c=a.n(o),s={name:"TableList",data:function(){return{collapseActiveKey:["1"],fileList:[],image:{name:"",showModal:!1,scr:""},breadcrumb:[{name:"全部任务",id:""}],confirmLoading:!1,queryParam:{filterName:""},dataSource:[]}},filters:{},created:function(){this.loadData()},computed:{rowSelection:function(){return{onChange:this.onSelectChange}}},methods:{formatDate:function(t){return c()(t).format("YYYY-MM-DD HH:mm:ss")},onClickBreadcrumb:function(t){this.queryParam.parentPath=t.id;for(var e=0;e<this.breadcrumb.length;e++)if(this.breadcrumb[e].id===t.id){this.breadcrumb.splice(e+1,this.breadcrumb.length);break}this.loadData()},deleteFile:function(t){var e=this,a={id:t.id};Object(i["c"])(a).then((function(t){e.loadData()}))},loadData:function(t){var e=this,a=Object.assign({},this.queryParam);Object(i["a"])(a).then((function(a){e.dataSource=a.data,t&&t()}))},onClickAnalysisResult:function(t){this.$router.push({path:"/task/"+t})}}},u=s,l=(a("985e"),a("2877")),d=Object(l["a"])(u,n,r,!1,null,"68f0e1c5",null);e["default"]=d.exports},"985e":function(t,e,a){"use strict";var n=a("c500"),r=a.n(n);r.a},c500:function(t,e,a){}}]);