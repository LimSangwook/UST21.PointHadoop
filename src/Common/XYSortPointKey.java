package Common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class XYSortPointKey extends PointKey{
	String sourceStr;
	double pZ = 0.0;
	public XYSortPointKey() {}
	
	public double GetZ() {
		return pZ;
	}
	public XYSortPointKey(Double x, Double y) {
		super(x, y);
	}
	
	public int	compareTo(XYSortPointKey o) {
		int ret = Double.compare(this.pX, o.pX);
		if (ret == 0) {
			ret = Double.compare(pY, o.pY);
		}
		return ret;
	}
	@Override
	public void readFields(DataInput arg0) throws IOException {
		pX = arg0.readDouble();
		pY = arg0.readDouble();
		sourceStr = arg0.readLine();
	}
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeDouble(pX);
		arg0.writeDouble(pY);
		arg0.writeChars(sourceStr);
	}
	public void SetSourceString(String str) {
		sourceStr = str;
	}

	public String GetSourceString() {
		return sourceStr;
	}
	
	static public XYSortPointKey Create(String value) {
		String str = value.toString();
		str = str.replaceAll("\t", " ");
		str = str.replaceAll("  ", " ");

		String [] xyz = str.toString().split(" ");
		
		if (xyz.length != 3) {
			xyz = str.split(",");
		}

		if (xyz.length != 3) return null;

		double X = 0.0;
		double Y = 0.0;
		double Z = 0.0;

		// DMS ex) 37-54-8.488
		if (xyz[0].contains("-") == true) {
			String[] DMStokens = xyz[0].split("-");
			double d = Double.parseDouble(DMStokens[0]);
			double m = Double.parseDouble(DMStokens[1]);
			double s = Float.parseFloat(DMStokens[2].replace('N', ' '));
			Y = d + m * 1/60 + s * 1/3600;

			DMStokens = xyz[1].split("-");
			d = Double.parseDouble(DMStokens[0]);
			m = Double.parseDouble(DMStokens[1]);
			s = Float.parseFloat(DMStokens[2].replace('E', ' '));
			X = d + m * 1/60 + s * 1/3600;
			
			Z = Float.parseFloat(xyz[2]);
			try{
				UTMCoord utm2 = UTMCoord.fromLatLon2UTMZone(Angle.fromDegrees(Y), Angle.fromDegrees(Z), 52);
				X = utm2.getEasting();
				Y = utm2.getNorthing();
			} catch (Exception e) {
				System.out.println("ERR X : " + X + " \t Y : " + Y);
			}
		} 
		// UTM 좌표 혹은 도만 있는좌표 
		else {
			X = Double.parseDouble(xyz[0]);
			Y = Double.parseDouble(xyz[1]);
		}
		Z = Double.parseDouble(xyz[2]);
		
		
		XYSortPointKey ptKey = new XYSortPointKey(X,Y);
		ptKey.SetSourceString(str);
		ptKey.pZ = Z;
		return ptKey;
	}
}
