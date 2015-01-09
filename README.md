LickNet
=======

LickNet is a tool for building and analyzing networks of guitar licks and solos. The networks may be built with a set of guitar tab files as input. It is also possible to create a network for each set of files in order to create categorized networks, for examples a different network for each guitarist may be created. Once the networks are created it is possible to classify an unknown lick or solo and also new licks and solos can be generated. The used tecniques and algorithms will be discussed in the Documentation part.

##Dependencies
###Graphstream 
The core library (>= 1.2) must be downloaded and put in the lib folder. http://graphstream-project.org/download/

###Tuxguitar
The version needed is >= 1.3 (current version) and unfortunately there are no ready jar lib files for the current version, thus the source from the sourcefourge svn repository must be downloaded and re-build. To rebuild it, once you have checked out a svn working copy, go into

build-scripts/tuxguitar-linux-x86_64/

here, start the compilation using maven (you will need to install maven and some other dependencies like jack-devel, fluidsynth-devel, etc ...) :

mvn clean package -Dnative-modules=true

You will find the compiled distrib in

build-scripts/tuxguitar-linux-x86_64/target/tuxguitar-1.3-SNAPSHOT-linux-x86_64/ 

Now copy lib/tuxguitar-lib.jar, share/plugins/tuxguitar-gtp.jar and share/plugin/tuxguitar-gpx.jar in the build folder.

##Build
TODO

##Contribute
TODO
