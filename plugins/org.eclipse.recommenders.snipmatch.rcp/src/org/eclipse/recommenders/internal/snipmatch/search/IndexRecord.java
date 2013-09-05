package org.eclipse.recommenders.internal.snipmatch.search;

/**
 * This class is used to represent one record in the search index file. This sores the path and summary of a snippet.
 * 
 */
public class IndexRecord {
    public String summary;
    public String filePath;
    public boolean enable;

    public IndexRecord(String s, String file) {
        this.summary = s;
        this.filePath = file;
        this.enable = true;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
