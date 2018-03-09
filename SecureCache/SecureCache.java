import java.util.*;
import java.io.*;
import java.lang.Math.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class SecureCache{

  /* numset represents the number of sets whilst numblock is the amount of blocks per set */
  int numset;
  int numblock;


  /*Sum of the number of sets and cache blocks per set */
  int sumset;

  /*Number for the set-way associative */
  int setassoc;

 /*to record the hit and misses the simulation accumulate */
  int hit;
  int necessarymiss;
  int capacitymiss;

  /*To check if protectsets have been set */
  boolean protectsetscomplete;

  /*The map to find protected caches */
  Map<Integer, Integer> map = new HashMap<Integer, Integer>();

  /*The arraylist representing the memory cache */
  ArrayList<ArrayList<Integer>> memcache = new ArrayList<ArrayList<Integer>>();

  /*This is the ReMapping Table - this is size of memory cache of all possible blocks */
  Integer[][] rmt;

  /*Array to for the protected set numbers */
  ArrayList<Integer> protectedsets = new ArrayList<Integer>();
  boolean protect = false;

  /*To build the cache based on inputs from the user of the power of based on user input */
  void initalisecache(int setnum, int numblocks, int setasoc){
    /*To initalise the attacker, the 'power of' inputs are used in getting set and tag from the address */
    numset = setnum;
    numblock = numblocks;
    setassoc = setasoc;
    sumset = (int) Math.pow(2, setnum);
    rmt = new Integer[sumset*setassoc][2];
    arraynull();
    for(int i = 0; i <sumset; i++){
    memcache.add(new ArrayList<Integer>());
    }
     for(int i = 0; i<sumset; i++){
      for(int x = 0; x<setassoc; x++){
             memcache.get(i).add(x, null);
      }
     }
     /*As if the set size is too small the protected set is easier to identify */
    if(sumset>= 32){
      protect = true;
    }
  }

/*Makes RMT all null */
  void arraynull(){
    for(int y = 0; y<rmt.length; y++){
       rmt[y][0] = null;
       rmt[y][1] = null;
    }
  }

 /*This encryption is necessary to find protected sets */
  void execute_encryption_firstround(ArrayList<String> addressstream){
  	  protectsetscomplete = false;
      hit = 0;
      necessarymiss = 0;
      capacitymiss = 0;
      Integer temp;
      for(int x = 0; x < addressstream.size(); x++){
        String address = addressstream.get(x);
        Long num = Long.parseLong(address, 16);
        int tag = return_tag(num);
        /*This checks tag is not in RMT */
        int position = checkrmt(tag);
        if(position >= 0){
           reverseorder(position, tag);
           hit++;
        }
        else{
          /*Fully Associative method of assigning tags */
            int randset = rmtrand(num);
            if(randset > sumset || randset < 0){
              System.out.println("The hashset inputted is greater or less than the sets available " + randset);
              System.exit(0);
            }
            assert(randset <= sumset);
            miss(randset, tag);
        }
      }
      if(protect){
        /*To protect top five most accessed sets or blocks */
      	 sortByValues();
   }
  }

/* This execution of encryption without taking note of protected sets */
  void execute_encryption_secondround(ArrayList<String> addressstream){
      hit = 0;
      necessarymiss = 0;
      capacitymiss = 0;
      Integer temp;
      for(int x = 0; x < addressstream.size(); x++){
        String address = addressstream.get(x);
        Long num = Long.parseLong(address, 16);
        int tag = return_tag(num);
        int position = checkrmt(tag);
         if(position >= 0){
           reverseorder(position, tag);
           hit++;
         }
         else{
            int randset = rmtrand(num);
            if(randset > sumset){
             System.out.println("The hashset inputted is greater than the sets available");
             System.exit(0);
            }
           assert(randset <= sumset);
           /*This takes account that there may be protected sets in place */
           misssecondround(randset, tag);
         }
      }
      if((hit + necessarymiss + capacitymiss) != addressstream.size()){
        System.out.println("There is too few or many hits and misses for this address stream");
        System.out.println("THis is the address size " + addressstream.size());
        System.out.println((hit + necessarymiss + capacitymiss));
        System.exit(0);
      }
      assert((hit + necessarymiss + capacitymiss) == addressstream.size());
  }

  /*To return the amount of hits per addresss stream read */
  int return_hit(){
   return hit;
  }

  /*To return the amount of misses per address stream read */
  int return_miss(){
    int miss = capacitymiss + necessarymiss;
    return miss;
  }

  /*A hashing method using the tag */
  int rmtrand(Long address){
    Long newset = address%sumset;
    int resultset = newset.intValue();
    if(resultset < 0){
    	resultset = 0;
    }
    return resultset;
  }

/*This checks whether the tag is in the RMT */
  int checkrmt(int tag){
    for(int i = 0; i<rmt.length; i++){
        if(rmt[i][0] != null){
         if(rmt[i][0] == tag){
            if(protectsetscomplete == false && protect == true){
             /*Adding to map to caculate later which were most accessed sets */
             Integer value = map.get(rmt[i][1]);
             /*If the value is already present in the map */
             if (value != null) {
               map.put(rmt[i][1], map.get(rmt[i][1]) + 1);
             }
             /*Add the value into the map */
             else{
                map.put(rmt[i][1], 1);
             }
            }
          return rmt[i][1];
         }
        }
    }
    return -1;
  }

   Integer return_tag(Long address){
     Long result = address >> (numset + numblock);
     Integer intresult = result.intValue();
     return intresult;
   }

  void miss(int set, int tag){
      /*This checks whether there is free space in the cache */
      Boolean space = false;
      for(int x = 0; x < setassoc; x++){
           if(memcache.get(set).get(x) == null){
            memcache.get(set).set(x, tag);
            updatetable(set, tag);
            necessarymiss++;
            space = true;
            break;
          }
      }
      /*If there is no memory left in the block */
      if(space == false){
          int temp = getrandtag();
          /*Either a value is in there thus RMT needs updating or there is a new addition to the RMT */
          if(memcache.get(temp).get(0) != null){
             Integer temptag = memcache.get(temp).get(0);
             memcache.get(temp).remove(0);
             memcache.get(temp).add(setassoc-1, tag);
             updatetablefrommiss2(temp, tag, temptag);
          }
          else{
              memcache.get(temp).set(0, tag);
              updatetable(temp, tag);
          }
          capacitymiss++;
         }
  }

  /*When the protected sets have be selected (if over 32 sets)*/
  void misssecondround(int set, int tag){
  	 /*This checks whether there is free space in the cache */
      Boolean space = false;
      Integer temp;
      for(int x = 0; x < setassoc; x++){
           if(memcache.get(set).get(x) == null){
            memcache.get(set).set(x, tag);
            updatetable(set, tag);
            necessarymiss++;
            space = true;
            break;
          }
      }
      /*If there is no memory left in the block */
      if(space == false){
          if(protect == true){
          /*To ensure protected sets are not overwritten */
      	   temp = getprotectrandtag();
      	  }
      	  else{
          temp = getrandtag();
          }
          /*Either a value is in there thus RMT needs updating or there is a new addition to the RMT */
          if(memcache.get(temp).get(0) != null){
             Integer temptag = memcache.get(temp).get(0);
             memcache.get(temp).remove(0);
             memcache.get(temp).add(setassoc-1, tag);
             updatetablefrommiss2(temp, tag, temptag);
          }
          else{
              memcache.get(temp).set(0, tag);
              updatetable(temp, tag);
          }
          capacitymiss++;
         }
  }

  Integer getrandtag(){
  	/*To return a set number */
    Integer temp;
    temp = ThreadLocalRandom.current().nextInt(0, sumset);
    return temp;
  }

/*This has an additional check to not replace protected sets */
  Integer getprotectrandtag(){
    Integer randNum;
    do {
        randNum = ThreadLocalRandom.current().nextInt(0, sumset);
        } while (!protectedsets.contains(randNum));
    return randNum;
  }

/*Implementation of the MRU replacement policy */
  void reverseorder(int set, int tag){
    int y;
     if(memcache.get(set).size() > setassoc){
          System.out.println("Size of the memory cache set is greater than the blocks allocated to it");
          System.exit(0);
     }
     if(memcache.get(set).contains(tag) == true){
       y = memcache.get(set).indexOf(tag);
       memcache.get(set).remove(y);
       memcache.get(set).add(setassoc-1, tag);
     }
  }


/*To update the table if there is space left in the cache */
  void updatetable(int set, int tag){
       boolean found = false;
       for(int i = 0; i<rmt.length; i++){
           if(rmt[i][0] == null){
              rmt[i][0] = tag;
              rmt[i][1] = set;
              found = true;
              break;
           }
       }
       if(found == false){
        System.out.println("There is an error as there is no space left in the RMT");
         System.exit(0);
        }
  }

/*Update the table in the case of a conflict in the cache */
  void updatetablefrommiss2(int set, int tag, int temptag){
  	/*Search through table to find replaced tag and update */
      boolean replaced = false;
      for(int i = 0; i<rmt.length; i++){
      	 if(rmt[i][0] != null){
          if(rmt[i][0].equals(temptag)){
              rmt[i][0] = tag;
              rmt[i][1] = set;
              replaced = true;
              break;
          }
         }
      }
      if(replaced == false){
         System.out.println("There is an error in replacing a value in the RMT");
         System.exit(0);
       }
  }

/*Used when evicting specific data */
   void removetagfromRMT(Integer tag){
   	  boolean foundtag = false;
      for(int i = 0; i<rmt.length; i++){
      	 if(rmt[i][0] != null){
          if(rmt[i][0].equals(tag)){
              rmt[i][0] = null;
              rmt[i][1] = null;
              foundtag = true;
              break;
          }
         }
      }
      if(foundtag == false){
         System.out.println("There is an error in finding the RMT value");
         System.exit(0);
       }
  }


/*Evict set */
   void evictset(int set){
       if(set > sumset){
        System.err.println("set being evicted is greater than the sumset");
        System.exit(0);
       }
        assert(set <= sumset);
        /*This deletes all blocks in the set */
        for(int i = 0; i<setassoc; i++){
           if(memcache.get(set).get(i) != null){
              removetagfromRMT(memcache.get(set).get(i));
              memcache.get(set).set(i, null);
          }
        }
   }

/* Finding the protected set numbers for the memcache */
  void sortByValues(){
    List<Integer> values = new ArrayList<Integer>(map.values());
    Collections.sort(values, Collections.reverseOrder());
    for(int i = 0; i<5; i++){
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
          if( values.get(i)== entry.getValue()){
                protectedsets.add(entry.getKey());
            }
        }
    }
     map.clear();
  }

  /*To flush the cache of any previous data */
   void flushcache(){
       for(int i = 0; i<sumset; i++){
        for(int x = 0; x<setassoc; x++){
            memcache.get(i).set(x, null);
        }
       }
        arraynull();
   }

}
