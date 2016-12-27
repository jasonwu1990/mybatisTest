package com.jason.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public class ScanUtils {

	public static Set<Class<?>> getClasses(String pack) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageName = pack;
		String packageDirName = packageName.replace(".", "/");
		
		try {
			Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while(dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				
				if("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					loadClassInFile(filePath, packageName, classes);
				}//else if("jar".equals(protocol)) {
//					JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
//					Enumeration<JarEntry> entries = jar.entries();
//					while(entries.hasMoreElements()) {
//						JarEntry entry = entries.nextElement();
//						String name = entry.getName();
//					}
//				}
//				TODO: jar包加载回头加上
				else if("class".equals(protocol)) {
					try {
						String className = url.getFile();
						classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	
	private static void loadClassInFile(String filePath, String packageName, Set<Class<?>> classes) {
		File dir = new File(filePath);
		if((!dir.exists()) || (!dir.isDirectory())) {
			return;
		}
		
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().endsWith(".class");
			}
			
		});
		
		for(File file : files) {
			if(file.isDirectory()) {
				loadClassInFile(filePath, packageName, classes);
			}else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName+"."+className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
	}
 	
}
