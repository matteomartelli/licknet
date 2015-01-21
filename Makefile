APPNAME = licknet
DIR = `cd $$(dirname "${BASH_SOURCE[0]}") && pwd`
LIBDIR = lib
LIBGS = gs-core-1.2.jar
LIBGSALGO = gs-algo-1.2.jar
LIBTG = tuxguitar-lib.jar
LIBTGGTP = tuxguitar-gtp.jar
LIBTGGPX = tuxguitar-gpx.jar
CLASSPATH = $(DIR)/$(LIBDIR)/$(LIBGS):$(DIR)/$(LIBDIR)/$(LIBGSALGO):$(DIR)/$(LIBDIR)/$(LIBTG):$(DIR)/$(LIBDIR)/$(LIBTGGTP):$(DIR)/$(LIBDIR)/$(LIBGPX)
BUILDDIR = build
DISTDIR = dist
OUTJAR = $(DIR)/$(DISTDIR)/$(APPNAME).jar
JAVAPATH = `readlink -f $$(which java)` 
EXECUTABLE = $(APPNAME).sh
MAINCLASS = $(APPNAME).Main

all:
	ant
	@echo "#!/bin/bash\n\
	$(JAVAPATH) -cp $(CLASSPATH):$(OUTJAR) $(MAINCLASS)" > $(EXECUTABLE)
	@chmod +x $(EXECUTABLE)
clean:
	ant clean; rm $(EXECUTABLE)
