package tools;

import tools.struct.Point;

public class CoordinateConversion {

        /**
         * 经纬度转墨卡托
         * @param LonLat 经纬度坐标
         * @return
         */
        public static Point lonLatToMercator(Point LonLat){
            Point mercator = new Point();
            double x =  (LonLat.getxIndex() * 20037508.342789 / 180);
            double y =  (Math.log(Math.tan((90 + LonLat.getyIndex()) * Math.PI / 360)) / (Math.PI / 180));
            y =  (double)(y * 20037508.342789 / 180);
            mercator.setxIndex(x);
            mercator.setyIndex(y);
            return mercator;
        }

        /**
         * 墨卡托转经纬度
         * @param mercator 墨卡托坐标
         * @return
         */
        public static Point mercatorToLonLat(Point mercator){
            Point lonlat = new Point();
            double x =   (mercator.getxIndex() / 20037508.342789 * 180);
            double y =  (mercator.getyIndex() / 20037508.342789 * 180);
            y = (double) (180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2));
            lonlat.setxIndex(x);
            lonlat.setyIndex(y);
            return lonlat;
        }

    public static void main(String[] args) {
        Point point = new Point(402133.625298, 3399042.219073);
        Point result = mercatorToLonLat(point);
        System.out.println(result);
    }
}
