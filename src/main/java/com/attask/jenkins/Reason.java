package com.attask.jenkins;

import hudson.model.Action;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;
import java.util.List;

/**
 * User: Joel Johnson
 * Date: 9/12/12
 * Time: 12:40 PM
 */
@ExportedBean
public class Reason implements Action, Serializable {
	private final String failureNotes;
	private final List<String> failureCategories;

	public Reason(String failureNotes, List<String> failureCategories) {
		this.failureNotes = failureNotes;
		this.failureCategories = failureCategories;
	}

	@Exported
	public String getFailureNotes() {
		return failureNotes;
	}

	@Exported
	public List<String> getFailureCategories() {
		return failureCategories;
	}

	public String getIconFileName() {
		return null;
	}

	public String getDisplayName() {
		return "reason";
	}

	public String getUrlName() {
		return null;
	}
}
