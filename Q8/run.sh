
#!/bin/bash

# Set HADOOP_HOME if not set
if [ -z "$HADOOP_HOME" ]; then
    export HADOOP_HOME=/usr/local/hadoop
fi

# Set HBASE_HOME if not set
if [ -z "$HBASE_HOME" ]; then
    export HBASE_HOME=/usr/local/hbase
fi

# Set up the classpath
export CLASSPATH=.:$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/common/lib/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HBASE_HOME/lib/*

# Run the program
java HBaseAggregation