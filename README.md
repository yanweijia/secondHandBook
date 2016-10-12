# secondHandBook
####  second-hand book  二手书交易平台Demo   (myeclipse)tomcat+mysql
##获取所有书籍:

###方法名称:getAllBooks
		请求方法:GET
		参数:无参
		方法说明:返回JSON格式,最多100本
			status:返回状态,success为成功,fail为失败
			reason:失败原因,当status为fail的时候有效.
			count:当前结果中书籍的个数
			books:书籍的JSONArray封装好的数组
		返回JSON格式如下:
			{"status":"success","count":2,"books":[{"book_id":2,"book_name":"MySQL从入门到精通","book_price":"60","viewed":0,"postdate":"2016-10-11 14:01:00.0","book_img_thumbnail":"缩略图"},{"book_id":1,"book_name":"疯狂Android讲义","book_price":"50","viewed":0,"postdate":"2016-10-10 10:10:50.0","book_img_thumbnail":"缩略图"}]}
		
##获取详细信息:

###方法名称:getDetailInfo
  		请求方法:GET
  		参数:book_id int类型,书籍编号
  		方法说明:返回JSON格式
  			status:返回状态,success为成功,fail为失败
  			reason:失败原因,当status为fail的时候有效.
  		调用示例:
  			http://yanweijia.cn:8080/secondHandBook/getDetailInfo?book_id=1
  		返回JSON格式如下:
  			{"status":"success","book":{"book_id":"1","book_name":"疯狂Android讲义","book_price":50,"book_intro":"李刚写的疯狂安卓讲义,第二版,值得购买","book_isbn":"忘记ISBN了","contact_name":"严唯嘉","contact_phone":"18621703545","viewed":0,"postdate":"2016-10-10 10:10:50.0","book_img":"图片"}}
  		
##删除指定书籍:

###方法名称:deleteBook
  		请求方法:GET
  		参数:book_id int类型,书籍编号
  			password String类型,MD5加密后的16位大写密码.
  		方法说明:返回JSON格式
  			status:返回状态,success为成功,fail为失败
  			reason:失败原因,当status为fail的时候有效.
  		调用示例:
  			http://yanweijia.cn:8080/secondHandBook/deleteBook?book_id=1&password=3F572FCB0F9AF03848738946954B8C43
  		返回JSON格式如下:
  			正确结果:{"status":"success"}
  			错误结果:{"status":"fail","reason":"密码错误,删除失败"}
  			错误结果2:{"status":"fail","reason":"数据库中没有编号为1的书"}
  		
##发布一本二手书:

###方法名称:postNewBook
  		请求方法:POST
  		参数:book_name String 书籍名称
  			book_price double 价格
  			book_intro String 书籍介绍
  			book_isbn Sting 书籍ISBN编号
  			contact_name String 联系人
  			password String MD5加密后的密码,32位大写
  			contact_phone String 联系手机
  			book_img String 书籍图片的Base64编码后文字
  			book_img_thumbnail String 书籍图片缩略图的Base64编码后的文字
  		方法说明:返回JSON格式
  			status:返回状态,success为成功,fail为失败
  			reason:失败原因,当status为fail的时候有效.
  		调用示例:
  			自行调用,然后获取最新书籍查看调用结果
  		返回JSON格式如下:
  			正确结果:{"status":"success"}
  			错误结果:{"status":"fail":"具体的错误原因"}
  		
##根据指定条件查找书籍:

###方法名称:getBooksBy
  		请求方法:GET
  		参数:book_name String 书籍名称  ,默认为空,匹配方式为%,匹配所有书籍
  			low	int 最低价格(包括) ,默认为0
  			high int 最高价格(包括) ,默认为int的最大值 Integer.MAX_VALUE
  			singlePageNum int 单页多少个结果 ,默认10
  			page int 第几页 ,默认1
  			sortWay boolean 排序方式,true为按照价格从低到高,false相反  .默认true
  		注意:上述参数可以全传,也可以全不传,不传的则使用默认值.
  		方法说明:返回JSON格式,最多100本
			status:返回状态,success为成功,fail为失败
			reason:失败原因,当status为fail的时候有效.
			count:当前结果中书籍的个数
			books:书籍的JSONArray封装好的数组
  		调用示例:
  			http://yanweijia.cn:8080/secondHandBook/getBooksBy?book_name=疯狂&low=30&high=50
  		返回JSON格式如下:
			{"status":"success","count":1,"books":[{"book_id":1,"book_name":"疯狂Android讲义","book_price":"50","viewed":0,"postdate":"2016-10-10 10:10:50.0","book_img_thumbnail":"缩略图"}]}
  		
