package com.meshtasks.taskoperation;

import com.meshtasks.metadata.beans.TaskDataBean;
import com.meshtasks.system.TaskObjectFactory;
import com.meshtasks.utils.CommonUtils;
import com.meshtasks.utils.TaskUtils;

public class InitTaskHandler {
	
	private TaskObjectFactory objectFactory = TaskObjectFactory.getInstance();

	public boolean addTask( boolean propogate, TaskDataBean dataBean ) {
		/**
		 * 1. Compile Task code
		 * 2. If valid then add to Object factory and inform nodes else return FALSE.
		 * 3. Return TRUE if Code is valid and compiled.
		 */
		boolean status = validateTaskCode(dataBean);
		System.out.println("Validate Task Status >> "+status);
		if ( status && propogate ) {
			/* Inform all other nodes about Task. */
		} else if ( status && !propogate ) {
			/* Add it in Object Factory */
			objectFactory.loadTaskClass(dataBean.getTaskName());
		}
		return status;
	}
	
	public boolean validateTaskCode( TaskDataBean dataBean ) {
		/**
		 * 1. Create Java class from code and write to File system
		 * 2. Compile Code
		 * 3. If its activate then Load it in ClassLoader
		 * */
		String taskDir = TaskUtils.getTaskSourceDir();
		String javaCode = TaskUtils.joinCodeLines(dataBean.getCodeLines());
		StringBuilder content = new StringBuilder(javaCode.length() + 120);
		content.append("package com.meshtasks.taskclass;\n");
		content.append(javaCode);
		CommonUtils.writeFile(taskDir, dataBean.getTaskName()+".java", content.toString());
		/* Compile Java class */
		return TaskUtils.compileTaskCode(dataBean.getTaskName());
	}
}