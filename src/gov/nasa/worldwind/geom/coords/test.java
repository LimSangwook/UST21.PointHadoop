package gov.nasa.worldwind.geom.coords;


import gov.nasa.worldwind.avlist.AVKey;

public class test {
    public static void main( String[] args )
    {	
    		UTMCoordConverter a = new UTMCoordConverter();
    		a.convertUTMToGeodetic(52, AVKey.NORTH, 183635.60, 4148023.27);
    		double latitude = a.getLatitude() * 180 / Math.PI;
    		double longitude = a.getLongitude() * 180 / Math.PI;
    		System.out.println("Lat : " + latitude + "\t Lon : " + longitude);
    		
    		
    }
}
