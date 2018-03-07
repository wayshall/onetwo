package org.onetwo.common.web.asyn;

import java.util.List;

public interface ProgressAsyncWebProcessor extends AsyncWebProcessor {
	
	public <T> void handleList(List<T> datas, int dataCountPerTask, ProgressAsyncTaskCreator<T> creator);
}
