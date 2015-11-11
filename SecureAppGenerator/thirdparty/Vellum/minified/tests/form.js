define(["tests/utils","chai","jquery","underscore","vellum/form","vellum/tree","text!static/form/alternate-root-node-name.xml","text!static/form/question-referencing-other.xml","text!static/form/group-with-internal-refs.xml","text!static/form/hidden-value-in-group.xml","text!static/form/nested-groups.xml","text!static/form/select-questions.xml","text!static/form/mismatch-tree-order.xml","text!static/form/hidden-value-tree-order.xml"],function(j,i,h,n,o,f,c,m,e,l,g,k,b,d){var a=i.assert,p=j.call;describe("The form component",function(){before(function(r){j.init({javaRosa:{langs:["en"]},core:{onReady:r}})});it("should get and fix serialization errors for mugs with matching paths",function(){var t=j.loadXML(""),s=j.addQuestion("Text","question"),r=j.addQuestion("Text","question");a.notEqual(s.absolutePath,r.absolutePath);var u=t.getSerializationWarnings();a.equal(u.length,1,"missing serialization error message");a.equal(u[0].mug.ufid,r.ufid);t.fixSerializationWarnings(u);a.equal(s.p.nodeID,"question");a.notEqual(s.absolutePath,r.absolutePath);u=t.getSerializationWarnings();a.deepEqual(u,[],JSON.stringify(u))});it("should retain expression meaning on rename matching path",function(){var r=j.addQuestion("Text","blue"),s=j.addQuestion("Text","green"),t=j.addQuestion("DataBindOnly","black");t.p.calculateAttr="/data/blue + /data/green";s.p.nodeID="blue";a.notEqual(s.p.nodeID,"blue");a(!j.isTreeNodeValid(s),"expected validation error");r.p.nodeID="orange";a.equal(s.p.nodeID,"blue");a.equal(t.p.calculateAttr,"/data/orange + /data/blue");a(j.isTreeNodeValid(r),r.getErrors().join("\n"));a(j.isTreeNodeValid(s),s.getErrors().join("\n"));a(j.isTreeNodeValid(t),t.getErrors().join("\n"))});it("should retain conflicted mug ID on move",function(){var s=j.loadXML(""),r=j.addQuestion("DataBindOnly","hid"),u=j.addQuestion("Text","text"),t=j.addQuestion("Group","group");j.addQuestion("Text","text");r.p.calculateAttr="/data/text + /data/group/text";s.moveMug(u,"into",t);a.notEqual(r.p.calculateAttr,"/data/text + /data/group/text");a.notEqual(u.p.nodeID,"text");a(!j.isTreeNodeValid(u),"expected /data/text error");s.moveMug(u,"into",null);a.equal(u.p.nodeID,"text");a.equal(r.p.calculateAttr,"/data/text + /data/group/text");a(j.isTreeNodeValid(u),u.getErrors().join("\n"))});it("should show warnings for broken references on delete mug",function(){j.loadXML(m);var r=p("getMugByPath","/data/blue"),s=p("getMugByPath","/data/green"),t=p("getMugByPath","/data/black");a(j.isTreeNodeValid(s),"sanity check failed: green is invalid");a(j.isTreeNodeValid(t),"sanity check failed: black is invalid");j.clickQuestion("blue");r.form.removeMugsFromForm([r]);a(j.isTreeNodeValid(s),"green should be valid");a(!j.isTreeNodeValid(t),"black should not be valid")});it("should remove warnings when broken reference is fixed",function(){j.loadXML(m);var r=p("getMugByPath","/data/blue"),s=p("getMugByPath","/data/black");r.form.removeMugsFromForm([r]);a(!j.isTreeNodeValid(s),"black should not be valid");r=j.addQuestion("Text","blue");a(j.isTreeNodeValid(s),j.getMessages(s))});it("should show duplicate question ID warning inline",function(){j.loadXML("");j.addQuestion("Text","text");var t=j.addQuestion("Text","text"),r=t.messages.get("nodeID");a.equal(r.length,1,r.join("\n"));var u=h("[name=property-nodeID]").closest(".control-group"),s=u.find(".messages").children();a.equal(s.length,r.length,s.text());a.equal(s[0].text,r[0].message)});it("should warn about top-level question named 'case'",function(){j.loadXML("");var r=j.addQuestion("Text","case");a(r.messages.get("nodeID","mug-nodeID-case-warning"),"mug-nodeID-case-warning was expected but not present");r.p.nodeID="the-case";a.equal(j.getMessages(r),"")});it("should not warn about question named 'case' in group",function(){j.loadXML("");j.addQuestion("Group","group");var r=j.addQuestion("Text","case");a.equal(j.getMessages(r),"")});it("should add ODK warning to mug on create Audio question",function(){j.loadXML("");var r=j.addQuestion("Audio"),s=j.getMessages(r);i.expect(s).to.include("Android")});it("should preserve internal references in copied group",function(){j.loadXML(e);var s=p("getData").core.form,t=j.getMug("group");s.duplicateMug(t);var r=j.getMug("copy-1-of-group/green");a.equal(r.p.relevantAttr,"/data/copy-1-of-group/blue = 'red' and /data/red = 'blue'")});it("should set non-standard form root node",function(){j.loadXML(c);var s=p("getData").core.form,r=p("getMugByPath","/other/blue");a.equal(s.getBasePath(),"/other/");a(r!==null,"mug not found: /other/blue")});it("should be able to move item from Select to MSelect",function(){j.loadXML(k);var t=p("getData").core.form,s=j.getMug("question1/item1"),r=j.getMug("question2/item2");t.moveMug(s,"before",r)});it("should update reference to hidden value in group",function(){j.loadXML(l);var t=p("getMugByPath","/data/group"),r=p("getMugByPath","/data/group/label"),s=p("getMugByPath","/data/group/hidden");i.expect(r.p.relevantAttr).to.include("/data/group/hidden");t.p.nodeID="x";a.equal(t.absolutePath,"/data/x");a.equal(r.absolutePath,"/data/x/label");a.equal(s.absolutePath,"/data/x/hidden");i.expect(r.p.relevantAttr).to.include("/data/x/hidden")});it("should update reference to moved hidden value in output tag",function(){j.loadXML(l);var s=p("getData").core.form,r=p("getMugByPath","/data/group/label"),t=p("getMugByPath","/data/group/hidden");i.expect(r.p.relevantAttr).to.include("/data/group/hidden");i.expect(r.p.labelItext.defaultValue()).to.include("/data/group/hidden");s.moveMug(t,"first",null);a.equal(t.absolutePath,"/data/hidden");i.expect(r.p.relevantAttr).to.include("/data/hidden");i.expect(r.p.labelItext.defaultValue()).to.include("/data/hidden")});it("should update repeat group reference",function(){j.loadXML("");var s=j.addQuestion("Text","text"),r=j.addQuestion("Repeat","repeat");r.p.repeat_count="/data/text";a.equal(r.p.repeat_count,"/data/text");s.p.nodeID="text2";a.equal(r.p.repeat_count,"/data/text2")});it("should show warnings for duplicate choice value",function(){j.loadXML("");var r=j.addQuestion("Select","select"),t=r.form.getChildren(r)[0],s=r.form.getChildren(r)[1];a(j.isTreeNodeValid(t),t.getErrors().join("\n"));a(j.isTreeNodeValid(s),s.getErrors().join("\n"));s.p.nodeID="item1";a(j.isTreeNodeValid(t),"item1 should be valid");a(!j.isTreeNodeValid(s),"item2 should be invalid")});it("should preserve order of the control tree",function(){j.loadXML(b);j.assertJSTreeState("question1","question4","question2","  question3","question5","question6")});it("should merge data-only-nodes with control nodes",function(){j.loadXML(d);j.assertJSTreeState("question1","question5","question2","  question3","question6","question4")});it("should delete nested groups",function(){var r=j.loadXML(g),s=j.clickQuestion("group1","group1/group2");r.removeMugsFromForm(s);j.assertJSTreeState("")});it("should delete nested groups v2",function(){var r=j.loadXML(g),s=j.clickQuestion("group1","group1/group2/group3");r.removeMugsFromForm(s);j.assertJSTreeState("")});function q(x,w,v,u){var s=n.isString(w)?w:w.createXML(),t=h(s),r=t.find("model > instance[id='"+x+"']").attr("src");a.equal(r,v,u?u+"\n"+s:"")}it("should not drop referenced instance on delete dynamic select",function(){var r=j.loadXML("");j.paste([["id","type","labelItext:en-default","calculateAttr","itemsetData"],["/hidden","DataBindOnly","null","instance('some-fixture')/some-fixture_list/some-fixture/@id","null"],["/select","SelectDynamic","select","null",'[{"instance":{"id":"some-fixture","src":"jr://fixture/item-list:some-fixture"},"nodeset":"instance(\'some-fixture\')/some-fixture_list/some-fixture","labelRef":"name","valueRef":"@id"}]']]);j.deleteQuestion("select");q("some-fixture",r,"jr://fixture/item-list:some-fixture","some-fixture instance not found")});it("should drop instance on delete last reference",function(){var r=j.loadXML("");j.paste([["id","type","calculateAttr","instances"],["/hidden","DataBindOnly","instance('some-fixture')/some-fixture_list/some-fixture/@id",'{"some-fixture":"jr://fixture/item-list:some-fixture"}'],]);q("some-fixture",r,"jr://fixture/item-list:some-fixture");j.deleteQuestion("hidden");q("some-fixture",r,undefined,"some-fixture instance not found")});it("should maintain instance on delete and re-add last reference",function(){var s=j.loadXML("");j.paste([["id","type","calculateAttr","instances"],["/hidden","DataBindOnly","instance('some-fixture')/some-fixture_list/some-fixture/@id",'{"some-fixture":"jr://fixture/item-list:some-fixture"}'],]);j.deleteQuestion("hidden");q("some-fixture",s,undefined);var r=j.addQuestion("DataBindOnly","hid");r.p.calculateAttr="instance('some-fixture')/some-fixture_list/some-fixture/@id";q("some-fixture",s,"jr://fixture/item-list:some-fixture","some-fixture instance not found")});describe("instance tracker",function(){var s,r,t="calculateAttr";before(function(){s=j.loadXML("");r=j.addQuestion("DataBindOnly","hid");t="calculateAttr";s.addInstanceIfNotExists({id:"old",src:"old://"},r,"relevantAttr");s.addInstanceIfNotExists({id:"blank"},r,"relevantAttr");s.updateKnownInstances({known:"known://"})});n.each([[{id:"new0",src:null},"new0",undefined],[{id:"new1",src:"new://1"},"new1","src"],[{id:"new2",src:"old://"},"old","src"],[{id:"old",src:null},"old","old://"],[{id:"old",src:"new://3"},"old-1","src"],[{id:"old",src:"old://"},"old","src"],[{id:"any",src:"old://"},"old","src"],[{id:"known",src:null},"known","known://"],[{id:"blank",src:"blank://"},"blank","src"],[{id:null,src:"new://5"},"data-1","src"],],function(x){var v=x[0],w=x[1],u=x[2]==="src"?v.src:x[2];it("should add instance "+JSON.stringify(v)+" -> "+w+": "+u,function(){q(w,s,w==="old"?u:undefined);var y=s.addInstanceIfNotExists(v,r,t);if(n.isRegExp(w)){i.expect(y).to.match(w)}else{a.equal(y,w)}q(w,s,u,w+" instance not found");s.dropAllInstanceReferences(r,t);if(w==="old"||w==="blank"){q(w,s,u,w+" instance should be removed")}else{q(w,s,undefined,w+" instance should be removed")}})})})})});