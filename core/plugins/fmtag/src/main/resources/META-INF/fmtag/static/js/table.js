
var Common = function () {
};

(function ($) {
	var loadHtml = '<div id="loadingModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-body"><p>正在加载，请稍候……</p></div></div>';
	//var progress = '<div class="progress progress-striped active"><div class="bar" style="width: 40%;"></div></div>';

	var jfish = {};
	jfish.asyncCallbackManager = {};
	jfish._blockMsgState = false;
	
	$.extend({
		showMessageOn : function(ele, message, cb){
			var msgDIV = $("<div style='text-align:center;color:red;display:none'><b>"+message+"</b></div>");
			msgDIV.insertBefore(ele);
			msgDIV.show("slow", function(){
				if(cb) cb(msgDIV); else msgDIV.hideIn("slow", 2000);
			});
		},
		
		showTipsWindow: function(message, title){
			var modalWin = $('#showTipsModal')
			if(modalWin.length==0){
				alert(message);
				return ;
			}
			if(!title)
				title = "提示";
			modalWin.find('.modal-title').html(title);
			modalWin.find('.modal-body').html(message);
			modalWin.modal({keyboard: false, backdrop: 'static'});
			modalWin.modal('show');
		},
		
		closeTipsWindow: function(){
			var modalWin = $('#showTipsModal');
			modalWin.modal('toggle');
		},
		
		showBlockMsg: function(message){
			if(jfish._blockMsgState==true)
				return ;
			var loadDiv = $('#loadingModal');
			if(loadDiv.length==0){
				loadDiv = $(loadHtml);
				loadDiv.appendTo(document.body);
				loadDiv.modal({keyboard: false, backdrop: 'static'});
			}
			if(message)
				loadDiv.find('.modal-body').html(message);
			loadDiv.modal('show');
			jfish._blockMsgState = true;
		},
		closeBlockMsg: function(){
			var loadDiv = $('#loadingModal');
			if(loadDiv)
				loadDiv.modal('hide');
			jfish._blockMsgState = false;
		}
		
	});
	
	if(AjaxAnywhere){
		AjaxAnywhere.prototype.hideLoadingMessage = function() {
			$.closeBlockMsg()
		};
		AjaxAnywhere.prototype.showLoadingMessage = function() {
			$.showBlockMsg()
		};
		AjaxAnywhere.prototype.handlePrevousRequestAborted = function() {
			$.showTipsWindow('请求正在加载中，请勿频繁操作！')
		};
	}
	
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
			linkButton: "a[data-method][class!='dg-toolbar-button-delete'][class!='dg-toolbar-button-batch']",
			formButton : ".form-button",
			
			checkAll : ".dg-checkbox-all",
			checkField : ".dg-checkbox-field",
			toolbarButtonDelete : ".dg-toolbar-button-delete",
			toolbarBatchButton : ".dg-toolbar-button-batch"
		},
		
		handleFormFunc : function(form, restMethod, unValidateCheckbox){
			return function(){
				form = form || $(this).parents("form:first");
				
				var nocheckbox = $(this).attr('data-nocheckbox') || unValidateCheckbox || false;
				if(!nocheckbox){
					var checkfields = $(jfish.cssKeys.checkField, form);
					var values = $(form).checkedValues();
//	    			var table = $(form).children("table:first");
					if(!values || values.length==0){
						$.showTipsWindow("请先选择数据！");
//						alert("请先选择数据！");
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
				
				var dataAction = $(this).dataAction();
				if(dataAction){
					$(form).attr("action", dataAction);
				}

				var amethod = $(this).attr('data-method');
				if(!!!amethod && restMethod){//if no data-method
					amethod = restMethod;
				}
				if(!!amethod){//true
					jfish.appendHiddenMethodParamIfNecessary(form, amethod);
//					$(form).attr("method", "post")
					if(amethod!='get')
						$(form).attr("method", "post")
					else
						$(form).attr("method", "get")
				}

				jfish.appendHiddenByDataParams(this, form);
				
				var remote = eval($(this).attr('remote'));
				if(remote && remote==true){
					var ajaxName = $(this).attr('ajaxName');
					if(!ajaxName){
						alert("不支持ajax请求！");
						return false;
					}
					ajaxAnywhere.getZonesToReload = function() {
						return ajaxName;
					}
					ajaxAnywhere.formName=$(form).attr('name') || $(form).attr('id');
					ajaxAnywhere.submitAJAX();
					return false;
				}else{
					$(form).submit();
					return false;
				}
			}
		},
		
		appendAsInputHidden : function(form, paramsData){
			var params = eval("("+paramsData+")");
			params_input = "";
			if(params){
				for(var p in params){
					var hiddenField = $(form).find("#"+p);
					if(hiddenField.length>0){
						hiddenField.val(params[p]);
					}else{
						$(form).append('<input id="'+p+'" name="'+p+'" value="'+params[p]+'" type="hidden" />');
					}
				}
			}
		},
		
		appendHiddenByDataParams : function(e, form){
			var params = $(e).attr("data-params");
			if(!!params){
				var params_input = jfish.appendAsInputHidden($(form), params);
//				$(form).append(params_input);
			}
		},
		
		appendHiddenMethodParamIfNecessary: function(form, method){
			var methodEle = $('input[name=_method]', $(form));
			if(methodEle.length>0){
				methodEle.val(method);
			}else{
				var metadata_input = '<input name="_method" value="'+method+'" type="hidden" />';
				$(form).append(metadata_input);
			}
		},
		
		showAjaxMsg : function(data){
			if(jfish._blockMsgState===true)
				$.closeBlockMsg();
			if(data.success && data.success==true){
				$.showTipsWindow(data.message);
			}else{
				$.showTipsWindow(data.message, '错误');
			}
		},
		
		init : function(){
			$(jfish.cssKeys.linkButton).live("click", function(){
				if($(this).is(jfish.cssKeys.formButton))
					return false;
				var confirmMsg = $(this).attr('data-confirm')
				if(confirmMsg && confirmMsg!="false"){
					if(!confirm(confirmMsg))
						return false;
				}
				jfish.handleMethod($(this));
				return false;
			});
			
			$(jfish.cssKeys.formButton).live("click", jfish.handleFormFunc(null, null, true));
			
			$('.jfish-toggle-control').live("click", function(){
				var $this = $(this);
				var parent = $this.parents('.jfish-container');
				var body = parent.find('.jfish-toggle-body');
				if($this.attr('control')=='0'){
					body.fadeToggle();
					$this.html('隐藏');
					$this.attr('control', '1');
				}else{
					body.fadeToggle();
					$this.html('显示');
					$this.attr('control', '0');
				}
			});
			
		},
		
		initAfterPage: function(){
			var initBtn = function(){
				$(this).button('loading');//$(this).button('reset')
			};
			$('button[data-loading-text]').click(initBtn);
			$('input[type=submit][data-loading-text]').click(initBtn);
			$('.data-board').each(function(){
				var name = $(this).attr('name');
				var url = $(this).attr('data-url');
				
			});

			
		},
		
		setCheckboxEvent: function(formName){
			var form = $(formName) || $(this);
			
			var checkboxAll = $(jfish.cssKeys.checkAll, form);
			var childBox = $(jfish.cssKeys.checkField, form);//$("input[type='checkbox'][id!='"+checkboxAll.attr("id")+"']", form);
			checkboxAll.live('click', function(){
				$(jfish.cssKeys.checkField, form).attr("checked", !!$(this).attr("checked"));
			});
			childBox.live('click', function(){
				var checkeds = $("input[type='checkbox'][id!='"+checkboxAll.attr("id")+"'][checked]", form);
				checkboxAll.attr("checked", (checkeds.length==$(jfish.cssKeys.checkField, form).length));
			});
		},
		
		handleMethod : function(link) {
			var href = link.attr('href');
			href = encodeURI(href);
			var method = link.attr('data-method') || 'get';
			var target = link.attr('target');
			
			var remote = eval($(link).attr('remote'));
			var ajaxInst = null;
			var form = null;
			if(remote && remote==true){
				var ajaxName = $(link).attr('ajaxName');
				//ajaxInst = AjaxAnywhere.findInstance(ajaxName);
				ajaxInst = ajaxAnywhere;
				if(!ajaxName || !ajaxInst){
					alert("不支持ajax请求");
					return false;
				}

				ajaxAnywhere.getZonesToReload = function() {
					return ajaxName;
				}
				if(method && method.toLowerCase()=='get'){
					ajaxInst.getAJAX(href);
					return false;
				}
				//var formId = ajaxInst.formName;
				//form = $('#'+formId);
			}
			
			if(!form){
				form = $(link).parents("form:first");
			}

			var metadata_input = "";
			if(link.attr('data-form')){
				form = $(link.attr('#'+data-form));
				form.attr("action", href);
				form.attr("method", method);
			}
			else if(form.length>0){
				form = $(form.get(0));
				form.attr("action", href);
				form.attr("method", method);
			}
			else{
				var formId = '_linkForm';
				form = $('#'+formId);
				if(form.length==0){
					form = $('<form id="'+formId+'" name="'+formId+'" method="post" action="'+ href + '"></form>');
					metadata_input = '<input name="_method" value="'+ method + '" type="hidden" />';
					form.appendTo('body');
				}
			}

			jfish.appendHiddenMethodParamIfNecessary(form, method);
			jfish.appendHiddenByDataParams(link, form);

			if (target) {
				form.attr('target', target);
			}
			
			if(ajaxInst){
				ajaxInst.formName=form.attr('id') || form.attr('name');
				ajaxInst.submitAJAX();
				return false;
			}else{
				var params = $(link).children(".link_params").html();
				if(params){
					form.append(params);
				}
//				jfish.appendHiddenByDataParams(link, form);
				$(form).submit();
			}
			
			return false;
		},
		

		selecter : function(config){
			return new jfish._selecter(config);
		},
		
		_selecter : function(config){
			var _this = this;
			var aconfig = config || {};
			this.config = aconfig;
			this.selectEl = $('#'+config['el']);
			this.actived = this.selectEl.length>0;
//			this.type="selecter";

			/*if(!$.nodeName(this.parent[0], "select")){
				throw "element must be a select!"
			}*/
			var data_key = aconfig['dataKey'];
			var data_value = aconfig['dataValue'];
			this.activedInst = function(){return _this.actived?_this:null};
			

			_this.appendTs = function(config, url){
				var appendTs = true;
				if(config['appendTs']!=undefined){
					appendTs = config['appendTs'];
				}
				if(appendTs===true){
					if(url.indexOf('?')==-1){
						url = url + "?ts="+new Date().getTime();
					}else{
						url = url + "&ts="+new Date().getTime();
					}
				}
				return url;
			};
			
			this.loadDatas = function(ds){
				if(!_this.actived){
					alert("ajax selecter["+config.el+"] is not actived")
					return ;
				}
				$(_this.selectEl).empty();
				var emptyOption = aconfig['emptyOption'] || true;
				if(emptyOption){
					$(_this.selectEl).append("<option value=''>---请选择</option>");
				}
				
				var dataUrl;
				if(ds && ds.config){
					dataUrl = aconfig.loadUrl + ds.selectEl.val();
				}else if(typeof(ds)=="string"){
					dataUrl = ds;
				}else{
					dataUrl = aconfig.loadUrl;
				}
				
				dataUrl = _this.appendTs(aconfig, dataUrl);
				dataUrl = encodeURI(dataUrl);//encodeURIComponent(dataUrl);
				
				$.getJSON(dataUrl, {}, function(json){
					if(json.length<1){
						alert("没有数据！");
						return ;
					}
					for(var i=0; i<json.length; i++){
						var data = json[i];
						$(_this.selectEl).append("<option value='"+data[data_value]+"'>"+data[data_key]+"</option>");
					}
					if(aconfig.checkedValue){
						_this.selectEl.val(aconfig.checkedValue);
						_this.selectEl.trigger('change');
					}
				});
			};
			
			this.clearOptions = function(){
				if(_this.actived){
					_this.selectEl.empty();
				}
				if(_this.notifiedSelecter && _this.notifiedSelecter.actived){
					_this.notifiedSelecter.clearOptions();
				}
			};
			
			this.notifyTo = function(selecter){
				_this.notifiedSelecter = selecter;
				$(_this.selectEl).change(function(evt){
					selecter.clearOptions();
					selecter.loadDatas(_this);
				});
				return _this;
			};

			return this;
		},
		
		ajaxSelecter : function(config){
			return new jfish._ajaxSelecter(config);
		},
		
		_ajaxSelecter : function(config){
			var _this = this;
			var aconfig = config || {};
			this.config = aconfig;
			this.selectEl = $('#'+config['el']);
			this.actived = this.selectEl.length>0;

			/*if(!$.nodeName(this.parent[0], "select")){
				throw "element must be a select!"
			}*/
			var data_key = aconfig['dataKey'];
			var data_value = aconfig['dataValue'];
			_this.activedInst = function(){return _this.actived?_this:null}
			
			_this.appendTs = function(config, url){
				var appendTs = true;
				if(config['appendTs']!=undefined){
					appendTs = config['appendTs'];
				}
				if(appendTs===true){
					if(url.indexOf('?')==-1){
						url = url + "?ts="+new Date().getTime();
					}else{
						url = url + "&ts="+new Date().getTime();
					}
				}
				return url;
			};
			
			_this.notify_to = function(childConfig){
				var childConfig = childConfig || {};
				var alterMsg = childConfig['alterIfNoDatas'];
				$(_this.selectEl).change(function(evt, cb){
					if(!_this.actived){
						alert("ajax selecter["+config.el+"] is not actived")
						return ;
					}
					
					if(!childConfig.el){
						throw "notify select element has not defined";
					}
					var select = $('#'+childConfig.el);
					if(select.length==0 || !$(this).val())
						return;
					select.empty();
					select.attr("disabled", "true");
					
					var url = (childConfig.url+$(_this.selectEl).val()) || alert("notify url can not blank!");
					var params = childConfig.params || {};
					var pre_datas = childConfig.options;
					var emptyOption = childConfig.emptyOption || true;
					

					url = _this.appendTs(childConfig, url);
					$.getJSON(url, params, function(json){
						select.removeAttr("disabled");

						if(emptyOption){
							$(select).append("<option value=''>---请选择</option>");
						}
						if(pre_datas && pre_datas.length && pre_datas.length>0){
							for(var i=0; i<pre_datas.length; i++){
								predata = pre_datas[i];
								$(select).append("<option value='"+predata[data_value]+"'>"+predata[data_key]+"</option>");
							}
						}

						if(json.length<1 && alterMsg){
							alert(alterMsg);
							return ;
						}
						
						var data;
						for(var i=0; i<json.length; i++){
							data = json[i];
							$(select).append("<option value='"+data[data_value]+"'>"+data[data_key]+"</option>");
						}
//						select.trigger('change');

						if(aconfig.checkedValue){
							_this.selectEl.val(aconfig.checkedValue);
							_this.selectEl.trigger('change');
						}
						
						if(cb)
							cb();
					});
				});
				
				_this.notifyTo = _this.notify_to;
				
				return _this;
			};

			_this.loadDatas = function(loadUrl){
				if(!_this.actived){
					alert("ajax selecter["+config.el+"] is not actived")
					return ;
				}
				var emptyOption = aconfig['emptyOption'] || true;
				if(emptyOption){
					$(_this.selectEl).append("<option value=''>---请选择</option>");
				}
				
				loadUrl = _this.appendTs(config, loadUrl);
				$.getJSON(loadUrl, {}, function(json){
					if(json.length<1){
						alert("没有数据！");
						return ;
					}
					for(var i=0; i<json.length; i++){
						var data = json[i];
						$(_this.selectEl).append("<option value='"+data[data_value]+"'>"+data[data_key]+"</option>");
					}
					if(aconfig.checkedValue){
						_this.selectEl.val(aconfig.checkedValue);
						_this.selectEl.trigger('change');
					}
				});
			} 
			
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
		
		asynSubmitForm : function(frm, tips){
			var btn = $(this);
			var aform = $(frm);
			$(this).live('click', function(){
				var msg = tips || '确定要提交此操作？';
				if(!confirm(msg)){
					return false;
				}
				
				btn.attr('disabled', true);
				var txt = btn.text();
				btn.text('正在处理……');
				
				var processBar = aform.find('.asyn-process-bar');
				if(processBar.length==0){
					processBar = $('<div class="asyn-process-bar alert" style="display: none" ><button type="button" class="close" data-dismiss="alert">&times;</button><div class="process-msg alert alert-success">正在处理…… </div> <div class="progress progress-striped active"> <div class="bar"></div> </div> <div class="info-msg " style="height: 300px;width: 100%; overflow: auto" > </div> </div>');
					aform.append(processBar);
				}
				var infoMsg = processBar.find('.info-msg');
				infoMsg.html('');
				var processMsg = processBar.find('.process-msg');
				
				processBar.fadeIn('slow');
				
				aform.asynSubmit(function(msg, percent, type){
					if(type=='INFO'){
						infoMsg.append('<p>'+msg+"</p>");
						infoMsg.get(0).scrollTop = infoMsg.get(0).scrollHeight;
					}else{
						processMsg.html(msg);
						$('.bar').css({width: percent+'%'});
						if(type=='FINISHED'){
							btn.removeAttr('disabled');
							btn.text(txt);
						}
					}
				});
				return false;
			});
		},
		
		asynSubmit : function(cb){
			if(!$.nodeName(this[0], "form")){
				alert('it must be a form');
				return false;
			}
			if(!cb){
				alert('callback can not null');
			}
			var ifrmId = $(this).attr('target') || '_ifmProcess';
			var ifrm = $('iframe[name'+ifrmId+']');
			var cbName = ifrmId+'Callback';
			if(ifrm.length==0){
				ifrm = $('<iframe id="'+ifrmId+'" name="'+ifrmId+'" style="display:none;"></iframe>');
				ifrm.appendTo('body');
			}
			var cbField = $(this).find('input[name="asynCallback"]');
			if(cbField.length==0){
				cbField = $('<input name="asynCallback" type="hidden" value="'+cbName+'"/>');
				$(this).append(cbField);
			}
			jfish.asyncCallbackManager = jfish.asyncCallbackManager || {};
			jfish.asyncCallbackManager[cbName] = cb;
			$(this).attr('target', ifrmId);
			
			
			$(this).submit();
			return false;
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
		
		checkedValues : function(reserved){
			var checkfields = $(jfish.cssKeys.checkField, this);
			var values = [];
			var getreserved = reserved || false; 
			if(getreserved){
				checkfields.each(function(){
					if(!$(this).attr("checked"))
						return ;
					var val = $(this).val();
					
					if(val){
						values.push({reserved:$(this).attr(reserved), 'value':val});
					}
				});
			}else{
				checkfields.each(function(){
					if(!$(this).attr("checked"))
						return ;
					var val = $(this).val();
					if(val) values.push(val);
				});
			}
			return values;
		},
		/*checkboxValues : function(returnObject){
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
		},*/
		onCenter : function(){
			var t = ($(window).height()+$(window).scrollTop()-$(this).height())/2;
			var l = ($(window).width()-$(this).width())/2;
//			alert("t:"+t+" l:"+l);
//			$(this).offset({top: t, left: l});
			//$(this).css({top: t+"px", left: l+"px"});
			$(this).offset({top: t, left: l});
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

