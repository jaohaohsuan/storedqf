val p1 = """(.+-\d+)\s+(\S+\s-->\s\S+)\s+([\s\S]*$)""".r
val t1 = "customer0-28476 00:00:28.476 --> 00:00:32.148 \n<v R0>那個 您送 撥您 的電話 三十 五 % 是 一樣 </v>\n"
t1 match {
  case p1(cudeid, time, text) =>
    println(cudeid)
    println(time)
    print(text)
    print("OK")
}
