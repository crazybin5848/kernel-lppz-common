set hive.exec.compress.output=true;  
set mapred.output.compress=true;  
set mapred.output.compression.codec=org.apache.hadoop.io.compress.SnappyCodec;
set io.compression.codecs=org.apache.hadoop.io.compress.SnappyCodec;  
SET mapred.output.compression.type=BLOCK;