import copy,random,datetime
import matplotlib.pyplot as plt
import pandas as pd
path_data=pd.read_excel(r'D:\JAVA\学习\食堂\食堂坐标.xls')
df=pd.read_excel(r'D:\JAVA\学习\食堂\距离矩阵.xlsx')
name=path_data['食堂名称']
lenth=len(name)
number=[]
for i in range(lenth):
    number.append(i)
df.drop('Unnamed: 0',axis=1,inplace=True)
df.columns=number


class Taboo_search:
    def __init__(self,is_random = True):
        self.city_list = [i for i in range(lenth)]
        self.candidate_count = 100   #候选集合长度
        self.taboo_list_length = 10
        self.iteration_count = 10   #迭代次数
        self.min_route,self.min_cost = self.random_first_full_road() if is_random else self.greedy_first_full_road()
        #is_random为参数，第一次为True
        self.taboo_list = []
        self.record = []
        self.record1 = [0 for i in range(100)]


    #计算两城市间的距离
    def city_distance(self,city1,city2):
        distance =df[city1][city2]
        return distance

    #获取当前城市邻居城市中距离最短的一个
    def next_shotest_road(self,city1,other_cities):
        tmp_min = 999999
        tmp_next = None
        for i in range(0,len(other_cities)):
            distance = self.city_distance(city1,other_cities[i])
            #print(distance)
            if distance < tmp_min:
                tmp_min = distance
                tmp_next = other_cities[i]
        return tmp_next,tmp_min


    #随机生成初始线路
    def random_first_full_road(self):
        cities = copy.deepcopy(self.city_list)
        cities.remove(cities[0])
        route = copy.deepcopy(cities)
        random.shuffle(route)#用来打乱元素排列顺序
        cost = self.route_cost(route)#计算线路路径长度
        return route,cost

    #根据贪婪算法获取初始线路
    def greedy_first_full_road(self):
        remain_city = copy.deepcopy(self.city_list)
        current_city = remain_city[0]
        road_list = []
        remain_city.remove(current_city)
        all_distance = 0
        while len(remain_city) > 0:
            next_city,distance = self.next_shotest_road(current_city,remain_city)#获取当前城市邻居城市中距离最短的一个
            all_distance += distance
            road_list.append(next_city)
            remain_city.remove(next_city)
            current_city = next_city
        all_distance += self.city_distance(self.city_list[0],road_list[-1])
        return road_list,round(all_distance,2)

    #随机交换2个城市位置
    def random_swap_2_city(self,route):
        #print(route)
        road_list = copy.deepcopy(route)
        two_rand_city = random.sample(road_list,2)#随机地从指定列表中提取出N个不同的元素，列表的维数没有限制
        #print(two_rand_city)
            
        index_a = road_list.index(two_rand_city[0])
        index_b = road_list.index(two_rand_city[1])
        road_list[index_a] = two_rand_city[1]
        road_list[index_b] = two_rand_city[0]
        return road_list,sorted(two_rand_city)#注意顺序，先小后大

    #计算线路路径长度
    def route_cost(self,route ):
        road_list = copy.deepcopy(route)
        current_city = self.city_list[0]
        while current_city in road_list:
            road_list.remove(current_city)
        all_distance = 0
        while len(road_list) > 0 :
            distance = self.city_distance(current_city,road_list[0])
            all_distance += distance
            current_city = road_list[0]
            road_list.remove(current_city)
        all_distance += self.city_distance(current_city,self.city_list[0])
        return round(all_distance,2)

    #获取下一条线路
    def single_search(self,route):
        #生成候选集合列表和其对应的移动列表
        candidate_list = []
        candidate_move_list = []
        while len(candidate_list) < self.candidate_count:#self.candidate_count = 100 候选集合长度
            tmp_route,tmp_move = self.random_swap_2_city(route)#tmp_move为route的改变的节点索引一维数组
            # print("tmp_route:",tmp_route)
            if tmp_route not in candidate_list: 
                candidate_list.append(tmp_route)
                candidate_move_list.append(tmp_move)
        #计算候选集合各路径的长度
        candidate_cost_list = []
        for candidate in candidate_list:
            candidate_cost_list.append(self.route_cost(candidate))
        #print(candidate_list)
        for i in range(100):
            self.record1[i]=candidate_cost_list[i]
        print('\n',self.record1)
        min_candidate_cost = min(candidate_cost_list)   #候选集合中最短路径长度
        self.record.append(min_candidate_cost)
        min_candidate_index = candidate_cost_list.index(min_candidate_cost)#候选集合最小长度的索引
        min_candidate = candidate_list[min_candidate_index]  #候选集合中最短路径对应的线路
        move_city = candidate_move_list[min_candidate_index]

        if min_candidate_cost < self.min_cost:#候选集合中最短路径长度小于最优距离
            self.min_cost = min_candidate_cost
            self.min_route = min_candidate
            if move_city in self.taboo_list:  #藐视法则，当此移动导致的值更优，则无视该禁忌列表
                self.taboo_list.remove(move_city)
            if len(self.taboo_list) >= self.taboo_list_length:  #判断该禁忌列表长度是否以达到限制，是的话移除最初始的move
                self.taboo_list.remove(self.taboo_list[0])
            self.taboo_list.append(move_city)    #将该move加入到禁忌列表
            return min_candidate

        else:
       #当未找到更优路径时，选择次优路线，如果该次优路线在禁忌表里，则更次一层，依次类推，找到一条次优路线
            if move_city in self.taboo_list:
                tmp_min_candidate = min_candidate
                tmp_move_city = move_city#用于跳出死循环，回到原点

                while move_city in self.taboo_list:#注意是while不是判断if
                    candidate_list.remove(min_candidate)
                    candidate_cost_list.remove(min_candidate_cost)
                    candidate_move_list.remove(move_city)

                    min_candidate_cost = min(candidate_cost_list)  # 候选集合中最短路径
                    min_candidate_index = candidate_cost_list.index(min_candidate_cost)
                    min_candidate = candidate_list[min_candidate_index]  # 候选集合中最短路径对应的线路
                    move_city = candidate_move_list[min_candidate_index]
                    if len(candidate_list) < 10: #防止陷入死循环，在候选集个数小于10的时候跳出
                        min_candidate = tmp_min_candidate
                        move_city = tmp_move_city
            if len(self.taboo_list) >= self.taboo_list_length:  # 判断该禁忌列表长度是否以达到限制，是的话移除最初始的move
                self.taboo_list.remove(self.taboo_list[0])
            self.taboo_list.append(move_city)
            return min_candidate

    #进行taboo_search直到达到终止条件:循环100次
    def taboo_search(self):
        route = copy.deepcopy(self.min_route)
        for i in range(self.iteration_count):#self.iteration_count = 100迭代次数
            route = self.single_search(route) #调用函数，获取下一条线路
        new_route = [self.city_list[0]]
        new_route.extend(self.min_route)#在列表末尾一次性追加另一个序列中的多个值
        new_route.append(self.city_list[0]) #前后插入首个城市信息
        print(self.record)
        return new_route,self.min_cost




if __name__ == "__main__":
    ts_greedy = Taboo_search(is_random=False)
    route_greedy,cost_greedy = ts_greedy.taboo_search()







