Koalas
======

Lightweight Data Analysis Framework. Inspired from R and Python-Pandas. All because we wanted to learn Java. 

Two major concepts:
* DataFrame
* Series

You can refer [here](http://mrgokul.github.io/koalas/) for the API docs. Get the JAR.


```
<dependencies>
  <dependency>
    <groupId>com.latentview.koalas</groupId>
    <artifactId>koalas</artifactId>
    <version>0.0.1-beta</version>
  </dependency>
</dependencies>

<repositories>
  <repository>
    <id>ossrh</id>
    <url>https://oss.sonatype.org/content/groups/staging/</url>
  </repository>
</repositories>
```

##DataFrame

### Read a file

```java

DataFrame df = Koalas.readCSV("example.csv");
System.out.println(df);
/*

name             age              subject          score            
____________________________________________________________________

alice            10.0             Math             7.0              
bar              10.0             Physics          100.0            
charlie          11.0             Chemistry        99.0             
doug             12.0             Physics          95.0             
eve              9.0              Physics          4.0              
foo              13.0             Chemistry        101.0            
george           12.0             Math             92.0             
harry            11.0             Chemistry        96.0             
idiot            11.0             Physics          1.0              
joker            12.0             Chemistry        2.0              


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

```
String[] cols= {"name"};
DataFrame dfm = Koalas.join(df, df, cols,"right");

```


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

DataFrame dfg= df.groupBy(cols,by,func);
```

##Series


Extends ArrayList, supports many mathematical methods of Pandas' Series and displays the Recycable nature of the R vector. 
### Examples

```
Series S =  Series.from(1,2,3);
S.add(4);

System.out.println(S);
// [1,2,3,4]

System.out.println(S.mean());
// 2.5

System.out.println(S.plus(S));
// [2.0, 4.0, 6.0, 8.0]


```