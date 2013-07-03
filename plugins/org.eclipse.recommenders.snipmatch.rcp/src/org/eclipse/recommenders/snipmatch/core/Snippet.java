package org.eclipse.recommenders.snipmatch.core;

import java.util.ArrayList;

/**
 * This class represent a snippet.
 * This is used to serialize and de-serialize snippet with gson.
 *
 */
public class Snippet {

	private ArrayList<String> patterns;
	private ArrayList<SnippetParameter> params;
	private String envName = "";

	/**
	 * The major type is used by the server to categorize and nest results.
	 * Examples of major types are "expr" (expression) and "stmt" (statement).
	 */
	private String majorType = "";
	/**
	 * The minor type is used by the client to further check snippet
	 * compatibility in nested results. Examples of minor types are "int" and
	 * "java.util.ArrayList".
	 */
	private String minorType = "";
	private String code = "";
	private String summary = "";
	private String id = "";

	public Snippet() {

		patterns = new ArrayList<String>();
		params = new ArrayList<SnippetParameter>();
	}
	
	public void addPatterns(String pattern){
		patterns.add(pattern);
	}
	
	public ArrayList<String> getPatterns(){
		return patterns;
	}
	
	public void addParameter(SnippetParameter parameter){
		params.add(parameter);
	}
	
	public ArrayList<SnippetParameter> getParameter(){
		return params;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
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
