import requests
import pandas as pd
import re
import copy
import numpy as np
import matplotlib.pyplot as plt 
header= {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.64'}
path_data=pd.read_excel(r'D:\JAVA\学习\食堂\食堂坐标.xls')
head=path_data['纬度']
tail=path_data['经度']
position=path_data['食堂名称']

for i in range(len(position)):
    for j in range(len(position)):
        url='http://api.map.baidu.com/routematrix/v2/walking?output=json&origins={0},{1}&destinations={2},{3}&ak=muHHVQSKZL4bVN12fHmAMMqS95KdDQKh'.format(str(head[i]),str(tail[i]),str(head[j]),str(tail[j]))
        html=requests.get(url,headers=header, timeout=5).text
        html=re.split('[:",}]',html)   
        if i==0 and j==0:  
            dict_data={'position1':[position[i]],'position2':[position[j]],'distance':[html[14]]}    
            data=pd.DataFrame(dict_data,columns=['position1','position2','distance'])  
        else:      
            dict_data =pd.DataFrame({'position1': [position[i]],'position2':[position[j]],'distance': [html[14]]})   
            data=data.append(dict_data,ignore_index=True)
data1=copy.deepcopy(data)
for i in range(len(data)):
    if '公里' in  data['distance'][i]:
        data['distance'][i]=data['distance'][i].strip('公里')
        data['distance'][i]=float(data['distance'][i])*1000
    elif '米' in data['distance'][i]:
        data['distance'][i]=float(data['distance'][i].strip('米'))

lenth=len(path_data['食堂名称'])
name=list(path_data['食堂名称'])
distance=data['distance']
distance=list(distance)
a=[[0 for i in range(lenth)] for j in range(lenth)]
j=0
k=0
for i in range(len(distance)):
    if k==16:
        j+=1
        k=0
    a[j][k]=distance[i]
    k+=1
df=pd.DataFrame(a,columns=name,index=name)  
number=[]
for i in range(lenth):
    number.append(i)
# df.drop('Unnamed: 0',axis=1,inplace=True)
df.columns=number
df.index=number

def initpara():
    alpha = 0.99
    t = (1,40)
    markovlen = 1000

    return alpha,t,markovlen

solutionnew = np.arange(lenth)


solutioncurrent = solutionnew.copy()
valuecurrent =99000 

solutionbest = solutionnew.copy()
valuebest = 99000 
alpha,t2,markovlen = initpara()
t = t2[1]

result1 = [] #记录迭代过程中的接受的解的变化
result2=[] #记录迭代过程中最优解
while t > t2[0]:
    for i in np.arange(markovlen):
        #下面的两交换和三角换是两种扰动方式，用于产生新解
        if np.random.rand() > 0.5:# 交换路径中的这2个节点的顺序
            # np.random.rand()产生[0, 1)区间的均匀随机数
            while True:#产生两个不同的随机数
                loc1 = np.int(np.ceil(np.random.rand()*(lenth-1)))
                loc2 = np.int(np.ceil(np.random.rand()*(lenth-1)))
                if loc1 != loc2:
                    break
            solutionnew[loc1],solutionnew[loc2] = solutionnew[loc2],solutionnew[loc1]
        else: #三交换
            while True:
                loc1 = np.int(np.ceil(np.random.rand()*(lenth-1)))
                loc2 = np.int(np.ceil(np.random.rand()*(lenth-1))) 
                loc3 = np.int(np.ceil(np.random.rand()*(lenth-1)))

                if((loc1 != loc2)&(loc2 != loc3)&(loc1 != loc3)):
                    break

            # 下面的三个判断语句使得loc1<loc2<loc3
            if loc1 > loc2:
                loc1,loc2 = loc2,loc1
            if loc2 > loc3:
                loc2,loc3 = loc3,loc2
            if loc1 > loc2:
                loc1,loc2 = loc2,loc1

            #下面的三行代码将[loc1,loc2)区间的数据插入到loc3之后
            tmplist = solutionnew[loc1:loc2].copy()
            solutionnew[loc1:loc3-loc2+1+loc1] = solutionnew[loc2:loc3+1].copy()
            solutionnew[loc3-loc2+1+loc1:loc3+1] = tmplist.copy()  

        valuenew = 0
        for i in range(lenth-1):
            valuenew += df[solutionnew[i]][solutionnew[i+1]]
        valuenew += df[solutionnew[0]][solutionnew[lenth-1]]
       # print (valuenew)
        if valuenew<valuecurrent: #接受该解
            #更新solutioncurrent 和solutionbest
            valuecurrent = valuenew
            solutioncurrent = solutionnew.copy()
            if valuecurrent < valuebest:
                valuebest = valuenew
                solutionbest = solutionnew.copy()
        else:#按一定的概率接受该解
            if np.random.rand() < np.exp(-(valuenew-valuecurrent)/t):
                valuecurrent = valuenew
                solutioncurrent = solutionnew.copy()
            else:
                solutionnew = solutioncurrent.copy()
            if valuecurrent < valuebest:
                valuebest = valuecurrent
                solutionbest = solutioncurrent.copy()
    t = alpha*t#0.99*t
    result1.append(valuecurrent)
    result2.append(valuebest)
    print (t) #程序运行时间较长，打印t来监视程序进展速度
#用来显示结果
for i in range(lenth):
    print(solutionbest[i],end='-->')
print(solutionbest[0])
# plt.xlim((0,10))
plt.plot(np.array(result1))
plt.ylabel("valuecurent")
plt.xlabel("t")
plt.show()
plt.plot(np.array(result2))
plt.ylabel("valuebest")
plt.xlabel("t")
plt.show()