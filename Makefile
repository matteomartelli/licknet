BUILD_DIR = build
OUT_DIR = dist
OUT_JAR = ${OUT_DIR}/licknet.jar
EXECUTABLE = licknet.sh

all:
	ant
	echo "java -jar ${OUT_JAR}" > ${EXECUTABLE}
	chmod +x ${EXECUTABLE}
clean:
	rm -rf ${BUILD_DIR} ${OUT_DIR} ${EXECUTABLE}
