package com.example.map;

import com.example.client.MyLog;
import com.example.map.util.ToastUtil;
import com.example.view.TitleBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class Message extends Activity{
	private EditText edit;
	private TextView text;
	private TitleBar mTitleBarView;
	String htm = null;
	 String url = "http://health.sina.cn/?vt=4&pos=108";//edit.getText().toString().trim();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.message);
	        text = (TextView) findViewById(R.id.text);
	        text.setBackgroundColor(Color.parseColor("#FFFFFF"));
	        getHTML(url);
	        mTitleBarView=(TitleBar) findViewById(R.id.title_bar);
			initTitleView();
	}
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnRight(R.drawable.refresh);
		mTitleBarView.setTitleText(R.string.title_message);
		if(Socket.warn == 1)
			mTitleBarView.setBackgroundColor(Color.parseColor("#e24949"));
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getHTML(url);
			}
		});
	}
	private void getHTML(String url) {
		// TODO Auto-generated method stub
		 new HttpUtils().send(
	                HttpRequest.HttpMethod.GET,
	                url,
	                new RequestCallBack<String>() {
	                    @Override
	                    public void onSuccess(ResponseInfo<String> info) {
	                        String html = info.result;
	                        parseHTML(html);
	                    }

	                    @Override
	                    public void onFailure(HttpException e, String s) {

	                    }
	                }
	        );
	}
	private void get(String url){

        new HttpUtils().send(
                HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> info) {
                        String html = info.result;
                        parse(html);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                }
        );

    }
    
    

    private void parseHTML(String html){
        // ����
        //text.append(html);
        Document doc = Jsoup.parse(html);
        // page title
//        String title = doc.getElementsByTag("title").first().text();
//        text.append(title);
//        text.append("\n------------\n");
/*	        Log.e("11111111111111", "22222222222222222222222222222");
        // ���� title
        String atitle = doc.getElementsByClass("article_t").first().text();
        text.append(atitle);
        text.append("\n------------\n");

        Log.e("11111111111111", "33333333333333333333333");
        // ���� ����
        Elements cats = doc.getElementsByClass("category_r");

        Log.e("11111111111111", "4444444444444444444444444444444");
        for(Element cat : cats){
            text.append(cat.text());
        }

        Log.e("11111111111111", "555555555555555555555555555555");
        text.append("\n------------\n");

        // ���� content
        String acontent = doc.getElementsByClass("article_c").first().text();
        Log.e("11111111111111", "666666666666666666666666666666");
        text.append(acontent);
*///	        text.append("\n------------\n");
        Elements es = doc.getElementsByAttributeValue("class","carditems");
		Elements aes = es.select("a");

//			Map<String, String> map = new HashMap<String, String>();
			
			
			for (Element ae : aes) {
				text.append(ae.getElementsByTag("img").attr("alt"));
				htm = ae.getElementsByTag("a").attr("href");
//				text.append(ae.getElementsByTag("a").attr("href"));
				break;
				}
				
//			Document doc2 = Jsoup.parse(htm);
//			String title1 = doc2.getElementsByTag("title").first().text();
//	        text.append(title1);
//	        text.append("\n------------\n");
	        get(htm);

//	        Elements ds = doc2.getElementsByAttributeValue("class","art_t");
//	        for (Element e : ds) {
//				text.append(e.text());
//				}
	        
//			map.put("title", e.getElementsByTag("a").text());
			
//			map.put("href", "http://www.cnbeta.com"
//					+ e.getElementsByTag("a").attr("href"));
//		}
    }
    private void parse(String htm) {
		Document doc2 = Jsoup.parse(htm);
//		String title1 = doc2.getElementsByTag("title").first().text();
//        text.append(title1);
        text.append("\n------------\n");

        Elements ds = doc2.getElementsByAttributeValue("class","art_t");
        for (Element e : ds) {
			text.append(e.text());
			}
    }
	

}
