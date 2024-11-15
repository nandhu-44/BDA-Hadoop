
#!/bin/bash

# Set up environment variables
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:$(hbase classpath)

# Compile the Java file
echo "Compiling HBaseAggregation.java..."
javac -cp $HADOOP_CLASSPATH HBaseAggregation.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running HBaseAggregation..."
    java -cp .:$HADOOP_CLASSPATH HBaseAggregation
else
    echo "Compilation failed!"
fi