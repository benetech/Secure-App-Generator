define(["require","module","underscore","jquery","vellum/util","tpl!vellum/templates/multimedia_modal","tpl!vellum/templates/multimedia_upload_trigger","text!vellum/templates/multimedia_queue.html","text!vellum/templates/multimedia_errors.html","text!vellum/templates/multimedia_existing_image.html","text!vellum/templates/multimedia_existing_audio.html","text!vellum/templates/multimedia_existing_video.html","text!vellum/templates/multimedia_existing_text.html","tpl!vellum/templates/multimedia_nomedia","text!vellum/templates/multimedia_block.html","vellum/core"],function(q,c,x,i,a,r,v,m,t,y,p,h,f,s,w){var o={image:[{description:"Image",extensions:"*.jpg;*.png;*.gif"}],audio:[{description:"Audio",extensions:"*.mp3;*.wav"}],video:[{description:"Video",extensions:"*.3gp;*.mp4"}],text:[{description:"HTML",extensions:"*.html"}],},n={image:y,audio:p,video:h,text:f,},k={image:"CommCareImage",audio:"CommCareAudio",video:"CommCareVideo",text:"CommCareMultimedia",},g={image:"fd_hqimage",audio:"fd_hqaudio",video:"fd_hqvideo",text:"fd_hqtext",};var d=function(z,B,C){var A={};A.mediaType=z;A.updateRef=function(D){A.path=D;A.linkedObj=B[D]};A.isMediaMatched=function(){return x.isObject(A.linkedObj)};A.getUrl=function(){return A.linkedObj.url};A.updateController=function(){var D=C[A.mediaType].value;D.resetUploader();D.currentReference=A;D.uploadParams={path:A.path,media_type:k[A.mediaType],old_ref:(A.isMediaMatched())?A.linkedObj.m_id:"",replace_attachment:true};D.updateUploadFormUI()};return A};var e=function(E,z,B){E.mediaRef=d(E.form,z,B);var C=E.getItextValue||E.getValue,G=E.getControl(),H=i("<div />"),A=E.getUIElement,D=i("<div />").addClass("fd-mm-preview-container"),F=E.mug.form.vellum.data.javaRosa.ICONS;E.getUIElement=function(){H=A();var I=H.find(".controls").not(".messages"),J=i("<div />").addClass("fd-mm-upload-container");I.empty().addClass("control-row").data("form",E.form);E.updateReference();D.html(b(E,z,F));I.append(D);J.html(w);J.find(".fd-mm-upload-trigger").append(u(E,z));J.find(".fd-mm-path-input").append(G);J.find(".fd-mm-path-show").click(function(L){var K=i(this);K.addClass("hide");J.find(".fd-mm-path").removeClass("hide");L.preventDefault()});J.find(".fd-mm-path-hide").click(function(L){var K=i(this);K.parent().addClass("hide");J.find(".fd-mm-path-show").removeClass("hide");L.preventDefault()});G.bind("change keyup",function(){E.updateMultimediaBlockUI(z)});H.on("mediaUploadComplete",function(K,L){E.handleUploadComplete(K,L,z)});I.append(J);G.bind("change keyup",E.updateValue);return H};E.handleUploadComplete=function(L,M,N){if(M.ref&&M.ref.path){var I="."+M.ref.path.split(".").pop().toLowerCase(),K="."+C().split(".").pop().toLowerCase();if(I!==K){var J=C().replace(/\.[^/.]+$/,I);E.getControl().val(J);E.handleChange()}N[M.ref.path]=M.ref}E.updateMultimediaBlockUI(N)};E.updateMultimediaBlockUI=function(I){D.html(b(E,I,F)).find(".existing-media").tooltip();H.find(".fd-mm-upload-trigger").empty().append(u(E,I));E.updateReference()};E.updateReference=function(){var I=C();H.attr("data-hqmediapath",I);E.mediaRef.updateRef(I)}};var b=function(E,F,C){var B=x.isFunction(E.getItextValue),D=B?E.getItextValue():E.getValue(),A;if(!B&&!D&&!E.isDefaultLang){D=E.getItextItem().get(E.form,E.defaultLang)}if(D in F){var z=F[D];A=x.template(n[E.form])({url:z.url})}else{A=s({iconClass:C[E.form]})}return A};var u=function(B,C){var A=B.getItextValue?B.getItextValue():B.getValue(),z;z=i(v({multimediaExists:A in C,uploaderId:g[B.form],mediaType:o[B.form][0].description}));z.click(function(){B.mediaRef.updateController()});return z};var l=c.uri.split("/"),j=l.slice(0,l.length-1).join("/")+"/";i.vellum.plugin("uploader",{objectMap:false,sessionid:false,uploadUrls:{image:false,audio:false,video:false,text:false},},{init:function(){var z=this.opts().uploader,C=z.uploadUrls,D=z.objectMap&&z.uploadUrls&&z.uploadUrls.image,A=z.sessionid,B=j+"../bower_components/MediaUploader/flashuploader.swf";this.data.uploader.uploadEnabled=D;this.data.uploader.objectMap=z.objectMap;if(!D){return}this.data.deferredInit=function(){this.data.uploader.uploadControls={image:this.initUploadController({uploaderSlug:"fd_hqimage",mediaType:"image",sessionid:A,uploadUrl:C.image,swfUrl:B}),audio:this.initUploadController({uploaderSlug:"fd_hqaudio",mediaType:"audio",sessionid:A,uploadUrl:C.audio,swfUrl:B}),video:this.initUploadController({uploaderSlug:"fd_hqvideo",mediaType:"video",sessionid:A,uploadUrl:C.video,swfUrl:B}),text:this.initUploadController({uploaderSlug:"fd_hqtext",mediaType:"text",sessionid:A,uploadUrl:C.text,swfUrl:B})}}},initWidget:function(z){this.__callOld();if(!this.data.uploader.uploadEnabled){return}var A=this.data.deferredInit;if(A!==null){this.data.deferredInit=null;A.apply(this)}e(z,this.data.uploader.objectMap,this.data.uploader.uploadControls)},initUploadController:function(B){var A=i(r({mediaType:B.mediaType,modalId:B.uploaderSlug}));this.$f.find(".fd-multimedia-modal-container").append(A);var z={value:null};q(["file-uploader"],function(C){if(z.value!==null){return}z.value=new C(B.uploaderSlug,B.mediaType,{fileFilters:o[B.mediaType],uploadURL:B.uploadUrl,swfURL:B.swfUrl,isMultiFileUpload:false,queueTemplate:m,errorsTemplate:t,existingFileTemplate:n[B.mediaType],licensingParams:["shared","license","author","attribution-notes"],uploadParams:{},sessionid:B.sessionid});z.value.init()});return z},destroy:function(){x.each(this.data.uploader.uploadControls,function(A,z){if(A.value){A.value.uploader.destroy()}delete A.value})}})});