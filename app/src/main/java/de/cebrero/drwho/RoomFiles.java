package de.cebrero.drwho;

import android.util.AtomicFile;
import de.cebrero.drwho.core.RoomLibrary;
import java.io.*;
import java.util.*;

/** Private, atomic, reusable layout files; display names never become paths. */
final class RoomFiles implements RoomLibrary.Store {
    private final File directory;
    RoomFiles(File directory){this.directory=directory;}
    private void prepare()throws IOException{if(!directory.isDirectory()&&!directory.mkdirs())throw new IOException("Raumspeicher nicht erreichbar.");}
    public Map<String,byte[]> loadAll()throws IOException{
        prepare();File[] files=directory.listFiles();if(files==null)throw new IOException("Räume konnten nicht gelesen werden.");
        Map<String,byte[]> result=new LinkedHashMap<>();
        for(File file:files){String name=file.getName();if(!name.matches("[a-f0-9-]{36}\\.drwroom(?:\\.bak)?"))continue;
            String id=name.substring(0,36);if(result.containsKey(id))continue;File base=new File(directory,id+".drwroom");
            try{if(file.length()>128*1024)throw new IOException("Raumdatei zu groß.");result.put(id,new AtomicFile(base).readFully());}
            catch(IOException error){result.put(id,new byte[0]);}
        }
        return result;
    }
    public void save(String id,byte[] bytes)throws IOException{
        if(!id.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"))throw new IOException("Ungültige Raum-ID.");
        prepare();AtomicFile file=new AtomicFile(new File(directory,id+".drwroom"));FileOutputStream out=file.startWrite();
        try{out.write(bytes);file.finishWrite(out);}catch(IOException error){file.failWrite(out);throw error;}
    }
}
