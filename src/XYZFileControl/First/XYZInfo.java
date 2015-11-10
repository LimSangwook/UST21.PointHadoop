package XYZFileControl.First;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class XYZInfo {
	public static void main(String[] args) {
		String str = "                    754580.380   4159300.390            3.9     C:\\SJJEONG\\2";
		str = str.trim();
		System.out.println("str : " + str);
		str = str.replace("            ", " ");
		str = str.replace("   ", " ");
		str = str.replace("   C:\\SJJEONG\\2", " ");
		System.out.println("str : " + str);
		String [] token = str.split(" ");
		System.out.println("str : " + token.length);
		Point3D pt3D1 = Point3D.Create("37-49-20.262 124-37-29.881 56.");
//		Point3D pt3D2 = Point3D.Create("114865.22102865113 4195125.34989971 56.00");
		
		UTMCoord utm = UTMCoord.fromUTM(52, AVKey.NORTH, 114865.22102865113, 4195125.34989971);
//		UTMCoord utm2 = UTMCoord.fromLatLon(Angle.fromDegrees(utm.getLatitude().getDegrees()), Angle.fromDegrees(utm.getLongitude().getDegrees()));

		
		System.out.println(pt3D1.GetSourceString() + " : " + pt3D1.toDMSString());
		System.out.println(pt3D1.GetSourceString() + " : " + pt3D1.toDMSString());
	}
}
