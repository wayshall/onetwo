package org.onetwo.webapp.oauth2.resource.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.utils.LangOps;
import org.onetwo.webapp.oauth2.resource.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
	
	@Autowired
	private HttpServletRequest request;
	private Map<Long, ArticleVO> articles = LangOps.generateMap(10L, i->new ArticleVO(Long.valueOf(i), "article-"+i, "content-"+i));
	
	@RequestMapping(method=RequestMethod.GET)
	public SimpleDataResult<?> list(Principal principal){
		return WebResultCreator.creator().list(articles).buildResult();
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public SimpleDataResult<?> get(Long id){
		return WebResultCreator.creator().data(articles.get(id)).buildResult();
	}

}
