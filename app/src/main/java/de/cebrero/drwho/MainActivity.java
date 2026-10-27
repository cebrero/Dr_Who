package de.cebrero.drwho;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.*;
import android.os.*;
import android.util.AtomicFile;
import android.view.*;
import android.widget.*;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import de.cebrero.drwho.core.*;
import java.io.*;
import java.util.*;

/** Portal first; administration is deliberately on a separate screen. */
public class MainActivity extends Activity {
    private static final int INK=Color.rgb(7,17,30),CYAN=Color.rgb(104,227,246),WHITE=Color.rgb(224,241,250),MUTED=Color.rgb(145,177,197);
    private final Handler handler=new Handler(Looper.getMainLooper());
    private GameEngine engine;
    private PortalAudio audio;
    private PortalAudioEvents audioEvents;
    private boolean foreground;
    private LocalServer server;
    private PortalNetwork network;
    private WifiManager.LocalOnlyHotspotReservation reservation;
    private boolean hotspotPending,destroyed,hotspotPermissionRequested;
    private int hotspotGeneration;
    private volatile int networkGeneration;
    private FrameLayout root,stage,setupScreen,noticeScreen;
    private TardisView setupFlight,noticeFlight;
    private KeyView noticeKey;
    private BoardView setupBoard;
    private TextView setupHeading,setupHint,setupTeam,setupTrivia,noticeText;
    private long triviaStarted;
    private int triviaIndex=-1;
    private String triviaSetup="";
    private final Runnable fadeTrivia=()->{if(setupTrivia!=null&&setupTrivia.getVisibility()==View.VISIBLE)setupTrivia.animate().alpha(0).setDuration(500).start();};
    private boolean wasPreparing;
    private SharedPreferences prefs;
    private String activeNotice="";
    private final Runnable finishNotice=()->{if(!activeNotice.isEmpty()){prefs.edit().putString("seen-notice-"+engine.sessionId(),activeNotice).apply();activeNotice="";}noticeScreen.setVisibility(View.GONE);updatePortal();};
    private ScrollView lobby,director,play;
    private TextView playRound,playActor,playDetail,playEvents,playPositions,playDiceLabel;
    private DiceView playDice;
    private LinearLayout goCard;
    private ImageView goPortrait;
    private TextView goCharacter,goDescription,goStats,goAbilities;
    private String goCharacterId="";
    private ImageView lobbyBackground;
    private LinearLayout playerRows;
    private TextView countBadge,playerCount,wifiName,wifiPassword,wifiStatus,website,fallback,gameStatus,stageStatus;
    private Button wifiButton,startButton;
    private String ssid="",password="",rosterSignature="";
    private final Runnable refresh=new Runnable(){public void run(){updatePortal();handler.postDelayed(this,500);}};

    @Override public void onCreate(Bundle saved){
        super.onCreate(saved);setVolumeControlStream(android.media.AudioManager.STREAM_MUSIC);getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        root=new FrameLayout(this);root.setBackgroundColor(INK);
        root.setOnApplyWindowInsetsListener((v,i)->{v.setPadding(i.getSystemWindowInsetLeft(),i.getSystemWindowInsetTop(),i.getSystemWindowInsetRight(),i.getSystemWindowInsetBottom());return i;});
        lobbyBackground=new ImageView(this);lobbyBackground.setImageResource(R.drawable.portal_background);
        lobbyBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);lobbyBackground.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        root.addView(lobbyBackground,new FrameLayout.LayoutParams(-1,-1));
        setContentView(root);prefs=getSharedPreferences("portal-wifi",MODE_PRIVATE);
        prefs.edit().remove("manualConfirmed").remove("setupPending").remove("ssid").remove("password").apply();
        try{
            engine=new GameEngine(new AndroidStore(new File(getFilesDir(),"session-v1.drw")));
            byte[] page=asset("player.html");Map<String,byte[]> boardAssets=new HashMap<>();for(String name:new String[]{"board-art.js","board-art.css","character-dossiers.js","character-dossiers.css"})boardAssets.put("/"+name,asset(name));
            try{server=new LocalServer(engine,page,80,this::portrait,BuildConfig.VERSION_NAME,boardAssets);}catch(IOException unavailable){server=new LocalServer(engine,page,8080,this::portrait,BuildConfig.VERSION_NAME,boardAssets);}
            restartPortalNetwork();
        }catch(IOException error){root.addView(text("Start fehlgeschlagen. Der vorhandene Spielstand bleibt erhalten.\n"+error.getMessage(),18,WHITE));return;}
        audio=new PortalAudio(this);
        buildLobby();buildDirector();buildPlay();buildStage();buildSetup();if(engine.playing())showPlay();else showLobby();updatePortal();
    }
    private byte[] asset(String path)throws IOException{
        try(InputStream in=getAssets().open(path);ByteArrayOutputStream bytes=new ByteArrayOutputStream()){
            byte[] buffer=new byte[4096];int n;while((n=in.read(buffer))!=-1)bytes.write(buffer,0,n);return bytes.toByteArray();
        }
    }
    private byte[] portrait(String filename)throws IOException{try{return asset("portraits/"+filename);}catch(FileNotFoundException missing){return null;}}
    private int dp(float value){return Math.round(value*getResources().getDisplayMetrics().density);}
    private LinearLayout column(){LinearLayout v=new LinearLayout(this);v.setOrientation(LinearLayout.VERTICAL);return v;}
    private LinearLayout row(){LinearLayout v=new LinearLayout(this);v.setOrientation(LinearLayout.HORIZONTAL);v.setGravity(Gravity.CENTER_VERTICAL);return v;}
    private TextView text(String value,int size,int color){TextView v=new TextView(this);v.setText(value);v.setTextSize(size);v.setTextColor(color);v.setPadding(0,dp(3),0,dp(3));return v;}
    private Button button(String label,Runnable action){Button b=new Button(this);b.setAllCaps(false);b.setText(label);b.setTextColor(CYAN);b.setTextSize(14);b.setOnClickListener(v->action.run());return b;}
    private LinearLayout card(LinearLayout parent){
        LinearLayout c=column();c.setPadding(dp(12),dp(9),dp(12),dp(9));
        GradientDrawable bg=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{Color.rgb(14,38,60),Color.rgb(9,25,44)});
        bg.setCornerRadius(dp(18));bg.setStroke(dp(1),Color.rgb(35,77,101));c.setBackground(bg);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(8);parent.addView(c,lp);return c;
    }
    private ScrollView screen(LinearLayout content){ScrollView s=new ScrollView(this);s.setFillViewport(true);s.addView(content);root.addView(s,new FrameLayout.LayoutParams(-1,-1));return s;}
    private LinearLayout padded(){LinearLayout c=column();c.setPadding(dp(18),dp(14),dp(18),dp(20));return c;}
    private void buildLobby(){
        LinearLayout content=padded();lobby=screen(content);
        LinearLayout heading=row();LinearLayout titles=column();TextView title=text("Dr WHO",32,WHITE);title.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD));titles.addView(title);
        heading.addView(titles,new LinearLayout.LayoutParams(0,-2,1));countBadge=text("0 / 4",22,WHITE);heading.addView(countBadge);content.addView(heading);
        Space artwork=new Space(this);content.addView(artwork,new LinearLayout.LayoutParams(-1,dp(10)));
        LinearLayout connection=card(content);connection.addView(text("1  MIT DEM WLAN VERBINDEN",11,MUTED));
        wifiName=text("WLAN starten",22,WHITE);connection.addView(wifiName);wifiName.setTextIsSelectable(true);
        wifiPassword=text("",18,CYAN);wifiPassword.setTextIsSelectable(true);connection.addView(wifiPassword);
        wifiStatus=text("",12,MUTED);connection.addView(wifiStatus);
        wifiButton=button("Spiel-WLAN starten",this::startAutomaticHotspot);connection.addView(wifiButton);
        connection.addView(text("2  SPIELSEITE ÖFFNEN",11,MUTED));website=text("Warte auf WLAN …",22,CYAN);website.setTextIsSelectable(true);connection.addView(website);
        fallback=text("",12,MUTED);fallback.setTextIsSelectable(true);connection.addView(fallback);
        LinearLayout qrButtons=row();qrButtons.addView(button("WLAN-QR",()->showQr(true)),new LinearLayout.LayoutParams(0,-2,1));qrButtons.addView(button("Seiten-QR",()->showQr(false)),new LinearLayout.LayoutParams(0,-2,1));connection.addView(qrButtons);
        LinearLayout players=card(content);playerCount=text("DEIN TEAM · 0 / 4",16,CYAN);players.addView(playerCount);playerRows=column();players.addView(playerRows);
        startButton=button("Abenteuer starten",()->{if(engine.playing())showPlay();else startGame();});content.addView(startButton);
        LinearLayout footer=row();footer.addView(button("Einstellungen",this::settingsDialog),new LinearLayout.LayoutParams(0,-2,1));footer.addView(button("Zurücksetzen",()->resetDialog(true)),new LinearLayout.LayoutParams(0,-2,1));content.addView(footer);
    }
    private void buildDirector(){
        LinearLayout content=padded();director=screen(content);director.setVisibility(View.GONE);
        content.addView(button("← Portal",()->{if(engine.playing())showPlay();else showLobby();}));content.addView(text("Spielleitung",26,CYAN));
        gameStatus=text("",15,WHITE);gameStatus.setMaxLines(3);content.addView(gameStatus);
        content.addView(button("Gegner hinzufügen",this::enemyDialog));
        content.addView(button("Bewegungshelfer bestimmen",()->{try{toast(engine.requestEnemyMovement()+" bewegt die Gegnerfigur.");updatePortal();}catch(Exception e){toast(e.getMessage());}}));
        content.addView(button("Angriff anzeigen",this::attackDialog));
        content.addView(button("Gegnerzug abschließen",()->{try{engine.finishEnemyTurn();updatePortal();}catch(Exception e){toast(e.getMessage());}}));
        content.addView(button("Speichern / Laden / Zurücksetzen",this::settingsDialog));
    }
    private void buildPlay(){
        LinearLayout content=padded();play=screen(content);play.setVisibility(View.GONE);
        LinearLayout top=row();top.addView(text("Dr WHO",27,WHITE),new LinearLayout.LayoutParams(0,-2,1));playRound=text("RUNDE 1",17,CYAN);top.addView(playRound);content.addView(top);
        LinearLayout turn=card(content);turn.addView(text("JETZT AM ZUG",11,MUTED));playActor=text("",25,WHITE);turn.addView(playActor);playDetail=text("",17,CYAN);turn.addView(playDetail);
        goCard=column();turn.addView(goCard);goPortrait=new ImageView(this);goPortrait.setScaleType(ImageView.ScaleType.FIT_CENTER);goPortrait.setAdjustViewBounds(true);goCard.addView(goPortrait,new LinearLayout.LayoutParams(-1,dp(140)));
        goCharacter=text("",23,CYAN);goDescription=text("",15,WHITE);goStats=text("",15,CYAN);goAbilities=text("",14,MUTED);
        goCard.addView(goCharacter);goDescription.setVisibility(View.GONE);goCard.addView(goDescription);goCard.addView(goStats);goAbilities.setVisibility(View.GONE);goCard.addView(goAbilities);goCard.setVisibility(View.GONE);
        playDiceLabel=text("Noch kein Wurf",14,MUTED);playDiceLabel.setGravity(Gravity.CENTER);turn.addView(playDiceLabel);
        playDice=new DiceView(this);turn.addView(playDice,new LinearLayout.LayoutParams(-1,dp(150)));
        LinearLayout events=card(content);events.addView(text("STORY",13,CYAN));playEvents=text("",19,WHITE);playEvents.setLineSpacing(dp(3),1);playEvents.setMaxLines(4);playEvents.setEllipsize(android.text.TextUtils.TruncateAt.END);events.addView(playEvents);
        LinearLayout positions=card(content);positions.addView(text("DEIN TEAM AUF DEM SPIELFELD",12,CYAN));playPositions=text("",15,WHITE);positions.addView(playPositions);
        LinearLayout actions=row();actions.addView(button("Aufbau",this::showBoard),new LinearLayout.LayoutParams(0,-2,1));
        actions.addView(button("Portal-Lobby",this::showLobby),new LinearLayout.LayoutParams(0,-2,1));actions.addView(button("Einstellungen",this::settingsDialog),new LinearLayout.LayoutParams(0,-2,1));content.addView(actions);
    }
    private void showPlay(){lobbyBackground.setVisibility(View.VISIBLE);lobby.setVisibility(View.GONE);director.setVisibility(View.GONE);stage.setVisibility(View.GONE);play.setVisibility(View.VISIBLE);updatePortal();}
    private void buildStage(){
        stage=new FrameLayout(this);stage.setBackgroundColor(INK);root.addView(stage,new FrameLayout.LayoutParams(-1,-1));stage.setVisibility(View.GONE);
        stage.addView(new TardisView(this),new FrameLayout.LayoutParams(-1,-1));
        TextView title=text("Dr WHO",32,CYAN);title.setGravity(Gravity.CENTER);FrameLayout.LayoutParams top=new FrameLayout.LayoutParams(-1,-2,Gravity.TOP);top.topMargin=dp(32);stage.addView(title,top);
        stageStatus=text("",20,WHITE);stageStatus.setGravity(Gravity.CENTER);stageStatus.setPadding(dp(20),0,dp(20),dp(35));stage.addView(stageStatus,new FrameLayout.LayoutParams(-1,-2,Gravity.BOTTOM));
        stage.setContentDescription("Drehende TARDIS. Antippen öffnet die Portal-Lobby.");stage.setOnClickListener(v->showLobby());
    }
    private void buildSetup(){
        setupScreen=new FrameLayout(this);setupScreen.setBackgroundColor(INK);root.addView(setupScreen,new FrameLayout.LayoutParams(-1,-1));setupScreen.setVisibility(View.GONE);
        LinearLayout layout=column();layout.setGravity(Gravity.CENTER);layout.setPadding(dp(18),dp(20),dp(18),dp(20));setupScreen.addView(layout,new FrameLayout.LayoutParams(-1,-1));
        TextView kicker=text("DR WHO · DAS RAUMGEFÜGE",12,CYAN);kicker.setGravity(Gravity.CENTER);layout.addView(kicker);
        setupHeading=text("",30,WHITE);setupHeading.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD));setupHeading.setGravity(Gravity.CENTER);layout.addView(setupHeading);
        FrameLayout visual=new FrameLayout(this);layout.addView(visual,new LinearLayout.LayoutParams(-1,0,1));
        setupFlight=new TardisView(this,true);visual.addView(setupFlight,new FrameLayout.LayoutParams(-1,-1));setupBoard=new BoardView(this);visual.addView(setupBoard,new FrameLayout.LayoutParams(-1,-1));
        setupTeam=text("",16,CYAN);layout.addView(setupTeam);setupHint=text("",17,MUTED);setupHint.setGravity(Gravity.CENTER);layout.addView(setupHint);
        setupTrivia=text("",24,Color.WHITE);setupTrivia.setGravity(Gravity.CENTER);setupTrivia.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));setupTrivia.setPadding(dp(12),dp(10),dp(12),dp(10));
        setupTrivia.setAutoSizeTextTypeUniformWithConfiguration(18,24,2,android.util.TypedValue.COMPLEX_UNIT_SP);setupTrivia.setMaxLines(4);setupTrivia.setTextDirection(View.TEXT_DIRECTION_LOCALE);
        setupTrivia.setBackgroundColor(0xff143748);layout.addView(setupTrivia,new LinearLayout.LayoutParams(-1,dp(112)));setupTrivia.setVisibility(View.GONE);
        layout.addView(button("Einstellungen",this::settingsDialog));
        noticeScreen=new FrameLayout(this);noticeScreen.setBackgroundColor(INK);root.addView(noticeScreen,new FrameLayout.LayoutParams(-1,-1));noticeScreen.setVisibility(View.GONE);noticeScreen.setClickable(true);
        noticeFlight=new TardisView(this,true);noticeScreen.addView(noticeFlight,new FrameLayout.LayoutParams(-1,-1));noticeKey=new KeyView(this);noticeScreen.addView(noticeKey,new FrameLayout.LayoutParams(-1,-1));noticeKey.setVisibility(View.GONE);noticeText=text("",32,WHITE);noticeText.setGravity(Gravity.CENTER);noticeText.setAutoSizeTextTypeUniformWithConfiguration(18,32,2,android.util.TypedValue.COMPLEX_UNIT_SP);noticeText.setPadding(dp(25),dp(24),dp(25),dp(24));noticeText.setBackgroundColor(0x4807111e);noticeScreen.addView(noticeText,new FrameLayout.LayoutParams(-1,-1,Gravity.CENTER));
        noticeScreen.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);noticeText.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
    }
    private void updateSetup(GameEngine.PortalState info){
        setupScreen.setVisibility(info.preparing?View.VISIBLE:View.GONE);
        if(info.preparing){
            lobby.setVisibility(View.GONE);director.setVisibility(View.GONE);play.setVisibility(View.GONE);stage.setVisibility(View.GONE);
            setupHeading.setText(info.setupTitle);boolean building=info.setupStage.equals("building"),briefing=info.setupStage.equals("briefing"),showBoard=building||briefing;setupFlight.setVisibility(showBoard?View.GONE:View.VISIBLE);setupBoard.setVisibility(showBoard?View.VISIBLE:View.GONE);
            if(showBoard)setupBoard.setPlan(info.tiles,info.boardPlayers,info.subdivisions);
            StringBuilder team=new StringBuilder();for(int i=0;i<info.boardPlayers.size();i++){String[] p=info.boardPlayers.get(i);if(team.length()>0)team.append("\n");team.append(i+1).append(" · ").append(p[0]);int x=Integer.parseInt(p[2]),y=Integer.parseInt(p[3]);if(x>=0)team.append(" · ").append(engine.fieldName(x,y));}
            setupTeam.setText(briefing?info.dossierStatus:team.toString());setupTeam.setVisibility(showBoard?View.VISIBLE:View.GONE);
            setupHint.setText(building?"↑ NORDEN · PORTAL · Live-Aufbau":briefing?"Alle bestätigen Gelesen am eigenen Handy.":info.setupStage.equals("launching")?"Aufbau gespeichert. Stellt eure Figuren auf die markierten Startfelder.":"Die Anfrage erscheint auf dem ausgewählten Spielerhandy.");
        }
        updateTrivia(info.preparing&&info.setupStage.equals("building"),info.sessionId+":"+info.setupStage);
        boolean ended=wasPreparing&&!info.preparing;wasPreparing=info.preparing;if(ended){if(engine.playing()){play.setVisibility(View.VISIBLE);lobbyBackground.setVisibility(View.VISIBLE);}else showLobby();}
        if(activeNotice.isEmpty()&&!info.setupStage.equals("selecting")&&!info.setupStage.equals("launching")){
            String seen=prefs.getString("seen-notice-"+info.sessionId,"");int next=0;
            for(int i=0;i<info.notices.size();i++)if(info.notices.get(i).id.equals(seen))next=i+1;
            if(next<info.notices.size()){
                GameEngine.PortalNotice notice=info.notices.get(next);activeNotice=notice.id;boolean foundKey=notice.id.equals(info.keyEvent);noticeFlight.setVisibility(foundKey?View.GONE:View.VISIBLE);noticeKey.setVisibility(foundKey?View.VISIBLE:View.GONE);noticeText.setGravity(foundKey?Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL:Gravity.CENTER);noticeText.setText(notice.text);noticeScreen.setVisibility(View.VISIBLE);noticeScreen.bringToFront();handler.postDelayed(finishNotice,3000);
            }
        }
    }
    private void updateTrivia(boolean building,String identity){
        if(!building){handler.removeCallbacks(fadeTrivia);setupTrivia.animate().cancel();setupTrivia.setAlpha(1);setupTrivia.setVisibility(View.GONE);triviaSetup="";triviaIndex=-1;return;}
        long now=SystemClock.elapsedRealtime();
        if(!identity.equals(triviaSetup)){triviaSetup=identity;triviaStarted=now;triviaIndex=-1;}
        long age=now-triviaStarted;int index=(int)(age/8000)%PortalTrivia.FACTS.size();
        setupTrivia.setVisibility(View.VISIBLE);
        if(triviaIndex!=index){
            handler.removeCallbacks(fadeTrivia);setupTrivia.animate().cancel();triviaIndex=index;setupTrivia.setText(PortalTrivia.FACTS.get(index));
            boolean reduced=!android.animation.ValueAnimator.areAnimatorsEnabled();setupTrivia.setAlpha(reduced?1:0);
            if(!reduced){setupTrivia.animate().alpha(1).setDuration(500).start();handler.postDelayed(fadeTrivia,6500);}
        }
    }
    private void showLobby(){play.setVisibility(View.GONE);lobbyBackground.setVisibility(View.VISIBLE);lobby.setVisibility(View.VISIBLE);director.setVisibility(View.GONE);stage.setVisibility(View.GONE);}
    private void showDirector(){play.setVisibility(View.GONE);lobbyBackground.setVisibility(View.GONE);lobby.setVisibility(View.GONE);director.setVisibility(View.VISIBLE);stage.setVisibility(View.GONE);updatePortal();}
    private void updatePortal(){
        if(engine==null||network==null||playerRows==null)return;
        try{engine.tick();}catch(IOException error){gameStatus.setText("Speichern fehlgeschlagen. Der letzte bestätigte Zustand bleibt erhalten.");return;}
        boolean wifiReady=reservation!=null&&!ssid.isEmpty();
        if(wifiReady)network.update(server.port());
        String ip=wifiReady?network.address:"",host=wifiReady?network.hostname:"";
        wifiName.setText(ssid.isEmpty()?"Spiel-WLAN noch nicht gestartet":ssid);
        wifiPassword.setText(password.isEmpty()?"":("Passwort  "+password));
        wifiStatus.setText(hotspotPending?"WLAN startet …":wifiReady?"Hotspot läuft · ohne Internet verbunden bleiben":"Zum Verbinden Spiel-WLAN starten");
        wifiButton.setEnabled(!hotspotPending&&reservation==null);wifiButton.setText(hotspotPending?"WLAN startet …":wifiReady?"Spiel-WLAN läuft":"Spiel-WLAN starten");
        website.setText(ip.isEmpty()?"WLAN zuerst starten":ConnectionLinks.site(host.isEmpty()?ip:host,server.port()));
        fallback.setText(ip.isEmpty()?"Danach erscheint hier deine Spielseite.":host.isEmpty()?network.status:"Alternativ: "+ConnectionLinks.site(ip,server.port()));
        java.util.List<String[]> players=engine.playersForHost();countBadge.setText(players.size()+" / 4");
        playerCount.setText("DEIN TEAM · "+players.size()+" / 4 · "+engine.selectedCount()+" bereit");
        String signature=Arrays.deepToString(players.toArray());
        if(!signature.equals(rosterSignature)){
            rosterSignature=signature;playerRows.removeAllViews();
            if(players.isEmpty())playerRows.addView(text("Noch keine Spieler beigetreten.",14,MUTED));
            for(String[] p:players){
                LinearLayout r=row();r.setPadding(0,dp(4),0,dp(4));CharacterAvatarView avatar=new CharacterAvatarView(this);
                String id=p[3];avatar.setCharacter(id,p[2]);
                r.addView(avatar,new LinearLayout.LayoutParams(dp(44),dp(44)));LinearLayout labels=column();labels.setPadding(dp(12),0,0,0);
                labels.addView(text(p[1],17,WHITE));labels.addView(text(id.isEmpty()?"Wählt noch eine Figur …":p[2],12,MUTED));
                r.addView(labels,new LinearLayout.LayoutParams(0,-2,1));
                Button remove=button("×",()->{try{engine.removePlayer(p[0]);rosterSignature="";updatePortal();}catch(IOException e){toast("Entfernen fehlgeschlagen. Spieler bleibt angemeldet.");}});remove.setTextColor(0xffff6666);remove.setTextSize(26);remove.setMinWidth(0);remove.setMinimumWidth(0);remove.setPadding(0,0,0,0);remove.setContentDescription(p[1]+" aus der Partie entfernen");r.addView(remove,new LinearLayout.LayoutParams(dp(48),dp(48)));playerRows.addView(r);
            }
        }
        startButton.setEnabled(engine.readyForPlay());startButton.setText(engine.playing()?"Spielansicht öffnen":engine.preparing()?"Aufbau läuft…":"Abenteuer starten");
        gameStatus.setText(engine.hostText());stageStatus.setText(summary());
        GameEngine.PortalState info=engine.portalState();updateSetup(info);updateAudio(info,players);
        playRound.setText("RUNDE "+info.round);playActor.setText(info.headline);
        goCard.setVisibility(info.awaitingGo?View.VISIBLE:View.GONE);playDice.setVisibility(info.awaitingGo?View.GONE:View.VISIBLE);playDiceLabel.setVisibility(info.awaitingGo?View.GONE:View.VISIBLE);
        if(info.awaitingGo&&info.activeCharacter!=null){
            CharacterProfile c=info.activeCharacter;goCharacter.setText(c.name);goDescription.setText(c.description);goStats.setText("Ausdauer: "+c.endurance+"  ·  Kraft: "+c.strength);goAbilities.setText(android.text.TextUtils.join("\n",c.abilities));
            if(!goCharacterId.equals(c.id)){goCharacterId=c.id;goPortrait.setContentDescription(c.name+" · Charakterbild");
                try(InputStream in=getAssets().open("portraits/"+c.id+".jpg")){goPortrait.setImageBitmap(BitmapFactory.decodeStream(in));}catch(IOException missing){goPortrait.setImageDrawable(null);}
            }
        }
        if(play.getVisibility()==View.VISIBLE&&!info.awaitingGo){
            String seenKey="seen-roll-"+info.sessionId;
            playDice.showRoll(info.rollId.isEmpty()?"legacy-"+info.die:info.rollId,info.die,!info.rollId.isEmpty()&&!prefs.getString(seenKey,"").equals(info.rollId),()->{prefs.edit().putString(seenKey,info.rollId).apply();updatePortal();});
        }
        boolean revealing=!info.awaitingGo&&playDice.isAnimating();playDetail.setText(revealing?"Der Wurf entsteht im Zeitwirbel …":info.detail);
        playDiceLabel.setText(info.die==0?"Noch kein Wurf":revealing?info.rollPlayer+" würfelt …":info.rollPlayer+" · letzter Wurf: "+info.die);
        StringBuilder events=new StringBuilder();
        if(revealing)events.append(info.rollPlayer).append(" würfelt. Gleich zeigt sich das Ergebnis.");
        else for(int i=info.events.size()-1;i>=Math.max(0,info.events.size()-1);i--){if(events.length()>0)events.append("\n\n");events.append(info.events.get(i));}
        playEvents.setText(events.toString());playPositions.setText(android.text.TextUtils.join("\n",info.positions));
    }
    private void updateAudio(GameEngine.PortalState info,java.util.List<String[]> players){
        java.util.List<String> ids=new ArrayList<>();for(String[] p:players)ids.add(p[0]);
        if(audioEvents==null){audioEvents=new PortalAudioEvents(info.sessionId,info.setupStage,ids);return;}
        PortalAudioEvents.Cue cue=audioEvents.observe(info.sessionId,info.setupStage,ids);if(!foreground||audio==null)return;
        if(cue==PortalAudioEvents.Cue.THEME)audio.playTheme();else if(cue==PortalAudioEvents.Cue.JOIN)audio.playJoin();
    }
    private String summary(){StringBuilder b=new StringBuilder(engine.hostText().split("\n")[0]);for(String line:engine.hostText().split("\n"))if(line.startsWith("Am Zug:")||line.startsWith("Gegnerzug:")||line.startsWith("Noch ")||line.startsWith("Wurf "))b.append('\n').append(line);return b.append("\n\nAntippen · Portal-Lobby").toString();}
    private void startGame(){try{engine.startDemo();updatePortal();if(engine.playing())showPlay();}catch(IOException|IllegalArgumentException e){toast(e.getMessage());}}
    private void showBoard(){
        GameEngine.PortalState info=engine.portalState();if(info.tiles.isEmpty()){toast("Bisher ist nur das alte Testfeld gespeichert. Unter Spielleitung kannst du euren Aufbau erfassen.");return;}
        LinearLayout content=column();BoardView board=new BoardView(this);board.setPlan(info.tiles,info.boardPlayers,info.subdivisions);content.addView(board,new LinearLayout.LayoutParams(-1,dp(360)));
        StringBuilder team=new StringBuilder();for(int i=0;i<info.boardPlayers.size();i++){String[] p=info.boardPlayers.get(i);int x=Integer.parseInt(p[2]),y=Integer.parseInt(p[3]);team.append(i+1).append(" · ").append(p[0]).append(" · ").append(x<0?"noch nicht aufgestellt":engine.fieldName(x,y)).append("\n");}TextView positions=text(team.toString(),16,CYAN);positions.setPadding(dp(15),dp(10),dp(15),dp(10));content.addView(positions);
        new AlertDialog.Builder(this).setTitle("Euer Raumgefüge · aktuelle Positionen").setView(content).setPositiveButton("Zurück",null).show();
    }
    private void settingsDialog(){
        String[] options={"Spiel speichern","Spiel laden","Spielstand als Datei exportieren","Spielstanddatei importieren","Spiel zurücksetzen","Raumgefüge anzeigen","Raumgefüge bearbeiten","Gegner verwalten"};
        new AlertDialog.Builder(this).setTitle("Einstellungen").setItems(options,(d,i)->{switch(i){case 0:saveDialog();break;case 1:loadDialog();break;case 2:exportDialog();break;case 3:startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("*/*"),92);break;case 4:resetDialog(false);break;case 5:showBoard();break;case 6:new AlertDialog.Builder(this).setMessage("Partie zum Bearbeiten des Raumgefüges pausieren?").setPositiveButton("Bearbeiten",(a,b)->{try{engine.rebuildBoard();updatePortal();}catch(Exception e){toast(e.getMessage());}}).setNegativeButton("Zurück",null).show();break;case 7:showDirector();break;}}).setNegativeButton("Zurück",null).show();
    }
    private File archiveDir(){File dir=new File(getFilesDir(),"saved-games");dir.mkdirs();return dir;}
    private void saveNamed(String name)throws IOException{
        String stamp=new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS",Locale.GERMANY).format(new Date());
        String safe=name.replaceAll("[^a-zA-Z0-9äöüÄÖÜß _-]","").trim();if(safe.isEmpty())safe="Abenteuer";if(safe.length()>40)safe=safe.substring(0,40);
        new AndroidStore(new File(archiveDir(),stamp+"_"+safe+".drwho")).save(engine.exportGame());
    }
    private void saveDialog(){EditText name=new EditText(this);name.setHint("Name des Spielstands");name.setText("Abenteuer");new AlertDialog.Builder(this).setTitle("Gesamtes Spiel speichern").setMessage("Raumplanung, alle Figurenpositionen, Runde, Würfel und Story werden gespeichert.").setView(name).setPositiveButton("Speichern",(d,w)->{try{saveNamed(name.getText().toString());toast("Gesamtes Spiel gespeichert.");}catch(IOException e){toast("Speichern fehlgeschlagen: "+e.getMessage());}}).setNegativeButton("Zurück",null).show();}
    private void loadDialog(){File[] files=archiveDir().listFiles((dir,n)->n.endsWith(".drwho"));if(files==null||files.length==0){toast("Noch kein Spielstand gespeichert.");return;}Arrays.sort(files,(a,b)->b.getName().compareTo(a.getName()));String[] names=new String[files.length];for(int i=0;i<files.length;i++)names[i]=files[i].getName().replace(".drwho","");new AlertDialog.Builder(this).setTitle("Gespeichertes Spiel laden").setItems(names,(d,i)->new AlertDialog.Builder(this).setTitle("Spiel laden?").setMessage("Der aktuelle Stand wird vorher automatisch gesichert. Spieler treten danach mit ihrem gespeicherten Namen erneut bei.").setPositiveButton("Laden",(a,b)->{try{byte[] bytes=new AndroidStore(files[i]).load();saveNamed("Vor_Laden");engine.loadGame(bytes);afterGameChange();toast("Geladen. Spieler mit ihrem bisherigen Namen beitreten lassen.");}catch(IOException e){toast("Laden fehlgeschlagen. Aktuelle Partie bleibt erhalten. "+e.getMessage());}}).setNegativeButton("Zurück",null).show()).setNegativeButton("Zurück",null).show();}
    private void resetDialog(boolean fullPortal){
        String message="Zurücksetzen verwirft die laufende Partie ohne neue Sicherung. Alternativ kannst du vorher den gesamten Spielstand sichern.";
        if(fullPortal)message+=" Der Spiel-Hotspot wird beendet und die Startseite vollständig geleert.";
        LinearLayout choices=padded();choices.addView(text(message,15,WHITE));
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle(fullPortal?"Portal vollständig zurücksetzen?":"Laufendes Spiel zurücksetzen?").setView(choices).create();
        choices.addView(button("Zurück",dialog::dismiss));
        choices.addView(button("Zurücksetzen (ohne Speicherung)",()->{dialog.dismiss();resetCurrentGame(false,fullPortal);}));
        choices.addView(button("Sichern und zurücksetzen",()->{dialog.dismiss();resetCurrentGame(true,fullPortal);}));dialog.show();
    }
    private void resetCurrentGame(boolean save,boolean fullPortal){try{
        if(save)saveNamed("Vor_Reset");engine.resetGame();
        if(fullPortal){stopGameHotspot();prefs.edit().clear().apply();lobby.scrollTo(0,0);}
        afterGameChange();
    }catch(IOException e){toast("Zurücksetzen abgebrochen: "+e.getMessage());}}
    private void restartPortalNetwork(){
        int generation=++networkGeneration;if(network!=null)network.close();
        network=new PortalNetwork(this,name->{if(!destroyed&&generation==networkGeneration&&server!=null)server.setLocalHostname(name);});
    }
    private void stopGameHotspot(){
        hotspotGeneration++;hotspotPermissionRequested=false;hotspotPending=false;
        WifiManager.LocalOnlyHotspotReservation active=reservation;reservation=null;ssid="";password="";
        if(active!=null)active.close();restartPortalNetwork();
    }
    private void afterGameChange(){if(audio!=null)audio.stop();audioEvents=null;GameEngine.PortalState restored=engine.portalState();if(!restored.notices.isEmpty())prefs.edit().putString("seen-notice-"+restored.sessionId,restored.notices.get(restored.notices.size()-1).id).apply();handler.removeCallbacks(finishNotice);activeNotice="";noticeScreen.setVisibility(View.GONE);wasPreparing=false;rosterSignature="";showLobby();if(engine.playing())showPlay();updatePortal();}
    private void exportDialog(){startActivityForResult(new Intent(Intent.ACTION_CREATE_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("application/octet-stream").putExtra(Intent.EXTRA_TITLE,"Dr_Who_Spielstand.drwho"),91);}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(result!=RESULT_OK||data==null||data.getData()==null)return;try{
        if(request==91){try(OutputStream out=getContentResolver().openOutputStream(data.getData())){if(out==null)throw new IOException("Datei nicht erreichbar.");out.write(engine.exportGame());}toast("Spielstanddatei exportiert.");}
        if(request==92){byte[] bytes;try(InputStream in=getContentResolver().openInputStream(data.getData());ByteArrayOutputStream out=new ByteArrayOutputStream()){if(in==null)throw new IOException("Datei nicht erreichbar.");byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1){out.write(b,0,n);if(out.size()>8*1024*1024)throw new IOException("Spielstand zu groß.");}bytes=out.toByteArray();}new AlertDialog.Builder(this).setTitle("Datei als Spielstand laden?").setMessage("Der aktuelle Stand wird vorher gesichert. Danach treten Spieler mit ihrem gespeicherten Namen bei.").setPositiveButton("Laden",(d,w)->{try{saveNamed("Vor_Import");engine.loadGame(bytes);afterGameChange();}catch(IOException e){toast("Datei ungültig. Aktuelle Partie bleibt erhalten.");}}).setNegativeButton("Zurück",null).show();}
    }catch(IOException e){toast("Datei konnte nicht verarbeitet werden: "+e.getMessage());}}
    private void enemyDialog(){
        java.util.List<CharacterProfile> available=engine.availableEnemies();if(available.isEmpty()){toast(engine.readyForPlay()?"Keine übrige bekannte Figur verfügbar.":"Zuerst wählen alle Spieler ihre Figuren.");return;}
        String[] names=new String[available.size()];for(int i=0;i<names.length;i++)names[i]=available.get(i).name;
        new AlertDialog.Builder(this).setTitle("Gegner hinzufügen").setItems(names,(d,which)->{try{engine.addEnemy(available.get(which).id);updatePortal();}catch(IOException|IllegalArgumentException e){toast(e.getMessage());}}).setNegativeButton("Abbrechen",null).show();
    }
    private void attackDialog(){
        java.util.List<String[]> players=engine.playersForHost();if(players.isEmpty()){toast("Zuerst Spieler anmelden.");return;}
        LinearLayout content=padded();Spinner target=new Spinner(this);String[] names=new String[players.size()];for(int i=0;i<names.length;i++)names[i]=players.get(i)[1]+" · "+players.get(i)[2];
        target.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,names));content.addView(target);
        EditText outcome=new EditText(this);outcome.setHint("Was ist mit dem Charakter passiert?");outcome.setMinLines(3);outcome.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(400)});content.addView(outcome);
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Angriffsfolge anzeigen").setView(content).setPositiveButton("Beim Ziel anzeigen",null).setNegativeButton("Abbrechen",null).create();
        dialog.setOnShowListener(v->dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(button->{try{engine.reportAttack(players.get(target.getSelectedItemPosition())[0],outcome.getText().toString());dialog.dismiss();updatePortal();}catch(IOException|IllegalArgumentException e){toast(e.getMessage());}}));dialog.show();
    }
    private void startAutomaticHotspot(){
        if(reservation!=null||hotspotPending||hotspotPermissionRequested)return;
        String permission=Build.VERSION.SDK_INT>=33?Manifest.permission.NEARBY_WIFI_DEVICES:Manifest.permission.ACCESS_FINE_LOCATION;
        if(checkSelfPermission(permission)!=PackageManager.PERMISSION_GRANTED){hotspotPermissionRequested=true;requestPermissions(Build.VERSION.SDK_INT>=33?new String[]{permission}:new String[]{permission,Manifest.permission.ACCESS_COARSE_LOCATION},7);return;}
        hotspotPending=true;int generation=++hotspotGeneration;updatePortal();
        WifiManager.LocalOnlyHotspotCallback callback=new WifiManager.LocalOnlyHotspotCallback(){
            @Override public void onStarted(WifiManager.LocalOnlyHotspotReservation r){
                if(destroyed||generation!=hotspotGeneration){r.close();return;}
                hotspotPending=false;reservation=r;
                if(Build.VERSION.SDK_INT>=30){ssid=clean(r.getSoftApConfiguration().getSsid());password=clean(r.getSoftApConfiguration().getPassphrase());}
                else{ssid=clean(r.getWifiConfiguration().SSID);password=clean(r.getWifiConfiguration().preSharedKey);}updatePortal();
            }
            @Override public void onStopped(){if(destroyed||generation!=hotspotGeneration)return;stopGameHotspot();updatePortal();}
            @Override public void onFailed(int reason){if(destroyed||generation!=hotspotGeneration)return;hotspotPending=false;toast("WLAN-Start fehlgeschlagen ("+reason+"). Prüfe die Android-WLAN-Einstellungen und versuche es erneut.");updatePortal();}
        };
        try{WifiManager wifi=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);wifi.startLocalOnlyHotspot(callback,handler);}
        catch(SecurityException|IllegalStateException|UnsupportedOperationException e){hotspotPending=false;toast("WLAN konnte nicht gestartet werden. Prüfe WLAN-Berechtigung und Android-Einstellungen.");updatePortal();}
    }
    private String clean(String value){if(value==null)return "";return value.length()>1&&value.startsWith("\"")&&value.endsWith("\"")?value.substring(1,value.length()-1):value;}
    @Override public void onRequestPermissionsResult(int request,String[] permissions,int[] results){super.onRequestPermissionsResult(request,permissions,results);if(request==7&&hotspotPermissionRequested&&!destroyed){hotspotPermissionRequested=false;String p=Build.VERSION.SDK_INT>=33?Manifest.permission.NEARBY_WIFI_DEVICES:Manifest.permission.ACCESS_FINE_LOCATION;if(checkSelfPermission(p)==PackageManager.PERMISSION_GRANTED)startAutomaticHotspot();else toast("Zum Starten des Spiel-WLANs wird die Android-WLAN-Berechtigung benötigt.");}}
    private void showQr(boolean wifi){
        if(reservation==null){toast("Zuerst Spiel-WLAN starten.");return;}
        String value;
        try{value=wifi?ConnectionLinks.wifiQr(ssid,password):ConnectionLinks.site(network.address,server.port());}
        catch(IllegalArgumentException error){toast("Zuerst das WLAN einrichten und auf die Adresse warten.");return;}
        try{
            Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            BitMatrix bits=new QRCodeWriter().encode(value,BarcodeFormat.QR_CODE,720,720,hints);int[] pixels=new int[720*720];for(int y=0;y<720;y++)for(int x=0;x<720;x++)pixels[y*720+x]=bits.get(x,y)?Color.BLACK:Color.WHITE;
            Bitmap bitmap=Bitmap.createBitmap(pixels,720,720,Bitmap.Config.ARGB_8888);ImageView image=new ImageView(this);image.setImageBitmap(bitmap);image.setAdjustViewBounds(true);image.setPadding(dp(14),dp(14),dp(14),dp(14));image.setContentDescription(wifi?"QR-Code zum Spiel-WLAN":"QR-Code zur Spielseite");
            new AlertDialog.Builder(this).setTitle(wifi?"1 · Mit dem WLAN verbinden":"2 · Spielseite öffnen").setView(image).setMessage(wifi?"Mit der Kamera des Spielerhandys scannen.":"Zuerst mit dem WLAN verbinden, dann scannen.").setPositiveButton("Fertig",null).show();
        }catch(WriterException error){toast("QR-Code konnte nicht erstellt werden.");}
    }
    private void toast(String message){Toast.makeText(this,message,Toast.LENGTH_LONG).show();}
    @Override protected void onStart(){super.onStart();foreground=true;audioEvents=null;if(noticeScreen!=null&&!activeNotice.isEmpty()){handler.removeCallbacks(finishNotice);handler.postDelayed(finishNotice,3000);}if(engine!=null&&playerRows!=null)handler.post(refresh);}
    @Override protected void onStop(){foreground=false;if(audio!=null)audio.stop();handler.removeCallbacks(refresh);handler.removeCallbacks(finishNotice);handler.removeCallbacks(fadeTrivia);if(setupTrivia!=null){setupTrivia.animate().cancel();triviaSetup="";triviaIndex=-1;}super.onStop();}
    @Override protected void onDestroy(){destroyed=true;if(audio!=null)audio.stop();hotspotGeneration++;networkGeneration++;hotspotPermissionRequested=false;handler.removeCallbacks(refresh);handler.removeCallbacks(finishNotice);handler.removeCallbacks(fadeTrivia);if(reservation!=null)reservation.close();if(network!=null)network.close();if(server!=null)server.close();super.onDestroy();}
    private static final class AndroidStore implements GameEngine.Store{
        private final AtomicFile file;AndroidStore(File path){file=new AtomicFile(path);}
        @Override public byte[] load()throws IOException{try{return file.readFully();}catch(FileNotFoundException missing){return null;}}
        @Override public void save(byte[] data)throws IOException{FileOutputStream out=file.startWrite();try{out.write(data);file.finishWrite(out);}catch(IOException error){file.failWrite(out);throw error;}}
    }
}
