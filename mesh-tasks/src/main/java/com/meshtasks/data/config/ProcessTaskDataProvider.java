package com.meshtasks.data.config;

import java.util.HashMap;
import java.util.Map;

import com.meshtasks.metadata.MeshTask;

public class ProcessTaskDataProvider {

	private static ProcessTaskDataProvider appDataProvider = new ProcessTaskDataProvider();
	
	private final Map<String, MeshTask> taskNameMap;
	
	private ProcessTaskDataProvider() {
		taskNameMap = new HashMap<String, MeshTask>(5);
	}
	
	public static ProcessTaskDataProvider getInstance() {
		return appDataProvider;
	}

	public boolean addTask( MeshTask meshTask ) {
		return taskNameMap.put(meshTask.getTaskName(), meshTask) != null;
	}
	
}