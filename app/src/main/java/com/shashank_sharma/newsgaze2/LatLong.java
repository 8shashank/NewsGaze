package com.shashank_sharma.newsgaze2;

/**
 * Created by Vikas on 3/21/2015.
 */
public class LatLong {
    private static LatLng LatitLongit;
    private static final double EARTH_RADIUS =3963.1676;
    private static int degreeboundary = 20;
    public LatLong(double lat,double longi,double angle){
        LatitLongit = new LatLng(lat,longi);
    }
    public static boolean isinRange(double latitude,double longitude,double arcangle) {
        LatLng check = new LatLng(latitude,longitude);
        double angle1 = computeHeading(LatitLongit,check);
        if(Math.abs(angle1)!=angle1){
            angle1 = 360-Math.abs(angle1);
        }
        double angle2 = arcangle;
        if(Math.abs(angle2)!=angle2){
            angle2 =360-Math.abs(angle2);
        }
        if((angle2-degreeboundary)%360>(angle1%360)){
            return false;
        }
        else if ((angle2+degreeboundary)%360<(angle1%360)){
            return false;
        }
        return true;
    }
    public static LatLng computeOffset(LatLng from, double distance, double heading) {
        distance /= EARTH_RADIUS;
        heading = heading*Math.PI/360;
        double fromLat = from.latitude*Math.PI/360;
        double fromLng = from.longitude*Math.PI/360;
        double cosDistance = Math.cos(distance);
        double sinDistance = Math.sin(distance);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(
                sinDistance * cosFromLat * Math.sin(heading),
                cosDistance - sinFromLat * sinLat);
        return new LatLng(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }
    public static double computeHeading(LatLng from, LatLng to) {
        double fromLat = Math.toRadians(from.latitude);
        double fromLng = Math.toRadians(from.longitude);
        double toLat = Math.toRadians(to.latitude);
        double toLng = Math.toRadians(to.longitude);
        double dLng = toLng - fromLng;
        double heading = Math.atan2(
                Math.sin(dLng) * Math.cos(toLat),
                Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dLng));
        return wrap(Math.toDegrees(heading),-180,180);
    }
    static double wrap(double n, double min, double max) {
        return (n >= min && n < max) ? n : (mod(n - min, max - min) + min);
    }
    static double mod(double x, double m) {
        return ((x % m) + m) % m;
    }
}