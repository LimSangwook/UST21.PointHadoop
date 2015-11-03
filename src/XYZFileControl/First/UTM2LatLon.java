package XYZFileControl.First;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class UTM2LatLon {
	public static void main(String []args){
		
		Point3D pt3d = new Point3D(185878.74, 4149571.29, 15.290f);
		UTMCoord utm = UTMCoord.fromUTM(51, AVKey.NORTH, pt3d.GetX(), pt3d.GetY());
		System.out.println("pt : " + pt3d.toString() + "\t DMS : " + utm.toDMSString() + " : " + utm.toString());
	}
}