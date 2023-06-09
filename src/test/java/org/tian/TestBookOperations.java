package org.tian;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.tian.mappers.BookMapper;
import org.tian.mappers.UserMapper;
import org.tian.pojo.Book;
import org.tian.pojo.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestBookOperations {
    /*
     * 本类用于测试对于books表的基本增删改查操作。
     * */
    @Test
    public void testSelectAll() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        List<Book> books;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            books = mapper.selectAllBooks();
        }

        for (Book book :
                books) {
            System.out.println(book);
        }
    }

    @Test
    public void testSelectOne() throws IOException {
        int bookId = 1;

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            Book book1 = mapper.selectOne(bookId);
            System.out.println(book1);
        }
    }

    @Test
    public void testAddBook() throws IOException {
        Book book = new Book("这是一个书名", "我叫作者", "我是描述");
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.addBook(book);
            System.out.println("添加成功。"+book.getBookId()); // 返回主键
            session.commit();
        }

        System.out.println("下面是新的全部book信息：");

        testSelectAll();
    }

    @Test
    public void testEditBook () throws IOException {
        Book book = new Book(7675, "new Name", "new author", "new description");

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.editBook(book);
            session.commit();
            System.out.println("编辑成功。");
        }

        System.out.println("下面是新的全部book信息，请注意对应id的book信息是否修改完成。");

        testSelectAll();
    }

    /**
     * 测试给book绑定user。
     * @throws IOException 抛出异常。
     */
    @Test
    public void boundUser() throws IOException {
        Book book = new Book("bookName", "author", "description");
        User user = new User("userName");

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.addUser(user);
            int userid = user.getUserId();
            System.out.println("新添加的user是：" + userid); // 输出一下刚刚添加的userID。
            book.setUserId(userid);
            BookMapper bookMapper = session.getMapper(BookMapper.class);
            bookMapper.addBook(book);
            session.commit();
            System.out.println("新添加的book是："+book.getBookId());
        }

        System.out.println("下面是新的全部book信息，请注意对应id的book信息是否修改完成。");

        testSelectAll();
    }
}
