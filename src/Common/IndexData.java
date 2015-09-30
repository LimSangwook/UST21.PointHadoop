package Common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class IndexData implements Writable {
	public long offset;
	public long maxOffset;
	private PointData minPT = null;
	private PointData maxPT = null;
	public float avgZ;
	public int cnt;
	public PointData GetMinPT() {
		return minPT;
	}
	public PointData GetMaxPT() {
		return maxPT;
	}
	public IndexData() {
		minPT = new PointData();
		maxPT = new PointData();
	}
	public void SetMinPT(PointData pt) {
		minPT = new PointData(pt.GetX(), pt.GetY(), pt.GetZ());
	}
	public void SetMaxPT(PointData pt) {
		maxPT = new PointData(pt.GetX(), pt.GetY(), pt.GetZ());
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		offset = arg0.readLong();
		minPT.readFields(arg0);
		maxPT.readFields(arg0);
		avgZ = arg0.readFloat();
		cnt = arg0.readInt();
		maxOffset = arg0.readLong();
	}
	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeLong(offset);
		minPT.write(arg0);
		maxPT.write(arg0);
		arg0.writeFloat(avgZ);
		arg0.writeInt(cnt);
		arg0.writeLong(maxOffset);
	}
	
	public String toString() {
		return Long.toString(offset)
				+ " " + minPT.toString() 
				+ " " + maxPT.toString() 
				+ " " + Float.toString(avgZ)
				+ " " + Integer.toString(cnt)
				+ " " + Long.toString(maxOffset);
	}
	public static IndexData ParseFromIndexFile(String[] strs) {
//		if (strs.length != 10)return null;

		IndexData idxData = new IndexData();
		idxData.offset = Long.parseLong(strs[1]);
		idxData.avgZ = Float.parseFloat(strs[8]);
		idxData.cnt = Integer.parseInt(strs[9]);

		// MinPoint
		PointData pt = new PointData(Double.parseDouble(strs[2]), Double.parseDouble(strs[3]), Float.parseFloat(strs[4]));
		idxData.SetMinPT(pt);

		// MaxPoint
		pt.Set(Double.parseDouble(strs[5]), Double.parseDouble(strs[6]), Float.parseFloat(strs[7]));
		idxData.SetMaxPT(pt);

		return idxData;
	}
}
