define(["./core","./var/deletedIds","./data/support","./data/accepts"],function(h,f,e){var g=/^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,i=/([A-Z])/g;function c(l,k,m){if(m===undefined&&l.nodeType===1){var j="data-"+k.replace(i,"-$1").toLowerCase();m=l.getAttribute(j);if(typeof m==="string"){try{m=m==="true"?true:m==="false"?false:m==="null"?null:+m+""===m?+m:g.test(m)?h.parseJSON(m):m}catch(n){}h.data(l,k,m)}else{m=undefined}}return m}function b(k){var j;for(j in k){if(j==="data"&&h.isEmptyObject(k[j])){continue}if(j!=="toJSON"){return false}}return true}function d(m,k,o,n){if(!h.acceptData(m)){return}var q,p,r=h.expando,s=m.nodeType,j=s?h.cache:m,l=s?m[r]:m[r]&&r;if((!l||!j[l]||(!n&&!j[l].data))&&o===undefined&&typeof k==="string"){return}if(!l){if(s){l=m[r]=f.pop()||h.guid++}else{l=r}}if(!j[l]){j[l]=s?{}:{toJSON:h.noop}}if(typeof k==="object"||typeof k==="function"){if(n){j[l]=h.extend(j[l],k)}else{j[l].data=h.extend(j[l].data,k)}}p=j[l];if(!n){if(!p.data){p.data={}}p=p.data}if(o!==undefined){p[h.camelCase(k)]=o}if(typeof k==="string"){q=p[k];if(q==null){q=p[h.camelCase(k)]}}else{q=p}return q}function a(n,l,j){if(!h.acceptData(n)){return}var p,m,o=n.nodeType,k=o?h.cache:n,q=o?n[h.expando]:h.expando;if(!k[q]){return}if(l){p=j?k[q]:k[q].data;if(p){if(!h.isArray(l)){if(l in p){l=[l]}else{l=h.camelCase(l);if(l in p){l=[l]}else{l=l.split(" ")}}}else{l=l.concat(h.map(l,h.camelCase))}m=l.length;while(m--){delete p[l[m]]}if(j?!b(p):!h.isEmptyObject(p)){return}}}if(!j){delete k[q].data;if(!b(k[q])){return}}if(o){h.cleanData([n],true)}else{if(e.deleteExpando||k!=k.window){delete k[q]}else{k[q]=null}}}h.extend({cache:{},noData:{"applet ":true,"embed ":true,"object ":"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"},hasData:function(j){j=j.nodeType?h.cache[j[h.expando]]:j[h.expando];return !!j&&!b(j)},data:function(k,j,l){return d(k,j,l)},removeData:function(k,j){return a(k,j)},_data:function(k,j,l){return d(k,j,l,true)},_removeData:function(k,j){return a(k,j,true)}});h.fn.extend({data:function(m,p){var l,k,o,n=this[0],j=n&&n.attributes;if(m===undefined){if(this.length){o=h.data(n);if(n.nodeType===1&&!h._data(n,"parsedAttrs")){l=j.length;while(l--){if(j[l]){k=j[l].name;if(k.indexOf("data-")===0){k=h.camelCase(k.slice(5));c(n,k,o[k])}}}h._data(n,"parsedAttrs",true)}}return o}if(typeof m==="object"){return this.each(function(){h.data(this,m)})}return arguments.length>1?this.each(function(){h.data(this,m,p)}):n?c(n,m,h.data(n,m)):undefined},removeData:function(j){return this.each(function(){h.removeData(this,j)})}});return h});