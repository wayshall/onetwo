package org.onetwo.webapp.oauth2.authorization.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {
	
	private Long id;
	private String title;
	private String content;

}
