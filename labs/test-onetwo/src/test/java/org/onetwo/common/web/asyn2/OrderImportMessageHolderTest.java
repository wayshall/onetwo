package org.onetwo.common.web.asyn2;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("serial")
public class OrderImportMessageHolderTest extends AsynMessageHolder<String>{

	public final AsynState orderSucceed = createState(5, "下单成功");
	public final AsynState orderFailed = createState(-5, "下单失败");
	public final AsynState sendoutSucceed = createState(10, "发货成功");
	public final AsynState sendoutFailed = createState(-10, "发货失败");
	public final AsynState ignore = createState(0, "重复未处理");
	public final AsynState errorPhone = createState(-1, "手机号码错误");
	
	public OrderImportMessageHolderTest(){
		registeState(orderSucceed)
		.registeState(orderFailed)
		.registeState(sendoutSucceed)
		.registeState(sendoutFailed)
		.registeState(ignore)
		.registeState(errorPhone);
	}
	

	public String createTaskMessage(ProccessorState state, int taskCount, DeamonTask task){
		String msg = "";
		if(state==ProccessorState.afterSplitTask){
			msg = "进度 ：共分成"+taskCount+"个导入任务……";
		}else if(state==ProccessorState.executingTask){
			int done = taskCount/10;
			msg = "进度 ：正在执行["+task.getName()+"]"+taskCount+"% "+LangUtils.repeatString(done, "- ")+LangUtils.repeatString(10-done, "| ");
		}else if(state==ProccessorState.errorTask){
			msg = "进度 ：导入出错,任务终止"+task.getException().getMessage();
		}else if(state==ProccessorState.finishedTask){
			msg = "进度 ：["+task.getName()+"]完成！";
		}else if(state==ProccessorState.finished){
			msg = "结果 ：一共导入"+getTotalCount()+". "+getStatesAsString();
		}else{
			throw new UnsupportedOperationException();
		}
		return msg;
	}

	@Override
	protected void triggerCounters(SimpleMessage<String> msg){
		ImportMessage imsg = (ImportMessage)msg;
		this.triggerStates(imsg.getState(), imsg.getSendoutState());
	}
	
	/*public SimpleMessage<String> addOrderFailedMessage(String data){
		return addMessage(data, orderFailed);
	}

	public SimpleMessage<String> addSendoutSucceedMessage(String data){
		return addMessage(data, sendoutSucceed);
	}
	public SimpleMessage<String> addSendoutFailedMessage(String data){
		return addMessage(data, sendoutFailed);
	}*/
	public SimpleMessage<String> addIgnoreMessage(String data){
		return addMessage(data, ignore);
	}

	public static class ImportMessage extends SimpleMessage<String> {
		private AsynState sendoutState;
		
		public ImportMessage(String source){
			super(source);
		}

		public AsynState getSendoutState() {
			return sendoutState;
		}

		public void setSendoutState(AsynState sendoutState) {
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
