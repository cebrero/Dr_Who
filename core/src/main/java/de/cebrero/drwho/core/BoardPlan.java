package de.cebrero.drwho.core;

import java.io.*;
import java.util.*;

/** The physical plan, not an inventory or a generated scenario. Coordinates are zero based. */
public final class BoardPlan {
    public static final int SIZE = 20;
    public static final class Tile {
        public final int x, y, rotation;
        public final String kind;
        Tile(int x,int y,String kind,int rotation){this.x=x;this.y=y;this.kind=kind;this.rotation=rotation;}
        public int endX(){return x+(kind.equals("portal")&&(rotation%2==0)?1:0);}
        public int endY(){return y+(kind.equals("portal")&&(rotation%2==1)?1:0);}
        public boolean covers(int cx,int cy){return cx==x&&cy==y||cx==endX()&&cy==endY();}
        public boolean wall(int direction){
            int edge=(direction-rotation+4)%4;
            return kind.equals("corner")&&(edge==0||edge==1)||kind.equals("parallel")&&(edge==0||edge==2);
        }
    }
    private final List<Tile> tiles=new ArrayList<>();
    public List<Tile> tiles(){return Collections.unmodifiableList(tiles);}
    public Tile at(int x,int y){for(Tile t:tiles)if(t.covers(x,y))return t;return null;}
    public boolean exists(int x,int y){return at(x,y)!=null;}
    public void place(int x,int y,String kind,int rotation){
        Tile tile=new Tile(x,y,kind,rotation);validate(tile,null);tiles.add(tile);sort();
    }
    public void rotate(int x,int y){Tile old=require(x,y);replace(old,new Tile(old.x,old.y,old.kind,(old.rotation+1)%4));}
    public void move(int x,int y,int toX,int toY){Tile old=require(x,y);replace(old,new Tile(toX,toY,old.kind,old.rotation));}
    public void remove(int x,int y){tiles.remove(require(x,y));}
    private Tile require(int x,int y){Tile t=at(x,y);if(t==null)throw new IllegalArgumentException("Hier liegt keine Kachel.");return t;}
    private void replace(Tile old,Tile next){validate(next,old);tiles.remove(old);tiles.add(next);sort();}
    private void sort(){tiles.sort(Comparator.comparingInt((Tile t)->t.y).thenComparingInt(t->t.x));}
    private void validate(Tile t,Tile ignored){
        if(!Arrays.asList("corner","floor","parallel","door","dungeon","well","portal").contains(t.kind)||t.rotation<0||t.rotation>3)
            throw new IllegalArgumentException("Unbekannte Kachel oder Drehung.");
        if(t.x<0||t.y<0||t.endX()>=SIZE||t.endY()>=SIZE)throw new IllegalArgumentException("Die Kachel passt nicht vollständig ins 20×20-Raster.");
        for(Tile other:tiles)if(other!=ignored&&(other.covers(t.x,t.y)||other.covers(t.endX(),t.endY())))
            throw new IllegalArgumentException("Dort liegt bereits eine Kachel. Erst verschieben oder entfernen.");
    }
    public int cells(){int n=0;for(Tile t:tiles)n+=t.kind.equals("portal")?2:1;return n;}
    public boolean canStep(int x,int y,int nx,int ny){
        Tile a=at(x,y),b=at(nx,ny);if(a==null||b==null||Math.abs(nx-x)+Math.abs(ny-y)!=1)return false;
        if(a==b)return true; // The two portal squares have no internal wall.
        int d=ny<y?0:nx>x?1:ny>y?2:3;return !a.wall(d)&&!b.wall((d+2)%4);
    }
    public static String cell(int x,int y){return ""+(char)('A'+x)+(y+1);}
    public String json(){
        StringBuilder b=new StringBuilder("{\"size\":20,\"tiles\":[");
        for(int i=0;i<tiles.size();i++){Tile t=tiles.get(i);if(i>0)b.append(',');b.append("{\"x\":").append(t.x).append(",\"y\":").append(t.y).append(",\"kind\":").append(GameEngine.quote(t.kind)).append(",\"rotation\":").append(t.rotation).append('}');}
        return b.append("]}").toString();
    }
    void write(DataOutputStream out)throws IOException{out.writeInt(tiles.size());for(Tile t:tiles){out.writeInt(t.x);out.writeInt(t.y);out.writeUTF(t.kind);out.writeInt(t.rotation);}}
    static BoardPlan read(DataInputStream in)throws IOException{
        BoardPlan board=new BoardPlan();int n=in.readInt();if(n<0||n>400)throw new IOException("Ungültige Kartengröße.");
        try{for(int i=0;i<n;i++)board.place(in.readInt(),in.readInt(),in.readUTF(),in.readInt());}
        catch(IllegalArgumentException e){throw new IOException("Ungültiger Aufbau.",e);}return board;
    }
}
