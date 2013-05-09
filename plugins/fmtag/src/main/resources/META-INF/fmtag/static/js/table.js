
var Common = function () {
};

(function ($) {
	
	$.extend({
		showMessageOn : function(ele, message, cb){
			var msgDIV = $("<div style='text-align:center;color:red;display:none'><b>"+message+"</b></div>");
			msgDIV.insertBefore(ele);
			msgDIV.show("slow", function(){
				if(cb) cb(msgDIV); else msgDIV.hideIn("slow", 2000);
			});
		}
	});
	
	var jfish;
	$.jfish = jfish = {
		state : {
			failed : 0,
			succeed : 1
		},
		cssKeys : {
			//
			toolbarButton : ".dg-toolbar-button",
			buttonDelete : ".dg-button-delete",
			
			//
			linkButton: "a[data-method][class!='dg-toolbar-button-delete']",
			formButton : ".form-button",
			
			checkAll : ".dg-checkbox-all",
			checkField : ".dg-checkbox-field",
			toolbarButtonDelete : ".dg-toolbar-button-delete",
			toolbarBatchButton : ".dg-toolbar-button-batch"
		},
		
		handleFormFunc : function(form, restMethod, type){
			return function(){

				form = form || $(this).parents("form:first")[0];
				

				var checkfields = $(jfish.cssKeys.checkAll, form);
				if(checkfields.length>0){
					var values = $(form).checkboxValues(true);
	    			var table = $(form).children("table:first");
					if(!values || values.length==0){
						if(table.length==1){
							$.showMessageOn(table, "请先选择数据！");
						}else{
							alert("请先选择数据！");
						}
						return false;
					}
				}
				//if(confirm("确定删除选中的？")){
					//return true;
				//}
				var confirmMsg = $(this).attr('data-confirm') || "确定要执行此操作？";
				if(confirmMsg!="false"){
					if(!confirm(confirmMsg))
						return false;
				}
				
				$(form).attr("method", "post")
				var dataAction = $(this).dataAction();
				if(dataAction){
					$(form).attr("action", dataAction);
				}

				var amethod = $(this).attr('data-method');
				if(!!!amethod && restMethod){//if no data-method
					amethod = restMethod;
				}
				if(!!amethod){//true
					var methodEle = $('input[name="_method"]', $(form));
					if(methodEle){
						methodEle.val(amethod);
					}else{
						metadata_input = '<input name="_method" value="'+amethod+'" type="hidden" />';
						$(form).append(metadata_input);
					}
				}

				var params = eval("("+$(this).attr("data-params")+")");
				if(params){
					params_input = "";
					for(var p in params){
						params_input += '<input name="'+p+'" value="'+params[p]+'" type="hidden" />';
					}
					$(form).append(params_input);
				}
				$(form).submit();
				return false;
			}
		},
		
		toInputHidden : function(params){
			var params = eval("("+params+")");
			params_input = "";
			if(params){
				for(var p in params){
					params_input += '<input name="'+p+'" value="'+params[p]+'" type="hidden" />';
				}
			}
			return params_input;
		},
		
		appendHiddenByDataParams : function(e, form){
			var params = $(e).attr("data-params");
			if(!!params){
				var params_input = jfish.toInputHidden(params);
				$(form).append(params_input);
			}
		},
		
		init : function(){
			$(jfish.cssKeys.linkButton).live("click", function(){
				var confirmMsg = $(this).attr('data-confirm')
				if(confirmMsg && confirmMsg!="false"){
					if(!confirm(confirmMsg))
						return false;
				}
				jfish.handleMethod($(this));
				return false;
			});
			
			$(jfish.cssKeys.formButton).live("click", jfish.handleFormFunc());
			
		},
		
		setCheckboxEvent: function(formName){
			var form = $(formName) || $(this);
			
			var checkboxAll = $(jfish.cssKeys.checkAll, form);
			var childBox = $("input[type='checkbox'][id!='"+checkboxAll.attr("id")+"']", form);
			checkboxAll.click(function(){
				childBox.attr("checked", !!$(this).attr("checked"));
			});
			childBox.click(function(){
				var checkeds = $("input[type='checkbox'][id!='"+checkboxAll.attr("id")+"'][checked]", form);
				checkboxAll.attr("checked", (checkeds.length==childBox.length));
			});
		},
		
		handleMethod : function(link) {
			var href = link.attr('href');
			var method = link.attr('data-method');
			target = link.attr('target');
			var form = null;
			if(link.attr('data-form')){
				form = $(link.attr('#'+data-form));
			}else{
				form = $('<form method="post" action="'+ href + '"></form>');
				metadata_input = '<input name="_method" value="'+ method + '" type="hidden" />';
				form.appendTo('body');
			}
			
			var params = $(link).children(".link_params").html();
			if(params){
				metadata_input += params;
			}
			
			jfish.appendHiddenByDataParams(link, form);

			if (target) {
				form.attr('target', target);
			}
			form.hide().append(metadata_input);
			form.submit();
		}
		
	};

	jfish.init();

	$.extend($.fn, {
		
		
		initDatagrid : function(){
			$this = $(this);
//			var form = $(jfish.cssKeys.checkAll).parents("form:first");
			var form = $this;
			
			jfish.setCheckboxEvent(form);
			
			
			//批量删除按钮
			$(jfish.cssKeys.toolbarButtonDelete, $(form)).click(jfish.handleFormFunc(form, "delete"));
			$(jfish.cssKeys.toolbarBatchButton, $(form)).click(jfish.handleFormFunc(form));
		},
			
		hideIn : function(s, t){
			var $this = $(this);
			setTimeout(function(){$this.hide(s, function(){$(this).remove()});}, t);
		},
		
		dataAction : function(actionUrl){
			if(!actionUrl){
				var url = jQuery.nodeName(this[0], "a")?$(this).attr("href"):$(this).attr("action");
				return url;
			}else{
				if($.nodeName(this[0], "a"))
					$(this).attr("href", actionUrl);
				else
					$(this).attr("action", actionUrl);
			}
		},
		
		doButtionAction : function(params, callback){
			var msg = $(this).attr("confirmMessage") || "确定要执行此操作？";
			if(!confirm(msg))
				return false;
			return $(this).doAction(params, callback);
		},
		
		doAction : function(params, callback){
			$this = $(this);
			$this.attr("disabled", "true");
			var action = $(this).dataAction();
			if(!action)
				alert("no action!");
			if(!callback && $.isFunction(params)) {callback = params; params = ""}
			params = ($(this).attr("params") || params || "");
			//params = $.param(params, true);
			params = $.param(params);
			
			jQuery.ajax({
				type: "post",
				url: action,
				data: params,
				success: function(data){
    				var message = data["message"];
    				var stateCode = data["message_code"] || jfish.state.failed;
    				$this.removeAttr("disabled");
    				if(callback){
    					callback(message, stateCode, data);
    				}else{
    					alert(message);
    				}
    			},
    			error : function (XMLHttpRequest, textStatus) {
				    alert("服务器忙碌："+textStatus);
    				$this.removeAttr("disabled");
				},
				dataType: "json"
			});
    		
    		if(jQuery.nodeName(this[0], "a")){
    			return false;
    		}
		},
		
		checkboxValues : function(returnObject){
			var checkfields = $(jfish.cssKeys.checkField, this);
			var values = [];
			checkfields.each(function(){
				if(!$(this).attr("checked"))
					return ;
				var val = $(this).val();
				if(val)
					values.push(val);
			});
			if(!returnObject)
				return values;
			if(values.length>0){
				var rs = {};
				rs[$(checkfields[0]).attr("name")] = values;
				return rs;
			}
			return null;
		},
		onCenter : function(){
			var t = ($(window).height()+$(window).scrollTop()-$(this).height())/2;
			var l = ($(window).width()-$(this).width())/2;
//			alert("t:"+t+" l:"+l);
//			$(this).offset({top: t, left: l});
			$(this).css({top: t+"px", left: l+"px"});
		},
		
		onFormCenter : function(loadE){
			var parentForm = loadE.parent();
			var t = parentForm.offset().top + (parentForm.height()/2) - (this.height()/2) - 8;
			var l = parentForm.offset().left + (parentForm.width()/2) - (this.width()/2);
			$(this).css({top: t+"px", left: l+"px"});
		}
		
	});
	
	$.extend(Common, {
		jumpPage:function (formId, pageNo, ajaxInst) {
			var form = $(formId);
			if(pageNo)
				form.find("#pageNo").val(pageNo);
			
			if(!this.isInt(form.find("#pageNo").val())){
				alert("请输入正确的页码！");
				return ;
			}
			this.submitForm(form, ajaxInst);
		}, 
		
		isInt : function(value){
			return /^\d+$/.test(value);
		},
		
		submitForm : function(form, ajaxInst){
			if(ajaxInst){
				var loadEId = varkeys.idKeys.loading_tips_pref + ajaxInst.id + varkeys.idKeys.loading_tips;
				var loadE = $(loadEId);
				if(loadE){
					loadE.onFormCenter(loadE);
					loadE.show("slow");
				}
				ajaxInst.submitAJAX();
			}else
				$(form).submit();
		},
		
		sort:function (orderBy, defaultOrder, formId, ajaxInst) {
			var form = $(formId);
			if (form.find("#orderBy").val() == orderBy) {
				if (form.find("#order").val() == "") {
					form.find("#order").val(defaultOrder);
				} else {
					if (form.find("#order").val() == "desc") {
						form.find("#order").val("asc");
					} else {
						if (form.find("#order").val() == "asc") {
							form.find("#order").val("desc");
						}
					}
				}
			} else {
				form.find("#orderBy").val(orderBy);
				form.find("#order").val(defaultOrder);
			}
			this.submitForm(form, ajaxInst);
		}, 
		
		search:function (formId, ajaxInst) {
			var form = $(formId || "#mainForm");
			form.find("#order").val("");
			form.find("#orderBy").val("");
			form.find("#pageNo").val("1");
			this.submitForm(formId, ajaxInst);
		}, 
		
		doAction : function(url, msg){
			if(msg){
				if($.trim(msg)=='')
					msg = '你确定要进行此操作？';
				if(!window.confirm(msg))
					return ;
			}
			location.href = url;
		},
		
		doDelete : function(url){
			this.doAction(url, "确定要删除记录？")
		},
		
		loadJs : function(jsfile){
			var head = $("head:first");
			$("<script></script>").attr({
				id:"loadScript",
				type : "text/javascript",
				src : jsfile
			}).appendTo(head);
		},
		
		loadCss : function(cssfile){
			var head = $("head:first");
			$("<link></link>").attr({
				rel: "stylesheet",
				type: "text/css",
				href: cssfile
			}).appendTo(head);
		},
		
		jumpPageAfter : function(time, url){
			var o = this;
			window.setTimeout(function(){
				if(time>0){
					jQuery('#time').html(time);
					time--;
					o.jumpPageAfter(time, url);
				}else{
					window.location.href=url;
				}
			}, 1000);
		}
	});
})(jQuery);

