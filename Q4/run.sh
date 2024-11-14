
#!/bin/bash

# Remove output directory if it exists
hadoop fs -rm -r -f /user/nandhu/matrix/output

# Run the MapReduce job
# Arguments: <input path> <output path> <rows A> <cols A/rows B> <cols B>
hadoop jar MatrixMultiplication.jar MatrixMultiplication \
    /user/nandhu/matrix/input \
    /user/nandhu/matrix/output \
    2 3 2

# Display the results
echo "Matrix multiplication results:"
hadoop fs -cat /user/nandhu/matrix/output/part-r-*