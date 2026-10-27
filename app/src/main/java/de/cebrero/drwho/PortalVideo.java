package de.cebrero.drwho;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;

/** Private local video, decorative only. Never drives game state or network requests. */
final class PortalVideo extends TextureView implements TextureView.SurfaceTextureListener {
    private MediaPlayer player;
    private Surface surface;
    private boolean requested;
    PortalVideo(Context context){super(context);setSurfaceTextureListener(this);setOpaque(false);setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);}
    void show(boolean visible){requested=visible;setVisibility(visible?VISIBLE:GONE);if(visible&&isAvailable())start();else if(!visible)release();}
    private void start(){
        if(player!=null||!requested||getSurfaceTexture()==null)return;
        try(AssetFileDescriptor fd=getContext().getAssets().openFd("video/portal_blue.mp4")){
            MediaPlayer current=new MediaPlayer();player=current;surface=new Surface(getSurfaceTexture());current.setSurface(surface);
            current.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());current.setLooping(true);current.setVolume(0,0);
            current.setOnPreparedListener(p->{if(player==p&&requested){fit(p.getVideoWidth(),p.getVideoHeight());p.start();}});
            current.setOnVideoSizeChangedListener((p,w,h)->{if(player==p)fit(w,h);});
            current.setOnErrorListener((p,what,extra)->{if(player==p)release();return true;});current.prepareAsync();
        }catch(Exception unavailable){release();} // Static navy background remains readable.
    }
    private void fit(int width,int height){
        if(width<=0||height<=0||getWidth()==0||getHeight()==0)return;
        float scale=Math.max(getWidth()/(float)width,getHeight()/(float)height);
        Matrix matrix=new Matrix();matrix.setScale(width*scale/getWidth(),height*scale/getHeight(),getWidth()/2f,getHeight()/2f);setTransform(matrix);
    }
    private void release(){if(player!=null){player.setOnPreparedListener(null);player.setOnVideoSizeChangedListener(null);player.setOnErrorListener(null);player.release();player=null;}if(surface!=null){surface.release();surface=null;}}
    public void onSurfaceTextureAvailable(SurfaceTexture texture,int width,int height){start();}
    public void onSurfaceTextureSizeChanged(SurfaceTexture texture,int width,int height){if(player!=null)try{fit(player.getVideoWidth(),player.getVideoHeight());}catch(IllegalStateException ignored){}}
    public boolean onSurfaceTextureDestroyed(SurfaceTexture texture){release();return true;}
    public void onSurfaceTextureUpdated(SurfaceTexture texture){}
    @Override protected void onDetachedFromWindow(){release();super.onDetachedFromWindow();}
}
