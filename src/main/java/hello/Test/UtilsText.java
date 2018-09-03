package hello.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hello.Utils.Message;
import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class UtilsText {

    @Test
    public void getDiffText() {
        String prev = "this is a book";
        String curr = "this is a beautiful book";
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(Message.getDiff(prev, curr, sb), 11);
        Assert.assertEquals(sb.toString(), "eautiful b");

        Message.getDiff(prev, prev, sb);
        Assert.assertEquals(sb.toString(), Strings.EMPTY);
    }

}
