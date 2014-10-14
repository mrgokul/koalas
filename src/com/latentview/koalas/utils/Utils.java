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


import java.util.Collections;
import java.util.List;

import com.latentview.koalas.common.Series;

public  class Utils {
	// quicksort a[left] to a[right]
			public static void quicksort(Series a, List<Integer> index, int left, int right, List<Series> sorts,int sorttype) {

			    if (right <= left){
			    	return;
			    }
			    int i = partition(a, index, left, right, sorts,sorttype);
			    quicksort(a, index, left, i-1, sorts, sorttype);
			    quicksort(a, index, i+1, right, sorts, sorttype);
			}

			// partition a[left] to a[right], assumes left < right
			private static int partition(Series a, List<Integer> index, 
			int left, int right, List<Series> sorts,int sorttype) {
				if (sorttype ==1){
			    int i = left - 1;
			    int j = right;
			     while (true) {
			        while (more(a.get(++i), a.get(right))){	    // find item on left to swap
			        }
			                                            // a[right] acts as sentinel
			        while (more(a.get(right), a.get(--j))){
			        	if (j == left) break;           // find item on right to swap
			        }
			                                             // don't go out-of-bounds
			        if (i >= j) break;                  // check if pointers cross
			        exch(a, index, i, j, sorts);               // swap two elements into place
			    }
			    exch(a, index, i, right,  sorts);               // swap with partition element
			    return i;
			}
			
				else {
				    int i = left - 1;
				    int j = right;
				     while (true) {
				        while (less(a.get(++i), a.get(right))){	    // find item on left to swap
				        }
				                                            // a[right] acts as sentinel
				        while (less(a.get(right), a.get(--j))){
				        	if (j == left) break;           // find item on right to swap
				        }
				                                             // don't go out-of-bounds
				        if (i >= j) break;                  // check if pointers cross
				        exch(a, index, i, j, sorts);               // swap two elements into place
				    }
				    exch(a, index, i, right,  sorts);               // swap with partition element
				    return i;
					}
			}

			// is x < y ?
			@SuppressWarnings({ "unchecked", "rawtypes" })
			private static boolean less(Object x, Object y) {
				if (x==null){
					return false;
				}
				if (y==null){
					return true;
				}
				if(x.getClass().getName().equals("java.lang.String")){
					return ((Comparable) x.toString().toLowerCase()).compareTo((Comparable) y.toString().toLowerCase()) < 0;
					}
				else{				
					return ((Comparable) x).compareTo((Comparable) y) < 0;
				}
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			private static boolean more(Object x, Object y) {
				if(x.getClass().getName().equals("java.lang.String")){
					return ((Comparable) x.toString().toLowerCase()).compareTo((Comparable) y.toString().toLowerCase()) > 0;
					}
				else{
					return ((Comparable) x).compareTo((Comparable) y) > 0;
				}
			}

			// exchange a[i] and a[j]
			private static void exch(Series a,List<Integer> index, int i, int j, List<Series> sorts) {
				Collections.swap(a,i,j);
		        Collections.swap(index,i,j);
				for(int k = 0; k<sorts.size(); k++){
					Collections.swap(sorts.get(k),i,j);			    
				}
				
				
			}

}

