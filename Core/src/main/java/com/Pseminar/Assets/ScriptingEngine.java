package com.Pseminar.Assets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.Pseminar.Logger;
import com.Pseminar.ECS.BuiltIn.BaseComponent;

public class ScriptingEngine {
    private ClassLoader classLoader;
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

    /** 
     * @return ClassLoader
     */
    public ClassLoader GetClassLoader() {
        return this.classLoader;
    }
        
    /** 
     * @param jarPath
     */
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

    /** 
     * @param ClassName
     * @return Class<?>
     */
    public Class<?> GetBaseClass(String ClassName) {
        return ComponentDictionary.get(ClassName);
    }

    /**
     * @return die hoffentlich einzige scripting engine damit man die componments jar nicht öfters laden muss
     */
    public static ScriptingEngine GetInstance() {
        return INSTANCE;
    }

    /** 
     * @return Set<String>
     */
    public Set<String> GetClasses() {
        return this.ComponentDictionary.keySet();
    }

    public static void InitEditor() {
        new ScriptingEngine(ProjectInfo.GetProjectInfo().GetComoponentJarPath());
    }

    /** 
     * @param componentJar
     */
    public static void InitRuntime(byte[] componentJar) {
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            public URLStreamHandler createURLStreamHandler(String urlProtocol) {
                if ("customJarLoader".equalsIgnoreCase(urlProtocol)) {
                    return new URLStreamHandler() {
                        @Override
                        protected URLConnection openConnection(URL url) throws IOException {
                            return new URLConnection(url) {
                                public void connect() throws IOException {}
                                public InputStream getInputStream() throws IOException {
                                    return new ByteArrayInputStream(componentJar);
                                }
                            };
                        }
                    };
                }
                return null;
            }
        });

        new ScriptingEngine("customJarLoader:xyz");
    }
}
