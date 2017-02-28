package org.onetwo.common.reflect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.utils.FieldName;

public class IntroTest {
	
	public static class IntroTestClass {
		private String name;

		@FieldName("testName")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	@Test
	public void testJFishProperty(){
		Intro<IntroTestClass> intro = ClassIntroManager.getInstance().getIntro(IntroTestClass.class);
		assertThat(intro.getJFishProperty("name", false).hasAnnotation(FieldName.class)).isTrue();
	}

}
