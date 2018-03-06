import java.util.*;
import java.io.*;
import java.text.*;
import java.util.Arrays;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Paths;

class Attacker {

  /*Initalises the cache */
  SecureCache sc = new SecureCache();


  /*Store all plaintext bytes */
  ArrayList<Integer> plainarray = new ArrayList<Integer>();
  /*Stores key candidates for 1 plaintextbyte */
  int[] setsforevict = new int[256];
  /*To record the start and end of plaintextbyte array */
  int start = 0;
  int end = 16;

  /*Store results from attack */
  ArrayList<Integer> list = new ArrayList<Integer>();

  /*This is from the input and displays which plaintext bytes are being considered for the attack */
  int inputnumber = 0;

  /*Address stream from file */
  ArrayList<String> addressstream = new ArrayList<String>();

/*To begin the attack and initalise the cache */
void begin(ArrayList<Integer> plaintext, int numSet, int numBlock, int input, int setasoc){
     plainarray.addAll(plaintext);
     sc.initalisecache(numSet, numBlock, setasoc);
     inputnumber = input;
     readfile();
}

/*From the outputted file after the filetrimmer getting the address stream */
void readfile(){
    ArrayList<String> listoffiles = new ArrayList<String>();
    File dir = new File("/home...");
    File[] files = dir.listFiles();
    Arrays.sort(files); /*list.files() does not save files in particular order so this function sorts them numerically */
    for (File filename : files){
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
           if(inputnumber > loopnum){
             String c = "Time taken for one address read beg " + new SimpleDateFormat("HH.mm.ss.SS.").format(new Date());
             System.out.println(c);
             timewithoutevict();
       	     caculateevictsets(start);
             loopnum++; /*Loop number is how file output is incremeneted as well so incremented before output */
             runattack(loopnum);
             String d = "Time taken for one address read end " + new SimpleDateFormat("HH.mm.ss.SS.").format(new Date());
             System.out.println(d);
           }
           start++;
       }
           /*This ensures that it will count the next number of plaintext bytes, all should be a sequence of 16 */
      end += 16;
}

void timewithoutevict(){
  	    runaddressstream();
  	    runaddressstreamsecond();
        list.add(sc.return_hit());
        list.add(sc.return_miss());
        /* This is to add space for Key, Plaintext Byte and Set */
        list.add(0);
        list.add(0);
        list.add(0);
        flushthecache();
}

/*This gathers candidate keys */
void caculateevictsets(int plainnum){
       int temp;
       Long l;
       for(int i = 0; i<256; i++){
           temp = plainarray.get(plainnum) ^ i;
           l = Long.valueOf(temp);
           setsforevict[i] = return_set(l);
       }
}

int return_set(Long address){
      address += 0x6041a0; /*This is t-table look up for hexadecimal addresses */
      int intresult = sc.rmtrand(address);
      return intresult;
}

void evictset(int set){
  	   sc.evictset(set);
}

/*This tells the cache to run the address stream */
void runaddressstream(){
    /*Secure cache runs this address stream to caculate protected sets */
  	   sc.execute_encryption_firstround(addressstream);
}

void runaddressstreamsecond(){
      /*This is a normal address run without calculation of protected sets */
       sc.execute_encryption_secondround(addressstream);
}

/*This removes all data from the cache */
void flushthecache(){
  	   sc.flushcache();
}

/*This gathers the hit and misses from the file */
void runattack(int loopnum){
       for(int x = 0; x<256; x++){
          runaddressstreamsecond();
          evictset(setsforevict[x]);
          runaddressstreamsecond();
          list.add(sc.return_hit());
          list.add(sc.return_miss());
          list.add(x);
          list.add(plainarray.get(start));
          list.add(setsforevict[x]);
          flushthecache();
       }
       outputfile(loopnum);
}


 void outputfile(int num){
       /*You also need to add the plaintext value into results */
       String printed = "";
       if(num > 16){
          System.err.println("The loopnum has gone above 16");
          System.exit(0);
       }
       int line = 0;
       String fileDir = "/home/tf75/Documents/Master's project/Securecache/output";
       String fileName = "result" + new SimpleDateFormat("yyyy.MM.dd.HHmmss.SS!").format(new Date()) + num;
       File f = new File (fileDir,fileName);
       try{
           PrintWriter writer = new PrintWriter(f);
           for(int value: list){
               line++;
               printed = Integer.toString(value);
               /*H = HITS, M = MISS, K = KEY, P = PLAINTEXT, S = SET */
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
