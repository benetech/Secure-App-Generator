define(["./core","./var/strundefined","./core/access","./css/var/rnumnonpx","./css/curCSS","./css/addGetHookIf","./css/support","./core/init","./css","./selector"],function(i,e,d,g,f,c,h){f=f.curCSS;var b=window.document.documentElement;function a(j){return i.isWindow(j)?j:j.nodeType===9?j.defaultView||j.parentWindow:false}i.offset={setOffset:function(l,v,p){var r,o,j,m,k,t,u,q=i.css(l,"position"),n=i(l),s={};if(q==="static"){l.style.position="relative"}k=n.offset();j=i.css(l,"top");t=i.css(l,"left");u=(q==="absolute"||q==="fixed")&&i.inArray("auto",[j,t])>-1;if(u){r=n.position();m=r.top;o=r.left}else{m=parseFloat(j)||0;o=parseFloat(t)||0}if(i.isFunction(v)){v=v.call(l,p,k)}if(v.top!=null){s.top=(v.top-k.top)+m}if(v.left!=null){s.left=(v.left-k.left)+o}if("using" in v){v.using.call(l,s)}else{n.css(s)}}};i.fn.extend({offset:function(k){if(arguments.length){return k===undefined?this:this.each(function(p){i.offset.setOffset(this,k,p)})}var j,o,m={top:0,left:0},l=this[0],n=l&&l.ownerDocument;if(!n){return}j=n.documentElement;if(!i.contains(j,l)){return m}if(typeof l.getBoundingClientRect!==e){m=l.getBoundingClientRect()}o=a(n);return{top:m.top+(o.pageYOffset||j.scrollTop)-(j.clientTop||0),left:m.left+(o.pageXOffset||j.scrollLeft)-(j.clientLeft||0)}},position:function(){if(!this[0]){return}var l,m,j={top:0,left:0},k=this[0];if(i.css(k,"position")==="fixed"){m=k.getBoundingClientRect()}else{l=this.offsetParent();m=this.offset();if(!i.nodeName(l[0],"html")){j=l.offset()}j.top+=i.css(l[0],"borderTopWidth",true);j.left+=i.css(l[0],"borderLeftWidth",true)}return{top:m.top-j.top-i.css(k,"marginTop",true),left:m.left-j.left-i.css(k,"marginLeft",true)}},offsetParent:function(){return this.map(function(){var j=this.offsetParent||b;while(j&&(!i.nodeName(j,"html")&&i.css(j,"position")==="static")){j=j.offsetParent}return j||b})}});i.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},function(l,k){var j=/Y/.test(k);i.fn[l]=function(m){return d(this,function(n,q,p){var o=a(n);if(p===undefined){return o?(k in o)?o[k]:o.document.documentElement[q]:n[q]}if(o){o.scrollTo(!j?p:i(o).scrollLeft(),j?p:i(o).scrollTop())}else{n[q]=p}},l,m,arguments.length,null)}});i.each(["top","left"],function(j,k){i.cssHooks[k]=c(h.pixelPosition,function(m,l){if(l){l=f(m,k);return g.test(l)?i(m).position()[k]+"px":l}})});return i});