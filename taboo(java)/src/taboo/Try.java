package taboo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator; 
import java.util.ListIterator; 
public class Try{
	static int[] city_list= {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
	static int[] solutionnew=city_list.clone();
	static int lenth=17;
	static int[][] df = new int[17][17];
	static int loc1,loc2,temp;
	static ArrayList<int[]> taboo_list = new ArrayList<>();// 禁忌表
	void Lead() {
		LeadIn abc = new LeadIn();
		df = abc.Lead();
	}
	static int[] create_new() {
		int loc1, loc2, temp;
		int[] zu=new int[2];
		do {
			do {
				loc1 = (int) (Math.random() * (solutionnew.length ));
				loc2 = (int) (Math.random() * (solutionnew.length ));
			} while (loc1 == loc2);
			if (loc1 > loc2) {
				temp = loc1;
				loc1 = loc2;
				loc2 = temp;
				zu[0]= loc1;
				zu[1]=loc2;
			} // 使loc1<loc2
		}while(!(taboo_list.contains(zu)));
		temp = city_list[loc1];
		city_list[loc1] = city_list[loc2];
		city_list[loc2] = temp;
		int[] move = { loc1, loc2 };
		return move;
	}
	static int[] random_first_full_road() {// #随机生成初始线路
		int[] route =new int[lenth-1];
		for(int i=0;i<route.length;i++) {
			route[i]= city_list[i+1];
		}//route={1  2  3  4  5  6  7  8  9  10  11  12  13  14  15  16  }
		ArrayList<Integer> cities = new ArrayList<>();
		for (int i = 0; i < route.length; i++) {
			cities.add(route[i]);
		}
		Collections.shuffle(cities);// 用来打乱元素排列顺序
		Integer[] routefake = cities.toArray(new Integer[cities.size()]);
		for (int i = 0; i < routefake.length; i++) {
			route[i] = routefake[i];
		}
		return route;
	}
//	static ArrayList<int[]> result1 = new ArrayList<>();
	public static void main(String[] args) {
//		TabooSearch obj=new TabooSearch();
		int[] zu=new int[2];
		do {
			do {
				loc1 = (int) (Math.random() * (solutionnew.length ));
				loc2 = (int) (Math.random() * (solutionnew.length ));
			} while (loc1 == loc2);
			if (loc1 > loc2) {
				temp = loc1;
				loc1 = loc2;
				loc2 = temp;
				zu[0]= loc1;
				zu[1]=loc2;
			} // 使loc1<loc2
		}while(taboo_list.contains(zu));
		System.out.println(loc1+"  "+loc2);
}
}
	
	
//for(int i=0;i<min_candidate.length;i++) {
//	System.out.print(min_candidate[i]+"  ");
//}
//System.out.println("  ");
