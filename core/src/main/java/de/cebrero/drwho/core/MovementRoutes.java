package de.cebrero.drwho.core;

import java.util.*;

/** Exact-length orthogonal walks. Revisiting fields retains the existing movement rule. */
final class MovementRoutes {
 private MovementRoutes(){}
 static String key(int x,int y){return x+","+y;}
 static List<int[]> neighbours(BoardPlan board,boolean legacy,int x,int y){
  if(!legacy)return board.neighbours(x,y);
  List<int[]> n=new ArrayList<>();for(int[] d:new int[][]{{0,-1},{1,0},{0,1},{-1,0}}){int a=x+d[0],b=y+d[1];if(a>=0&&a<6&&b>=0&&b<6)n.add(new int[]{a,b});}return n;
 }
 static Map<String,List<int[]>> routes(BoardPlan board,boolean legacy,int x,int y,int steps,Set<String> blocked,boolean terminalPortal){
  Map<String,List<int[]>> level=new LinkedHashMap<>(),result=new LinkedHashMap<>();level.put(key(x,y),List.of());
  for(int depth=1;depth<=steps;depth++){
   Map<String,List<int[]>> next=new LinkedHashMap<>();
   for(Map.Entry<String,List<int[]>> e:level.entrySet()){
    String[] at=e.getKey().split(",");for(int[] n:neighbours(board,legacy,Integer.parseInt(at[0]),Integer.parseInt(at[1]))){
     String k=key(n[0],n[1]);if(blocked.contains(k))continue;
     List<int[]> path=new ArrayList<>(e.getValue());path.add(n);
     if(terminalPortal&&!legacy&&board.isPortal(n[0],n[1])){result.putIfAbsent(k,path);continue;}
     next.putIfAbsent(k,path);if(depth==steps)result.putIfAbsent(k,path);
    }
   }level=next;
  }return result;
 }
}
