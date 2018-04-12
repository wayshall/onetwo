package org.onetwo.boot.module.oauth2.util;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * @author wayshall
 * <br/>
 */
public enum PasswordEncoders {
	BCrypt,
	Pbkdf2,
	SCrypt,
	Standard,
	NoOp;
	
	public static PasswordEncoders of(String pe){
		try {
			return PasswordEncoders.valueOf(pe);
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error("error PasswordEncoders: ", e.getMessage());
			return null;
		}
	}
	
	public static PasswordEncoder newEncoder(String encoderString){
		PasswordEncoders encoder = PasswordEncoders.of(encoderString);
		PasswordEncoder passwordEncoder = null;
		if(encoder==PasswordEncoders.BCrypt){
			passwordEncoder = new BCryptPasswordEncoder();
		}else if(encoder==PasswordEncoders.Pbkdf2){
			passwordEncoder = new Pbkdf2PasswordEncoder();
		}else if(encoder==PasswordEncoders.SCrypt){
			passwordEncoder = new SCryptPasswordEncoder();
		}else if(encoder==PasswordEncoders.Standard){
			passwordEncoder = new StandardPasswordEncoder();
		}else if(encoder==PasswordEncoders.NoOp){
			passwordEncoder = NoOpPasswordEncoder.getInstance();
		}else{
			passwordEncoder = ReflectUtils.newInstance(encoderString);
		}
		return passwordEncoder;
	}
}
