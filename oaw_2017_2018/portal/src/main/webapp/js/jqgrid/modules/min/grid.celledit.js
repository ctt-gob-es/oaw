!function(e){"use strict";"function"==typeof define&&define.amd?define(["jquery","./grid.base","./jquery.fmatter","./grid.common"],e):"object"==typeof module&&module.exports?module.exports=function(l,t){return l||(l=window),void 0===t&&(t="undefined"!=typeof window?require("jquery"):require("jquery")(l)),require("./grid.base"),require("./jquery.fmatter"),require("./grid.common"),e(t),t}:e(jQuery)}(function(e){"use strict";var l=e.jgrid,t=function(){var t=e.makeArray(arguments);return t.unshift(""),t.unshift(""),t.unshift(this.p),l.feedback.apply(this,t)},i=function(l,t){var i=this.grid.fbRows;return e((null!=i&&i[0].cells.length>t?i[l.rowIndex]:l).cells[t])},r=function(e,l){var t=e.height();Math.abs(t-l)>=1&&l>0&&(e.height(l),t=e.height(),Math.abs(l-t)>=1&&e.height(l+Math.round(l-t)))};l.extend({editCell:function(a,o,d){return this.each(function(){var n,s,c,u,f=this,h=e(f),p=f.p,v=f.rows;if(f.grid&&!0===p.cellEdit&&null!=v&&null!=v[a]&&(a=parseInt(a,10),o=parseInt(o,10),!isNaN(a)&&!isNaN(o))){var C,m=v[a],g=null!=m?m.id:null,b=e(m),w=parseInt(p.iCol,10),j=parseInt(p.iRow,10),q=e(v[j]),G=p.savedRow;if(null!=g){if(p.selrow=g,p.knv||h.jqGrid("GridNav"),G.length>0){if(!0===d&&a===j&&o===w)return;h.jqGrid("saveCell",G[0].id,G[0].ic)}else setTimeout(function(){e("#"+l.jqID(p.knv)).attr("tabindex","-1").focus()},1);if(u=p.colModel[o],"subgrid"!==(n=u.name)&&"cb"!==n&&"rn"!==n){c=i.call(f,m,o);var y=u.editable;e.isFunction(y)&&(y=y.call(f,{rowid:g,iCol:o,iRow:a,cmName:n,cm:u,mode:"cell"}));var x=h.jqGrid("getGuiStyles","states.select","edit-cell"),k=h.jqGrid("getGuiStyles","states.hover","selected-row");if(!0!==y||!0!==d||c.hasClass("not-editable-cell"))p.noCellSelection||(w>=0&&j>=0&&(i.call(f,q[0],w).removeClass(x),q.removeClass(k)),c.addClass(x),b.addClass(k)),s=c.html().replace(/&#160;/gi,""),t.call(f,"onSelectCell",g,n,s,a,o);else{p.noCellSelection||(w>=0&&j>=0&&(i.call(f,q[0],w).removeClass(x),q.removeClass(k)),c.addClass(x),b.addClass(k)),u.edittype||(u.edittype="text"),C=u.edittype;try{s=e.unformat.call(f,c,{rowId:g,colModel:u},o)}catch(e){s="textarea"===C?c.text():c.html()}if(p.autoEncodeOnEdit&&(s=l.oldDecodePostedData(s)),("&nbsp;"===s||"&#160;"===s||1===s.length&&160===s.charCodeAt(0))&&(s=""),e.isFunction(p.formatCell)){var E=p.formatCell.call(f,g,n,s,a,o);void 0!==E&&(s=E)}t.call(f,"beforeEditCell",g,n,s,a,o),G.push({id:a,ic:o,name:n,v:s}),p.editingInfo[g]={mode:"cellEditing",savedRow:G[G.length-1],editable:{}},p.editingInfo[g].editable[n]=y;var I=e.extend({},u.editoptions||{},{id:a+"_"+n,name:n,rowId:g,mode:"cell",cm:u,iCol:o}),R=l.createEl.call(f,C,I,s,!0,e.extend({},l.ajaxOptions,p.ajaxSelectOptions||{})),S=c,D=!0===p.treeGrid&&n===p.ExpandColumn;D&&(S=c.children("span.cell-wrapperleaf,span.cell-wrapper")),S.html("").append(R).attr("tabindex","0"),D&&e(R).width(c.width()-c.children("div.tree-wrap").outerWidth()),l.bindEv.call(f,R,I),p.frozenColumns&&o<h.jqGrid("getNumberOfFrozenColumns")&&r(e(f.rows[m.rowIndex].cells[o]),c.height()),setTimeout(function(){e(R).focus()},0),e("input, select, textarea",c).on("keydown",function(l){if(27===l.keyCode&&(e("input.hasDatepicker",c).length>0?e(".ui-datepicker").is(":hidden")?h.jqGrid("restoreCell",a,o):e("input.hasDatepicker",c).datepicker("hide"):h.jqGrid("restoreCell",a,o)),13===l.keyCode&&!l.shiftKey)return h.jqGrid("saveCell",a,o),!1;if(9===l.keyCode){if(f.grid.hDiv.loading)return!1;l.shiftKey?h.jqGrid("prevCell",a,o):h.jqGrid("nextCell",a,o)}l.stopPropagation()}),t.call(f,"afterEditCell",g,n,s,a,o)}p.iCol=o,p.iRow=a}}}})},saveCell:function(r,a){return this.each(function(){var o=this,d=e(o),n=o.p,s=o.grid,c=l.info_dialog,u=l.jqID;if(s&&!0===n.cellEdit){var f=d.jqGrid("getGridRes","errors"),h=f.errcap,p=d.jqGrid("getGridRes","edit").bClose,v=n.savedRow,C=v.length>=1?0:null;if(null!==C){var m,g=o.rows[r],b=g.id,w=e(g),j=n.colModel[a],q=j.name,G=i.call(o,g,a),y={},x=l.getEditedValue.call(o,G,j,y);if(x!==v[C].v){void 0!==(m=d.triggerHandler("jqGridBeforeSaveCell",[b,q,x,r,a]))&&(x=m),e.isFunction(n.beforeSaveCell)&&void 0!==(m=n.beforeSaveCell.call(o,b,q,x,r,a))&&(x=m);var k=l.checkValues.call(o,x,a,void 0,void 0,{oldValue:v[C].v,newValue:x,cmName:q,rowid:b,iCol:a,iRow:r,cm:j,tr:g,td:G,mode:"cell"}),E=j.formatoptions||{};if(null==k||!0===k||!0===k[0]){var I=d.triggerHandler("jqGridBeforeSubmitCell",[b,q,x,r,a])||{};if(e.isFunction(n.beforeSubmitCell)&&((I=n.beforeSubmitCell.call(o,b,q,x,r,a))||(I={})),e("input.hasDatepicker",G).length>0&&e("input.hasDatepicker",G).datepicker("hide"),"date"===j.formatter&&!0!==E.sendFormatted&&(x=e.unformat.date.call(o,x,j)),"remote"===n.cellsubmit)if(n.cellurl){var R={};R[q]=x;var S=n.prmNames,D=S.id,F=S.oper;R[D]=l.stripPref(n.idPrefix,b),R[F]=S.editoper,R=e.extend(I,R),n.autoEncodeOnEdit&&e.each(R,function(t,i){e.isFunction(i)||(R[t]=l.oldEncodePostedData(i))}),d.jqGrid("progressBar",{method:"show",loadtype:n.loadui,htmlcontent:d.jqGrid("getGridRes","defaults.savetext")||"Saving..."}),s.hDiv.loading=!0,e.ajax(e.extend({url:e.isFunction(n.cellurl)?n.cellurl.call(o,n.cellurl,r,a,b,x,q):n.cellurl,data:l.serializeFeedback.call(o,n.serializeCellData,"jqGridSerializeCellData",R),type:"POST",complete:function(l){if(s.endReq.call(o),(l.status<300||304===l.status)&&(0!==l.status||4!==l.readyState)){var i=d.triggerHandler("jqGridAfterSubmitCell",[o,l,R.id,q,x,r,a])||[!0,""];(!0===i||!0===i[0]&&e.isFunction(n.afterSubmitCell))&&(i=n.afterSubmitCell.call(o,l,R.id,q,x,r,a)),null==i||!0===i||!0===i[0]?(d.jqGrid("setCell",b,a,x,!1,!1,!0),G.addClass("dirty-cell"),w.addClass("edited"),t.call(o,"afterSaveCell",b,q,x,r,a),v.splice(0,1),delete n.editingInfo[b]):(c.call(o,h,i[1],p),d.jqGrid("restoreCell",r,a))}},error:function(l,t,i){d.triggerHandler("jqGridErrorCell",[l,t,i]),e.isFunction(n.errorCell)?(n.errorCell.call(o,l,t,i),d.jqGrid("restoreCell",r,a)):(c.call(o,h,l.status+" : "+l.statusText+"<br/>"+t,p),d.jqGrid("restoreCell",r,a))}},l.ajaxOptions,n.ajaxCellOptions||{}))}else try{c.call(o,h,f.nourl,p),d.jqGrid("restoreCell",r,a)}catch(e){}if("clientArray"===n.cellsubmit){if(d.jqGrid("setCell",b,a,"select"===j.edittype&&"select"!==j.formatter?y.text:x,!1,!1,!0),G.addClass("dirty-cell"),w.addClass("edited"),t.call(o,"afterSaveCell",b,q,x,r,a),n.frozenColumns&&a<d.jqGrid("getNumberOfFrozenColumns"))try{o.rows[g.rowIndex].cells[a].style.height=""}catch(e){}v.splice(0,1),delete n.editingInfo[b]}}else try{setTimeout(function(){var t=l.getRelativeRect.call(o,G);c.call(o,h,x+" "+k[1],p,{top:t.top,left:t.left+e(o).closest(".ui-jqgrid").offset().left})},50),d.jqGrid("restoreCell",r,a)}catch(e){}}else d.jqGrid("restoreCell",r,a)}setTimeout(function(){e("#"+u(n.knv)).attr("tabindex","-1").focus()},0)}})},restoreCell:function(l,r){return this.each(function(){var a,o,d,n=this,s=n.p,c=n.rows[l],u=c.id;if(n.grid&&!0===s.cellEdit){var f=s.savedRow,h=i.call(n,c,r);if(f.length>=1){if(e.isFunction(e.fn.datepicker))try{e("input.hasDatepicker",h).datepicker("hide")}catch(e){}if(o=s.colModel[r],!0===s.treeGrid&&o.name===s.ExpandColumn?h.children("span.cell-wrapperleaf,span.cell-wrapper").empty():h.empty(),h.attr("tabindex","-1"),a=f[0].v,d=o.formatoptions||{},"date"===o.formatter&&!0!==d.sendFormatted&&(a=e.unformat.date.call(n,a,o)),e(n).jqGrid("setCell",u,r,a,!1,!1,!0),s.frozenColumns&&r<e(n).jqGrid("getNumberOfFrozenColumns"))try{n.rows[c.rowIndex].cells[r].style.height=""}catch(e){}t.call(n,"afterRestoreCell",u,a,l,r),f.splice(0,1),delete s.editingInfo[u]}setTimeout(function(){e("#"+s.knv).attr("tabindex","-1").focus()},0)}})},nextCell:function(l,t){return this.each(function(){var i,r,a,o=this,d=e(o),n=o.p,s=!1,c=o.rows;if(o.grid&&!0===n.cellEdit&&null!=c&&null!=c[l]){for(i=t+1;i<n.colModel.length;i++)if(a=n.colModel[i],r=a.editable,e.isFunction(r)&&(r=r.call(o,{rowid:c[l].id,iCol:i,iRow:l,cmName:a.name,cm:a,mode:"cell"})),!0===r){s=i;break}!1!==s?d.jqGrid("editCell",l,s,!0):n.savedRow.length>0&&d.jqGrid("saveCell",l,t)}})},prevCell:function(l,t){return this.each(function(){var i,r,a,o=this,d=e(o),n=o.p,s=!1,c=o.rows;if(o.grid&&!0===n.cellEdit&&null!=c&&null!=c[l]){for(i=t-1;i>=0;i--)if(a=n.colModel[i],r=a.editable,e.isFunction(r)&&(r=r.call(o,{rowid:c[l].id,iCol:i,iRow:l,cmName:a.name,cm:a,mode:"cell"})),!0===r){s=i;break}!1!==s?d.jqGrid("editCell",l,s,!0):n.savedRow.length>0&&d.jqGrid("saveCell",l,t)}})},GridNav:function(){return this.each(function(){function l(e,l,t){var i=a.rows[e];if("v"===t.substr(0,1)){var r=s.clientHeight,o=s.scrollTop,d=i.offsetTop+i.clientHeight,n=i.offsetTop;"vd"===t&&d>=o+r&&(s.scrollTop=s.scrollTop+i.clientHeight),"vu"===t&&n<o&&(s.scrollTop=s.scrollTop-i.clientHeight)}if("h"===t){var c=s.clientWidth,u=s.scrollLeft,f=i.cells[l],h=f.offsetLeft+f.clientWidth,p=f.offsetLeft;h>=c+parseInt(u,10)?s.scrollLeft=s.scrollLeft+f.clientWidth:p<u&&(s.scrollLeft=s.scrollLeft-f.clientWidth)}}function t(e,l){var t,i=0,r=d.colModel;if("lft"===l)for(i=e+1,t=e;t>=0;t--)if(!0!==r[t].hidden){i=t;break}if("rgt"===l)for(i=e-1,t=e;t<r.length;t++)if(!0!==r[t].hidden){i=t;break}return i}var i,r,a=this,o=e(a),d=a.p,n=a.grid;if(n&&!0===d.cellEdit){var s=n.bDiv;d.knv=d.id+"_kn";var c=e("<div style='position:fixed;top:0px;width:1px;height:1px;' tabindex='0'><div tabindex='-1' style='width:1px;height:1px;' id='"+d.knv+"'></div></div>");e(c).insertBefore(n.cDiv),e("#"+d.knv).focus().keydown(function(e){var n=parseInt(d.iRow,10),s=parseInt(d.iCol,10);switch(r=e.keyCode,"rtl"===d.direction&&(37===r?r=39:39===r&&(r=37)),r){case 38:n-1>0&&(l(n-1,s,"vu"),o.jqGrid("editCell",n-1,s,!1));break;case 40:n+1<=a.rows.length-1&&(l(n+1,s,"vd"),o.jqGrid("editCell",n+1,s,!1));break;case 37:s-1>=0&&(l(n,i=t(s-1,"lft"),"h"),o.jqGrid("editCell",n,i,!1));break;case 39:s+1<=d.colModel.length-1&&(l(n,i=t(s+1,"rgt"),"h"),o.jqGrid("editCell",n,i,!1));break;case 13:s>=0&&n>=0&&o.jqGrid("editCell",n,s,!0);break;default:return!0}return!1})}})},getChangedCells:function(t){var r=[];return t||(t="all"),this.each(function(){var a=this,o=a.p,d=l.htmlDecode,n=a.rows;a.grid&&!0===o.cellEdit&&e(n).each(function(l){var s={};if(e(this).hasClass("edited")){var c=this;e(this.cells).each(function(r){var u=o.colModel[r],f=u.name,h=i.call(a,c,r);if("cb"!==f&&"subgrid"!==f&&"rn"!==f&&("dirty"!==t||h.hasClass("dirty-cell")))try{s[f]=e.unformat.call(a,h[0],{rowId:n[l].id,colModel:u},r)}catch(e){s[f]=d(h.html())}}),s.id=this.id,r.push(s)}})}),r}})});
//# sourceMappingURL=grid.celledit.js.map