# Software-Simulator-for-Side-Channel-Attacks-Evict-and-Time-
A beginning of a project on building a software simulator for side channel attacks on memory caches. The attack focused on was Evict and Time. Please see below for manual: 

Evict and Time Attack Simulator 

Files
AES.c - 128 AES Encryption, I have modified it so it does not decrypt but if one wants to see the
source of this C program please refer to here: http://mok-kong-shen.de/index.html - this
program has been tested against National Institute of Standards and Technology Special
Publication 800-38A 2001 ED Appendix F: Example Vectors for Modes of Operation of the AES.
Filetrimmer.java – This turns all memory trace files into purely hex address files that can be read
by the memory cache.

Input.java – To test that all non-java files are correct and the attack has been set up correctly.

Attacker.java – Deals with manipulation of the cache to run address streams, evict sets or blocks
and outputs data.

SetCache.java – This class is of a basic set associative memory cache design.

SecureCache.java – This class implements replacement policies of the NewCache (please refer
here for more details: http://dl.acm.org/citation.cfm?id=1250723).

Caculateresults.java – Reads in all the attacker output and outputs information that can be read
to decrease the complexity of the key.

Running Attack
- First download Valgrind Lackey Tool, you can so do for free here: http://valgrind.org/docs/manual/lk-manual.html 

- Then run the AES.c for the desired output it is recommended to run a bash script with a
specified number, outputting it with specific data will make it easier for programs to read
in files chronologically, this will then output the necessary memory files –

for i in {0..n}; do
valgrind
--tool=lackey
--trace-mem=yes
--log-
file="memorytraces$(date +%Y%m%d-%H%M%S)" ./AES
done

- Whilst running this attack it must be specified where the plaintext byte values will be
outputted on this line (383), any file name will do but for clarity I simply labelled it
plaintext.txt and each line will have the 16 plaintext bytes:

void printplaintext(byte pt[])
{
int i;
FILE *file = fopen("/home/index/plaintext.txt", "a");
if (file == NULL)

- To run the attack, one must specify the pathways to specific files, please see below for
example:

System.out.println("This will now read all the plaintext bytes in the
file plaintext.txt");
try {
String fileDir = "/home/index ...";
String fileName = "plaintext.txt"; /*This copies all the plaintext
bytes into one array list */

• The above tests that all plaintext bytes inputted are correct. There are file index pathways
throughout in the FileTrimmer, Input and Attacker classes with description in the code
what these should be reading in or outputting.

• FileTrimmer class should contain the right pathway to the memtrace files outputted by
the Lackey tool. With a folder destination in the output that specifics where the Input and
Attacker will be reading the memory traces from.

• Input checks all plaintext bytes outputted by AES.c and all the files outputted from the
FileTrimmer to ensure they are hexadecimal addresses. If one wants to read in non-
hexadecimal addresses then they can edit this in the Input.java class.

• Attacker.java needs file paths to the FileTrimmer file output and somewhere to output
data files. It is also where the cache class is initialised.

• Caculateresults.java reads all the output from the Attacker.java and then outputs one file
relating to information regarding the candidate key information and the other regarding
plaintext byte inputs.

- To run the attack, first compile the code: ‘ javac -g -Xlint Attacker.java’ to run put ‘ java Input’ into the command window

- This will then ask you to input the power of number for the size of the sets and blocks,
along with a number for the set associative. If one wants to mimic direct memory cache
mapping put the set associative number to 1. It will ask you how many bytes you want to
read in, you can choose between 1-16.

- In the Attacker class, one can use the two example cache designs provided or they can slot
in their own designs. To see how one could do that then please examine the Attacker class
and replace any reference to ‘sc’ with a link to their cache design. It is important to note
that for the attack to work the cache must count the number of hits and misses, be able to
store tags in some capacity, be able to evict specific or the whole cache and be initialised
via the size inputs. To get an example of how to do this I would encourage to look over the
SetCache design that is the simplest to understand.

- Once the attack is completed then you can calculate the results. All output files from the
Attacker will have its key byte number after the ‘!’, this is so the Caculateresults program
outputs specific information relating to the attack on that key byte. The terminal will print
out the times in descending orders, if the attack is successful the candidate keys or
plaintext bytes in the range of the actual key byte should be producing the highest time.

Key for Attacker output file:
H 502663 – Hit Rate
M 7029 – Miss Rate
K 1 – Candidate Key
P 78 – Plaintext Byte
S 7 – Set being deleted

The first four lines of each output file are not read by the Caculateresults program as this does
not contain any eviction instead it is for the user to see the normal hit/miss rate without eviction.
