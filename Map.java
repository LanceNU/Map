import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.awt.*;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
//lulrich2@u.rochester.edu Lance Ulrich
public class Map {
    static HashMap<Node, Edge> map;
    static HashMap<String, Node> store;
    static double minl, ming,maxl,maxg, deltal, deltag, ratio; 

    public static void main(String args[]){
        Map ur = new Map(args[0]);
        double start = System.currentTimeMillis();
        String tmp = "";
        for(int i =1 ; i<args.length -2; i++)tmp += " " + args[i];
        if(tmp.contains("directions")){
        ArrayList<String> arr =  ur.Directions(args[args.length-2],args[args.length-1]);
            try {
                FileWriter myWriter = new FileWriter("Directions.csv");{
                for(String s: arr){
                myWriter.write(s);
                }
            }
                myWriter.close();
                } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                 }
            }
        if(tmp.contains("show"))
        ur.showmap();
        System.out.println((System.currentTimeMillis()-start)/1000);
    }
    public Map(String inputFile){
        minl = Double.MAX_VALUE;
        ming = Double.MAX_VALUE;
        maxl = -Double.MAX_VALUE;
        maxg = -Double.MAX_VALUE;
        Scanner s;
		try {
		s = new Scanner(new BufferedReader(new FileReader(inputFile)));
        map = new HashMap<>();
        store = new HashMap<>();
        while(s.hasNext()){
            String[] tmp = s.nextLine().split("\t");
            if(tmp[0].equals("i")){
                Node t = new Node(tmp[1],Double.parseDouble(tmp[3]),Double.parseDouble(tmp[2]));
                store.put(t.id,t);
                if(Double.parseDouble(tmp[2]) < minl){
                    minl = Double.parseDouble(tmp[2]);
                }
                if(Double.parseDouble(tmp[3]) < ming){
                    ming = Double.parseDouble(tmp[3]);
                }
                if(Double.parseDouble(tmp[2]) > maxl){
                    maxl = Double.parseDouble(tmp[2]);
                }
                if(Double.parseDouble(tmp[3]) > maxg){
                    maxg = Double.parseDouble(tmp[3]);
                }
            }
            if(tmp[0].equals("r")){
                Node t1 = store.get(tmp[2]);
                Node t2 = store.get(tmp[3]);
                Edge t3 = new Edge(tmp[1], t1, t2);
                Edge t4 = new Edge(tmp[1],t2,t1);
                if(map.containsKey(t1)){ 
                    t3.setN(map.get(t1));
                    map.put(t1,t3);
                }
                else
                map.put(t1,t3);
                if(map.containsKey(t2)){
                    t4.setN(map.get(t2));
                    map.put(t2,t4);
                }
                else
                map.put(t2,t4);
            }
        }
    }   catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    deltal = maxl - minl;
    deltag = maxg -ming;
    Edge e = new Edge("", new Node("", maxl,0), new Node(".", minl,0));
    Edge d = new Edge("", new Node("",0,maxg),new Node("",0,ming));
    ratio = e.Length()/d.Length(); 
    System.out.println(deltal + " :delta lat: " + deltag + " :delta long: " +minl + "  " +maxl+ " :lat range: " + ming + " "+ maxg+" :long range:");
}

    // my implementation of dijkstra's were we store the node that is previus on the shortest path to the node within the node 
    public void shortpath(Node src){
        PriorityQueue<Node> que = new PriorityQueue<>(new HComparator());src.setD(0);
        for(Node n: map.keySet()){
            que.add(n);
        }
        while(!que.isEmpty()){
            Node x = que.poll();
            Edge e = map.get(x);
            while(e!=null){
                double tmp = x.distance + e.Length();
                if(e.i2.distance > tmp){
                    que.remove(e.i2);
                    que.add(e.i2);
                    e.i2.setD(tmp);
                    e.i2.setprev(e.i1);
                }
                e=e.n;
            }
        }
    }

    // loops through the node's previus to get an arrraylist of ids for the shortest path
    public ArrayList<String> Directions(String from, String to){
        ArrayList<String> r = new ArrayList<String>();
        shortpath(store.get(from));
        Node n = store.get(to);
        r.add(n.id);
        while(!n.equals(from)){
            n.prev.setC();
            r.add(n.prev.id);
            n=n.prev;
        }
        Double d = store.get(to).distance * .6214;
        System.out.println(d + " MI" );
        return r;
    }
    //methode to creat and display that map window 
    public void showmap(){
        path a = new path();
        JFrame p = new JFrame("Map");
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setSize(800,(int)(900* ratio));
        p.setResizable(true);
        p.setLocationRelativeTo(null);
        p.add(a);
        a.setIgnoreRepaint(true);
        p.setVisible(true);
        p.setIgnoreRepaint(true);
    

    }

    private class path extends JPanel {
        //methode to draw the map and turn the lines on path red
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D line = (Graphics2D)g;
            for(Edge e: map.values()){
                while(e!=null){
                    if(e.i2.c.equals(Color.red)&&e.i1.c.equals(Color.red)){
                        line.setColor(Color.red);
                        line.setStroke(new BasicStroke(2));
                    }
                    else{
                        line.setColor(Color.black);
                        line.setStroke(new BasicStroke(1));
                    }
                    int x1 =(int)((1- (e.i1.lat - minl) / deltal)*(int)(600* ratio));
                    int y1 =(int)(((e.i1.lng - ming) / deltag)*(int)(750* ratio));
                    int x2 =(int)((1- (e.i2.lat - minl) / deltal)*(int)(600* ratio));
                    int y2 =(int)(((e.i2.lng - ming) / deltag)*(int)(750* ratio));;
                    line.drawLine(y1, x1, y2, x2);
                    e=e.n;
                }
            }
        }
                
    } 
}


class HComparator implements Comparator<Node>{
    public int compare(Node x, Node y)
    {
        return (int)(x.distance - y.distance);
    }
}

