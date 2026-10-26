package de.cebrero.drwho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.webkit.*;
import de.cebrero.drwho.core.BoardPlan;
import de.cebrero.drwho.core.GameEngine;
import java.util.*;

/** Read-only offline board, using exactly the same vector assets as the player page. */
public final class BoardView extends WebView {
    private String plan="{\"tiles\":[],\"players\":[],\"subdivisions\":2}",sent="";
    private boolean ready,released;
    @SuppressLint("SetJavaScriptEnabled") // Bundled local art only; no network or JavaScript bridge.
    public BoardView(Context context){
        super(context);setBackgroundColor(Color.rgb(6,24,37));
        getSettings().setJavaScriptEnabled(true);getSettings().setAllowFileAccess(false);
        getSettings().setAllowContentAccess(false);getSettings().setBlockNetworkLoads(true);
        setVerticalScrollBarEnabled(false);setHorizontalScrollBarEnabled(false);
        setOnTouchListener((view,event)->true);
        setWebViewClient(new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view,android.webkit.WebResourceRequest request){return true;}
            @Override public void onPageFinished(WebView view,String url){if(released)return;ready=true;sent="";publish();}
        });
        loadUrl("file:///android_asset/portal-board.html");
    }
    public void setPlan(List<BoardPlan.Tile> tiles,List<String[]> players){setPlan(tiles,players,1);}
    public void setPlan(List<BoardPlan.Tile> tiles,List<String[]> players,int subdivisions){
        StringBuilder data=new StringBuilder("{\"subdivisions\":"+subdivisions+",\"tiles\":[");
        for(int i=0;i<tiles.size();i++){BoardPlan.Tile t=tiles.get(i);if(i>0)data.append(',');data.append("{\"x\":").append(t.x).append(",\"y\":").append(t.y).append(",\"kind\":").append(GameEngine.quote(t.kind)).append(",\"rotation\":").append(t.rotation).append(",\"closed\":").append(t.closed).append('}');}
        data.append("],\"players\":[");for(int i=0;i<players.size();i++){String[] p=players.get(i);if(i>0)data.append(',');data.append("{\"name\":").append(GameEngine.quote(p[0])).append(",\"x\":").append(Integer.parseInt(p[2])).append(",\"y\":").append(Integer.parseInt(p[3])).append('}');}
        plan=data.append("]}").toString();setContentDescription("Gespeicherter Aufbau mit "+tiles.size()+" Kacheln und Koordinaten je Bewegungsfeld. Figuren entsprechen der nummerierten Teamliste.");publish();
    }
    private void publish(){if(ready&&!released&&!plan.equals(sent)){sent=plan;evaluateJavascript("renderPortal("+plan+")",null);}}
    @Override protected void onDetachedFromWindow(){ready=false;released=true;stopLoading();super.onDetachedFromWindow();destroy();}
}
