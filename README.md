LickNet
=======

LickNet is a tool for building and analyzing networks of guitar licks and solos. The networks may be built with a set of guitar tab files as input. It is also possible to create a network for each set of files in order to create categorized networks, for examples a different network for each guitarist may be created. Once the networks are created it is possible to classify an unknown lick or solo and also new licks and solos can be generated. The used tecniques and algorithms will be discussed in the Documentation part.

##Dependencies
###Graphstream 
The core and algo libraries (>= 1.2) must be [downloaded](http://graphstream-project.org/download/) and put in the *lib* directory. 

###Tuxguitar
The version needed is >= 1.3 (current version) and unfortunately there are no ready jar lib files for the current version, thus the source from the [sourcefourge repository](http://sourceforge.net/projects/tuxguitar/) must be downloaded and re-build. To re-build it, once you have checked out a svn working copy, go into
```
build-scripts/tuxguitar-linux-x86_64/
```
here, start the compilation using maven (you will need to install maven and some other dependencies like jack-devel, fluidsynth-devel, etc ...) :
```
mvn clean package -Dnative-modules=true
```
You will find the compiled distrib in
```
build-scripts/tuxguitar-linux-x86_64/target/tuxguitar-1.3-SNAPSHOT-linux-x86_64/ 
```
Now copy lib/tuxguitar-lib.jar, share/plugins/tuxguitar-gtp.jar and share/plugin/tuxguitar-gpx.jar in the build folder.

##Build
Make sure of the libraries are placed in the *lib* directory.
Then under linux, run *make* and launch *licknet.sh*.
With other OSs you should find how to build with *ant* that is what actually the Makefile does.

##Contribute
If you want to contribute 

    Fork LickNet
    Create a your branch based on the master branch (git checkout -B my-awsome-feature)
    Make your changes and push them to your fork
    Create a pull request
