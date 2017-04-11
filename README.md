# 

## route

### logs-yyyy.MM.dd/{type}/{id}?_id={percolatorId}

example
```
logs-2016.07.07/amiast/B95F18EFB9?_id=temporary
```

output as [vtt](http://html5doctor.com/video-subtitling-and-webvtt/)

```
#vtt


```

## analysis

### 数据元素
```
customer0-1988 00:00:01.988 --> 00:00:02.468 <v R0>對 </v> 
```
`customer0-1988`称为`cueid`在一个对话中不重复, 时间`00:00:01.988 --> 00:00:02.468`, 内容`<v R0>對 </v> `

### highlight过程
es highlight的结果，不受限于用`<em>`来标记
```
customer0-28476 鐘 小姐  <em>你好</em>
```
转化为符合vtt语法
```vtt
agent0-208484
00:03:28.484 --> 00:03:29.380
<v R1>鐘 小姐 <c.agent0>你好</c> </v>
```

### VTT sample
```vtt
WEBVTT

agent0-5188
00:00:05.188 --> 00:00:08.532
<v R1>很 高興 為您 服務 您好  </v>

customer0-9400
00:00:09.400 --> 00:00:10.008
<v R0>欸 您好  </v>

agent0-10280
00:00:10.280 --> 00:00:10.704
<v R1>是  </v>
```
