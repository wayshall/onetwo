package org.onetwo.common.delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings({"rawtypes"})
public class MutiDelegate {
	
	private Class[] argTypes;
	private List<DelegateMethodImpl> delegateMethods;
	
	public MutiDelegate(Class...argTypes){
		this.argTypes = argTypes;
		this.delegateMethods = new ArrayList<DelegateMethodImpl>();
	}

	public Class[] getArgTypes() {
		return argTypes;
	}
	
	public MutiDelegate add(Object target, String method){
		DelegateMethodImpl d = DelegateFactory.create(target, method, argTypes);
		this.delegateMethods.add(d);
		return this;
	}
	
	public Collection<Object> invoke(Object...args){
		Collection<Object> resultList = LangUtils.newArrayList(this.delegateMethods.size());
		Object result = null;
		for(DelegateMethodImpl target : this.delegateMethods){
			result = target.invoke(args);
			resultList.add(result);
		}
		return resultList;
	}
	
	public List<Object> invokeAsList(Object...args){
		return CUtils.tolist(invoke(args), false);
	}
	
	/*public <T> T getReturnValue(int index){
		return (T) this.delegateMethods.get(index).getReturnValue();
	}
	
	public <T> T getReturnValue(int index, Class<T> clazz){
		return (T) this.delegateMethods.get(index).getReturnValue();
	}*/
	
	/*public Object getReturnValue(String method){
		for(DelegateMethodImpl delegate : this.delegateMethods){
			if(method.equals(delegate.getMethodName())){
				return delegate.getReturnValue();
			}
		}
		throw LangUtils.asBaseException("can not find the method name : " + method);
	}*/
}
