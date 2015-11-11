define(["./core","./var/strundefined","./var/rnotwhite","./var/hasOwn","./var/slice","./event/support","./core/init","./data/accepts","./selector"],function(n,f,k,g,i,j){var b=/^(?:input|select|textarea)$/i,m=/^key/,d=/^(?:mouse|pointer|contextmenu)|click/,h=/^(?:focusinfocus|focusoutblur)$/,c=/^([^.]*)(?:\.(.+)|)$/;function e(){return true}function a(){return false}function l(){try{return document.activeElement}catch(o){}}n.event={global:{},add:function(s,y,D,v,u){var w,E,F,q,A,x,C,r,B,o,p,z=n._data(s);if(!z){return}if(D.handler){q=D;D=q.handler;u=q.selector}if(!D.guid){D.guid=n.guid++}if(!(E=z.events)){E=z.events={}}if(!(x=z.handle)){x=z.handle=function(t){return typeof n!==f&&(!t||n.event.triggered!==t.type)?n.event.dispatch.apply(x.elem,arguments):undefined};x.elem=s}y=(y||"").match(k)||[""];F=y.length;while(F--){w=c.exec(y[F])||[];B=p=w[1];o=(w[2]||"").split(".").sort();if(!B){continue}A=n.event.special[B]||{};B=(u?A.delegateType:A.bindType)||B;A=n.event.special[B]||{};C=n.extend({type:B,origType:p,data:v,handler:D,guid:D.guid,selector:u,needsContext:u&&n.expr.match.needsContext.test(u),namespace:o.join(".")},q);if(!(r=E[B])){r=E[B]=[];r.delegateCount=0;if(!A.setup||A.setup.call(s,v,o,x)===false){if(s.addEventListener){s.addEventListener(B,x,false)}else{if(s.attachEvent){s.attachEvent("on"+B,x)}}}}if(A.add){A.add.call(s,C);if(!C.handler.guid){C.handler.guid=D.guid}}if(u){r.splice(r.delegateCount++,0,C)}else{r.push(C)}n.event.global[B]=true}s=null},remove:function(r,y,F,s,x){var v,C,w,u,E,D,A,q,B,o,p,z=n.hasData(r)&&n._data(r);if(!z||!(D=z.events)){return}y=(y||"").match(k)||[""];E=y.length;while(E--){w=c.exec(y[E])||[];B=p=w[1];o=(w[2]||"").split(".").sort();if(!B){for(B in D){n.event.remove(r,B+y[E],F,s,true)}continue}A=n.event.special[B]||{};B=(s?A.delegateType:A.bindType)||B;q=D[B]||[];w=w[2]&&new RegExp("(^|\\.)"+o.join("\\.(?:.*\\.|)")+"(\\.|$)");u=v=q.length;while(v--){C=q[v];if((x||p===C.origType)&&(!F||F.guid===C.guid)&&(!w||w.test(C.namespace))&&(!s||s===C.selector||s==="**"&&C.selector)){q.splice(v,1);if(C.selector){q.delegateCount--}if(A.remove){A.remove.call(r,C)}}}if(u&&!q.length){if(!A.teardown||A.teardown.call(r,o,z.handle)===false){n.removeEvent(r,B,z.handle)}delete D[B]}}if(n.isEmptyObject(D)){delete z.handle;n._removeData(r,"events")}},trigger:function(o,v,r,C){var w,q,A,B,y,u,t,s=[r||document],z=g.call(o,"type")?o.type:o,p=g.call(o,"namespace")?o.namespace.split("."):[];A=u=r=r||document;if(r.nodeType===3||r.nodeType===8){return}if(h.test(z+n.event.triggered)){return}if(z.indexOf(".")>=0){p=z.split(".");z=p.shift();p.sort()}q=z.indexOf(":")<0&&"on"+z;o=o[n.expando]?o:new n.Event(z,typeof o==="object"&&o);o.isTrigger=C?2:3;o.namespace=p.join(".");o.namespace_re=o.namespace?new RegExp("(^|\\.)"+p.join("\\.(?:.*\\.|)")+"(\\.|$)"):null;o.result=undefined;if(!o.target){o.target=r}v=v==null?[o]:n.makeArray(v,[o]);y=n.event.special[z]||{};if(!C&&y.trigger&&y.trigger.apply(r,v)===false){return}if(!C&&!y.noBubble&&!n.isWindow(r)){B=y.delegateType||z;if(!h.test(B+z)){A=A.parentNode}for(;A;A=A.parentNode){s.push(A);u=A}if(u===(r.ownerDocument||document)){s.push(u.defaultView||u.parentWindow||window)}}t=0;while((A=s[t++])&&!o.isPropagationStopped()){o.type=t>1?B:y.bindType||z;w=(n._data(A,"events")||{})[o.type]&&n._data(A,"handle");if(w){w.apply(A,v)}w=q&&A[q];if(w&&w.apply&&n.acceptData(A)){o.result=w.apply(A,v);if(o.result===false){o.preventDefault()}}}o.type=z;if(!C&&!o.isDefaultPrevented()){if((!y._default||y._default.apply(s.pop(),v)===false)&&n.acceptData(r)){if(q&&r[z]&&!n.isWindow(r)){u=r[q];if(u){r[q]=null}n.event.triggered=z;try{r[z]()}catch(x){}n.event.triggered=undefined;if(u){r[q]=u}}}}return o.result},dispatch:function(o){o=n.event.fix(o);var s,t,x,p,r,w=[],v=i.call(arguments),q=(n._data(this,"events")||{})[o.type]||[],u=n.event.special[o.type]||{};v[0]=o;o.delegateTarget=this;if(u.preDispatch&&u.preDispatch.call(this,o)===false){return}w=n.event.handlers.call(this,o,q);s=0;while((p=w[s++])&&!o.isPropagationStopped()){o.currentTarget=p.elem;r=0;while((x=p.handlers[r++])&&!o.isImmediatePropagationStopped()){if(!o.namespace_re||o.namespace_re.test(x.namespace)){o.handleObj=x;o.data=x.data;t=((n.event.special[x.origType]||{}).handle||x.handler).apply(p.elem,v);if(t!==undefined){if((o.result=t)===false){o.preventDefault();o.stopPropagation()}}}}}if(u.postDispatch){u.postDispatch.call(this,o)}return o.result},handlers:function(o,q){var p,v,t,s,u=[],r=q.delegateCount,w=o.target;if(r&&w.nodeType&&(!o.button||o.type!=="click")){for(;w!=this;w=w.parentNode||this){if(w.nodeType===1&&(w.disabled!==true||o.type!=="click")){t=[];for(s=0;s<r;s++){v=q[s];p=v.selector+" ";if(t[p]===undefined){t[p]=v.needsContext?n(p,this).index(w)>=0:n.find(p,this,null,[w]).length}if(t[p]){t.push(v)}}if(t.length){u.push({elem:w,handlers:t})}}}}if(r<q.length){u.push({elem:this,handlers:q.slice(r)})}return u},fix:function(r){if(r[n.expando]){return r}var p,u,t,q=r.type,o=r,s=this.fixHooks[q];if(!s){this.fixHooks[q]=s=d.test(q)?this.mouseHooks:m.test(q)?this.keyHooks:{}}t=s.props?this.props.concat(s.props):this.props;r=new n.Event(o);p=t.length;while(p--){u=t[p];r[u]=o[u]}if(!r.target){r.target=o.srcElement||document}if(r.target.nodeType===3){r.target=r.target.parentNode}r.metaKey=!!r.metaKey;return s.filter?s.filter(r,o):r},props:"altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),fixHooks:{},keyHooks:{props:"char charCode key keyCode".split(" "),filter:function(p,o){if(p.which==null){p.which=o.charCode!=null?o.charCode:o.keyCode}return p}},mouseHooks:{props:"button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),filter:function(r,q){var o,s,t,p=q.button,u=q.fromElement;if(r.pageX==null&&q.clientX!=null){s=r.target.ownerDocument||document;t=s.documentElement;o=s.body;r.pageX=q.clientX+(t&&t.scrollLeft||o&&o.scrollLeft||0)-(t&&t.clientLeft||o&&o.clientLeft||0);r.pageY=q.clientY+(t&&t.scrollTop||o&&o.scrollTop||0)-(t&&t.clientTop||o&&o.clientTop||0)}if(!r.relatedTarget&&u){r.relatedTarget=u===r.target?q.toElement:u}if(!r.which&&p!==undefined){r.which=(p&1?1:(p&2?3:(p&4?2:0)))}return r}},special:{load:{noBubble:true},focus:{trigger:function(){if(this!==l()&&this.focus){try{this.focus();return false}catch(o){}}},delegateType:"focusin"},blur:{trigger:function(){if(this===l()&&this.blur){this.blur();return false}},delegateType:"focusout"},click:{trigger:function(){if(n.nodeName(this,"input")&&this.type==="checkbox"&&this.click){this.click();return false}},_default:function(o){return n.nodeName(o.target,"a")}},beforeunload:{postDispatch:function(o){if(o.result!==undefined&&o.originalEvent){o.originalEvent.returnValue=o.result}}}},simulate:function(p,r,q,o){var s=n.extend(new n.Event(),q,{type:p,isSimulated:true,originalEvent:{}});if(o){n.event.trigger(s,null,r)}else{n.event.dispatch.call(r,s)}if(s.isDefaultPrevented()){q.preventDefault()}}};n.removeEvent=document.removeEventListener?function(p,o,q){if(p.removeEventListener){p.removeEventListener(o,q,false)}}:function(q,p,r){var o="on"+p;if(q.detachEvent){if(typeof q[o]===f){q[o]=null}q.detachEvent(o,r)}};n.Event=function(p,o){if(!(this instanceof n.Event)){return new n.Event(p,o)}if(p&&p.type){this.originalEvent=p;this.type=p.type;this.isDefaultPrevented=p.defaultPrevented||p.defaultPrevented===undefined&&p.returnValue===false?e:a}else{this.type=p}if(o){n.extend(this,o)}this.timeStamp=p&&p.timeStamp||n.now();this[n.expando]=true};n.Event.prototype={isDefaultPrevented:a,isPropagationStopped:a,isImmediatePropagationStopped:a,preventDefault:function(){var o=this.originalEvent;this.isDefaultPrevented=e;if(!o){return}if(o.preventDefault){o.preventDefault()}else{o.returnValue=false}},stopPropagation:function(){var o=this.originalEvent;this.isPropagationStopped=e;if(!o){return}if(o.stopPropagation){o.stopPropagation()}o.cancelBubble=true},stopImmediatePropagation:function(){var o=this.originalEvent;this.isImmediatePropagationStopped=e;if(o&&o.stopImmediatePropagation){o.stopImmediatePropagation()}this.stopPropagation()}};n.each({mouseenter:"mouseover",mouseleave:"mouseout",pointerenter:"pointerover",pointerleave:"pointerout"},function(p,o){n.event.special[p]={delegateType:o,bindType:o,handle:function(s){var q,u=this,t=s.relatedTarget,r=s.handleObj;if(!t||(t!==u&&!n.contains(u,t))){s.type=r.origType;q=r.handler.apply(this,arguments);s.type=o}return q}}});if(!j.submitBubbles){n.event.special.submit={setup:function(){if(n.nodeName(this,"form")){return false}n.event.add(this,"click._submit keypress._submit",function(q){var p=q.target,o=n.nodeName(p,"input")||n.nodeName(p,"button")?p.form:undefined;if(o&&!n._data(o,"submitBubbles")){n.event.add(o,"submit._submit",function(r){r._submit_bubble=true});n._data(o,"submitBubbles",true)}})},postDispatch:function(o){if(o._submit_bubble){delete o._submit_bubble;if(this.parentNode&&!o.isTrigger){n.event.simulate("submit",this.parentNode,o,true)}}},teardown:function(){if(n.nodeName(this,"form")){return false}n.event.remove(this,"._submit")}}}if(!j.changeBubbles){n.event.special.change={setup:function(){if(b.test(this.nodeName)){if(this.type==="checkbox"||this.type==="radio"){n.event.add(this,"propertychange._change",function(o){if(o.originalEvent.propertyName==="checked"){this._just_changed=true}});n.event.add(this,"click._change",function(o){if(this._just_changed&&!o.isTrigger){this._just_changed=false}n.event.simulate("change",this,o,true)})}return false}n.event.add(this,"beforeactivate._change",function(p){var o=p.target;if(b.test(o.nodeName)&&!n._data(o,"changeBubbles")){n.event.add(o,"change._change",function(q){if(this.parentNode&&!q.isSimulated&&!q.isTrigger){n.event.simulate("change",this.parentNode,q,true)}});n._data(o,"changeBubbles",true)}})},handle:function(p){var o=p.target;if(this!==o||p.isSimulated||p.isTrigger||(o.type!=="radio"&&o.type!=="checkbox")){return p.handleObj.handler.apply(this,arguments)}},teardown:function(){n.event.remove(this,"._change");return !b.test(this.nodeName)}}}if(!j.focusinBubbles){n.each({focus:"focusin",blur:"focusout"},function(q,o){var p=function(r){n.event.simulate(o,r.target,n.event.fix(r),true)};n.event.special[o]={setup:function(){var s=this.ownerDocument||this,r=n._data(s,o);if(!r){s.addEventListener(q,p,true)}n._data(s,o,(r||0)+1)},teardown:function(){var s=this.ownerDocument||this,r=n._data(s,o)-1;if(!r){s.removeEventListener(q,p,true);n._removeData(s,o)}else{n._data(s,o,r)}}}})}n.fn.extend({on:function(q,o,t,s,p){var r,u;if(typeof q==="object"){if(typeof o!=="string"){t=t||o;o=undefined}for(r in q){this.on(r,o,t,q[r],p)}return this}if(t==null&&s==null){s=o;t=o=undefined}else{if(s==null){if(typeof o==="string"){s=t;t=undefined}else{s=t;t=o;o=undefined}}}if(s===false){s=a}else{if(!s){return this}}if(p===1){u=s;s=function(v){n().off(v);return u.apply(this,arguments)};s.guid=u.guid||(u.guid=n.guid++)}return this.each(function(){n.event.add(this,q,s,t,o)})},one:function(p,o,r,q){return this.on(p,o,r,q,1)},off:function(q,o,s){var p,r;if(q&&q.preventDefault&&q.handleObj){p=q.handleObj;n(q.delegateTarget).off(p.namespace?p.origType+"."+p.namespace:p.origType,p.selector,p.handler);return this}if(typeof q==="object"){for(r in q){this.off(r,o,q[r])}return this}if(o===false||typeof o==="function"){s=o;o=undefined}if(s===false){s=a}return this.each(function(){n.event.remove(this,q,s,o)})},trigger:function(o,p){return this.each(function(){n.event.trigger(o,p,this)})},triggerHandler:function(o,q){var p=this[0];if(p){return n.event.trigger(o,q,p,true)}}});return n});