Feature: highlight webvtt
  为了有效的发现对话在某个时间说了什么关键字
  作为质检人员
  我想要在听录音时快进到关键字出现的时间点

  Scenario: 高亮显示webvtt的关键字
    Given 一个未标记的hightlight片段 "agent0-30172 好 <c>麻煩您</c>"
    When cuid "agent0-30172" 的前缀 "agent0" 作为css样式的class时
    Then highlight结果的内容必须添加css样式, 内容 "好 <c>麻煩您</c>" 必须改为 "好 <c.agent0>麻煩您</c>" 的格式


  Scenario Outline: 让 mozilla/vtt.js 插件能显示对话内容的关键字
    Given 对话内容
      | agent0-30172 00:00:30.172 --> 00:00:32.356\n<v R1>好 麻煩您 改撥 </v>\n |
      | customer0-33156 00:00:33.156 --> 00:00:34.004\n<v R0>欸 那 半年 內 </v>\n |
      | agent0-34180 00:00:34.180 --> 00:00:34.884\n<v R1>七 八 九  </v>\n |
      | customer0-35856 00:00:35.856 --> 00:00:36.616\n<v R0>七 二 九  </v>\n |
    When 用搜寻到的hightlight片段 "<highlightFragment>" 取代对话内容
    Then 输出符合w3c webvtt格式文件, 并找出关键字 "<keywords>"

  Examples:
    | highlightFragment | keywords |
    | customer0-33156 欸 那 <c>半年</c> 內  | <c.customer0>半年</c> |