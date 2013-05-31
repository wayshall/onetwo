package org.onetwo.common.web.utils.asyn;

@Deprecated
public class StringMessageHolder extends AsynMessageHolder<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2307057128037819039L;

	@Override
	protected SimpleMessage createMessage(String data, Integer state) {
		SimpleMessage msg = new SimpleMessage();
		msg.setDetail(data);
		msg.setState(state);
		return msg;
	}

}
