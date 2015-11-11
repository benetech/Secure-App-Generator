require(["tests/options","tests/utils","chai","jquery","underscore","vellum/datasources","vellum/itemset",],function(k,g,f,e,i,d,a){var b=f.assert,j=g.clickQuestion,c=i.union(g.options.options.plugins||[],["itemset"]),h=[{id:"some-fixture",uri:"jr://fixture/item-list:some-fixture",path:"/some-fixture_list/some-fixture",name:"some-fixture-name",structure:{"inner-attribute":{structure:{"extra-inner-attribute":{structure:{"@id":{},name:{}}}}},"@id":{},name:{}}}];describe("The data source widget",function(){function l(m){g.init({plugins:c,javaRosa:{langs:["en"]},core:{dataSourcesEndpoint:function(n){n(h)},onReady:m}})}before(l);it("displays nested structures",function(){g.loadXML("");g.addQuestion("SelectDynamic","select1");j("select1/itemset");var m=e("[name=property-itemsetData] option"),n=function(o){return o.text};b.equal(i.map(m,n).join("\n"),["some-fixture-name","some-fixture-name - inner-attribute","some-fixture-name - inner-attribute - extra-inner-attribute",].join("\n"))});describe("",function(){before(function(m){g.init({plugins:c,javaRosa:{langs:["en"]},core:{dataSourcesEndpoint:function(n){n([])},onReady:m}})});it("should not crash when no fixtures are passed",function(){g.loadXML("");g.addQuestion("SelectDynamic","select1");j("select1/itemset");b(true)})});describe("async options loader",function(){var m;before(function(n){g.init({plugins:c,javaRosa:{langs:["en"]},core:{dataSourcesEndpoint:function(o){m=o},onReady:n}})});beforeEach(function(){d.reset();m=null});it("should indicate loading status for empty itemset",function(){g.loadXML("");g.addQuestion("SelectDynamic","select");j("select/itemset");var n=e("[name=property-itemsetData] option");b.equal(n.first().text(),"Loading...");b.equal(n.length,1,e("<div />").append(n).html())});it("should replace loading indicator with async loaded options",function(){g.loadXML("");g.addQuestion("SelectDynamic","select");j("select/itemset");m([]);var n=e("[name=property-itemsetData] option");b.equal(n.first().text(),"Not Found");b.equal(n.length,1,e("<div />").append(n).html())});it("should show custom option when loading itemset with value",function(){g.loadXML("");g.paste([["id","type","itemsetData"],["select","SelectDynamic",'[{"instance":null,"nodeset":"/items","labelRef":"@name","valueRef":"@id"}]'],]);j("select/itemset");var n=e("[name=property-itemsetData] option"),o=e(n[1]);b.equal(n.first().text(),"Loading...");b.equal(o.text(),"Lookup table was not found in the project");b.equal(o.parent().val(),'{"id":"","src":"","query":"/items"}');b.equal(n.length,2,e("<div />").append(n).html())});it("should select first option when empty and finished loading",function(){g.loadXML("");g.addQuestion("SelectDynamic","select");j("select/itemset");m([{id:"bar",uri:"jr://fixture/foo",path:"root",name:"outer",structure:{"@id":{},name:{},inner:{structure:{"@id":{},name:{},},},},}]);var n=e("[name=property-itemsetData]");b.equal(n.val(),'{"id":"bar","src":"jr://fixture/foo","query":"instance(\'bar\')root"}');b.equal(n.find("option:selected").text(),"outer");b.equal(e("[name=value_ref]").val(),"@id");b.equal(e("[name=label_ref]").val(),"name")});it("should select correct option when not empty and finished loading",function(){g.loadXML("");g.paste([["id","type","itemsetData"],["select","SelectDynamic",'[{"instance":{"id":"bar","src":"jr://fixture/foo","query":"instance(\'bar\')root/inner"},"nodeset":"instance(\'bar\')root/inner","labelRef":"name","valueRef":"@id"}]'],]);j("select/itemset");m([{id:"bar",uri:"jr://fixture/foo",path:"root",name:"outer",structure:{"@id":{},name:{},inner:{structure:{"@id":{},name:{},},},},}]);var n=e("[name=property-itemsetData]");b.equal(n.val(),'{"id":"bar","src":"jr://fixture/foo","query":"instance(\'bar\')root/inner"}');b.equal(n.find("option:selected").text(),"outer - inner")})})})});