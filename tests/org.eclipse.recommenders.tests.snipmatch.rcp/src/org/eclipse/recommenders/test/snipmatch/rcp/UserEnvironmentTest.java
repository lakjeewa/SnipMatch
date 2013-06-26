/**
 * 
 */
package org.eclipse.recommenders.test.snipmatch.rcp;

import static org.junit.Assert.*;

import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Lakjeewa
 *
 */
public class UserEnvironmentTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSearchBoxAnchor() {
		UserEnvironment userEnvironment = new UserEnvironment();
		assertNull(userEnvironment.getSearchBoxAnchor());
	}
	
	@Test
	public void testIsJavaEditor() {
		UserEnvironment userEnvironment = new UserEnvironment();
		assertFalse(userEnvironment.isJavaEditor());
	}

}
