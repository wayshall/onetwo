package org.onetwo.common.web.data;

import org.apache.log4j.Logger;
import org.onetwo.common.web.utils.HtmlFilter;

public class DelicateHtmlFilter implements Filter {

	private Logger log = Logger.getLogger(getClass());
	
	private HtmlFilter filter = null;
	StringBuilder sb = new StringBuilder();
	
	public DelicateHtmlFilter() {
        HtmlFilter.FilteredDelegate d = new HtmlFilter.FilteredDelegate() {

            public void filtered(String s) {
                sb.append(s);
            }
        };
        
        HtmlFilter.ImageFilteredDelegate imgFilter = new HtmlFilter.ImageFilteredDelegate() {

            public String filteredImage(String src) {
            	
            	/*if(src.indexOf(ResourceUtil.getInstance().getResourceDomain()) >= 0) {
            		return src;
            	}
            	
                // 下载外部图片
				src = HtmlFilter.decodeHtml(src).toString();
            	ResourceInfoEntity rs = ResourceUtil.getInstance().saveRemoteUploadFile(src, 0L, 0L, EditorFileEntity.class.getName());
                return rs==null ? src : (FCKUtils.RS_DOMAIN_HOLDER + rs.getPath() + rs.getFileName() + "?" + src);*/
            	return src;
            }
        };

        filter = new HtmlFilter(d, imgFilter, true);
        ///filter.setWashLink("http://xxx/goto?url=");
        String safeUrls[] = {"/","./","../"};
        filter.setSafeUrls(safeUrls);
//        filter.washMailto = "webmaster@ciipp.com?orginto=";
	}
	
	@Override
	public Object doFilter(Object content) {
		try {
			if(content instanceof String) {
				StringBuilder html = new StringBuilder(content.toString());
				filter.filter(html);
				
				String result = sb.toString();
				sb.delete(0, sb.length());
				
				return result;
			}
		} catch(Exception e) {
			log.warn("HTML过滤器在过滤以下内容时发生错误：\n" + content, e);
		}
		return (content);
	}

}
