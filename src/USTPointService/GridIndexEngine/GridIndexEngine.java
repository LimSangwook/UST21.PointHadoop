package USTPointService.GridIndexEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import Common.BasicInfo;
import Common.IndexData;
import Common.PointData;
import Common.Util;
import Index.SplitHadoopFile;

public class GridIndexEngine {
	HashMap<String, BasicInfo> m_pointInfoMap = new HashMap<String, BasicInfo>();
	HashMap<String, TreeMap<Integer, IndexData>> m_pointIndexMap = new HashMap<String, TreeMap<Integer, IndexData>>();
	
	public boolean SetUsePointName(String pointName) {
		boolean ret = false;
		if ((ret = LoadInfo(pointName)) == true) {
			ret = LoadIndex(pointName);
		}
		return ret;
	}
	
	public ArrayList<PointData>  LoadPointData(String pointName, ArrayList<Integer> idxKeyList) {
		ArrayList<PointData> retPtList = new ArrayList<PointData> ();

		// 읽어들일 파일별로 인덱스를 정렬한다.
		TreeMap<Integer, ArrayList<IndexData>> fileNumIndexKeyListMap = new TreeMap<Integer, ArrayList<IndexData>>();
		
		for (Integer idxKey : idxKeyList) {
			TreeMap<Integer, IndexData> IndexMap = GetIndexData(pointName);
			IndexData idxData = IndexMap.get(idxKey.intValue());
			int fileNum = GetFileNumFromOffset(idxData.offset);
			if (idxData.offset == 1302574521) {
				System.out.println(idxData);
			}
			if (fileNumIndexKeyListMap.containsKey(fileNum) == true) {
				fileNumIndexKeyListMap.get(fileNum).add(idxData);
			} else {
				ArrayList<IndexData> list = new ArrayList<IndexData>();
				list.add(idxData);
				fileNumIndexKeyListMap.put(fileNum, list);
			}
		}
		
		Set<Integer> keySet = fileNumIndexKeyListMap.keySet();
		Iterator<Integer> iter = keySet.iterator();
		while (iter.hasNext()) {
			Integer fileNum = iter.next();
			ArrayList<PointData> ptList = GetPointDataFromFile(pointName, fileNum, fileNumIndexKeyListMap.get(fileNum));
			retPtList.addAll(ptList);
		}
		return retPtList;
	}
	private ArrayList<PointData> GetPointDataFromFile(String pointName, Integer fileNum, ArrayList<IndexData> arrayList) {
		System.out.print("Load FileNumber " + fileNum + " Indexes : " + arrayList.size() + " \t");
		ArrayList<PointData> ptList = new ArrayList<PointData>();
		Configuration config 		= new Configuration();
		int idxFileNum 				= fileNum;
		Path filenamePath = null;

		String strIdxFileNum			= Integer.toString(idxFileNum);
		while (strIdxFileNum.length() < 5) strIdxFileNum = "0" + strIdxFileNum;
		filenamePath 			= new Path("hdfs://" 
											+ Util.HadoopServer + ":9000" 
											+ Util.GetSoredDataPath(pointName)+"/data"+strIdxFileNum+".txt");

		FileSystem fs;
		try {
			fs = filenamePath.getFileSystem(config);
		
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(filenamePath)));
			Iterator<IndexData> iter = arrayList.iterator();
			br.mark(SplitHadoopFile.FileSize);
			while (iter.hasNext()) {
				IndexData idxData = iter.next();
				int readCnt = 0;
				String str;
				long offset = idxData.offset % SplitHadoopFile.FileSize; 
				br.reset();
				br.skip(offset);
				while((str=br.readLine())!=null && readCnt < idxData.cnt){
					String [] strs = str.split(" ");
					if (strs.length == 3) {
						try {
							PointData pt = new PointData(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]), Float.parseFloat(strs[2]));
							ptList.add(readCnt, pt);
							readCnt ++;
						}catch (NumberFormatException e) {
							System.out.println(e.toString());
						}
					}
				}
				if (readCnt < idxData.cnt) {
					
				}
				
				idxFileNum ++;
				System.out.print(".");
			}
			System.out.println("\t End ");
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ptList;
	}

	public ArrayList<PointData>  LoadPointData(String pointName, IndexData idxData) {
		ArrayList<PointData> ptList = new ArrayList<PointData>();
		Configuration config 		= new Configuration();
		int idxFileNum 				= GetFileNumFromOffset(idxData.offset);
		Path filenamePath = null;

		try {
			int readCnt = 0;
			String str = "";
			String preStr = "";
			long offset = idxData.offset;
			while (readCnt < idxData.cnt) {
				String strIdxFileNum			= Integer.toString(idxFileNum);
				while (strIdxFileNum.length() < 5) strIdxFileNum = "0" + strIdxFileNum;
				offset = offset % SplitHadoopFile.FileSize;
				filenamePath 			= new Path("hdfs://" 
													+ Util.HadoopServer + ":9000" 
													+ Util.GetSoredDataPath(pointName)+"/data"+strIdxFileNum+".txt");
				FileSystem fs = filenamePath.getFileSystem(config);
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(filenamePath)));

				br.skip(offset);
				boolean firstData =true;
				while((str=br.readLine())!=null && readCnt < idxData.cnt){
					if (firstData == true ) {
						if (str.split(" ").length ==3 && preStr.split(" ").length == 3) {}
						else 							
							str = preStr + str;
					}
					String [] strs = str.split(" ");
					if (strs.length == 3) { 
						PointData pt = new PointData(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]), Float.parseFloat(strs[2]));
						if (firstData == true && preStr.length() > 0 && ptList.size() == readCnt && preStr.split(" ").length == 3) {
							readCnt--;
							ptList.set(readCnt, pt);
						} else {
							ptList.add(readCnt, pt);
						}						
						readCnt ++;
						firstData = false;
					}
					
					if (readCnt% 100000 == 0) {
						System.out.print(".");
					}
					preStr = str;
				}
				idxFileNum ++;
				br.close();
				offset = 0;
			}
		} catch(Exception ex){
			System.out.println("LoadPointData ERR : " + filenamePath + ex.toString());
			return null;
		}
		
		return ptList;
	}
	
	private int GetFileNumFromOffset(long offset) {
		return (int)(offset / SplitHadoopFile.FileSize);
	}

	public BasicInfo GetPointInfo(String pointName) { 
		return m_pointInfoMap.get(pointName);
	}
	
	public TreeMap<Integer, IndexData> GetIndexData(String pointName) { 
		return m_pointIndexMap.get(pointName);
	}
	
	private boolean LoadInfo(String pointName) {
		BasicInfo bi;
		try {
			bi = Util.GetBasicInfo(pointName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (bi == null) return false;

		m_pointInfoMap.put(pointName, bi);
		System.out.println();
		System.out.println("### " + pointName + " PointSet Information ###");
		System.out.println(bi.toString());
		System.out.println();
		
		return true;
	}

	private boolean LoadIndex(String pointName) {
		System.out.println("### Start Loading Index ###");
		Configuration config = new Configuration();
		int level = m_pointInfoMap.get(pointName).depth;
		String path = Util.GetIndexPath(pointName, level);
		TreeMap<Integer, IndexData> idxDataMap = new TreeMap<Integer, IndexData>();	
		
		Path filenamePath = new Path("hdfs://" + Util.HadoopServer + ":9000" + path+"/part-r-00000");
		try {
			FileSystem fs = filenamePath.getFileSystem(config);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(filenamePath)));
			String str;
			while((str=br.readLine())!=null){
				String[] strs = str.trim().split(" ");
				int indexNum = Integer.parseInt(strs[0]);
				IndexData idxData = IndexData.ParseFromIndexFile(strs);
				idxDataMap.put(indexNum, idxData);
			}
			br.close();
		} catch(Exception ex){
			System.out.println("LoadIndex Err: " + ex.toString());
			return false;
		}
		m_pointIndexMap.put(pointName, idxDataMap);
		System.out.println("### End Loading Index, Index Size : " + idxDataMap.size());
		return true;
	}

}
