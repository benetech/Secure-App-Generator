(function(){var w=this;var k=w._;var D={};var C=Array.prototype,f=Object.prototype,r=Function.prototype;var G=C.push,o=C.slice,y=C.concat,d=f.toString,j=f.hasOwnProperty;var K=C.forEach,q=C.map,E=C.reduce,c=C.reduceRight,b=C.filter,B=C.every,p=C.some,n=C.indexOf,l=C.lastIndexOf,u=Array.isArray,e=Object.keys,F=r.bind;var L=function(M){if(M instanceof L){return M}if(!(this instanceof L)){return new L(M)}this._wrapped=M};if(typeof exports!=="undefined"){if(typeof module!=="undefined"&&module.exports){exports=module.exports=L}exports._=L}else{w._=L}L.VERSION="1.4.4";var H=L.each=L.forEach=function(R,Q,P){if(R==null){return}if(K&&R.forEach===K){R.forEach(Q,P)}else{if(R.length===+R.length){for(var O=0,M=R.length;O<M;O++){if(Q.call(P,R[O],O,R)===D){return}}}else{for(var N in R){if(L.has(R,N)){if(Q.call(P,R[N],N,R)===D){return}}}}}};L.map=L.collect=function(P,O,N){var M=[];if(P==null){return M}if(q&&P.map===q){return P.map(O,N)}H(P,function(S,Q,R){M[M.length]=O.call(N,S,Q,R)});return M};var g="Reduce of empty array with no initial value";L.reduce=L.foldl=L.inject=function(Q,P,M,O){var N=arguments.length>2;if(Q==null){Q=[]}if(E&&Q.reduce===E){if(O){P=L.bind(P,O)}return N?Q.reduce(P,M):Q.reduce(P)}H(Q,function(T,R,S){if(!N){M=T;N=true}else{M=P.call(O,M,T,R,S)}});if(!N){throw new TypeError(g)}return M};L.reduceRight=L.foldr=function(S,P,M,O){var N=arguments.length>2;if(S==null){S=[]}if(c&&S.reduceRight===c){if(O){P=L.bind(P,O)}return N?S.reduceRight(P,M):S.reduceRight(P)}var R=S.length;if(R!==+R){var Q=L.keys(S);R=Q.length}H(S,function(V,T,U){T=Q?Q[--R]:--R;if(!N){M=S[T];N=true}else{M=P.call(O,M,S[T],T,U)}});if(!N){throw new TypeError(g)}return M};L.find=L.detect=function(P,O,N){var M;A(P,function(S,Q,R){if(O.call(N,S,Q,R)){M=S;return true}});return M};L.filter=L.select=function(P,O,N){var M=[];if(P==null){return M}if(b&&P.filter===b){return P.filter(O,N)}H(P,function(S,Q,R){if(O.call(N,S,Q,R)){M[M.length]=S}});return M};L.reject=function(O,N,M){return L.filter(O,function(R,P,Q){return !N.call(M,R,P,Q)},M)};L.every=L.all=function(P,O,N){O||(O=L.identity);var M=true;if(P==null){return M}if(B&&P.every===B){return P.every(O,N)}H(P,function(S,Q,R){if(!(M=M&&O.call(N,S,Q,R))){return D}});return !!M};var A=L.some=L.any=function(P,O,N){O||(O=L.identity);var M=false;if(P==null){return M}if(p&&P.some===p){return P.some(O,N)}H(P,function(S,Q,R){if(M||(M=O.call(N,S,Q,R))){return D}});return !!M};L.contains=L.include=function(N,M){if(N==null){return false}if(n&&N.indexOf===n){return N.indexOf(M)!=-1}return A(N,function(O){return O===M})};L.invoke=function(O,P){var M=o.call(arguments,2);var N=L.isFunction(P);return L.map(O,function(Q){return(N?P:Q[P]).apply(Q,M)})};L.pluck=function(N,M){return L.map(N,function(O){return O[M]})};L.where=function(N,M,O){if(L.isEmpty(M)){return O?null:[]}return L[O?"find":"filter"](N,function(Q){for(var P in M){if(M[P]!==Q[P]){return false}}return true})};L.findWhere=function(N,M){return L.where(N,M,true)};L.max=function(P,O,N){if(!O&&L.isArray(P)&&P[0]===+P[0]&&P.length<65535){return Math.max.apply(Math,P)}if(!O&&L.isEmpty(P)){return -Infinity}var M={computed:-Infinity,value:-Infinity};H(P,function(T,Q,S){var R=O?O.call(N,T,Q,S):T;R>=M.computed&&(M={value:T,computed:R})});return M.value};L.min=function(P,O,N){if(!O&&L.isArray(P)&&P[0]===+P[0]&&P.length<65535){return Math.min.apply(Math,P)}if(!O&&L.isEmpty(P)){return Infinity}var M={computed:Infinity,value:Infinity};H(P,function(T,Q,S){var R=O?O.call(N,T,Q,S):T;R<M.computed&&(M={value:T,computed:R})});return M.value};L.shuffle=function(P){var O;var N=0;var M=[];H(P,function(Q){O=L.random(N++);M[N-1]=M[O];M[O]=Q});return M};var a=function(M){return L.isFunction(M)?M:function(N){return N[M]}};L.sortBy=function(P,O,M){var N=a(O);return L.pluck(L.map(P,function(S,Q,R){return{value:S,index:Q,criteria:N.call(M,S,Q,R)}}).sort(function(T,S){var R=T.criteria;var Q=S.criteria;if(R!==Q){if(R>Q||R===void 0){return 1}if(R<Q||Q===void 0){return -1}}return T.index<S.index?-1:1}),"value")};var t=function(R,Q,N,P){var M={};var O=a(Q||L.identity);H(R,function(U,S){var T=O.call(N,U,S,R);P(M,T,U)});return M};L.groupBy=function(O,N,M){return t(O,N,M,function(P,Q,R){(L.has(P,Q)?P[Q]:(P[Q]=[])).push(R)})};L.countBy=function(O,N,M){return t(O,N,M,function(P,Q){if(!L.has(P,Q)){P[Q]=0}P[Q]++})};L.sortedIndex=function(T,S,P,O){P=P==null?L.identity:a(P);var R=P.call(O,S);var M=0,Q=T.length;while(M<Q){var N=(M+Q)>>>1;P.call(O,T[N])<R?M=N+1:Q=N}return M};L.toArray=function(M){if(!M){return[]}if(L.isArray(M)){return o.call(M)}if(M.length===+M.length){return L.map(M,L.identity)}return L.values(M)};L.size=function(M){if(M==null){return 0}return(M.length===+M.length)?M.length:L.keys(M).length};L.first=L.head=L.take=function(O,N,M){if(O==null){return void 0}return(N!=null)&&!M?o.call(O,0,N):O[0]};L.initial=function(O,N,M){return o.call(O,0,O.length-((N==null)||M?1:N))};L.last=function(O,N,M){if(O==null){return void 0}if((N!=null)&&!M){return o.call(O,Math.max(O.length-N,0))}else{return O[O.length-1]}};L.rest=L.tail=L.drop=function(O,N,M){return o.call(O,(N==null)||M?1:N)};L.compact=function(M){return L.filter(M,L.identity)};var x=function(N,O,M){H(N,function(P){if(L.isArray(P)){O?G.apply(M,P):x(P,O,M)}else{M.push(P)}});return M};L.flatten=function(N,M){return x(N,M,[])};L.without=function(M){return L.difference(M,o.call(arguments,1))};L.uniq=L.unique=function(S,R,Q,P){if(L.isFunction(R)){P=Q;Q=R;R=false}var N=Q?L.map(S,Q,P):S;var O=[];var M=[];H(N,function(U,T){if(R?(!T||M[M.length-1]!==U):!L.contains(M,U)){M.push(U);O.push(S[T])}});return O};L.union=function(){return L.uniq(y.apply(C,arguments))};L.intersection=function(N){var M=o.call(arguments,1);return L.filter(L.uniq(N),function(O){return L.every(M,function(P){return L.indexOf(P,O)>=0})})};L.difference=function(N){var M=y.apply(C,o.call(arguments,1));return L.filter(N,function(O){return !L.contains(M,O)})};L.zip=function(){var M=o.call(arguments);var P=L.max(L.pluck(M,"length"));var O=new Array(P);for(var N=0;N<P;N++){O[N]=L.pluck(M,""+N)}return O};L.object=function(Q,O){if(Q==null){return{}}var M={};for(var P=0,N=Q.length;P<N;P++){if(O){M[Q[P]]=O[P]}else{M[Q[P][0]]=Q[P][1]}}return M};L.indexOf=function(Q,O,P){if(Q==null){return -1}var N=0,M=Q.length;if(P){if(typeof P=="number"){N=(P<0?Math.max(0,M+P):P)}else{N=L.sortedIndex(Q,O);return Q[N]===O?N:-1}}if(n&&Q.indexOf===n){return Q.indexOf(O,P)}for(;N<M;N++){if(Q[N]===O){return N}}return -1};L.lastIndexOf=function(Q,O,P){if(Q==null){return -1}var M=P!=null;if(l&&Q.lastIndexOf===l){return M?Q.lastIndexOf(O,P):Q.lastIndexOf(O)}var N=(M?P:Q.length);while(N--){if(Q[N]===O){return N}}return -1};L.range=function(R,P,Q){if(arguments.length<=1){P=R||0;R=0}Q=arguments[2]||1;var N=Math.max(Math.ceil((P-R)/Q),0);var M=0;var O=new Array(N);while(M<N){O[M++]=R;R+=Q}return O};L.bind=function(O,N){if(O.bind===F&&F){return F.apply(O,o.call(arguments,1))}var M=o.call(arguments,2);return function(){return O.apply(N,M.concat(o.call(arguments)))}};L.partial=function(N){var M=o.call(arguments,1);return function(){return N.apply(this,M.concat(o.call(arguments)))}};L.bindAll=function(N){var M=o.call(arguments,1);if(M.length===0){M=L.functions(N)}H(M,function(O){N[O]=L.bind(N[O],N)});return N};L.memoize=function(O,N){var M={};N||(N=L.identity);return function(){var P=N.apply(this,arguments);return L.has(M,P)?M[P]:(M[P]=O.apply(this,arguments))}};L.delay=function(N,O){var M=o.call(arguments,2);return setTimeout(function(){return N.apply(null,M)},O)};L.defer=function(M){return L.delay.apply(L,[M,1].concat(o.call(arguments,1)))};L.throttle=function(R,T){var P,O,S,M;var Q=0;var N=function(){Q=new Date;S=null;M=R.apply(P,O)};return function(){var U=new Date;var V=T-(U-Q);P=this;O=arguments;if(V<=0){clearTimeout(S);S=null;Q=U;M=R.apply(P,O)}else{if(!S){S=setTimeout(N,V)}}return M}};L.debounce=function(O,Q,N){var P,M;return function(){var U=this,T=arguments;var S=function(){P=null;if(!N){M=O.apply(U,T)}};var R=N&&!P;clearTimeout(P);P=setTimeout(S,Q);if(R){M=O.apply(U,T)}return M}};L.once=function(O){var M=false,N;return function(){if(M){return N}M=true;N=O.apply(this,arguments);O=null;return N}};L.wrap=function(M,N){return function(){var O=[M];G.apply(O,arguments);return N.apply(this,O)}};L.compose=function(){var M=arguments;return function(){var N=arguments;for(var O=M.length-1;O>=0;O--){N=[M[O].apply(this,N)]}return N[0]}};L.after=function(N,M){if(N<=0){return M()}return function(){if(--N<1){return M.apply(this,arguments)}}};L.keys=e||function(O){if(O!==Object(O)){throw new TypeError("Invalid object")}var N=[];for(var M in O){if(L.has(O,M)){N[N.length]=M}}return N};L.values=function(O){var M=[];for(var N in O){if(L.has(O,N)){M.push(O[N])}}return M};L.pairs=function(O){var N=[];for(var M in O){if(L.has(O,M)){N.push([M,O[M]])}}return N};L.invert=function(O){var M={};for(var N in O){if(L.has(O,N)){M[O[N]]=N}}return M};L.functions=L.methods=function(O){var N=[];for(var M in O){if(L.isFunction(O[M])){N.push(M)}}return N.sort()};L.extend=function(M){H(o.call(arguments,1),function(N){if(N){for(var O in N){M[O]=N[O]}}});return M};L.pick=function(N){var O={};var M=y.apply(C,o.call(arguments,1));H(M,function(P){if(P in N){O[P]=N[P]}});return O};L.omit=function(O){var P={};var N=y.apply(C,o.call(arguments,1));for(var M in O){if(!L.contains(N,M)){P[M]=O[M]}}return P};L.defaults=function(M){H(o.call(arguments,1),function(N){if(N){for(var O in N){if(M[O]==null){M[O]=N[O]}}}});return M};L.clone=function(M){if(!L.isObject(M)){return M}return L.isArray(M)?M.slice():L.extend({},M)};L.tap=function(N,M){M(N);return N};var I=function(T,S,N,O){if(T===S){return T!==0||1/T==1/S}if(T==null||S==null){return T===S}if(T instanceof L){T=T._wrapped}if(S instanceof L){S=S._wrapped}var Q=d.call(T);if(Q!=d.call(S)){return false}switch(Q){case"[object String]":return T==String(S);case"[object Number]":return T!=+T?S!=+S:(T==0?1/T==1/S:T==+S);case"[object Date]":case"[object Boolean]":return +T==+S;case"[object RegExp]":return T.source==S.source&&T.global==S.global&&T.multiline==S.multiline&&T.ignoreCase==S.ignoreCase}if(typeof T!="object"||typeof S!="object"){return false}var M=N.length;while(M--){if(N[M]==T){return O[M]==S}}N.push(T);O.push(S);var V=0,W=true;if(Q=="[object Array]"){V=T.length;W=V==S.length;if(W){while(V--){if(!(W=I(T[V],S[V],N,O))){break}}}}else{var R=T.constructor,P=S.constructor;if(R!==P&&!(L.isFunction(R)&&(R instanceof R)&&L.isFunction(P)&&(P instanceof P))){return false}for(var U in T){if(L.has(T,U)){V++;if(!(W=L.has(S,U)&&I(T[U],S[U],N,O))){break}}}if(W){for(U in S){if(L.has(S,U)&&!(V--)){break}}W=!V}}N.pop();O.pop();return W};L.isEqual=function(N,M){return I(N,M,[],[])};L.isEmpty=function(N){if(N==null){return true}if(L.isArray(N)||L.isString(N)){return N.length===0}for(var M in N){if(L.has(N,M)){return false}}return true};L.isElement=function(M){return !!(M&&M.nodeType===1)};L.isArray=u||function(M){return d.call(M)=="[object Array]"};L.isObject=function(M){return M===Object(M)};H(["Arguments","Function","String","Number","Date","RegExp"],function(M){L["is"+M]=function(N){return d.call(N)=="[object "+M+"]"}});if(!L.isArguments(arguments)){L.isArguments=function(M){return !!(M&&L.has(M,"callee"))}}if(typeof(/./)!=="function"){L.isFunction=function(M){return typeof M==="function"}}L.isFinite=function(M){return isFinite(M)&&!isNaN(parseFloat(M))};L.isNaN=function(M){return L.isNumber(M)&&M!=+M};L.isBoolean=function(M){return M===true||M===false||d.call(M)=="[object Boolean]"};L.isNull=function(M){return M===null};L.isUndefined=function(M){return M===void 0};L.has=function(N,M){return j.call(N,M)};L.noConflict=function(){w._=k;return this};L.identity=function(M){return M};L.times=function(Q,P,O){var M=Array(Q);for(var N=0;N<Q;N++){M[N]=P.call(O,N)}return M};L.random=function(N,M){if(M==null){M=N;N=0}return N+Math.floor(Math.random()*(M-N+1))};var m={escape:{"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","/":"&#x2F;"}};m.unescape=L.invert(m.escape);var J={escape:new RegExp("["+L.keys(m.escape).join("")+"]","g"),unescape:new RegExp("("+L.keys(m.unescape).join("|")+")","g")};L.each(["escape","unescape"],function(M){L[M]=function(N){if(N==null){return""}return(""+N).replace(J[M],function(O){return m[M][O]})}});L.result=function(M,O){if(M==null){return null}var N=M[O];return L.isFunction(N)?N.call(M):N};L.mixin=function(M){H(L.functions(M),function(N){var O=L[N]=M[N];L.prototype[N]=function(){var P=[this._wrapped];G.apply(P,arguments);return s.call(this,O.apply(L,P))}})};var z=0;L.uniqueId=function(M){var N=++z+"";return M?M+N:N};L.templateSettings={evaluate:/<%([\s\S]+?)%>/g,interpolate:/<%=([\s\S]+?)%>/g,escape:/<%-([\s\S]+?)%>/g};var v=/(.)^/;var h={"'":"'","\\":"\\","\r":"r","\n":"n","\t":"t","\u2028":"u2028","\u2029":"u2029"};var i=/\\|'|\r|\n|\t|\u2028|\u2029/g;L.template=function(U,P,O){var N;O=L.defaults({},O,L.templateSettings);var Q=new RegExp([(O.escape||v).source,(O.interpolate||v).source,(O.evaluate||v).source].join("|")+"|$","g");var R=0;var M="__p+='";U.replace(Q,function(W,X,V,Z,Y){M+=U.slice(R,Y).replace(i,function(aa){return"\\"+h[aa]});if(X){M+="'+\n((__t=("+X+"))==null?'':_.escape(__t))+\n'"}if(V){M+="'+\n((__t=("+V+"))==null?'':__t)+\n'"}if(Z){M+="';\n"+Z+"\n__p+='"}R=Y+W.length;return W});M+="';\n";if(!O.variable){M="with(obj||{}){\n"+M+"}\n"}M="var __t,__p='',__j=Array.prototype.join,print=function(){__p+=__j.call(arguments,'');};\n"+M+"return __p;\n";try{N=new Function(O.variable||"obj","_",M)}catch(S){S.source=M;throw S}if(P){return N(P,L)}var T=function(V){return N.call(this,V,L)};T.source="function("+(O.variable||"obj")+"){\n"+M+"}";return T};L.chain=function(M){return L(M).chain()};var s=function(M){return this._chain?L(M).chain():M};L.mixin(L);H(["pop","push","reverse","shift","sort","splice","unshift"],function(M){var N=C[M];L.prototype[M]=function(){var O=this._wrapped;N.apply(O,arguments);if((M=="shift"||M=="splice")&&O.length===0){delete O[0]}return s.call(this,O)}});H(["concat","join","slice"],function(M){var N=C[M];L.prototype[M]=function(){return s.call(this,N.apply(this._wrapped,arguments))}});L.extend(L.prototype,{chain:function(){this._chain=true;return this},value:function(){return this._wrapped}})}).call(this);