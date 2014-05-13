package com.koalas.common;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.koalas.common.DataFrame;
import com.koalas.common.Series;

/**
 * Same as pandas in Python , which can perform joins,read csv operations
 * @author GokulRamesh,Sureshkrishna G
 *
 */
public class Koalas {
	/**
	 * To read a given csv file as DataFrame 
	 * @param csvfile location of csv file
	 * @param seperator default ","
	 * @return DataFrame

	 */
	@SuppressWarnings("resource")
	public static DataFrame readCSV(String csvfile,String seperator) {
		String line="";
		BufferedReader br;
		List<Object> Index = new ArrayList<Object>();
		List<Series> SeriesList = new ArrayList<Series>();
		String[] Columns = null;		
		try {
			br = new BufferedReader(new FileReader(csvfile));
			try {
				int count=0;
				while ((line = br.readLine()) != null) {
					count+=1;
					if (count==1){
						Columns= line.split(seperator);
						for (int j=0;j<Columns.length;j++){
							Index.add(Columns[j]);
						}
					}					
					else if (count !=1){
						Columns= line.split(seperator);
						ArrayList<Object> list= new ArrayList<Object>();
						for (String o:Columns){
							try  
							  {  							   
							    list.add(Float.valueOf(o)); 
							  }  
							  catch(NumberFormatException nfe)  
							  {  
									if (o.equals("")){
										list.add(null);
									}
									else{
										list.add(o);
									}									  
							  }									
					
						}
						Series eachcolumn = new Series(list);
						SeriesList.add(eachcolumn);
					}				
				}			
				/*for (int k=0; k<SeriesList.size();k++){
					if (SeriesList.get(k).size() != 6){
						System.out.println(SeriesList.get(k));
					};					
				}*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		DataFrame d= new DataFrame(SeriesList,Index,true);
		return d;	
	}
	

	
	/**
	 * To perform join operation between two DataFrames
	 * @param df1  First DataFrame
	 * @param df2  Second DataFrame
	 * @param cols Column names as string array  on which join has to be performed 
	 * @param jointype "inner","full","left","right"
	 * @return DataFrame
	 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public static DataFrame join(DataFrame  df1,DataFrame  df2,String[] cols,String jointype){
		
		boolean left;
		boolean full = false;
		boolean inner = false;
		Iterator<Series> itdf = null;
		Iterator<Series> itdf2 = null;
		
		if(jointype.equals("inner")){
			if (df1.size().get(0)>df2.size().get(0)){
				left = false;
				itdf2= df1.iterator();
				itdf= df2.iterator();
			}
			else{
				left = true;
				itdf= df1.iterator();
				itdf2= df2.iterator();				
			}
			inner = true;
		}
		else if(jointype.equals("left")){
			left = true;
			itdf= df1.iterator();
			itdf2= df2.iterator();			
		}
		else if(jointype.equals("right")){
			left = false;
			itdf2= df1.iterator();
			itdf= df2.iterator();		
		}
		else if(jointype.equals("full")){
			left = true;
			full = true;
			itdf= df1.iterator();
			itdf2= df2.iterator();		
		} 
		else{
			throw new IllegalArgumentException("Join type must be one of 'inner', 'left', 'right' or 'full'");
		}
		
		Map <List<Object>,List<Series>> map=new LinkedHashMap <List<Object>,List<Series>>();
		Map <List<Object>,List<Series>> mapfull=new LinkedHashMap <List<Object>,List<Series>>();
		List<Object> cols1= df1.getColumns();
		List<Object> cols2= df2.getColumns();
		Iterator<Object> it1 = cols1.iterator();
		Iterator<Object> it2 = cols2.iterator();
		List<Object> newcolumns= new ArrayList<Object>();
		for(String col: cols){
			newcolumns.add(col);
		}
		List<String> listCols =  Arrays.asList(cols);
		while(it1.hasNext()){
			Object nx = it1.next();
			if(listCols.indexOf(nx) == -1){
				if(cols2.indexOf(nx)>-1){
					cols2.set(cols2.indexOf(nx),nx+".y");
					nx = nx + ".x";
					newcolumns.add(nx);
				}
				else{
					newcolumns.add(nx);
				}
			}
		}
		
		while(it2.hasNext()){
			Object nx = it2.next();
			if(listCols.indexOf(nx) == -1){
				newcolumns.add(nx);
			}
		}
		

		
		while(itdf.hasNext()){
			List<Object>key=new ArrayList<Object>();
			Series ls= new Series();
			Series row = itdf.next();
			for(int j = 0; j<row.size();j++){
				if(listCols.indexOf(cols1.get(j)) > -1){
					key.add(row.get(j));
				}
				else{
					ls.add(row.get(j));
				}
			}
			if(!(map.containsKey(key))){
				List<Series>value=new ArrayList<Series>();
				value.add(ls);
				map.put(key, value);				
			}			
			else{
				map.get(key).add(ls);
			}
			
		}
		
		//System.out.println(map);
		while(itdf2.hasNext()){
			List<Object>key=new ArrayList<Object>();
			Series ls= new Series();
			Series row = itdf2.next();
			
			for(int j = 0; j<row.size();j++){
				if(listCols.indexOf(cols2.get(j)) > -1){
					key.add(row.get(j));					
				}
				else{
					ls.add(row.get(j));					
				}
			}
			
			if(!(mapfull.containsKey(key))){
				List<Series>value=new ArrayList<Series>();
				value.add(ls);
				mapfull.put(key, value);				
			}			
			else{
				mapfull.get(key).add(ls);
			}				
		}
		
			
		Iterator ita= mapfull.entrySet().iterator();
		while(ita.hasNext()){
			Map.Entry pairs = (Map.Entry)ita.next();
			List<Object> keys= (List<Object>) pairs.getKey();
			List<Series> s= new ArrayList<Series>();
			if (map.containsKey(keys)){	
				for(Series o:map.get(keys)){
					for (Series o1:mapfull.get(keys)){
						Series x= new Series(o);
						x.addAll(o1);
						s.add(x);
					}
					
				}
				map.put(keys,s);					
			}
		}
		
		if (full){
			Iterator it5= mapfull.entrySet().iterator();
			while(it5.hasNext()){
				Map.Entry pairs = (Map.Entry)it5.next();
				List<Object> keys= (List<Object>) pairs.getKey();
				List<Series> s= new ArrayList<Series>();
				if (!map.containsKey(keys)){
					for (Series o1:mapfull.get(keys)){
						Series nulllist= new Series();
						for(int j =cols2.size();j<newcolumns.size();j++,nulllist.add(null));
						nulllist.addAll(o1);
						s.add(nulllist);
					}					
					//System.out.println(j);
					map.put(keys, s);
				}
			}
		}
		
		
		Iterator it4= map.entrySet().iterator();
		List<Series> tmap= new ArrayList<Series>();
		while(it4.hasNext()){
			
			Map.Entry pairs = (Map.Entry)it4.next();
			List<Object> keys= (List<Object>) pairs.getKey();
			List<Object> values= (List<Object>) pairs.getValue();
			
			Iterator it6 = values.iterator();
			while (it6.hasNext()){
				Series row=new Series(keys);
				List<Object> val = (List<Object>) it6.next();
				//System.out.println(val);
				//System.out.println(val.size());
				//System.out.println(cols.length);
				if (!inner && full && left){
					if (val.size()+cols.length != newcolumns.size()){
						List<Object> nulllist= new ArrayList<Object>();
						for(int j =cols1.size();j<newcolumns.size();j++,nulllist.add(null));
						val.addAll(nulllist);
					}
					
				}
				if (!inner && !full){
					if (val.size()+cols.length != newcolumns.size()){
						List<Object> nulllist= new ArrayList<Object>();
						for(int j =cols1.size();j<newcolumns.size();j++,nulllist.add(null));
						val.addAll(nulllist);
					}
										
				}
				if (val.size()+cols.length == newcolumns.size()){
					row.addAll(val);
					tmap.add(row);	
				}
			}			
		}

		DataFrame df3= new DataFrame(tmap,newcolumns,true);
	return df3;
		

	}
	
}
