package tc.dedroid.util;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.os.Environment;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DedroidFile {

    final public static String EXTERN_STO_PATH=Environment.getExternalStorageDirectory().getAbsolutePath();

    public static void mkdir(String path) {
        File file = new File(path); //以某路径实例化一个File对象
        file.mkdirs();
    }
    public static void mkdir(File file) {
        file.mkdirs();
    }
    public static File[] list(File f) {
        return f.listFiles();
    }
    public static File[] list(String f) {
        return new File(f).listFiles();
    }
    public static List<String> listName(File f) {
        File[] fs=f.listFiles();
        List<String> ss=new ArrayList<>();
        for (File fi: fs){
            ss.add(fi.getName());
        }
        return ss;
    }
    public static List<String> listName(String f) {
        return listName(new File(f));
    }
    public static boolean mkfile(String fileName, String defaultContent) {
        boolean r=false;
        File file = new File(fileName); //实例化File对象
        if (!file.exists()) {
            mkdir(file.getParentFile());
            try {
                r = file.createNewFile();
                write(fileName, defaultContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return r;
    }
    public static boolean mkfile(String fileName) {
        return mkfile(fileName, "");
    }
    public static void write(String filepath, String content) throws IOException {
        File file = new File(filepath); //以某路径实例化一个File对象
        if (!file.exists()) {
            mkfile(filepath);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) { 
            bufferedWriter.write(content); 
        } 
    }
    public static void write(String filePath, byte[] data) throws IOException {
        if (data == null || filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Data or filePath cannot be null/empty.");
        }

        File file = new File(filePath);
        File parentDir = file.getParentFile();

        // 确保父目录存在
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create parent directories for " + filePath);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            fos.flush();
        }
    }

    public static String read(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
            return contentBuilder.toString().trim();
        }
    }

    public static void copy(String sourcePath, String targetPath) {
        try {
            // 检查源文件是否存在
            File sourceFile = new File(sourcePath);
            if (sourceFile.exists()) {
                // 创建文件输入流对象
                FileInputStream inputFile = new FileInputStream(sourceFile);
                // 创建文件输出流对象
                FileOutputStream outputFile = new FileOutputStream(new File(targetPath));

                // 定义缓冲区
                byte[] buffer = new byte[1444];

                // 循环读取并写入直到文件结束
                int bytesRead;
                while ((bytesRead = inputFile.read(buffer)) != -1) {
                    outputFile.write(buffer, 0, bytesRead);
                }

                // 关闭流
                inputFile.close();
                outputFile.close(); // 添加关闭outputFile
            }
        } catch (Exception e) {
            // 打印异常堆栈信息
            e.printStackTrace();
        }
    }


    public static boolean exists(String fileName) {
        return new File(fileName).exists();
    }
    public static void del(String path) {
        File file=new File(path);
        if (file.isDirectory()) {
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File filei : files) {
                        if (filei.isDirectory()) {
                            // 如果是目录，则递归删除
                            del(filei.getPath());
                        } else {
                            // 如果是文件，则直接删除
                            filei.delete();
                        }
                    }
                }
                // 删除完目录内的所有文件后，再删除当前目录
                file.delete();
            }
        } else {
            file.delete();
        }
    }
    public static String getLargestFile(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("The specified directory does not exist or is not a directory.");
            return null;
        }

        File largestFile = findLargestFileInDirectory(directory);
        return largestFile != null ? largestFile.getName() : null;
    }

    private static File findLargestFileInDirectory(File directory) {
        List<File> files = new ArrayList<>();
        collectFiles(directory, files);

        if (files.isEmpty()) {
            return null;
        }

        Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return Long.compare(f2.length(), f1.length()); // 降序排序
                }
            });

        return files.get(0);
    }

    private static void collectFiles(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }
}
