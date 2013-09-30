package org.onetwo.common.ws;

import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public abstract class WebServiceUtils {
	
	private static final Logger logger = MyLoggerFactory.getLogger(WebServiceUtils.class);

	public static <T extends WSResult> T tryCatch(WSBlock<T> block, Class<T> clazz){
		Assert.notNull(clazz);
		return tryCatch(block, ReflectUtils.newInstance(clazz));
	}
	
	public static <T extends WSResult> T tryCatch(WSBlock<T> block, T wsresult){
		Assert.notNull(wsresult);
		try {
			if(wsresult==null){
				wsresult = block.doExecute(wsresult);
				if(wsresult!=null){
					return wsresult;
				}
			}else{
				T rs = block.doExecute(wsresult);
				if(rs!=null)
					wsresult = rs;
			}

			if(wsresult.isSuccess()==null)
				wsresult.setSuccess(true);
			if(StringUtils.isBlank(wsresult.getMessage()))
				wsresult.setMessage("操作成功！");
		} catch (Exception e) {
			logger.error("web service occur error : " + e.getMessage(), e);
			e.printStackTrace();
			createWSResultIfNull(wsresult).setSuccess(false);
			wsresult.setMessage(e.getMessage());
			if(e instanceof ExceptionCodeMark){
				ExceptionCodeMark be = (ExceptionCodeMark) e;
				wsresult.setResultCode(be.getCode());
			}
			block.doExcepted(wsresult);
		} finally{
			block.doFinally(wsresult);
		}
		return wsresult;
	}
	
	public static WSResult createWSResultIfNull(WSResult wsresult){
		return wsresult==null? createWSResult() : wsresult;
	}
	
	public static WSResult createWSResult(){
		return new WSResult();
	}

}
