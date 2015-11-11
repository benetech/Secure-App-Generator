require(["tests/options","tests/utils","chai","jquery","underscore","vellum/form","text!static/markdown/markdown-test.xml","text!static/markdown/simple-markdown.xml","text!static/markdown/simple-markdown-no-chars.xml","text!static/markdown/no-markdown.xml","text!static/markdown/no-markdown-stars.xml"],function(o,h,g,f,k,c,d,b,i,j,l){var a=g.assert,n=h.call;function m(){return f(".itext-block-label-group-default").find(".markdown-output").is(":visible")}function e(){f(".markdown-trigger").first().click()}describe("The markdown widget",function(){function p(q){h.init({javaRosa:{langs:["en"]},core:{onReady:q}})}before(p);it("should parse form that has markdown",function(){h.loadXML(d);var q=h.getMug("/data/markdown_question");a(q.p.labelItext.hasMarkdown)});it("should use the markdown form when there are conflicting strings",function(){h.loadXML(d);var q=h.getMug("/data/markdown_question");a.strictEqual(q.p.labelItext.get(),"**some markdown**")});describe("when a user has not defined markdown usage",function(){it("should not show markdown with nothing in the text",function(){h.loadXML("");h.addQuestion("Text","markdown_question");a(!m())});it("should allow turning off markdown if markdown characters are input",function(){h.loadXML("");h.addQuestion("Text","markdown_question");f("[name=itext-en-label]").val("**markdown**").change();a(m());f(".markdown-trigger").first().click();a(!m())});it("should not show markdown if non markdown characters are not input",function(){h.loadXML("");h.addQuestion("Text","markdown_question");a(!m());f("[name=itext-en-label]").val("no markdown here").change();a(!m())});it("should write any markdown if markdown characters are input",function(){h.loadXML("");h.addQuestion("Text","markdown_question");f("[name=itext-en-label]").val("**some markdown**").change();h.assertXmlEqual(n("createXML"),b,{normalize_xmlns:true})});it("should not write any markdown if markdown characters are not input",function(){h.loadXML("");h.addQuestion("Text","markdown_question");f("[name=itext-en-label]").val("no markdown").change();h.assertXmlEqual(n("createXML"),j,{normalize_xmlns:true})})});describe("when a user explicitly wants no markdown",function(){beforeEach(function(q){h.loadXML("");h.addQuestion("Text","markdown_question");f("[name=itext-en-label]").val("**no markdown**").change();e();q()});it("should not show markdown when there are markdown characters",function(){a(!m());f("[name=itext-en-label]").val("~~more markdown~~").change();a(!m())});it("should allow re-enabling markdown",function(){e();a(m())});it("should not write any markdown",function(){h.assertXmlEqual(n("createXML"),l,{normalize_xmlns:true})})});describe("when a user explicitly wants markdown",function(){function q(r){h.loadXML("");h.addQuestion("Text","markdown_question");f("[name=itext-en-label]").val("**some markdown**").change();e();e();r()}beforeEach(q);it("should show markdown when there are no markdown characters",function(){f("[name=itext-en-label]").val("some markdown").change();a(m())});it("should allow turning off markdown",function(){a(m());e();a(!m())});it("should write markdown even if there are no markdown characters",function(){f("[name=itext-en-label]").val("some markdown").change();h.assertXmlEqual(n("createXML"),i,{normalize_xmlns:true})})})})});