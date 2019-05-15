package org.onetwo.boot.mybatis;

import java.util.List;
import java.util.Optional;

import org.onetwo.common.utils.Page;

import com.github.pagehelper.PageHelper;

public class MyBatisUtils {
	
	/*public static void setCurrentQueryPage(EasyDataGrid<?> page){
		if(page.isPagination()){
			PageHelper.startPage(page.getPage(), page.getPageSize());  
		}
	}*/
	
	public static void setCurrentQueryPage(Page<?> page){
		if(page.isPagination()){
			PageHelper.startPage(page.getPageNo(), page.getPageSize());  
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Optional<Page<T>> getPage(List<T> rows){
		if(Page.class.isInstance(rows)){
			Page<T> page = (Page<T>) rows;
			return Optional.of(page);
		}
		return Optional.empty();
	}
	private MyBatisUtils(){
	}
}
