define(function(){if(!Function.prototype.bind){Function.prototype.bind=function(a){if(typeof this!=="function"){throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable")}var e=Array.prototype.slice.call(arguments,1),d=this,b=function(){},c=function(){return d.apply(this instanceof b?this:a,e.concat(Array.prototype.slice.call(arguments)))};b.prototype=this.prototype;c.prototype=new b();return c}}if(!String.prototype.startsWith){Object.defineProperty(String.prototype,"startsWith",{enumerable:false,configurable:false,writable:false,value:function(b,a){a=a||0;return this.lastIndexOf(b,a)===a}})}});