var helper = function () {
};
helper.config = {
	baseUrl: ''
};
helper.page = {
};

(function($) {
	$.ajaxSetup({
		dataFilter: function(data, type){
			var rs = data;
			if(typeof rs === 'string'){
				rs = JSON.parse(rs);
			}
			if(rs.code && rs.code==='NOT_LOGIN'){
				$.messager.alert('提示！', rs.message, 'warning');
			}
			if(rs.error){
				console.log('错误提示:');
				console.dir(rs);
				$.messager.alert('错误提示！', rs.message, 'warning');
			}
			return data;
		},
		headers: {
			'x-response-view': 'easyui'
		},
		/*error: function (request, textStatus, errorThrown){
			alert(errorThrown)
			console.dir(request)
			console.dir(textStatus)
			console.dir(errorThrown)
		},*/
		complete: function(request, textStatus) {
			//textStatus : error
		    this; // 调用本次AJAX请求时传递的options参数
		    if(textStatus=='error'){
		    	console.log('error:');
		    	console.dir(this);
				$.messager.alert('提示！', '请求['+this.url+']出错！', 'warning');
		    }
		},
		statusCode: {
			500: function(){
				$.messager.alert('提示！', '服务器出错！', 'warning');
			},
			404: function(){
				$.messager.alert('提示！', '找不到路径!', 'warning');
			}
		}
	});

	//for $('e').extendMethod()
	$.extend($.fn, {
		
		isSelectedOne : function(){
            var selectedNodes = $(this).getGridSelections();
			return selectedNodes.length && selectedNodes.length==1;
		},
		
		getGridSelections: function(){
			return $(this).easyGrid('getSelections') || [];
		},
		
		easyGrid: function(dataName){
			var grid = null;
			if ($(this).data('treegrid')){
				grid = $(this).treegrid(dataName);
			} else {
				grid = $(this).datagrid(dataName);
			}
			return grid;
		},
		
		resetImg: function (maxWidth, maxHeight) {
	        var image = new Image();
	        var width, height;
	        $(this).each(function () {
	            image.src = $(this).attr('src');
	            width = image.width;
	            height = image.height;
	            if (width > height) {
	                $(this).css({width: maxWidth, height: 'auto'});
	            }
	            else if (width < height) {
	                $(this).css({width: 'auto', height: maxHeight});
	            }
	            else {
	                if (maxWidth > maxHeight) {
	                    $(this).css({width: maxWidth, height: 'auto'});
	                }
	                else {
	                    $(this).css({width: 'auto', height: maxHeight});
	                }
	            }
	        })
	    },

		getSelectedRow: function(){
		    var row = $(this).datagrid('getSelected');
		    return row;
		},
		
		selectRow: function(index){
			$(this).datagrid('selectRow',index);// 关键在这里  
		    var row = $(this).datagrid('getSelected');
		    return row;
		},
		
		kindeditor: function(opts){
			var config = opts || {
				   items: ['source', '|', 'undo', 'redo', '|', 'preview','|', 
				           'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', '|', 
				           'formatblock', 'forecolor', 'bold', 'image'
				          ],
		           uploadJson : '${siteConfig.baseURL}/web-admin/kindeditor/upload',
		           fileManagerJson : '',
		           allowFileManager : false
			   };
			var e = $(this).get();
			KindEditor.ready(function(K) {
			   window.editor = K.create(e, config);
			});
		}
		
	});
	
	$.extend($.fn.tree.methods, {
		getAllCheckedIds : function(jq){
			var ids = [];
			var nodes = $(jq).tree('getChecked', ['checked','indeterminate']);
			$.each(nodes, function(i, node){
				ids.push(node.id);
			});
			return ids;
		}
	});
	
	//for $.extendMethod()
	$.extend({
		
	});

	//for helper.extendMethod()
	helper.waitingMsgState = false;
	$.extend(helper, {
		/***
		 * config {
		 *  datagrid: '#dataGrid',
        	resetForm: '#dataForm',
        	closeDialog: '#addDataDialog',
        	callback: function(data){}
        }
		 */
		ajaxPut: function(url, config){
			config = config || {};
			var mergeParams = config.params || {};
			mergeParams['_method'] = 'put';
			helper.showWaitingMsg(config.waitingMsg);
			$.post(url, mergeParams, helper.processJsonDataResult(config));
		},
		
		gotoTopPage: function(){
			var current = window;
        	var url = location.href;
        	var hasParent = false;
        	while(current.parent!=null && current.parent!==current){
        		current = current.parent;
        		hasParent = true;
        	}
        	if(hasParent){
	        	current.location.href = url;
        	}
		},
		
		booleanFormatter: function(val, row, index){
			if(val===true || val=='true'){
				return '是';
			}else{
				return '否';
			}
		},
		
		addEmptyOptionForComboboxFilter: function(data){
			data.unshift({text:'全部', value:'', selected:true});
            return data;
		},
		
		defaultOnError: function(defResult){
			return function(data){
            	if(data.error && data.error===true){
            		return defResult || [];
            	}
            	return data;
            }
		},
		
		submitEasyForm: function(config){
	    	 var _config = $.extend({}, {
	    		 autoClear: true,
	    		 onSuccess: function(data){
	    			 if(data.success===true && !!_config.autoClear){
	                     $(_config.dataForm).form('reset');
	                     if(_config.dataDialog)
	                    	 $(_config.dataDialog).dialog('close');

	                     $(_config.datagrid || _config.treegrid).easyGrid('clearSelections');
	                     $(_config.datagrid || _config.treegrid).easyGrid('reload');
	    			 }
	    		 }
	    	 }, config);
	         
	    	 $(_config.dataForm).form('submit',{
	    		 url: $(_config.dataForm).attr('action'),
	             onSubmit:function(){
	                 var valid = $(this).form('enableValidation').form('validate');
	                 if(valid==true){
	        	         helper.showWaitingMsg();
	                 }
	                 return valid;
	             },
	             success: function(data){
//                	 $.messager.progress('close');
	            	 helper.closeWaitingMsg();
	                 data = JSON.parse(data);
	                 if(!!!data.success){
	                	 $.messager.alert('提交操作出错！',data.message,'warning');
	                 }else{
	                     $.messager.alert('提交操作成功！',data.message,'info');
	                 }
	                 if(_config.onSuccess){
                    	 _config.onSuccess(data);
                     }
	             }
	         });
	     },
	     
	     showWaitingMsg : function(msg){
	    	 var _msg = msg || '正在处理，请稍候……';
	    	 $.messager.progress({
	             title:'提示',
	             msg:_msg
	         });
	    	 helper.waitingMsgState = true;
	     },
	     
	     closeWaitingMsg : function(){
        	 $.messager.progress('close');
	    	 helper.waitingMsgState = false;
	     },
	     
	     selectOneHandler : function(datagrid, cb){
	    	 if(arguments.length==1 && typeof arguments[0] ==='object'){
	    		 var config = arguments[0];
	    		 return this.selectOneHandler(config.datagrid, function(row){
	    			 $.messager.confirm('提示', '确定要进行此操作？', function(rs){
	                     if (rs){
	                    	var params = (typeof config.params === 'object' && config.params) || (typeof config.params === 'function' && config.params(row)) || {};
	                    	$.extend(params, helper.getCsrfParams());
//	                    	console.log('params:'+params);
	                    	var url = (typeof config.url === 'string' && config.url) || (typeof config.url === 'function' && config.url(row)) || '';
//	                    	console.log('url:'+url);
	                     	helper.showWaitingMsg();
	                        $.post(url, params, helper.processJsonDataResult(config));
	                     }
	                 });
	    		 });
	    	 }
	    	 
             return function(){
            	 if(!$(datagrid).isSelectedOne()){
                     $.messager.alert('警告','请选择一条数据！','warning');
                     return;
            	 }
            	 var selectedNodes = $(datagrid).getGridSelections();
                 if(cb)
                	 cb(selectedNodes[0]);
             };
	     },
	     //处理ajax的json结果，显示处理结果信息，如果没有callback，则清除选择和刷新列表
	     /***
	      * config: {
	      		datagrid:
	      		resetForm:
	      		closeDialog:
	      		callback:
	      * }
	      */
	     processJsonDataResult: function(config, cb){
	    	 var _config = config || {};
	    	 var cb = cb || config.callback;
	    	 return function(data){
	             if(!!!data.success){
	            	 $.messager.progress('close');
	            	 $.messager.alert('操作出错！',data.message,'warning');
	             }else{
	                 $.messager.progress('close');
	                 $.messager.alert('操作成功！',data.message,'info');
	             }
	             if(cb){
	            	 cb(data);
	             }else{
                     $(_config.datagrid || _config.treegrid).easyGrid('clearSelections');
                     $(_config.datagrid || _config.treegrid).easyGrid('reload');
                     
                     $(_config.resetForm).form('reset');
                     if(_config.closeDialog)
                    	 $(_config.closeDialog).dialog('close');
	             }
		     }
	     },
	     
	     remoteMessageHandler: function(cb){
	    	 return function(data){
	             if(cb){
	            	 cb(data);
	             }
	    		 if(data.success){
	    			 if(data.success===true){
		            	 $.messager.progress('close');
		                 $.messager.alert('操作成功！',data.message,'info');
		             }else{
		                 $.messager.progress('close');
		            	 $.messager.alert('操作出错！',data.message,'warning');
		             }
	    		 }
		     }
	     },
	     
	     loadTreeErrorHandler: function(){
	    	 return function(node, data){
	    		 if(data.success){
	    			 if(data.success===true){
	    				 //
		             }else{
		                 $.messager.progress('close');
		            	 $.messager.alert('操作出错！',data.message,'warning');
		             }
	    		 }
	    	 }
	     },
	     
	     loadGridErrorHandler: function(){
	    	 return function(data){
	    		 if(data.success){
	    			 if(data.success===true){
	    				 //
		             }else{
		                 $.messager.progress('close');
		            	 $.messager.alert('操作出错！',data.message,'warning');
		             }
	    		 }
	    	 }
	     },

	     deleteHandler : function(config){
	    	 return function(){
	    		 helper.__deleteHandler(config);
	    	 }
	     },
	     
	     getCsrfParams : function(){
	    	 var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
             //var csrfHeader = $("meta[name='_csrf_header']").attr("content");
             var data = {};
             if(csrfParameter)
            	 data[csrfParameter] = $("meta[name='_csrf']").attr("content");
             return data;
	     },
	     
	     __deleteHandler : function(config){
	    	 var _config = config || {};
             if(!_config.url){
                 $.messager.alert('警告','没有配置提交地址！','warning');
            	 return ;
             }
             
             var selectedNodes = $(_config.datagrid || _config.treegrid).getGridSelections();
             if(selectedNodes.length<1){
                 $.messager.alert('警告','请至少选择一条数据！','warning');
                 return ;
             }

         	var params = helper.getCsrfParams() || {};
             if(_config.idField){
            	 var idValues = $.map(selectedNodes, function(e){
                     return e[_config.idField];
                 });
            	 var paramIdName = _config.paramIdName || _config.idField+'s';
            	 params[paramIdName] = idValues;
             }
         	$.extend(params, config.params || {});

         	$.messager.confirm('警告', '确定要删除记录？', function(rs){
                 if (rs){
                	params._method =  'delete';
                	params = $.param(params, true);
                 	helper.showWaitingMsg();
                    $.post(_config.url, params, helper.processJsonDataResult(_config, function(){
                    	//clearSelections, easyui bug，删除子节点后，如果不清楚，getSelections依然会返回子节点
                        $(_config.datagrid || _config.treegrid).easyGrid('clearSelections');
	                    $(_config.datagrid || _config.treegrid).easyGrid('reload');
                    }));
                 }
             });
	     },
	     
	     gridSelectionsRemoteHandler: function(config){
	    	return function(){
	    		helper.doGridSelectionsRemoteAction(config);
	    	} 
	     },
	     /****
	      * config: {
	       		url: '',
	       		datagrid: '#dataGrid',
		       	idField: 'id',
		       	paramIdName: 'ids',
		       	params: {},
		       	confirmMessage: ''
	      * }
	      */
	     doGridSelectionsRemoteAction : function(config){
	    	 var _config = config || {};
             if(!_config.url){
                 $.messager.alert('警告','没有配置提交地址！','warning');
            	 return ;
             }
             
             var selectedNodes = $(_config.datagrid || _config.treegrid).getGridSelections();
             if(selectedNodes.length<1){
                 $.messager.alert('警告','请至少选择一条数据！','warning');
                 return ;
             }

         	var params = helper.getCsrfParams() || {};
             if(_config.idField){
            	 var idValues = $.map(selectedNodes, function(e){
                     return e[_config.idField];
                 });
            	 var paramIdName = _config.paramIdName || _config.idField+'s';
            	 params[paramIdName] = idValues;
             }
         	$.extend(params, config.params || {});

        	params._method = params._method || 'post';

         	$.messager.confirm('警告', config.confirmMessage || '确定要执行此操作？', function(rs){
                 if (rs){
                 	helper.showWaitingMsg();
                    /*$.post(_config.url, params, helper.processJsonDataResult(_config, function(){
                    	//clearSelections, easyui bug，删除子节点后，如果不清楚，getSelections依然会返回子节点
                        $(_config.datagrid || _config.treegrid).easyGrid('clearSelections');
	                    $(_config.datagrid || _config.treegrid).easyGrid('reload');
                    }));*/
                    var type = params._method.toLowerCase()==='get'?params._method:'post';
                	params = $.param(params, true);
                    $.ajax(_config.url, {
                    	type: type,
                    	data: params,
                    	success: helper.processJsonDataResult(_config)
                    });
                 }
             });
	     },

	     /****
	      * config: {
	       		url: '',
		       	params: {_method: 'put'},
		       	confirmMessage: ''
	      * }
	      */
	     doRemoteAction : function(config){
	    	 var _config = config || {};
             if(!_config.url){
                 $.messager.alert('警告','没有配置提交地址！','warning');
            	 return ;
             }
             
         	var params = helper.getCsrfParams() || {};
         	$.extend(params, config.params || {});

        	params._method = params._method || 'post';

         	$.messager.confirm('警告', config.confirmMessage || '确定要执行此操作？', function(rs){
                 if (rs){
                 	helper.showWaitingMsg();
                    var type = params._method.toLowerCase()==='get'?params._method:'post';
                	params = $.param(params, true);
                    $.ajax(_config.url, {
                    	type: type,
                    	data: params,
                    	success: helper.processJsonDataResult(_config)
                    });
                 }
             });
	     }
	});
	
})(jQuery);