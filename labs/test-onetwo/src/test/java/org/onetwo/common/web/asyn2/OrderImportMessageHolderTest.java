package org.onetwo.common.web.asyn2;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("serial")
public class OrderImportMessageHolderTest extends ListMessageHolder{

	public final TaskState orderSucceed = createState(5, "下单成功");
	public final TaskState orderFailed = createState(-5, "下单失败");
	public final TaskState sendoutSucceed = createState(10, "发货成功");
	public final TaskState sendoutFailed = createState(-10, "发货失败");
	public final TaskState ignore = createState(0, "重复未处理");
	public final TaskState errorPhone = createState(-1, "手机号码错误");
	
	public OrderImportMessageHolderTest(){
		registeState(orderSucceed)
		.registeState(orderFailed)
		.registeState(sendoutSucceed)
		.registeState(sendoutFailed)
		.registeState(ignore)
		.registeState(errorPhone);
	}
	

	public String createTaskMessage(ProcessMessageType state, int taskCount, AsyncTask task){
		String msg = "";
		if(state==ProcessMessageType.SPLITED){
			msg = "进度 ：共分成"+taskCount+"个导入任务……";
		}else if(state==ProcessMessageType.PROGRESSING){
			int done = taskCount/10;
			msg = "进度 ：正在执行["+task.getName()+"]"+taskCount+"% "+LangUtils.repeatString(done, "- ")+LangUtils.repeatString(10-done, "| ");
		}else if(state==ProcessMessageType.FAILED){
			msg = "进度 ：导入出错,任务终止"+task.getException().getMessage();
		}else if(state==ProcessMessageType.SUCCEED){
			msg = "进度 ：["+task.getName()+"]完成！";
		}else if(state==ProcessMessageType.FINISHED){
			msg = "结果 ：一共导入"+getTotalCount()+". "+getStatesAsString();
		}else{
			throw new UnsupportedOperationException();
		}
		return msg;
	}

	@Override
	protected void triggerCounters(SimpleMessage msg){
		ImportMessage imsg = (ImportMessage)msg;
		this.triggerStates(imsg.getState(), imsg.getSendoutState());
	}
	
	/*public SimpleMessage addOrderFailedMessage(String data){
		return addMessage(data, orderFailed);
	}

	public SimpleMessage addSendoutSucceedMessage(String data){
		return addMessage(data, sendoutSucceed);
	}
	public SimpleMessage addSendoutFailedMessage(String data){
		return addMessage(data, sendoutFailed);
	}*/
	public SimpleMessage addIgnoreMessage(String data){
		return addMessage(data, ignore);
	}

	public static class ImportMessage extends SimpleMessage {
		private TaskState sendoutState;
		
		public ImportMessage(String source){
			super(source);
		}

		public TaskState getSendoutState() {
			return sendoutState;
		}

		public void setSendoutState(TaskState sendoutState) {
			this.sendoutState = sendoutState;
		}

		/*public AsynState getOrderState() {
			return state;
		}

		public void setOrderState(AsynState orderState) {
			this.state = orderState;
		}*/

		public String toString(){
			if(StringUtils.isNotBlank(detail))
				return detail;
			return toStateString();
		}
		public String toStateString(){
			return LangUtils.append(source==null?"":"["+source+"] ", state==null?"":state.getName(), " ", sendoutState==null?"":sendoutState.getName());
		}
		
	}

}
