package org.onetwo.common.test.mockito;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.onetwo.common.test.BaseTest;

public class BaseMockitoTest extends BaseTest{
	
	@Before
	public void setupOnMockito(){
		MockitoAnnotations.initMocks(this);
	}

}
