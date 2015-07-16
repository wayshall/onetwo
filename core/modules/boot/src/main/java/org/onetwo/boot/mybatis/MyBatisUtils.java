package org.onetwo.boot.mybatis;

import java.util.List;
import java.util.Optional;

import org.onetwo.easyui.EasyPage;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public class MyBatisUtils {
	
	public static void setCurrentQueryPage(EasyPage<?> page){
		if(page.isPagination()){
			PageHelper.startPage(page.getPage(), page.getPageSize());  
		}
	}

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
