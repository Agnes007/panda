# panda-git-remotetest
 aa simple in-memory storage engine that consists of  cuckoo filter, b+ tree and skiplist..
SkipNode类：节点类，包括存储的key-value的值，每个节点有前后左右四个指针
String key; String value; SkipListNode up,down,left,right

SkipList类：链表类，用于构建和操作链表
类函数：public SkipListNode findEntry(String k)//在最下层找到输入的key要插入的位置，返回找到的位置节点
public String get(String k)//返回key对应的value
public String put(String k,String j)////在跳表中插入k-v值
（public void backLink(SkipListNode pEntry,SkipListNode qEntry)//在p节点后插入q节点，
  public void horizontalLink(SkipListNode pEntry,SkipListNode qEntry)//节点水平连接
  public void vertiacalLink(SkipListNode pEntry,SkipListNode qEntry)//节点垂直前后连接）
public boolean delete(String key) //在跳表中删除k-v值
public void printHorizontal()//跳表按行来打印（private String getOneRow(SkipListNode pEntry)//获取跳表中的行）

Test类：测试类，用于测试跳表的插入删除 并打印生成的跳表
