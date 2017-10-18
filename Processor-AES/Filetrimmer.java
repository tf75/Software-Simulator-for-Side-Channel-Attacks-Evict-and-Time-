import java.util.*;
import java.io.*;
import java.text.*;




class Filetrimmer{

	/*To store the output from file after ediitng */
	ArrayList<String> array = new ArrayList<String>();

    public static void main(String[] args){
    Filetrimmer f = new Filetrimmer();
    f.copy_file();
  }

/*Copies the file output from the valgrind lackey tool to output memory addresses, this removes everything but the address stream */
  void copy_file(){
        File directory = new File("/home/index");
        File files[] = directory.listFiles();
        Arrays.sort(files); /*list.files() does not save files in particular order so this function sorts them numerically */
        for (File filename : files) {
					/*From C AES program the file output begins with memory */
          if(filename.getName().startsWith("memory")){
          	System.out.println(filename.getName());
          try{
          FileReader inputFile = new FileReader(filename);
          BufferedReader bufferReader = new BufferedReader(new FileReader(filename));
          String line;
          while ((line = bufferReader.readLine()) != null){
          if(!line.startsWith("=")){
          line = line.substring(3);
          int indexOfLast = line.lastIndexOf(",");
          String newString = line;
          if(indexOfLast >= 0) {
           newString = line.substring(0, indexOfLast);
           array.add(newString);
          }
         }
        }
        bufferReader.close(); // Closes the input stream
           }catch(IOException e){
             e.printStackTrace(); //It prints a stack trace for this Throwable object on the error output stream that is the value of the field
          }
          output_file();
          }
     }
 }

/*Checks whether inputted word is a hexadecimal address */
    void check_hex(String hex){
  	try
    {
    boolean isNumeric = hex.matches("\\p{XDigit}+");
    }
    catch(NumberFormatException nfe)
    {
    System.out.println("not a valid hex" + hex);
    }
   }

      void output_file(){
       String fileDir = "/home/index";
			 /*Outputting exact date helps to read in files chronologically */
       String fileName = "hexoutput" + new SimpleDateFormat("yyyy.MM.dd.HHmmss.SS").format(new Date());
       File f = new File (fileDir,fileName);
       try{
           PrintWriter writer = new PrintWriter(f);
            for(String str: array){
              check_hex(str);
              writer.write(str);
              writer.println();
            }
              writer.close();
        }
       catch(IOException e){
              e.printStackTrace();
       }
       array.clear();
   }
}
