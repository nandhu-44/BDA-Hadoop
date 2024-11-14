#!/bin/bash

# Set Hive home and required jar paths
HIVE_HOME="$HOME/hive/apache-hive-4.0.1-bin"
HIVE_JDBC_JAR="$HIVE_HOME/lib/hive-jdbc-4.0.1.jar"
HIVE_SERVICE_JAR="$HIVE_HOME/lib/hive-service-4.0.1.jar"
HIVE_COMMON_JAR="$HIVE_HOME/lib/hive-common-4.0.1.jar"
LIBTHRIFT_JAR="$HIVE_HOME/lib/libthrift-0.12.0.jar"
SLF4J_JAR="$HIVE_HOME/lib/slf4j-api-1.7.36.jar"

# Check for required JARs
for jar in "$HIVE_JDBC_JAR" "$HIVE_SERVICE_JAR" "$HIVE_COMMON_JAR" "$LIBTHRIFT_JAR" "$SLF4J_JAR"; do
    if [ ! -f "$jar" ]; then
        echo "Error: Required jar not found: $jar"
        exit 1
    fi
done

# Get Hadoop classpath
HADOOP_CLASSPATH=$(hadoop classpath)

# Build complete classpath
CLASSPATH="$HADOOP_CLASSPATH:$HIVE_JDBC_JAR:$HIVE_SERVICE_JAR:$HIVE_COMMON_JAR:$LIBTHRIFT_JAR:$SLF4J_JAR"

# Clean and compile
echo "Compiling with Hadoop and Hive dependencies..."
rm -f *.class
javac -cp "$CLASSPATH" HiveOperations.java

if [ $? -eq 0 ]; then
    echo "Compilation successful"
    # Run
    echo "Running HiveOperations..."
    java -cp "$CLASSPATH:." HiveOperations
else
    echo "Compilation failed"
    exit 1
fi