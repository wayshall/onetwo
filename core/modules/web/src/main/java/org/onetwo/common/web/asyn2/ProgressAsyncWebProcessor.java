package org.onetwo.common.web.asyn2;

import java.util.List;

public interface ProgressAsyncWebProcessor {
	
	public void handleList(List<?> datas, int dataCountPerTask, ProgressAsyncTaskCreator<List<?>> creator);
}
