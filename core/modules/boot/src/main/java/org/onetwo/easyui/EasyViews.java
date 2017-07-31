package org.onetwo.easyui;

import java.util.List;

import org.onetwo.common.data.DataResultWrapper;
import org.onetwo.common.utils.Page;

/**
 * @author wayshall
 * <br/>
 */
public class EasyViews {
	
	public static class EasyGridView implements DataResultWrapper {

		@Override
		public Object wrapResult(Object data) {
			Object newData = data;
			if(data instanceof Page){
				newData = EasyDataGrid.create((Page<?>)data);
			}else if(data instanceof List){
				newData = EasyDataGrid.create((List<?>)data);
			}
			return newData;
		}
		
	}

}
