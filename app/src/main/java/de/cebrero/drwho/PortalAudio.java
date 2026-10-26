package de.cebrero.drwho;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.*;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;

/** One foreground, non-looping sound at a time, entirely from private bundled assets. */
final class PortalAudio {
    private final Context context;
    private final AudioManager manager;
    private AudioFocusRequest focus;
    private int generation;
    private MediaPlayer player;
    private boolean ownsFocus;
    PortalAudio(Context context){
        this.context=context.getApplicationContext();manager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }
    private static AudioAttributes attributes(){return new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();}
    void playTheme(){play("audio/theme_music_12.ogg");}
    void playJoin(){play("audio/tardis_sound.ogg");}
    private void play(String asset){
        stop();int expected=++generation;
        focus=new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(attributes()).setOnAudioFocusChangeListener(change->{if(change<0&&expected==generation)stop();},new Handler(Looper.getMainLooper())).build();
        if(manager.requestAudioFocus(focus)!=AudioManager.AUDIOFOCUS_REQUEST_GRANTED)return;ownsFocus=true;
        MediaPlayer next=new MediaPlayer();player=next;next.setAudioAttributes(attributes());
        next.setOnCompletionListener(this::finished);next.setOnErrorListener((p,what,extra)->{Log.w("DrWhoAudio","Wiedergabe fehlgeschlagen: "+what+"/"+extra);finished(p);return true;});
        next.setOnPreparedListener(p->{if(p!=player)return;try{p.setLooping(false);p.start();}catch(IllegalStateException error){Log.w("DrWhoAudio","Tonstart fehlgeschlagen",error);finished(p);}});
        try(AssetFileDescriptor fd=context.getAssets().openFd(asset)){next.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());next.prepareAsync();}
        catch(IOException|IllegalStateException error){Log.w("DrWhoAudio","Audiodatei nicht abspielbar: "+asset,error);finished(next);}
    }
    private void finished(MediaPlayer current){if(current==player)stop();}
    void stop(){
        generation++;MediaPlayer current=player;player=null;if(current!=null){current.setOnPreparedListener(null);current.setOnCompletionListener(null);current.setOnErrorListener(null);current.release();}
        if(ownsFocus){ownsFocus=false;manager.abandonAudioFocusRequest(focus);}
    }
}
