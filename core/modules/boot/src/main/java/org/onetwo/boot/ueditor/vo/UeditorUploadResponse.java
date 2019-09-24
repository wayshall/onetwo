package org.onetwo.boot.ueditor.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weishao zeng
 * <br/>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UeditorUploadResponse {
	String state; //": "SUCCESS",
	String url; //": "upload/demo.jpg",
	String title; //": "demo.jpg",
	String original; //": "demo.jpg"
}
