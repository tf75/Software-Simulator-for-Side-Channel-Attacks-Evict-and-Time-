import java.util.*;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

class Caculateresults{


     /*The 16 list files each outputting results for 0-15 of the plaintext values */
      ArrayList<File> file1 = new ArrayList<File>();
      ArrayList<File> file2 = new ArrayList<File>();
      ArrayList<File> file3 = new ArrayList<File>();
      ArrayList<File> file4 = new ArrayList<File>();
      ArrayList<File> file5 = new ArrayList<File>();
      ArrayList<File> file6 = new ArrayList<File>();
      ArrayList<File> file7 = new ArrayList<File>();
      ArrayList<File> file8 = new ArrayList<File>();
      ArrayList<File> file9 = new ArrayList<File>();
      ArrayList<File> file10 = new ArrayList<File>();
      ArrayList<File> file11 = new ArrayList<File>();
      ArrayList<File> file12 = new ArrayList<File>();
      ArrayList<File> file13 = new ArrayList<File>();
      ArrayList<File> file14 = new ArrayList<File>();
      ArrayList<File> file15 = new ArrayList<File>();
      ArrayList<File> file16 = new ArrayList<File>();

	 /*This caculates the results of the equations and is used to print off key values */
	 List<Integer> results = new ArrayList<Integer>();


	 int hit = 0; /*These are the int values use to caculate average execution time per candidate key */
	 int miss = 0;

	 int cmiss = 100; /*Cost of a miss */
	 int chit = 1;    /*Cost of a hit */
	 int offset = 1;

	 /*To label file output*/
	 int outputnum = 0;

	 /*Boolean for whether it prints off plaintext or candidate key results */
	 boolean plaintext = false;


	public static void main(String[] args){
	 Caculateresults result = new Caculateresults();
	 result.sortoutput();
	}

/*All output files from attacker are read in and processed according to the number following the ! */
	void sortoutput(){
	    System.out.println("It will separate files according to plaintext byte number");
      File dir = new File("/home/tf75/Documents/Master's project/Set Cache/output");
      File[] files = dir.listFiles();
      Arrays.sort(files); /*list.files() does not save files in particular order so this function sorts them numerically */
      for(File filename : files){
      	  String name = filename.getName();
      	  int position = name.indexOf( '!' );
      	  name = name.substring(position+1);
      	  switch(name){
      	  	case "1":
      	  	     file1.add(filename);
      	  	     break;
      	  	case "2":
      	  	     file2.add(filename);
      	  	     break;
      	  	case "3":
      	  	     file3.add(filename);
      	  	     break;
      	  	case "4":
      	  	     file4.add(filename);
      	  	     break;
      	  	case "5":
      	  	     file5.add(filename);
      	  	     break;
      	  	case "6":
      	  	     file6.add(filename);
      	  	     break;
      	  	case "7":
      	  	     file7.add(filename);
      	  	     break;
      	  	case  "8":
      	  	     file8.add(filename);
      	  	     break;
      	  	case "9":
      	  	     file9.add(filename);
      	  	     break;
      	  	case "10":
      	  	     file10.add(filename);
      	  	     break;
      	  	case "11":
      	  	     file11.add(filename);
      	  	     break;
      	  	case "12":
      	  	     file12.add(filename);
      	  	     break;
      	  	case "13":
      	  	     file13.add(filename);
      	  	     break;
      	  	case "14":
      	  	     file14.add(filename);
      	  	     break;
      	  	case  "15":
      	  	     file15.add(filename);
      	  	     break;
      	  	case  "16":
      	  	     file16.add(filename);
                 break;
      	  }
      }
      gothroughlists();
    }

/*This goes through the file lists */
    void gothroughlists(){
    	System.out.println("It will go through all the files and find the average time of execution");
    	 for(int num = 0; num < 16; num++){
    	 	switch(num){
      	  	case  0:
                 outputnum = 1;
                 System.out.println("This is the output for attack on key byte 1");
      	  	     analyse(file1);
                 file1.clear();
      	  	     break;
      	  	case  1:
                 outputnum = 2;
                 System.out.println("This is the output for attack on key byte 2");
      	  	     analyse(file2);
                 file2.clear();
      	  	     break;
      	  	case  2:
                 outputnum = 3;
                 System.out.println("This is the output for attack on key byte 3");
      	  	     analyse(file3);
                 file3.clear();
      	  	     break;
      	  	case  3:
                 outputnum = 4;
                 System.out.println("This is the output for attack on key byte 4");
      	  	     analyse(file4);
                 file4.clear();
      	  	     break;
      	  	case  4:
      	  	     outputnum = 5;
                 System.out.println("This is the output for attack on key byte 5");
                 analyse(file5);
                 file5.clear();
      	  	     break;
      	  	case  5:
                 outputnum = 6;
                 System.out.println("This is the output for attack on key byte 6");
      	  	     analyse(file6);
                 file6.clear();
      	  	     break;
      	  	case  6:
                 outputnum = 7;
                 System.out.println("This is the output for attack on key byte 7");
      	  	     analyse(file7);
                 file7.clear();
      	  	     break;
      	  	case  7:
                 outputnum = 8;
                 System.out.println("This is the output for attack on key byte 8");
      	  	     analyse(file8);
                 file8.clear();
      	  	     break;
      	  	case  8:
                 outputnum = 9;
                 System.out.println("This is the output for attack on key byte 9");
      	  	     analyse(file9);
                 file9.clear();
      	  	     break;
      	  	case  9:
                 outputnum = 10;
                 System.out.println("This is the output for attack on key byte 10");
      	  	     analyse(file10);
                 file10.clear();
      	  	     break;
      	  	case  10:
                 outputnum = 11;
                 System.out.println("This is the output for attack on key byte 11");
      	  	     analyse(file11);
                 file11.clear();
      	  	     break;
      	  	case  11:
                 outputnum = 12;
                 System.out.println("This is the output for attack on key byte 12");
      	  	     analyse(file12);
                 file12.clear();
      	  	     break;
      	  	case  12:
                 outputnum = 13;
                 System.out.println("This is the output for attack on key byte 13");
      	  	     analyse(file13);
                 file13.clear();
      	  	     break;
      	  	case  13:
                 outputnum = 14;
                 System.out.println("This is the output for attack on key byte 14");
      	  	     analyse(file14);
                 file14.clear();
      	  	     break;
      	  	case  14:
                 outputnum = 15;
                 System.out.println("This is the output for attack on key byte 15");
      	  	     analyse(file15);
                 file15.clear();
      	  	     break;
      	  	case  15:
                 outputnum = 16;
                 System.out.println("This is the output for attack on key byte 16");
      	  	     analyse(file16);
                 file16.clear();
                 break;
      	  }
      }
      System.out.println("Calculation finished");
   }

/*This analyses the data to find average time of execution */
   void analyse(ArrayList<File> chosenfile){
	 int linecount;
     for (File filename : chosenfile) {
          if(chosenfile.size() == 0){
            /*If nothing in file array then it simply continues */
            continue;
          }
     	    linecount = 0;
      	 try{
          FileReader inputFile = new FileReader(filename);
          BufferedReader bufferReader = new BufferedReader(new FileReader(filename));
          String line;
           while ((line = bufferReader.readLine()) != null){
           	linecount++;
            /*To ensure the first hit and misses aren't read as they are formally part of the attack, please refer to manual */
             if(linecount > 5){ /*So preceding optimal time data is not read */
             	  valuesforaverage(line);
             }
           }
           bufferReader.close(); // Closes the input stream
           }
       catch(IOException e){
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
             System.exit(0);
       }
     }
     /*To ensure that it only reads file lists with files in them */
     if(chosenfile.size() > 0){
      int avresult = getaveragetime();
      runsecondtime(chosenfile, avresult);
     }
   }

/*This finds the AMAT (Average Memory Access Time) of the file input */
    int getaveragetime(){
      ArrayList<Integer> hits = new ArrayList<Integer>();
      ArrayList<Integer> misses = new ArrayList<Integer>();
      for(int x = 0; x<results.size(); x++){
        if(x % 2 == 0){
        	  hits.add(results.get(x));
        }
        else{
        	  misses.add(results.get(x));
        }
      }

       int meanofhit = findmeanofhits(hits);
       int meanofmisses = findmeanofmisses(misses);

       /*Average time used to compare to find higher execution times */
       /* Below is AMAT */
       int average = meanofhit + meanofmisses * cmiss;
       System.out.println("This is the average time of execution " + average);
       hits.clear();
       misses.clear();
       results.clear();
       return average;
    }

/*To find the average hit count among the chosen files */
    int findmeanofhits(ArrayList<Integer> hits){
         Long value = 0l;
         int count = 0;
         for(int x = 0; x<hits.size(); x++){
            value += Long.valueOf(hits.get(x));
            count++;
         }
         Long lcount = Long.valueOf(count);
         Long result = value / lcount;
         int intresult = result.intValue();
         System.out.println("This is hit average for this file output " + intresult);
         return intresult;
    }

/*To find the average miss count among the chosen file */
    int findmeanofmisses(ArrayList<Integer> misses){
         Long value = 0l;
         int count = 0;
         for(int x = 0; x<misses.size(); x++){
            value += Long.valueOf(misses.get(x));
            count++;
         }
         Long lcount = Long.valueOf(count);
         Long result = value / lcount;
         int intresult = result.intValue();
         System.out.println("This is miss average for this file output  " + intresult);
         return intresult;
    }

/*To only count the hits and misss to get the AMAT */
    void valuesforaverage(String line){
        if(line.startsWith("H")){
             line = line.substring(2);
             results.add(Integer.parseInt(line));
          }
        if(line.startsWith("M")){
            line = line.substring(2);
            results.add(Integer.parseInt(line));
       }
    }

/*This now uses the data to find information about the key */
   void runsecondtime(ArrayList<File> chosenfile, int avresult){
     System.out.println("The system will go through and average encryption time for each candidate key");
     /*Long to ensure it doesn't go out of range when adding up execution times */
     /*The [][0] is for time and [][1] is the count */
     /*Each line of the array represents the values 0-255 */
     Long[][] array = new Long[256][2];
     for(int i = 0; i<256;  i++){
        for(int x = 0; x<2; x++){
            array[i][x] = 0l;
        }
     }
     int linecount;
     int count = 0;
     int result_time = 0;
     int result_key = 0;
     Long result = 0l;
     for (File filename : chosenfile) {
           count++;
           if(chosenfile.size() == 0){
            continue;
           }
          linecount = 0;
          try{
            FileReader inputFile = new FileReader(filename);
            BufferedReader bufferReader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = bufferReader.readLine()) != null){
               linecount++;
             /*This reads the first five lines without adding this to the results*/
              if(linecount > 5){
                 result_time = valuesfromlines(line, avresult);
                 if(result_time > 0){
                 	/*So the time does not reset back to 0 until it encounter a miss line */
                    result = new Long(result_time);
                 }
                 /*Will return 256 until it finds a line with key value */
                 result_key = checkkey(line);
                 if(result_key <= 255){
                    array[result_key][0] += result;
                    array[result_key][1] += 1l;
                 }
              }
            }
            bufferReader.close(); // Closes the input stream
          }
          catch(IOException e){
          	 System.out.println("Error reading in candidate key bytes on file and linecount " + chosenfile + " " + linecount);
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
             System.exit(0);
          }
     }
     System.out.println("This is the total of outputs read in " + count);
     sorttimes(array);
     outputplaintext(array, chosenfile, avresult);
    }

/*To check that the key line will be read, key ranges from 0-255 */
    int checkkey(String line){
        int result = 256;
        if(line.startsWith("K")){
           line = line.substring(2);
           result = Integer.parseInt(line);
           return result;
        }
        return result;
    }

/*This reads now to find the time measured by the hits and misses */
   int valuesfromlines(String line, int avresult){
          	if(line.startsWith("H")){
          	  line = line.substring(2);
                  hit = Integer.parseInt(line);
                  return 0;
            }
            if(line.startsWith("M")){
                line = line.substring(2);
                miss = Integer.parseInt(line);
                /*To caculate the execution time of the address reading */
                int time = offset + (chit * hit) + (cmiss* miss);
                return time;
            }
            return 0;
    }


/*To find the averge time per candidate key */
    void sorttimes(Long[][] array){
         Long time;
         Long count;
         Long result;
         for(int i = 0; i<256; i++){
               time = array[i][0];
               if(time > Long.MAX_VALUE || time < 0){
                  System.out.println("The time has gone above the Long max value");
                  System.exit(0);
               }
               count = array[i][1];
               if(count > 0 && time > 0){
                 result = time / count;
                 results.add(i);
                 results.add(result.intValue());
               }
         }
         /*This is if the file output is of a candidate key file */
         if(plaintext == false){
         printoffresults();
         plaintext = true;
         }
         else{
         	/*This prints off results for the plaint text bytes */
         	printoffplainresults();
         	plaintext = false;
         }
    }


/*Printing off the results for the candidate keys */
    void printoffresults(){
       System.out.println("This will now print the file of results that were above average execution time for key bytes");
       int line = 0;
       String printed = "";
       String fileDir = "/home/tf75/Documents/Master's project/Set Cache/CaculateData/Analysis";
       String fileName = "analysis" + outputnum + ".txt";
       File f = new File (fileDir, fileName);
       try{
           PrintWriter writer = new PrintWriter(f);
           for(int value: results){
               line++;
               /*Only values printed are K = Key and its corresponding T = Time */
               printed = Integer.toString(value);
               if(line == 1){
                   printed = "K " + printed;
               }
               else{
                   printed = "T " + printed;
                   line = 0;
               }
               writer.write(printed);
               writer.println();
               }
               writer.close();
       }catch(IOException e){
       	      System.out.println("Issue outputting file for candidate key caculations");
              e.printStackTrace();
              System.exit(0);
        }
       /*Terminal prints off all candidate key times descending */
       System.out.println("See all the times printed in descending order");
       results = results.stream().distinct().collect(Collectors.toList());
       Collections.sort(results);
       for(int i = results.size()-1; 0 < i; i--){
          if(results.get(i) > 256){
             System.out.println(results.get(i));
          }
       }
       results.clear();
   }

/*This now caculates the plaintext bytes to see if key information can be deduced */
    void outputplaintext(Long[][] array, ArrayList<File> chosenfile, int avresult){
        System.out.println("The system will go through and average encryption time for each plaintext byte");
        /*Set all values back to 0 */
        for(int i = 0; i<256;  i++){
         for(int x = 0; x<2; x++){
            array[i][x] = 0l;
         }
        }
       int linecount;
       int count = 0;
       int result_time = 0;
       int result_plain = 0;
       Long result = 0l;
       boolean ophit;  /*To ensure the first hit is read in as optimal hit */
       for (File filename : chosenfile) {
        if(chosenfile.size() == 0){
            continue;
        }
       linecount = 0;
        try{
          FileReader inputFile = new FileReader(filename);
          BufferedReader bufferReader = new BufferedReader(new FileReader(filename));
          String line;
          while ((line = bufferReader.readLine()) != null){
             linecount++;
             if(linecount > 5){ /*So preceding optimal time data is not read */
               result_time = valuesfromlines(line, avresult);
               if(result_time > 0){
                  result = new Long(result_time);
                }
               result_plain = checkplain(line);
               if(result_plain <= 255){
                  array[result_plain][0] += result;
                  array[result_plain][1] += 1l;
               }
             }
            }
           bufferReader.close(); // Closes the input stream
           }
       catch(IOException e){
       	     System.out.println("Error reading in plaintext bytes on file and line count " + chosenfile  + " " + linecount);
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
             System.exit(0);
       }
     }
     sorttimes(array);
   }

    /*This reads the plaintext byte value */
    int checkplain(String line){
    	int result = 256;
        if(line.startsWith("P")){
           line = line.substring(2);
           result = Integer.parseInt(line);
           return result;
        }
        return result;
    }

/*Prints off the plaintext value file */
    void printoffplainresults(){
       System.out.println("This will now print the file of results that were above average execution time for plaintext");
       int line = 0;
       String printed = "";
       String fileDir = "/home/tf75/Documents/Master's project/Set Cache/CaculateData/Analysis";
       String fileName = "plainanalysis" + outputnum + ".txt";
       File f = new File (fileDir, fileName);
       try{
           PrintWriter writer = new PrintWriter(f);
           for(int value: results){
                 line++;
                 /*Prints out P = Plaintext Byte and T = Time */
                 printed = Integer.toString(value);
                 if(line == 1){
                    printed = "P " + printed;
                 }
                 else{
                   printed = "T " + printed;
                   line = 0;
                 }
           writer.write(printed);
           writer.println();
           }
       writer.close();
       }catch(IOException e){
             System.out.println("Error outputting plaintext file");
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
             System.exit(0);
        }
       System.out.println("See all the times printed in descending order for plaintext");
       results = results.stream().distinct().collect(Collectors.toList());
       Collections.sort(results);
       for(int i = results.size()-1; 0 < i; i--){
          if(results.get(i) > 256){
             System.out.println(results.get(i));
          }
       }
       results.clear();
   }
}
