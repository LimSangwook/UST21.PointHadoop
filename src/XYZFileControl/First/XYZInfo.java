package XYZFileControl.First;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class XYZInfo {
	public static void main(String[] args) {
		Point3D pt3D1 = Point3D.Create("37-49-20.262 124-37-29.881 56.");
//		Point3D pt3D2 = Point3D.Create("114865.22102865113 4195125.34989971 56.00");
		
		UTMCoord utm = UTMCoord.fromUTM(52, AVKey.NORTH, 114865.22102865113, 4195125.34989971);
//		UTMCoord utm2 = UTMCoord.fromLatLon(Angle.fromDegrees(utm.getLatitude().getDegrees()), Angle.fromDegrees(utm.getLongitude().getDegrees()));

		
		System.out.println(pt3D1.GetSourceString() + " : " + pt3D1.toDMSString());
		System.out.println(pt3D1.GetSourceString() + " : " + pt3D1.toDMSString());
	}
}
