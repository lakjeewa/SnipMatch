/**
 * 
 */
package org.eclipse.recommenders.internal.snipmatch.rcp.search;

import static org.junit.Assert.*;

import org.eclipse.recommenders.internal.snipmatch.search.UserEnvironment;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
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
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        UserEnvironment userEnvironment = new UserEnvironment(editor);
        assertNull(userEnvironment.getSearchBoxAnchor());
    }

}
