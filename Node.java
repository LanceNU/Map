//lulrich2@u.rochester.edu Lance Ulrich
import java.awt.*;
public class Node {
    String id; 
    double lng,lat,distance;
    Node prev; 
    Color c = Color.black;
    public Node(String i,double g, double t){id = i; lng = g; lat = t; distance = Double.MAX_VALUE;}
    public void setD(double d){distance = d;}
    public void setprev(Node n){prev = n;}
    public boolean equals(String n){if(id.equals(n))return true; return false;}
    public void setC(){
        c = Color.red;
    }

}
