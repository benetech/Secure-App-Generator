(function(a){a(function(g){G.promise=H;G.resolve=t;G.reject=x;G.defer=j;G.join=l;G.all=s;G.map=z;G.reduce=o;G.settle=B;G.any=C;G.some=I;G.isPromise=c;G.isPromiseLike=c;function G(W,Z,X,Y){return F(W).then(Z,X,Y)}function H(W){return new E(W,S.PromiseStatus&&S.PromiseStatus())}function E(W,Z){var ag,ac,aa=[];ag=this;this._status=Z;this.inspect=ae;this._when=Y;try{W(X,af,ad)}catch(ab){af(ab)}function ae(){return ac?ac.inspect():w()}function Y(aj,ai,al,ah,ak){aa?aa.push(am):k(function(){am(ac)});function am(an){an._when(aj,ai,al,ah,ak)}}function X(ai){if(!aa){return}var ah=aa;aa=y;ac=D(ag,ai);k(function(){if(Z){u(ac,Z)}v(ah,ac)})}function af(ah){X(new r(ah))}function ad(ai){if(aa){var ah=aa;k(function(){v(ah,new f(ai))})}}}m=E.prototype;m.then=function(Z,X,Y){var W=this;return new E(function(ac,ab,aa){W._when(ac,aa,Z,X,Y)},this._status&&this._status.observed())};m["catch"]=m.otherwise=function(W){return this.then(y,W)};m["finally"]=m.ensure=function(X){return typeof X==="function"?this.then(W,W)["yield"](this):this;function W(){return t(X())}};m.done=function(W,X){this.then(W,X)["catch"](P)};m.yield=function(W){return this.then(function(){return W})};m.tap=function(W){return this.then(W)["yield"](this)};m.spread=function(W){return this.then(function(X){return s(X,function(Y){return W.apply(y,Y)})})};m.always=function(W,X){return this.then(W,W,X)};function F(W){return W instanceof E?W:t(W)}function t(W){return H(function(X){X(W)})}function x(W){return G(W,function(X){return new r(X)})}function j(){var Y,Z,X;Y={promise:y,resolve:y,reject:y,notify:y,resolver:{resolve:y,reject:y,notify:y}};Y.promise=Z=H(W);return Y;function W(aa,ab,ac){Y.resolve=Y.resolver.resolve=function(ad){if(X){return t(ad)}X=true;aa(ad);return Z};Y.reject=Y.resolver.reject=function(ad){if(X){return t(new r(ad))}X=true;ab(ad);return Z};Y.notify=Y.resolver.notify=function(ad){ac(ad);return ad}}}function v(W,Y){for(var X=0;X<W.length;X++){W[X](Y)}}function D(Y,X){if(X===Y){return new r(new TypeError())}if(X instanceof E){return X}try{var W=X===Object(X)&&X.then;return typeof W==="function"?h(W,X):new K(X)}catch(Z){return new r(Z)}}function h(X,W){return H(function(Z,Y){k(function(){try{i(X,W,Z,Y)}catch(aa){Y(aa)}})})}N=Object.create||function(X){function W(){}W.prototype=X;return new W()};function K(W){this.value=W}K.prototype=N(m);K.prototype.inspect=function(){return e(this.value)};K.prototype._when=function(X,W,Z){try{X(typeof Z==="function"?Z(this.value):this.value)}catch(Y){X(new r(Y))}};function r(W){this.value=W}r.prototype=N(m);r.prototype.inspect=function(){return U(this.value)};r.prototype._when=function(Z,X,Y,W){try{Z(typeof W==="function"?W(this.value):this)}catch(aa){Z(new r(aa))}};function f(W){this.value=W}f.prototype=N(m);f.prototype._when=function(X,Y,aa,Z,W){try{Y(typeof W==="function"?W(this.value):this.value)}catch(ab){Y(ab)}};function u(Y,X){Y.then(W,Z);function W(){X.fulfilled()}function Z(aa){X.rejected(aa)}}function c(W){return W&&typeof W.then==="function"}function I(Y,X,aa,W,Z){return G(Y,function(ab){return H(ac).then(aa,W,Z);function ac(an,ap,ao){var al,ak,am,ae,aj,ag,ai,ah;ai=ab.length>>>0;al=Math.max(0,Math.min(X,ai));am=[];ak=(ai-al)+1;ae=[];if(!al){an(am)}else{ag=function(aq){ae.push(aq);if(!--ak){aj=ag=M;ap(ae)}};aj=function(aq){am.push(aq);if(!--al){aj=ag=M;an(am)}};for(ah=0;ah<ai;++ah){if(ah in ab){G(ab[ah],ad,af,ao)}}}function af(aq){ag(aq)}function ad(aq){aj(aq)}}})}function C(X,aa,W,Y){function Z(ab){return aa?aa(ab[0]):ab[0]}return I(X,1,Z,W,Y)}function s(X,Z,W,Y){return A(X,M).then(Z,W,Y)}function l(){return A(arguments,M)}function B(W){return A(W,e,U)}function z(X,W){return A(X,W)}function A(Y,W,X){return G(Y,function(aa){return new E(Z);function Z(ai,ah,ag){var af,ac,ae,ad;ae=ac=aa.length>>>0;af=[];if(!ae){ai(af);return}for(ad=0;ad<ac;ad++){if(ad in aa){ab(aa[ad],ad)}else{--ae}}function ab(ak,aj){G(ak,W,X).then(function(al){af[aj]=al;if(!--ae){ai(af)}},ah,ag)}}})}function o(Y,X){var W=i(p,arguments,1);return G(Y,function(aa){var Z;Z=aa.length;W[0]=function(ac,ad,ab){return G(ac,function(ae){return G(ad,function(af){return X(ae,af,ab,Z)})})};return L.apply(aa,W)})}function e(W){return{state:"fulfilled",value:W}}function U(W){return{state:"rejected",reason:W}}function w(){return{state:"pending"}}var m,N,L,p,i,T,n,V,b,q,S,O,J,Q,y;J=g;n=[];function k(W){if(n.push(W)===1){T(R)}}function R(){v(n);n=[]}S=typeof console!=="undefined"?console:G;if(typeof process==="object"&&process.nextTick){T=process.nextTick}else{if(Q=(typeof MutationObserver==="function"&&MutationObserver)||(typeof WebKitMutationObserver==="function"&&WebKitMutationObserver)){T=(function(W,Y,Z){var X=W.createElement("div");new Y(Z).observe(X,{attributes:true});return function(){X.setAttribute("x","x")}}(document,Q,R))}else{try{T=J("vertx").runOnLoop||J("vertx").runOnContext}catch(d){O=setTimeout;T=function(W){O(W,0)}}}}V=Function.prototype;b=V.call;i=V.bind?b.bind(b):function(X,W){return X.apply(W,p.call(arguments,2))};q=[];p=q.slice;L=q.reduce||function(ab){var X,Z,Y,W,aa;aa=0;X=Object(this);W=X.length>>>0;Z=arguments;if(Z.length<=1){for(;;){if(aa in X){Y=X[aa++];break}if(++aa>=W){throw new TypeError()}}}else{Y=Z[1]}for(;aa<W;++aa){if(aa in X){Y=ab(Y,X[aa],aa,X)}}return Y};function M(W){return W}function P(W){if(typeof S.reportUnhandled==="function"){S.reportUnhandled()}else{k(function(){throw W})}throw W}return G})})(typeof define==="function"&&define.amd?define:function(a){module.exports=a(require)});