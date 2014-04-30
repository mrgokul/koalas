Koalas
======

Lightweight Data Analysis Framework. Inspired from R and Python-Pandas, and adapted to a terri*** language called Java. 

Two major concepts:
* DataFrame
* Series



##DataFrame

### Read a file

```java

DataFrame df = Koalas.readCSV("example.csv");
System.out.println(df);
/*



*/


//Note:- All numeric columns are cast as float!

```

### Project


```java

Series s = df.get("name"); // Get one column

String[] col = {"name","age"};
DataFrame dfp = df.get(col); // Get multiple columns
```

### Select

```java

Series s = df.ix(0); // Get one row

Integer[] row = {0,2,4};
DataFrame dfs = df.ix(row); // Get multiple rows

/* Subsetting DataFrame on conditions requires a Series of Boolean.
   Use the Series' Relational operators such as eq(),gt(),lt() and 
   optionally Logical operators such as and(), or(), not() to setup
   the boolean Series for subsetting. Yay! No Operator Overloading!
*/

/*
The below statement is equivalent to the SQL statement
SELECT * from df WHERE subject = 'Physics' AND score > 10;
*/

Series condition = df.get("subject").eq("Physics").and(df.get("score").gt(10.0f));
DataFrame dfs1 = df.subset(condition);

```

### Sort

```java
// Quicksort of course!

String[] cols = {"subject","age"}; //First sort by and then by
Integer[] order = {0,1}; //First ascending and then descending

df.sort(cols,order);
```

### Join



### Aggregate

```java
Apply func = new Apply() {
                       @Override
                       public Object map(Series x) {
                               return x.sum();
                       }
           };
 

String[] cols= {"score"};
String[] by= {"subject"};

DataFrame x= m.groupBy(cols,by,func);
```

##Series

```java
String[] cols= {"name"};
DataFrame dfm = Koalas.join(df, df, cols,"inner"));
```

Extends ArrayList, supports many mathematical methods of Pandas' Series and displays the Recycable nature of the R vector. See here for the complete docs. 

### Examples
