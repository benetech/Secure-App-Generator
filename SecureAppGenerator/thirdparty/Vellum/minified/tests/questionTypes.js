require(["chai","jquery","underscore","tests/utils","text!static/all_question_types.xml"],function(e,d,h,f,g){var j=f.call,i=f.clickQuestion,c=f.addQuestion,b=e.assert,a=[{type:"Text",nodeId:"question1",attrs:{dataValue:"default data value",constraintAttr:"/data/question20 = 2",relevantAttr:"/data/question20",requiredAttr:true,},inputs:{calculateAttr:0}},{type:"Trigger",nodeId:"question2",inputs:{calculateAttr:0,constraintAttr:1,requiredAttr:0,relevantAttr:1}},{type:"Trigger",nodeId:"question30",attrs:{appearance:""},},{type:"Select",nodeId:"question3",inputs:{calculateAttr:0,constraintAttr:1,requiredAttr:1,relevantAttr:1}},{type:"MSelect",nodeId:"question6"},{type:"Int",nodeId:"question13"},{type:"PhoneNumber",nodeId:"question14"},{type:"Double",nodeId:"question15"},{type:"Long",nodeId:"question16"},{type:"Date",nodeId:"question17"},{type:"Time",nodeId:"question18"},{type:"DateTime",nodeId:"question19"},{type:"DataBindOnly",nodeId:"question20",inputs:{calculateAttr:1,constraintAttr:0,requiredAttr:0,}},{type:"DataBindOnly",nodeId:"question32",attrs:{calculateAttr:"1 + 2"},inputs:{calculateAttr:1,constraintAttr:0,requiredAttr:0,relevantAttr:1}},{clickBeforeAdd:"question19",type:"Repeat",nodeId:"question22"},{type:"FieldList",path:"question22/",nodeId:"question23"},{type:"Group",path:"question22/question23/",nodeId:"question40"},{clickBeforeAdd:"question22/question23",type:"Repeat",path:"question22/question23/",nodeId:"question41"},{clickBeforeAdd:"question22/question23",type:"Image",path:"question22/question23/",nodeId:"question24"},{type:"Audio",path:"question22/question23/",nodeId:"question25"},{type:"Video",path:"question22/question23/",nodeId:"question26"},{type:"Geopoint",path:"question22/question23/",nodeId:"question27"},{type:"Secret",path:"question22/question23/",nodeId:"question28"},{type:"Signature",path:"question22/question23/",nodeId:"question29"},{type:"AndroidIntent",path:"question22/question23/",nodeId:"question7"},{clickBeforeAdd:"question19",type:"Group",nodeId:"question21"},{type:"Repeat",path:"question21/",nodeId:"question31",attrs:{repeat_count:2}}];describe("Vellum",function(){describe("on load XML",function(){before(function(k){f.init({core:{form:g,onReady:function(){k()}}})});it("preserves all question types and attributes",function(){f.assertXmlEqual(f.call("createXML"),g)});h.each(a,function(l,k){var m=l.nodeId;describe("with "+l.type+"["+m+"]",function(){before(function(n){if(k>0){i((l.path||"")+m)}n()});it("should be selected when clicked",function(){b.equal(j("getCurrentlySelectedMug").p.nodeID,m)});h.each(l.attrs||{},function(o,n){it("should show 1 input for "+n,function(){f.assertInputCount(n,1,m)})});h.each(l.inputs||{},function(o,n){if(l.attrs&&l.attrs.hasOwnProperty(n)){b.equal(o,1,"test configuration conflict for "+n)}else{it("should show "+o+" inputs for "+n,function(){f.assertInputCount(n,o,m)})}})})})});it("adds all question types and attributes",function(k){this.timeout(10000);f.init({core:{form:null,onReady:function(){h.each(a,function(o,m){var n=(m>0?a[m-1]:{}),p=o.clickBeforeAdd||(n.nodeId?(n.path||"")+n.nodeId:null);c.call({prevId:p},o.type,o.nodeId,o.attrs)});function l(){d(".btn:contains(image)").click();d(".btn:contains(audio)").click();d(".btn:contains(video)").click();d(".btn:contains(long)").click();d(".btn:contains(short)").click();d(".btn:contains(custom)").click();d(".fd-modal-generic-container").find("input").val("custom");d(".fd-modal-generic-container").find(".btn:contains(Add)").click()}i("question1");l();d("[name='itext-en-label']").val("question1 en label").change();d("[name='itext-hin-label']").val("question1 hin label").change();d("[name='itext-en-constraintMsg']").val("question1 en validation").change();d("[name='itext-hin-constraintMsg']").val("question1 hin validation").change();d("[name='itext-en-hint']").val("question1 en hint").change();d("[name='itext-hin-hint']").val("question1 hin hint").change();d("[name='itext-en-help']").val("question1 en help").change();d("[name='itext-hin-help']").val("question1 hin help").change();d("[name='itext-en-label-long']").val("question1 en long").change();d("[name='itext-hin-label-long']").val("question1 hin long").change();d("[name='itext-en-label-short']").val("question1 en short").change();d("[name='itext-hin-label-short']").val("question1 hin short").change();d("[name='itext-en-label-custom']").val("question1 en custom").change();d("[name='itext-hin-label-custom']").val("question1 hin custom").change();i("question3/item1");l();d("[name='itext-en-label-long']").val("item1 long en").change();d("[name='itext-hin-label-long']").val("item1 long hin").change();d("[name='itext-en-label-short']").val("item1 short en").change();d("[name='itext-hin-label-short']").val("item1 short hin").change();d("[name='itext-en-label-custom']").val("item1 custom en").change();d("[name='itext-hin-label-custom']").val("item1 custom hin").change();i("question22/question23/question7");d("[name='property-androidIntentAppId']").val("app_id").change();d("[name='property-androidIntentExtra'] .fd-kv-key").val("key1").change();d("[name='property-androidIntentExtra'] .fd-kv-val").val("value1").change();d("[name='property-androidIntentResponse'] .fd-kv-key").val("key2").change();d("[name='property-androidIntentResponse'] .fd-kv-val").val("value2").change();f.assertXmlEqual(j("createXML"),g.replace(' foo="bar"',"").replace(' spam="eggs"',"").replace(' foo="baz"',"").replace(/<unrecognized>[\s\S]+<\/unrecognized>/,"").replace("non-itext label","").replace("non-itext hint","").replace(/<instance[^>]+?casedb[^>]+?><\/instance>/,"").replace(/<setvalue[^>]+?>/,""),{normalize_xmlns:true});i("question1");k()}}})});describe("can",function(){var l=[["Text","Trigger"],["Trigger","Select"],["Image","Select"],["Audio","Select"],["Video","Select"],["Image","Audio"],["PhoneNumber","Text"],["Select","Text"],["MSelect","Text"],["Select","MSelect"],["MSelect","Select"],["Select + Choices","MSelect"],["MSelect + Choices","Select"]],m=[["MSelect + Choices","Text"],["Select + Choices","Text"]];before(function(n){f.init({core:{onReady:function(){n()}}})});function k(r,q){var p=r.indexOf(" + Choices")>-1;r=(p?r.replace(" + Choices",""):r);var o=(r+(p?"_Choices":"")+"_to_"+q),n=c(r,o);if(!p&&r.indexOf("Select")>-1){f.deleteQuestion(o+"/item1");f.deleteQuestion(o+"/item2")}b.equal(n.p.nodeID,o,"got wrong mug before changing type");b.equal(n.__className,r,"wrong mug type");return n}h.each(l,function(p){var o=p[0],n=p[1];it("change "+o+" to "+n,function(){var q=k(o,n);j("changeMugType",q,n);q=f.getMug(q.p.nodeID);b.equal(q.__className,n);j("loadXML",j("createXML"));q=f.getMug(q.p.nodeID);b.equal(q.__className,n)})});h.each(m,function(p){var o=p[0],n=p[1];it("not change "+o+" to "+n,function(){var q=k(o,n),s=true;try{j("changeMugType",q,n);s=false}catch(r){b(String(r).indexOf("Cannot change")>0,String(r))}b(s,"Error not raised when changing "+o+" to "+n);q=f.getMug(q.p.nodeID);b.equal(q.__className,o.replace(" + Choices",""))})})});it("question type change survives save + load",function(){f.loadXML("");c("Text","question");var k=j("getMugByPath","/data/question");j("changeMugType",k,"Trigger");f.saveAndReload(function(){k=j("getMugByPath","/data/question");b.equal(k.__className,"Trigger")})});it("should allow user to view longs but not add them",function(){f.loadXML("");f.addQuestion("Long","long");f.addQuestion("Int","int");f.assertJSTreeState("long","int");var l=d(".fd-question-changer");l.children("a").click();b.equal(l.find("[data-qtype='Text']").length,1);b.equal(l.find("[data-qtype='Long']").length,0);var k=d(".fd-container-question-type-group");b.equal(k.find("[data-qtype='Text']:not(.btn)").length,1);b.equal(k.find("[data-qtype='Long']").length,0)});it("prevents changing selects with children to non-selects",function(){f.loadXML("");f.addQuestion("Select","question1");var l=".fd-question-changer";d(l+" > a").click();var k=d(l+" .change-question");b.equal(k.length,1);b.equal(k.length,k.filter("[data-qtype*='Select']").length);f.deleteQuestion("question1/item1");f.deleteQuestion("question1/item2");f.clickQuestion("question1");b.ok(d(l+" .change-question:not([data-qtype*='Select'])").length>0)});it("should show error on delete validation condition but not message",function(){f.loadXML("");var k=f.addQuestion("Text","text");k.p.constraintAttr="a = b";k.p.constraintMsgItext.set("A != B");k.p.constraintAttr="";b(!f.isTreeNodeValid(k),"question should not be valid")})})});