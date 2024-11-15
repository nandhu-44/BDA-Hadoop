
#!/bin/bash

# Set up environment variables
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:$(hbase classpath)

# Compile the Java file
echo "Compiling HBaseSorting.java..."
javac -cp $HADOOP_CLASSPATH HBaseSorting.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running HBaseSorting..."
    java -cp .:$HADOOP_CLASSPATH HBaseSorting
else
    echo "Compilation failed!"
fi