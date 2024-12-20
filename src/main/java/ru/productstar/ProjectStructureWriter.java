package ru.productstar;// src/main/java/org.example.productstar.ProjectStructureWriter.java

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectStructureWriter {

    private static final String GIT_IGNORE_FILE = ".gitignore.ProjectStructureWriter";

    public static void writeProjectStructure(String projectPath, String outputFile) throws IOException {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не является директорией");
        }

        List<String> ignoredFiles = readIgnoredFiles(projectPath);
        writeProjectStructure(projectDir, ignoredFiles, outputFile);
    }

    private static List<String> readIgnoredFiles(String projectPath) throws IOException {
        Path gitIgnorePath = Paths.get(projectPath, GIT_IGNORE_FILE);
        if (Files.exists(gitIgnorePath)) {
            return Files.readAllLines(gitIgnorePath);
        } else {
            return new ArrayList<>();
        }
    }

    private static void writeProjectStructure(File dir, List<String> ignoredFiles, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writeProjectStructure(dir, ignoredFiles, writer, 0, new boolean[100]);
        }
    }

    private static void writeProjectStructure(File dir, List<String> ignoredFiles, FileWriter writer, int level, boolean[] isLast) throws IOException {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                if (!isIgnored(file, ignoredFiles)) {
                    writeDirectory(file, writer, level, isLast);
                    isLast[level] = false;
                    writeProjectStructure(file, ignoredFiles, writer, level + 1, isLast);
                }
            } else {
                if (!isIgnored(file, ignoredFiles)) {
                    writeFile(file, writer, level, isLast);
                }
            }
        }
    }

    private static boolean isIgnored(File file, List<String> ignoredFiles) {
        String fileName = file.getName();
        for (String ignoredFile : ignoredFiles) {
            if (fileName.equals(ignoredFile.trim())) {
                return true;
            }
        }
        return false;
    }

    private static void writeDirectory(File dir, FileWriter writer, int level, boolean[] isLast) throws IOException {
        writePrefix(writer, level, isLast);
        writer.write("📁 " + dir.getName() + "/\n");
        isLast[level] = true;
    }

    private static void writeFile(File file, FileWriter writer, int level, boolean[] isLast) throws IOException {
        writePrefix(writer, level, isLast);
        writer.write("📄 " + file.getName() + "\n");
        isLast[level] = false;
    }

    private static void writePrefix(FileWriter writer, int level, boolean[] isLast) throws IOException {
        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                writer.write(isLast[i] ? "└── " : "├── ");
            } else {
                writer.write(isLast[i] ? "    " : "│   ");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        writeProjectStructure(".", "project_structure.txt");
    }
}