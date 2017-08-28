package org.onetwo.boot.core.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

/**
 * @author wayshall
 * <br/>
 */
@RequestMapping(UploadViewController.CONTROLLER_PATH)
public class UploadViewController {
	
	public static final String CONTROLLER_PATH = "/uploadView";
	
	@Autowired
	private FileStorer<?> fileStorer;
	

	@GetMapping(value="/**")
	public ResponseEntity<InputStreamResource> read(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response){
		String accessablePath = RequestUtils.getServletPath(request);
		if(accessablePath.length()>CONTROLLER_PATH.length()){
			accessablePath = accessablePath.substring(CONTROLLER_PATH.length());
		}else{
			throw new BaseException("error path: " + accessablePath);
		}
		/*if(webRequest.checkNotModified(fileStorer.getLastModified(accessablePath))){
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType("image/"+FileUtils.getExtendName(accessablePath)))
					.cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
					.lastModified(new Date().getTime())
					.eTag(accessablePath)
					.build();
		}*/
		return ResponseEntity.ok()
							.contentType(MediaType.parseMediaType("image/"+FileUtils.getExtendName(accessablePath)))
							//一起写才起作用
							.cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
							.lastModified(new Date().getTime())
							.eTag(accessablePath)
							.body(new InputStreamResource(fileStorer.readFileStream(accessablePath)));
	}
	
	@GetMapping(value="/img/**")
	public void read2(HttpServletRequest request, HttpServletResponse response){
		String accessablePath = RequestUtils.getServletPath(request);
		if(accessablePath.length()>CONTROLLER_PATH.length()){
			accessablePath = accessablePath.substring(CONTROLLER_PATH.length());
		}else{
			throw new BaseException("error path: " + accessablePath);
		}
		OutputStream output = null;
		try {
			response.setContentType("image/"+FileUtils.getExtendName(accessablePath));
			output = response.getOutputStream();
			fileStorer.readFileTo(accessablePath, output);
		} catch (IOException e) {
			throw new BaseException("read file error!", e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

}
