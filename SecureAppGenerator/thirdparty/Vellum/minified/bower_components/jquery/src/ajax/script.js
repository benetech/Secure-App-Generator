define(["../core","../ajax"],function(a){a.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/(?:java|ecma)script/},converters:{"text script":function(b){a.globalEval(b);return b}}});a.ajaxPrefilter("script",function(b){if(b.cache===undefined){b.cache=false}if(b.crossDomain){b.type="GET";b.global=false}});a.ajaxTransport("script",function(d){if(d.crossDomain){var b,c=document.head||a("head")[0]||document.documentElement;return{send:function(e,f){b=document.createElement("script");b.async=true;if(d.scriptCharset){b.charset=d.scriptCharset}b.src=d.url;b.onload=b.onreadystatechange=function(h,g){if(g||!b.readyState||/loaded|complete/.test(b.readyState)){b.onload=b.onreadystatechange=null;if(b.parentNode){b.parentNode.removeChild(b)}b=null;if(!g){f(200,"success")}}};c.insertBefore(b,c.firstChild)},abort:function(){if(b){b.onload(undefined,true)}}}}})});