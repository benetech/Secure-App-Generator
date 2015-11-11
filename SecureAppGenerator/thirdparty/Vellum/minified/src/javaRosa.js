define(["underscore","jquery","xpath","xpathmodels","tpl!vellum/templates/edit_source","tpl!vellum/templates/language_selector","tpl!vellum/templates/control_group","tpl!vellum/templates/markdown_help","text!vellum/templates/button_remove.html","vellum/widgets","vellum/util","vellum/tsv","vellum/xml","vellum/core"],function(P,x,O,f,o,u,k,v,g,y,j,G,q){var F=["image","audio","video"],H={image:"png",audio:"mp3",video:"3gp"},I=["default","short","long","audio","video","image"],w=1,l=["Item","Group","FieldList","Repeat"];function c(Q){this.forms=Q.forms||[];this.id=Q.id||"";this.autoId=P.isUndefined(Q.autoId)?true:Q.autoId;this.hasMarkdown=P.isUndefined(Q.hasMarkdown)?false:Q.hasMarkdown;this.itextModel=Q.itextModel;this.key=String(w++);this.refCount=1}c.prototype={clone:function(){var Q=new c({forms:P.map(this.forms,function(R){return R.clone()}),id:this.id,autoId:this.autoId,itextModel:this.itextModel,hasMarkdown:this.hasMarkdown});return Q},getForms:function(){return this.forms},getFormNames:function(){return this.forms.map(function(Q){return Q.name})},hasForm:function(Q){return this.getFormNames().indexOf(Q)!==-1},getForm:function(Q){return j.reduceToOne(this.forms,function(R){return R.name===Q},"form name = "+Q)},getOrCreateForm:function(Q){try{return this.getForm(Q)}catch(R){return this.addForm(Q)}},addForm:function(Q){if(!this.hasForm(Q)){var R=new i({name:Q,itextModel:this.itextModel});this.forms.push(R);return R}},removeForm:function(R){var S=this.getFormNames();var Q=S.indexOf(R);if(Q!==-1){this.forms.splice(Q,1)}},cloneForm:function(R,S){var Q=this.getOrCreateForm(R).clone();Q.name=S;this.forms.push(Q)},get:function(Q,R){if(P.isUndefined(Q)||Q===null){Q="default"}if(P.isUndefined(R)||R===null){R=this.itextModel.getDefaultLanguage()}if(this.hasForm(Q)){return this.getForm(Q).getValue(R)}},set:function(T,S,U){if(P.isUndefined(S)||S===null){S="default"}var R=this.getOrCreateForm(S);if(P.isUndefined(U)||U===null){U=this.itextModel.getDefaultLanguage();var Q=R.getValue(U);R.setValue(U,T);P.each(this.itextModel.languages,function(W){var V=R.getValue(W);if(!V||V===Q){R.setValue(W,T)}})}else{R.setValue(U,T)}},defaultValue:function(){return this.get()},isEmpty:function(){if(this.forms){return P.every(this.forms,function(Q){return Q.isEmpty()})}return true},hasHumanReadableItext:function(){return Boolean(this.hasForm("default")||this.hasForm("long")||this.hasForm("short"))}};function i(Q){this.itextModel=Q.itextModel;this.data=Q.data||{};this.name=Q.name||"default";this.outputExpressions=null}i.prototype={clone:function(){return new i({itextModel:this.itextModel,data:P.clone(this.data),name:this.name})},getValue:function(Q){return this.data[Q]},setValue:function(R,Q){this.data[R]=Q;this.outputExpressions=null},getValueOrDefault:function(S){if(this.data[S]){return this.data[S]}var R=this.itextModel.getDefaultLanguage();if(S!==R&&this.data[R]){return this.data[R]}for(var Q in this.data){if(this.data.hasOwnProperty(Q)&&this.data[Q]){return this.data[Q]}}return""},isEmpty:function(){for(var Q in this.data){if(this.data.hasOwnProperty(Q)&&this.data[Q]){return false}}return true},getOutputRefExpressions:function(){if(this.outputExpressions===null){this.updateOutputRefExpressions()}return this.outputExpressions},updateOutputRefExpressions:function(){var Q={},S,T,R;for(var U in this.data){if(this.data.hasOwnProperty(U)&&this.data[U]){T=/(?:<output (?:value|ref)=")(.*?)(?:"\s*(?:\/|><\/output)>)/gim;S=[];R=T.exec(this.data[U]);while(R!==null){S.push(R[1]);R=T.exec(this.data[U])}Q[U]=S}}this.outputExpressions=Q}};function r(){j.eventuality(this);this.languages=[]}r.prototype={getLanguages:function(){return this.languages},hasLanguage:function(Q){return this.languages.indexOf(Q)!==-1},addLanguage:function(Q){if(!this.hasLanguage(Q)){this.languages.push(Q)}},removeLanguage:function(Q){if(this.hasLanguage(Q)){this.languages.splice(this.languages.indexOf(Q),1)}if(this.getDefaultLanguage()===Q){this.setDefaultLanguage(this.languages.length>0?this.languages[0]:"")}},setDefaultLanguage:function(Q){this.defaultLanguage=Q},getDefaultLanguage:function(){if(this.defaultLanguage){return this.defaultLanguage}else{return this.languages.length>0?this.languages[0]:""}},createItem:function(S,Q,R){return new c({id:S,autoId:Q,itextModel:this,forms:[new i({name:"default",itextModel:this})],hasMarkdown:R})},updateForNewMug:function(Q){return this.updateForMug(Q,Q.getDefaultLabelValue())},updateForExistingMug:function(Q){return this.updateForMug(Q,Q.getLabelValue())},updateForMug:function(R,Q){if(!R.options.isDataOnly){if(!R.p.labelItext&&R.getPresence("labelItext")!=="notallowed"){var S=R.p.labelItext=this.createItem();S.set(Q)}if(!R.p.hintItext&&R.getPresence("hintItext")!=="notallowed"){R.p.hintItext=this.createItem()}if(!R.p.helpItext&&R.getPresence("helpItext")!=="notallowed"){R.p.helpItext=this.createItem()}}if(!R.options.isControlOnly){if(R.getPresence("constraintMsgItext")!=="notallowed"&&!R.p.constraintMsgItext){R.p.constraintMsgItext=this.createItem()}}}};var a=["labelItext","hintItext","helpItext","constraintMsgItext"];function t(S,R){var Q={};S.tree.walk(function(T,U,V){if(T){P.each(a,function(X){var W=T.p[X];if(W&&!W.key){window.console.log("ignoring ItextItem without a key: "+W.id);return}else{if(W&&!Q.hasOwnProperty(W.key)){Q[W.key]=true;R(W,T,X)}}})}V()})}function D(U,S){var V=S,Q=[],R={},T=P.object(P.map(a,function(W){return[W,W.replace("Itext","")]}));t(U,function(aa,X,ab){var W=aa.isEmpty();if(!W||V){var ac=aa.autoId||!aa.id?z(X,T[ab]):aa.id,Y=ac,Z=2;if(R.hasOwnProperty(ac)&&(W||aa===R[ac])){return}while(R.hasOwnProperty(ac)){ac=Y+Z;Z++}aa.id=ac;R[ac]=aa;if(!S){Q.push(aa)}}});return S?R:Q}var m=function(X,ac){var W=y.text(X,ac),Y=W.input,aa=null;function Z(){return z(X,W.path)}function S(){U(Z());R(true)}function ab(){if(aa.refCount>1){aa.refCount--;aa=aa.clone()}}var U=W.setValue;W.setValue=function(ad){aa=ad;if(ad.autoId){S()}else{U(ad.id);R(false)}};W.getValue=function(){aa.id=Y.val();aa.autoId=T();return aa};var V=x("<input />").attr("type","checkbox");V.change(function(){if(x(this).prop("checked")){ab();S();W.handleChange()}});function R(ad){V.prop("checked",ad)}function T(){return V.prop("checked")}var Q=W.getUIElement;W.getUIElement=function(){var ae=Q(),ad=x("<div />").addClass("fd-itextID-checkbox-container"),af=x("<label />").text("auto?").addClass("checkbox");af.prepend(V);ad.append(af);ae.css("position","relative");ae.find(".controls").not(".messages").addClass("fd-itextID-controls").after(ad);return ae};W.input.keyup(function(){var ad=x(this).val();if(ad!==Z()){R(false)}});X.on("property-changed",function(ad){if(T()&&ad.property==="nodeID"){Y.val(Z())}},null,"teardown-mug-properties");return W};var h=function(Q,R){var T=Q.form.vellum.data.javaRosa.Itext,V={};j.eventuality(V);V.mug=Q;V.itextType=R.itextType;V.languages=T.getLanguages();V.defaultLang=T.getDefaultLanguage();V.forms=R.forms||["default"];V.getItextItem=function(){return R.getItextByMug(V.mug)};V.setValue=function(W){};V.getValue=function(W){};var S=x("<div />").addClass("controls").addClass("messages"),U=x("<div />").addClass("itext-block-container").addClass("itext-block-"+V.itextType);V.getFormGroupClass=function(W){return"itext-block-"+V.itextType+"-group-"+W};V.getFormGroupContainer=function(W){return x("<div />").addClass(V.getFormGroupClass(W)).addClass("itext-lang-group")};V.getForms=function(){return V.forms};V.refreshMessages=function(){if(R.messagesPath){var W=y.util.getMessages(Q,R.messagesPath);S.empty().append(W)}};Q.on("messages-changed",function(){V.refreshMessages()},null,"teardown-mug-properties");V.getUIElement=function(){P.each(V.getForms(),function(X){var W=V.getFormGroupContainer(X);P.each(V.languages,function(aa){var Y=V.itextWidget(V.mug,aa,X,P.extend(R,{parent:U}));Y.init();Y.on("change",function(){V.fire("change")});var Z=Y.getUIElement();y.util.setWidget(Z,Y);W.append(Z)});U.append(W)});U.append(S);return U};return V};var B=function(Q,R){var S=h(Q,R);if(P.contains(l,Q.__className)){S.itextWidget=s}else{S.itextWidget=p}return S};var K=function(Q,R){var V=h(Q,R);V.isCustomAllowed=R.isCustomAllowed;V.activeForms=V.getItextItem().getFormNames();V.displayName=R.displayName;V.formToIcon=R.formToIcon||{};V.itextWidget=d;V.getForms=function(){var W=P.difference(V.activeForms,I),X=P.intersection(V.activeForms,V.forms);return P.union(W,X)};var S=V.getFormGroupContainer;V.getFormGroupContainer=function(X){var W=S(X);W.addClass("itext-lang-group-config").data("formtype",X);return W};V.getAddFormButtonClass=function(W){return"itext-block-"+V.itextType+"-add-form-"+W};V.getAddFormButtons=function(){var W=x("<div />").addClass("btn-group itext-options");P.each(V.forms,function(Z){var Y=x("<div />");Y.text(" "+Z).addClass(V.getAddFormButtonClass(Z)).addClass("btn itext-option").click(function(){V.addItext(Z)});var X=V.formToIcon[Z];if(X){Y.prepend(x("<i />").addClass(X).after(" "))}if(V.activeForms.indexOf(Z)!==-1){Y.addClass("disabled")}W.append(Y)});if(V.isCustomAllowed){W.append(V.getAddCustomItextButton())}return W};V.getAddCustomItextButton=function(){var W=x("<button />").text("custom...").addClass("btn").attr("type","button"),X="fd-new-itext-button";W.click(function(){var Z,aa,Y;Z=Q.form.vellum.generateNewModal("New Content Type",[{title:"Add",cssClasses:X+" disabled ",attributes:{disabled:"disabled"}}]);aa=x(k({label:"Content Type"}));Y=x("<input />").attr("type","text");Y.keyup(function(){var ac=x(this).val(),ab=Q.form.vellum.$f.find("."+X);if(!ac||I.indexOf(ac)!==-1||V.activeForms.indexOf(ac)!==-1){ab.addClass("disabled").removeClass("btn-primary").attr("disabled","disabled")}else{ab.removeClass("disabled").addClass("btn-primary").removeAttr("disabled")}});aa.find(".controls").not(".messages").append(Y);Z.find(".modal-body").append(aa);Q.form.vellum.$f.find("."+X).click(function(){var ab=Y.val();if(ab){V.addItext(Y.val());Z.modal("hide")}});Z.modal("show");Z.one("shown",function(){Y.focus()})});return W};V.deleteItextForm=function(X){var W=V.getItextItem();if(W){W.removeForm(X)}V.activeForms=P.without(V.activeForms,X);V.fire("change")};V.getDeleteFormButton=function(W){var X=x(g);X.addClass("pull-right").click(function(){var Y=x("."+V.getFormGroupClass(W));V.deleteItextForm(W);V.mug.fire({type:"question-itext-deleted",form:W});Y.remove();x(this).remove();x("."+V.getAddFormButtonClass(W)).removeClass("disabled")});return X};V.addItext=function(W){if(V.activeForms.indexOf(W)!==-1){return}V.activeForms.push(W);V.fire("change");x("."+V.getAddFormButtonClass(W)).addClass("disabled");var X=V.getFormGroupContainer(W);P.each(V.languages,function(aa){var Y=V.itextWidget(V.mug,aa,W,R);Y.init(true);Y.on("change",function(){V.fire("change")});var Z=Y.getUIElement();y.util.setWidget(Z,Y);X.append(Z)});U.find(".new-itext-control-group").after(X);X.before(V.getDeleteFormButton(W))};var U=x("<div />"),T=V.getUIElement;V.getUIElement=function(){U=T();var W=x(k({label:V.displayName,}));W.addClass("new-itext-control-group").find(".controls").not(".messages").append(V.getAddFormButtons());U.prepend(W);var X=U.find(".itext-lang-group");X.each(function(){x(this).before(V.getDeleteFormButton(x(this).data("formtype")))});return U};return V};var e=function(Q,R){var T=K(Q,R),S=R.pathPrefix;if(!P.isString(R.pathPrefix)){S="/"+R.itextType}T.getForms=function(){return P.intersection(T.activeForms,T.forms)};T.itextWidget=L(S+Q.form.getBasePath());return T};var s=function(V,T,R,Z){var S=V.form.vellum,W=S.data.javaRosa.Itext,Q="itext-"+T+"-"+Z.itextType;if(Z.idSuffix){Q=Q+Z.idSuffix}Z.id=Q;var U=y.multilineText(V,Z),X=U.input;if(Z.path==="labelItext"){j.questionAutocomplete(X,V,{category:"Output Value",insertTpl:'<output value="${name}" />',property:"labelItext",});X.addClass("jstree-drop");X.keydown(function(ah){if(ah&&ah.which===8||ah.which===46){var af=U.getControl()[0],aj=j.getCaretPosition(af),ac=U.getValue(),aa="<output",ae="/>",ab,ad,ag;if(ah.which===8){ag=ac.substr(aj-2,2);if(ag===ae){ab=ac.lastIndexOf(aa,aj);ad=aj}}else{if(ah.which===46){ag=ac.substr(aj,aa.length);if(ag===aa){ad=ac.indexOf(ae,aj);ad=ad===-1?ad:ad+2;ab=aj}}}if(ab||ad&&ab!==-1&&ad!==-1){var ai=ac.slice(0,ab)+ac.slice(ad,ac.length);U.setValue(ai);j.setCaretPosition(af,ab);ah.preventDefault()}}})}U.displayName=Z.displayName;U.itextType=Z.itextType;U.form=R||"default";U.language=T;U.languageName=j.langCodeToName[U.language]||U.language;U.showOneLanguage=W.getLanguages().length<2;U.defaultLang=W.getDefaultLanguage();U.isDefaultLang=U.language===U.defaultLang;U.isSyncedWithDefaultLang=false;U.hasNodeIdAsDefault=Z.path==="labelItext";U.getItextItem=function(){return Z.getItextByMug(U.mug)};U.getItextValue=function(ab){var aa=U.getItextItem();if(!ab){ab=U.language}return aa&&aa.get(U.form,ab)};U.setItextValue=function(ab){var aa=U.getItextItem();if(aa){if(U.isDefaultLang){U.mug.fire({type:"defaultLanguage-itext-changed",form:U.form,prevValue:aa.get(U.form,U.language),value:ab,itextType:U.itextType})}aa.getForm(U.form).setValue(U.language,ab);U.fireChangeEvents()}};U.getLangDesc=function(){if(U.showOneLanguage){return""}return" ("+U.languageName+")"};U.getDisplayName=function(){return U.displayName+U.getLangDesc()};U.init=function(ac){if(ac){var aa=U.getDefaultValue();U.getItextItem().getOrCreateForm(U.form);U.setValue(aa);U.handleChange()}else{var ab=U.getItextValue();if(!P.isString(ab)){if(!U.isDefaultLang){ab=U.getItextValue(U.defaultLang)||""}else{ab=U.hasNodeIdAsDefault?U.mug.p.nodeID:""}}U.setItextValue(ab);U.setValue(ab)}};var Y=U.updateValue;U.updateValue=function(){Y();if(!U.getValue()&&!U.isDefaultLang){U.setItextValue(U.getItextValue(U.defaultLang))}};U.destroy=function(aa){if(aa.form===U.form){U.fireChangeEvents()}};U.mug.on("question-itext-deleted",U.destroy,null,U);U.toggleDefaultLangSync=function(aa){U.isSyncedWithDefaultLang=!aa&&!U.isDefaultLang};U.getDefaultValue=function(){return null};if(U.hasNodeIdAsDefault&&U.isDefaultLang){U.mug.on("property-changed",function(aa){if(aa.property==="nodeID"){if(U.getItextValue()===aa.previous){U.setItextValue(aa.val);U.setValue(aa.val)}}},null,"teardown-mug-properties")}if(!U.isDefaultLang){U.mug.on("defaultLanguage-itext-changed",function(aa){if(aa.form===U.form&&aa.itextType===U.itextType){if(U.getItextValue()===aa.prevValue){U.setItextValue(aa.value);U.setValue(aa.value)}}},null,"teardown-mug-properties")}U.fireChangeEvents=function(){var aa=U.getItextItem();if(!aa){return}var ab=V.form.getMugList();if(S.data.core.currentItextDisplayLanguage===U.language){ab.map(function(ac){var ad=aa.get(U.form,U.language)||ac.form.vellum.getMugDisplayName(ac),ae=ac.p.labelItext;if(ae&&ae.id===aa.id&&U.form==="default"){ac.form.fire({type:"question-label-text-change",mug:ac,text:ad})}})}};U.refreshMessages=function(){U.getMessagesContainer().empty().append(U.getMessages(V,U.id))};U.save=function(){U.setItextValue(U.getValue())};return U};var p=function(X,U,S,ad){ad=ad||{};var aa=ad.parent;var W=s(X,U,S,ad),Z=W.setValue,T=W.getUIElement,V=W.handleChange,Y=true,ab,ac,R;function Q(ae){return/^\d+\. |^\*|~~.+~~|# |\*{1,3}\S+\*{1,3}|\[.+\]\(\S+\)/m.test(ae)}W.toggleMarkdown=function(){aa.toggleClass("has-markdown")};R=x("<div>").addClass("controls well markdown-output");W.handleChange=function(){V();var af=W.getValue(),ae=this.getItextItem();if(Q(af)){if(Y){aa.removeClass("markdown-ignorant");aa.addClass("has-markdown")}}else{if(!af){aa.removeClass("has-markdown")}}ae.hasMarkdown=ab.is(":visible");R.html(j.markdown(af)).removeClass("hide")};W.setValue=function(ae){Z(ae);if(!ae){R.addClass("hide")}R.html(j.markdown(ae))};W.getUIElement=function(){var ae=T(),af=W.getValue();ae.detach(".markdown-output");ae.append(R);ae.find(".control-label").append(v({title:ad.lstring}));ab=ae.find(".turn-markdown-off").click(function(){Y=false;W.getItextItem().hasMarkdown=false;W.toggleMarkdown();return false});ac=ae.find(".turn-markdown-on").click(function(){Y=true;W.getItextItem().hasMarkdown=true;W.toggleMarkdown();return false});if(W.getItextItem().hasMarkdown){aa.addClass("has-markdown")}else{aa.addClass("markdown-ignorant")}if(Q(af)){R.html(j.markdown(af));ab.removeClass("hide")}return ae};return W};var d=function(Q,U,S,R){R=R||{};R.idSuffix="-"+S;var T=s(Q,U,S,R);T.getDisplayName=function(){return S+T.getLangDesc()};return T};var b={image:"icon-picture",audio:"icon-volume-up",video:"icon-facetime-video"};var L=function(Q){return function(R,V,T,S){var U=d(R,V,T,S);U.getDefaultValue=function(){if(F.indexOf(T)!==-1){var W=H[T];return"jr://file/commcare/"+T+Q+M(U.mug)+"."+W}return null};U.mug.form.vellum.initWidget(U);return U}};var N=function(R,X,V){var Q=["default","audio","image","video"],S=V.getLanguages(),Z=G.makeRowParser(X),U=Z(),T,ab,Y,aa;if(U){U=P.map(U,function(ad){var ac=ad.split(/[-_]/);if(Q.indexOf(ac[0])===-1||S.indexOf(ac[1])===-1){return null}return{form:ac[0],lang:ac[1]}})}var W=D(R,true);for(ab=Z();ab;ab=Z()){aa=W[ab[0]];if(!aa){continue}for(T=1;T<ab.length;T++){Y=U[T];if(Y){if(aa.hasForm(Y.form)){aa.getForm(Y.form).setValue(Y.lang,ab[T])}else{if(x.trim(ab[T])){aa.getOrCreateForm(Y.form).setValue(Y.lang,ab[T])}}}}}V.fire("change")};var C=function(R,W){function U(ad,ab,Z,aa){var ac=[ad];P.each(Z,function(ae){P.each(ab,function(af){ac.push(aa(af,ae))})});return ac}function T(aa,ab,Z){return U(aa.id,ab,Z,function(ad,ac){return aa.hasForm(ac)?aa.get(ac,ad):""})}function V(aa,Z){return U("label",aa,Z,function(ac,ab){return ab+"_"+ac})}var Q=["default","audio","image","video"],S=W.getLanguages(),Y=[];if(S.length>0){var X=D(R);Y.push(V(S,Q));P.each(X,function(Z){Y.push(T(Z,S,Q))})}return G.tabDelimit(Y)};function J(S,R,U){if(!R.options.canOutputValue){var T=S.vellum.getCurrentlySelectedMug(),Q=R.options.typeName;if(T){T.addMessage(null,{key:"javaRosa-output-value-type-error",level:R.WARNING,message:Q+" nodes cannot be used in an output value. Please remove the output value for '"+U+"' or your form will have errors."})}}}function A(R,Q){if(Q){return'<output value="format-date(date('+R+"), '"+Q+"')\"/>"}else{return'<output value="'+R+'" />'}}function E(S,Q,T,V,R){if(T==="."&&S==="label"){var U=Q.p.getDefinition(S).lstring;Q.addMessage(R,{key:"core-circular-reference-warning",level:Q.WARNING,message:"The "+U+" for a question is not allowed to reference the question itself. Please remove the "+V+" from the "+U+" or your form will have errors."})}}function n(R,W,V,T,Q){var S=A(V,Q),U=R.data.core.form;j.insertTextAtCursor(W,S,true);if(T){E("label",T,V,"output value",W.attr("name"));J(U,T,V)}}function M(Q){if(Q.__className==="Item"){var R=new RegExp(j.invalidAttributeRegex.source,"g");return M(Q.parentMug)+"-"+Q.getNodeID().replace(R,"_")}else{var S=Q.form.getAbsolutePath(Q,true);if(!S){if(Q.parentMug){S=Q.form.getAbsolutePath(Q.parentMug,true)+"/"+Q.getNodeID()}else{S="/"+Q.getNodeID()}}return S.slice(1)}}function z(Q,R){return M(Q)+"-"+R}x.vellum.plugin("javaRosa",{langs:["en"],displayLanguage:"en"},{init:function(){this.data.javaRosa.ItextItem=c;this.data.javaRosa.ItextForm=i;this.data.javaRosa.ICONS=b},handleDropFinish:function(Y,Z,U){var S=Y&&Y.attr("name")&&Y.attr("name").lastIndexOf("itext-",0)===0,V=this;if(S){var Q=U&&U.options.typeName;if(Q==="Date"){var T={"":"No Formatting","%d/%n/%y":"DD/MM/YY e.g. 04/01/14","%a, %b %e, %Y":"DDD, MMM DD, YYYY e.g. Sun, Jan 1, 2014"};var W='<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu"><li><strong>Date Format Options</strong></li>';P(T).each(function(aa,ab){W+='<li><a tabindex="-1" href="#" data-format="'+ab+'">'+aa+"</a></li>"});W+="</ul>";var R=x(W);x("body").append(R);R.find("li a").click(function(){var aa=x(this).data("format");n(V,Y,Z,U,aa);if(window.analytics){window.analytics.usage("Output Value","Drag and Drop",aa)}R.remove()});var X=window.event;R.css({top:X.clientY,left:X.clientX}).show()}else{n(V,Y,Z,U);if(window.analytics){window.analytics.usage("Output Value","Drag and Drop")}}}else{V.__callOld()}},handleNewMug:function(Q){var R=this.__callOld();this.data.javaRosa.Itext.updateForNewMug(Q);return R},_makeLanguageSelectorDropdown:function(){var V=this,S=this.data.javaRosa.Itext.getLanguages(),R=this.$f.find("#fd-questions-dropdown-menu"),U,T,Q;Q=P.map(S,function(W){return{code:W,name:j.langCodeToName[W]||W}});Q[Q.length]={code:"_ids",name:"Question ID"};if(Q.length<2){return}R.append(x(u({languages:Q})).html());U=R.find(".fd-display-item");U.click(function(W){T.val(x(this).data("code")).change();W.preventDefault()});T=R.parent().find(".fd-question-tree-display");T.change(function(){var W=T.val();V._changeTreeDisplayLanguage(W);U.removeClass("selected").filter("[data-code="+W+"]").addClass("selected")});T.val(this.data.core.currentItextDisplayLanguage).change()},_changeTreeDisplayLanguage:function(R){var S=this,Q=this.data.core.form;this.data.core.currentItextDisplayLanguage=R;this.data.core.$tree.find("li").each(function(V,W){var U=x(W),T=Q.getMugByUFID(U.prop("id"));try{if(S.data.core.currentItextDisplayLanguage==="_ids"){S.jstree("rename_node",U,T.getNodeID())}else{if(T.p.labelItext){var Y=S.getMugDisplayName(T);S.jstree("rename_node",U,Y||S.opts().core.noTextString)}}}catch(X){if(X!=="NoItextItemFound"){throw X}}})},loadXML:function(W){var U=this,R=this.opts().javaRosa.langs,V,T;this.data.javaRosa.Itext=V=new r();this.data.javaRosa.itextMap=T={};function Q(){var aa=x(this);var ab=aa.attr("lang");function ac(){var ad=x(this);var ag=ad.attr("id");var ae=T[ag];if(!ae||!T.hasOwnProperty(ag)){ae=V.createItem(ag);ae.refCount=0;T[ag]=ae}function af(){var ah=x(this);var ai=ah.attr("form");if(!ai){ai="default";if(ae.hasMarkdown){return}}else{if(ai==="markdown"){ae.hasMarkdown=true;ae.getOrCreateForm("default").setValue(ab,q.humanize(ah));return}}ae.getOrCreateForm(ai).setValue(ab,q.humanize(ah))}ad.children().each(af)}if(R&&R.indexOf(ab)===-1){U.data.core.parseWarnings.push('You have languages in your form that are not specified in the "Languages" page of the application builder. The following language will be deleted on save unless you add it to the "Languages" page: '+ab+".");return}V.addLanguage(ab);if(aa.attr("default")!==undefined){V.setDefaultLanguage(ab)}aa.children().each(ac)}if(R&&R.length>0){for(var S=0;S<R.length;S++){V.addLanguage(R[S])}V.setDefaultLanguage(R[0])}var Z;if(W){Z=x.parseXML(W);var X=x(Z).find("h\\:head, head"),Y=X.find("itext");x(Y).children().each(Q)}this.data.core.currentItextDisplayLanguage=this.opts().javaRosa.displayLanguage||V.getDefaultLanguage();this._makeLanguageSelectorDropdown();this.__callOld();delete this.data.javaRosa.itextMap;V.on("change",function(){U.onFormChange()})},populateControlMug:function(W,ab){this.__callOld();var X=this.data.javaRosa.Itext,V=this.data.javaRosa.itextMap;function U(af){try{var ad=O.parse(af);if(ad instanceof f.XPathFuncExpr&&ad.id==="jr:itext"){return ad.args[0].value}}catch(ae){}return""}function Q(ag,ae){var af=!ag||ag===z(W,ae);if(ag){var ad=V[ag];if(ad&&V.hasOwnProperty(ag)){if(!af){ad.autoId=false}ad.refCount++;return ad}}return X.createItem(ag,af)}function ac(ad,af){var ae=ad.attr("ref");return Q(ae?U(ae):"",af)}var S=ab.children("label"),Z=ab.children("hint"),Y=ab.children("help");if(S.length&&W.getPresence("label")!=="notallowed"){var T=ac(S,"label");if(T.isEmpty()){var aa=q.humanize(S);T.set(aa||W.getDefaultLabelValue())}W.p.labelItext=T}if(Z.length&&W.getPresence("hintLabel")!=="notallowed"){W.p.hintItext=ac(Z,"hint")}if(Y.length&&W.getPresence("label")!=="notallowed"){W.p.helpItext=ac(Y,"help")}if(W.p.constraintMsgAttr){var R=U(W.p.constraintMsgAttr);if(R){W.p.constraintMsgItext=Q(R,"constraintMsg");W.p.constraintMsgAttr=null}}},handleMugParseFinish:function(Q){this.__callOld();this.data.javaRosa.Itext.updateForExistingMug(Q)},handleMugRename:function(R,V,Y,W,X,Q){this.__callOld();function aa(ac,ab){if(ab){ac=RegExp.escape(ac);return'<output\\s*(ref|value)="'+ac+'"\\s*(/|></output)>'}else{return'<output value="'+ac+'" />'}}var U,T,S,Z;Q=Q?RegExp.escape(Q):Q;if(V.options.isSpecialGroup){U=new RegExp(Q+"/","mg");X=X+"/"}else{U=new RegExp(Q+"(?![\\w/-])","mg")}t(R,function(ac,ab){Z=false;P(ac.forms).each(function(ad){P(ad.getOutputRefExpressions()).each(function(ae,af){P(ae).each(function(ag){if(ag.match(U)){S=ag.replace(U,X);T=new RegExp(aa(ag,true),"mg");ad.setValue(af,ad.getValue(af).replace(T,aa(S)));Z=true}})})});if(Z){R.fire({type:"question-label-text-change",mug:ab,text:ac.get()})}})},duplicateMugProperties:function(Q){this.__callOld();P.each(a,function(S){var R=Q.p[S];if(R&&R.autoId){Q.p[S]=R.clone()}})},contributeToModelXML:function(Y){var Z=this.data.javaRosa.Itext,aa=this.data.javaRosa.itextItemsFromBeforeSerialize,U=Z.getLanguages(),ab,Q,R,S,T;if(U.length>0){Y.writeStartElement("itext");for(var X=0;X<U.length;X++){S=U[X];Y.writeStartElement("translation");Y.writeAttributeString("lang",S);if(Z.getDefaultLanguage()===S){Y.writeAttributeString("default","")}for(var W=0;W<aa.length;W++){ab=aa[W];Y.writeStartElement("text");Y.writeAttributeString("id",ab.id);Q=ab.getForms();for(var V=0;V<Q.length;V++){R=Q[V];T=R.getValueOrDefault(S);Y.writeStartElement("value");if(R.name!=="default"){Y.writeAttributeString("form",R.name)}Y.writeXML(q.normalize(T));Y.writeEndElement()}if(ab.hasMarkdown){T=ab.get("default",S);Y.writeStartElement("value");Y.writeAttributeString("form","markdown");Y.writeXML(q.normalize(T));Y.writeEndElement()}Y.writeEndElement()}Y.writeEndElement()}Y.writeEndElement()}},beforeSerialize:function(){this.__callOld();this.data.javaRosa.itextItemsFromBeforeSerialize=D(this.data.core.form)},afterSerialize:function(){this.__callOld();delete this.data.javaRosa.itextItemsFromBeforeSerialize},beforeBulkInsert:function(Q){this.__callOld();this.data.javaRosa.itextById=D(Q,true)},afterBulkInsert:function(){this.__callOld();delete this.data.javaRosa.itextById},getMugTypes:function(){var Q=this.__callOld(),R=Q.normal;R.Group.spec=j.extend(R.Group.spec,{constraintMsgItext:{presence:"notallowed"}});return Q},getMugSpec:function(){var R=this.__callOld(),U=this,X=R.databind,W=R.control;function T(Z,Y){return function(ab){var aa=ab.p[Z],ac=aa&&aa.hasHumanReadableItext();if(!ac&&ab.getPresence(Z)==="required"){return Y+" is required"}if(aa&&!aa.autoId&&!aa.isEmpty()){if(!aa.id){return Y+" Itext ID is required"}else{if(!j.isValidAttributeValue(aa.id)){return aa.id+" is not a valid ID"}}}return"pass"}}function Q(Y){Y.serialize=function(ad,aa,Z,ac){var ab=false;P.each(ad.forms,function(ae){if(!ae.isEmpty()){ab=true;P.each(ad.itextModel.languages,function(ag){var af=aa+":"+ag+"-"+ae.name;ac[af]=ae.getValue(ag)})}});if(ab&&!ad.autoId){ac[aa]=ad.id}};Y.deserialize=function(aq,ar,ap,ae){var am=ap.p[ar],ag=false;if(aq[ar]){var ac=ap.form.vellum.data.javaRosa.itextById,ai=aq[ar];if(ac.hasOwnProperty(ai)&&!ac[ai].autoId){ap.p[ar]=am=ac[ai];am.refCount++}else{am.id=aq[ar];am.autoId=false;ac[am.id]=am}}var al=am.itextModel.getDefaultLanguage(),af=am.itextModel.languages,Z="",ab=P.object(F,F),ao=U.data.uploader.objectMap||{};if(aq.id){Z=aq.id.slice(aq.id.lastIndexOf("/")+1)}function ak(at){return at===null||at===undefined?"":String(at)}var ad=P.memoize(function(at){return !P.find(af,function(au){return aq[ar+":"+au+"-"+at]})});P.each(af,function(aw){var av=ar.length+aw.length+2,au=new RegExp("^"+RegExp.escape(ar)+":"+RegExp.escape(aw)+"-"),at={};P.each(aq,function(aB,ay){if(au.test(ay)){var aA=ay.slice(av),az=ab.hasOwnProperty(aA);if(az&&!aB&&ad(aA)){return}if(!at.hasOwnProperty(aA)){at[aA]=true;var ax=ar+":"+al+"-"+aA;am.set(aq.hasOwnProperty(ax)?ak(aq[ax]):ak(aB),aA)}am.set(ak(aB),aA,aw);if(az&&!ao.hasOwnProperty(aB)){ap.addMessage(ar,{key:"missing-multimedia-warning",level:ap.WARNING,message:"MultiMedia was not copied; it must be uploaded separately."})}ag=true}})});if(ag&&!aq[ar]){am.id=z(ap,ar.replace(/Itext$/,""))}var aj="javaRosa-discarded-languages-warning",an=new RegExp("^"+RegExp.escape(ar)+":(\\w+)-"),ah=P.filter(P.map(P.keys(aq),function(au){var at=au.match(an);if(at&&af.indexOf(at[1])===-1){return at[1]}}),P.identity);if(ah.length){var aa=ae.get(null,aj);if(aa){aa.langs=P.union(aa.langs,ah);aa.message="Discarded languages: "+aa.langs.join(", ")}else{ae.update(null,{key:aj,level:ap.WARNING,langs:ah,message:"Discarded languages: "+ah.join(", ")})}}ap.validate(ar)};return Y}function S(Z){var Y=Z.p.constraintMsgItext;if(!Z.p.constraintAttr&&Y&&!Y.isEmpty()){return"You cannot have a Validation Error Message with no Validation Condition!"}return"pass"}X.constraintMsgAttr.visibility="visible_if_present";X.constraintMsgItext=Q({visibility:"visible",presence:function(Y){return Y.options.isSpecialGroup?"notallowed":"optional"},lstring:"Validation Message",widget:function(Y,Z){return B(Y,x.extend(Z,{itextType:"constraintMsg",messagesPath:"constraintMsgItext",getItextByMug:function(aa){return aa.p.constraintMsgItext},displayName:"Validation Message"}))},validationFunc:function(aa){var Z=aa.p.constraintMsgItext;if(!aa.p.constraintAttr&&Z&&Z.id&&!Z.autoId){return"Can't have a Validation Message Itext ID without a Validation Condition"}var Y=T("constraintMsgItext","Validation Message")(aa);if(Y==="pass"){Y=S(aa)}return Y}});var V=X.constraintAttr.validationFunc;X.constraintAttr.validationFunc=function(Z){var Y=V(Z);if(Y==="pass"){Y=S(Z)}return Y};X.constraintMsgItextID={visibility:"constraintMsgItext",presence:"optional",lstring:"Validation Message Itext ID",widget:m,widgetValuePath:"constraintMsgItext"};X.constraintMediaIText=function(Y){return Y.isSpecialGroup?undefined:{visibility:"constraintMsgItext",presence:"optional",lstring:"Add Validation Media",widget:function(Z,aa){return e(Z,x.extend(aa,{displayName:"Add Validation Media",itextType:"constraintMsg",getItextByMug:function(ab){return ab.p.constraintMsgItext},forms:F,formToIcon:b}))}}};W.label.visibility="visible_if_present";W.hintLabel.visibility="visible_if_present";W.labelItext=Q({visibility:"visible",presence:"optional",lstring:"Label",widget:function(Y,Z){return B(Y,x.extend(Z,{itextType:"label",messagesPath:"labelItext",getItextByMug:function(aa){return aa.p.labelItext},displayName:"Label"}))},validationFunc:T("labelItext","Label")});W.labelItextID={visibility:"labelItext",presence:"optional",lstring:"Label Itext ID",widget:m,widgetValuePath:"labelItext"};W.hintItext=Q({visibility:"visible",presence:function(Y){return Y.options.isSpecialGroup?"notallowed":"optional"},lstring:"Hint Message",widget:function(Y,Z){return B(Y,x.extend(Z,{itextType:"hint",messagesPath:"hintItext",getItextByMug:function(aa){return aa.p.hintItext},displayName:"Hint Message"}))},validationFunc:T("hintItext","Hint Message")});W.hintItextID={visibility:"hintItext",lstring:"Hint Itext ID",widget:m,widgetValuePath:"hintItext"};W.helpItext=Q({visibility:"visible",presence:function(Y){return Y.options.isSpecialGroup?"notallowed":"optional"},lstring:"Help Message",widget:function(Y,Z){var aa=B(Y,x.extend(Z,{itextType:"help",messagesPath:"helpItext",getItextByMug:function(ab){return ab.p.helpItext},displayName:"Help Message"}));return aa},validationFunc:T("helpItext","Help Message")});W.helpItextID={visibility:"helpItext",lstring:"Help Itext ID",widget:m,widgetValuePath:"helpItext"};W.otherItext=function(Y){return Y.isSpecialGroup?undefined:{visibility:"labelItext",presence:"optional",lstring:"Add Other Content",widget:function(Z,aa){return K(Z,x.extend(aa,{displayName:"Add Other Content",itextType:"label",getItextByMug:function(ab){return ab.p.labelItext},forms:["long","short"],isCustomAllowed:true}))}}};W.mediaItext=function(Y){return Y.isSpecialGroup?undefined:{visibility:"labelItext",presence:"optional",lstring:"Add Multimedia",widget:function(Z,aa){return e(Z,x.extend(aa,{displayName:"Add Multimedia",itextType:"label",pathPrefix:"",getItextByMug:function(ab){return ab.p.labelItext},forms:F,formToIcon:b}))}}};W.helpMediaIText=function(Y){return Y.isSpecialGroup?undefined:{visibility:"helpItext",presence:"optional",lstring:"Add Help Media",widget:function(Z,aa){return e(Z,x.extend(aa,{displayName:"Add Help Media",itextType:"help",getItextByMug:function(ab){return ab.p.helpItext},forms:F,formToIcon:b}))}}};return R},getMainProperties:function(){var Q=this.__callOld();Q.splice(1+Q.indexOf("label"),0,"labelItext");return Q},getLogicProperties:function(){var Q=this.__callOld();Q.splice(1+Q.indexOf("constraintAttr"),0,"constraintMsgItext");return Q},getMediaProperties:function(){var Q=this.__callOld();Q.push("constraintMediaIText");return Q},getAdvancedProperties:function(){var Q=this.__callOld();Q=Q.concat(["labelItextID","constraintMsgItextID","hintItextID","hintItext","helpItextID","helpItext","helpMediaIText",]);Q=Q.concat(["otherItext"]);return Q},getToolsMenuItems:function(){var Q=this;return this.__callOld().concat([{name:"Edit Bulk Translations",action:function(R){Q.showItextModal(R)}}])},showItextModal:function(S){var R=this,T,Q,W,V=R.data.javaRosa.Itext,U=R.data.core.form;T=R.generateNewModal("Edit Bulk Translations",[{title:"Update Translations",cssClasses:"btn-primary",action:function(){N(U,W.val(),V);T.modal("hide");S()}}]);Q=x(o({description:"Copy these translations into a spreadsheet program like Excel. You can edit them there and then paste them back here when you're done. These will update the translations used in your form. Press 'Update Translations' to save changes, or 'Close' to cancel."}));T.find(".modal-body").html(Q);W=Q.find("textarea");W.val(C(U,V));T.modal("show");T.one("shown",function(){W.focus()})}});return{parseXLSItext:N,generateItextXLS:C}});