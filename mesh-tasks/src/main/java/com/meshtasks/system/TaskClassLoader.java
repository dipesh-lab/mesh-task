package com.meshtasks.system;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;

import com.meshtasks.utils.CommonUtils;
import com.meshtasks.utils.TaskUtils;

public class TaskClassLoader {
	
	private CustomClassLoader customClassLoader = null;
	
	public TaskClassLoader() {
		customClassLoader = new CustomClassLoader( this.getClass().getClassLoader() );
	}
	
	public Class<?> loadClassToSystem( String className ) {
        String classPath = TaskUtils.getTaskClassDir() + File.separator + className +".class";
        System.out.println("Load Class Path "+classPath);
        byte[] classData = CommonUtils.getFileData(classPath);
        if ( classData == null || classData.length == 0 ) return null;
        return customClassLoader.loadTaskClass("com.meshtasks.taskclass." + className, classData);
	}
	
	private static class CustomClassLoader extends URLClassLoader {
		
		private static URL[] urls = new URL[1];
		
		public CustomClassLoader(ClassLoader parent) {
			super(urls, parent);
		}

		@Override
		public Class<?> findClass(String name) throws ClassNotFoundException {
			return super.findClass(name);
		}
		
		private Class<?> loadTaskClass( String fqdnName, byte[] data ) {
			return defineClass(fqdnName, ByteBuffer.wrap(data), this.getClass().getProtectionDomain());
		}
	}

}