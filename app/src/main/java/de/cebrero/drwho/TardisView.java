package de.cebrero.drwho;

import android.content.Context;
import android.graphics.*;
import android.os.SystemClock;
import android.view.View;

/** Small offline 3D turntable, drawn from a reusable police-box texture. */
final class TardisView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Matrix matrix = new Matrix();
    private final Bitmap facade = Bitmap.createBitmap(160, 280, Bitmap.Config.ARGB_8888);
    private final float[] from = {0,0,160,0,160,280,0,280}, to = new float[8];
    private final float[] depths = new float[4];
    private final int[] order = {0,1,2,3};
    private boolean visible;
    private final boolean flying;
    TardisView(Context context) {this(context,false);}
    TardisView(Context context,boolean flying) {
        super(context);this.flying=flying;setContentDescription(flying?"Eine fliegende TARDIS im Zeitwirbel":"Eine sich drehende TARDIS im Zeitwirbel");
        Canvas c = new Canvas(facade); c.drawColor(Color.rgb(22,61,111));
        paint.setColor(Color.rgb(48,99,157)); c.drawRect(4,0,156,9,paint); c.drawRect(4,270,156,280,paint);
        paint.setColor(Color.rgb(7,21,42)); c.drawRect(11,13,149,41,paint);
        paint.setColor(Color.WHITE); paint.setTextAlign(Paint.Align.CENTER); paint.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD));
        paint.setTextSize(16); c.drawText("POLICE BOX",80,33,paint);
        for (int door=0;door<2;door++) {
            int x=14+door*68;
            paint.setColor(Color.rgb(12,41,78)); c.drawRect(x,49,x+62,264,paint);
            paint.setColor(Color.rgb(36,79,130)); c.drawRect(x+3,52,x+59,261,paint);
            for(int row=0;row<2;row++)for(int col=0;col<3;col++) {
                paint.setColor(Color.rgb(188,233,243)); c.drawRect(x+6+col*17,57+row*25,x+20+col*17,78+row*25,paint);
            }
            for(int row=0;row<3;row++) {
                paint.setColor(Color.rgb(13,45,84));c.drawRect(x+7,116+row*46,x+55,154+row*46,paint);
                paint.setColor(Color.rgb(26,65,111));c.drawRect(x+10,119+row*46,x+53,152+row*46,paint);
            }
        }
        paint.setColor(Color.rgb(210,230,221));c.drawRect(26,121,56,152,paint);
        paint.setColor(Color.rgb(234,205,133));c.drawCircle(86,183,3,paint);
    }
    @Override public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible); visible=isVisible;if(visible)postInvalidateOnAnimation();
    }
    private float x(float x,float z,float a){return (float)(x*Math.cos(a)+z*Math.sin(a));}
    private float z(float x,float z,float a){return (float)(-x*Math.sin(a)+z*Math.cos(a));}
    private void point(int i,float wx,float wy,float wz,float angle,float scale,float cx,float cy){
        to[i*2]=cx+x(wx,wz,angle)*scale;to[i*2+1]=cy+wy*scale+z(wx,wz,angle)*scale*.12f;
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean animate=android.animation.ValueAnimator.areAnimatorsEnabled();
        long duration=flying?8000:24000;float a=(animate?SystemClock.uptimeMillis()%duration:0)/(float)duration*(float)(Math.PI*2),scale=Math.min(getWidth()/2.8f,getHeight()/2.3f);
        float cx=getWidth()/2f,cy=getHeight()/2f+(float)Math.sin(a*1.7f)*5;
        if(flying){
            scale*=.78f+(float)Math.sin(a*1.3f)*.08f;cx+=(float)Math.sin(a*.8f)*getWidth()*.12f;cy+=(float)Math.cos(a*1.1f)*getHeight()*.07f;
            paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(2);
            for(int i=0;i<28;i++){double angle=i*2.399963+a*.14;float travel=(i*.137f+a*.23f)%1f;float distance=scale*(.7f+travel*2.3f);paint.setColor(Color.argb((int)(25+70*travel),110,218,245));float sx=getWidth()/2f+(float)Math.cos(angle)*distance,sy=getHeight()/2f+(float)Math.sin(angle)*distance*.8f;canvas.drawLine(sx,sy,sx+(float)Math.cos(angle)*scale*.14f,sy+(float)Math.sin(angle)*scale*.11f,paint);}
        }
        paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(1.5f);paint.setColor(Color.argb(50,85,223,244));
        for(int r=0;r<4;r++)canvas.drawOval(cx-scale*(.7f+r*.17f),cy-scale*(.7f+r*.08f),cx+scale*(.7f+r*.17f),cy+scale*(.7f+r*.08f),paint);
        for(int f=0;f<4;f++){float t=f*(float)Math.PI/2;depths[f]=z((float)Math.sin(t)*.42f,-(float)Math.cos(t)*.42f,a);order[f]=f;}
        for(int i=0;i<4;i++)for(int j=i+1;j<4;j++)if(depths[order[i]]<depths[order[j]]){int t=order[i];order[i]=order[j];order[j]=t;}
        int bank=canvas.save();if(flying)canvas.rotate((float)Math.sin(a*1.7f)*13,cx,cy);
        paint.setStyle(Paint.Style.FILL);paint.setColor(Color.WHITE);
        for(int f:order){
            float t=f*(float)Math.PI/2,c=(float)Math.cos(t),s=(float)Math.sin(t);
            float lx=-.42f*c+.42f*s,lz=-.42f*s-.42f*c,rx=.42f*c+.42f*s,rz=.42f*s-.42f*c;
            point(0,lx,-.84f,lz,a,scale,cx,cy);point(1,rx,-.84f,rz,a,scale,cx,cy);
            point(2,rx,.84f,rz,a,scale,cx,cy);point(3,lx,.84f,lz,a,scale,cx,cy);
            matrix.reset();if(matrix.setPolyToPoly(from,0,to,0,4))canvas.drawBitmap(facade,matrix,paint);
        }
        paint.setColor(Color.rgb(118,224,246));canvas.drawCircle(cx,cy-scale*.96f,scale*.045f,paint);
        paint.setColor(Color.argb(50,118,224,246));canvas.drawCircle(cx,cy-scale*.96f,scale*.10f,paint);
        canvas.restoreToCount(bank);
        if(visible&&animate)postInvalidateOnAnimation();
    }
}
