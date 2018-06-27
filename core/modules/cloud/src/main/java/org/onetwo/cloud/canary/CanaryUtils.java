package org.onetwo.cloud.canary;

import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author wayshall
 * <br/>
 */
final public class CanaryUtils {

	public static final String HEADER_CANARY_ENABLED = "x-canary";
	public static final String HEADER_CLIENT_TAG = "x-client-tag";

	public static final String ATTR_CANARYCONTEXT = "__JFISH_CANARYCONTEXT__";
	
	public static HttpServletRequest getHttpServletRequest(){
		ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(attrs==null){
			throw new BaseException("ServletRequestAttributes not found, you may missing config [jfish.cloud.hystrix.shareRequestContext=true]");
		}
        final HttpServletRequest request = attrs.getRequest();
        return request;
//		request.setAttribute(CanaryUtils.ATTR_CANARYCONTEXT, ctx);
	}
	
	public static void storeCanaryContext(CanaryContext ctx){
		getHttpServletRequest().setAttribute(CanaryUtils.ATTR_CANARYCONTEXT, ctx);
	}
	
	public static Optional<CanaryContext> getCurrentCanaryContext(){
		Object value = getHttpServletRequest().getAttribute(CanaryUtils.ATTR_CANARYCONTEXT);
		return Optional.ofNullable((CanaryContext)value);
	}
	
	
	public static enum CanaryMode {
		//不启用，走默认侧策略
		DISABLED,
		//当没有x-canary头的时候，查找没有配置canary.filter的服务
		CANARY_NONE,
		//启用，当没有匹配的时候，使用默认查找策略
		SMOOTHNESS,
		//启用，强制匹配，没有匹配的时候，抛错
		FORCE;
		

		public static CanaryMode of(String mode){
			if(StringUtils.isBlank(mode)){
				return CANARY_NONE;
			}
			return Stream.of(values()).filter(s->s.name().equalsIgnoreCase(mode))
										.findAny()
										.orElse(DISABLED);
//										.orElseThrow(()->new IllegalArgumentException("mode: " + mode));
		}
	}
	

    public static enum CanaryErrors implements ErrorType {
        CANARY_SERVER_NOT_MATCH("can not find any matched cannary server!"),
        CANARY_NONE_SERVER_NOT_MATCH("can not find any matched server that not configurated cannary.filter!");

        final private String message;

        CanaryErrors(String message) {
            this.message = message;
        }

        @Override
        public String getErrorCode() {
            return name();
        }

        public String getErrorMessage() {
            return message;
        }

        public static CanaryErrors of(String status) {
            return Stream.of(values()).filter(s -> s.name().equalsIgnoreCase(status)).findAny()
                    .orElseThrow(() -> new IllegalArgumentException("error status: " + status));
        }

    }
	
	private CanaryUtils(){
	}

}
