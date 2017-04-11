Feature: highlight webvtt
  为了有效的发现对话在某个时间说了什么关键字
  作为质检人员
  我想要在听录音时快进到关键字出现的时间点

  Scenario: how we style webvtt highlight keywords
    * first convert a small piece of highlight result "agent0-30172 好 <c>麻煩您</c>" to HighlightFragment
    * then using "agent0" as a csss class which come from the prefix of cueid without dash & numbers
    * we have to ensure cueid which must form as "agent0-30172" it will be used to locate cue position in webvtt substituting
    * to support css class level styling like this "<c.agent0>" we call replace method to get result "好 <c.agent0>麻煩您</c>"

  Scenario: 产生播放器可以读取的webvtt格式, 并且标记关键字位置
    Given a raw of webvtt like fragment:
      """
      {
        "vtt": [
          "agent0-30172 00:00:30.172 --> 00:00:32.356\n<v R1>好 麻煩您 改 撥 啦 他是 零 八 零零  </v>\n",
          "customer0-33156 00:00:33.156 --> 00:00:34.004\n<v R0>欸 那 半年 內  </v>\n",
          "agent0-34180 00:00:34.180 --> 00:00:34.884\n<v R1>七 八 九  </v>\n",
          "customer0-35856 00:00:35.856 --> 00:00:36.616\n<v R0>七 二 九  </v>\n"
        ]
      }
      """
    When a query highlight result:
      """
        {
          "result": [
            "agent0-30172 好 <c>麻煩您</c> 改 撥 啦 他是 零 八 零零 "
          ]
        }
      """
    Then webvtt output must append a css class on cue text "<c.agent0>麻煩您</c>"