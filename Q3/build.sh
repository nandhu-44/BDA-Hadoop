#!/bin/bash

# Get Hadoop classpath dynamically
HADOOP_CLASSPATH=$(hadoop classpath)

# Clean up any existing class files
rm -f *.class

# Compile using Hadoop classpath
javac -cp "$HADOOP_CLASSPATH" MaxTemperature.java

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation complete"
    
    # Create the JAR file with all class files
    jar cvf MaxTemperature.jar *.class
    echo "JAR file MaxTemperature.jar created successfully"
else
    echo "Compilation failed. Please check the error messages above."
    exit 1
fi