//lulrich2@u.rochester.edu Lance Ulrich
public class Edge{
    String id;
    Node i1,i2;
    Edge n;
    public Edge(String i,Node t1,Node t2){id = i; i1 =t1;i2=t2;}
    public double Length(){
        Double dLat = Math.abs(Math.toRadians(i2.lat - i1.lat));
        Double dLon = Math.abs(Math.toRadians(i1.lng - i2.lng));
        Double lat1 = Math.toRadians(i1.lat);
        Double lat2 = Math.toRadians(i2.lat);
        Double a = (Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2));
        Double radius = 6371.0;
        Double c = 2 * Math.asin(Math.sqrt(a));
        return (radius * c);
    }
    public void setN(Edge N){n = N;}
    public boolean hasNext(){if(n!=null)return true; return false;}
    public void tcolor(){
        i1.setC();
        i2.setC();
    }

}