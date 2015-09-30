package Index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import Common.Util;

public class SplitHadoopFile {
	public static int FileSize = 64*1024*1024;
	public static void main(String [] args){
		long startTime = System.currentTimeMillis();
		String pointName = "1m.xyz";
		Configuration config 		= new Configuration();
		config.setInt("dfs.block.size", FileSize);
		Path readFilePath 		= new Path("hdfs://" 
											+ Util.HadoopServer + ":9000" 
											+ Util.GetSoredDataPath(pointName)+"/part-r-00000");
		try {
			FileSystem rfs = readFilePath.getFileSystem(config);
			BufferedReader br = new BufferedReader(new InputStreamReader(rfs.open(readFilePath)));

			int writeFileIndex = 0;
			String writeIndexStr = Integer.toString(writeFileIndex);
			while (writeIndexStr.length() < 5) writeIndexStr = '0' + writeIndexStr;
			
			Path writeFilePath = new Path("hdfs://" 
					+ Util.HadoopServer + ":9000" 
					+ Util.GetSoredDataPath(pointName)+"/data"+ writeIndexStr + ".txt");

			FileSystem wfs = 	writeFilePath.getFileSystem(config);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(wfs.create(writeFilePath)));

			char[] cbuf = new char[FileSize];
			int readLen = 0;
			int totReadLen = 0;
			while ((readLen = br.read(cbuf)) > 0) {
				if ((totReadLen + readLen)/FileSize >= 1) {
					bw.write(cbuf,0,FileSize - totReadLen);
					bw.close();
				
					writeIndexStr = Integer.toString(++writeFileIndex);
					while (writeIndexStr.length() < 5) writeIndexStr = '0' + writeIndexStr;
					
					writeFilePath = new Path("hdfs://" 
							+ Util.HadoopServer + ":9000" 
							+ Util.GetSoredDataPath(pointName)+"/data"+ writeIndexStr+".txt");
					System.out.println("Create " + writeIndexStr);
					wfs = 	writeFilePath.getFileSystem(config);
					bw = new BufferedWriter(new OutputStreamWriter(wfs.create(writeFilePath)));
					bw.write(cbuf, FileSize - totReadLen, readLen - FileSize - totReadLen);
					totReadLen = (totReadLen + readLen) % FileSize;
					
				} else {
					bw.write(cbuf, 0, readLen);
					totReadLen += readLen;
				}
			}
			bw.close();
			br.close();
		} catch(Exception ex){
			System.out.println("ERR : " + ex.toString());
		}
		long endTime = System.currentTimeMillis(); 
		System.out.println((endTime-startTime)/1000.0 +" 초 걸림");
	}
}
