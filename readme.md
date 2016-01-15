# 爬虫系统概述

## 主要功能
对赔率相关的数据源进行抓取，提取信息

##启动方式
java -jar spider-robot.jar arg

arg可以有如下选项，"500", "jbb", "lj", "jbb3", "lj3", "statistic", "win310", "pinnacle", "sportteryAll", "sporttery"

eg. java -jar spider-robot.jar 500就是启动500万的爬虫

如果arg没有值，则一次启动上述参数哦包含的所有抓取任务
