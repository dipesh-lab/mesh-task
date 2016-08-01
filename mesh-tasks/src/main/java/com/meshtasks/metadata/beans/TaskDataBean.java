package com.meshtasks.metadata.beans;

import java.util.List;

public class TaskDataBean {

	private String taskName;
	private List<String> codeLines;
	private boolean activate;
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<String> getCodeLines() {
		return codeLines;
	}
	public void setCodeLines(List<String> codeLines) {
		this.codeLines = codeLines;
	}
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
}