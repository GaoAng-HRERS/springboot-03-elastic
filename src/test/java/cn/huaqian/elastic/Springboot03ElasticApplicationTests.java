package cn.huaqian.elastic;

import cn.huaqian.elastic.bean.Article;
import cn.huaqian.elastic.bean.Book;
import cn.huaqian.elastic.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot03ElasticApplicationTests {

    @Autowired
    private JestClient jestClient;
    @Autowired
    BookRepository bookRepository;

    @Test
    public void test(){
       /* Book book = new Book();
        book.setId(1);
        book.setAuthor("Ang");
        book.setBookName("Elasticsearch");
        bookRepository.index(book);*/

        List<Book> list = bookRepository.findByBookNameLike("Elasticsearch");

        for(Book book : list){
            System.out.println(book);
        }
    }

    @Test
    public void contextLoads() {
        //1、给Es中索引（保存）一个文档
        Article article = new Article();
        article.setId(1);
        article.setTitle("好消息");
        article.setAuthor("Ang");
        article.setContent("Hello Elastic");
        //构建一个索引功能
        Index index = new Index.Builder(article).index("atguigu").type("news").build();

        try {
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void search(){
        String json="{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"content\" : \"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Search search = new Search.Builder(json).addIndex("atguigu").addType("news").build();

        try {
            SearchResult result = jestClient.execute(search);
            System.out.println(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
