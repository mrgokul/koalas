package com.koalas.common;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;


import com.koalas.utils.Utils;

public class Series extends ArrayList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Series(List<Object> a) {	
		super(a);
		// TODO Auto-generated constructor stub
	}
	
	public Series() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	 public static Series from(Object... objects) {
         Series s = new Series();
         
         for(Object obj : objects){
                 s.add(obj);
         }
         return s;
	 }
	 

	
	public boolean add(Object o) {
	        return super.add(o);
	    }
	
	public String toString(){
		return super.toString();
		
	}
	
	public Series clone(){
		return (Series) super.clone();
	}
	
	public Series slice(int a, int b){
		return new Series(super.subList(a,b));
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort(){
		 Collections.sort((List) this);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void reverse(){
		 this.sort();
		 Collections.reverse((List) this);
	}
	public   Float sum(){
		 int length=this.size();
		 Float first = Float.valueOf(this.get(0).toString());
		 for (int i=1;i<length;i++){
			 first = first +  Float.valueOf(this.get(i).toString());
		 }
		 return first;
		 }
	public Float mean(){
		return this.sum()/this.size();
	}
	
	
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
		Iterator<Object> it = clone.iterator();
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
	  
	  public Float median(){
	          Float[] prob = {0.5f};
	          return quantile(prob).get(0.5f);
	  }
	  
	  public Map<Float,Float> summary(){
	          Float[] prob = {0.0f,0.25f,0.5f,0.75f,1.0f};
	          return quantile(prob);
	  }
	
	public Series unique(){
		Series uniq= new Series();
		Iterator<Object> iterate=this.iterator();
		while(iterate.hasNext()){
			Object next= iterate.next();
			if(!(uniq.contains(next))){
			uniq.add(next);
			}
		}
		return uniq;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object max(){
		return Collections.max((List) this);
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object min(){
		return Collections.min((List) this);
		
	}
	
	public Series eq(Object obj){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(((Comparable) it.next()).compareTo((Comparable) obj) == 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	public Series neq(Object obj){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(((Comparable) it.next()).compareTo((Comparable) obj) == 0){
				bool.add(false);
			}
			else{
				bool.add(true);
			}
		}
		return bool;		
	}
	
	public Series gt(Object obj){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(((Comparable) it.next()).compareTo((Comparable) obj) > 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	public Series gte(Object obj){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(((Comparable) it.next()).compareTo((Comparable) obj) >= 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	public Series lt(Object obj){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(((Comparable) it.next()).compareTo((Comparable) obj) < 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	public Series lte(Object obj){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(((Comparable) it.next()).compareTo((Comparable) obj) <= 0){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}

	public Series in(List<Object> arr){
		Series bool= new Series();
		Iterator<Object> it=this.iterator();
		while(it.hasNext()){
			if(arr.indexOf(it.next()) > -1){
				bool.add(true);
			}
			else{
				bool.add(false);
			}
		}
		return bool;		
	}
	
	public Series and(Series s){
		Series bool= new Series();
		if (!(this.size()== s.size())){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else{
			Iterator<Object> it1=this.iterator();
			Iterator<Object> it2=s.iterator();
			while(it1.hasNext()){
				bool.add((Boolean)it1.next() & (Boolean)it2.next());
			}
			return bool;
		}
	}
	
	public Series or(Series s){
		Series bool= new Series();
		if (!(this.size()== s.size())){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else{
		Iterator<Object> it1=this.iterator();
		Iterator<Object> it2=s.iterator();
		while(it1.hasNext()){
			bool.add((Boolean)it1.next() | (Boolean)it2.next());
		}
		return bool;
		}
	}
	
	public Series not(){
		Series bool= new Series();
		Iterator<Object> it1=this.iterator();
		while(it1.hasNext()){
			bool.add(!(Boolean)it1.next() );
		}
		return bool;
	}

	public Series plus(Series s){
		Series n= new Series();
		Iterator<Object> it1=this.iterator();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			while(it1.hasNext()){
				n.add(Float.valueOf(it1.next().toString())+Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			while(it1.hasNext()){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(it1.next().toString())+Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	

	
	public Series multiply(Series s){
		Series n= new Series();
		Iterator<Object> it1=this.iterator();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			while(it1.hasNext()){
				n.add(Float.valueOf(it1.next().toString())*Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			while(it1.hasNext()){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(it1.next().toString())*Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	public Series minus(Series s){
		Series n= new Series();
		Iterator<Object> it1=this.iterator();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			while(it1.hasNext()){
				n.add(Float.valueOf(it1.next().toString())-Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			while(it1.hasNext()){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(it1.next().toString())-Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	public Series divide(Series s){
		Series n= new Series();
		Iterator<Object> it1=this.iterator();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			while(it1.hasNext()){
				n.add(Float.valueOf(it1.next().toString())/Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			while(it1.hasNext()){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(it1.next().toString())/Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	public Series modulus(Series s){
		Series n= new Series();
		Iterator<Object> it1=this.iterator();
		Iterator<Object> it2=s.iterator();
		if (!(this.size()== s.size() || this.size() % s.size() ==0)){
			throw new IllegalArgumentException("Series sizes are diferent");
		}
		else if (this.size()== s.size()) {
			while(it1.hasNext()){
				n.add(Float.valueOf(it1.next().toString())%Float.valueOf(it2.next().toString()));
			}
		}
		else {
			int i=0;
			while(it1.hasNext()){
				if( i % s.size()  == 0){
					it2=s.iterator();
				}
				n.add(Float.valueOf(it1.next().toString())%Float.valueOf(it2.next().toString()));
				i++;
			}	
		}
		return n;		
	}
	
	public void replace(Object x){
		Collections.replaceAll(this, null, x);
		}
	
	public Series duplicate(){
		Series bool= new Series();
		Series check = new Series();
		Iterator<Object> it1=this.iterator();
		Object it;
		while(it1.hasNext()){
			it=it1.next();
			if (check.contains(it)){
				bool.add(true);
			}
			else{
				check.add(it);
				bool.add(false);
			}
		}
		return bool;
	}
	
	public void append(Series s){
		Iterator<Object> it1= s.iterator();
		while(it1.hasNext()){
			this.add(it1.next());
		}
	}
	
	
	public float var(){
		return (float) Math.pow(this.sd(),2);
	}
	
	public Object percentile(int p){
		Series clone = this.clone();
		clone.sort();
		Float rank=(float) (((float)p/100)*(this.size()+1));
		if (rank-Math.floor(rank)==0){
			return clone.get((int) Math.floor(rank-1));
		}
		else{
			int rank1=(int) Math.floor(rank);
			return (rank-Math.floor(rank1))*
					(Float.valueOf(clone.get(rank1).toString())-Float.valueOf(clone.get(rank1-1).toString()))+
					Float.valueOf(clone.get(rank1-1).toString());
		}
		
	}
	
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
	
	public Series cummax(){
		Iterator<Object> it1= this.iterator();
		Series cum = new Series();
		int i=0;
		while(it1.hasNext()){
			Float max=Float.valueOf(it1.next().toString());
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
	
	public Series cummin(){
		Iterator<Object> it1= this.iterator();
		Series cum = new Series();
		int i=0;
		while(it1.hasNext()){
			Float max=Float.valueOf(it1.next().toString());
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
	
	
	public Series cumprod(){
		Iterator<Object> it1= this.iterator();
		Series cum = new Series();
		int i=0;
		while(it1.hasNext()){
			Float curr=Float.valueOf(it1.next().toString());
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
	
	public Series cumsum(){
		Iterator<Object> it1= this.iterator();
		Series cum = new Series();
		int i=0;
		while(it1.hasNext()){
			Float curr=Float.valueOf(it1.next().toString());
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
	
	public int argmax(){
		Series uniq=this.unique();
		Map <Object,List<Integer>> map =new HashMap <Object,List<Integer>>();
		Iterator<Object> it1= uniq.iterator();
		Iterator<Object> it2= this.iterator();
		while(it1.hasNext()){
			List<Integer>f=new ArrayList<Integer>();
			map.put(it1.next(),f );
		}
		int i=0;
		while(it2.hasNext()){
			map.get(it2.next()).add(i);
			i++;
		}
		return Collections.min(map.get(this.max()));
	}
	public int argmin(){
		Series uniq=this.unique();
		Map <Object,List<Integer>> map =new HashMap <Object,List<Integer>>();
		Iterator<Object> it1= uniq.iterator();
		Iterator<Object> it2= this.iterator();
		while(it1.hasNext()){
			List<Integer>f=new ArrayList<Integer>();
			map.put(it1.next(),f );
		}
		int i=0;
		while(it2.hasNext()){
			map.get(it2.next()).add(i);
			i++;
		}
		return Collections.min(map.get(this.min()));
	}
	
	public List<Integer> argsort(){
		Series uniq=this.unique();
		List<Integer> args=new ArrayList<Integer>();
		Map <Object,List<Integer>> map =new HashMap <Object,List<Integer>>();
		Iterator<Object> it1= uniq.iterator();	
		Iterator<Object> it2= this.iterator();
		while(it1.hasNext()){
			List<Integer>f=new ArrayList<Integer>();
			map.put(it1.next(),f );
		}
		int i=0;
		while(it2.hasNext()){
			map.get(it2.next()).add(i);
			i++;
		}
		uniq.sort();
		Iterator<Object> it3= uniq.iterator();
		while(it3.hasNext()){
			List<Integer>f2=map.get(it3.next());
			Collections.sort(f2);
			args.addAll(f2);
		}
		return args;
	}
}

	



