package com.lppz.util.javafork;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Christopher Bartling, Pintail Consulting LLC
 * @since  Oct 4, 2008
 */
public class JavaProcessBuilder {

    private String mainClass;
    private int startingHeapSizeInMegabytes = 40;
    private int maximumHeapSizeInMegabytes = 128;
    private String workingDirectory;
    private List<String> classpathEntries = new ArrayList<String>();
    private List<String> mainClassArguments = new ArrayList<String>();
    private String javaRuntime = "java";

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public int getStartingHeapSizeInMegabytes() {
        return startingHeapSizeInMegabytes;
    }

    public void setStartingHeapSizeInMegabytes(int startingHeapSizeInMegabytes) {
        this.startingHeapSizeInMegabytes = startingHeapSizeInMegabytes;
    }

    public int getMaximumHeapSizeInMegabytes() {
        return maximumHeapSizeInMegabytes;
    }

    public void setMaximumHeapSizeInMegabytes(int maximumHeapSizeInMegabytes) {
        this.maximumHeapSizeInMegabytes = maximumHeapSizeInMegabytes;
    }

    private String getClasspath() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        final int totalSize = classpathEntries.size();
        for (String classpathEntry : classpathEntries) {
            builder.append(classpathEntry);
            count++;
            if (count < totalSize) {
                builder.append(System.getProperty("path.separator"));
            }
        }
        return builder.toString();
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void addClasspathEntry(String classpathEntry) {
        this.classpathEntries.add(classpathEntry);
    }

    public void addArgument(String argument) {
        this.mainClassArguments.add(argument);
    }

    public void setJavaRuntime(String javaRuntime) {
        this.javaRuntime = javaRuntime;
    }

    public Process startProcess() throws IOException {
        List<String> argumentsList = new ArrayList<String>();
        argumentsList.add(this.javaRuntime);
        argumentsList.add(MessageFormat.format("-Xms{0}M", String.valueOf(this.startingHeapSizeInMegabytes)));
        argumentsList.add(MessageFormat.format("-Xmx{0}M", String.valueOf(this.maximumHeapSizeInMegabytes)));
        argumentsList.add("-classpath");
        argumentsList.add(getClasspath());
        argumentsList.add(this.mainClass);
        for (String arg : mainClassArguments) {
            argumentsList.add(arg);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(argumentsList.toArray(new String[argumentsList.size()]));
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(this.workingDirectory));
        return processBuilder.start();
    }
}