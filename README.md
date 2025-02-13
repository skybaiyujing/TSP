# 华中科技大学食堂路线规划

## 项目简介

本项目旨在通过 **禁忌搜索算法** (Tabu Search) 计算华中科技大学所有食堂的最优游览路线，以最小化食堂间的总行程距离。为了实现这一目标，我们利用 **百度地图 API** 采集食堂坐标及其之间的道路距离，并基于 **禁忌搜索** 进行路径优化。

## 功能特性

- **食堂数据采集**: 通过百度拾取坐标系统获取各个食堂的坐标，并存入 Excel 文件。
- **食堂间距离计算**: 使用 **百度地图 API** 获取两个食堂之间的真实道路距离。
- **禁忌搜索算法求解**: 采用 SWAP 算子优化遍历路径，避免局部最优。
- **最优路径可视化**: 生成最优路径并可视化，包括步行路线和行车路线。

## 项目结构

```
.
├── data/                   # 存放食堂坐标及距离数据
│   ├── canteen_coordinates.xlsx
│   ├── distance_matrix.xlsx
├── src/                    # 代码目录
│   ├── get_canteen_data.py  # 采集食堂坐标信息
│   ├── get_distance.py      # 计算食堂之间的实际距离
│   ├── tabu_search.py       # 禁忌搜索算法优化路径
│   ├── visualize.py         # 可视化食堂路线
├── results/                # 结果存储
│   ├── optimal_route.png    # 最优路径示意图
│   ├── optimal_path.txt     # 最优食堂游览顺序
├── README.md               # 项目说明文档
└── requirements.txt        # 依赖环境文件
```

## 运行指南

### 1. 安装依赖

```bash
pip install -r requirements.txt
```

### 2. 获取食堂数据

运行以下脚本从百度拾取坐标系统获取食堂坐标信息，并保存至 `data/canteen_coordinates.xlsx`。

```bash
python src/get_canteen_data.py
```

### 3. 计算食堂间的距离

```bash
python src/get_distance.py
```

此步骤将使用 **百度地图 API** 计算食堂间的实际道路距离，并保存为 `data/distance_matrix.xlsx`。

### 4. 运行禁忌搜索算法

```bash
python src/tabu_search.py
```

执行后，将输出最优路径，并生成 `results/optimal_path.txt`。

### 5. 可视化最优路径

```bash
python src/visualize.py
```

此步骤会生成 `results/optimal_route.png`，显示最优遍历路径。

## 结果示例

最终计算出的最优游览顺序如下：

```
韵苑学生食堂（一、二楼） → 东三 → 东一（一、二楼） → 百惠园 → 紫菘美食屋 →
西二（一、二楼） → 西一（一、二楼） → 西华园 → 百品屋 → 百景园（一、二、三楼） →
喻园 → 集锦园 → 集贤楼（梧桐雨） → 东教工食堂 → 东园（一、二、三楼） → 东篱 →
韵苑学生食堂
```

## 说明

- **数据来源**: 使用 **百度拾取坐标系统** 采集食堂坐标，并通过 **百度地图 API** 获取食堂间的实际道路距离。
- **禁忌搜索算法**: 通过 **SWAP** 算子交换节点位置，优化食堂遍历路径。
- **限制**: 由于百度地图 API 的调用限制，部分数据可能存在误差。

## 参考资料

- [百度地图 API 文档](https://lbsyun.baidu.com/)
- [禁忌搜索算法介绍](https://en.wikipedia.org/wiki/Tabu_search)



