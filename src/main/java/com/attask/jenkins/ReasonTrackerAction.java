package com.attask.jenkins;

import hudson.model.Action;
import hudson.model.Build;
import hudson.model.Run;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * User: Joel Johnson
 * Date: 9/11/12
 * Time: 1:10 PM
 */
public class ReasonTrackerAction implements Action {
	private final String buildId;
	private final Category category;

	public ReasonTrackerAction(Run run, Category category) {
		this.buildId = run.getExternalizableId();
		this.category = category;
	}

	public String getBuildId() {
		return buildId;
	}

	public Category getCategory() {
		return category;
	}

	public Run findRun() {
		return Build.fromExternalizableId(buildId);
	}

	public List<Reason> findReasons() {
		Run run = findRun();
		return Collections.unmodifiableList(run.getActions(Reason.class));
	}

	public void doSubmit(StaplerRequest request, StaplerResponse response) throws IOException {
		if(!"POST".equals(request.getMethod().toUpperCase())) {
			throw new IllegalArgumentException("Must be POST");
		}
		String notes = request.getParameter("notes");
		Enumeration parameterNames = request.getParameterNames();
		Map<String, String> categoryMap = new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String o1, String o2) {
				if(o1 == null) {
					if(o2 == null) {
						return 0;
					} else {
						return -(o2.compareTo(o1));
					}
				}
				return o1.compareTo(o2);
			}
		});
		while(parameterNames.hasMoreElements()) {
			String name = (String)parameterNames.nextElement();
			if(name.startsWith("dropdown_")) {
				String[] parameters = request.getParameterValues(name);
				for (String parameter : parameters) {
					if(!"{null}".equals(parameter)) {
						categoryMap.put(name, parameter);
						break;
					}
				}
			}
		}
		List<String> categories = new ArrayList<String>(categoryMap.values());
		Run run = findRun();
		Reason reason = new Reason(notes, categories);
		Reason action = run.getAction(Reason.class);
		if(action != null) {
			run.getActions().remove(action);
		}
		run.addAction(reason);
		run.save();

		String contentType = request.getContentType();
		if(contentType.contains("json")) {
			JSONObject object = JSONObject.fromObject(reason);
			ServletOutputStream outputStream = response.getOutputStream();
			try {
				outputStream.print(object.toString());
			} finally {
				outputStream.flush();
				outputStream.close();
			}
		} else {
			response.sendRedirect(".");
		}
	}

	public String getIconFileName() {
		return "/plugin/Reason/icon.png";
	}

	public String getDisplayName() {
		return "Reason Tracker";
	}

	public String getUrlName() {
		return "reasonTracker";
	}
}
