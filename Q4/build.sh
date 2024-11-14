#!/bin/bash

# Create input directory if it doesn't exist
mkdir -p input

# Check if input.txt exists, if not create it
if [ ! -f input/input.txt ]; then
    echo "Creating input/input.txt with sample matrix data..."
    cat > input/input.txt << EOL
A,0,0,2.0
A,0,1,3.0
A,0,2,1.0
A,1,0,4.0
A,1,1,1.0
A,1,2,2.0
B,0,0,1.0
B,0,1,2.0
B,1,0,3.0
B,1,1,1.0
B,2,0,2.0
B,2,1,4.0
EOL
fi

# Get Hadoop classpath dynamically
HADOOP_CLASSPATH=$(hadoop classpath)

# Clean up any existing class files
rm -f *.class

# Compile using Hadoop classpath
javac -cp "$HADOOP_CLASSPATH" MatrixMultiplication.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation complete"
    
    # Create the JAR file with all class files
    jar cvf MatrixMultiplication.jar *.class
    echo "JAR file MatrixMultiplication.jar created successfully"

    # Create HDFS directories and upload input file
    echo "Creating HDFS directories and uploading input file..."
    hadoop fs -rm -r -f /user/nandhu/matrix/input
    hadoop fs -mkdir -p /user/nandhu/matrix/input
    hadoop fs -put input/input.txt /user/nandhu/matrix/input/
    
    echo "Setup complete. You can now run the MapReduce job."
else
    echo "Compilation failed. Please check the error messages above."
    exit 1
fi