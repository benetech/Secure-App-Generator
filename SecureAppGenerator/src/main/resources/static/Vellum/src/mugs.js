define(["jquery","underscore","xpathmodels","vellum/tree","vellum/javaRosa","vellum/widgets","vellum/util","vellum/logic"],function(B,R,c,k,i,C,h,I){function K(T,W,V,S){var U=null;h.eventuality(this);if(S){U=R.object(R.map(S,function(Y,X){if(Y&&typeof Y==="object"){if(B.isPlainObject(Y)){Y=R.clone(Y)}else{Y=Y.clone()}}return[X,Y]}))}this.ufid=h.get_guid();this.form=W;this.messages=new D();this._baseSpec=V;this.setOptionsAndProperties(T,U)}K.prototype={setOptionsAndProperties:function(T,U){var S=U||(this.p&&this.p.getAttrs())||{};this.options=h.extend(L,T);this.__className=this.options.__className;this.spec=G(this._baseSpec,this.options.spec,this.options);R.each(this.spec,function(V,W){if(V.deleteOnCopy){delete S[W]}});this.p=new n({spec:this.spec,mug:this,});this.options.init(this,this.form);this.p.setAttrs(S);this.p.shouldChange=this.form.shouldMugPropertyChange.bind(this.form)},getAppearanceAttribute:function(){return this.options.getAppearanceAttribute(this)},getIcon:function(){return this.options.getIcon(this)},validate:function(S){var T=this;return this._withMessages(function(){var U=false;T.form.updateLogicReferences(T,S);if(S){U=T._validate(S)}else{R.each(R.keys(T.p.__data),function(V){U=T._validate(V)||U})}return U})},_validate:function(S){var U=this,T=U.spec[S];if(!T){window.console.log("unexpected property: "+S);return false}var Z=U.p[S],W=U.getPresence(S),V=T.lstring||S,Y="";if(!Z&&W==="required"){Y=V+" is required."}else{if(Z&&W==="notallowed"){Y=V+" is not allowed."}else{if(T.validationFunc){try{Y=T.validationFunc(U)}catch(X){Y=V+" validation failed\n"+h.formatExc(X)}if(Y==="pass"){Y=""}}}}return this.messages.update(S,{key:"mug-"+S+"-error",level:this.ERROR,message:Y})},ERROR:"error",WARNING:"warning",addMessage:function(S,U){var T=this.messages;this._withMessages(function(){return T.update(S,U)})},dropMessage:function(S,U){var T=this.spec[S];this.addMessage(S,{key:U});if(T&&T.dropMessage){T.dropMessage(this,S,U)}},addMessages:function(T){var S=this;this._withMessages(function(){return R.reduce(T,function(V,W,U){return R.reduce(W,function(X,Y){return S.messages.update(U,Y)||X},false)||V},false)})},_withMessages:function(S){var T=R.isUndefined(this._messagesChanged),U=false;if(T){this._messagesChanged=false}try{U=S()||this._messagesChanged;if(U){if(T){this.fire({type:"messages-changed",mug:this})}else{this._messagesChanged=true}}}finally{if(T){delete this._messagesChanged}}return U},getErrors:function(){return R.uniq(this.messages.get())},getSerializationWarnings:function(){var S=[];this.messages.each(function(T){if(T.fixSerializationWarning){S.push(T)}});return S},fixSerializationWarnings:function(T){var S=this;R.each(T,function(U){U.fixSerializationWarning(S)})},getDefaultLabelValue:function(){var S=this.p.label,T=this.p.nodeID;if(S){return S}else{if(T){return T}}},getLabelValue:function(){var S=this.p.label;if(S){return S}else{return""}},getNodeID:function(){return this.p.nodeID},getPresence:function(T){var S=this.spec[T];if(R.isUndefined(S)){throw new Error("unknown property: $1.spec.$2".replace("$1",this.__className).replace("$2",T))}if(R.isFunction(S.presence)){return S.presence(this)}return S.presence},isVisible:function(U){if(this.getPresence(U)==="notallowed"){return false}var S=this.spec[U],T=S.visibility;if(T==="visible"){return true}if(T==="visible_if_present"){return !R.isUndefined(this.p[U])}if(T==="hidden"){return false}if(R.isFunction(T)){return T(this,S)}if(this.spec.hasOwnProperty(T)){return this.isVisible(T)}throw new Error("unknown visibility: $1.spec.$2 = $3".replace("$1",this.__className).replace("$2",U).replace("$3",String(T)))},getDisplayName:function(Y){var U=this.p.labelItext,W=this.form.vellum.data.javaRosa.Itext,T=W.getDefaultLanguage(),S,V,X=this.p.conflictedNodeId||this.p.nodeID;if(this.__className==="ReadOnly"){return"Unknown (read-only) question type"}if(this.__className==="Itemset"){return"Lookup Table Data"}if(!U||Y==="_ids"){return X}Y=Y||T;if(!Y){return"No Translation Data"}V=U.get("default",T);S=U.get("default",Y)||V;if(S&&S!==X){if(Y!==T&&S===V){S+=" ["+T+"]"}return B("<div>").text(S).html()}return X},serialize:function(){var S=this,T={type:S.__className};R.each(S.spec,function(U,V){if(S.getPresence(V)==="notallowed"){return}var W=S.p[V];if(U.serialize){W=U.serialize(W,V,S,T);if(!R.isUndefined(W)){T[V]=W}}else{if(W&&!(R.isEmpty(W)&&(R.isObject(W)||R.isArray(W)))){T[V]=W}}});return T},deserialize:function(U,V){var T=this,S=[];R.each(T.spec,function(W,X){if(T.getPresence(X)!=="notallowed"){if(W.deserialize){var Y=W.deserialize(U,X,T,V);if(!R.isUndefined(Y)){if(Y instanceof r){S.push(Y)}else{T.p[X]=Y}}}else{if(U.hasOwnProperty(X)){T.p[X]=U[X]}}}});return S},teardownProperties:function(){this.fire({type:"teardown-mug-properties",mug:this})},isInRepeat:function(){if(this.__className==="Repeat"){return true}return this.parentMug&&this.parentMug.isInRepeat()}};Object.defineProperty(K.prototype,"absolutePath",{get:function(){return this.form.getAbsolutePath(this)}});Object.defineProperty(K.prototype,"parentMug",{get:function(){var S=this.form.tree.getNodeFromMug(this);if(S&&S.parent){return S.parent.value}else{return null}}});function G(U,V,T){var W=U.control,X=U.databind;if(T.isDataOnly){W={}}else{if(T.isControlOnly){X={}}}var S=B.extend(true,{},X,W,V);R.each(S,function(Y,Z){if(R.isFunction(Y)){Y=Y(T)}if(!Y){delete S[Z];return}S[Z]=Y});return S}function r(S){this.execute=S}function D(){this.messages={}}D.prototype={update:function(S,X){S=S||"";if(arguments.length===1){if(this.messages.hasOwnProperty(S)){delete this.messages[S];return true}return false}if(!this.messages.hasOwnProperty(S)&&!X.message){return false}if(!X.key){throw new Error("missing key: "+JSON.stringify(X))}var U=this.messages[S]||[],W=false;for(var T=U.length-1;T>=0;T--){var V=U[T];if(V.key===X.key){if(V.level===X.level&&V.message===X.message){return false}U.splice(T,1);W=true;break}}if(X.message){U.push(X)}else{if(!W){return false}}if(U.length){this.messages[S]=U}else{delete this.messages[S]}return true},get:function(S,T){if(arguments.length){if(T){return R.find(this.messages[S||""],function(U){return U.key===T})||null}return R.pluck(this.messages[S||""],"message")}return R.flatten(R.map(this.messages,function(U){return R.pluck(U,"message")}))},each:function(){var S,T;if(arguments.length>1){S=arguments[0]||"";T=arguments[1];R.each(this.messages[S],T)}else{T=arguments[0];R.each(this.messages,function(V,U){R.each(V,function(W){T(W,U)})})}}};function n(S){this.__data={};this.__spec=S.spec;this.__mug=S.mug;this.shouldChange=function(){return function(){}}}n.setBaseSpec=function(S){R.each(S,function(T,U){Object.defineProperty(n.prototype,U,{get:function(){return this._get(U)},set:function(V){this._set(U,V)},configurable:true})})};n.prototype={getDefinition:function(S){return this.__spec[S]},getAttrs:function(){return R.clone(this.__data)},has:function(S){return this.__data.hasOwnProperty(S)},set:function(S,T){if(arguments.length>1){this.__data[S]=T}else{delete this.__data[S]}},_get:function(S){return this.__data[S]},_set:function(S,W){var U=this.__spec[S],V=this.__data[S],T=this.__mug;if(!U||W===V||(T.getPresence(S)==="notallowed"&&T.__className!=="DataBindOnly")){return}var X=this.shouldChange(T,S,W,V);if(X){if(U.setter){U.setter(T,S,W)}else{this.__data[S]=W}X()}},setAttrs:function(S){var T=this;R(S).each(function(V,U){T[U]=V})}};function m(V,T,S,U){if(V&&/\binstance\(/.test(V)){U.instances=R.extend(U.instances||{},S.form.parseInstanceRefs(V))}return V||undefined}function g(U,T,S){if(U.hasOwnProperty("instances")&&!R.isEmpty(U.instances)){S.form.updateKnownInstances(U.instances)}return U[T]}function z(S){S.p.conflictedNodeId=null}var J={databind:{nodeID:{visibility:"visible",presence:"required",lstring:"Question ID",setter:function(T,S,U){T.form.moveMug(T,"rename",U)},mugValue:function(S,T){if(arguments.length===1){if(S.p.has("conflictedNodeId")){return S.p.conflictedNodeId}return S.p.nodeID}S.p.nodeID=T},widget:C.identifier,validationFunc:function(S){var T={key:"mug-nodeID-case-warning",level:S.WARNING,};if(!S.parentMug&&S.p.nodeID==="case"){T.message="The ID 'case' may cause problems with case management. It is recommended to pick a different Question ID."}S.addMessage("nodeID",T);if(!h.isValidElementName(S.p.nodeID)){return S.p.nodeID+" is not a legal Question ID. It must start with a letter and contain only letters, numbers, and '-' or '_' characters."}return"pass"},dropMessage:function(T,S,U){if(S==="nodeID"&&U==="mug-conflictedNodeId-warning"){z(T)}},serialize:function(V,T,S,U){U.id=S.form.getAbsolutePath(S,true)},deserialize:function(U,T,S){if(U.id&&U.id!==S.p.nodeID){S.p.nodeID=U.id.slice(U.id.lastIndexOf("/")+1)||S.form.generate_question_id(null,S);if(U.conflictedNodeId){return new r(function(){S.p.nodeID=U.conflictedNodeId})}}return new r(function(){if(S.p.conflictedNodeId){z(S)}})}},conflictedNodeId:{visibility:"hidden",presence:"optional",setter:function(T,S,V){var U=null;if(V){T.p.set(S,V);U="This question has the same Question ID as another question in the same group. Please choose a unique Question ID."}else{T.p.set(S)}T.addMessage("nodeID",{key:"mug-conflictedNodeId-warning",level:T.WARNING,message:U,fixSerializationWarning:z})},deserialize:function(){}},dataValue:{visibility:"visible_if_present",presence:"optional",lstring:"Default Data Value",},xmlnsAttr:{visibility:"visible",presence:"notallowed",lstring:"Special Hidden Value XMLNS attribute"},rawDataAttributes:{presence:"optional",lstring:"Extra Data Attributes",},relevantAttr:{visibility:"visible",presence:"optional",widget:C.xPath,xpathType:"bool",serialize:m,deserialize:g,lstring:"Display Condition"},calculateAttr:{visibility:"visible_if_present",presence:"optional",widget:C.xPath,xpathType:"generic",serialize:m,deserialize:g,lstring:"Calculate Condition"},constraintAttr:{visibility:"visible",presence:"optional",validationFunc:function(S){return J.databind.constraintMsgAttr.validationFunc(S)},widget:C.xPath,xpathType:"bool",serialize:m,deserialize:g,lstring:"Validation Condition"},constraintMsgAttr:{visibility:"visible",presence:"optional",validationFunc:function(S){if(S.p.constraintMsgAttr&&!S.p.constraintAttr){return"You cannot have a Validation Error Message with no Validation Condition!"}else{return"pass"}},lstring:"Validation Error Message"},requiredAttr:{visibility:"visible",presence:"optional",lstring:"Is this Question Required?",widget:C.checkbox},nodeset:{visibility:"hidden",presence:"optional"},rawBindAttributes:{presence:"optional",lstring:"Extra Bind Attributes"},defaultValue:{visibility:"hidden",presence:"optional",lstring:"Default Value",widget:C.xPath,xpathType:"generic",serialize:m,deserialize:g,validationFunc:function(S){var T=new I.LogicExpression(S.p.defaultValue).getPaths();T=R.filter(T,function(U){return U.initial_context!==c.XPathInitialContextEnum.EXPR});if(T.length){return"You are referencing a node in this form. This can cause errors in the form"}return"pass"}},},control:{appearance:{deleteOnCopy:true,visibility:"visible",presence:"optional",lstring:"Appearance Attribute"},label:{visibility:"visible",presence:"optional",lstring:"Default Label",validationFunc:function(S){if(!S.p.label&&S.getPresence("label")==="required"){return"Default Label is required"}return"pass"}},hintLabel:{visibility:"visible",presence:"optional",lstring:"Hint Label"},rawControlAttributes:{presence:"optional",lstring:"Extra Control Attributes",},rawControlXML:{presence:"optional",lstring:"Raw XML"},dataParent:{lstring:"Data Parent",visibility:function(S){function T(U){if(!U){return true}else{if(!U.options.possibleDataParent){return false}}return T(U.parentMug)}return T(S.parentMug)},presence:"optional",setter:function(T,S,U){var V=T.absolutePath;T.p.set(S,U);T.form._updateMugPath(T,V)},widget:C.droppableText,validationFunc:function(T){var V=T.p.dataParent,U=T.form,S;if(V){S=U.getMugByPath(V);if(!S&&U.getBasePath().slice(0,-1)!==V){return"Must be valid path"}else{if(S&&!S.options.possibleDataParent){return S.absolutePath+" is not a valid data parent"}else{if(!T.spec.dataParent.visibility(T)){return"Children of repeat groups cannot have a different data parent"}}}}return"pass"}},}};var L={typeName:"Base",tagName:"input",isDataOnly:false,isControlOnly:false,isTypeChangeable:true,typeChangeError:function(T,S){return""},isRemoveable:true,isCopyable:true,isODKOnly:false,canOutputValue:true,maxChildren:-1,icon:null,supportsDataNodeRole:false,parseDataNode:function(T,S){return S.children()},controlNodeChildren:null,getPathName:null,getTagName:null,dataChildFilter:null,controlChildFilter:null,getExtraDataAttributes:null,writeDataNodeXML:null,getBindList:function(T){var V=T.p.constraintMsgItext,S;if(V&&!V.isEmpty()){S="jr:itext('"+V.id+"')"}else{S=T.p.constraintMsgAttr}var U={nodeset:T.form.getAbsolutePath(T),type:T.options.dataType,constraint:T.p.constraintAttr,"jr:constraintMsg":S,relevant:T.p.relevantAttr,required:h.createXPathBoolFromJS(T.p.requiredAttr),calculate:T.p.calculateAttr,};R.each(T.p.rawBindAttributes,function(X,W){if(!U.hasOwnProperty(W)||R.isUndefined(U[W])){U[W]=X}});return U.nodeset?[U]:[]},getSetValues:function(S){var T=[];if(S.p.defaultValue){T=[{value:S.p.defaultValue,event:S.isInRepeat()?"jr-insert":"xforms-ready",ref:S.absolutePath}]}return T},writeControlLabel:true,writeControlHint:true,writeControlHelp:true,writeControlRefAttr:"ref",writeCustomXML:null,writesOnlyCustomXML:false,afterInsert:function(T,S){},getAppearanceAttribute:function(S){return S.p.appearance},getIcon:function(S){return S.options.icon},init:function(S,T){},spec:{}};var s=h.extend(L,{isDataOnly:true,typeName:"Hidden Value",icon:"fcc fcc-fd-variable",isTypeChangeable:false,spec:{xmlnsAttr:{presence:"optional"},requiredAttr:{presence:"notallowed"},constraintAttr:{presence:"notallowed"},calculateAttr:{visibility:"visible"}}});var y=h.extend(L,{writesOnlyCustomXML:true,writeCustomXML:function(S,T){return S.writeXML(B("<div>").append(T.p.rawControlXML).clone().html())},spec:{readOnlyControl:{visibility:"visible",widget:C.readOnlyControl}}});var p=h.extend(L,{typeName:"Text",dataType:"xsd:string",icon:"fcc fcc-fd-text",init:function(S,T){}});var f=h.extend(p,{typeName:"Phone Number or Numeric ID",icon:"icon-signal",init:function(S,T){p.init(S,T);S.p.appearance="numeric"}});var u=h.extend(L,{typeName:"Password",dataType:"xsd:string",tagName:"secret",icon:"icon-key",canOutputValue:false,init:function(S,T){}});var t=h.extend(L,{typeName:"Integer",dataType:"xsd:int",icon:"fcc fcc-fd-numeric",init:function(S,T){}});var j=h.extend(L,{typeName:"Audio Capture",dataType:"binary",tagName:"upload",icon:"fcc fcc-fd-audio-capture",isODKOnly:true,mediaType:"audio/*",canOutputValue:false,writeCustomXML:function(S,T){S.writeAttributeString("mediatype",T.options.mediaType)},});var M=h.extend(j,{typeName:"Image Capture",icon:"icon-camera",mediaType:"image/*",});var N=h.extend(j,{typeName:"Video Capture",icon:"icon-facetime-video",mediaType:"video/*",});var O=h.extend(M,{typeName:"Signature Capture",icon:"fcc fcc-fd-signature",init:function(S,T){M.init(S,T);S.p.appearance="signature"}});var F=h.extend(L,{typeName:"GPS",dataType:"geopoint",icon:"icon-map-marker",isODKOnly:true,init:function(S,T){}});var d=h.extend(L,{typeName:"Barcode Scan",dataType:"barcode",icon:"icon-barcode",isODKOnly:true,init:function(S,T){}});var H=h.extend(L,{typeName:"Date",dataType:"xsd:date",icon:"icon-calendar",init:function(S,T){}});var v=h.extend(L,{typeName:"Date and Time",dataType:"xsd:dateTime",icon:"fcc fcc-fd-datetime",init:function(S,T){}});var E=h.extend(L,{typeName:"Time",dataType:"xsd:time",icon:"icon-time",init:function(S,T){}});var o=h.extend(t,{typeName:"Long",dataType:"xsd:long",icon:"fcc fcc-fd-long",init:function(S,T){}});var a=h.extend(t,{typeName:"Decimal",dataType:"xsd:double",icon:"fcc fcc-fd-decimal",init:function(S,T){}});var q=h.extend(L,{isControlOnly:true,typeName:"Choice",tagName:"item",icon:"fcc fcc-fd-single-circle",isTypeChangeable:false,canOutputValue:false,getIcon:function(S){if(S.parentMug.__className==="Select"){return"fcc fcc-fd-single-circle"}else{return"fcc fcc-fd-multi-box"}},writeControlHint:false,writeControlHelp:false,writeControlRefAttr:null,writeCustomXML:function(S,T){var U=T.p.nodeID;if(U){S.writeStartElement("value");S.writeString(U);S.writeEndElement()}},init:function(S,T){},spec:{nodeID:{lstring:"Choice Value",visibility:"visible",presence:"required",widget:C.identifier,setter:null,validationFunc:function(S){if(/\s/.test(S.p.nodeID)){return"Whitespace in values is not allowed."}if(S.parentMug){var U=S.form.getChildren(S.parentMug),T=R.any(U,function(V){return V!==S&&V.p.nodeID===S.p.nodeID});if(T){return"This choice value has been used in the same question"}}return"pass"},serialize:function(V,T,S,U){var W=S.form.getAbsolutePath(S.parentMug,true);U.id=W+"/"+V},deserialize:function(S){return S.id&&S.id.slice(S.id.lastIndexOf("/")+1)}},conflictedNodeId:{presence:"notallowed"},hintLabel:{presence:"notallowed"},hintItext:{presence:"notallowed"},helpItext:{presence:"notallowed"},defaultValue:{presence:"optional",visibility:"hidden"},}});var x=h.extend(L,{typeName:"Label",dataType:'xsd:string" readonly="true()',tagName:"input",icon:"icon-tag",init:function(S,T){S.p.appearance="minimal"},spec:{dataValue:{presence:"optional"},defaultValue:{presence:"optional",visibility:"hidden"},requiredAttr:{visibility:function(S){return S.p.appearance!=="minimal"}},}});var A=h.extend(L,{validChildTypes:["Item"],controlNodeChildren:function(S){return S.children().not("label, value, hint, help")},typeChangeError:function(T,S){if(T.form.getChildren(T).length>0&&!S.match(/^M?Select$/)){return"Cannot change a Multiple/Single Choice question to a non-Choice question if it has Choices. Please remove all Choices and try again."}return""},afterInsert:function(U,S){var T="Item";U.createQuestion(S,"into",T,true);U.createQuestion(S,"into",T,true)},});var w=h.extend(A,{typeName:"Multiple Answer",tagName:"select",icon:"fcc fcc-fd-multi-select",spec:{},defaultOperator:"selected"});var l=h.extend(w,{typeName:"Single Answer",tagName:"select1",icon:"fcc fcc-fd-single-select",defaultOperator:null});var e=h.extend(L,{typeName:"Group",tagName:"group",icon:"icon-folder-open",isSpecialGroup:true,isNestableGroup:true,isTypeChangeable:false,possibleDataParent:true,canOutputValue:false,controlNodeChildren:function(S){return S.children().not("label, value, hint, help")},init:function(S,T){},spec:{hintLabel:{presence:"notallowed"},hintItext:{presence:"notallowed"},helpItext:{presence:"notallowed"},calculateAttr:{presence:"notallowed"},constraintAttr:{presence:"notallowed"},constraintMsgAttr:{presence:"notallowed"},dataValue:{presence:"notallowed"},requiredAttr:{presence:"notallowed"},defaultValue:{presence:"optional",visibility:"hidden"},}});var Q=h.extend(e,{typeName:"Question List",icon:"icon-reorder",init:function(S,T){e.init(S,T);S.p.appearance="field-list"},});var b=h.extend(e,{typeName:"Repeat Group",icon:"icon-retweet",possibleDataParent:false,controlNodeChildren:function(S){return S.children("repeat").children()},getExtraDataAttributes:function(S){return{}},controlChildFilter:function(W,S){var V=S.form.getAbsolutePath(S),U=S.p.repeat_count,T=R.object(R.filter(R.map(S.p.rawRepeatAttributes,function(Y,X){return X.toLowerCase()!=="jr:noaddremove"?[X,Y]:null}),R.identity));return[new k.Node(W,{getNodeID:function(){},getAppearanceAttribute:function(){return"field-list"},p:{rawControlAttributes:T},options:{tagName:"repeat",writeControlLabel:false,writeControlHint:false,writeControlHelp:false,writeControlRefAttr:null,writeCustomXML:function(X,Y){if(U){X.writeAttributeString("jr:count",String(U));X.writeAttributeString("jr:noAddRemove","true()")}X.writeAttributeString("nodeset",V)},}})]},writeControlRefAttr:null,init:function(S,T){S.p.repeat_count=null},spec:{repeat_count:{lstring:"Repeat Count",visibility:"visible_if_present",presence:"optional",widget:C.droppableText},rawRepeatAttributes:{presence:"optional",lstring:"Extra Repeat Attributes",}}});function P(V,S,W){var Y=this,U=W.features.group_in_field_list;this.auxiliaryTypes=S.auxiliary;this.normalTypes=S.normal;this.baseSpec=V;n.setBaseSpec(h.extend.apply(null,[V.databind,V.control].concat(R.filter(R.pluck(h.extend(this.normalTypes,this.auxiliaryTypes),"spec"),R.identity))));this.allTypes=B.extend({},this.auxiliaryTypes,this.normalTypes);var X=R.keys(this.allTypes),T=R.without.apply(R,[X].concat(R.keys(this.auxiliaryTypes)));if(!U){this.normalTypes.FieldList.validChildTypes=R.without.apply(R,[T].concat(R.without(R.map(this.allTypes,function(aa,Z){return aa.isNestableGroup?Z:null}),null)))}R.each(this.auxiliaryTypes,function(Z){Z.validChildTypes=[]});R.each(this.normalTypes,function(ab,Z){if(ab.validChildTypes){return}var aa;if(ab.isNestableGroup){aa=T}else{aa=[]}ab.validChildTypes=aa});R.each(this.allTypes,function(aa,Z){aa.__className=Z;Y[Z]=aa})}P.prototype={make:function(S,U,V){var W=this.allTypes[S];var T=V?V.p.getAttrs():null;return new K(W,U,this.baseSpec,T)},changeType:function(T,S){var W=T.form,U=W.getChildren(T);var V=this.allTypes[T.__className].typeChangeError(T,S);if(V){throw new Error(V)}T.setOptionsAndProperties(this.allTypes[S]);if(S.indexOf("Select")!==-1){R.each(U,function(X){W.fire({type:"parent-question-type-change",childMug:X})})}T.validate();W.fire({type:"question-type-change",qType:S,mug:T});W.fireChange(T)}};return{defaultOptions:L,baseMugTypes:{normal:{Audio:j,Barcode:d,DataBindOnly:s,Date:H,DateTime:v,Double:a,FieldList:Q,Geopoint:F,Group:e,Image:M,Int:t,Long:o,MSelect:w,PhoneNumber:f,ReadOnly:y,Repeat:b,Secret:u,Select:l,Signature:O,Text:p,Time:E,Trigger:x,Video:N},auxiliary:{Item:q}},MugTypesManager:P,MugMessages:D,WARNING:K.WARNING,ERROR:K.ERROR,baseSpecs:J,deserializeXPath:g,serializeXPath:m,}});