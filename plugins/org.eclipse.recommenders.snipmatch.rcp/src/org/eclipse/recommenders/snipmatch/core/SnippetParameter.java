package org.eclipse.recommenders.snipmatch.core;

/**
 * 
 * This class represent a parameter of a snippet. One snippet can have several
 * parameters.
 * 
 */
public class SnippetParameter {

    private String name = "";
    private String majorType = "";
    private String minorType = "";
    private String value = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajorType() {
        return majorType;
    }

    public void setMajorType(String majorType) {
        this.majorType = majorType;
    }

    public String getMinorType() {
        return minorType;
    }

    public void setMinorType(String minorType) {
        this.minorType = minorType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
