package com.Pseminar.Assets;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.Pseminar.Logger;
import com.Pseminar.ECS.BuiltIn.BaseComponent;

public class ScriptingEngine {
    private URLClassLoader classLoader;
    private static ScriptingEngine INSTANCE;

    private Map<String, Class<?>> ComponentDictionary = new HashMap<>();

    /**
     * Erstellt die script engine
     * 
     * @param jarPath den path zu der jar wovon man die components laden soll
     */
    public ScriptingEngine(String jarPath) {
        INSTANCE = this;

        LoadJar(jarPath);
    }
        
    private void LoadJar(String jarPath) {
        try {
            URL jarFile = new File(jarPath).toURI().toURL();
			classLoader = new URLClassLoader(new URL[] { jarFile }, this.getClass().getClassLoader());

			JarFile jarFileContent = new JarFile(new File(jarPath));

			Enumeration<JarEntry> entries = jarFileContent.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					String className = entry.getName().replace("/", ".");
					className = className.substring(0, className.length() - 6);

					Class<?> clazz = classLoader.loadClass(className);

                    if(BaseComponent.class.isAssignableFrom(clazz)) {
                        Logger.info("loaded class " + className);
                        this.ComponentDictionary.put(className, clazz);
                    }
                }
			}

			jarFileContent.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * erstellt ein neues component als object basirend auf dem klassen name
     * 
     * @param ClassName der klassen name also com.abc.xyz
     * @return
     */
    public BaseComponent GetNewComponent(String ClassName) {
        if(!ComponentDictionary.containsKey(ClassName)) {
            Logger.error("Class " + ClassName + " not found");
            return null;
        }

        try {
            return (BaseComponent) this.classLoader.loadClass(ClassName).getConstructors()[0].newInstance();
        } catch (Exception e) {
            Logger.error("failed to load class " + ClassName);
            e.printStackTrace();

            return null;
        }
    }

    public static ScriptingEngine GetInstance() {
        return INSTANCE;
    }
}
