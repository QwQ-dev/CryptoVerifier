package twomillions.other.cryptoverifier.plugin;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import twomillions.other.cryptoverifier.entrypoint.EntryPointInterface;
import twomillions.other.cryptoverifier.events.custom.LoadPluginsEvent;
import twomillions.other.cryptoverifier.events.impl.EventManager;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {
    @SneakyThrows
    public static void loadJarsFromDirectory(String directoryPath) {
        LoadPluginsEvent loadPluginsEvent = new LoadPluginsEvent(directoryPath);
        EventManager.getEventManager().callEvent(loadPluginsEvent);

        directoryPath = loadPluginsEvent.getDirectoryPath();

        File directory = new File(directoryPath);
        IOFileFilter jarFileFilter = FileFilterUtils.suffixFileFilter(".jar");
        IOFileFilter fileFilter = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), jarFileFilter);
        List<File> jarFiles = new ArrayList<>(FileUtils.listFiles(directory, fileFilter, null));

        jarFiles.sort((file1, file2) -> {
            String name1 = file1.getName();
            String name2 = file2.getName();

            int index1 = name1.indexOf('[');
            int index2 = name2.indexOf('[');

            if (index1 != -1 && index2 != -1) {
                String num1 = name1.substring(index1 + 1, name1.indexOf(']'));
                String num2 = name2.substring(index2 + 1, name2.indexOf(']'));

                try {
                    int val1 = Integer.parseInt(num1);
                    int val2 = Integer.parseInt(num2);
                    return Integer.compare(val1, val2);
                } catch (NumberFormatException ignore) {
                    // ignore
                }
            }

            return name1.compareTo(name2);
        });

        URL[] jarURLs = new URL[jarFiles.size()];

        for (int i = 0; i < jarFiles.size(); i++) {
            jarURLs[i] = jarFiles.get(i).toURI().toURL();
        }

        @Cleanup URLClassLoader classLoader = new URLClassLoader(jarURLs);

        for (File jarFile : jarFiles) {
            invokeOnLoadMethod(jarFile, classLoader);
        }
    }

    private static void invokeOnLoadMethod(File jarFile, ClassLoader classLoader) throws Exception {
        @Cleanup JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (!name.endsWith(".class")) {
                continue;
            }

            name = name.replace("/", ".").replaceAll("\\.class$", "");
            Class<?> loadedClass = classLoader.loadClass(name);

            if (!EntryPointInterface.class.isAssignableFrom(loadedClass)) {
                continue;
            }

            Object instance = loadedClass.getDeclaredConstructor().newInstance();
            Method onEnableMethod = loadedClass.getMethod("onLoad");
            onEnableMethod.invoke(instance);
        }
    }
}