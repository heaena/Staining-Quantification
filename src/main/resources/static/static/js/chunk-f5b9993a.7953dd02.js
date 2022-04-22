(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-f5b9993a"],{"1f3d":function(a,t,e){"use strict";var s=e("63f1"),n=e.n(s);n.a},"3f0e":function(a,t,e){"use strict";e.d(t,"b",(function(){return r})),e.d(t,"a",(function(){return i})),e.d(t,"c",(function(){return o}));var s=e("b775"),n={remove:"/task/remove",taskList:"/task/listTask",taskResult:"/task/taskResult"};function r(a){return Object(s["b"])({url:n.taskResult,method:"get",params:a})}function i(a){return Object(s["b"])({url:n.taskList,method:"get",params:a})}function o(a){return Object(s["b"])({url:n.remove,method:"delete",params:a})}},"63f1":function(a,t,e){},a3ab:function(a,t,e){"use strict";e.r(t);var s=function(){var a=this,t=this,e=t.$createElement,s=t._self._c||e;return s("a-card",{attrs:{bordered:!1}},[s("a-row",[s("a-col",{attrs:{span:22}},[s("div",[s("strong",[t._v("Task Title：")]),t._v(t._s(this.dataSource.taskName)+"，Task Time："+t._s(t.formatDate(this.dataSource.taskTime))+" ")]),s("div",{staticStyle:{"margin-bottom":"5px"}},[s("span",{staticStyle:{color:"red"}},[t._v("Images Path：")]),t._v(t._s(this.dataSource.canonicalPath)+" ")])]),s("a-col",{attrs:{span:2}},[s("a-button",{staticStyle:{"margin-left":"8px"},on:{click:function(){return a.$router.push({path:"/tasks"})}}},[t._v("All Tasks")])],1)],1),s("div",[s("a-row",[s("a-col",{attrs:{span:24}},[s("a-form",{attrs:{layout:"inline"}},[s("a-form-item",{attrs:{label:"Source Image Name"}},[s("a-input",{attrs:{placeholder:"Source Image Name"},model:{value:t.queryParam.filterName,callback:function(a){t.$set(t.queryParam,"filterName",a)},expression:"queryParam.filterName"}})],1),s("a-form-item",[s("a-button",{on:{click:function(a){return t.loadData()}}},[t._v("Search")]),s("a-button",{staticStyle:{"margin-left":"8px"},on:{click:function(){return a.queryParam={}}}},[t._v("Reset")]),s("a-button",{staticStyle:{"margin-left":"8px"},attrs:{type:"primary"},on:{click:function(){return a.analysisResultData.showModal=!0}}},[t._v("Analysis Result Data")])],1)],1)],1)],1)],1),s("a-row",[s("a-col",{attrs:{span:6}},[s("a-table",{attrs:{"data-source":t.tableDataSource,pagination:!0,size:"small"}},[s("a-table-column",{key:"name",attrs:{title:"Source Image Name","data-index":"name"},scopedSlots:t._u([{key:"default",fn:function(a,e){return[s("a",{staticStyle:{"font-size":"16px",width:"100%",display:"block"},on:{click:function(a){return t.onClickSourceImageName(e.name,e.resourcePath,e.canonicalFilePath)}}},[s("a-icon",{staticStyle:{fontSize:"20px","margin-right":"10px"},attrs:{type:"file-text",theme:"filled"}}),s("span",{staticStyle:{color:"black"}},[t._v(t._s(e.name))])],1)]}}])})],1)],1),s("a-col",{attrs:{span:18}},[s("div",{staticStyle:{"padding-left":"10px"}},[s("a-row",t._l(t.imageDetail,(function(a){return s("a-col",{key:a.name,attrs:{span:12}},[s("a-card",{attrs:{hoverable:""}},[s("strong",[t._v(t._s(a.name))]),s("img",{staticStyle:{width:"100%"},attrs:{src:t.getImagePath(a.resourcePath)}})])],1)})),1)],1)])],1),s("a-drawer",{attrs:{title:t.image.name,placement:"left",closable:!0,maskClosable:!1,visible:t.image.showModal,width:"90%"},on:{close:t.handleImageModalCancel}},[s("a",{staticStyle:{"font-size":"20px",border:"silver 1px"},attrs:{href:t.image.resourcePath,download:t.image.name}},[t._v("Download")]),s("span",{staticStyle:{"margin-left":"20px"}},[t._v("Image path "+t._s(t.image.canonicalFilePath))]),s("img",{attrs:{src:t.image.resourcePath,width:"100%"}})]),s("a-drawer",{attrs:{title:"Analysis results data",placement:"right",closable:!0,maskClosable:!0,visible:t.analysisResultData.showModal,width:"auto"},on:{close:t.handleAnalysisResultDataCancel}},[s("span",{staticStyle:{color:"red"}},[t._v("Analysis results data file path：")]),t._v(t._s(this.dataSource.canonicalPath)+"/out_stats_all/out_stats_all.csv "),t.dataSource.dataJson?s("table",{staticClass:"sample-table",attrs:{border:"1"}},t._l(t.dataSource.dataJson,(function(a,e){return s("tr",{key:e},t._l(a,(function(a,e){return s("td",{key:e},[t._v(t._s(a))])})),0)})),0):t._e()])],1)},n=[],r=(e("4160"),e("b0c0"),e("b64b"),e("159b"),e("3f0e")),i=e("c1df"),o=e.n(i),l={name:"TableList",data:function(){return{collapseActiveKey:["1"],fileList:[],image:{name:"",showModal:!1,scr:""},analysisResultData:{showModal:!1},confirmLoading:!1,queryParam:{filterName:""},dataSource:{},tableDataSource:[],imageDetail:[]}},filters:{},created:function(){this.loadData()},computed:{},methods:{str2JSON:function(a){return JSON.parse(a)},formatDate:function(a){return o()(a).format("YYYY-MM-DD HH:mm:ss")},onClickSourceImageName:function(a){this.imageDetail=this.dataSource.imageMap[a]},onClickImage:function(a,t,e){this.image.name=a,this.image.resourcePath=t,this.image.canonicalFilePath=e,this.image.showModal=!0},handleImageModalCancel:function(a){this.image.showModal=!1},handleAnalysisResultDataCancel:function(){this.analysisResultData.showModal=!1},getImagePath:function(a){return a},loadData:function(a){var t=this,e=Object.assign({taskName:this.$route.params.taskName},this.queryParam);Object(r["b"])(e).then((function(e){if(t.dataSource=e.data,e.data.imageMap){var s=[];Object.keys(e.data.imageMap).forEach((function(a){s.push({name:a})})),t.tableDataSource=s}e.data.data&&(t.dataSource.dataJson=t.str2JSON(e.data.data)),a&&a()}))}}},c=l,u=(e("1f3d"),e("2877")),m=Object(u["a"])(c,s,n,!1,null,"333b6b66",null);t["default"]=m.exports}}]);