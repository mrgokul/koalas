/*
 * Copyright 2014 Gokul Ramesh <gokul.q3a@gmail.com>
 * Copyright 2014 Sureshkrishna Gonugunta <gsuresh92@gmail.com>
 * Copyright 2014 LatentView Analytics, Inc.
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



package com.latentview.koalas.common;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.latentview.koalas.utils.Utils;

/**
 * One-dimensional ndarray
 * @author GokulRamesh,Sureshkrishna
 *
 */
public class Series extends ArrayList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This converts a List of objects to Series on a condition that all
	 * objects are of same data type
	 * @param a List of Objects
	 */
	public Series(List<Object> a) {	
		super(a);
		// TODO Auto-generated constructor stub
	}

	public Series() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Easy way to create a Series
	 * @param objects List of Objects separated by comma 
	 * @return Series formed by given objects
	 */
	 public static Series from(Object... objects) {
         Series s = new Series();
         
         for(Object obj : objects){
                 s.add(obj);
         }
         return s;
	 }
	 
	 /**
	  * Appending  a Object to end of the Series 
	  * @param o Object that has to be appended to Series 
	  */
	public boolean add(Object o) {
	        return super.add(o);
	    }
	/**
	 * To output the Series
	 */
	public String toString(){
		return super.toString();
		
	}
	/**
	 * Cloning the Series
	 */
	public Series clone(){
		return (Series) super.clone();
	}
	/**
	 * Slicing a Series provided starting and ending indexes
	 * @param a Starting Index
	 * @param b Ending Index
	 * @return Series
	 */
	public Series slice(int a, int b){
		return new Series(super.subList(a,b));
	}
	
	/**
	 * Sorts the series in ascending order
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort(){
		 Collections.sort((List) this);
	}
	/**
	 * Sorts the series in descending order
	 */
	@SuppressWarnings({ "rawtypes" })
	public void reverse(){
		 this.sort();
		 Collections.reverse((List) this);
	}
	/**
	 * This method can be used to find sum of all elements in a series
	 * @return Float which is the sum of all elements
	 */
	public   Float sum(){
		 int length=this.size();
		 Float first = Float.valueOf(this.get(0).toString());
		 for (int i=1;i<length;i++){
			 first = first +  Float.valueOf(this.get(i).toString());
		 }
		 return first;
		 }
	/**
	 * This method can be used to find mean of all elements in a series
	 * @return Float which is the average of elements in series
	 */	
	public Float mean(){
		return this.sum()/this.size();
	}
	
	/**
	 * This method can be used to find Standard deviation of all elements in a series
	 * @return Float which is the Standard deviation of elements in series
	 */	
	public Float sd(){
		int length=this.size();
		Double sum = (double) 0;
		Float mean =mean();
		for (int i=0;i<length;i++){
			sum += Math.pow((Float.valueOf(this.get(i).toString())-mean),2);
		}
		return (float) Math.sqrt( sum / ( length - 1 ) ); 		
	}

	
	public Map<Float,Float> quantile(Float[] a){
        Arrays.sort(a);
        if(a[a.length-1] > 1.0f){
                throw new IllegalArgumentException("Probability values cannot be greater than 1");
        }
        Series clone = this.clone();
        clone.sort();
        //Float perc = 0.0f/clone.size();
		int i = 0;
		int j = 0;
		Map<Float,Float> out = new LinkedHashMap<Float,Float>();
		while(i < a.length){
		        Float p = a[i];
		        Float perc = (float) (j/((clone.size()-1)*1.0));
		        while(perc > p & i < a.length){
		                p = a[i];
		                
		        Float prev = (float) ((j-1)/((clone.size()-1)*1.0));
		        if(perc > 1.0){
		                out.put(p,Float.valueOf(clone.get(j-1).toString()));
		        }
		        else{
		                Float q = ((perc-p) * Float.valueOf(clone.get(j-1).toString()) +
		                                    (p-prev) *Float.valueOf(clone.get(j).toString()))/(perc-prev);
		                out.put(p, q);
		                
		        }
		                i++;
		        }
		        j++;
		}
		return out;
	}
	
	  public Float quantile(Float a){
          Float[] prob = {a};
          return quantile(prob).get(a);
	  }
		/**
		 * This method can be used to find median of all elements in a series
		 * @return Float which is the median of elements in series
		 */	
	  public Float median(){
	          Float[] prob = {0.5f};
	          return quantile(prob).get(0.5f);
	  }
	  
	  public Map<Float,Float> summary(){
	          Float[] prob = {0.0f,0.25f,0.5f,0.75f,1.0f};
	          return quantile(prob);
	  }
	  
	  /**
	   * To find the unique elements 
	   * @return Series of unique elements
	   */
	public Series unique(){
		Series uniq= new Series();
		for (Object o:this){
			if(!(uniq.contains(o))){
			uniq.add(o);
			}
		}
		return uniq;
	}
	
	  /**
	   * To find the maximum of  elements 
	   * @return maximum of  elements 
	   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object max(){
		return Collections.max((List) this);
	}
	  /**
	   * To find the minimum of  elements 
	   * @return minimum of  elements 
	   */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object min(){
		return Collections.min((List) this);
		
	}
	  /**
	   * To find the elements which are equal to given Object
	   * @param obj Object for which the '=' condition has to be checked  
	   * @return Series of booleans
	   */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series eq(Object obj){
		Series bool= new Series();
		for (Object o:this){
			if(((Comparable) o).compareTo((Comparable) obj) == 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	  /**
	   * To find the elements which are not equal to given Object
	   * @param obj Object for which the '!=' condition has to be checked  
	   * @return Series of booleans
	   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series neq(Object obj){
		Series bool= new Series();
		for (Object o:this){
			if(((Comparable) o).compareTo((Comparable) obj) == 0){
				bool.add(false);
			}
			else{
				bool.add(true);
			}
		}
		return bool;		
	}
	
	  /**
	   * To find the elements which are greater than given Object
	   * @param obj Object for which the '>' condition has to be checked  
	   * @return Series of booleans
	   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series gt(Object obj){
		Series bool= new Series();
		for (Object o:this){
			if(((Comparable) o).compareTo((Comparable) obj) > 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	  /**
	   * To find the elements which are greater than or equal  to given Object
	   * @param obj Object for which the '>=' condition has to be checked  
	   * @return Series of booleans
	   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series gte(Object obj){
		Series bool= new Series();
		for (Object o:this){
			if(((Comparable) o).compareTo((Comparable) obj) >= 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	  /**
	   * To find the elements which are less than given Object
	   * @param obj Object for which the '<' condition has to be checked  
	   * @return Series of booleans
	   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series lt(Object obj){
		Series bool= new Series();
		for (Object o:this){
			if(((Comparable) o).compareTo((Comparable) obj) < 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	  /**
	   * To find the elements which are less than or equal  to given Object
	   * @param obj Object for which the '<=' condition has to be checked  
	   * @return Series of booleans
	   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series lte(Object obj){
		Series bool= new Series();
		for (Object o:this){
			if(((Comparable) o).compareTo((Comparable) obj) <= 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	/**
	 * To find whether any of objects in given list are in the series
	 * @param arr List of Objects
	 * @return Series of booleans of same length
	 */
	public Series in(List<Object> arr){
		Series bool= new Series();
		for (Object o:this){
			if(arr.indexOf(o) > -1){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	/**
	 * To find the 'and' condition between two Series of booleans
	 * (between same level elements) 
	 * @param s Series of booleans
	 * @return Series of booleans
	 */
	public Series and(Series s){
		Series bool= new Series();
		if (!(this.size()== s.size())){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else{
			Iterator<Object> it=s.iterator();
			for (Object o:this){
				bool.add((Boolean)o & (Boolean) it.next());
			}
			return bool;
		}
	}
	/**
	 * To find the 'or' condition between two Series of booleans
	 * (between same level elements) 
	 * @param s Series of booleans
	 * @return Series of booleans
	 */
	public Series or(Series s){
		Series bool= new Series();
		if (!(this.size()== s.size())){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else{
		Iterator<Object> it=s.iterator();
		for (Object o:this){
			bool.add((Boolean)o | (Boolean)it.next());
		}
		return bool;
		}
	}
	/**
	 * To find opposite of each booleans for a Series of booleans 
	 * @return Series of booleans
	 */
	public Series not(){
		Series bool= new Series();
		for (Object o:this){
			bool.add(!(Boolean)o );
		}
		return bool;
	}
	/**
	 * Sum of two series(sum of elements which are at same level) 
	 * @param s Series to which the current series to be added
	 * @return Series obtained after adding two series
	 */
	public Series plus(Series s){
		Series n= new Series();
		Iterator<Object> it=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			for (Object o:this){
				n.add(Float.valueOf(o.toString())+Float.valueOf(it.next().toString()));
			}
		}
		else {
			int i=0;
			for (Object o:this){
				if( i % s.size()  == 0){
					it=s.iterator();
				}
				n.add(Float.valueOf(o.toString())+Float.valueOf(it.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	

	/**
	 * Multiplication of two series(Multiplication of elements which are at same level)
	 * @param s Series to which the current series to be multiplied
	 * @return Series obtained after Multiplication of two series
	 */
	public Series multiply(Series s){
		Series n= new Series();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			for (Object o:this){
				n.add(Float.valueOf(o.toString())*Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			for (Object o:this){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(o.toString())*Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	/**
	 * Subtraction of two series(Subtraction of elements which are at same level) Current Series- new Series
	 * @param s Series by  which the current series to be Subtracted
	 * @return Series obtained after Subtraction of two series
	 */
	public Series minus(Series s){
		Series n= new Series();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			for (Object o:this){
				n.add(Float.valueOf(o.toString())-Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			for (Object o:this){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(o.toString())-Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	/**
	 * Division of two series(Division of elements which are at same level) Current Series / new Series
	 * @param s Series by  which the current series to be divided
	 * @return Series obtained after Division of  two series
	 */
	public Series divide(Series s){
		Series n= new Series();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			for (Object o:this){
				n.add(Float.valueOf(o.toString())/Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			for (Object o:this){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(o.toString())/Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	/**
	 * Modulo of two series(modulus of elements which are at same level) Current Series % new series
	 * @param s Series 
	 * @return Series obtained after Modulo of two series
	 */
	public Series modulus(Series s){
		Series n= new Series();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			for (Object o:this){
				n.add(Float.valueOf(o.toString())%Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			for (Object o:this){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(o.toString())%Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	/**
	 * To replace all null's in series with a given object
	 * @param x Object which replaces the nulls
	 */
	public void replace(Object x){
		Collections.replaceAll(this, null, x);
		}
	/**
	 * To know whether there are any duplicates in a series, true for first occurrence  and 
	 * false for second occurrence onwards 
	 * @return Series of  Booleans
	 */
	public Series duplicate(){
		Series bool= new Series();
		Series check = new Series();
		for (Object o:this){
			if (check.contains(o)){
				bool.add(true);
			}
			else{
				check.add(o);
				bool.add(false);
			}
		}
		return bool;
	}
	
	/**
	 * To append two series
	 * @param s Series which has to be appended to current series
	 */
	public void append(Series s){
		for (Object o:s){
			this.add(o);
		}
	}
	
	/**
	 * To find variance of elements in series
	 * @return Float which is the variance 
	 */
	public float var(){
		return (float) Math.pow(this.sd(),2);
	}
	

	/**
	 * To find the Covariance between two series   
	 * @param s Series 
	 * @return FLoat which is the cov between two series 
	 */
	public Float cov(Series s){
		if(!(this.size()== s.size())){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else{
			int length=this.size();
			System.out.println(this.multiply(s).sum()-((float)length*this.mean()*s.mean()));
			return ((float)1/(length-1))*(this.multiply(s).sum()-((float)length*this.mean()*s.mean()));
		}
	}
	
	/**
	 * To find the cumulative max  of a series 
	 * @return Series with cumulative maximum at each level
	 */
	public Series cummax(){
		Series cum = new Series();
		int i=0;
		for (Object o:this){
			Float max=Float.valueOf(o.toString());
			System.out.println(cum);
			if (i==0){
				cum.add(this.get(i));
			}
			else{
				if (Float.valueOf(cum.get(i-1).toString()) > max){
					cum.add(cum.get(i-1));
				}
				else{
					cum.add(this.get(i));
				}
			}
			i++;
		}
		return cum;
	}
	
	/**
	 * To find the cumulative min  of a series 
	 * @return Series with cumulative minimum at each level
	 */
	public Series cummin(){
		Series cum = new Series();
		int i=0;
		for (Object o:this){
			Float max=Float.valueOf(o.toString());
			System.out.println(cum);
			if (i==0){
				cum.add(this.get(i));
			}
			else{
				if (Float.valueOf(cum.get(i-1).toString()) < max){
					cum.add(cum.get(i-1));
				}
				else{
					cum.add(this.get(i));
				}
			}
			i++;
		}
		return cum;
	}
	
	
	/**
	 * To find the cumulative product  of a series 
	 * @return Series with cumulative product at each level
	 */
	public Series cumprod(){
		Series cum = new Series();
		int i=0;
		for (Object o:this){
			Float curr=Float.valueOf(o.toString());
			if (i==0){
				cum.add(this.get(i));
			}
			else{
					cum.add(Float.valueOf(cum.get(i-1).toString())*curr);
				}
			i++;
		}		
		return cum;
	}
	
	/**
	 * To find the cumulative sum  of a series 
	 * @return Series with cumulative sum at each level
	 */
	public Series cumsum(){
		Series cum = new Series();
		int i=0;
		for (Object o:this){
			Float curr=Float.valueOf(o.toString());
			if (i==0){
				cum.add(this.get(i));
			}
			else{
					cum.add(Float.valueOf(cum.get(i-1).toString())+curr);
				}
			i++;
		}		
		return cum;
	}
	
	@SuppressWarnings("rawtypes")
	public Series cut(Float[] breaks){
        if(breaks.length < 2){
                throw new IllegalArgumentException("Invalid number of Intervals");
        }
        Arrays.sort(breaks);
        Float curr = breaks[0];
        for(int j = 1; j<breaks.length;j++){
                if(breaks[j] == curr){
                        throw new IllegalArgumentException("Breaks have to be unique");
                }
                curr = breaks[j];
        }
        List<Integer> s = new ArrayList<Integer>();
        Series clone = this.clone();
        for(int i = 0; i < clone.size(); i++, s.add(i));
        List<Series> sorts= new ArrayList<Series>();
        Utils.quicksort(clone, s, 0, s.size()-1, sorts,0);
        boolean first = true;
        Series out = new Series();
        Iterator it = clone.iterator();
        Float fv = breaks[0];
        Float sf = Float.valueOf(it.next().toString());
        Float nv = null;
        for(int k =1; k<breaks.length;k++){
        	nv = breaks[k];
            if(sf>nv){
                    fv = nv;
                    first = false;
                    continue;        
            }
            if(first){
                    while(sf < fv & it.hasNext()){
                            out.add(null);
                            sf = Float.valueOf(it.next().toString());
                            first = false;
                    }
                    while(sf < nv & it.hasNext()){
                    	if(first){
                            out.add("["+fv+"-"+nv+"]");
                    	}
                    	else{
                    		out.add("("+fv+"-"+nv+"]");
                    	}
                            sf = Float.valueOf(it.next().toString());
                    }
            }
            else{
                    while(sf <= nv & it.hasNext()){
                            out.add("("+fv+"-"+nv+"]");
                            sf = Float.valueOf(it.next().toString());
                            
                    }
            }
            fv = nv;
            first = false;                      
		    }
		    if(sf>nv){
		            out.add(null);
		    }
		    else{
		        out.add("("+fv+"-"+nv+"]");
			}
			while(it.hasNext()){
			        out.add(null);
			        it.next();
			}
			sorts.add(out);
			Series intseries = new Series();
			intseries.addAll(s);
			Utils.quicksort(intseries, s, 0, s.size()-1, sorts, 0);
			return sorts.get(0);
	}
	
	/**
	 * To find the index of first occurrence of maximum of series 
	 * @return integer which is the index of the maximum value
	 */
	public int argmax(){
		Series uniq=this.unique();
		Map <Object,List<Integer>> map =new HashMap <Object,List<Integer>>();
		for (Object o:uniq){
			List<Integer>f=new ArrayList<Integer>();
			map.put(o,f );
		}
		int i=0;
		for (Object o:this){
			map.get(o).add(i);
			i++;
		}
		return Collections.min(map.get(this.max()));
	}
	/**
	 * To find the index of first occurrence of minimum of series 
	 * @return integer which is the index of the minimum value
	 */
	public int argmin(){
		Series uniq=this.unique();
		Map <Object,List<Integer>> map =new HashMap <Object,List<Integer>>();
		for(Object o:uniq){
			List<Integer>f=new ArrayList<Integer>();
			map.put(o,f );
		}
		int i=0;
		for(Object o:this){
			map.get(o).add(i);
			i++;
		}
		return Collections.min(map.get(this.min()));
	}
	
	/**
	 * To find the index after sorting a series 
	 * @return List of integers which contains the index after sorting
	 */
	public List<Integer> argsort(){
		Series uniq=this.unique();
		List<Integer> args=new ArrayList<Integer>();
		Map <Object,List<Integer>> map =new HashMap <Object,List<Integer>>();
		for(Object o:uniq){
			List<Integer>f=new ArrayList<Integer>();
			map.put(o,f );
		}
		int i=0;
		for(Object o:this){
			map.get(o).add(i);
			i++;
		}
		uniq.sort();
		for(Object o:uniq){
			List<Integer>f2=map.get(o);
			Collections.sort(f2);
			args.addAll(f2);
		}
		return args;
	}
}

	



