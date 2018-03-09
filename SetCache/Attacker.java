import java.util.*;
import java.io.*;
import java.text.*;
import java.util.Arrays;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Paths;

class Attacker {

  /*Initalises the cache */
  SetCache sc = new SetCache();


  /*Store all plaintext bytes */
  ArrayList<Integer> plainarray = new ArrayList<Integer>();
  /*Stores key candidates for 1 plaintextbyte */
  int[] setsforevict = new int[256];
  /*To record the start and end of plaintextbyte array */
  int start = 0;
  int end = 16;
  /*This is to list the results from attack */
  ArrayList<Integer> list = new ArrayList<Integer>();


  /*This is from the input and displays which plaintext bytes are being considered for the attack */
  int inputnumber = 0;

  /*Address stream from file */
  ArrayList<String> addressstream = new ArrayList<String>();

/*To begin the attack and initalise the cache */
 void begin(ArrayList<Integer> plaintext, int numSet, int numBlock, int input, int setasoc){
     plainarray.addAll(plaintext);
     /*To initalise cache with size dimensions */
     sc.initalisecache(numSet, numBlock, setasoc);
     inputnumber = input;
     readfile();
  }

/*From the outputted file after the filetrimmer getting the address stream */
  void readfile(){
    ArrayList<String> listoffiles = new ArrayList<String>();
    File dir = new File("/home/");
    File[] files = dir.listFiles();
    Arrays.sort(files); /*list.files() does not save files in particular order so this function sorts them numerically */
    for (File filename : files) {
      /*Output from FileTrimmer were hex files */
      if(filename.getName().startsWith("hex")){
         try{
             FileReader inputFile = new FileReader(filename);
             BufferedReader bufferReader = new BufferedReader(new FileReader(filename));
             String line;
             while ((line = bufferReader.readLine()) != null){
                     addressstream.add(line);
             }
        bufferReader.close(); // Closes the input stream
      }
      catch(IOException e){
                          e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
                          System.exit(0);
       }
       /*Time outputs were added in to time output and check the program isn't stuck in a loop */
      System.out.println("Now it will begin to run the simulation for " + filename);
      String a = "Time taken to run attack for one encryption beg " + new SimpleDateFormat("HH.mm.ss.SS.").format(new Date());
      System.out.println(a);
      beginsimulation();
      String b = "Time taken to run attack for one encryption end " + new SimpleDateFormat("HH.mm.ss.SS.").format(new Date());
      System.out.println(b);
      System.out.println("I am about to clear addressstream");
    }
   addressstream.clear();
   }
 }

  void beginsimulation(){
       int loopnum = 0;
       while(start<end){
           /*The loop will only output files if specified by the user as input number */
           if(inputnumber > loopnum){
           timewithoutevict(); /*This is for if the user wants to know a standard time without evict */
       	   caculateevictsets(start);
           loopnum++; /*Loop number is how Attacker output is incremeneted as well so incremented before output */
           runattack(loopnum);
           }
           else{
            loopnum++;
           }
           start++;
      }
     /*This ensures that it will count the next number of plaintext bytes, all should be a sequence of 16 */
     end += 16;
  }

  void timewithoutevict(){
  	runaddressstream();
  	runaddressstream();
  	list.add(sc.return_hit());
    list.add(sc.return_miss());
    /*To add in Key, Plaintext Byte and Set */
    list.add(0);
    list.add(0);
    list.add(0);
    flushthecache();
  }

/*This gathers candidate keys */
  void caculateevictsets(int plainnum){
       Long temp;
       Long l;
       for(int i = 0; i<256; i++){
           temp = Long.valueOf(plainarray.get(plainnum) ^ i);
           setsforevict[i] = return_set(temp);
       }
  }

  int return_set(Long address){
      address += 0x6041a0; /*This is t-table look up for hexadecimal addresses */
      int intresult = sc.return_set(address);
      return intresult; /*To get set of cand key XOR plaintext + T-table address */
   }

/*Simply evicts the set from cache */
  void evictset(int set){
  	   sc.evictset(set);
  }


/*This tells the cache to run the address stream */
  void runaddressstream(){
  	   sc.execute_encryption(addressstream);
  }

/*This removes all data from the cache */
  void flushthecache(){
  	   sc.flushcache();
  }


/*This gathers the hit and misses from the file */
  void runattack(int loopnum){
    /* 256 candidate keys from value 0-255 */
  	for(int x = 0; x<256; x++){
  		 runaddressstream();
  		 evictset(setsforevict[x]);
  		 runaddressstream();
       list.add(sc.return_hit());
       list.add(sc.return_miss());
       list.add(x);
       list.add(plainarray.get(start));
       list.add(setsforevict[x]);
       flushthecache();
  	}
  	outputfile(loopnum);
  }

/*Outputs the Attacker's data from the attack */
 void outputfile(int num){
       if(num > 16){
         System.err.println("The loopnum has gone above 16");
         System.exit(0);
       }
       int line = 0;
       String fileDir = "/home/";
       String fileName = "result" + new SimpleDateFormat("yyyy.MM.dd.HHmmss.SS!").format(new Date()) + num;
       File f = new File (fileDir,fileName);
       try{
           PrintWriter writer = new PrintWriter(f);
           for(int value: list){
                 line++;
                 String printed = Integer.toString(value);
                 /* H = HIT, M = MISS, K = KEY, P = PLAINTEXT, S = SET */
                 if(line == 1){
                    printed = "H " + printed;
                 }
                 else if(line == 2){
                         printed = "M " + printed;
                 }
                 else if(line == 3){
                         printed = "K " + printed;
                 }
                 else if(line == 4){
                         printed = "P " + printed;
                 }
                 else{
                      printed = "S " + printed;
                      line = 0;
                 }
          writer.write(printed);
          writer.println();
          }
       writer.close();
       }
       catch(IOException e){
            e.printStackTrace();
            System.exit(0);
       }
      list.clear();
  }
}
