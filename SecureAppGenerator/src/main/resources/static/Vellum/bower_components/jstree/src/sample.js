(function(a,c){var b=null;a.jstree.defaults.sample={sample_option:"sample_val"};a.jstree.plugins.sample=function(d,e){this.sample_function=function(f){if(e.sample_function){e.sample_function.call(this,f)}};this.init=function(g,f){e.init.call(this,g,f)};this.bind=function(){e.bind.call(this)};this.unbind=function(){e.unbind.call(this)};this.teardown=function(){e.teardown.call(this)};this.get_state=function(){var f=e.get_state.call(this);f.sample={"var":"val"};return f};this.set_state=function(f,g){if(e.set_state.call(this,f,g)){if(f.sample){delete f.sample;this.set_state(f,g);return false}return true}return false};this.get_json=function(l,g,m){var k=e.get_json.call(this,l,g,m),h,f;if(a.isArray(k)){for(h=0,f=k.length;h<f;h++){k[h].sample="value"}}else{k.sample="value"}return k}};a(function(){});a.jstree.defaults.plugins.push("sample")})(jQuery);