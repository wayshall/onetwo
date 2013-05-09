package org.onetwo.common.web.solr;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class HitCounterTest {

//	@Test
	public void test(){
		String text = "张柏芝士蛋糕房 ZHANG'S CAKE SHOP，网友们Hold不住了：宋祖英语培训班、周渝民政服务中心、容祖儿童医院、吴奇隆胸医院、苏永康复中心、梁朝伟哥专卖、陈冠希望小学、吴彦祖传中医坊、林书豪华酒店";
		
		HitCounter hitCount = new HitCounter(true, text);
		System.out.println(text+":\n"+hitCount.getTopWordString(20));
	}

	@Test
	public void test2() throws Exception{
		String testString = "张伯芝士蛋糕房 ZHANG'S CAKE SHOP，网友们Hold不住了：宋祖英语培训班、周渝民政服务中心、容祖儿童医院、吴奇隆胸医院、苏永康复中心、梁朝伟哥专卖、陈冠希望小学、吴彦祖传中医坊、林书豪华酒店";
		 
        Analyzer analyzer = new IKAnalyzer(true);
        TokenStream st = analyzer.tokenStream("", new StringReader(testString));
        CharTermAttribute term = st.getAttribute(CharTermAttribute.class);
        StringBuilder sb = new StringBuilder();
        while (st.incrementToken()) {
            sb.append(term.toString()).append("|");
        }
        System.out.println(sb.toString());
 
        IKSegmenter iks = new IKSegmenter(new StringReader(testString), true);
        StringBuilder sb2 = new StringBuilder();
        Lexeme lexem;
        while ((lexem = iks.next()) != null) {
            sb2.append(lexem.getLexemeText()).append("|");
        }
        System.out.println(sb2.toString());
	}
}
