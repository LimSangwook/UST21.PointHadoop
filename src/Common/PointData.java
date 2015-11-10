package Common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class PointData implements Writable {
	double pX;
	double pY;
	float pZ;
	
	public PointData() {
		pX = 0.0;
		pY = 0.0;
		pZ = 0.0f;
	}
	public PointData clone() {
		PointData pt = new PointData(pX, pY, pZ);
		return pt;	
	}
	
	public PointData(double x, double y, float z) {
		pX = x; pY = y; pZ =z;
	}
	
	public void Set(double x, double y, float z) {
		pX = x; pY = y; pZ =z;
	}
	
	public double GetX() {
		return pX;
	}
	
	public double GetY() {
		return pY;
	}
	
	public float GetZ() {
		return pZ;
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		pX = arg0.readDouble();
		pY = arg0.readDouble();
		pZ = arg0.readFloat();		
	}
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeDouble(pX);
		arg0.writeDouble(pY);
		arg0.writeFloat(pZ);
	}
	
	public String GetXYString() {
		String str = Double.toString(pX) + " " + Double.toString(pY);
		return str;
	}
	public String toString() {
		return String.format("%.3f",GetX()) + " " + String.format("%.3f",GetY()) + " " + String.format("%.3f", GetZ());	// 소수점 1자리 남기고 절사
	}
}
