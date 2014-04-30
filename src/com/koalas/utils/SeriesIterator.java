package com.koalas.utils;


import java.util.Iterator;

import com.koalas.common.Series;
import com.koalas.common.DataFrame;;

public class SeriesIterator implements Iterator{
	
	private DataFrame df;
	private int position=0;
	
	
	public SeriesIterator(DataFrame df){
		this.df=df;
	}
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		if (position < df.size().get(0)){
			return true;
		}
		else{
			return false;
		}
		
	}

	@Override
	public Series next() {
		// TODO Auto-generated method stub
		Series s=df.ix(position);
		position++;
		return s;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
	

}

