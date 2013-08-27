package org.eclipse.recommenders.tests.snipmatch.core;

import static org.junit.Assert.*;

import org.eclipse.recommenders.snipmatch.core.SnippetParameter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SnippetParameterTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetName() {
        SnippetParameter snippetParameter = new SnippetParameter();
        assertEquals("", snippetParameter.getName());
    }

    @Test
    public void testSetName() {
        SnippetParameter snippetParameter = new SnippetParameter();
        snippetParameter.setName("name");
        assertEquals("name", snippetParameter.getName());
    }

    @Test
    public void testGetMajorType() {
        SnippetParameter snippetParameter = new SnippetParameter();
        assertEquals("", snippetParameter.getMajorType());
    }

    @Test
    public void testSetMajorType() {
        SnippetParameter snippetParameter = new SnippetParameter();
        snippetParameter.setMajorType("majorType");
        assertEquals("majorType", snippetParameter.getMajorType());
    }

    @Test
    public void testGetMinorType() {
        SnippetParameter snippetParameter = new SnippetParameter();
        assertEquals("", snippetParameter.getMinorType());
    }

    @Test
    public void testSetMinorType() {
        SnippetParameter snippetParameter = new SnippetParameter();
        snippetParameter.setMinorType("minorType");
        assertEquals("minorType", snippetParameter.getMinorType());
    }

    @Test
    public void testGetValue() {
        SnippetParameter snippetParameter = new SnippetParameter();
        assertEquals("", snippetParameter.getValue());
    }

    @Test
    public void testSetValue() {
        SnippetParameter snippetParameter = new SnippetParameter();
        snippetParameter.setValue("value");
        assertEquals("value", snippetParameter.getValue());
    }

}
