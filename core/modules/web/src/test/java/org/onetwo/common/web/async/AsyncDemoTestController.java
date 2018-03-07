package org.onetwo.common.web.async;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.asyn.AsyncTask;
import org.onetwo.common.web.asyn.AsyncWebProcessorBuilder;
import org.onetwo.common.web.asyn.ProgressAsyncWebProcessor;
import org.onetwo.common.web.asyn.StringMessageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * async-demo.html:
   <iframe id="taskFrame" name="taskFrame" src="" style="display: none;">
       </iframe>
       <div id="msgProgress">处理进度。。。</div>
       <div id="msgDetail">错误：<br/></div>
       <form id="taskForm" target="taskFrame" action="${pluginHelper.baseURL}/asyncDemo/doTask" method="post">
        <input type="submit" value="提交">
       </form>
       
       <script>
    function doAsynCallback(data){
        function doAsynCallback(data){
        if(data.state=='FINISHED'){
            $('#msgProgress').append("<br/>")
            $('#msgProgress').append(data.message)
        }else if(data.state=='EMPTY'){
            //$('#msgDetail').append(data.message)
        }else if(data.state=='FAILED'){
            $('#msgDetail').append(data.message)
        }else{
            $('#msgProgress').html(data.message)
        }
    } 
    }   
</script>
 * @author wayshall
 * <br/>
 */
@Controller("asyncDemo")
public class AsyncDemoTestController {
	

	@RequestMapping("taskPage")
	public ModelAndView taskPage(){
		return new ModelAndView("async-demo");
	}
	
	@RequestMapping("doTask")
	public void doTask(HttpServletResponse response){
		ProgressAsyncWebProcessor proccessor = AsyncWebProcessorBuilder.newBuilder(response)
																		.messageTunnel(new StringMessageHolder())
																		.asynCallback("parent.doAsynCallback")
																		.writeEmptyMessage(true)
																		.buildProgressAsyncWebProcessor();
		List<String> datas = LangOps.generateList(100, i->{
			return "test-"+i;
		});
		proccessor.handleList(datas, 10, ctx->{
			return new AsyncTask("task-"+(ctx.getTaskIndex()+1), ctx.getTaskIndex()) {
				
				@Override
				public void execute() throws Exception {
					System.out.println("ctx:"+ctx);
					LangUtils.awaitInMillis(300);
					if(ctx.getTaskIndex()==3){
						throw new BaseException("err");
					}else{
						System.out.println("导入完成:" + ctx.getTaskIndex());
					}
				}
			};
		});
	}

}
