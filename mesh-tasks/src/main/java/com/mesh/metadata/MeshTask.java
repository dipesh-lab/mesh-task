package com.mesh.metadata;

import com.mesh.metadata.beans.TaskStatus;

public interface MeshTask {

	public String getTaskName();
	
	public TaskStatus getStatus();
	
	public int getExecIndex();
	
	public Object executeTask(Object object);
	
}