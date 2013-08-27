package org.eclipse.recommenders.tests.snipmatch.core;

import static org.junit.Assert.*;

import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.snipmatch.core.SnippetParameter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SnippetTest {
    @Test
    public void testSnippet() {
        Snippet snippet = new Snippet();
        assertNotNull(snippet.getPatterns());
        assertNotNull(snippet.getParameter());
    }

    @Test
    public void testAddPatterns() {
        Snippet snippet = new Snippet();
        snippet.addPatterns("pattern");
        assertEquals(1, snippet.getPatterns().size());
    }

    @Test
    public void testGetPatterns() {
        Snippet snippet = new Snippet();
        snippet.addPatterns("pattern");
        assertEquals("pattern", snippet.getPatterns().get(0));
    }

    @Test
    public void testAddParameter() {
        Snippet snippet = new Snippet();
        snippet.addParameter(new SnippetParameter());
        assertEquals(1, snippet.getParameter().size());
    }

    @Test
    public void testGetParameter() {
        Snippet snippet = new Snippet();
        SnippetParameter param = new SnippetParameter();
        snippet.addParameter(param);
        assertEquals(param, snippet.getParameter().get(0));
    }

    @Test
    public void testGetEnvName() {
        Snippet snippet = new Snippet();
        assertEquals("", snippet.getEnvName());
    }

    @Test
    public void testSetEnvName() {
        Snippet snippet = new Snippet();
        snippet.setEnvName("envName");
        assertEquals("envName", snippet.getEnvName());
    }

    @Test
    public void testGetMajorType() {
        Snippet snippet = new Snippet();
        assertEquals("", snippet.getMajorType());
    }

    @Test
    public void testSetMajorType() {
        Snippet snippet = new Snippet();
        snippet.setMajorType("majorType");
        assertEquals("majorType", snippet.getMajorType());
    }

    @Test
    public void testGetMinorType() {
        Snippet snippet = new Snippet();
        assertEquals("", snippet.getMinorType());
    }

    @Test
    public void testSetMinorType() {
        Snippet snippet = new Snippet();
        snippet.setMinorType("minorType");
        assertEquals("minorType", snippet.getMinorType());
    }

    @Test
    public void testGetCode() {
        Snippet snippet = new Snippet();
        assertEquals("", snippet.getCode());
    }

    @Test
    public void testSetCode() {
        Snippet snippet = new Snippet();
        snippet.setCode("Code");
        assertEquals("Code", snippet.getCode());
    }

    @Test
    public void testGetSummary() {
        Snippet snippet = new Snippet();
        assertEquals("", snippet.getSummary());
    }

    @Test
    public void testSetSummary() {
        Snippet snippet = new Snippet();
        snippet.setSummary("summary");
        assertEquals("summary", snippet.getSummary());
    }

    @Test
    public void testGetId() {
        Snippet snippet = new Snippet();
        assertEquals("", snippet.getId());
    }

    @Test
    public void testSetId() {
        Snippet snippet = new Snippet();
        snippet.setId("id");
        assertEquals("id", snippet.getId());
    }
}
