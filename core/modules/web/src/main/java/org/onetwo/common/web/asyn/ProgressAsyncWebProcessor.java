package org.onetwo.common.web.asyn;

import java.util.List;

public interface ProgressAsyncWebProcessor {
	
	public void handleList(List<?> datas, int dataCountPerTask, ProgressAsyncTaskCreator<List<?>> creator);
}
