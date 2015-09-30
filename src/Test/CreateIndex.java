package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import Common.BasicInfo;
import Common.PointKey;
import Common.Util;

public class CreateIndex {
	private static final String HadoopServer = "hadoop1";

	public static void main(String[] args) throws Exception {
		String fileName = "ust_1m.xyz";
//		HashMap<Integer, IndexData> indexMap = new HashMap<Integer, IndexData>();
		BasicInfo basicInfo = Util.GetBasicInfo(fileName);

		Configuration config = new Configuration();

		String path = Util.GetSoredDataPath(fileName) + "/part-r-00000";
		Path filenamePath = new Path("hdfs://" + HadoopServer + ":9000" + path);

		FileSystem fs = filenamePath.getFileSystem(config);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(filenamePath)));			
//			String str;
			int k = 0;
			while((br.readLine())!=null){
//				String [] strs = str.split(" ");
						
				
				k++;
//				if (k % 1000000 == 0 ) {
//					System.out.print("@");
//				}
				int lv = Util.GetIndexOfLevel(basicInfo, new PointKey(148340.079, 4143037.185), 7);
				System.out.println("LevelIndex :  " + lv);
			}
			System.out.println("");
			System.out.println("Cnt : " + k);
			br.close();

		} catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
}
