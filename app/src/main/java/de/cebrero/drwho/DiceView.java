package de.cebrero.drwho;

import android.animation.*;
import android.content.Context;
import android.graphics.*;
import android.view.View;

/** Presentation only: the host has already committed the result before this view animates. */
final class DiceView extends View {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Camera camera=new Camera();
    private final Matrix matrix=new Matrix();
    private final RectF face=new RectF(-48,-48,48,48),fogBounds=new RectF(-125,-100,125,100);
    private final Shader halo=new RadialGradient(0,12,105,new int[]{Color.argb(80,82,224,247),Color.TRANSPARENT},null,Shader.TileMode.CLAMP);
    private final Shader mist=new RadialGradient(0,12,115,new int[]{Color.rgb(187,225,223),Color.TRANSPARENT},null,Shader.TileMode.CLAMP);
    private final Shader flashLight=new RadialGradient(0,0,105,new int[]{Color.rgb(255,255,238),Color.TRANSPARENT},null,Shader.TileMode.CLAMP);
    private String eventId="";
    private int result;
    private boolean holding;
    private float progress=1;
    private ValueAnimator animator;
    DiceView(Context context){super(context);setLayerType(View.LAYER_TYPE_SOFTWARE,null);setContentDescription("W6 noch nicht gewürfelt");}
    boolean isAnimating(){return holding||animator!=null&&animator.isRunning();}
    void waitForPlayer(String id){
        if(id.equals(eventId)&&holding)return;
        if(animator!=null){animator.removeAllListeners();animator.cancel();}
        eventId=id;holding=true;result=0;progress=.3f;setContentDescription("W6 wartet auf das Spielerhandy. Ergebnis noch verborgen.");
        if(ValueAnimator.areAnimatorsEnabled()){
            animator=ValueAnimator.ofFloat(0,.8f);animator.setDuration(1600);animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new android.view.animation.LinearInterpolator());animator.addUpdateListener(a->{progress=(float)a.getAnimatedValue();invalidate();});animator.start();
        }else invalidate();
    }
    void showRoll(String id,int value,boolean animate,Runnable complete){
        if(id.equals(eventId)&&!holding)return;
        boolean release=holding;holding=false;
        if(animator!=null){animator.removeAllListeners();animator.cancel();}
        eventId=id;result=value;progress=1;
        if(animate&&value>0&&ValueAnimator.areAnimatorsEnabled()){
            progress=0;setContentDescription("W6 wird gewürfelt. Ergebnis folgt.");
            animator=ValueAnimator.ofFloat(0,1);animator.setDuration(release?650:2600);
            animator.setInterpolator(new android.view.animation.LinearInterpolator());
            animator.addUpdateListener(a->{progress=(float)a.getAnimatedValue();invalidate();});
            animator.addListener(new AnimatorListenerAdapter(){@Override public void onAnimationEnd(Animator a){progress=1;setContentDescription("W6-Ergebnis: "+result);invalidate();complete.run();}});
            animator.start();
        }else{setContentDescription(value>0?"W6-Ergebnis: "+value:"W6 noch nicht gewürfelt");invalidate();if(value>0)post(complete);}
    }
    @Override protected void onDraw(Canvas c){
        super.onDraw(c);float scale=Math.min(getWidth()/240f,getHeight()/200f);
        c.save();c.translate(getWidth()/2f,getHeight()/2f);c.scale(scale,scale);
        float p=progress;boolean moving=p<1;
        paint.setShader(halo);c.drawCircle(0,12,105,paint);paint.setShader(null);
        c.save();
        if(moving){camera.save();camera.rotateX((1-p)*690-10);camera.rotateY((1-p)*950-12);camera.getMatrix(matrix);camera.restore();c.concat(matrix);c.scale(.65f+.35f*p,.65f+.35f*p);}
        paint.setShadowLayer(14,0,9,Color.argb(100,0,0,0));paint.setColor(Color.rgb(235,225,195));c.drawRoundRect(face,14,14,paint);paint.clearShadowLayer();
        paint.setColor(Color.rgb(168,135,72));paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(2);c.drawRoundRect(face,14,14,paint);paint.setStyle(Paint.Style.FILL);
        int visible=moving&&p<.88f?1+(int)(p*33)%6:result;
        paint.setColor(Color.rgb(16,57,67));
        if(visible==0){paint.setTextSize(58);paint.setTypeface(Typeface.create("serif",Typeface.BOLD));paint.setTextAlign(Paint.Align.CENTER);c.drawText("?",0,20,paint);}
        else{if(visible==1||visible==3||visible==5)pip(c,0,0);if(visible>=2){pip(c,-24,-24);pip(c,24,24);}if(visible>=4){pip(c,24,-24);pip(c,-24,24);}if(visible==6){pip(c,-24,0);pip(c,24,0);}}
        c.restore();
        if(moving){
            float fog=(1-p)*(.6f+.4f*(float)Math.sin(Math.PI*p));
            c.save();c.translate(-45+90*p,0);paint.setShader(mist);paint.setAlpha((int)(210*fog));c.drawOval(fogBounds,paint);paint.setShader(null);paint.setAlpha(255);c.restore();
            if(p>.86f){float flash=(float)Math.sin((p-.86f)/.14f*Math.PI);paint.setShader(flashLight);paint.setAlpha((int)(180*flash));c.drawCircle(0,0,105,paint);paint.setShader(null);paint.setAlpha(255);}
        }
        c.restore();
    }
    private void pip(Canvas c,float x,float y){c.drawCircle(x,y,6.5f,paint);}
    @Override protected void onDetachedFromWindow(){if(animator!=null){animator.removeAllListeners();animator.cancel();}super.onDetachedFromWindow();}
}
