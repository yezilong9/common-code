import com.yz.app.EmailReceiver;
import com.yz.app.EmailSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zilongye on 17/1/22.
 */
public class Test {

    public static void main(String[] args) {
//        List<String> sendTo = new ArrayList<String>();
//        sendTo.add("363062816@qq.com");
//        boolean flag = EmailSender.send("标题","内容",sendTo);
//        System.out.println(flag);

        EmailReceiver.loadEmail();
    }
}
