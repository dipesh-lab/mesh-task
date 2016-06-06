package com.mesh.metadata.beans;


public class MeshTaskBean {

	private String taskId;
	private String taskName;
	private TaskStatus status;
	private int execIndex;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public int getExecIndex() {
		return execIndex;
	}
	public void setExecIndex(int execIndex) {
		this.execIndex = execIndex;
	}
}