package XYZFileControl.First;

public class Util {
	static double DMS2D(String dmsStr) {
		double x = 0.0;
		String[] DMStokens = dmsStr.split("-");
		double d = Double.parseDouble(DMStokens[0]);
		double m = Double.parseDouble(DMStokens[1]);
		double s = Float.parseFloat(DMStokens[2].replace('N', ' '));
		x = d + m * 1/60 + s * 1/3600;
		return x;
	}
}
