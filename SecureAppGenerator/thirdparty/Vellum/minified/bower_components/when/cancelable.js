(function(a){a(function(c){var b=c("./when");return function(d,f){var e=b.defer();d.cancel=function(){return d.reject(f(d))};d.promise.then(e.resolve,e.reject,e.notify);d.promise=e.promise;return d}})})(typeof define==="function"&&define.amd?define:function(a){module.exports=a(require)});