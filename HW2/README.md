It is a implementation of FP-Growth. Java Programming Language was used to implement it.
There are two example dataset inside of this folder. These are Dataset1.txt and Dataset2.txt. <br />

It is an example dataset.<br />
<b>
I1,I2,I3 <br />
I2,I3,I4 <br />
I4,I5 <br />
I1,I2,I4 <br />
I1,I2,I3,I5 <br />
I1,I2,I3,I4  <br />
</b>
These means that I1,I2,I3 are together, I2,I3,I4 are together... <br />
Datasets have to be like that. You can seperate value with [, ?.@] or more than one with them. <br />

FP_Growth.java has main function to test implementation. To test this program you have to 
assingn value of MIN_SUPPORT and FILE_NAME. MIN_SUPPORT is required to find at least support 
item count and FILE_NAME is required for dataset. You can use IntelliJ to run these programs.
<a href = "https://github.com/alihaydarkurban/CSE454-Data-Mining/blob/master/HW2/Outputs.pdf"> Result of the implementation was included.</a>
