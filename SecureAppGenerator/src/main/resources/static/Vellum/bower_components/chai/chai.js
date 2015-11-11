!function(e){if("object"==typeof exports&&"undefined"!=typeof module)module.exports=e();else if("function"==typeof define&&define.amd)define([],e);else{var t;t="undefined"!=typeof window?window:"undefined"!=typeof global?global:"undefined"!=typeof self?self:this,t.chai=e()}}(function(){return function e(t,n,o){function r(s,a){if(!n[s]){if(!t[s]){var c="function"==typeof require&&require;if(!a&&c)return c(s,!0);if(i)return i(s,!0);var h=new Error("Cannot find module '"+s+"'");throw h.code="MODULE_NOT_FOUND",h}var u=n[s]={exports:{}};t[s][0].call(u.exports,function(e){var n=t[s][1][e];return r(n?n:e)},u,u.exports,e,t,n,o)}return n[s].exports}for(var i="function"==typeof require&&require,s=0;s<o.length;s++)r(o[s]);return r}({1:[function(e,t){t.exports=e("./lib/chai")},{"./lib/chai":2}],2:[function(e,t,n){var o=[],n=t.exports={};n.version="3.2.0",n.AssertionError=e("assertion-error");var r=e("./chai/utils");n.use=function(e){return~o.indexOf(e)||(e(this,r),o.push(e)),this},n.util=r;var i=e("./chai/config");n.config=i;var s=e("./chai/assertion");n.use(s);var a=e("./chai/core/assertions");n.use(a);var c=e("./chai/interface/expect");n.use(c);var h=e("./chai/interface/should");n.use(h);var u=e("./chai/interface/assert");n.use(u)},{"./chai/assertion":3,"./chai/config":4,"./chai/core/assertions":5,"./chai/interface/assert":6,"./chai/interface/expect":7,"./chai/interface/should":8,"./chai/utils":21,"assertion-error":29}],3:[function(e,t){var n=e("./config");t.exports=function(e,t){function o(e,t,n){i(this,"ssfi",n||arguments.callee),i(this,"object",e),i(this,"message",t)}var r=e.AssertionError,i=t.flag;e.Assertion=o,Object.defineProperty(o,"includeStack",{get:function(){return console.warn("Assertion.includeStack is deprecated, use chai.config.includeStack instead."),n.includeStack},set:function(e){console.warn("Assertion.includeStack is deprecated, use chai.config.includeStack instead."),n.includeStack=e}}),Object.defineProperty(o,"showDiff",{get:function(){return console.warn("Assertion.showDiff is deprecated, use chai.config.showDiff instead."),n.showDiff},set:function(e){console.warn("Assertion.showDiff is deprecated, use chai.config.showDiff instead."),n.showDiff=e}}),o.addProperty=function(e,n){t.addProperty(this.prototype,e,n)},o.addMethod=function(e,n){t.addMethod(this.prototype,e,n)},o.addChainableMethod=function(e,n,o){t.addChainableMethod(this.prototype,e,n,o)},o.overwriteProperty=function(e,n){t.overwriteProperty(this.prototype,e,n)},o.overwriteMethod=function(e,n){t.overwriteMethod(this.prototype,e,n)},o.overwriteChainableMethod=function(e,n,o){t.overwriteChainableMethod(this.prototype,e,n,o)},o.prototype.assert=function(e,o,s,a,c,h){var u=t.test(this,arguments);if(!0!==h&&(h=!1),!0!==n.showDiff&&(h=!1),!u){var o=t.getMessage(this,arguments),f=t.getActual(this,arguments);throw new r(o,{actual:f,expected:a,showDiff:h},n.includeStack?this.assert:i(this,"ssfi"))}},Object.defineProperty(o.prototype,"_obj",{get:function(){return i(this,"object")},set:function(e){i(this,"object",e)}})}},{"./config":4}],4:[function(e,t){t.exports={includeStack:!1,showDiff:!0,truncateThreshold:40}},{}],5:[function(e,t){t.exports=function(e,t){function n(e,n){n&&E(this,"message",n),e=e.toLowerCase();var o=E(this,"object"),r=~["a","e","i","o","u"].indexOf(e.charAt(0))?"an ":"a ";this.assert(e===t.type(o),"expected #{this} to be "+r+e,"expected #{this} not to be "+r+e)}function o(){E(this,"contains",!0)}function r(e,n){n&&E(this,"message",n);var o=E(this,"object"),r=!1;if("array"===t.type(o)&&"object"===t.type(e)){for(var i in o)if(t.eql(o[i],e)){r=!0;break}}else if("object"===t.type(e)){if(!E(this,"negate")){for(var s in e)new N(o).property(s,e[s]);return}var a={};for(var s in e)a[s]=o[s];r=t.eql(a,e)}else r=o&&~o.indexOf(e);this.assert(r,"expected #{this} to include "+t.inspect(e),"expected #{this} to not include "+t.inspect(e))}function i(){var e=E(this,"object"),t=Object.prototype.toString.call(e);this.assert("[object Arguments]"===t,"expected #{this} to be arguments but got "+t,"expected #{this} to not be arguments")}function s(e,t){t&&E(this,"message",t);var n=E(this,"object");return E(this,"deep")?this.eql(e):void this.assert(e===n,"expected #{this} to equal #{exp}","expected #{this} to not equal #{exp}",e,this._obj,!0)}function a(e,n){n&&E(this,"message",n),this.assert(t.eql(e,E(this,"object")),"expected #{this} to deeply equal #{exp}","expected #{this} to not deeply equal #{exp}",e,this._obj,!0)}function c(e,t){t&&E(this,"message",t);var n=E(this,"object");if(E(this,"doLength")){new N(n,t).to.have.property("length");var o=n.length;this.assert(o>e,"expected #{this} to have a length above #{exp} but got #{act}","expected #{this} to not have a length above #{exp}",e,o)}else this.assert(n>e,"expected #{this} to be above "+e,"expected #{this} to be at most "+e)}function h(e,t){t&&E(this,"message",t);var n=E(this,"object");if(E(this,"doLength")){new N(n,t).to.have.property("length");var o=n.length;this.assert(o>=e,"expected #{this} to have a length at least #{exp} but got #{act}","expected #{this} to have a length below #{exp}",e,o)}else this.assert(n>=e,"expected #{this} to be at least "+e,"expected #{this} to be below "+e)}function u(e,t){t&&E(this,"message",t);var n=E(this,"object");if(E(this,"doLength")){new N(n,t).to.have.property("length");var o=n.length;this.assert(e>o,"expected #{this} to have a length below #{exp} but got #{act}","expected #{this} to not have a length below #{exp}",e,o)}else this.assert(e>n,"expected #{this} to be below "+e,"expected #{this} to be at least "+e)}function f(e,t){t&&E(this,"message",t);var n=E(this,"object");if(E(this,"doLength")){new N(n,t).to.have.property("length");var o=n.length;this.assert(e>=o,"expected #{this} to have a length at most #{exp} but got #{act}","expected #{this} to have a length above #{exp}",e,o)}else this.assert(e>=n,"expected #{this} to be at most "+e,"expected #{this} to be above "+e)}function p(e,n){n&&E(this,"message",n);var o=t.getName(e);this.assert(E(this,"object")instanceof e,"expected #{this} to be an instance of "+o,"expected #{this} to not be an instance of "+o)}function d(e,n){n&&E(this,"message",n);var o=E(this,"object");this.assert(o.hasOwnProperty(e),"expected #{this} to have own property "+t.inspect(e),"expected #{this} to not have own property "+t.inspect(e))}function l(e,n,o){"string"==typeof n&&(o=n,n=null),o&&E(this,"message",o);var r=E(this,"object"),i=Object.getOwnPropertyDescriptor(Object(r),e);i&&n?this.assert(t.eql(n,i),"expected the own property descriptor for "+t.inspect(e)+" on #{this} to match "+t.inspect(n)+", got "+t.inspect(i),"expected the own property descriptor for "+t.inspect(e)+" on #{this} to not match "+t.inspect(n),n,i,!0):this.assert(i,"expected #{this} to have an own property descriptor for "+t.inspect(e),"expected #{this} to not have an own property descriptor for "+t.inspect(e)),E(this,"object",i)}function g(){E(this,"doLength",!0)}function b(e,t){t&&E(this,"message",t);var n=E(this,"object");new N(n,t).to.have.property("length");var o=n.length;this.assert(o==e,"expected #{this} to have a length of #{exp} but got #{act}","expected #{this} to not have a length of #{act}",e,o)}function y(e,t){t&&E(this,"message",t);var n=E(this,"object");this.assert(e.exec(n),"expected #{this} to match "+e,"expected #{this} not to match "+e)}function v(e){var n,o=E(this,"object"),r=!0,i="keys must be given single argument of Array|Object|String, or multiple String arguments";switch(t.type(e)){case"array":if(arguments.length>1)throw new Error(i);break;case"object":if(arguments.length>1)throw new Error(i);e=Object.keys(e);break;default:e=Array.prototype.slice.call(arguments)}if(!e.length)throw new Error("keys required");var s=Object.keys(o),a=e,c=e.length,h=E(this,"any"),u=E(this,"all");if(h||u||(u=!0),h){var f=a.filter(function(e){return~s.indexOf(e)});r=f.length>0}if(u&&(r=e.every(function(e){return~s.indexOf(e)}),E(this,"negate")||E(this,"contains")||(r=r&&e.length==s.length)),c>1){e=e.map(function(e){return t.inspect(e)});var p=e.pop();u&&(n=e.join(", ")+", and "+p),h&&(n=e.join(", ")+", or "+p)}else n=t.inspect(e[0]);n=(c>1?"keys ":"key ")+n,n=(E(this,"contains")?"contain ":"have ")+n,this.assert(r,"expected #{this} to "+n,"expected #{this} to not "+n,a.slice(0).sort(),s.sort(),!0)}function w(e,n,o){o&&E(this,"message",o);var r=E(this,"object");new N(r,o).is.a("function");var i=!1,s=null,a=null,c=null;0===arguments.length?(n=null,e=null):e&&(e instanceof RegExp||"string"==typeof e)?(n=e,e=null):e&&e instanceof Error?(s=e,e=null,n=null):"function"==typeof e?(a=e.prototype.name||e.name,"Error"===a&&e!==Error&&(a=(new e).name)):e=null;try{r()}catch(h){if(s)return this.assert(h===s,"expected #{this} to throw #{exp} but #{act} was thrown","expected #{this} to not throw #{exp}",s instanceof Error?s.toString():s,h instanceof Error?h.toString():h),E(this,"object",h),this;if(e&&(this.assert(h instanceof e,"expected #{this} to throw #{exp} but #{act} was thrown","expected #{this} to not throw #{exp} but #{act} was thrown",a,h instanceof Error?h.toString():h),!n))return E(this,"object",h),this;var u="error"===t.type(h)&&"message"in h?h.message:""+h;if(null!=u&&n&&n instanceof RegExp)return this.assert(n.exec(u),"expected #{this} to throw error matching #{exp} but got #{act}","expected #{this} to throw error not matching #{exp}",n,u),E(this,"object",h),this;if(null!=u&&n&&"string"==typeof n)return this.assert(~u.indexOf(n),"expected #{this} to throw error including #{exp} but got #{act}","expected #{this} to throw error not including #{act}",n,u),E(this,"object",h),this;i=!0,c=h}var f="",p=null!==a?a:s?"#{exp}":"an error";i&&(f=" but #{act} was thrown"),this.assert(i===!0,"expected #{this} to throw "+p+f,"expected #{this} to not throw "+p+f,s instanceof Error?s.toString():s,c instanceof Error?c.toString():c),E(this,"object",c)}function x(e,n){n&&E(this,"message",n);var o=E(this,"object"),r=E(this,"itself"),i="function"!==t.type(o)||r?o[e]:o.prototype[e];this.assert("function"==typeof i,"expected #{this} to respond to "+t.inspect(e),"expected #{this} to not respond to "+t.inspect(e))}function m(e,n){n&&E(this,"message",n);var o=E(this,"object"),r=e(o);this.assert(r,"expected #{this} to satisfy "+t.objDisplay(e),"expected #{this} to not satisfy"+t.objDisplay(e),this.negate?!1:!0,r)}function j(e,t,n){return e.every(function(e){return n?t.some(function(t){return n(e,t)}):-1!==t.indexOf(e)})}function O(e,t,n){n&&E(this,"message",n);var o=E(this,"object");new N(e,n).to.have.property(t),new N(o).is.a("function");var r=e[t];o(),this.assert(r!==e[t],"expected ."+t+" to change","expected ."+t+" to not change")}function M(e,t,n){n&&E(this,"message",n);var o=E(this,"object");new N(e,n).to.have.property(t),new N(o).is.a("function");var r=e[t];o(),this.assert(e[t]-r>0,"expected ."+t+" to increase","expected ."+t+" to not increase")}function P(e,t,n){n&&E(this,"message",n);var o=E(this,"object");new N(e,n).to.have.property(t),new N(o).is.a("function");var r=e[t];o(),this.assert(e[t]-r<0,"expected ."+t+" to decrease","expected ."+t+" to not decrease")}var N=e.Assertion,E=(Object.prototype.toString,t.flag);["to","be","been","is","and","has","have","with","that","which","at","of","same"].forEach(function(e){N.addProperty(e,function(){return this})}),N.addProperty("not",function(){E(this,"negate",!0)}),N.addProperty("deep",function(){E(this,"deep",!0)}),N.addProperty("any",function(){E(this,"any",!0),E(this,"all",!1)}),N.addProperty("all",function(){E(this,"all",!0),E(this,"any",!1)}),N.addChainableMethod("an",n),N.addChainableMethod("a",n),N.addChainableMethod("include",r,o),N.addChainableMethod("contain",r,o),N.addChainableMethod("contains",r,o),N.addChainableMethod("includes",r,o),N.addProperty("ok",function(){this.assert(E(this,"object"),"expected #{this} to be truthy","expected #{this} to be falsy")}),N.addProperty("true",function(){this.assert(!0===E(this,"object"),"expected #{this} to be true","expected #{this} to be false",this.negate?!1:!0)}),N.addProperty("false",function(){this.assert(!1===E(this,"object"),"expected #{this} to be false","expected #{this} to be true",this.negate?!0:!1)}),N.addProperty("null",function(){this.assert(null===E(this,"object"),"expected #{this} to be null","expected #{this} not to be null")}),N.addProperty("undefined",function(){this.assert(void 0===E(this,"object"),"expected #{this} to be undefined","expected #{this} not to be undefined")}),N.addProperty("NaN",function(){this.assert(isNaN(E(this,"object")),"expected #{this} to be NaN","expected #{this} not to be NaN")}),N.addProperty("exist",function(){this.assert(null!=E(this,"object"),"expected #{this} to exist","expected #{this} to not exist")}),N.addProperty("empty",function(){var e=E(this,"object"),t=e;Array.isArray(e)||"string"==typeof object?t=e.length:"object"==typeof e&&(t=Object.keys(e).length),this.assert(!t,"expected #{this} to be empty","expected #{this} not to be empty")}),N.addProperty("arguments",i),N.addProperty("Arguments",i),N.addMethod("equal",s),N.addMethod("equals",s),N.addMethod("eq",s),N.addMethod("eql",a),N.addMethod("eqls",a),N.addMethod("above",c),N.addMethod("gt",c),N.addMethod("greaterThan",c),N.addMethod("least",h),N.addMethod("gte",h),N.addMethod("below",u),N.addMethod("lt",u),N.addMethod("lessThan",u),N.addMethod("most",f),N.addMethod("lte",f),N.addMethod("within",function(e,t,n){n&&E(this,"message",n);var o=E(this,"object"),r=e+".."+t;if(E(this,"doLength")){new N(o,n).to.have.property("length");var i=o.length;this.assert(i>=e&&t>=i,"expected #{this} to have a length within "+r,"expected #{this} to not have a length within "+r)}else this.assert(o>=e&&t>=o,"expected #{this} to be within "+r,"expected #{this} to not be within "+r)}),N.addMethod("instanceof",p),N.addMethod("instanceOf",p),N.addMethod("property",function(e,n,o){o&&E(this,"message",o);var r=!!E(this,"deep"),i=r?"deep property ":"property ",s=E(this,"negate"),a=E(this,"object"),c=r?t.getPathInfo(e,a):null,h=r?c.exists:t.hasProperty(e,a),u=r?c.value:a[e];if(s&&arguments.length>1){if(void 0===u)throw o=null!=o?o+": ":"",new Error(o+t.inspect(a)+" has no "+i+t.inspect(e))}else this.assert(h,"expected #{this} to have a "+i+t.inspect(e),"expected #{this} to not have "+i+t.inspect(e));arguments.length>1&&this.assert(n===u,"expected #{this} to have a "+i+t.inspect(e)+" of #{exp}, but got #{act}","expected #{this} to not have a "+i+t.inspect(e)+" of #{act}",n,u),E(this,"object",u)}),N.addMethod("ownProperty",d),N.addMethod("haveOwnProperty",d),N.addMethod("ownPropertyDescriptor",l),N.addMethod("haveOwnPropertyDescriptor",l),N.addChainableMethod("length",b,g),N.addMethod("lengthOf",b),N.addMethod("match",y),N.addMethod("matches",y),N.addMethod("string",function(e,n){n&&E(this,"message",n);var o=E(this,"object");new N(o,n).is.a("string"),this.assert(~o.indexOf(e),"expected #{this} to contain "+t.inspect(e),"expected #{this} to not contain "+t.inspect(e))}),N.addMethod("keys",v),N.addMethod("key",v),N.addMethod("throw",w),N.addMethod("throws",w),N.addMethod("Throw",w),N.addMethod("respondTo",x),N.addMethod("respondsTo",x),N.addProperty("itself",function(){E(this,"itself",!0)}),N.addMethod("satisfy",m),N.addMethod("satisfies",m),N.addMethod("closeTo",function(e,n,o){o&&E(this,"message",o);var r=E(this,"object");if(new N(r,o).is.a("number"),"number"!==t.type(e)||"number"!==t.type(n))throw new Error("the arguments to closeTo must be numbers");this.assert(Math.abs(r-e)<=n,"expected #{this} to be close to "+e+" +/- "+n,"expected #{this} not to be close to "+e+" +/- "+n)}),N.addMethod("members",function(e,n){n&&E(this,"message",n);var o=E(this,"object");new N(o).to.be.an("array"),new N(e).to.be.an("array");var r=E(this,"deep")?t.eql:void 0;return E(this,"contains")?this.assert(j(e,o,r),"expected #{this} to be a superset of #{act}","expected #{this} to not be a superset of #{act}",o,e):void this.assert(j(o,e,r)&&j(e,o,r),"expected #{this} to have the same members as #{act}","expected #{this} to not have the same members as #{act}",o,e)}),N.addChainableMethod("change",O),N.addChainableMethod("changes",O),N.addChainableMethod("increase",M),N.addChainableMethod("increases",M),N.addChainableMethod("decrease",P),N.addChainableMethod("decreases",P),N.addProperty("extensible",function(){var e=E(this,"object");this.assert(Object.isExtensible(e),"expected #{this} to be extensible","expected #{this} to not be extensible")}),N.addProperty("sealed",function(){var e=E(this,"object");this.assert(Object.isSealed(e),"expected #{this} to be sealed","expected #{this} to not be sealed")}),N.addProperty("frozen",function(){var e=E(this,"object");this.assert(Object.isFrozen(e),"expected #{this} to be frozen","expected #{this} to not be frozen")})}},{}],6:[function(e,t){t.exports=function(e,t){var n=e.Assertion,o=t.flag,r=e.assert=function(t,o){var r=new n(null,null,e.assert);r.assert(t,o,"[ negation message unavailable ]")};r.fail=function(t,n,o,i){throw o=o||"assert.fail()",new e.AssertionError(o,{actual:t,expected:n,operator:i},r.fail)},r.isOk=function(e,t){new n(e,t).is.ok},r.isNotOk=function(e,t){new n(e,t).is.not.ok},r.equal=function(e,t,i){var s=new n(e,i,r.equal);s.assert(t==o(s,"object"),"expected #{this} to equal #{exp}","expected #{this} to not equal #{act}",t,e)},r.notEqual=function(e,t,i){var s=new n(e,i,r.notEqual);s.assert(t!=o(s,"object"),"expected #{this} to not equal #{exp}","expected #{this} to equal #{act}",t,e)},r.strictEqual=function(e,t,o){new n(e,o).to.equal(t)},r.notStrictEqual=function(e,t,o){new n(e,o).to.not.equal(t)},r.deepEqual=function(e,t,o){new n(e,o).to.eql(t)},r.notDeepEqual=function(e,t,o){new n(e,o).to.not.eql(t)},r.isAbove=function(e,t,o){new n(e,o).to.be.above(t)},r.isBelow=function(e,t,o){new n(e,o).to.be.below(t)},r.isTrue=function(e,t){new n(e,t).is["true"]},r.isFalse=function(e,t){new n(e,t).is["false"]},r.isNull=function(e,t){new n(e,t).to.equal(null)},r.isNotNull=function(e,t){new n(e,t).to.not.equal(null)},r.isNaN=function(e,t){new n(e,t).to.be.NaN},r.isNotNaN=function(e,t){new n(e,t).not.to.be.NaN},r.isUndefined=function(e,t){new n(e,t).to.equal(void 0)},r.isDefined=function(e,t){new n(e,t).to.not.equal(void 0)},r.isFunction=function(e,t){new n(e,t).to.be.a("function")},r.isNotFunction=function(e,t){new n(e,t).to.not.be.a("function")},r.isObject=function(e,t){new n(e,t).to.be.a("object")},r.isNotObject=function(e,t){new n(e,t).to.not.be.a("object")},r.isArray=function(e,t){new n(e,t).to.be.an("array")},r.isNotArray=function(e,t){new n(e,t).to.not.be.an("array")},r.isString=function(e,t){new n(e,t).to.be.a("string")},r.isNotString=function(e,t){new n(e,t).to.not.be.a("string")},r.isNumber=function(e,t){new n(e,t).to.be.a("number")},r.isNotNumber=function(e,t){new n(e,t).to.not.be.a("number")},r.isBoolean=function(e,t){new n(e,t).to.be.a("boolean")},r.isNotBoolean=function(e,t){new n(e,t).to.not.be.a("boolean")},r.typeOf=function(e,t,o){new n(e,o).to.be.a(t)},r.notTypeOf=function(e,t,o){new n(e,o).to.not.be.a(t)},r.instanceOf=function(e,t,o){new n(e,o).to.be.instanceOf(t)},r.notInstanceOf=function(e,t,o){new n(e,o).to.not.be.instanceOf(t)},r.include=function(e,t,o){new n(e,o,r.include).include(t)},r.notInclude=function(e,t,o){new n(e,o,r.notInclude).not.include(t)},r.match=function(e,t,o){new n(e,o).to.match(t)},r.notMatch=function(e,t,o){new n(e,o).to.not.match(t)},r.property=function(e,t,o){new n(e,o).to.have.property(t)},r.notProperty=function(e,t,o){new n(e,o).to.not.have.property(t)},r.deepProperty=function(e,t,o){new n(e,o).to.have.deep.property(t)},r.notDeepProperty=function(e,t,o){new n(e,o).to.not.have.deep.property(t)},r.propertyVal=function(e,t,o,r){new n(e,r).to.have.property(t,o)},r.propertyNotVal=function(e,t,o,r){new n(e,r).to.not.have.property(t,o)},r.deepPropertyVal=function(e,t,o,r){new n(e,r).to.have.deep.property(t,o)},r.deepPropertyNotVal=function(e,t,o,r){new n(e,r).to.not.have.deep.property(t,o)},r.lengthOf=function(e,t,o){new n(e,o).to.have.length(t)},r["throws"]=function(e,t,r,i){("string"==typeof t||t instanceof RegExp)&&(r=t,t=null);var s=new n(e,i).to["throw"](t,r);return o(s,"object")},r.doesNotThrow=function(e,t,o){"string"==typeof t&&(o=t,t=null),new n(e,o).to.not.Throw(t)},r.operator=function(e,r,i,s){var a;switch(r){case"==":a=e==i;break;case"===":a=e===i;break;case">":a=e>i;break;case">=":a=e>=i;break;case"<":a=i>e;break;case"<=":a=i>=e;break;case"!=":a=e!=i;break;case"!==":a=e!==i;break;default:throw new Error('Invalid operator "'+r+'"')}var c=new n(a,s);c.assert(!0===o(c,"object"),"expected "+t.inspect(e)+" to be "+r+" "+t.inspect(i),"expected "+t.inspect(e)+" to not be "+r+" "+t.inspect(i))},r.closeTo=function(e,t,o,r){new n(e,r).to.be.closeTo(t,o)},r.sameMembers=function(e,t,o){new n(e,o).to.have.same.members(t)},r.sameDeepMembers=function(e,t,o){new n(e,o).to.have.same.deep.members(t)},r.includeMembers=function(e,t,o){new n(e,o).to.include.members(t)},r.changes=function(e,t,o){new n(e).to.change(t,o)},r.doesNotChange=function(e,t,o){new n(e).to.not.change(t,o)},r.increases=function(e,t,o){new n(e).to.increase(t,o)},r.doesNotIncrease=function(e,t,o){new n(e).to.not.increase(t,o)},r.decreases=function(e,t,o){new n(e).to.decrease(t,o)},r.doesNotDecrease=function(e,t,o){new n(e).to.not.decrease(t,o)},r.ifError=function(e){if(e)throw e},r.isExtensible=function(e,t){new n(e,t).to.be.extensible},r.isNotExtensible=function(e,t){new n(e,t).to.not.be.extensible},r.isSealed=function(e,t){new n(e,t).to.be.sealed},r.isNotSealed=function(e,t){new n(e,t).to.not.be.sealed},r.isFrozen=function(e,t){new n(e,t).to.be.frozen},r.isNotFrozen=function(e,t){new n(e,t).to.not.be.frozen},function i(e,t){return r[t]=r[e],i}("isOk","ok")("isNotOk","notOk")("throws","throw")("throws","Throw")("isExtensible","extensible")("isNotExtensible","notExtensible")("isSealed","sealed")("isNotSealed","notSealed")("isFrozen","frozen")("isNotFrozen","notFrozen")}},{}],7:[function(e,t){t.exports=function(e){e.expect=function(t,n){return new e.Assertion(t,n)},e.expect.fail=function(t,n,o,r){throw o=o||"expect.fail()",new e.AssertionError(o,{actual:t,expected:n,operator:r},e.expect.fail)}}},{}],8:[function(e,t){t.exports=function(e){function t(){function t(){return this instanceof String||this instanceof Number||this instanceof Boolean?new n(this.valueOf(),null,t):new n(this,null,t)}function o(e){Object.defineProperty(this,"should",{value:e,enumerable:!0,configurable:!0,writable:!0})}Object.defineProperty(Object.prototype,"should",{set:o,get:t,configurable:!0});var r={};return r.fail=function(t,n,o,i){throw o=o||"should.fail()",new e.AssertionError(o,{actual:t,expected:n,operator:i},r.fail)},r.equal=function(e,t,o){new n(e,o).to.equal(t)},r.Throw=function(e,t,o,r){new n(e,r).to.Throw(t,o)},r.exist=function(e,t){new n(e,t).to.exist},r.not={},r.not.equal=function(e,t,o){new n(e,o).to.not.equal(t)},r.not.Throw=function(e,t,o,r){new n(e,r).to.not.Throw(t,o)},r.not.exist=function(e,t){new n(e,t).to.not.exist},r["throw"]=r.Throw,r.not["throw"]=r.not.Throw,r}var n=e.Assertion;e.should=t,e.Should=t}},{}],9:[function(e,t){var n=e("./transferFlags"),o=e("./flag"),r=e("../config"),i="__proto__"in Object,s=/^(?:length|name|arguments|caller)$/,a=Function.prototype.call,c=Function.prototype.apply;t.exports=function(e,t,h,u){"function"!=typeof u&&(u=function(){});var f={method:h,chainingBehavior:u};e.__methods||(e.__methods={}),e.__methods[t]=f,Object.defineProperty(e,t,{get:function(){f.chainingBehavior.call(this);var t=function p(){var e=o(this,"ssfi");e&&r.includeStack===!1&&o(this,"ssfi",p);var t=f.method.apply(this,arguments);return void 0===t?this:t};if(i){var h=t.__proto__=Object.create(this);h.call=a,h.apply=c}else{var u=Object.getOwnPropertyNames(e);u.forEach(function(n){if(!s.test(n)){var o=Object.getOwnPropertyDescriptor(e,n);Object.defineProperty(t,n,o)}})}return n(this,t),t},configurable:!0})}},{"../config":4,"./flag":12,"./transferFlags":28}],10:[function(e,t){var n=e("../config"),o=e("./flag");t.exports=function(e,t,r){e[t]=function(){var i=o(this,"ssfi");i&&n.includeStack===!1&&o(this,"ssfi",e[t]);var s=r.apply(this,arguments);return void 0===s?this:s}}},{"../config":4,"./flag":12}],11:[function(e,t){t.exports=function(e,t,n){Object.defineProperty(e,t,{get:function(){var e=n.call(this);return void 0===e?this:e},configurable:!0})}},{}],12:[function(e,t){t.exports=function(e,t,n){var o=e.__flags||(e.__flags=Object.create(null));return 3!==arguments.length?o[t]:void(o[t]=n)}},{}],13:[function(e,t){t.exports=function(e,t){return t.length>4?t[4]:e._obj}},{}],14:[function(e,t){t.exports=function(e){var t=[];for(var n in e)t.push(n);return t}},{}],15:[function(e,t){var n=e("./flag"),o=e("./getActual"),r=(e("./inspect"),e("./objDisplay"));t.exports=function(e,t){var i=n(e,"negate"),s=n(e,"object"),a=t[3],c=o(e,t),h=i?t[2]:t[1],u=n(e,"message");return"function"==typeof h&&(h=h()),h=h||"",h=h.replace(/#{this}/g,r(s)).replace(/#{act}/g,r(c)).replace(/#{exp}/g,r(a)),u?u+": "+h:h}},{"./flag":12,"./getActual":13,"./inspect":22,"./objDisplay":23}],16:[function(e,t){t.exports=function(e){if(e.name)return e.name;var t=/^\s?function ([^(]*)\(/.exec(e);return t&&t[1]?t[1]:""}},{}],17:[function(e,t){function n(e){var t=e.replace(/([^\\])\[/g,"$1.["),n=t.match(/(\\\.|[^.]+?)+/g);return n.map(function(e){var t=/^\[(\d+)\]$/,n=t.exec(e);return n?{i:parseFloat(n[1])}:{p:e.replace(/\\([.\[\]])/g,"$1")}})}function o(e,t,n){var o,r=t;n=void 0===n?e.length:n;for(var i=0,s=n;s>i;i++){var a=e[i];r?("undefined"!=typeof a.p?r=r[a.p]:"undefined"!=typeof a.i&&(r=r[a.i]),i==s-1&&(o=r)):o=void 0}return o}var r=e("./hasProperty");t.exports=function(e,t){var i=n(e),s=i[i.length-1],a={parent:i.length>1?o(i,t,i.length-1):t,name:s.p||s.i,value:o(i,t)};return a.exists=r(a.name,a.parent),a}},{"./hasProperty":20}],18:[function(e,t){var n=e("./getPathInfo");t.exports=function(e,t){var o=n(e,t);return o.value}},{"./getPathInfo":17}],19:[function(e,t){t.exports=function(e){function t(e){-1===n.indexOf(e)&&n.push(e)}for(var n=Object.getOwnPropertyNames(e),o=Object.getPrototypeOf(e);null!==o;)Object.getOwnPropertyNames(o).forEach(t),o=Object.getPrototypeOf(o);return n}},{}],20:[function(e,t){var n=e("type-detect"),o={number:Number,string:String};t.exports=function(e,t){var r=n(t);return"null"===r||"undefined"===r?!1:(o[r]&&"object"!=typeof t&&(t=new o[r](t)),e in t)}},{"type-detect":34}],21:[function(e,t,n){var n=t.exports={};n.test=e("./test"),n.type=e("type-detect"),n.getMessage=e("./getMessage"),n.getActual=e("./getActual"),n.inspect=e("./inspect"),n.objDisplay=e("./objDisplay"),n.flag=e("./flag"),n.transferFlags=e("./transferFlags"),n.eql=e("deep-eql"),n.getPathValue=e("./getPathValue"),n.getPathInfo=e("./getPathInfo"),n.hasProperty=e("./hasProperty"),n.getName=e("./getName"),n.addProperty=e("./addProperty"),n.addMethod=e("./addMethod"),n.overwriteProperty=e("./overwriteProperty"),n.overwriteMethod=e("./overwriteMethod"),n.addChainableMethod=e("./addChainableMethod"),n.overwriteChainableMethod=e("./overwriteChainableMethod")},{"./addChainableMethod":9,"./addMethod":10,"./addProperty":11,"./flag":12,"./getActual":13,"./getMessage":15,"./getName":16,"./getPathInfo":17,"./getPathValue":18,"./hasProperty":20,"./inspect":22,"./objDisplay":23,"./overwriteChainableMethod":24,"./overwriteMethod":25,"./overwriteProperty":26,"./test":27,"./transferFlags":28,"deep-eql":30,"type-detect":34}],22:[function(e,t,n){function o(e,t,n){var o={showHidden:t,seen:[],stylize:function(e){return e}};return r(o,e,"undefined"==typeof n?2:n)}function r(e,t,o){if(t&&"function"==typeof t.inspect&&t.inspect!==n.inspect&&(!t.constructor||t.constructor.prototype!==t)){var l=t.inspect(o);return"string"!=typeof l&&(l=r(e,l,o)),l}var w=i(e,t);if(w)return w;if(v(t)){if("outerHTML"in t)return t.outerHTML;try{if(document.xmlVersion){var x=new XMLSerializer;return x.serializeToString(t)}var m="http://www.w3.org/1999/xhtml",j=document.createElementNS(m,"_");return j.appendChild(t.cloneNode(!1)),html=j.innerHTML.replace("><",">"+t.innerHTML+"<"),j.innerHTML="",html}catch(O){}}var M=y(t),P=e.showHidden?b(t):M;if(0===P.length||d(t)&&(1===P.length&&"stack"===P[0]||2===P.length&&"description"===P[0]&&"stack"===P[1])){if("function"==typeof t){var N=g(t),E=N?": "+N:"";return e.stylize("[Function"+E+"]","special")}if(f(t))return e.stylize(RegExp.prototype.toString.call(t),"regexp");if(p(t))return e.stylize(Date.prototype.toUTCString.call(t),"date");if(d(t))return s(t)}var S="",k=!1,_=["{","}"];if(u(t)&&(k=!0,_=["[","]"]),"function"==typeof t){var N=g(t),E=N?": "+N:"";S=" [Function"+E+"]"}if(f(t)&&(S=" "+RegExp.prototype.toString.call(t)),p(t)&&(S=" "+Date.prototype.toUTCString.call(t)),d(t))return s(t);if(0===P.length&&(!k||0==t.length))return _[0]+S+_[1];if(0>o)return f(t)?e.stylize(RegExp.prototype.toString.call(t),"regexp"):e.stylize("[Object]","special");e.seen.push(t);var q;return q=k?a(e,t,o,M,P):P.map(function(n){return c(e,t,o,M,n,k)}),e.seen.pop(),h(q,S,_)}function i(e,t){switch(typeof t){case"undefined":return e.stylize("undefined","undefined");case"string":var n="'"+JSON.stringify(t).replace(/^"|"$/g,"").replace(/'/g,"\\'").replace(/\\"/g,'"')+"'";return e.stylize(n,"string");case"number":return 0===t&&1/t===-1/0?e.stylize("-0","number"):e.stylize(""+t,"number");case"boolean":return e.stylize(""+t,"boolean")}return null===t?e.stylize("null","null"):void 0}function s(e){return"["+Error.prototype.toString.call(e)+"]"}function a(e,t,n,o,r){for(var i=[],s=0,a=t.length;a>s;++s)i.push(Object.prototype.hasOwnProperty.call(t,String(s))?c(e,t,n,o,String(s),!0):"");return r.forEach(function(r){r.match(/^\d+$/)||i.push(c(e,t,n,o,r,!0))}),i}function c(e,t,n,o,i,s){var a,c;if(t.__lookupGetter__&&(t.__lookupGetter__(i)?c=t.__lookupSetter__(i)?e.stylize("[Getter/Setter]","special"):e.stylize("[Getter]","special"):t.__lookupSetter__(i)&&(c=e.stylize("[Setter]","special"))),o.indexOf(i)<0&&(a="["+i+"]"),c||(e.seen.indexOf(t[i])<0?(c=null===n?r(e,t[i],null):r(e,t[i],n-1),c.indexOf("\n")>-1&&(c=s?c.split("\n").map(function(e){return"  "+e}).join("\n").substr(2):"\n"+c.split("\n").map(function(e){return"   "+e}).join("\n"))):c=e.stylize("[Circular]","special")),"undefined"==typeof a){if(s&&i.match(/^\d+$/))return c;a=JSON.stringify(""+i),a.match(/^"([a-zA-Z_][a-zA-Z_0-9]*)"$/)?(a=a.substr(1,a.length-2),a=e.stylize(a,"name")):(a=a.replace(/'/g,"\\'").replace(/\\"/g,'"').replace(/(^"|"$)/g,"'"),a=e.stylize(a,"string"))}return a+": "+c}function h(e,t,n){var o=0,r=e.reduce(function(e,t){return o++,t.indexOf("\n")>=0&&o++,e+t.length+1},0);return r>60?n[0]+(""===t?"":t+"\n ")+" "+e.join(",\n  ")+" "+n[1]:n[0]+t+" "+e.join(", ")+" "+n[1]}function u(e){return Array.isArray(e)||"object"==typeof e&&"[object Array]"===l(e)}function f(e){return"object"==typeof e&&"[object RegExp]"===l(e)}function p(e){return"object"==typeof e&&"[object Date]"===l(e)}function d(e){return"object"==typeof e&&"[object Error]"===l(e)}function l(e){return Object.prototype.toString.call(e)}var g=e("./getName"),b=e("./getProperties"),y=e("./getEnumerableProperties");t.exports=o;var v=function(e){return"object"==typeof HTMLElement?e instanceof HTMLElement:e&&"object"==typeof e&&1===e.nodeType&&"string"==typeof e.nodeName}},{"./getEnumerableProperties":14,"./getName":16,"./getProperties":19}],23:[function(e,t){var n=e("./inspect"),o=e("../config");t.exports=function(e){var t=n(e),r=Object.prototype.toString.call(e);if(o.truncateThreshold&&t.length>=o.truncateThreshold){if("[object Function]"===r)return e.name&&""!==e.name?"[Function: "+e.name+"]":"[Function]";if("[object Array]"===r)return"[ Array("+e.length+") ]";if("[object Object]"===r){var i=Object.keys(e),s=i.length>2?i.splice(0,2).join(", ")+", ...":i.join(", ");return"{ Object ("+s+") }"}return t}return t}},{"../config":4,"./inspect":22}],24:[function(e,t){t.exports=function(e,t,n,o){var r=e.__methods[t],i=r.chainingBehavior;r.chainingBehavior=function(){var e=o(i).call(this);return void 0===e?this:e};var s=r.method;r.method=function(){var e=n(s).apply(this,arguments);
return void 0===e?this:e}}},{}],25:[function(e,t){t.exports=function(e,t,n){var o=e[t],r=function(){return this};o&&"function"==typeof o&&(r=o),e[t]=function(){var e=n(r).apply(this,arguments);return void 0===e?this:e}}},{}],26:[function(e,t){t.exports=function(e,t,n){var o=Object.getOwnPropertyDescriptor(e,t),r=function(){};o&&"function"==typeof o.get&&(r=o.get),Object.defineProperty(e,t,{get:function(){var e=n(r).call(this);return void 0===e?this:e},configurable:!0})}},{}],27:[function(e,t){var n=e("./flag");t.exports=function(e,t){var o=n(e,"negate"),r=t[0];return o?!r:r}},{"./flag":12}],28:[function(e,t){t.exports=function(e,t,n){var o=e.__flags||(e.__flags=Object.create(null));t.__flags||(t.__flags=Object.create(null)),n=3===arguments.length?n:!0;for(var r in o)(n||"object"!==r&&"ssfi"!==r&&"message"!=r)&&(t.__flags[r]=o[r])}},{}],29:[function(e,t){function n(){function e(e,n){Object.keys(n).forEach(function(o){~t.indexOf(o)||(e[o]=n[o])})}var t=[].slice.call(arguments);return function(){for(var t=[].slice.call(arguments),n=0,o={};n<t.length;n++)e(o,t[n]);return o}}function o(e,t,o){var r=n("name","message","stack","constructor","toJSON"),i=r(t||{});this.message=e||"Unspecified AssertionError",this.showDiff=!1;for(var s in i)this[s]=i[s];o=o||arguments.callee,o&&Error.captureStackTrace?Error.captureStackTrace(this,o):this.stack=(new Error).stack}t.exports=o,o.prototype=Object.create(Error.prototype),o.prototype.name="AssertionError",o.prototype.constructor=o,o.prototype.toJSON=function(e){var t=n("constructor","toJSON","stack"),o=t({name:this.name},this);return!1!==e&&this.stack&&(o.stack=this.stack),o}},{}],30:[function(e,t){t.exports=e("./lib/eql")},{"./lib/eql":31}],31:[function(e,t){function n(e,t,n){return o(e,t)?!0:"date"===l(e)?i(e,t):"regexp"===l(e)?s(e,t):d.isBuffer(e)?u(e,t):"arguments"===l(e)?a(e,t,n):r(e,t)?"object"!==l(e)&&"object"!==l(t)&&"array"!==l(e)&&"array"!==l(t)?o(e,t):p(e,t,n):!1}function o(e,t){return e===t?0!==e||1/e===1/t:e!==e&&t!==t}function r(e,t){return l(e)===l(t)}function i(e,t){return"date"!==l(t)?!1:o(e.getTime(),t.getTime())}function s(e,t){return"regexp"!==l(t)?!1:o(e.toString(),t.toString())}function a(e,t,o){return"arguments"!==l(t)?!1:(e=[].slice.call(e),t=[].slice.call(t),n(e,t,o))}function c(e){var t=[];for(var n in e)t.push(n);return t}function h(e,t){if(e.length!==t.length)return!1;for(var n=0,o=!0;n<e.length;n++)if(e[n]!==t[n]){o=!1;break}return o}function u(e,t){return d.isBuffer(t)?h(e,t):!1}function f(e){return null!==e&&void 0!==e}function p(e,t,o){if(!f(e)||!f(t))return!1;if(e.prototype!==t.prototype)return!1;var r;if(o){for(r=0;r<o.length;r++)if(o[r][0]===e&&o[r][1]===t||o[r][0]===t&&o[r][1]===e)return!0}else o=[];try{var i=c(e),s=c(t)}catch(a){return!1}if(i.sort(),s.sort(),!h(i,s))return!1;o.push([e,t]);var u;for(r=i.length-1;r>=0;r--)if(u=i[r],!n(e[u],t[u],o))return!1;return!0}var d,l=e("type-detect");try{d=e("buffer").Buffer}catch(g){d={},d.isBuffer=function(){return!1}}t.exports=n},{buffer:void 0,"type-detect":32}],32:[function(e,t){t.exports=e("./lib/type")},{"./lib/type":33}],33:[function(e,t,n){function o(e){var t=Object.prototype.toString.call(e);return i[t]?i[t]:null===e?"null":void 0===e?"undefined":e===Object(e)?"object":typeof e}function r(){this.tests={}}var n=t.exports=o,i={"[object Array]":"array","[object RegExp]":"regexp","[object Function]":"function","[object Arguments]":"arguments","[object Date]":"date"};n.Library=r,r.prototype.of=o,r.prototype.define=function(e,t){return 1===arguments.length?this.tests[e]:(this.tests[e]=t,this)},r.prototype.test=function(e,t){if(t===o(e))return!0;var n=this.tests[t];if(n&&"regexp"===o(n))return n.test(e);if(n&&"function"===o(n))return n(e);throw new ReferenceError('Type test "'+t+'" not defined or invalid.')}},{}],34:[function(e,t,n){arguments[4][32][0].apply(n,arguments)},{"./lib/type":35,dup:32}],35:[function(e,t,n){function o(e){var t=Object.prototype.toString.call(e).match(i)[1].toLowerCase();return"function"==typeof Promise&&e instanceof Promise?"promise":null===e?"null":void 0===e?"undefined":t}function r(){return this instanceof r?void(this.tests={}):new r}var n=t.exports=o,i=/^\[object (.*)\]$/;n.Library=r,r.prototype.of=o,r.prototype.define=function(e,t){return 1===arguments.length?this.tests[e]:(this.tests[e]=t,this)},r.prototype.test=function(e,t){if(t===o(e))return!0;var n=this.tests[t];if(n&&"regexp"===o(n))return n.test(e);if(n&&"function"===o(n))return n(e);throw new ReferenceError('Type test "'+t+'" not defined or invalid.')}},{}]},{},[1])(1)});