package Storer.impl;

import PCinfo.Systeminformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StoreManager{
    public static int unusedID=3;
    public static int unusedGroupID=0;
    public static Map<String, Integer> Process_to_GroupID_Map = new ConcurrentHashMap<>();
    public static Map<Integer, List<Integer>> IDs_to_GroupID_Map = new ConcurrentHashMap<>();
    public static Map<Integer, Storage> ID_to_last_Runnumber = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> GroupIDandSysteminformation_to_ID=null;


    public StoreManager(){ }

    public static synchronized int getUnused_ID(int GroupID) {
        int newID = ++unusedID;
        List<Integer> GroupID_IDList = IDs_to_GroupID_Map.get(GroupID);
        GroupID_IDList.add(newID);
        IDs_to_GroupID_Map.put(GroupID,GroupID_IDList);
        return newID;
    }
    public static synchronized int getUnused_GroupID(){
        int newGroup = ++unusedGroupID;
        IDs_to_GroupID_Map.put(newGroup,new ArrayList<Integer>());
        return newGroup;
    }
    public static int getGroupID(String Processname){
        if(!Process_to_GroupID_Map.containsKey(Processname)){
            int newGroupID = StoreManager.getUnused_GroupID();
            Process_to_GroupID_Map.put(Processname,newGroupID);
            IDs_to_GroupID_Map.put(newGroupID,new ArrayList<>());
            return newGroupID;
        }else{
            int GroupID=Process_to_GroupID_Map.get(Processname);
            return GroupID;
        }
    }
    public static int lookForID(int GroupID,Systeminformation sysinfo){
        int ID;
        //System.out.println("Hashcode = "+sysinfo.hashCode());
        if(GroupIDandSysteminformation_to_ID==null){
            System.out.println("Neue Hashmap wird generiert");
            GroupIDandSysteminformation_to_ID = new ConcurrentHashMap<>();
            ConcurrentHashMap<Integer,Integer> GroupMap = new ConcurrentHashMap<>();
            ID=StoreManager.getUnused_ID(GroupID);
            GroupMap.put(sysinfo.hashCode(),ID);
            GroupIDandSysteminformation_to_ID.put(GroupID,GroupMap);
        }else{
            if(GroupIDandSysteminformation_to_ID.containsKey(GroupID)){
                //System.out.println("GroupId gefunden");
                ConcurrentHashMap<Integer,Integer> GroupMap = GroupIDandSysteminformation_to_ID.get(GroupID);
                int Key = sysinfo.hashCode();
                //System.out.println("Key = "+Key);
                if(GroupMap.containsKey(Key)){
                    //System.out.println("SysInfo Hashcode enthalten");
                    ID=GroupMap.get(Key);
                }else{
                    //System.out.println("SysInfo Hashcode nicht enthalten");
                    ID=getUnused_ID(GroupID);
                    GroupMap.put(Key,ID);
                }

            }else{
                ConcurrentHashMap<Integer,Integer> GroupMap = new ConcurrentHashMap<>();
                ID=StoreManager.getUnused_ID(GroupID);
                int Key = sysinfo.hashCode();
                GroupMap.put(Key,ID);
                GroupIDandSysteminformation_to_ID.put(GroupID,GroupMap);
            }
        }
        return ID;
    }
    public static Storage lookForStorage(String processname, Systeminformation sysinfo){
        int GroupID = getGroupID(processname);
        int ID = lookForID(GroupID,sysinfo);
        if(ID_to_last_Runnumber.containsKey(ID)){
            Storage myStorage = ID_to_last_Runnumber.get(ID);
            myStorage.createNewRun();
            return myStorage;
        }else{
            Storage myStorage = new Storage(ID)
                    .setGroupid(GroupID)
                    .setSys(sysinfo)
                    .setProcessname(processname);
            ID_to_last_Runnumber.put(ID,myStorage);
            return myStorage;
        }
    }



}
