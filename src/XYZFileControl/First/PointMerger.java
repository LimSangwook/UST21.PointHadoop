package XYZFileControl.First;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PointMerger {
	// -------------------------------------
	// FileListPath 내의 모든 파일을 읽어 
	// outPutFile 하나로 Merge 한다.
	// -------------------------------------
	static int cnt = 0;
	static int cntLine = 0;
	public static void main(String []args) {
		String FileListPath = "/home/iswook/PointList/3.5000_SUPPRESS_SO/ALL_LL";
		String outPutFile = "/home/iswook/PointList/3.5000_SUPPRESS_SO/ALL_LL.xyz";
		
		
		MergeFileList(FileListPath, outPutFile);
		System.out.println("cntLine : " + cntLine);
	}

	private static boolean MergeFileList(String fileListPath, String outPutFile) {
		File dir = new File(fileListPath);
		File[] fileList = dir.listFiles();
		
		for (File file : fileList) {
			if(file.isFile()) {
				try {
					System.out.println(++cnt + " File : " + file.getCanonicalPath().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				WriteMergeFile(file, outPutFile);
			}
			else if(file.isDirectory()) {
				try {
					MergeFileList(file.getCanonicalPath().toString(), outPutFile);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}			
			}
		} 
		return true;
	}

	private static void WriteMergeFile(File file, String outPutFile) {
		FileReader in = null;
		BufferedReader reader = null;
		FileWriter out = null;
		BufferedWriter writer = null;
		
		try {
			out = new FileWriter(outPutFile, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer = new BufferedWriter(out);		
		
		try {
			in = new FileReader(file);
			reader = new BufferedReader(in);
			String str;
			while (true) {
				str = reader.readLine();
				if (str == null) 
					break;
				cntLine ++;
				writer.write(str);
				writer.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.flush();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
