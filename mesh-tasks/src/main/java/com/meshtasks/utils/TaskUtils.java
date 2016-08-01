package com.meshtasks.utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class TaskUtils {
	
	public static String joinCodeLines(List<String> lines) {
		StringBuilder content = new StringBuilder(lines.size() * 20);
		for( String line:lines ) content.append(line.replaceAll("\\\"", "\"")+"\n");
		return content.toString();
	}

	public static String getTaskClassDir() {
		File jarPath=new File(TaskUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String dirPath=jarPath.getParentFile().getAbsolutePath();
        String classPath = dirPath + File.separator + "taskconfig" + File.separator 
        	+ "com" + File.separator + "meshtasks" + File.separator + "taskclass";
		return classPath;
	}
	
	public static String getTaskSourceDir() {
		File jarPath=new File(TaskUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String dirPath=jarPath.getParentFile().getAbsolutePath();
        return dirPath + File.separator + "taskconfig";
	}
	
	public static boolean compileTaskCode( String taskName ) {
		Process process = null;
		String dirPath = TaskUtils.getTaskSourceDir();
        InputStream inStream = null;
        byte[] chunk = null;
        try {
            String[] compileCmds = { "javac", "-source", "1.6", "-d", dirPath, 
            		dirPath + File.separator + taskName+".java" };
            ProcessBuilder pb = new ProcessBuilder(compileCmds);
            process = pb.start();
            process.waitFor();
            inStream = process.getInputStream();
            chunk = CommonUtils.readInputStream(inStream);
        } catch ( Exception ioe ) {
        } finally {
            if ( process != null ) {
                process.destroy();
            }
        }
        if ( chunk == null || chunk.length == 0 ) return true;
		return false;
	}
	
}