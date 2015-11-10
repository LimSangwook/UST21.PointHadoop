package Common;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class PointKey implements WritableComparable<PointKey>{
	protected double pX;
	protected double pY;

	public PointKey() {}
	
	public PointKey(Double x, Double y) {
		pX = x;
		pY = y;
	}
	
	public double GetX() { return pX;}
	public double GetY() { return pY;}
	

	public String toString() {
		return Double.toString(pX) + " " + Double.toString(pY);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		pX = arg0.readDouble();
		pY = arg0.readDouble();
	}
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeDouble(pX);
		arg0.writeDouble(pY);
	}
	
	@Override
	public int compareTo(PointKey o) {
		if (Util.basicInfo == null) {
			try {
				Util.basicInfo = Util.GetBasicInfo("ust_1m.xyz");
				Util.basicInfo.depth = 10;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (Util.basicInfo.depth < 10) {
			Util.basicInfo.dataCnt = 168829013;
			Util.basicInfo.minX = 145470.781;
			Util.basicInfo.maxX = 167952.758;
			Util.basicInfo.minY = 4137365.808;
			Util.basicInfo.maxY = 4144104.797;
			
			Util.basicInfo.depth = 10;
		}
		int l1 = Util.GetIndexOfLevel(Util.basicInfo, this, Util.basicInfo.depth);
		int l2 = Util.GetIndexOfLevel(Util.basicInfo, o, Util.basicInfo.depth);
		int ret = Integer.compare(l1, l2);
		if (ret == 0) {
			ret = Double.compare(pX, o.pX);
			if (ret == 0) {
				ret = Double.compare(-pY, -o.pY);
			}
		}
//		System.out.println("##### " + this.toString() + "," + o.toString()+" : "+ret +"\tL1: "+l1 + "\tL2 : "+l2 + " : " + Util.basicInfo.toString());
		return ret;
	}
}
