package de.cebrero.drwho.core;
import java.io.*;
import java.util.*;
/** Tile coordinates and movement coordinates are distinct: a new tile has 2×2 steps. */
public final class BoardPlan {
 public static final int SIZE=20;
 private boolean subdivided;
 public BoardPlan(){this(true);} BoardPlan(boolean subdivided){this.subdivided=subdivided;}
 public int scale(){return subdivided?2:1;} public boolean subdivided(){return subdivided;} public void upgrade(){subdivided=true;}
 public static final class Tile {
  public final int x,y,rotation;public final String kind;public boolean closed;
  Tile(int x,int y,String kind,int rotation){this.x=x;this.y=y;this.kind=kind;this.rotation=rotation;closed=kind.equals("dungeon");}
  public int endX(){return x+(kind.equals("portal")&&rotation%2==0?1:0);}public int endY(){return y+(kind.equals("portal")&&rotation%2==1?1:0);}
  public boolean covers(int cx,int cy){return cx==x&&cy==y||cx==endX()&&cy==endY();}
  public boolean wall(int d){int e=(d-rotation+4)%4;return kind.equals("corner")&&(e==0||e==3)||kind.equals("single")&&e==0||kind.equals("parallel")&&(e==0||e==2)||kind.equals("dungeon")&&closed;}
 }
 private final List<Tile> tiles=new ArrayList<>();
 public List<Tile> tiles(){return Collections.unmodifiableList(tiles);}public Tile at(int x,int y){for(Tile t:tiles)if(t.covers(x,y))return t;return null;}
 public boolean exists(int x,int y){return x>=0&&y>=0&&at(x/scale(),y/scale())!=null;}
 public boolean isPortal(int x,int y){return exists(x,y)&&at(x/scale(),y/scale()).kind.equals("portal");}
 public int[] canonical(int x,int y){Tile t=exists(x,y)?at(x/scale(),y/scale()):null;return t!=null&&t.kind.equals("portal")?new int[]{t.x*scale(),t.y*scale()}:new int[]{x,y};}
 public boolean logical(int x,int y){int[] c=canonical(x,y);return exists(x,y)&&c[0]==x&&c[1]==y;}
 public boolean itemField(int x,int y){return exists(x,y)&&!isPortal(x,y);}
 public List<int[]> fields(){List<int[]> result=new ArrayList<>();for(int y=0;y<SIZE*scale();y++)for(int x=0;x<SIZE*scale();x++)if(logical(x,y))result.add(new int[]{x,y});return result;}
 /** Four wall sides of a rectangular portal, clockwise from its closed long back edge. */
 private boolean portalAccess(Tile t,int x,int y){int q=scale(),left=t.x*q,right=(t.endX()+1)*q-1,top=t.y*q,bottom=(t.endY()+1)*q-1;
  return t.rotation==0?y==bottom+1&&x>=left&&x<=right:t.rotation==1?x==left-1&&y>=top&&y<=bottom:t.rotation==2?y==top-1&&x>=left&&x<=right:x==right+1&&y>=top&&y<=bottom;}
 public List<int[]> neighbours(int x,int y){List<int[]> result=new ArrayList<>();if(!logical(x,y))return result;
  if(isPortal(x,y)){Tile t=at(x/scale(),y/scale());for(int[] f:fields())if(!isPortal(f[0],f[1])&&portalAccess(t,f[0],f[1])&&canStep(x,y,f[0],f[1]))result.add(f);}
  else for(int[] d:new int[][]{{0,-1},{1,0},{0,1},{-1,0}}){int[] c=canonical(x+d[0],y+d[1]);if(canStep(x,y,c[0],c[1])&&result.stream().noneMatch(a->Arrays.equals(a,c)))result.add(c);}return result;
 }
 public void place(int x,int y,String kind,int rotation){Tile t=new Tile(x,y,kind,rotation);validate(t,null);tiles.add(t);sort();}
 public void rotate(int x,int y){Tile old=require(x,y);Tile t=new Tile(old.x,old.y,old.kind,(old.rotation+1)%4);t.closed=old.closed;replace(old,t);}
 public void move(int x,int y,int nx,int ny){Tile old=require(x,y);Tile t=new Tile(nx,ny,old.kind,old.rotation);t.closed=old.closed;replace(old,t);}
 public void remove(int x,int y){tiles.remove(require(x,y));}
 public void toggleDungeon(int x,int y){Tile t=require(x,y);if(!t.kind.equals("dungeon"))throw new IllegalArgumentException("Hier steht kein Kerker.");t.closed=!t.closed;}
 private Tile require(int x,int y){Tile t=at(x,y);if(t==null)throw new IllegalArgumentException("Hier liegt keine Kachel.");return t;}
 private void replace(Tile old,Tile t){validate(t,old);tiles.remove(old);tiles.add(t);sort();}private void sort(){tiles.sort(Comparator.comparingInt((Tile t)->t.y).thenComparingInt(t->t.x));}
 private void validate(Tile t,Tile ignored){
  if(!Arrays.asList("floor","corner","single","parallel","dungeon","cemetery","compass","lava","lava-stone","well","door","portal").contains(t.kind)||t.rotation<0||t.rotation>3)throw new IllegalArgumentException("Unbekannte Kachel oder Drehung.");
  if(t.x<0||t.y<0||t.endX()>=SIZE||t.endY()>=SIZE)throw new IllegalArgumentException("Die Kachel passt nicht ins Raster.");
  for(Tile o:tiles)if(o!=ignored&&(o.covers(t.x,t.y)||o.covers(t.endX(),t.endY())))throw new IllegalArgumentException("Dort liegt bereits eine Kachel.");
 }
 public int cells(){int n=0;for(Tile t:tiles)n+=t.kind.equals("portal")?1:scale()*scale();return n;}
 public boolean canStep(int x,int y,int nx,int ny){
  if(!logical(x,y)||!logical(nx,ny)||x==nx&&y==ny)return false;
  Tile a=at(x/scale(),y/scale()),b=at(nx/scale(),ny/scale());
  if(a.kind.equals("portal")||b.kind.equals("portal")){
   if(a.kind.equals("portal")&&b.kind.equals("portal"))return false;
   Tile portal=a.kind.equals("portal")?a:b;int ox=a==portal?nx:x,oy=a==portal?ny:y;
   if(!portalAccess(portal,ox,oy))return false;
   int toward=(portal.rotation+0)%4; // open side faces south at rotation zero; outside looks north.
   Tile outside=a==portal?b:a;return !outside.wall(toward);
  }
  if(Math.abs(nx-x)+Math.abs(ny-y)!=1)return false;if(a==b)return true;
  int d=ny<y?0:nx>x?1:ny>y?2:3;return !a.wall(d)&&!b.wall((d+2)%4);
 }
 public static String cell(int x,int y){if(x<0||y<0)return "noch nicht aufgestellt";StringBuilder column=new StringBuilder();int at=x;do{column.insert(0,(char)('A'+at%26));at=at/26-1;}while(at>=0);return column.toString()+(y+1);}
 public String fieldName(int x,int y){int[] c=canonical(x,y);return cell(c[0],c[1]);}
 public String tileName(int x,int y){Tile tile=at(x,y);if(tile!=null&&tile.kind.equals("portal"))return cell(tile.x*scale(),tile.y*scale());int endX=tile==null?x:tile.endX(),endY=tile==null?y:tile.endY();String first=cell(x*scale(),y*scale()),last=cell((endX+1)*scale()-1,(endY+1)*scale()-1);return first.equals(last)?first:first+"–"+last;}
 public String json(){StringBuilder b=new StringBuilder("{\"size\":20,\"subdivisions\":"+scale()+",\"tiles\":[");for(int i=0;i<tiles.size();i++){Tile t=tiles.get(i);if(i>0)b.append(',');b.append("{\"x\":").append(t.x).append(",\"y\":").append(t.y).append(",\"kind\":").append(GameEngine.quote(t.kind)).append(",\"rotation\":").append(t.rotation).append(",\"closed\":").append(t.closed).append('}');}return b.append("]}").toString();}
 void write(DataOutputStream out)throws IOException{out.writeBoolean(subdivided);out.writeInt(tiles.size());for(Tile t:tiles){out.writeInt(t.x);out.writeInt(t.y);out.writeUTF(t.kind);out.writeInt(t.rotation);out.writeBoolean(t.closed);}}
 static BoardPlan read(DataInputStream in)throws IOException{return read(in,true);}static BoardPlan read(DataInputStream in,boolean modern)throws IOException{
  BoardPlan b=new BoardPlan(modern&&in.readBoolean());int n=in.readInt();if(n<0||n>400)throw new IOException("Ungültige Kartengröße.");try{for(int i=0;i<n;i++){int x=in.readInt(),y=in.readInt();String k=in.readUTF();int r=in.readInt();b.place(x,y,k,r);Tile t=b.at(x,y);t.closed=modern?in.readBoolean():false;}}catch(IllegalArgumentException e){throw new IOException("Ungültiger Aufbau.",e);}return b;
 }
}
