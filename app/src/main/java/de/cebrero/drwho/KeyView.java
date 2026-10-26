package de.cebrero.drwho;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.SystemClock;
import android.view.View;

/** Gold portal-key reveal, drawn locally with no external artwork. */
final class KeyView extends View {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    KeyView(Context context){super(context);setLayerType(View.LAYER_TYPE_SOFTWARE,null);setContentDescription("Ein goldener Portalschlüssel schwebt im Zeitwirbel");}
    @Override protected void onDraw(Canvas canvas){
        super.onDraw(canvas);canvas.drawColor(Color.rgb(7,17,30));float t=SystemClock.uptimeMillis()/1000f;
        float scale=Math.min(getWidth()/320f,getHeight()/300f);canvas.save();canvas.translate(getWidth()/2f,getHeight()*.32f+(float)Math.sin(t*2)*8);canvas.scale(scale,scale);
        paint.setStyle(Paint.Style.FILL);paint.setShader(new RadialGradient(0,0,145,new int[]{0x88f6bc4a,0x225bcbdb,Color.TRANSPARENT},null,Shader.TileMode.CLAMP));canvas.drawCircle(0,0,145,paint);paint.setShader(null);
        canvas.rotate(-28+(float)Math.sin(t*1.7)*7);paint.setColor(0xffe7b64e);paint.setShadowLayer(12,0,0,0xffd6ab45);paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(12);canvas.drawCircle(-65,0,29,paint);canvas.drawLine(-35,0,90,0,paint);canvas.drawLine(58,0,58,25,paint);canvas.drawLine(85,0,85,20,paint);paint.clearShadowLayer();paint.setColor(0xffffedaa);paint.setStrokeWidth(3);canvas.drawArc(-92,-27,-38,27,205,120,false,paint);canvas.drawLine(-30,-3,88,-3,paint);canvas.restore();
        if(isShown()&&ValueAnimator.areAnimatorsEnabled())postInvalidateOnAnimation();
    }
}
