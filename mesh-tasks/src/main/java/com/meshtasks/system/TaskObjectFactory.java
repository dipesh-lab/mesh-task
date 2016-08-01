package com.meshtasks.system;

import java.util.HashMap;
import java.util.Map;


public class TaskObjectFactory {

	private static TaskObjectFactory OBJECT_FACTORY = new TaskObjectFactory();
	
	private Map<String, Object> TASK_NAME_MAP = new HashMap<String, Object>();
	
	private final TaskClassLoader taskClassLoader;
	
	private TaskObjectFactory() {
		taskClassLoader = new TaskClassLoader();
	}
	
	public static TaskObjectFactory getInstance() {
		return OBJECT_FACTORY;
	}
	
	public void loadTaskClass( String taskName ) {
		Class<?> classObj = taskClassLoader.loadClassToSystem(taskName);
		if ( classObj != null ) {
			System.out.println("Task Class is loaded...");
			try {
				Object taskObject = classObj.newInstance();
				TASK_NAME_MAP.put(taskName, taskObject);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}