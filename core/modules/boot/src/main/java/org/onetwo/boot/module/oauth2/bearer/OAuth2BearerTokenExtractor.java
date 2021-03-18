package org.onetwo.boot.module.oauth2.bearer;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.module.oauth2.bearer.BearerTokenProperties.BearerHeadersConfig;
import org.onetwo.boot.utils.PathMatcher;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.util.AntPathMatcher;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;


/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class OAuth2BearerTokenExtractor extends BearerTokenExtractor {
	
	private static final Set<String> DEFAULT_EXCLUDE_POSTFIX = Sets.newHashSet("png", "jpg", "jpeg", "bpm", "gif", "js", "css", "mp3", "mp4", "html", "htm");

	@Autowired
	private BearerTokenProperties bearerTokenProperties;
	
//	private List<FixHeadersConfig> fixHeaders;
	private AntPathMatcher pathMatcher = new AntPathMatcher();
//	private Expression expression = ExpressionFacotry.newExpression("(", ")");
	private Set<String> excludePostfix = DEFAULT_EXCLUDE_POSTFIX;
	

	@Override
	protected String extractHeaderToken(HttpServletRequest request) {
		if (!shouldFilter(request)) {
			return super.extractHeaderToken(request);
		}

		String path = getRequestPath(request);
		Optional<BearerHeadersConfig> header = bearerTokenProperties.getHeaders().values().stream().filter(fix -> {
			if (fix.getMatcher()==PathMatcher.REGEX) {
				return doRegexMatcher(request, fix, path);
			} else {
				return doAntMatcher(request, fix, path);
			}
		}).findFirst();
		
		if (header.isPresent()) {
			BearerHeadersConfig hv = header.get();
			if (bearerTokenProperties.isDebug()) {
				log.info("add OAuth2 customer Authorization header");
			}
			return hv.getValue();
		} else {
			return super.extractHeaderToken(request);
		}
	}
	

	public boolean shouldFilter(HttpServletRequest request) {
		String path = getRequestPath(request);
		String ext = FileUtils.getExtendName(path);
		if(StringUtils.isNotBlank(ext) && excludePostfix.contains(ext.toLowerCase())){
			return false;
		}
		return true;
	}
	
	protected String getRequestPath(HttpServletRequest request){
		String path = RequestUtils.getServletPath(request);
		return path;
	}

	
	private boolean doAntMatcher(HttpServletRequest request, BearerHeadersConfig fix, String path){
		boolean match = fix.getPathPatterns().stream().anyMatch(pattern->{
			return pathMatcher.match(pattern, path);
		});
		return match;
	}
	
	private boolean doRegexMatcher(HttpServletRequest request, BearerHeadersConfig fix, String path){
		for(Entry<String, Pattern> entry : fix.getPatterns().entrySet()){
			Matcher matcher = entry.getValue().matcher(path);
			boolean isMatch = matcher.matches();
			if(isMatch){
				return isMatch;
			}
		}
		return false;
	}


}
