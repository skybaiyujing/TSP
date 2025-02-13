package taboo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class TabooSearch {
	int[] city_list = {0,11,10,12,15,7,13,14,16,8,5,6,9,2,1,3,4};
	int lenth=city_list.length;
//	int solutionnew[] = random_first_full_road();
//	int solutionnew[] = greedy_first_full_road();
	int solutionnew[] = city_list.clone();
	int[] solutionbest=new int[solutionnew.length];
	int valuenew = route_cost(solutionnew);// 新解距离
	int valuebest = valuenew;
	int candidate_count = 1000;// 候选集合长度
	int taboo_list_length = 10;// 禁忌表的最大长度
	int iteration_count = 100;// 迭代次数
	static int[][] df = new int[17][17];//距离矩阵
	ArrayList<int[]> taboo_list = new ArrayList<>();// 禁忌表
	ArrayList<Integer> record = new ArrayList<>();// 记录每次迭代中候选表的最优解
	void Lead() {
		LeadIn abc = new LeadIn();
		df = abc.Lead();
	}

	int[] next_shotest_road(int city1, int[] other_cities) { // city1为当前节点， other_cities为还没去过的节点
		int tmp_min = 999999;
		int tmp_next = 0;
		Lead();
		for (int i = 0; i < other_cities.length; i++) {// 找到邻居城市中距离最短的一个
			if (df[city1][other_cities[i]] < tmp_min) {
				tmp_min = df[city1][other_cities[i]];// 到该城市的距离
				tmp_next = other_cities[i];// 该城市编号
			}
		}
		int[] next = { tmp_next, tmp_min };// next[0]为该城市编号next[1]为距离
		return next;
	}

	int[] random_first_full_road() {// #随机生成初始线路
		ArrayList<Integer> cities = new ArrayList<>();
		for (int i = 0; i < city_list.length; i++) {
			cities.add(city_list[i]);
		}
		Collections.shuffle(cities);// 用来打乱元素排列顺序
		Integer[] routefake = cities.toArray(new Integer[cities.size()]);
		int[] route = new int[routefake.length];
		for (int i = 0; i < routefake.length; i++) {
			route[i] = routefake[i];
		}
		return route;
	}

	int[] greedy_first_full_road() {
		ArrayList<Integer> remain_city = new ArrayList<>();
		ArrayList<Integer> road_list = new ArrayList<>();
		int current_city = city_list[0];
		road_list.add(current_city);
		for (int i = 1; i < city_list.length; i++) {
			remain_city.add(city_list[i]);
		}
		while (remain_city.size() > 0) {
			Integer[] fake = remain_city.toArray(new Integer[remain_city.size()]);
			int[] faker = new int[fake.length];
			for (int i = 0; i < faker.length; i++) {
				faker[i] = fake[i];
			}
			int[] next = next_shotest_road(current_city, faker);
			int next_city = next[0];
			road_list.add(next_city);
			remain_city.remove(remain_city.indexOf(next_city));
			current_city = next_city;
		}
		Integer[] r = road_list.toArray(new Integer[road_list.size()]);
		int[] route = new int[r.length];
		for (int i = 0; i < route.length; i++) {
			route[i] = r[i];
		}
		return route;
	}

	int route_cost(int[] route) {// 获取该线路的距离长度
		Lead();
		int all_distance = 0;
		for (int i = 0; i < route.length - 1; i++) {
			all_distance += df[route[i]][route[i + 1]];
		}
		all_distance += df[route[0]][route[route.length - 1]];
		return all_distance;
	}

	int[] create_new(int[] route) {
		int loc1, loc2, temp;
		do {
				loc1 = (int) (Math.random() * (route.length ));
				loc2 = (int) (Math.random() * (route.length ));
			} while (loc1 == loc2);
			if (loc1 > loc2) {temp = loc1;loc1 = loc2;loc2 = temp;} // 使loc1<loc2
		temp = route[loc1];
		route[loc1] = route[loc2];
		route[loc2] = temp;
		int[] move = new int[route.length + 2];
		for (int i = 0; i < route.length; i++) 
			move[i] = route[i];
		move[route.length] = loc1;
		move[route.length + 1] = loc2;
		return move;
	}
//	for (int i = 0; i < (loc2 - loc1) / 2; i++) {
//	temp = route[loc1 + i];
//	route[loc1 + i] = route[loc2 - i];
//	route[loc2 - i] = temp;
//}
	int[] single_search(int[] route) {
		ArrayList<int[]> candidate_list = new ArrayList<>();// 生成候选集合列表和其对应的移动节点列表
		ArrayList<int[]> candidate_move_list = new ArrayList<>();
		candidate_list.clear();
		candidate_move_list.clear();
		int[] candidate_cost_list = new int[candidate_count];// 候选集合中每个路线的距离长度数组
		while (candidate_list.size() < candidate_count) {// candidate_count = 100 候选集合长度
			int[] result = create_new(route);
			int[] move = { result[result.length - 2], result[result.length - 1] };// {loc1,loc2}
			int[] tmp_route = new int[lenth];
			System.arraycopy(result, 0, tmp_route, 0, lenth);
			if (!(candidate_list.contains(tmp_route))) {
				candidate_list.add(tmp_route);
				candidate_move_list.add(move);
			}
		}
		for (int i = 0; i < candidate_list.size(); i++) 
			candidate_cost_list[i] = route_cost(candidate_list.get(i));
		// 找集合中距离长度最小的解
		int min_candidate_cost = candidate_cost_list[0];
		int min_candidate_index = 0;
		for (int i = 0; i < candidate_cost_list.length; i++) {
			if (candidate_cost_list[i] < min_candidate_cost) {
				min_candidate_cost = candidate_cost_list[i];
				min_candidate_index = i;
			}
		}
		record.add(min_candidate_cost);
		int[] min_candidate = candidate_list.get(min_candidate_index);// 候选集合中最短路径对应的线路
		int[] move_city = candidate_move_list.get(min_candidate_index);
		if (min_candidate_cost < valuebest) {// 候选集合中最短路径长度小于最优距离
			valuebest = min_candidate_cost;
			for(int i=0;i<solutionbest.length;i++) 
				solutionbest[i] = min_candidate[i];
			if (taboo_list.contains(move_city)) // 藐视法则，当此移动导致的值更优，则无视该禁忌列表
				taboo_list.remove(taboo_list.indexOf(move_city));
			if (taboo_list.size() >= taboo_list_length) 
				taboo_list.remove(0);
			taboo_list.add(move_city);
			return min_candidate;
		} else {// 当未找到更优路径时，选择次优路线，如果该次优路线在禁忌表里，则更次一层，依次类推，找到一条次优路线
			if (taboo_list.contains(move_city)) {
				int[] tmp_min_candidate = min_candidate;
				int[] tmp_move_city = move_city;// 用于跳出死循环，回到原点
				while (taboo_list.contains(move_city)) {
					candidate_list.remove(candidate_list.indexOf(min_candidate));
					int index = 0;//找到候选集最优解的索引
					for (int i = 0; i < candidate_list.size(); i++) 
						if (candidate_cost_list[i] == min_candidate_cost) 
							index = i;
					int[] arrays_result = new int[candidate_cost_list.length - 1];// 将arrays数组在index前的元素都复制到新数组arrays_result中
					System.arraycopy(candidate_cost_list, 0, arrays_result, 0, index);// 判断index之后是否还有元素，有则将index后的元素从index位置复制到新数组中
					if (index < candidate_cost_list.length - 1) 
						System.arraycopy(candidate_cost_list, index + 1, arrays_result, index,arrays_result.length - index);
					candidate_cost_list = arrays_result;
					candidate_move_list.remove(candidate_move_list.indexOf(move_city));
					min_candidate_cost = candidate_cost_list[0];//候选集合中次短路径
					min_candidate_index = 0;
					for (int i = 0; i < candidate_cost_list.length; i++) 
						if (candidate_cost_list[i] < min_candidate_cost) {
							min_candidate_cost = candidate_cost_list[i];
							min_candidate_index = i;
						}
					min_candidate = candidate_list.get(min_candidate_index);
					move_city = candidate_move_list.get(min_candidate_index);
					if (candidate_list.size() < 10) {// 防止陷入死循环，在候选集个数小于10的时候跳出
						min_candidate = tmp_min_candidate;
						move_city = tmp_move_city;
					}
				}
			}
			if (taboo_list.size() >= taboo_list_length) 
				taboo_list.remove(0);
			taboo_list.add(move_city);
			return min_candidate;
		}

	}

	int[] taboo_search() {
		Lead();//导入距离矩阵
		for(int i=0;i<solutionnew.length;i++) {solutionbest[i]=solutionnew[i];}  //赋初值
		int[] route=new int[solutionnew.length];
		 for(int i=0;i<solutionnew.length;i++) 
			 route[i]=solutionnew[i];
		for (int i = 0; i < iteration_count; i++) // iteration_count = 100迭代次数
			route= single_search(route);// 调用函数，获取下一条线路
		int[] new_route = new int[city_list.length];
		for (int i = 0; i < solutionbest.length; i++) 
			new_route[i] = solutionbest[i];
//		System.out.print('\n'+"每次候选表的最优解有");
//		int[] a1=new int[record.size()];
//		for(int i=0;i<record.size();i++) {
//			a1[i]=record.get(i);
//			System.out.print(a1[i]+"  ");
//		}
		return new_route;
	}
}
