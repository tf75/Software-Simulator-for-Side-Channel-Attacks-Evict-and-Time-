import java.util.*;
import java.io.*;

class Input{

/*To keep track of line number in case an error is thrown */
  int linenum;
/*Store all plaintext bytes for Attacker */
  ArrayList<Integer> plaintext = new ArrayList<Integer>();

	public static void main(String[] args){
	 Input i = new Input();
	 i.setinputs();
	}

/*First it is input of sets or if set assoc is 1, total num of blocks */
	void setinputs(){
     System.out.println("Please input the power for amount of amount sets");
     Scanner in = new Scanner(System.in);
     int num = in.nextInt();
     if(num >=  1){
        int numset = num;
        setinputs_numbytes(numset);
     }
     else{
          /*If incorrect does not exit but just repeats till successful input */
        	System.out.println("Please input a power of equal to or greater than 1");
        	setinputs();
    }
	}

/*Input power of the size of per block */
	void setinputs_numbytes(int numset){
    int numbytes;
		System.out.println("Please input the power of memory space in the cache blocks per set (2 to the power of 2 will be 4 bytes)");
		Scanner in = new Scanner(System.in);
    int num = in.nextInt();
    if(num >=  1){
      numbytes = num;
      set_associative(numset, numbytes);
    }
    else{
        	System.out.println("Please input a power of equal to or greater than 1");
        	setinputs_numbytes(numset);
    }
	}

/*To set the set-assoc level, this is not a power of but just integer */
  void set_associative(int numset, int numbytes){
    int setasoc;
    System.out.println("Please input the number of set associative");
    Scanner in = new Scanner(System.in);
    int num = in.nextInt();
    if(num >=  1){
       setasoc = num;
       setinputs_plaintextbytes(numset, numbytes, setasoc);
    }
    else{
          System.out.println("Please input a number equal to or greater than 1");
          set_associative(numset, numbytes);
    }
  }

	void setinputs_plaintextbytes(int numset, int numbyte, int setasoc){
		 int i = 0;
		 int total = 0;
     System.out.println("This will now read all the plaintext bytes in the file plaintext.txt");
     try{
         	    String fileDir = "/home/tf75/Downloads/updated28022018-Software-Simulator-for-Side-Channel-Attacks-Evict-and-Time--master/plaintext";
              String fileName = "plaintext.txt"; /*This copies all the plaintext bytes into one array list */
              File f = new File (fileDir,fileName);
              Scanner scanner = new Scanner(f);
              while(scanner.hasNext()){
              plaintext.add(scanner.nextInt(16));
              /*Plaintext bytes should be equal to or less than 255 decimal value */
              if(plaintext.get(total) > 255){
                System.out.println("This plaintext byte is too large, please ensure they lie in the range of 0x00 to 0xff");
                System.exit(0);
              }
              total++;
              }
      }
		  catch(IOException e){
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
             System.exit(0);
      }
    /*Each line should of had 16 bytes */
		 if(total % 16 == 0){
		 }
		 else{
		 	      System.out.println("Please make sure that byte values are a multiple of 16 " + total);
            System.exit(0);
		 }
		 System.out.println("The list of plaintext bytes has been parsed. Total plaintext bytes are " + total);
     settingupattack(numset, numbyte, setasoc);
	}

	void settingupattack(int numset, int numbyte, int setasoc){
     checkfiles(); /*This checks that all files in the hexmemtraces are of hexadecimal addresses */
     /*Adds how many plaintext bytes read in, if you put 1 it will read from position 0 and so on */
     System.out.println("How many plaintext bytes do you want to read? 1-16");
     Scanner in = new Scanner(System.in);
     int num = in.nextInt();
     if(num > 0 && num <= 16){
     /*This initalises the attacker java class */
      Attacker attacker = new Attacker();
     /*This simply adds the plaintext byte array to the attacker */
      attacker.begin(plaintext, numset, numbyte, num, setasoc);
    }
    else{
      System.out.println("Please put in number between 1-16");
      settingupattack(numset, numbyte, setasoc);
    }
	}

  /*This checks that all files within the memory trace folder are of hexadecimal addresses */
  void checkfiles(){
  	System.out.println("The hex files will now be parsed");
    File directory = new File("/home/tf75/Downloads/updated28022018-Software-Simulator-for-Side-Channel-Attacks-Evict-and-Time--master/HexOutput");
    File files[] = directory.listFiles();
    for(File filename : files) {
        try{
           FileReader inputFile = new FileReader(filename);
           BufferedReader bufferReader = new BufferedReader(new FileReader(filename));
           String line;
           int linecount = 0;
           while((line = bufferReader.readLine()) != null){
                  linecount++;
            /*Checks whether the address value read in is hexadecimal notation */
                  check_hex(line, linecount, filename);
           }
           bufferReader.close(); // Closes the input stream
        }
        catch(IOException e){
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
             System.exit(0);
        }
    }
    System.out.println("Hex files successfully parsed");
  }

  /*This tests whether all the file is of hex inputs */
   void check_hex(String hex, int linecount, File filename){
     boolean isNumeric = hex.matches("\\p{XDigit}+");
     if(isNumeric == false){
        assert(isNumeric == false);
        /*Will print out the line and where it stopped parsing so the user can correct this */
        System.out.println("not a valid hex input " + hex + " on line " + linecount + "of file " + filename);
        System.exit(0);
     }
   }
}
