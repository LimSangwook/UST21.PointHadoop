package Common;

public class BasicInfo {
	public BasicInfo() {
	}
	public BasicInfo(BasicInfo bi) {
		minX = bi.minX;
		maxX = bi.maxX;
		minY = bi.minY;
		maxY = bi.maxY;
		dataCnt = bi.dataCnt;
		depth = bi.depth;
	}
	
	public String toString() {
		String ret = "MinX : " + minX;
		ret += "\tMinY : " + minY;
		ret += "\tMaxX : " + maxX;
		ret += "\tMaxY : " + maxY;
		ret += "\tDataCnt : " + dataCnt;
		ret += "\tDetpth : " + depth;
		return ret;
	}
	public double minX = 0.0;
	public double maxX = 0.0;
	public double minY = 0.0;
	public double maxY = 0.0;
	public double dataCnt = 0;
	public int depth = 0;
}
