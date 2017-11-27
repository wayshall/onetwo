package org.onetwo.boot.core.shutdown;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import sun.misc.Signal;
import sun.misc.SignalHandler;
/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("restriction")
public class GraceKillSignalHandler implements SignalHandler, InitializingBean {
	
	private Logger logger = JFishLoggerFactory.getCommonLogger();
	@Autowired(required=false)
	private List<GraceKillProcessor> graceKillProcessor;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(LangUtils.isEmpty(graceKillProcessor)){
			return ;
		}
		
		AnnotationAwareOrderComparator.sort(graceKillProcessor);
		graceKillProcessor.stream().forEach(p->{
			p.getSignals().forEach(s->{
				if(logger.isWarnEnabled()){
					logger.warn("register kill signal {}...", s);
				}
				Signal.handle(new Signal(s), this);
			});
		});
	}

	@Override
	public void handle(Signal signal) {
		if(logger.isWarnEnabled()){
			logger.warn("receive kill signal {}...", signal.getName());
		}
		if(LangUtils.isEmpty(graceKillProcessor)){
			return ;
		}
		final SignalInfo info = SignalInfo.builder()
									.name(signal.getName())
									.number(signal.getNumber())
									.build();
		graceKillProcessor.stream()
							.filter(p->p.getSignals().contains(info.getName()))
							.forEach(p->{
								logger.warn("start to execute GraceKillProcessor[{}] ...", p.getClass());
								try {
									p.handle(info);
								} catch (Exception e) {
									logger.error("execute GraceKillProcessor["+p.getClass()+"] error: ", e);
								}
								logger.warn("has executed GraceKillProcessor[{}] ...", p.getClass());
							});
	}
	
	public static interface GraceKillProcessor {
		String INT = "INT";//ctrl c for win
		String USR2 = "USR2";
		/***
		 * BootGraceKillProcessor -> boot
		 * @author wayshall
		 * @return
		 */
		default Collection<String> getSignals() {
			return Arrays.asList(INT);
		}
		void handle(SignalInfo singal);
	}

	@Data
	@Builder
	public static class SignalInfo {
		String name;
		int number;
	}
}
