package org.onetwo.common.ejb;

import org.junit.Test;
import org.onetwo.common.ejb.utils.EJBFinder;

public class EJBTest {
	
	@Test
	public void testEJBBean(){
		EJBFinder ejbfinder = EJBFinder.create();
		System.out.println("ejb: " + ejbfinder.getEJB("IBookingServiceRemote", true));
	}

}
