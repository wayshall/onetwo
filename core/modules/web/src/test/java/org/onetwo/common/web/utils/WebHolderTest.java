package org.onetwo.common.web.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebHolderTest {
	
	@Test
	public void test() {
		ServletRequestAttributes req = new ServletRequestAttributes(new MockHttpServletRequest());
		boolean active = WebHolder.isRequestAttributesActive(req);
		assertThat(active).isTrue();
		req.requestCompleted();
		active = WebHolder.isRequestAttributesActive(req);
		assertThat(active).isFalse();
	}

}
