## Compilation 

To compile, simply run `javac FPGrowth.java` in the root directory of this project.

## Execution

To execute, run `java FPGrowth <db file> <minimum support threshold>`.
For example, to run the program on data.txt with a 50% minimum support
threshold, one would run

`java FPGrowth data.txt 50`.

---

The program has been tested under Java 14 (OpenJDK) on Arch Linux. 
It has been confirmed to produce equivlalent output to the Apriori
algorithm I implemented in Assignment 1 for the following inputs:

* `data.txt` at 50%
* `connect.txt` at 98%
* `connect.txt` at 99%
* `retail.txt` at 12%
