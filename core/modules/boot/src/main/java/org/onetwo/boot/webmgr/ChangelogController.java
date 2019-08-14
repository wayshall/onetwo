package org.onetwo.boot.webmgr;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.utils.LangUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wayshall
 * <br/>
 */
@RestController
@RequestMapping(BootWebUtils.CONTROLLER_PREFIX+"/changelog")
//@RooUserAuth
public class ChangelogController extends AbstractBaseController {
	
	private String path = "changelog.md";
	
	@GetMapping("/version")
	@ResponseBody
	public Object getVersion(){
		String version = "no version";
		try {
			InputStream in = SpringUtils.classpath(path).getInputStream();
			List<String> datas = FileUtils.readAsList(in);
			if (!LangUtils.isEmpty(datas)) {
				version = datas.get(0);
			}
		} catch (IOException e) {
			throw new BaseException("read classpath file as stream error: " + path);
		}
		return DataResults.map("version", version).build();
	}

}
