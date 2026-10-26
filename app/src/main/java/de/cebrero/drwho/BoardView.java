package de.cebrero.drwho;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import de.cebrero.drwho.core.BoardPlan;
import java.util.*;

/** Live host view of the committed physical plan. Empty grid locations are deliberately dark. */
public final class BoardView extends View {
    private final Paint paint=new Paint(3);
    private List<BoardPlan.Tile> tiles=Collections.emptyList();
    private List<String[]> players=Collections.emptyList();
    public BoardView(Context context){super(context);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    public void setPlan(List<BoardPlan.Tile> tiles,List<String[]> players){this.tiles=tiles;this.players=players;setContentDescription("Gespeicherter Aufbau mit "+tiles.size()+" Kacheln. Startfiguren entsprechen der nummerierten Teamliste.");invalidate();}
    @Override protected void onDraw(Canvas c){
        super.onDraw(c);float width=getWidth(),height=getHeight();c.drawColor(Color.rgb(6,24,37));
        if(tiles.isEmpty()){
            for(int y=0;y<=20;y++){paint.setColor(0x183fa1ac);paint.setStrokeWidth(1);c.drawLine(12,y*(height-24)/20+12,width-12,y*(height-24)/20+12,paint);}
            for(int x=0;x<=20;x++)c.drawLine(x*(width-24)/20+12,12,x*(width-24)/20+12,height-12,paint);
            paint.setTextAlign(Paint.Align.CENTER);paint.setColor(0xffb5dce4);paint.setTextSize(15*getResources().getDisplayMetrics().scaledDensity);c.drawText("Das Raumgefüge entsteht hier…",width/2,height/2,paint);return;
        }
        int minX=19,minY=19,maxX=0,maxY=0;for(BoardPlan.Tile t:tiles){minX=Math.min(minX,t.x);minY=Math.min(minY,t.y);maxX=Math.max(maxX,t.endX());maxY=Math.max(maxY,t.endY());}
        minX=Math.max(0,minX-1);minY=Math.max(0,minY-1);maxX=Math.min(19,Math.max(maxX+1,minX+4));maxY=Math.min(19,Math.max(maxY+1,minY+4));
        float unit=Math.min((width-20)/(maxX-minX+1),(height-20)/(maxY-minY+1));float ox=(width-unit*(maxX-minX+1))/2-minX*unit,oy=(height-unit*(maxY-minY+1))/2-minY*unit;
        paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(1);paint.setColor(0x203fa1ac);
        for(int y=minY;y<=maxY+1;y++)c.drawLine(ox+minX*unit,oy+y*unit,ox+(maxX+1)*unit,oy+y*unit,paint);
        for(int x=minX;x<=maxX+1;x++)c.drawLine(ox+x*unit,oy+minY*unit,ox+x*unit,oy+(maxY+1)*unit,paint);
        for(BoardPlan.Tile t:tiles){
            float l=ox+t.x*unit,top=oy+t.y*unit,r=ox+(t.endX()+1)*unit,b=oy+(t.endY()+1)*unit;
            paint.setStyle(Paint.Style.FILL);paint.setColor(t.kind.equals("portal")?0xff17596a:0xffd8c59a);c.drawRoundRect(l+1,top+1,r-1,b-1,3,3,paint);
            paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(Math.max(1,unit*.018f));paint.setColor(0xffab9a70);c.drawRoundRect(l+1,top+1,r-1,b-1,3,3,paint);
            if(t.kind.equals("portal")){paint.setColor(0xff89e9ef);paint.setStrokeWidth(unit*.055f);c.drawOval(l+unit*.18f,top+unit*.16f,r-unit*.18f,b-unit*.16f,paint);c.drawOval(l+unit*.3f,top+unit*.28f,r-unit*.3f,b-unit*.28f,paint);}
            if(t.kind.equals("well")){paint.setColor(0xff386775);paint.setStyle(Paint.Style.FILL);c.drawOval(l+unit*.22f,top+unit*.29f,r-unit*.22f,b-unit*.22f,paint);paint.setStyle(Paint.Style.STROKE);paint.setColor(0xff8c895f);paint.setStrokeWidth(unit*.09f);c.drawOval(l+unit*.22f,top+unit*.24f,r-unit*.22f,b-unit*.33f,paint);}
            if(t.kind.equals("dungeon")){paint.setColor(0xff29474c);paint.setStyle(Paint.Style.FILL);c.drawRoundRect(l+unit*.24f,top+unit*.18f,r-unit*.24f,b-unit*.16f,unit*.13f,unit*.13f,paint);paint.setStyle(Paint.Style.STROKE);paint.setColor(0xffa4b7b3);paint.setStrokeWidth(unit*.035f);for(int i=0;i<3;i++)c.drawLine(l+unit*(.35f+i*.15f),top+unit*.28f,l+unit*(.35f+i*.15f),b-unit*.2f,paint);}
            paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(unit*.09f);paint.setColor(0xff253f40);
            for(int d=0;d<4;d++)if(t.wall(d))edge(c,l,top,unit,d,paint);
            if(t.kind.equals("door")){paint.setColor(0xff9c713e);paint.setStrokeWidth(unit*.11f);float midX=l+unit/2,midY=top+unit/2;int save=c.save();c.rotate(t.rotation*90,midX,midY);c.drawLine(l+unit*.28f,top+unit*.07f,l+unit*.72f,top+unit*.07f,paint);c.restoreToCount(save);}
            paint.setStyle(Paint.Style.FILL);paint.setColor(t.kind.equals("portal")?0xffd6ffff:0xff34534f);paint.setTextAlign(Paint.Align.LEFT);paint.setTextSize(Math.max(8,unit*.19f));c.drawText(BoardPlan.cell(t.x,t.y),l+unit*.07f,b-unit*.06f,paint);
        }
        for(int i=0;i<players.size();i++){
            String[] p=players.get(i);int x=Integer.parseInt(p[2]),y=Integer.parseInt(p[3]);if(x<0)continue;
            float cx=ox+(x+.72f)*unit,cy=oy+(y+.28f)*unit,r=unit*.21f;paint.setStyle(Paint.Style.FILL);paint.setColor(0xff126f7d);c.drawCircle(cx,cy,r,paint);paint.setStyle(Paint.Style.STROKE);paint.setColor(0xffd8faff);paint.setStrokeWidth(1);c.drawCircle(cx,cy,r,paint);paint.setStyle(Paint.Style.FILL);paint.setTextAlign(Paint.Align.CENTER);paint.setTextSize(unit*.26f);c.drawText(String.valueOf(i+1),cx,cy+unit*.09f,paint);
        }
    }
    private static void edge(Canvas c,float l,float t,float u,int d,Paint p){float pad=u*.05f;if(d==0)c.drawLine(l,t+pad,l+u,t+pad,p);if(d==1)c.drawLine(l+u-pad,t,l+u-pad,t+u,p);if(d==2)c.drawLine(l,t+u-pad,l+u,t+u-pad,p);if(d==3)c.drawLine(l+pad,t,l+pad,t+u,p);}
}
