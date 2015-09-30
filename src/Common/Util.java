package Common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Util {
	public static String OutputPath = "/USTPoint";
	public static String BasicInfoFileName = "part-r-00000";
	public static String HadoopServer = "hadoop1";
	public static BasicInfo basicInfo = new BasicInfo();

	public static String GetSoredDataPath(String string) {
		return OutputPath + "/" + Util.GetFileName(string) + "/SortedData";
	}
	public static String GetIndexPath(String string) {
		return OutputPath + "/" + Util.GetFileName(string) + "/Index";
	}
	
	public static String GetIndexPath(String string, int level) {
		return OutputPath + "/" + Util.GetFileName(string) + "/Index" + level;
	}
	
	public static String GetFileName(String string) {
		String [] strs = string.split("/");
		return strs[strs.length - 1];
	}
	
	public static BasicInfo GetBasicInfo(String fileName) throws IOException {
		BasicInfo info = new BasicInfo();

		Configuration config = new Configuration();

		String path = Util.OutputPath + "/" + fileName + "/"+ BasicInfoFileName;
		Path filenamePath = new Path("hdfs://" + HadoopServer + ":9000" + path);

		FileSystem fs = filenamePath.getFileSystem(config);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(filenamePath)));			String str;
			while((str=br.readLine())!=null){
				String[] strs = str.trim().split("\t");
				if (strs[0].compareTo("MinX") == 0) info.minX = Double.parseDouble(strs[1]);
				if (strs[0].compareTo("MinY") == 0) info.minY = Double.parseDouble(strs[1]);
				if (strs[0].compareTo("MaxX") == 0) info.maxX = Double.parseDouble(strs[1]);
				if (strs[0].compareTo("MaxY") == 0) info.maxY = Double.parseDouble(strs[1]);
				if (strs[0].compareTo("DataCnt") == 0) info.dataCnt = Double.parseDouble(strs[1]);
				if (strs[0].compareTo("Depth") == 0) info.depth = Integer.parseInt(strs[1]);
			}
			br.close();

		} catch(Exception ex){
			System.out.println(ex.toString());
			return null;
		}
		
		return info;
	}
	
	public static BasicInfo SetDepthToBasicInfo(String fileName, int depth) throws IOException {
		BasicInfo info = new BasicInfo();

		Configuration config = new Configuration();

		String path = Util.OutputPath + "/" + fileName + "/"+ BasicInfoFileName;
		Path filenamePath = new Path("hdfs://" + HadoopServer + ":9000" + path);

		FileSystem fs = filenamePath.getFileSystem(config);

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fs.append(filenamePath)));
			bufferedWriter.write("\r\nDepth\t" + depth);
			bufferedWriter.close();

		} catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		return info;
	}

	public static int GetIndexOfLevel(BasicInfo bi, PointKey pointKey, int level) {
		double pX = pointKey.GetX();
		double pY = pointKey.GetY();
		if (bi == null){
			System.out.println("BasicInfo is Null!!!! " + pointKey);
			return 500000;
		}
		if (bi.depth == 0) {
			System.out.println("depth is 0!!!! " + pointKey);
			return -1;
		}
		
		if (bi.minX > pX || bi.maxX < pX || bi.minY > pY || bi.maxY < pY) {
			System.out.println("Out of Range!!!! " + pointKey);
			return 500000;
		}
		
		BasicInfo tmpBI = new BasicInfo(bi);
		
		if (level == 0)
			return 0;
		if (level == 1)
			return 1;
		
		double rangeX, rangeY;
		int preIdx = 1;
		int offsetIdx = 0;
		for (int i = 2; i <= level ; i++) {
			offsetIdx = (preIdx - 1) * 4; 
			
			rangeX = tmpBI.maxX - tmpBI.minX;
			rangeY = tmpBI.maxY - tmpBI.minY;
			double midX = tmpBI.minX + rangeX / 2.0;
			double midY = tmpBI.minY + rangeY / 2.0;
			
			if (pX < midX) {
				tmpBI.maxX = midX;
				if (pY < midY) {
					tmpBI.maxY = midY;
					preIdx = 3;
				} else {
					tmpBI.minY = midY;
					preIdx = 1;
				}
			} else {
				tmpBI.minX = midX;
				if (pY < midY) {
					tmpBI.maxY = midY;
					preIdx = 4;
				} else {
					tmpBI.minY = midY;
					preIdx = 2;
				}
			}
			preIdx = offsetIdx + preIdx; 
		}
		return preIdx;
	}

}
