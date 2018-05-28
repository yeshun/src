package yess.com.yesskeys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.yehun.java2smali.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_main);

        String content = "成都M" +
                "2018-6-23Space00:00:00N" +
                "22L50O" +
                "20P"+
                "1000Q"+
                "3000#" +
                "银行转账A" +
                "无B" +
                "无C" +
                "无D" +
                "无E" +
                "无F" +
                "无G" +
                "1年内逾期少于3次且少于90天,信用良好,无逾期H" +
                "无I" +
                "无J" +
                "无K" +
                "无" +
                "||" +
                "无#" +
                "无A" +
                "无B" +
                "无C" +
                "无D" +
                "无E" +
                "无F" +
                "无G" +
                "1年内逾期少于3次且少于90天,信用良好,无逾期H" +
                "500I" +
                "无J" +
                "无K" +
                "无" ;

        /*
        5oiQ6YO9TTIwMTgtNi0yM1NwYWNlMDA6MDA6MDBOMjJMNTBPMjBQMTAwMFEzMDAwI+mTtuihjOi9rOi0pkHml6BC5pegQ+aXoETml6BF5pegRuaXoEcx5bm05YaF6YC+5pyf5bCR5LqOM+asoeS4lOWwkeS6jjkw5aSpLOS/oeeUqOiJr+WlvSzml6DpgL7mnJ9I5pegSeaXoErml6BL5pegfHzml6Aj5pegQeaXoELml6BD5pegROaXoEXml6BG5pegRzHlubTlhoXpgL7mnJ/lsJHkuo4z5qyh5LiU5bCR5LqOOTDlpKks5L+h55So6Imv5aW9LOaXoOmAvuacn0g1MDBJ5pegSuaXoEvml6A=
        * */
        try{
            content = content.trim();
            byte[] codeArray = content.getBytes("UTF-8");
            String enStr = new String(Base64.encode(codeArray, Base64.NO_WRAP)) ;
            Log.e("Yess ",enStr);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
