package de.cebrero.drwho;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import java.io.*;

/** Bundled JPEG character portraits; editable Canvas illustrations are the fallback. */
final class CharacterAvatarView extends View {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Path clip=new Path(),shape=new Path();
    private final RectF photoRect=new RectF();
    private String id="";
    private Bitmap photo;
    CharacterAvatarView(Context context){super(context);}
    void setCharacter(String id,String name){
        setContentDescription(id.isEmpty()?"Figur noch nicht gewählt":name+" · Charakterbild");
        if(this.id.equals(id))return;this.id=id;photo=null;
        if(id.matches("[a-z0-9-]{1,64}"))try(InputStream in=getContext().getAssets().open("portraits/"+de.cebrero.drwho.core.CharacterProfile.archetypeOf(id)+".jpg")){photo=BitmapFactory.decodeStream(in);}catch(IOException ignored){}
        invalidate();
    }
    @Override protected void onDraw(Canvas canvas){
        super.onDraw(canvas);float size=Math.min(getWidth(),getHeight()),offsetX=(getWidth()-size)/2,offsetY=(getHeight()-size)/2;
        canvas.save();canvas.translate(offsetX,offsetY);clip.reset();clip.addCircle(size/2,size/2,size/2,Path.Direction.CW);canvas.clipPath(clip);
        if(photo!=null){float ratio=Math.max(size/photo.getWidth(),size/photo.getHeight()),w=photo.getWidth()*ratio,h=photo.getHeight()*ratio;photoRect.set((size-w)/2,(size-h)/2,(size+w)/2,(size+h)/2);canvas.drawBitmap(photo,null,photoRect,paint);canvas.restore();return;}
        canvas.scale(size/100,size/100);canvas.drawColor(Color.rgb(21,49,75));
        paint.setColor(Color.argb(50,87,223,244));canvas.drawCircle(50,45,37,paint);
        if(id.equals("dalek"))drawDalek(canvas);else if(id.equals("weeping-angel"))drawAngel(canvas);else if(id.equals("doctor"))drawDoctor(canvas);else{
            paint.setColor(Color.rgb(106,155,178));canvas.drawCircle(50,37,13,paint);canvas.drawOval(23,56,77,102,paint);
        }
        canvas.restore();
    }
    private void drawDoctor(Canvas c){
        paint.setColor(Color.rgb(46,32,28));shape.reset();shape.moveTo(33,39);shape.lineTo(67,39);shape.lineTo(82,94);shape.lineTo(18,94);shape.close();c.drawPath(shape,paint);
        paint.setColor(Color.rgb(214,195,164));c.drawCircle(50,29,13,paint);
        paint.setColor(Color.rgb(50,35,27));c.drawOval(36,12,63,25,paint);
        paint.setColor(Color.rgb(241,229,204));shape.reset();shape.moveTo(41,41);shape.lineTo(59,41);shape.lineTo(50,65);shape.close();c.drawPath(shape,paint);
        paint.setColor(Color.rgb(127,32,38));c.drawRect(48,45,52,72,paint);
        paint.setColor(Color.rgb(171,184,181));paint.setStrokeWidth(4);c.drawLine(70,70,81,41,paint);
        paint.setColor(Color.rgb(107,238,255));c.drawCircle(81,40,5,paint);
    }
    private void drawDalek(Canvas c){
        paint.setColor(Color.rgb(178,136,69));shape.reset();shape.moveTo(35,43);shape.lineTo(65,43);shape.lineTo(79,91);shape.lineTo(21,91);shape.close();c.drawPath(shape,paint);
        c.drawArc(33,19,67,52,180,180,true,paint);c.drawRect(31,36,69,51,paint);
        paint.setColor(Color.rgb(49,65,72));paint.setStrokeWidth(5);c.drawLine(49,31,80,31,paint);c.drawLine(34,53,15,61,paint);c.drawLine(65,53,86,58,paint);
        paint.setColor(Color.rgb(118,224,249));c.drawCircle(81,31,5,paint);
        paint.setColor(Color.rgb(101,77,44));for(int row=0;row<3;row++)for(int col=0;col<3;col++)c.drawCircle(35+col*15,62+row*10,3.5f,paint);
        paint.setColor(Color.rgb(48,51,52));c.drawRect(19,90,81,95,paint);
    }
    private void drawAngel(Canvas c){
        paint.setColor(Color.rgb(137,151,154));shape.reset();shape.moveTo(34,43);shape.cubicTo(5,13,6,75,32,75);shape.lineTo(50,91);shape.lineTo(68,75);shape.cubicTo(94,75,94,13,66,43);shape.close();c.drawPath(shape,paint);
        paint.setColor(Color.rgb(190,198,192));c.drawCircle(50,30,12,paint);shape.reset();shape.moveTo(38,43);shape.lineTo(62,43);shape.lineTo(72,96);shape.lineTo(28,96);shape.close();c.drawPath(shape,paint);
        paint.setColor(Color.rgb(151,164,162));paint.setStrokeWidth(7);paint.setStrokeCap(Paint.Cap.ROUND);c.drawLine(35,56,45,30,paint);c.drawLine(65,56,55,30,paint);paint.setStrokeCap(Paint.Cap.BUTT);
    }
}
