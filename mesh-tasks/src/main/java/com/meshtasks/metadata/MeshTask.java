package com.meshtasks.metadata;

import com.meshtasks.metadata.beans.TaskStatus;

public interface MeshTask {

	public String getTaskName();
	
	public TaskStatus getStatus();
	
	public int getExecIndex();
	
	public Object executeTask(Object object);
	
}