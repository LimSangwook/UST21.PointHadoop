package Test;

import Common.BasicInfo;
import Common.PointKey;
import Common.Util;

public class LoadBasicInfo {
	public static void main(String[] args) throws Exception {
		try {
			Util.basicInfo = Util.GetBasicInfo("1m.xyz");
			BasicInfo bi = Util.basicInfo;
			bi.depth = 7;
			PointKey pt1 = new PointKey(145813.03, 4144053.353 );
			PointKey pt2 = new PointKey(146917.705, 4144047.0);
			
			int k = Util.GetIndexOfLevel(bi, pt1, 8);
			System.out.println("LevelIndex :  " + k);
			int k1 = Util.GetIndexOfLevel(bi, pt2, 10);
			System.out.println("LevelIndex :  " + k1);
			int ret = pt1.compareTo(pt2);
//			int ret = Integer.compare(k1, k);
			System.out.println("ret " + ret);
		} catch (Exception e) {
			System.out.println("error " + e.toString());
		}
	}
}