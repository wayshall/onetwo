package org.onetwo.common.web.utils.bbcode;

import org.springframework.stereotype.Component;

/***
 * 根据配置解释bbcode代码
 * @author weishao
 *
 */
//@Component
public class BBCodeParser {

	// @Resource
	protected BBCodeHandler bbcodeHandler = new BBCodeHandler();

	protected BBCodeHandler reverseHandler = new BBCodeHandler("bb_reverse_config.xml");

	public String parser(String text) {
//		this.bbcodeHandler.init();
		for (BBCode bbcode : this.bbcodeHandler.getBbList()) {
			text = text.replaceAll(bbcode.getRegex(), bbcode.getReplace());
		}
		return text;
	}

	public String reverse(String text) {
//		this.reverseHandler.init();
		for (BBCode bbcode : this.reverseHandler.getBbList()) {
			text = text.replaceAll(bbcode.getRegex(), bbcode.getReplace());
		}
		return text;
	}

	public static void main(String[] args){
		try {
			//String str = "aa";
			System.out.print(new String(new char[]{10}));
			System.out.println("----------");
			/*String path = BBCodeParser.class.getResource("test.html").getPath();
			System.out.println("path: " +path);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			StringBuilder text = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				text.append(line);
			}
			BBCodeParser bb = new BBCodeParser();
			System.out.println("text: " + bb.reverse(text.toString()));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
