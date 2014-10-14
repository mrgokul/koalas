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


package com.latentview.koalas.utils;


import java.util.Iterator;

import com.latentview.koalas.common.Series;
import com.latentview.koalas.common.DataFrame;

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

