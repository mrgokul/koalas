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

public class DataFrame implements Iterable{
	
    private  Map<String,Series> mapSeries;
    private  Map<Integer,Series> TmapSeries;
    
    
    private List<Object> Columns;
    private List<Integer> rownum;
	
	public DataFrame (List<Series> seriesList, List<Object> index){

		if (seriesList.size() != index.size()){
			throw new IllegalArgumentException("Unmatching size of index and List");

		}
		int first_size = seriesList.get(0).size();
		Iterator<Series> it=seriesList.iterator();
		Iterator<Series> it1=seriesList.iterator();
		while(it.hasNext()){
			if(first_size != it.next().size()){
				throw new IllegalArgumentException("Size of series is differing");
			}
		}
		this.Columns = index;
		this.mapSeries = new HashMap<String,Series>();
		this.TmapSeries = new HashMap<Integer,Series>();
		
		int j=0;
		while(it1.hasNext()){			 
			this.mapSeries.put((String) index.get(j), it1.next());
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
		Iterator<Series> it=seriesList.iterator();
		Iterator<Series> it1=seriesList.iterator();
		
		while(it.hasNext()){
			if(first_size != it.next().size()){
				throw new IllegalArgumentException("Size of series is differing");
			}
		}
		if (seriesList.get(0).size() != index.size()){
			throw new IllegalArgumentException("Unmatching size of index and List");
		
		}			
		while(it1.hasNext()){
			this.TmapSeries.put(j, it1.next());
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
	
	public  DataFrame ix(int[] rows){
		List<Series> row = new ArrayList<Series> ();
		for (int i=0; i< rows.length; i++){
			row.add(TmapSeries.get(rownum.get(i)));
		}
		DataFrame newdf = new DataFrame(row,Columns,true);
		return newdf;
	}

	public  Series ix(int row){
		return TmapSeries.get(rownum.get(row)).clone();
	}
	
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
	
	public Series get(String col){
		return mapSeries.get(col).clone();		
	}
	
	public void set(String col,int position,Object value){
		TmapSeries.get(rownum.get(position)).set(Columns.indexOf(col),value);
		mapSeries.get(col).set(position,value);
	}
	
	public void set(String col,Series newseries){
		if (Columns.contains(col)){
			if (TmapSeries.size() == newseries.size()){
				mapSeries.put(col, newseries);
				Iterator<Object> it=newseries.iterator();
				Iterator<Integer> it1=rownum.iterator();
				while(it.hasNext()){
					TmapSeries.get(it1.next()).set(Columns.indexOf(col),it.next());
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
				Iterator<Integer> it1=rownum.iterator();				
				for (int i=0;i<newseries.size();i++){
					TmapSeries.get(it1.next()).add(it.next());
				}
			}
			else{
				throw new IllegalArgumentException("Sizes of old and new columns are diferent");
			}			
		}		
	}
	 public List<Integer> size(){
		 List<Integer> size= new ArrayList<Integer>(); 
		 size.add(TmapSeries.size());
		 size.add(mapSeries.size());
		 return size;
	 }
	 
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
	
	
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
	
    public void sort(String column){
        int [] axis={0};
    String[] columns = {column};
        sort(columns,axis);
    }
    
    public void sort(String[] columnNames){
    	int [] axis=new int[columnNames.length];
    	for (int i=0;i<columnNames.length;i++){
    		axis[i]=0;
    	}
    	sort(columnNames,axis);
    }

    public DataFrame subset(Series s){
    	List<Series> l=new ArrayList<Series>();
    	Iterator<Object> it1=s.iterator();
    	int i=0;
    	while(it1.hasNext()){
    		if ((boolean) it1.next()){
    			l.add(TmapSeries.get(rownum.get(i)));   			
    		}
    		i++;
    	}
    	DataFrame sub=  new DataFrame(l,Columns,true);
    	return sub;
    }
    
    public DataFrame T(){
    	Iterator<Integer> it1=rownum.iterator();
    	List<Object> newcol= new ArrayList<Object>();
    	List<Series> sl= new ArrayList<Series>();
    	while(it1.hasNext()){
    		Integer next = it1.next();
    		newcol.add(next.toString());
    		sl.add(TmapSeries.get(next));    		
    	}
    	System.out.println(sl.get(0).size());
    	System.out.println(newcol.size());   	
    	DataFrame newdf= new DataFrame(sl,newcol);
    	return newdf;
    }

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return (Iterator) new SeriesIterator(this);
	}
	
	public void replace(Object x){
		for (int i=0;i<Columns.size();i++){
			Collections.replaceAll(mapSeries.get(Columns.get(i)), null, x);
		}
		for (int i=0;i<TmapSeries.size();i++){
			Collections.replaceAll(TmapSeries.get(i), null, x);
		}
	}
		
	public void replace(String col,Object x){
		Iterator<Object> it1= mapSeries.get(col).iterator();
		int i=0;
		while(it1.hasNext()){
			System.out.println(mapSeries.get(col).get(i));
			if (it1.next() == null){

				mapSeries.get(col).set(i, x);
				TmapSeries.get(rownum.get(i)).set(Columns.indexOf(col),x);
			}
			i++;
		}
	}

	public HashMap<Integer,Series> map2tmap(){
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
	
	public void drop(String[] cols){
		for (int i=0;i< cols.length;i++){
			Columns.remove(cols[i]);
			mapSeries.remove(cols[i]);
		}
		this.TmapSeries=map2tmap();
	}
	
	public List<Object> getColumns(){
		List<Object> cols =new ArrayList<Object>(this.Columns);
		return cols;
	}
	
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
		List<String> listCols =  Arrays.asList(cols);
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
				Iterator<Object> it2=b.iterator();
				Iterator<Object> it3=ls.iterator();
				while (it2.hasNext()){
					Series s2=(Series) it2.next();
					s2.add(it3.next());
				}
					
				}
			}
		Iterator it4= map.entrySet().iterator();
		List<Series>tmap2=new ArrayList<Series>();
		while(it4.hasNext()){			
			Map.Entry pairs = (Map.Entry)it4.next();
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


