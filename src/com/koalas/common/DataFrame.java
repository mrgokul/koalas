/*
 * Copyright 2009 Gokul Ramesh <gokul.q3a@gmail.com>
 * Copyright 2009 Sureshkrishna Gonugunta <gsuresh92@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.koalas.common;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.koalas.common.Series;
import com.koalas.utils.Apply;
import com.koalas.utils.Utils;
import com.koalas.utils.SeriesIterator;
/**
 * Tabular data structure with labeled axes (rows and columns).
 * @author GokulRamesh,Sureshkrishna
 *
 */
public class DataFrame implements Iterable{
	
    private  Map<String,Series> mapSeries;
    private  Map<Integer,Series> TmapSeries;
    
    
    private List<Object> Columns;
    private List<Integer> rownum;
	
	/**
	 * To Create a DataFrame given list of columns  as list of series and header as list of objects
	 * @param seriesList list of series which is list of columns
	 * @param index list of objects  which is header
	 */
	public DataFrame (List<Series> seriesList, List<Object> index){

		if (seriesList.size() != index.size()){
			throw new IllegalArgumentException("Unmatching size of index and List");

		}
		
		int first_size = seriesList.get(0).size();
		for(Series s:seriesList){
			if(first_size != s.size()){
				throw new IllegalArgumentException("Size of series is differing");
			}
		}
		Map<Object,Integer> map= new HashMap<Object,Integer>();
		List<Object> index1 = new ArrayList<Object>();
		for(Object o:index){
			if (!(map.containsKey(o))){
				map.put(o,1);
				index1.add(o);
			}
			else{
				index1.add((String)o+"."+ map.get(o));
				map.put(o,map.get(o)+1);
			}
		}
		this.Columns = index1;
		this.mapSeries = new HashMap<String,Series>();
		this.TmapSeries = new HashMap<Integer,Series>();
		
		int j=0;
		for(Series s:seriesList){			 
			this.mapSeries.put((String) index.get(j), s);
			j++;
		}

	
		for(int k=0; k<first_size; k++){
			Series invSeries = new Series();
			for(int l = 0; l < seriesList.size(); l ++){
				invSeries.add(seriesList.get(l).get(k));
			}
			TmapSeries.put(k, invSeries);
		}
		this.rownum = new ArrayList<Integer>();
		for(int i=0;i<TmapSeries.size();i++){
			rownum.add(i);
		}

		
	}
	/**
	 * To Create a DataFrame given list of rows  as list of series and header as list of objects
	 * @param seriesList list of series which is list of rows
	 * @param index list of objects  which is header
	 * @param trans Always 'true'. Just to differentiate between rows and columns input type 
	 */
	public DataFrame (List<Series> seriesList, List<Object> index,Boolean trans){
		
		Map<Object,Integer> map= new HashMap<Object,Integer>();
		List<Object> index1 = new ArrayList<Object>();
		for(Object o:index){
			if (!(map.containsKey(o))){
				map.put(o,1);
				index1.add(o);
			}
			else{
				index1.add((String)o+"."+ map.get(o));
				map.put(o,map.get(o)+1);
			}
		}
		this.Columns = index1;
		this.mapSeries = new HashMap<String,Series>();
		this.TmapSeries = new HashMap<Integer,Series>();
		int j=0;
		if (seriesList.size()==0){
			oneLine(Columns);
		}
		
		else{
			
		int first_size = seriesList.get(0).size();
		
		for(Series s:seriesList){
			if(first_size != s.size()){
				throw new IllegalArgumentException("Size of series is differing");
			}
		}
		if (seriesList.get(0).size() != index.size()){
			throw new IllegalArgumentException("Unmatching size of index and List");
		
		}			
		for(Series s:seriesList){
			this.TmapSeries.put(j, s);
			j++;
			}
		for(int k=0; k<first_size; k++){
			Series invSeries = new Series();
			for(int l = 0; l < seriesList.size(); l ++){
				invSeries.add(seriesList.get(l).get(k));
			}
			mapSeries.put((String) index.get(k), invSeries);
		}
		this.rownum = new ArrayList<Integer>();
		for(int m=0;m<TmapSeries.size();m++){
			rownum.add(m);
		}
		}
		
	}
	

	
	
	private String oneLine(List<Object> aList){
		String ret = "";
		String elem ="";
		for (int i = 0; i < aList.size(); i++){
			if (aList.get(i)==null){
				elem="Null";
			}
			else{
				elem = aList.get(i).toString();
			}			
			String newElem = elem;
			int space = 16 - elem.length() + 1 ;
			if(elem.length() > 16){
				newElem = elem.substring(0,14) + "   ";
			}
			ret += newElem;
			for(int j = 0; j < space; j++){
				ret += " ";
			}
		}
		return ret;
	}
	

	/**
	 * Outputs the DataFrame
	 */
	public String toString(){
		int head= 10;
		if (TmapSeries.size()<head){
			head=TmapSeries.size();
		}
		String ret = "";
		String line = "";
		for (int i = 0; i <  oneLine(Columns).length(); i ++){
			line=line+"_";
		}
		ret +=  oneLine(Columns) + "\n" + line + "\n\n";
		for(int i = 0; i < head; i ++){
			ret += oneLine(TmapSeries.get(rownum.get(i))) + "\n";
		}
		if (TmapSeries.size() > head){
			ret += ".\n.\n.\n"+(TmapSeries.size()-head) +" rows more...";
		}
		return ret;
	}
	
	/**
	 * Getting the first required number of rows as a separate DataFrame 
	 * @param rows required number of rows as integer
	 * @return DataFrame with number of rows = rows
	 */
	public DataFrame head(int rows){
		if (rows > TmapSeries.size()){
			throw new IllegalArgumentException("Not that many rows exist!");
		}
		List<Series> row = new ArrayList<Series> ();
		for (int i=0; i< rows; i++){
			row.add(TmapSeries.get(rownum.get(i)));
		}
		DataFrame newdf = new DataFrame(row,Columns,true);
		return newdf;
	}
	
	/**
	 * Getting the last required number of rows as a separate DataFrame 
	 * @param rows required number of rows as integer
	 * @return DataFrame with number of rows = rows
	 */
	public DataFrame tail(int rows){
		if (rows > TmapSeries.size()){
			throw new IllegalArgumentException("Not that many rows exist!");
		}
		List<Series> row = new ArrayList<Series> ();
		for (int i=TmapSeries.size() - rows; i< TmapSeries.size(); i++){
			row.add(TmapSeries.get(rownum.get(i)));
		}
		DataFrame newdf = new DataFrame(row,Columns,true);
		return newdf;
	}
	
	/**
	 * Getting the selective rows as a separate DataFrame by giving the row numbers as integer array   
	 * @param rows integer array
	 * @return DataFrame with number of rows = length of rows
	 */
	public  DataFrame ix(int[] rows){
		List<Series> row = new ArrayList<Series> ();
		for (int i=0; i< rows.length; i++){
			row.add(TmapSeries.get(rownum.get(i)));
		}
		DataFrame newdf = new DataFrame(row,Columns,true);
		return newdf;
	}

	/**
	 * Getting the required row from a DataFrame as Series  by giving the row index
	 * @param row integer 
	 * @return Series 
	 */
	public  Series ix(int row){
		return TmapSeries.get(rownum.get(row)).clone();
	}
	
	/**
	 *  Getting the selective columns as a separate DataFrame by giving the column names  as string array   
	 * @param columnnames string array
	 * @return DataFrame with number of columns = length of column names
	 */
	public DataFrame get(String[] columnnames){
		List<Series> row = new ArrayList<Series> ();
		List<Object> newcolumns= new ArrayList<Object>();
		for (int i=0; i< columnnames.length; i++){
			row.add(mapSeries.get(columnnames[i]));
			newcolumns.add(columnnames[i]);
		}
		DataFrame newdf = new DataFrame(row,newcolumns);
		return newdf;		
	}
	
	/**
	 *  Getting the selective columns as a separate DataFrame by giving the indexes of columns  as integer array   
	 * @param columnindex integer array
	 * @return DataFrame with number of columns = length of columnindex
	 */
	public DataFrame get(int[] columnindex){
		List<Series> column = new ArrayList<Series> ();
		List<Object> newcolumns= new ArrayList<Object>();
		for (int i=0; i< columnindex.length; i++){
			column.add(mapSeries.get(Columns.get(i)));
			newcolumns.add(Columns.get(i));
		}
		DataFrame newdf = new DataFrame(column,newcolumns);
		return newdf;		
	}
	
	
	/**
	 * Getting the required column from a DataFrame as Series  by giving the column name
	 * @param col columnname  
	 * @return Series 
	 */
	public Series get(String col){
		return mapSeries.get(col).clone();		
	}
	
	/**
	 * updating the value in a DataFrame provided column name and index of value in that column
	 * @param col column name as string
	 * @param position index of value in that column
	 * @param value new value
	 */
	public void set(String col,int position,Object value){
		TmapSeries.get(rownum.get(position)).set(Columns.indexOf(col),value);
		mapSeries.get(col).set(position,value);
	}
	/**
	 * updating the entire column in a DataFrame with a new series of replacing column size 
	 * @param col column name as string
	 * @param newseries Series which replaces the given column
	 */
	public void set(String col,Series newseries){
		if (Columns.contains(col)){
			if (TmapSeries.size() == newseries.size()){
				mapSeries.put(col, newseries);
				Iterator<Object> it=newseries.iterator();
				for ( Integer i :rownum){
					TmapSeries.get(i).set(Columns.indexOf(col),it.next());
				}
			}
			else{
				throw new IllegalArgumentException("Sizes of old and new columns are diferent");
			}
		}
		else{
			if (TmapSeries.size() == newseries.size()){
				mapSeries.put(col, newseries);
				Columns.add(col);
				Iterator<Object> it=newseries.iterator();			
				for ( Integer i :rownum){
					TmapSeries.get(i).add(it.next());
				}
			}
			else{
				throw new IllegalArgumentException("Sizes of old and new columns are diferent");
			}			
		}		
	}
	/**
	 * To find the size of a DataFrame
	 * @return List of integers which contains number of rows and number of columns
	 */
	 public List<Integer> size(){
		 List<Integer> size= new ArrayList<Integer>(); 
		 size.add(TmapSeries.size());
		 size.add(mapSeries.size());
		 return size;
	 }
	 /**
	  * To append a new DataFrame to the existing one on a condition 
	  * that both should have same number of columns
	  * @param df
	  */
	 public void append(DataFrame df){
		 if (!(df.mapSeries.size() == mapSeries.size())){
			 throw new IllegalArgumentException("Column Sizes of old and new dataframes are diferent");
		 }
		 else {
			 Set<Object> other = new HashSet<Object>(df.Columns);
			 Set<Object> existing = new HashSet<Object>(Columns);
			 if (!(existing.equals(other))){
				 throw new IllegalArgumentException("Column names of old and new dataframes are diferent");
			 }
		 }
		 int max= Collections.max(rownum);
		 for(int i=0;i<df.TmapSeries.size();i++){
			 TmapSeries.put(max+i+1,df.TmapSeries.get(df.rownum.get(i)) );
			 rownum.add(max+i+1);
		 }
		 for(int i=0;i<df.mapSeries.size();i++){
				 mapSeries.get(Columns.get(i)).addAll(df.mapSeries.get(Columns.get(i)));
		 }
		 
	 }
	

	/**
	 * To write a DataFrame to  csv by giving the location of new file 
	 * @param filelocation location to which csv has to be witten
	 */
	public  void toCSV(String filelocation){
		int length=TmapSeries.size();
		String content = "";
		try {
			File file = new File(filelocation);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i=0;i<Columns.size();i++){
				if(!(i==Columns.size()-1)){
					content = content +  (String) Columns.get(i) + ",";
				}
				else{
					content = content +  (String) Columns.get(i);
				}				
			}
			bw.write(content);
			bw.newLine();
			
			for (int i=0;i<length;i++){
				Series row = TmapSeries.get(i);
				Iterator<Object> it=row.iterator();
				content="";
				while(it.hasNext()){
					content = (String) it.next().toString();
					bw.write(content);
					if (it.hasNext()){
					bw.write(",");
					}
				}
				bw.newLine();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			bw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * To sort a DataFrame by giving the column names and axis 
	 * @param columnNames String array of column names on which sorting has to be performed 
	 * @param axis Integer array whcih contains 0 or 1 for each column mentioned in column names, 
	 * 0-asc, 1- desc
	 */
    @SuppressWarnings({ "unchecked" })
    public void sort(String[] columnNames,int[] axis){
		
	
		
		boolean first = true;
		boolean check = true;
    	for(int i =0;i<columnNames.length;i++){
    		List<Series> sorts = new ArrayList<Series>();
    		String col = columnNames[i];
    		int sorttype=axis[i];
    		for (int m=0;m<mapSeries.size();m++){
    			if (!(Columns.get(m).equals(col))){
    				sorts.add(mapSeries.get(Columns.get(m)));
    			}    			
    		}
    		if(first){
    			
    		Utils.quicksort(mapSeries.get(col), rownum, 0, rownum.size() - 1,sorts,sorttype);
    		}
    		else{
    			int j = 0;
    			int k = 1;
    			Series s = mapSeries.get(col);
    			
    			//System.out.println(prev);
    			while(k < s.size()){
    				for(int p=i-1;p>=0;p--){
    					if(((Comparable) mapSeries.get(columnNames[p]).get(j)).compareTo((Comparable) mapSeries.get(columnNames[p]).get(k)) == 0){
        					check= true;        					
        				}
    					else{
    						check=false;
    						break;
    					}
    				}
    				if(check == true){
    					k++;
    				}
    				else{
    					if(k-j > 1){
    					    Utils.quicksort(s, rownum, j, k-1,sorts,sorttype);
    					    j = k-1;
    					}
    					j++;
    					k++;
    				}
    			}
    			
    		}
    		first = false;
    	}     		
    }
	
    /**
     * To sort a DataFrame in ascending order by giving the column name 
     * @param column  Name of the column as string  
     */
    public void sort(String column){
        int [] axis={0};
    String[] columns = {column};
        sort(columns,axis);
    }
    /**
     *  To sort a DataFrame in ascending order by giving the column names
     * @param columnNames Names of the columns as string array
     */
    public void sort(String[] columnNames){
    	int [] axis=new int[columnNames.length];
    	for (int i=0;i<columnNames.length;i++){
    		axis[i]=0;
    	}
    	sort(columnNames,axis);
    }
    /**
     * To subset a DataFrame by giving a Series of booleans (True:consider the row, False: don't consider the row) 
     * @param s Series of booleans 
     * @return DataFrame
     */
    public DataFrame subset(Series s){
    	List<Series> l=new ArrayList<Series>();
    	int i=0;
    	for(Object o:s){
    		if ((boolean) o){
    			System.out.println(o);
    			l.add(TmapSeries.get(rownum.get(i)));   			
    		}
    		i++;
    	}
    	DataFrame sub=  new DataFrame(l,Columns,true);
    	return sub;
    }
    /**
     * To get the Transpose of a DataFrame
     * @return DataFrame
     */
    public DataFrame T(){
    	List<Object> newcol= new ArrayList<Object>();
    	List<Series> sl= new ArrayList<Series>();
    	for (Integer i:rownum){
    		Integer next = i;
    		newcol.add(next.toString());
    		sl.add(TmapSeries.get(next));    		
    	}  	
    	DataFrame newdf= new DataFrame(sl,newcol);
    	return newdf;
    }
    /**
     * To iterate through each row in DataFrame
     */
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return (Iterator) new SeriesIterator(this);
	}
	
	/**
	 * To replace all null values in a DataFrame with the given object
	 * @param x Object which will replace the null values 
	 */
	public void replace(Object x){
		for (int i=0;i<Columns.size();i++){
			Collections.replaceAll(mapSeries.get(Columns.get(i)), null, x);
		}
		for (int i=0;i<TmapSeries.size();i++){
			Collections.replaceAll(TmapSeries.get(i), null, x);
		}
	}
		/**
		 * To replace all null values in the given column of a DataFrame with the given object
		 * @param col column name as string
		 * @param x  Object which will replace the null values 
		 */
	public void replace(String col,Object x){
		int i=0;
		for(Object o:mapSeries.get(col)){
			if (o == null){
				mapSeries.get(col).set(i, x);
				TmapSeries.get(rownum.get(i)).set(Columns.indexOf(col),x);
			}
			i++;
		}
	}

	private HashMap<Integer,Series> map2tmap(){
		HashMap<Integer, Series> tmap2=  new HashMap<Integer,Series>();		
		for(int k=0; k<rownum.size(); k++){
			Series invSeries = new Series();
			for(int l = 0; l < mapSeries.size(); l ++){
				invSeries.add(mapSeries.get(Columns.get(l)).get(k));
			}
			tmap2.put(k, invSeries);
		}
		return tmap2;
	} 
	/**
	 * To drop the  given column names in the DataFrame
	 * @param cols Column names as String array
	 */
	public void drop(String[] cols){
		for (int i=0;i< cols.length;i++){
			Columns.remove(cols[i]);
			mapSeries.remove(cols[i]);
		}
		this.TmapSeries=map2tmap();
	}
	
	/**
	 * To get the Header of a DataFrame
	 * @return List of column names
	 */
	public List<Object> getColumns(){
		List<Object> cols =new ArrayList<Object>(this.Columns);
		return cols;
	}
	
	/**
	 * To rename the column names of DataFrame
	 * @param cols1 Old Column names as String array
	 * @param cols2 New Column names as String array
	 */
	public void setcolumns(String[] cols1,String[] cols2){
		for(int i=0;i<cols1.length;i++){
			Columns.set(Columns.indexOf(cols1[i]),cols2[i]);
			Series obj = mapSeries.remove(cols1[i]);
			mapSeries.put(cols2[i], obj);					
		}		
	}

	public DataFrame groupBy(String[] x,String[] cols,Apply func){
		List<Object> newcol= new ArrayList<Object>();
		Map <List<Object>,Series> map=new LinkedHashMap <List<Object>,Series>();
		@SuppressWarnings("unused")
		List<Object> cols1= this.getColumns();
		Iterator itdf = this.iterator();
		newcol.addAll(Arrays.asList(cols));
		newcol.addAll(Arrays.asList(x));
		while(itdf.hasNext()){
			List<Object>key=new ArrayList<Object>();
			Series ls= new Series();
			Series row = (Series) itdf.next();
			for(int j = 0; j<cols.length;j++){
				key.add(row.get(Columns.indexOf(cols[j])));							
			}
			for(int j = 0; j<x.length;j++){
				ls.add(row.get(Columns.indexOf(x[j])));
				//System.out.println(ls);
			}
			
			if(!(map.containsKey(key))){
				Series value=new Series();
				for(Object o:ls){
					Series s1=new Series();
					s1.add(o);
					value.add(s1);
				}
				map.put(key, value);
				
			}			
			else{
				Series b = map.get(key);
				Iterator<Object> it=ls.iterator();
				for (Object o:b){
					Series s2=(Series) o;
					s2.add(it.next());
				}
					
				}
			}
		Iterator mapkey= map.entrySet().iterator();
		List<Series>tmap2=new ArrayList<Series>();
		while(mapkey.hasNext()){			
			Map.Entry pairs = (Map.Entry)mapkey.next();
			@SuppressWarnings("unchecked")
			List<Object> keys= (List<Object>) pairs.getKey();
			Series values= (Series) pairs.getValue();
			Series agg= aggregate(func,values);
			map.put(keys,agg);
			Series s= new Series(keys);
			s.addAll(agg);
			tmap2.add(s);
		}
		DataFrame df = new DataFrame(tmap2,newcol,true);
		return df;
		
	}
		
	
	

	private  Series aggregate(Apply func, Series s) {
		
        Series j= new Series();
        for(Object o:s){
        	j.add(func.map((Series) o));
        }
        return j;
    }
	

	
}


