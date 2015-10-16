package XYZFileControl.First;
import java.awt.geom.Point2D;

public class Point3D {
	double X;
	double Y;
	float Z;
	
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
		String[] tokens = str.split(" ");
		Point3D pt = null;
		if (tokens.length != 3) {
			tokens = str.split(",");
		}
		if (tokens.length == 3) {
			double x = Double.parseDouble(tokens[0]);
			double y = Double.parseDouble(tokens[1]);
			float z = Float.parseFloat(tokens[2]);
			pt = new Point3D(x, y , z);
		}
		return pt;
	}
	public String toString() {
		return GetX() + " " + GetY() + " " + GetZ();
 	}
	
}
