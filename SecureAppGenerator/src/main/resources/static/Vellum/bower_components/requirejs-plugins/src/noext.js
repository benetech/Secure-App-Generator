define(function(){var a="noext";return{load:function(c,e,d,b){e([e.toUrl(c)],function(f){d(f)})},normalize:function(b,c){b+=(b.indexOf("?")<0)?"?":"&";return b+a+"=1"}}});