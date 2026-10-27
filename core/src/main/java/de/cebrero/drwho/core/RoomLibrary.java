package de.cebrero.drwho.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Reusable physical layouts. Never contains players, tokens, story, dice, bags or hidden keys. */
public final class RoomLibrary {
    private static final int MAGIC=0x4457524d,FORMAT=1,MAX_BYTES=128*1024;
    public interface Store {
        Map<String,byte[]> loadAll() throws IOException;
        void save(String id,byte[] bytes) throws IOException;
    }
    public static class MemoryStore implements Store {
        private final Map<String,byte[]> files=new LinkedHashMap<>();
        public Map<String,byte[]> loadAll(){Map<String,byte[]> copy=new LinkedHashMap<>();files.forEach((id,b)->copy.put(id,b.clone()));return copy;}
        public void save(String id,byte[] bytes)throws IOException{files.put(id,bytes.clone());}
    }
    private static final class Room {
        String id,name;long created;
        BoardPlan board;
    }
    private final Store store;
    private final Map<String,Room> rooms=new LinkedHashMap<>();
    private int unreadable;
    public RoomLibrary(Store store)throws IOException{
        this.store=Objects.requireNonNull(store);
        for(Map.Entry<String,byte[]> e:store.loadAll().entrySet())try{
            Room room=decode(e.getValue());if(!room.id.equals(e.getKey()))throw new IOException("Raum-ID stimmt nicht.");rooms.put(room.id,room);
        }catch(IOException|IllegalArgumentException invalid){unreadable++;} // Preserve unreadable originals on disk.
    }
    static boolean validId(String id){return id!=null&&id.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");}
    static String validName(String name){
        String clean=name==null?"":name.trim();
        if(clean.isEmpty()||clean.length()>80||clean.codePoints().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException("Raumname: 1–80 Zeichen ohne Zeilenumbrüche.");
        return clean;
    }
    /** A stable request-derived ID makes a lost successful response safe across host restarts. */
    synchronized boolean saved(String request,String name){
        Room room=rooms.get(UUID.nameUUIDFromBytes(request.getBytes(StandardCharsets.UTF_8)).toString());
        if(room==null)return false;if(!room.name.equals(validName(name)))throw new IllegalArgumentException("Diese Speicheranfrage wurde bereits verwendet.");return true;
    }
    synchronized void save(String request,String name,BoardPlan board,long created)throws IOException{
        name=validName(name);String id=UUID.nameUUIDFromBytes(request.getBytes(StandardCharsets.UTF_8)).toString();
        Room previous=rooms.get(id);if(previous!=null){if(!previous.name.equals(name))throw new IllegalArgumentException("Diese Speicheranfrage wurde bereits verwendet.");return;}
        for(Room room:rooms.values())if(room.name.equalsIgnoreCase(name))throw new IllegalArgumentException("Dieser Raumname ist schon vergeben. Bitte einen anderen Namen wählen.");
        if(board.cells()==0)throw new IllegalArgumentException("Zuerst mindestens eine Kachel platzieren.");
        Room room=new Room();room.id=id;room.name=name;room.created=created;room.board=board;
        byte[] bytes=encode(room);Room saved=decode(bytes);store.save(id,bytes);rooms.put(id,saved);
    }
    synchronized BoardPlan board(String id)throws IOException{return decode(encode(require(id))).board;}
    synchronized String name(String id){return require(id).name;}
    private Room require(String id){if(!validId(id)||!rooms.containsKey(id))throw new IllegalArgumentException("Gespeicherter Raum nicht gefunden.");return rooms.get(id);}
    synchronized String json(String defaultName){
        List<Room> list=new ArrayList<>(rooms.values());list.sort(Comparator.comparingLong((Room r)->r.created).reversed().thenComparing(r->r.id));
        StringBuilder b=new StringBuilder("{\"defaultName\":").append(GameEngine.quote(defaultName)).append(",\"unreadable\":").append(unreadable).append(",\"rooms\":[");
        for(int i=0;i<list.size();i++){Room r=list.get(i);if(i>0)b.append(',');b.append("{\"id\":").append(GameEngine.quote(r.id)).append(",\"name\":").append(GameEngine.quote(r.name)).append(",\"created\":").append(r.created).append(",\"tiles\":").append(r.board.tiles().size()).append(",\"fields\":").append(r.board.cells()).append('}');}
        return b.append("]}").toString();
    }
    private static byte[] encode(Room r)throws IOException{
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);
        out.writeInt(MAGIC);out.writeInt(FORMAT);out.writeUTF(r.id);out.writeUTF(r.name);out.writeLong(r.created);r.board.write(out);out.flush();
        if(bytes.size()>MAX_BYTES)throw new IOException("Gespeicherter Raum zu groß.");return bytes.toByteArray();
    }
    private static Room decode(byte[] bytes)throws IOException{
        if(bytes==null||bytes.length>MAX_BYTES)throw new IOException("Ungültige Raumdatei.");
        DataInputStream in=new DataInputStream(new ByteArrayInputStream(bytes));if(in.readInt()!=MAGIC||in.readInt()!=FORMAT)throw new IOException("Unbekanntes Raumformat.");
        Room r=new Room();r.id=in.readUTF();r.name=in.readUTF();r.created=in.readLong();r.board=BoardPlan.read(in,true);
        if(!validId(r.id)||!validName(r.name).equals(r.name)||r.created<0||r.board.cells()==0||in.available()!=0)throw new IOException("Beschädigte Raumdatei.");return r;
    }
}
