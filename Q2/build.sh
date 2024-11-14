
#!/bin/bash

# Get Hadoop classpath dynamically
HADOOP_CLASSPATH=$(hadoop classpath)

# Compile using Hadoop classpath
javac -cp "$HADOOP_CLASSPATH" WordFrequency.java

echo "Compilation complete"