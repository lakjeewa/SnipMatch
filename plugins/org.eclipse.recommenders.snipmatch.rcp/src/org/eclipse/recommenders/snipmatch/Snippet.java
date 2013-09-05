package org.eclipse.recommenders.snipmatch;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * This class represent a snippet. This is used to serialize and de-serialize snippet with gson.
 */
// TODO MB: I'd like to make this UUIDable
public class Snippet {

    private List<String> patterns = Lists.newLinkedList();
    private String code = "";
    private String summary = "";
    private String id = "";

    public List<String> getPatterns() {
        return patterns;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
