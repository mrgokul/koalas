Koalas
======

Lightweight Data Analysis Framework. Inspired from R and Python-Pandas, and adapted to a terri*** language called Java. Use it only because you have no other choice.

Two major concepts:
* DataFrame
* Series

See here for the complete docs.

##DataFrame

Very similar to a Pandas/R DataFrame

### Read a file

```java

DataFrame df = Koalas.readCSV("example.csv");
System.out.println(df);
/*



*/

```

### Project


```java

Series s = df.get(); // Get one column

String[] col = {};
DataFrame dfp = df.get(col); // Get multiple columns
```

### Select

```java

Series s = df.ix(); // Get one row

Integer[] row = {};
DataFrame dfs = df.get(row); // Get multiple rows

/* Subsetting DataFrame on conditions requires a Series of Boolean.
   Use the Series' Relational operators such as eq(),gt(),lt() and 
   optionally Logical operators such as and(), or(), not() to setup
   the boolean Series for subsetting. Yay! No Operator Overloading!
*/

/*
The below statement is equivalent to the SQL statement
SELECT * from df WHERE = AND > ;
*/

Series condition = df.get().eq().and(df.get().gt());
DataFrame dfs1 = df.subset(condition);

```

### Sort

```
// Quicksort ofcourse!

String[] cols = {}; //First sort by and then by
Integer[] order = {}; //First ascending and second descending

df.sort(cols,order);
```


