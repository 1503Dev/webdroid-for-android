package tc.webdroid;
import android.content.res.*;
import android.content.*;
import java.io.*;
import tc.dedroid.util.*;
import android.util.*;
import java.util.zip.*;
import android.widget.*;
import java.nio.charset.*;
import android.graphics.*;
import javax.security.cert.*;
import java.security.*;
import java.security.cert.CertificateException;

public class Utils{
	public static void copyAssetToExternalStorage(Context context, String assetPath, String outputPath) throws IOException {
		AssetManager assetManager = context.getAssets();
		File outputFile = new File(outputPath);
		try (
		InputStream in = assetManager.open(assetPath);
		OutputStream out = new FileOutputStream(outputFile)
		) {
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			out.flush();
			Log.d("TAG", "File copied to: " + outputFile.getAbsolutePath());
		}
	}
	public static void zipFolderWithoutRoot(String srcFolderPath, String destZipFilePath) throws IOException {
		FileOutputStream fos = new FileOutputStream(destZipFilePath);
		ZipOutputStream zos = new ZipOutputStream(fos);

		File sourceFolder = new File(srcFolderPath);
		if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
			throw new IllegalArgumentException("The provided source folder path does not exist or is not a directory.");
		}

		// We remove the last segment of the source folder path to exclude it from the ZIP entries.
		String rootPathToRemove = sourceFolder.getAbsolutePath() + File.separator;

		addFolderContentsToZip(sourceFolder, "", rootPathToRemove, zos);

		zos.close();
		fos.close();
	}

	private static void addFolderContentsToZip(File folder, String parentPath, String rootPathToRemove, ZipOutputStream zos) throws IOException {
		for (File file : folder.listFiles()) {
			String filePath = file.getAbsolutePath();
			String zipEntryPath = filePath.substring(rootPathToRemove.length(), filePath.length());

			if (file.isDirectory()) {
				addFolderContentsToZip(file, zipEntryPath + File.separator, rootPathToRemove, zos);
			} else {
				FileInputStream fis = new FileInputStream(file);
				ZipEntry zipEntry = new ZipEntry(zipEntryPath);
				zos.putNextEntry(zipEntry);

				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zos.write(bytes, 0, length);
				}

				zos.closeEntry();
				fis.close();
			}
		}
	}
	public static void copyDirectory(File sourceDir, File targetDir) throws IOException {
		// 确保目标目录存在
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		// 遍历源目录
		for (File file : sourceDir.listFiles()) {
			// 创建目标文件或目录的路径
			File targetFile = new File(targetDir, file.getName());

			if (file.isDirectory()) {
				// 如果是目录，递归复制
				copyDirectory(file, targetFile);
			} else {
				// 如果是文件，直接复制文件内容
				try (FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(targetFile)) {

					byte[] buffer = new byte[1024];
					int length;

					while ((length = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, length);
					}
				}
			}
		}
	}

	public static String readAssetsFile(AssetManager assetManager, String path) {
		StringBuilder fileContent = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(path)));
			String line;
			while ((line = reader.readLine()) != null) {
				fileContent.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent.toString();
	}
    public static void unzip(String zipFileString, String outputFolderString) throws IOException {
        File destDir = new File(outputFolderString);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry entry = zipIn.getNextEntry();

        while (entry != null) {
            String filePath = outputFolderString + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                extractFile(zipIn, filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }

        zipIn.close();
    }

    /**
     * 提取单个条目
     * @param zipIn ZIP输入流
     * @param filePath 输出文件路径
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	public static void sign(Context ctx){
		// Under development
		try {
            copyAssetToExternalStorage(ctx,"defualt_key.keystore",DedroidFile.EXTERN_STO_PATH+"/1503Dev/webdroid_key.keystore");
			// 加载KeyStore
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream fileInputStream = new FileInputStream(DedroidFile.EXTERN_STO_PATH+"/1503Dev/webdroid_key.keystore");
			char[] password = "Webdroid".toCharArray();
			keystore.load(fileInputStream, password);

			// 获取私钥
			String alias = "Webdroid";
			PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, "Webdroid".toCharArray());
            privateKey.hashCode();
			System.out.println("Private key loaded successfully.");

		} catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException |
		UnrecoverableKeyException e) {
			e.printStackTrace();
		}
	}
}
