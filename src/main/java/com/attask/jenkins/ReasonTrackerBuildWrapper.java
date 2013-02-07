package com.attask.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * User: Joel Johnson
 * Date: 9/11/12
 * Time: 1:10 PM
 */
public class ReasonTrackerBuildWrapper extends BuildWrapper {
	private final String categories;

	@DataBoundConstructor
	public ReasonTrackerBuildWrapper(String categories) {
		this.categories = categories;
	}

	@Override
	public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
		build.addAction(new ReasonTrackerAction(build, findCategories()));
		return new Environment() {
			@Override
			public boolean tearDown(AbstractBuild build, BuildListener listener) throws IOException, InterruptedException {
				return true;
			}
		};
	}

	public String getCategories() {
		return categories;
	}

	public Category findCategories() {
		return Category.parse(this.getCategories());
	}

	@Extension
	public static class DescriptorImpl extends BuildWrapperDescriptor {
		@Override
		public boolean isApplicable(AbstractProject<?, ?> item) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Enable reason tracker";
		}
	}
}
