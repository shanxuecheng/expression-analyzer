expression-analyzer
===================

基于有限自动机和LL(1)分析法实现的公式解析器，具备以下功能：

1)实现了算术和逻辑表达式的解析，可使用单条或多条语句，表达式中可使用注释；

2)支持多种数据类型，包括数值、布尔、字符串和日期；

3)支持变量定义，表达式解析前后能够设置和取得变量的值；

4)支持函数运算，除默认的系统函数外，用户可使用自定义函数；

5)实现了对if-else分支结构的解析；

6)能够对语句中出现的词法错误和语法错误给出提示并定位。

简单使用示例（具体使用说明参见downloads中的文档）：

Expression expression = new Expression("a +1;");//创建表达式

expression.initVariable("a", 1);//设置变量值

Valuable result = expression.reParseAndEvaluate(); //执行，返回结果

如需取得数值结果可调用： result.getNumberValue()，返回BigDecimal，结果为2
