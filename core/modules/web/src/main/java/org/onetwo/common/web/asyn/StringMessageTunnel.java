package org.onetwo.common.web.asyn;

import java.util.Collection;

import org.onetwo.common.utils.LangUtils;

public class StringMessageTunnel extends AsyncMessageTunnel<String> {
	
	private Collection<String> messages = LangUtils.newArrayList();

	@Override
	protected Collection<String> getMessages() {
		return messages;
	}

}
