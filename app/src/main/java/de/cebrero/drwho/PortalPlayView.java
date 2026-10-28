package de.cebrero.drwho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.webkit.*;
import de.cebrero.drwho.core.*;
import org.json.JSONTokener;
import java.io.*;
import java.util.function.Consumer;

/** Local, read-only presentation. No network, storage API, or JavaScript-to-Java bridge. */
final class PortalPlayView extends WebView {
    private boolean ready,released,active;
    private String data="",rollId="",reported="";
    private Consumer<String> completed;
    @SuppressLint("SetJavaScriptEnabled") // Only explicitly allowed bundled presentation assets.
    PortalPlayView(Context context){
        super(context);setBackgroundColor(Color.TRANSPARENT);getSettings().setJavaScriptEnabled(true);
        getSettings().setAllowFileAccess(false);getSettings().setAllowContentAccess(false);getSettings().setBlockNetworkLoads(true);
        setVerticalScrollBarEnabled(false);setHorizontalScrollBarEnabled(false);
        setWebViewClient(new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request){return true;}
            @Override public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request){
                String url=request.getUrl().toString(),prefix="file:///android_asset/";if(!url.startsWith(prefix))return empty();
                String file=url.substring(prefix.length());boolean picture=file.matches("portraits/[a-z0-9-]{1,64}\\.jpg");
                if(!picture&&!file.equals("portal-stage.html")&&!file.equals("portal-stage.css")&&!file.equals("portal-stage.js"))return empty();
                try{return new WebResourceResponse(picture?"image/jpeg":file.endsWith(".css")?"text/css":file.endsWith(".js")?"application/javascript":"text/html","UTF-8",getContext().getAssets().open(file));}catch(IOException missing){return empty();}
            }
            @Override public void onPageFinished(WebView view,String url){if(released)return;ready=true;evaluateJavascript("PortalStage.pause("+(!active)+")",null);publish();}
        });
        loadUrl("file:///android_asset/portal-stage.html");
    }
    private static WebResourceResponse empty(){return new WebResourceResponse("text/plain","UTF-8",new ByteArrayInputStream(new byte[0]));}
    void show(GameEngine.PortalState state,boolean seen,boolean reduced,Consumer<String> onComplete){
        data=PortalScene.json(state,seen,reduced);rollId=state.currentRoll?state.rollId:"";completed=onComplete;
        setContentDescription(state.awaitingGo?state.activePlayerName+" ist dran. Wartet auf Go am Spielerhandy.":state.headline+". "+(state.rollPresented?state.detail:"Der Spieler würfelt."));publish();
    }
    void active(boolean value){if(active==value)return;active=value;if(ready&&!released)evaluateJavascript("PortalStage.pause("+(!value)+")",null);if(value){onResume();publish();}else onPause();}
    private void publish(){
        if(!ready||released||!active||data.isEmpty())return;
        String expected=rollId;
        evaluateJavascript("PortalStage.update("+data+")",result->{
            if(released||!active||!expected.equals(rollId))return;
            try{Object id=new JSONTokener(result).nextValue();if(id instanceof String&&!((String)id).isEmpty()&&id.equals(expected)&&!id.equals(reported)){reported=(String)id;if(completed!=null)completed.accept(reported);}}catch(Exception ignored){}
        });
    }
    @Override protected void onDetachedFromWindow(){released=true;ready=false;stopLoading();super.onDetachedFromWindow();destroy();}
}
