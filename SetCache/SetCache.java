import java.util.*;
import java.io.*;
import java.lang.Math.*;

class SetCache{

  /* numset represents the number of sets whilst numblock is the amount of blocks per set */
  int numset;
  int numblock;

  /*Sum of the number of sets and cache blocks per set */
  int sumset;

  /*set associativity number */
  int setassoc;

  /*to record the hit and misses the simulation accumulate */
  int hit;
  int necessarymiss;
  int capacitymiss;

  /*The arraylist representing the memory cache */
  ArrayList<ArrayList<Integer>> memcache = new ArrayList<ArrayList<Integer>>();

  /*To build the cache based on inputs from the user of the power of based on user input */
  void initalisecache(int setnum, int numblocks, int setasoc){
   /*To initalise the attacker, the 'power of' inputs are used in getting set and tag from the address */
   numset = setnum;
   numblock = numblocks;
   setassoc = setasoc;
   sumset = (int) Math.pow(2, setnum);
   int sumblock = (int) Math.pow(2, numblocks);
   int memsize = (sumset*sumblock) * setassoc;
   System.out.println("This is the total number of bytes in the cache");
   System.out.println(memsize);
   for(int i = 0; i <sumset; i++){
    memcache.add(new ArrayList<Integer>());
   }
    for(int i = 0; i<sumset; i++){
     for(int x = 0; x<setassoc; x++){
            memcache.get(i).add(x, null);
     }
    }
  }


/*This reads in the address stream from the attacker */
  void execute_encryption(ArrayList<String> addressstream){
  	  hit = 0;
      necessarymiss = 0;
      capacitymiss = 0;
      int tag = 0;
      Long num = 0l;
      int set = 0;
      for(int x = 0; x < addressstream.size(); x++){
         num = Long.parseLong(addressstream.get(x), 16);
         set = return_set(num);
         if(set > sumset){
           System.out.println("The set inputted is greater than the sets available");
           System.exit(0);
         }
      assert(set <= sumset);
      tag = return_tag(num);
      hitandmiss(tag, set);
     }
    /*This is to ensure that hits and misses are equal to the address size for testing */
    if((hit + necessarymiss + capacitymiss) != addressstream.size()){
       System.out.println("There is too few or many hits and misses for this address stream of " + addressstream.size());
       System.out.println("This is number of hits " + hit + " " + "Number of necessary misses " + necessarymiss + " " + "Number of capacity misses " + capacitymiss);

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
    int miss = necessarymiss + capacitymiss;
  	return miss;
  }

  void evictset(int set){
  	   if(set > sumset){
  	   	System.err.println("set being evicted is greater than the sumset");
        System.exit(0);
  	   }
       assert(set <= sumset);
       /*This deletes all blocks in the set */
       for(int i = 0; i<setassoc; i++){
       	   memcache.get(set).set(i, null);
       }
  }

   void hitandmiss(int tag, int set){
      /*This checks whether there is a space not filled yet or if the tag is present */
      Boolean capacity = true;
      capacity = checkhitmiss(tag, set);
      /*This means the tag is not in the set and is added at the back MRU */
      if(capacity){
        assert(capacity == true);
        memcache.get(set).remove(0);
        memcache.get(set).add(setassoc-1, tag);
        /*The capacity miss incurs when there are more addresses than available */
        capacitymiss++;
      }
   }


   boolean checkhitmiss(int tag, int set){
     boolean capacity = true;
     int y;
     /*This checks that the address is in the set, MRU policy */
     if(memcache.get(set).contains(tag) == true){
       y = memcache.get(set).indexOf(tag);
       reverseorder(y, set, tag);
       hit++;
       capacity = false;
     }
     else{
         for(int x = 0; x < setassoc; x++){
          if(memcache.get(set).get(x) == null){
                memcache.get(set).set(x, tag);
      /*This is a necessary miss as the cache has an empty block */
                necessarymiss++;
                capacity = false;
                break;
           }
         }
      }
    return capacity;
  }

/*This returns an int that tells where the set should be located */
   int return_set(Long address){
      Long result = (address >> numblock) & ((1 << numset)-1);
      int intresult = result.intValue();
      return intresult;
   }

/*The tag is the reference of the address and unique to the address */
   int return_tag(Long address){
     Long result = address >> (numset + numblock);
     int intresult = result.intValue();
     return intresult;
   }

/*To ensure that MRU is placed at the back of the queue */
  void reverseorder(int position, int set, int tag){
    memcache.get(set).remove(position);
    memcache.get(set).add(setassoc-1, tag);
    if(memcache.get(set).size() > setassoc){
       System.out.println("Size of the memory cache set is greater than the blocks allocated to it");
       System.exit(0);
    }
    assert(memcache.get(set).size() <= setassoc);
  }

/*To flush the cache of any previous data */
   void flushcache(){
       for(int i = 0; i<sumset; i++){
        for(int x = 0; x<setassoc; x++){
            memcache.get(i).set(x, null);
        }
       }
   }
}
