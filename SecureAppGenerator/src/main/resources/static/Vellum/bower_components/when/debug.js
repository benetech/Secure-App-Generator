(function(a){a(function(j){var i,q,g,c,r,y,A,d;i=j("./when");q=0;g={};r=Object.prototype.hasOwnProperty;y=(typeof console!=="undefined"&&typeof console.warn==="function")?function(p){console.warn(p)}:function(){};A=typeof vertx==="object"?function(B,p){return vertx.setTimer(p,B)}:setTimeout;c={RangeError:1,ReferenceError:1,SyntaxError:1,TypeError:1};y("when/debug is deprecated and will be removed. Use when/monitor/console instead");function m(E,p,D,C){var B=[E].concat(w(E,[p,D,C]));return s(i.apply(null,B),i.resolve(E))}function s(E,C){var G,F,D,B;if(r.call(E,"parent")){return E}q++;G=(C&&"id" in C)?(C.id+"."+q):q;F=E.then;D=k(E);D.id=G;D.parent=C;D.toString=function(){return v("Promise",G)};D.then=function(p,I,H){l(p,I,H);if(typeof I==="function"){var J=D;do{J.handled=true}while((J=J.parent)&&!J.handled)}return s(F.apply(E,w(D,arguments)),D)};B=function(){console.error(D.toString())};E.then(function(p){D.toString=function(){return v("Promise",G,"resolved",p)};return p},b(D,function(p){D.toString=function(){return v("Promise",G,"REJECTED",p)};z("reject",D,p);if(!D.handled){B()}throw p}));return D}function h(){var I,C,F,J,D,p,L,K,E,B;I=i.defer();try{throw new Error("deferred.then was removed, use deferred.promise.then")}catch(G){C=G}I.then=function H(){o(C)};F="pending";J=g;B=arguments[arguments.length-1];if(B===d){B=++q}K=I.promise.then;I.id=B;I.promise=s(I.promise,I);E=I.promise.always;I.promise.always=x("promise.always","promise.ensure",E,I.promise);I.resolver=k(I.resolver);I.resolver.toString=function(){return v("Resolver",B,F,J)};L=I.resolver.notify;I.notify=I.resolver.notify=function(M){z("progress",I,M);return L(M)};D=I.resolver.resolve;I.resolve=I.resolver.resolve=function(M){J=M;F="resolving";z("resolve",I,M);return D.apply(d,arguments)};p=I.resolver.reject;I.reject=I.resolver.reject=function(M){J=M;F="REJECTING";return p.apply(d,arguments)};I.toString=function(){return v("Deferred",B,F,J)};K(function(M){F="resolved";return M},function(M){F="REJECTED";return i.reject(M)});I.resolver.id=B;return I}m.defer=h;m.isPromise=i.isPromise;f("all",i.all);f("any",i.any);f("some",i.some,2);for(var t in i){if(i.hasOwnProperty(t)&&!(t in m)){u(t,i[t])}}return m;function u(p,B){m[p]=function(){return s(B.apply(i,arguments))}}function f(p,B,C){u(p,function(){C=C||1;if(typeof arguments[C]==="function"||typeof arguments[C+1]==="function"||typeof arguments[C+2]==="function"){y(p+"() onFulfilled, onRejected, and onProgress are deprecated, use returnedPromise.then/otherwise/ensure instead")}return B.apply(i,arguments)})}function b(B,p){return function(C){try{return p(C)}catch(D){if(D){var E=(m.debug&&m.debug.exceptionsToRethrow)||c;if(D.name in E){o(D)}z("reject",B,D)}throw D}}}function w(F,E){var B,C,p,D;C=[];for(D=0,p=E.length;D<p;D++){C[D]=typeof(B=E[D])=="function"?b(F,B):B}return C}function z(E,G,C,B){var D=m.debug;if(!(D&&typeof D[E]==="function")){return}if(arguments.length<4&&E=="reject"){try{throw new Error(G.toString())}catch(F){B=F}}try{D[E](G,C,B)}catch(p){o(new Error("when.js global debug handler threw: "+String(p)))}}function v(B,E,p,D){var C="[object "+B+" "+E+"]";if(arguments.length>2){C+=" "+p;if(D!==g){C+=": "+D}}return C}function o(p){A(function(){throw p},0)}function x(p,D,C,B){return function(){y(new Error(p+" is deprecated, use "+D).stack);return C.apply(B,arguments)}}function l(){var C,p,B;for(C=0,p=arguments.length;C<p;C++){B=arguments[C];if(!e(B)){y(new Error("arg "+C+" must be a function, null, or undefined, but was a "+typeof B).stack)}}}function e(p){return typeof p==="function"||p==null}function n(){}function k(p){n.prototype=p;p=new n();n.prototype=d;return p}})})(typeof define==="function"&&define.amd?define:function(a){module.exports=a(require)});