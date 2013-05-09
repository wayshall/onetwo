package org.onetwo.common.web.s2.tag;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.web.config.SiteConfig;

public class FCKUtils {

	public static final String RS_DOMAIN = SiteConfig.getConfig("rs.domain");
	public static final String RS_DOMAIN_HOLDER = "${siteConfig.domain}";

	private FCKUtils() {
	}

	public static String codeFckText(String text) {
		String rep = null;
		if (StringUtils.isNotBlank(RS_DOMAIN))
			rep = RS_DOMAIN;
		else
			rep = "";
		return text == null ? null : text.replace(rep, RS_DOMAIN_HOLDER);
	}

	public static String decodeFckText(String text) {
		String rep = null;
		if (StringUtils.isNotBlank(RS_DOMAIN))
			rep = RS_DOMAIN;
		else
			rep = "";
		return text == null ? null : text.replace(RS_DOMAIN_HOLDER, rep);
	}

	public static Double transWithAndHight(Double width, Double height,
			Double advertWidth, Double advertHeight) {
		Double s;
		if (Math.abs(1 - width / advertWidth) >= Math.abs(1 - height
				/ advertHeight)) {
			s = advertHeight / height;
		} else {
			s = advertWidth / width;
		}
		return s;
	}
	

}
