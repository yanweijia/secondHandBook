CREATE DATABASE secondHandBook default charset utf8;

CREATE TABLE books(
	book_id INT PRIMARY KEY AUTO_INCREMENT,
	book_name varchar(50) NOT NULL,
	book_price DOUBLE NOT NULL DEFAULT 0,
	book_intro varchar(255),
	book_isbn varchar(20),
	status BIT NOT NULL DEFAULT 1,
	contact_name varchar(20) NOT NULL,
	password varchar(32) NOT NULL,
	contact_phone varchar(50) NOT NULL,
	viewed INT NOT NULL DEFAULT 0,
	postdate DATETIME,
	book_img TEXT,
	book_img_thumbnail TEXT
)AUTO_INCREMENT=1;

INSERT INTO books(book_name,book_price,book_intro,book_isbn,contact_name,password,contact_phone,postdate,book_img,book_img_thumbnail)
VALUES
('疯狂Android讲义',50.00,'李刚写的疯狂安卓讲义,第二版,值得购买','忘记ISBN了','严唯嘉','3F572FCB0F9AF03848738946954B8C43','18621703545','2016-10-10 10:10:50','图片','缩略图'),
('MySQL从入门到精通',60.00,'严唯嘉送你的书,最新版','这是ISBN','严唯嘉','zheshimima','18621703545','2016-10-11 14:01:00','完整图片','缩略图');


--获取所有书籍(最近的100本)
SELECT book_id,book_name,book_price,viewed,postdate,book_img_thumbnail FROM books WHERE status=1 ORDER BY postdate DESC LIMIT 0,100;

--获取指定书籍
SELECT book_name,book_price,book_intro,book_isbn,contact_name,contact_phone,viewed,postdate,book_img FROM books WHERE status=1 AND book_id=指定书籍编号;