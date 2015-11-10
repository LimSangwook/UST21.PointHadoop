package XYZFileControl.First;
import java.awt.geom.Point2D;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class Point3D {
	double X;
	double Y;
	float Z;
	String oriStr;
	
	public Point3D(double x, double y, float z) {
		X = x;
		Y = y;
		Z = z;
	}

	public Point2D.Double GetPoint2D() {
		return new Point2D.Double(X, Y);
	}
	
	public double GetX() {
		return X;
	}

	public double GetY() {
		return Y;
	}

	public float GetZ() {
		return Z;
	}
	
	public void SetZ(float z) {
		Z = z;		
	}

	public static Point3D Create(String str) {
		str = str.trim();
		str = str.replaceAll("\t", " ");
		str = str.replaceAll("  ", " ");
		str = str.replaceAll("N", "");
		str = str.replaceAll("E", "");
		
		String[] tokens = str.split(" ");
		Point3D pt = null;
		if (tokens.length != 3 && tokens.length < 7) {
			tokens = str.split(",");
		}
		
		if (tokens.length == 3) {
			// DMS ex) 37-54-8.488
			if (tokens[0].contains("-") == true) {
				double x = 0.0, y = 0.0;
				String[] DMStokens = tokens[0].split("-");
				double d = Double.parseDouble(DMStokens[0]);
				double m = Double.parseDouble(DMStokens[1]);
				double s = Float.parseFloat(DMStokens[2].replace('N', ' '));
				y = d + m * 1/60 + s * 1/3600;

				DMStokens = tokens[1].split("-");
				d = Double.parseDouble(DMStokens[0]);
				m = Double.parseDouble(DMStokens[1]);
				s = Float.parseFloat(DMStokens[2].replace('E', ' '));
				x = d + m * 1/60 + s * 1/3600;
				
				float z = Float.parseFloat(tokens[2]);
				pt = new Point3D(x, y , z);
				pt.oriStr = str;
//				pt.LAT2UTM();
			}
			// UTM, Lat-Lon
			
			else {
				try {
					tokens[0] = tokens[0].trim();
					
					byte[] k = new byte[1];
					tokens[0].getBytes(0, 1, k, 0);
					if (k[0]==-1) {
						tokens[0] = tokens[0].substring(1);
					}
					double x = Float.parseFloat(tokens[0]);
					double y = Double.parseDouble(tokens[1]);
					float z = Float.parseFloat(tokens[2]);
					pt = new Point3D(x, y , z);
					pt.oriStr = str;
				} catch(Exception e) {
					System.out.println("ERR Point3D Create : " + e.toString() + "\t string : " + str);
					pt = null;
				}
			}
		}
//		 37 29 52.5574 N 125 51 42.9661 E 4.66
		else if (tokens.length > 9 ) {
			if (tokens[3].compareTo("N") == 0 && tokens[7].compareTo("N") == 0) {
			}
			double x = 0.0, y = 0.0;
			double d = Double.parseDouble(tokens[0]);
			double m = Double.parseDouble(tokens[1]);
			double s = Float.parseFloat(tokens[2]);
			y = d + m * 1/60 + s * 1/3600;
			
			d = Double.parseDouble(tokens[4]);
			m = Double.parseDouble(tokens[5]);
			s = Float.parseFloat(tokens[6]);
			x = d + m * 1/60 + s * 1/3600;
			float z = Float.parseFloat(tokens[8]);
			pt = new Point3D(x, y , z);
			pt.oriStr = str;
			pt.LAT2UTM();
		}
		return pt;
	}
		
	public String toString() {
		return String.format("%.3f",GetX()) + " " + String.format("%.3f",GetY()) + " " + String.format("%.3f", GetZ());	// 소수점 1자리 남기고 절사
//		return GetX() + " " + GetY() + " " + GetZ();
 	}

	public String toDMSString() {
		return GetX() + " " + GetY() + " " + String.format("%.2f", GetZ());	// 소수점 1자리 남기고 절사
//		return GetX() + " " + GetY() + " " + GetZ();
 	}

	public String GetSourceString() {
		return oriStr;
	}

	private void LAT2UTM() {
		try {
			UTMCoord utm = UTMCoord.fromLatLon(Angle.fromDegrees(GetY()), Angle.fromDegrees(GetX()));
			Point3D utmPT = new Point3D(utm.getEasting(), utm.getNorthing(), GetZ());
			this.X = utmPT.X;
			this.Y = utmPT.Y;
		}catch (Exception e) {
			System.out.println("ERR : " + this.GetSourceString());
		}
	}
	
}
