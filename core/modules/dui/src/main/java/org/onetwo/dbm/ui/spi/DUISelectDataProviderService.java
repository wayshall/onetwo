package org.onetwo.dbm.ui.spi;

import org.onetwo.dbm.ui.meta.DUIFieldMeta.DUISelectMeta;
import org.onetwo.dbm.ui.vo.UISelectDataRequest;

/**
 * dui select组件统一获取下拉框数据的服务接口
 * @author weishao zeng
 * <br/>
 */

public interface DUISelectDataProviderService {

	/***
	 * 获取下拉框数据列表
	 * @author weishao zeng
	 * @param request
	 * @return
	 */
	Object getDatas(UISelectDataRequest request);
	
	/***
	 * 获取下拉框列表显示的值
	 * @author weishao zeng
	 * @param uiselect
	 * @param value
	 * @return
	 */
	Object getListValue(DUISelectMeta uiselect, Object value);
	
}
