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
import android.provider.Settings;
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
    private LocalServer server;
    private PortalNetwork network;
    private WifiManager.LocalOnlyHotspotReservation reservation;
    private boolean hotspotPending,destroyed,confirming;
    private FrameLayout root,stage,setupScreen,noticeScreen;
    private TardisView setupFlight;
    private BoardView setupBoard;
    private TextView setupHeading,setupHint,setupTeam,noticeText;
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
    private TextView countBadge,playerCount,wifiName,wifiPassword,wifiStatus,website,fallback,code,gameStatus,stageStatus;
    private Button wifiButton,startButton;
    private String ssid="",password="",rosterSignature="";
    private final Runnable refresh=new Runnable(){public void run(){updatePortal();handler.postDelayed(this,500);}};

    @Override public void onCreate(Bundle saved){
        super.onCreate(saved);getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        root=new FrameLayout(this);root.setBackgroundColor(INK);
        root.setOnApplyWindowInsetsListener((v,i)->{v.setPadding(i.getSystemWindowInsetLeft(),i.getSystemWindowInsetTop(),i.getSystemWindowInsetRight(),i.getSystemWindowInsetBottom());return i;});
        lobbyBackground=new ImageView(this);lobbyBackground.setImageResource(R.drawable.portal_background);
        lobbyBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);lobbyBackground.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        root.addView(lobbyBackground,new FrameLayout.LayoutParams(-1,-1));
        setContentView(root);prefs=getSharedPreferences("portal-wifi",MODE_PRIVATE);
        if(prefs.getBoolean("manualConfirmed",false)){ssid=prefs.getString("ssid",ConnectionLinks.SSID);password=prefs.getString("password",ConnectionLinks.PASSWORD);}
        try{
            engine=new GameEngine(new AndroidStore(new File(getFilesDir(),"session-v1.drw")));
            byte[] page=asset("player.html");
            try{server=new LocalServer(engine,page,80,this::portrait,BuildConfig.VERSION_NAME);}catch(IOException unavailable){server=new LocalServer(engine,page,8080,this::portrait,BuildConfig.VERSION_NAME);}
            network=new PortalNetwork(this,name->{if(server!=null)server.setLocalHostname(name);});
        }catch(IOException error){root.addView(text("Start fehlgeschlagen. Der vorhandene Spielstand bleibt erhalten.\n"+error.getMessage(),18,WHITE));return;}
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
        LinearLayout lobbyTabs=row();Button connectionTab=button("WLAN & Zugang",()->{}),teamTab=button("Spieler",()->{});lobbyTabs.addView(connectionTab,new LinearLayout.LayoutParams(0,-2,1));lobbyTabs.addView(teamTab,new LinearLayout.LayoutParams(0,-2,1));content.addView(lobbyTabs);
        LinearLayout connection=card(content);connection.addView(text("1  MIT DEM WLAN VERBINDEN",11,MUTED));
        wifiName=text("WLAN starten",22,WHITE);connection.addView(wifiName);wifiName.setTextIsSelectable(true);
        wifiPassword=text("",18,CYAN);wifiPassword.setTextIsSelectable(true);connection.addView(wifiPassword);
        wifiStatus=text("",12,MUTED);connection.addView(wifiStatus);
        wifiButton=button("Spiel-WLAN einrichten",this::wifiOptions);connection.addView(wifiButton);
        connection.addView(text("2  SPIELSEITE ÖFFNEN",11,MUTED));website=text("Warte auf WLAN …",22,CYAN);website.setTextIsSelectable(true);connection.addView(website);
        fallback=text("",12,MUTED);fallback.setTextIsSelectable(true);connection.addView(fallback);
        LinearLayout qrButtons=row();qrButtons.addView(button("WLAN-QR",()->showQr(true)),new LinearLayout.LayoutParams(0,-2,1));qrButtons.addView(button("Seiten-QR",()->showQr(false)),new LinearLayout.LayoutParams(0,-2,1));connection.addView(qrButtons);
        code=text("Nur Name eingeben · keinen Portalcode nötig",13,CYAN);connection.addView(code);
        LinearLayout players=card(content);playerCount=text("DEIN TEAM · 0 / 4",16,CYAN);players.addView(playerCount);playerRows=column();players.addView(playerRows);
        connectionTab.setOnClickListener(v->{connection.setVisibility(View.VISIBLE);players.setVisibility(View.GONE);});teamTab.setOnClickListener(v->{connection.setVisibility(View.GONE);players.setVisibility(View.VISIBLE);});
        if(ssid.isEmpty())players.setVisibility(View.GONE);else connection.setVisibility(View.GONE);
        startButton=button("Abenteuer starten",()->{if(engine.playing())showPlay();else startGame();});content.addView(startButton);
        LinearLayout footer=row();footer.addView(button("Einstellungen",this::settingsDialog),new LinearLayout.LayoutParams(0,-2,1));footer.addView(button("Zurücksetzen",this::resetDialog),new LinearLayout.LayoutParams(0,-2,1));content.addView(footer);
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
        setupTeam=text("",16,CYAN);layout.addView(setupTeam);setupHint=text("",17,MUTED);setupHint.setGravity(Gravity.CENTER);layout.addView(setupHint);layout.addView(button("Einstellungen",this::settingsDialog));
        noticeScreen=new FrameLayout(this);noticeScreen.setBackgroundColor(INK);root.addView(noticeScreen,new FrameLayout.LayoutParams(-1,-1));noticeScreen.setVisibility(View.GONE);noticeScreen.setClickable(true);
        noticeScreen.addView(new TardisView(this,true),new FrameLayout.LayoutParams(-1,-1));noticeText=text("",32,WHITE);noticeText.setGravity(Gravity.CENTER);noticeText.setAutoSizeTextTypeUniformWithConfiguration(18,32,2,android.util.TypedValue.COMPLEX_UNIT_SP);noticeText.setPadding(dp(25),dp(24),dp(25),dp(24));noticeText.setBackgroundColor(0xb807111e);noticeScreen.addView(noticeText,new FrameLayout.LayoutParams(-1,-1,Gravity.CENTER));
        noticeScreen.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);noticeText.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
    }
    private void updateSetup(GameEngine.PortalState info){
        setupScreen.setVisibility(info.preparing?View.VISIBLE:View.GONE);
        if(info.preparing){
            lobby.setVisibility(View.GONE);director.setVisibility(View.GONE);play.setVisibility(View.GONE);stage.setVisibility(View.GONE);
            setupHeading.setText(info.setupTitle);boolean building=info.setupStage.equals("building");setupFlight.setVisibility(building?View.GONE:View.VISIBLE);setupBoard.setVisibility(building?View.VISIBLE:View.GONE);
            if(building)setupBoard.setPlan(info.tiles,info.boardPlayers,info.subdivisions);
            StringBuilder team=new StringBuilder();for(int i=0;i<info.boardPlayers.size();i++){String[] p=info.boardPlayers.get(i);if(team.length()>0)team.append("\n");team.append(i+1).append(" · ").append(p[0]);int x=Integer.parseInt(p[2]),y=Integer.parseInt(p[3]);if(x>=0)team.append(" · ").append(engine.fieldName(x,y));}
            setupTeam.setText(team.toString());setupTeam.setVisibility(building?View.VISIBLE:View.GONE);
            setupHint.setText(building?"↑ NORDEN · PORTAL\nDer gespeicherte Aufbau vom Raummeister-Handy erscheint hier live. Nummern zeigen die Startfelder.":info.setupStage.equals("launching")?"Aufbau gespeichert. Stellt eure Figuren auf die markierten Startfelder.":"Die Anfrage erscheint auf dem ausgewählten Spielerhandy.");
        }
        boolean ended=wasPreparing&&!info.preparing;wasPreparing=info.preparing;if(ended){play.setVisibility(View.VISIBLE);lobbyBackground.setVisibility(View.VISIBLE);}
        if(activeNotice.isEmpty()&&!info.setupStage.equals("selecting")&&!info.setupStage.equals("launching")){
            String seen=prefs.getString("seen-notice-"+info.sessionId,"");int next=0;
            for(int i=0;i<info.notices.size();i++)if(info.notices.get(i).id.equals(seen))next=i+1;
            if(next<info.notices.size()){
                GameEngine.PortalNotice notice=info.notices.get(next);activeNotice=notice.id;noticeText.setText(notice.text);noticeScreen.setVisibility(View.VISIBLE);noticeScreen.bringToFront();handler.postDelayed(finishNotice,3000);
            }
        }
    }
    private void showLobby(){play.setVisibility(View.GONE);lobbyBackground.setVisibility(View.VISIBLE);lobby.setVisibility(View.VISIBLE);director.setVisibility(View.GONE);stage.setVisibility(View.GONE);}
    private void showDirector(){play.setVisibility(View.GONE);lobbyBackground.setVisibility(View.GONE);lobby.setVisibility(View.GONE);director.setVisibility(View.VISIBLE);stage.setVisibility(View.GONE);updatePortal();}
    private void updatePortal(){
        if(engine==null||network==null||playerRows==null)return;
        try{engine.tick();}catch(IOException error){gameStatus.setText("Speichern fehlgeschlagen. Der letzte bestätigte Zustand bleibt erhalten.");return;}
        network.update(server.port());
        String ip=network.address,host=network.hostname;
        wifiName.setText(ssid.isEmpty()?"Spiel-WLAN noch nicht eingerichtet":ssid);
        wifiPassword.setText(password.isEmpty()?"":("Passwort  "+password));
        wifiStatus.setText(hotspotPending?"WLAN startet …":reservation!=null?"Hotspot läuft · ohne Internet verbunden bleiben":prefs.getBoolean("manualConfirmed",false)?"WLAN-Daten aus deiner Einrichtung": "Einrichten oder automatisch starten");
        website.setText(ip.isEmpty()?"WLAN zuerst starten":ConnectionLinks.site(host.isEmpty()?ip:host,server.port()));
        fallback.setText(ip.isEmpty()?"Danach erscheint hier deine Spielseite.":host.isEmpty()?network.status:"Alternativ: "+ConnectionLinks.site(ip,server.port()));
        java.util.List<String[]> players=engine.playersForHost();countBadge.setText(players.size()+" / 4");
        playerCount.setText("DEIN TEAM · "+players.size()+" / 4 · "+engine.selectedCount()+" bereit");
        String signature=Arrays.deepToString(players.toArray());
        if(!signature.equals(rosterSignature)){
            rosterSignature=signature;playerRows.removeAllViews();
            for(int slot=0;slot<4;slot++){
                LinearLayout r=row();r.setPadding(0,dp(9),0,dp(9));CharacterAvatarView avatar=new CharacterAvatarView(this);
                String[] p=slot<players.size()?players.get(slot):null;String id=p==null?"":p[3];avatar.setCharacter(id,p==null?"Freier Platz":p[2]);
                r.addView(avatar,new LinearLayout.LayoutParams(dp(52),dp(52)));LinearLayout labels=column();labels.setPadding(dp(12),0,0,0);
                labels.addView(text(p==null?"Platz "+(slot+1)+" frei":p[1],18,p==null?MUTED:WHITE));labels.addView(text(p==null?"Bereit für einen Mitreisenden":id.isEmpty()?"Wählt noch eine Figur …":p[2],13,MUTED));
                r.addView(labels,new LinearLayout.LayoutParams(0,-2,1));r.addView(text(id.isEmpty()?"○":"✓",21,id.isEmpty()?MUTED:CYAN));playerRows.addView(r);
            }
        }
        startButton.setEnabled(engine.readyForPlay());startButton.setText(engine.playing()?"Spielansicht öffnen":engine.preparing()?"Aufbau läuft…":"Abenteuer starten");
        gameStatus.setText(engine.hostText());stageStatus.setText(summary());
        GameEngine.PortalState info=engine.portalState();updateSetup(info);
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
    private String summary(){StringBuilder b=new StringBuilder(engine.hostText().split("\n")[0]);for(String line:engine.hostText().split("\n"))if(line.startsWith("Am Zug:")||line.startsWith("Gegnerzug:")||line.startsWith("Noch ")||line.startsWith("Wurf "))b.append('\n').append(line);return b.append("\n\nAntippen · Portal-Lobby").toString();}
    private void startGame(){try{engine.startDemo();updatePortal();if(engine.playing())showPlay();}catch(IOException|IllegalArgumentException e){toast(e.getMessage());}}
    private void showBoard(){
        GameEngine.PortalState info=engine.portalState();if(info.tiles.isEmpty()){toast("Bisher ist nur das alte Testfeld gespeichert. Unter Spielleitung kannst du euren Aufbau erfassen.");return;}
        LinearLayout content=column();BoardView board=new BoardView(this);board.setPlan(info.tiles,info.boardPlayers,info.subdivisions);content.addView(board,new LinearLayout.LayoutParams(-1,dp(360)));
        StringBuilder team=new StringBuilder();for(int i=0;i<info.boardPlayers.size();i++){String[] p=info.boardPlayers.get(i);int x=Integer.parseInt(p[2]),y=Integer.parseInt(p[3]);team.append(i+1).append(" · ").append(p[0]).append(" · ").append(x<0?"noch nicht aufgestellt":engine.fieldName(x,y)).append("\n");}TextView positions=text(team.toString(),16,CYAN);positions.setPadding(dp(15),dp(10),dp(15),dp(10));content.addView(positions);
        new AlertDialog.Builder(this).setTitle("Euer Raumgefüge · aktuelle Positionen").setView(content).setPositiveButton("Zurück",null).show();
    }
    private void settingsDialog(){
        String[] options={"Spiel speichern","Spiel laden","Spielstand als Datei exportieren","Spielstanddatei importieren","Spiel zurücksetzen","Kerker öffnen / schließen","Raumgefüge anzeigen","Raumgefüge bearbeiten","Gegner verwalten","WLAN / Verbindung"};
        new AlertDialog.Builder(this).setTitle("Einstellungen").setItems(options,(d,i)->{switch(i){case 0:saveDialog();break;case 1:loadDialog();break;case 2:exportDialog();break;case 3:startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("*/*"),92);break;case 4:resetDialog();break;case 5:dungeonDialog();break;case 6:showBoard();break;case 7:new AlertDialog.Builder(this).setMessage("Partie zum Bearbeiten des Raumgefüges pausieren?").setPositiveButton("Bearbeiten",(a,b)->{try{engine.rebuildBoard();updatePortal();}catch(Exception e){toast(e.getMessage());}}).setNegativeButton("Zurück",null).show();break;case 8:showDirector();break;case 9:wifiOptions();break;}}).setNegativeButton("Zurück",null).show();
    }
    private File archiveDir(){File dir=new File(getFilesDir(),"saved-games");dir.mkdirs();return dir;}
    private void saveNamed(String name)throws IOException{
        String stamp=new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS",Locale.GERMANY).format(new Date());
        String safe=name.replaceAll("[^a-zA-Z0-9äöüÄÖÜß _-]","").trim();if(safe.isEmpty())safe="Abenteuer";if(safe.length()>40)safe=safe.substring(0,40);
        new AndroidStore(new File(archiveDir(),stamp+"_"+safe+".drwho")).save(engine.exportGame());
    }
    private void saveDialog(){EditText name=new EditText(this);name.setHint("Name des Spielstands");name.setText("Abenteuer");new AlertDialog.Builder(this).setTitle("Gesamtes Spiel speichern").setMessage("Raumplanung, alle Figurenpositionen, Runde, Würfel und Story werden gespeichert.").setView(name).setPositiveButton("Speichern",(d,w)->{try{saveNamed(name.getText().toString());toast("Gesamtes Spiel gespeichert.");}catch(IOException e){toast("Speichern fehlgeschlagen: "+e.getMessage());}}).setNegativeButton("Zurück",null).show();}
    private void loadDialog(){File[] files=archiveDir().listFiles((dir,n)->n.endsWith(".drwho"));if(files==null||files.length==0){toast("Noch kein Spielstand gespeichert.");return;}Arrays.sort(files,(a,b)->b.getName().compareTo(a.getName()));String[] names=new String[files.length];for(int i=0;i<files.length;i++)names[i]=files[i].getName().replace(".drwho","");new AlertDialog.Builder(this).setTitle("Gespeichertes Spiel laden").setItems(names,(d,i)->new AlertDialog.Builder(this).setTitle("Spiel laden?").setMessage("Der aktuelle Stand wird vorher automatisch gesichert. Spieler treten danach mit ihrem gespeicherten Namen erneut bei.").setPositiveButton("Laden",(a,b)->{try{byte[] bytes=new AndroidStore(files[i]).load();saveNamed("Vor_Laden");engine.loadGame(bytes);afterGameChange();toast("Geladen. Spieler mit ihrem bisherigen Namen beitreten lassen.");}catch(IOException e){toast("Laden fehlgeschlagen. Aktuelle Partie bleibt erhalten. "+e.getMessage());}}).setNegativeButton("Zurück",null).show()).setNegativeButton("Zurück",null).show();}
    private void resetDialog(){new AlertDialog.Builder(this).setTitle("Laufendes Spiel zurücksetzen?").setMessage("Ein vollständiger Sicherungsstand wird vorher angelegt. Anschließend beginnt eine neue Partie ohne Spieler und Aufbau.").setPositiveButton("Sichern und zurücksetzen",(d,w)->{try{saveNamed("Vor_Reset");engine.resetGame();afterGameChange();}catch(IOException e){toast("Zurücksetzen abgebrochen: "+e.getMessage());}}).setNegativeButton("Zurück",null).show();}
    private void afterGameChange(){handler.removeCallbacks(finishNotice);activeNotice="";noticeScreen.setVisibility(View.GONE);wasPreparing=false;rosterSignature="";showLobby();if(engine.playing())showPlay();updatePortal();}
    private void exportDialog(){startActivityForResult(new Intent(Intent.ACTION_CREATE_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("application/octet-stream").putExtra(Intent.EXTRA_TITLE,"Dr_Who_Spielstand.drwho"),91);}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(result!=RESULT_OK||data==null||data.getData()==null)return;try{
        if(request==91){try(OutputStream out=getContentResolver().openOutputStream(data.getData())){if(out==null)throw new IOException("Datei nicht erreichbar.");out.write(engine.exportGame());}toast("Spielstanddatei exportiert.");}
        if(request==92){byte[] bytes;try(InputStream in=getContentResolver().openInputStream(data.getData());ByteArrayOutputStream out=new ByteArrayOutputStream()){if(in==null)throw new IOException("Datei nicht erreichbar.");byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1){out.write(b,0,n);if(out.size()>8*1024*1024)throw new IOException("Spielstand zu groß.");}bytes=out.toByteArray();}new AlertDialog.Builder(this).setTitle("Datei als Spielstand laden?").setMessage("Der aktuelle Stand wird vorher gesichert. Danach treten Spieler mit ihrem gespeicherten Namen bei.").setPositiveButton("Laden",(d,w)->{try{saveNamed("Vor_Import");engine.loadGame(bytes);afterGameChange();}catch(IOException e){toast("Datei ungültig. Aktuelle Partie bleibt erhalten.");}}).setNegativeButton("Zurück",null).show();}
    }catch(IOException e){toast("Datei konnte nicht verarbeitet werden: "+e.getMessage());}}
    private void dungeonDialog(){java.util.List<String[]> list=engine.dungeonStates();if(list.isEmpty()){toast("Kein Kerker im Aufbau.");return;}String[] labels=new String[list.size()];for(int i=0;i<labels.length;i++)labels[i]=list.get(i)[2];new AlertDialog.Builder(this).setTitle("Kerkerzustand ändern").setItems(labels,(d,i)->{try{engine.toggleDungeon(Integer.parseInt(list.get(i)[0]),Integer.parseInt(list.get(i)[1]));updatePortal();}catch(Exception e){toast(e.getMessage());}}).setNegativeButton("Zurück",null).show();}
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
    private void wifiOptions(){
        if(hotspotPending){toast("Das WLAN wird gerade gestartet.");return;}
        String message="Einfacher Zugang für deine Runde:\n\nWLAN: "+ConnectionLinks.SSID+"\nPasswort: "+ConnectionLinks.PASSWORD+"\n\nFür dieses Passwort einmal in Android einrichten. Automatisches Starten nutzt dagegen die Zugangsdaten von Android.";
        new AlertDialog.Builder(this).setTitle("Spiel-WLAN").setMessage(message)
            .setPositiveButton("So einrichten",(d,w)->openWifiSettings())
            .setNeutralButton("Automatisch starten",(d,w)->startAutomaticHotspot())
            .setNegativeButton("Abbrechen",null).show();
    }
    private void openWifiSettings(){
        if(reservation!=null){reservation.close();reservation=null;ssid="";password="";}
        prefs.edit().putBoolean("setupPending",true).apply();
        String[] actions={"com.android.settings.WIFI_TETHER_SETTINGS","android.settings.TETHER_SETTINGS",Settings.ACTION_WIRELESS_SETTINGS,Settings.ACTION_SETTINGS};
        for(String action:actions)try{startActivity(new Intent(action));return;}catch(ActivityNotFoundException|SecurityException unavailable){}
        prefs.edit().putBoolean("setupPending",false).apply();toast("Bitte Android-Einstellungen und den mobilen Hotspot öffnen.");
    }
    private void confirmManualWifi(){
        if(confirming)return;confirming=true;
        LinearLayout content=padded();content.addView(text("In Android den mobilen Hotspot einschalten und diese Daten einstellen:",16,WHITE));
        EditText name=new EditText(this);name.setSingleLine(true);name.setHint("WLAN-Name");name.setText(prefs.getString("ssid",ConnectionLinks.SSID));content.addView(name);
        EditText pass=new EditText(this);pass.setSingleLine(true);pass.setHint("Passwort");pass.setText(prefs.getString("password",ConnectionLinks.PASSWORD));content.addView(pass);
        content.addView(text("Wenn du andere Daten eingestellt hast, trage sie hier ein. Danach zurück zur Lobby.",13,MUTED));
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Hotspot bereit?").setView(content).setPositiveButton("Ist eingerichtet",null)
            .setNeutralButton("Automatisch starten",(d,w)->{prefs.edit().putBoolean("setupPending",false).apply();startAutomaticHotspot();})
            .setNegativeButton("Später",(d,w)->prefs.edit().putBoolean("setupPending",false).apply()).create();
        dialog.setOnDismissListener(d->confirming=false);dialog.setOnShowListener(d->dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
            String n=name.getText().toString().trim(),p=pass.getText().toString();if(n.isEmpty()||p.length()<8||p.length()>63){toast("WLAN-Name und Passwort mit 8–63 Zeichen eingeben.");return;}
            ssid=n;password=p;prefs.edit().putString("ssid",n).putString("password",p).putBoolean("manualConfirmed",true).putBoolean("setupPending",false).apply();dialog.dismiss();updatePortal();
        }));dialog.show();
    }
    private void startAutomaticHotspot(){
        if(reservation!=null){toast("Der automatische Hotspot läuft bereits.");return;}if(hotspotPending)return;
        String permission=Build.VERSION.SDK_INT>=33?Manifest.permission.NEARBY_WIFI_DEVICES:Manifest.permission.ACCESS_FINE_LOCATION;
        if(checkSelfPermission(permission)!=PackageManager.PERMISSION_GRANTED){requestPermissions(Build.VERSION.SDK_INT>=33?new String[]{permission}:new String[]{permission,Manifest.permission.ACCESS_COARSE_LOCATION},7);return;}
        hotspotPending=true;
        WifiManager.LocalOnlyHotspotCallback callback=new WifiManager.LocalOnlyHotspotCallback(){
            @Override public void onStarted(WifiManager.LocalOnlyHotspotReservation r){hotspotPending=false;if(destroyed){r.close();return;}reservation=r;prefs.edit().putBoolean("manualConfirmed",false).putBoolean("setupPending",false).apply();
                if(Build.VERSION.SDK_INT>=30){ssid=clean(r.getSoftApConfiguration().getSsid());password=clean(r.getSoftApConfiguration().getPassphrase());}
                else{ssid=clean(r.getWifiConfiguration().SSID);password=clean(r.getWifiConfiguration().preSharedKey);}updatePortal();}
            @Override public void onStopped(){hotspotPending=false;reservation=null;ssid="";password="";updatePortal();}
            @Override public void onFailed(int reason){hotspotPending=false;toast("WLAN konnte nicht gestartet werden. Bitte Android-Hotspot einrichten.");updatePortal();}
        };
        try{
            WifiManager wifi=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            wifi.startLocalOnlyHotspot(callback,handler);
        }catch(SecurityException|IllegalStateException|UnsupportedOperationException e){hotspotPending=false;toast("Bitte den Android-Hotspot über WLAN einrichten aktivieren.");updatePortal();}
    }
    private String clean(String value){if(value==null)return "";return value.length()>1&&value.startsWith("\"")&&value.endsWith("\"")?value.substring(1,value.length()-1):value;}
    @Override public void onRequestPermissionsResult(int request,String[] permissions,int[] results){super.onRequestPermissionsResult(request,permissions,results);if(request==7){String p=Build.VERSION.SDK_INT>=33?Manifest.permission.NEARBY_WIFI_DEVICES:Manifest.permission.ACCESS_FINE_LOCATION;if(checkSelfPermission(p)==PackageManager.PERMISSION_GRANTED)startAutomaticHotspot();else toast("Bitte den Android-Hotspot verwenden.");}}
    private void showQr(boolean wifi){
        String value;
        try{value=wifi?ConnectionLinks.wifiQr(ssid,password):ConnectionLinks.site(network.address,server.port());}
        catch(IllegalArgumentException error){toast("Zuerst das WLAN einrichten und auf die Adresse warten.");return;}
        try{
            Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            BitMatrix bits=new QRCodeWriter().encode(value,BarcodeFormat.QR_CODE,720,720,hints);int[] pixels=new int[720*720];for(int y=0;y<720;y++)for(int x=0;x<720;x++)pixels[y*720+x]=bits.get(x,y)?Color.BLACK:Color.WHITE;
            Bitmap bitmap=Bitmap.createBitmap(pixels,720,720,Bitmap.Config.ARGB_8888);ImageView image=new ImageView(this);image.setImageBitmap(bitmap);image.setAdjustViewBounds(true);image.setPadding(dp(14),dp(14),dp(14),dp(14));image.setContentDescription(wifi?"QR-Code zum Spiel-WLAN":"QR-Code zur Spielseite mit ausgefülltem Portalcode");
            new AlertDialog.Builder(this).setTitle(wifi?"1 · Mit dem WLAN verbinden":"2 · Spielseite öffnen").setView(image).setMessage(wifi?"Mit der Kamera des Spielerhandys scannen.":"Zuerst mit dem WLAN verbinden, dann scannen. Der Portalcode wird ausgefüllt.").setPositiveButton("Fertig",null).show();
        }catch(WriterException error){toast("QR-Code konnte nicht erstellt werden.");}
    }
    private void toast(String message){Toast.makeText(this,message,Toast.LENGTH_LONG).show();}
    @Override protected void onStart(){super.onStart();if(noticeScreen!=null&&!activeNotice.isEmpty()){handler.removeCallbacks(finishNotice);handler.postDelayed(finishNotice,3000);}if(engine!=null&&playerRows!=null)handler.post(refresh);}
    @Override protected void onResume(){super.onResume();if(prefs!=null&&prefs.getBoolean("setupPending",false)&&playerRows!=null)handler.post(this::confirmManualWifi);}
    @Override protected void onStop(){handler.removeCallbacks(refresh);handler.removeCallbacks(finishNotice);super.onStop();}
    @Override protected void onDestroy(){destroyed=true;handler.removeCallbacks(refresh);handler.removeCallbacks(finishNotice);if(reservation!=null)reservation.close();if(network!=null)network.close();if(server!=null)server.close();super.onDestroy();}
    private static final class AndroidStore implements GameEngine.Store{
        private final AtomicFile file;AndroidStore(File path){file=new AtomicFile(path);}
        @Override public byte[] load()throws IOException{try{return file.readFully();}catch(FileNotFoundException missing){return null;}}
        @Override public void save(byte[] data)throws IOException{FileOutputStream out=file.startWrite();try{out.write(data);file.finishWrite(out);}catch(IOException error){file.failWrite(out);throw error;}}
    }
}
