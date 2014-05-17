

<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
	    <title>
			${title!"管理后台"}
	    </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">
		
		<script type="text/javascript" src="${pluginConfig.jsPath}/extjs/ext-all.js"></script>
		<link rel="stylesheet" type="text/css" href="${pluginConfig.jsPath}/extjs/resources/ext-theme-${theme }/ext-theme-${theme }-all.css" />
		
		<script type="text/javascript">

		mainPage = true;
		
		function redirect(url){
			location.href = url;
		}

		function addTimestamp(url){
			if(url.indexOf('?')==-1){
				return url + "?t="+new Date().getTime();
			}else{
				return url + "&t="+new Date().getTime();
			}
		}
		
		function clickMenuItem(view, record, item, index, e, eOpts){
			if(!record.raw.leaf)
				return ;
			var url = "${siteConfig.baseURL}" + record.raw.url;
			url = addTimestamp(url);
			Ext.getCmp('mainTabPanel').setActiveTab(1);
			Ext.get("contentIframe").dom.src = url;
		}
		
		function logout(){
			
			var win = new Ext.Window({  
                id : 'win',  
                layout : 'fit', //自适应布局     
                align : 'center',  
                width : 330,  
                title: '注销确认',
                height : 182,  
                resizable : false,  
                draggable : false,  
                border : false,  
                maximizable : false,//禁止最大化  
                closeAction : 'destroy',  
                items : [{
                	id : 'logoutForm',
                	xtype:'form',
                	layout: "fit",
                	bodyPadding: 30,
                	border : false,  
                    buttonAlign : 'center', 
                	items: [{
                		xtype: "text",
                		text : "确定要注销？",
                		bodyStyle:"text-align:center",
                		align: "center"
                	}],
                	buttons: [{
                		text:"确定",
                		handler: function(){
                			logoutHandler(win);
                		}
                	},{
                		text:"取消",
                		handler: function(){
                			win.destroy();
                		}
                	}]
                }]   
            });  
            win.show();//显示窗体
            
            
		}
		
		function logoutHandler(win){
			document.getElementById('userLogoutForm').submit();
			return false;
		}
		
		Ext.require(['*']);
		var panels = ${treePanelDatas};
		

		
		
	    Ext.onReady(function() {

	        Ext.QuickTips.init();

	        // NOTE: This is an example showing simple state management. During development,
	        // it is generally best to disable state management as dynamically-generated ids
	        // can change across page loads, leading to unpredictable results.  The developer
	        // should ensure that stable state ids are set for stateful components in real apps.
	        Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

	        var northCmp = Ext.create('Ext.Component', {
                region: 'north',
                height: 32, // give north and south regions a height
                contentEl: 'north'
            });
	        var viewport = Ext.create('Ext.Viewport', {
	            id: 'border-iccard',
	            layout: 'border',
	            items: [
	            	northCmp,
	              {
	                region: 'west',
	                stateId: 'navigation-panel',
	                id: 'west-panel', // see Ext.getCmp() below
	                title: '功能菜单',
	                split: true,
	                width: 200,
	                minWidth: 175,
	                maxWidth: 400,
	                collapsible: true,
	                animCollapse: true,
	                margins: '0 0 0 5',
	                layout: 'accordion',
	                items:  panels
	            },
	            // in this instance the TabPanel is not wrapped by another panel
	            // since no title is needed, this Panel is added directly
	            // as a Container
	            Ext.create('Ext.tab.Panel', {
	            	id:'mainTabPanel',
	                region: 'center', // a center region is ALWAYS required for border layout
	                deferredRender: false,
	                activeTab: 0,     // first tab initially active
	                items: [{
	                	id:'cententTab',
	                   	contentEl: 'contentPage',
	                    title: '内容窗口',
	                    autoScroll: true
	                	}
	                ]
	            })]
	        });
	        // get a reference to the HTML element with id "hideit" and add a click listener to it
	        if(Ext.get("hideit")){
		        Ext.get("hideit").on('click', function(){
		            // get a reference to the Panel that was created with id = 'west-panel'
		            var w = Ext.getCmp('west-panel');
		            // expand or collapse that Panel based on its collapsed property state
		            w.collapsed ? w.expand() : w.collapse();
		        });
	        }
	    });
	    
		
		</script>
		
	</head>
	<body>	
		<!-- use class="x-hide-display" to prevent a brief flicker of the content -->
	
    <div id="north" class="" style="margin-top: 5px;margin-bottom: 3px;margin-right: 10px">
    <div  style="float:left">
    	【${(loginUserInfo.nickName)!"游客"}】，你好！欢迎使用${title}&nbsp; 
    </div>
   	<div  style="float:right;">
   		<strong><a href="javascript:logout();">注销</a></strong>
   	</div>
    	</div>
    </div>
    <div id="west" class="x-hide-display">
    </div>
   
   <!-- 
    <div id="myPage" class="x-hide-display" style="height:100%;overflow:scroll;overflow-y:hidden">
         <iframe id="mainIframe" name="mainIframe" src="" width="100%"  width="100%" height="100%" frameborder="0" style="height:100%;overflow:scroll;overflow-y:hidden"></iframe>
    </div>
     -->
    <div id="readCardPage" class="x-hide-display" style="height:100%;overflow:scroll;overflow-y:hidden">
         <iframe id="mainIframe" name="mainIframe" src="admintools" width="100%"  width="100%" height="100%" frameborder="0" style="height:100%;overflow:scroll;overflow-y:hidden"></iframe>
    </div>
    <div id="contentPage" class="x-hide-display" style="height:100%;overflow:scroll;overflow-y:hidden">
         <iframe id="contentIframe" name="contentIframe" src="" width="100%"  width="100%" height="100%" frameborder="0" style="height:100%;overflow:scroll;overflow-y:hidden"></iframe>
    </div>
   
    <div id="props-panel" class="x-hide-display" style="width:200px;height:200px;overflow:hidden;">
    </div>
    <div id="south" class="x-hide-display">
        <p>south - generally for informational stuff, also could be for status bar</p>
    </div>
    
    <div style="display:hidden">
    	<form id="userLogoutForm" action="${ssoConfig.serverLoginUrl}" method="post">
    	</form>
    </div>
	</body>
	
</html>
