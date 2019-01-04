package hk.hku.eee.randomstock;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Vector;

//-------------------------------------

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.tts.client.SpeechSynthesizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static hk.hku.eee.randomstock.Function.*;


public class StockActivity extends AppCompatActivity {
    /*----------------news_begin-----------------*/
    String API_KEY = "23ef241a29d54320bfe0126ea92ffb72"; // ### YOUE NEWS API HERE ###
    //    String COUNTRY = "hk&category=business";
    String SOURCE = "business-insider,cnbc,die-zeit,financial-post,the-economist";
    ListView listNews;
    ProgressBar loader;


    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";



    /*---------------news_end----------------------------------*/

    //----------------view begin--------

    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView chksharesLayout;
    private TextView casharesLayout;
    private TextView chomeLayout;
    private TextView cindicatorLayout;
    private TextView cnewsLayout;

    private TextView Homett;
    private TextView Homenum;
    private TextView IDnum;
    private TextView IDtt;

    // scrollbar pictures
    private ImageView scrollbar;

    private ImageView Homeimg;
    private ImageView Idimg;
    // init scrollbar offset
    private int scrollbaroffset = 0;
    // current page number
    private int currIndex = 2;
    // scrollbar width
    private int scrollbarwidth;
    //one page width
    private int onepagewidth;

    //UI Textsize;
    private int titletextsize = 19;
    private int stocknametextsize = 17;
    private int stockvaluetextsize = 18;

    private Integer Idcount = 0;

    Handler Home_handler;
    Handler Id_handler;
    //private static final String homedate = "homedate";
    private static final String HOMEINDEX = "homeindex";
    private static final String HOMEWEATHER = "homeweather";

    private static final String IDTT = "IDTT";
    private static final String IDNUM = "IDNUM";
    private static final String IDBG = "IDBG";
    Calendar cal;
    private static String year = "2018";
    private static String month = "";
    private static String day = "";
    private static String hour = "";
    private static String minute = "";
    String idh="";
    String ida="";

    private int Cal_Flag = 0;


    //--------------end view----------

    private static HashSet<String> AStockIds_ = new HashSet();//A
    private static Vector<String> ASelectedStockItems_ = new Vector();

    private static HashSet<String> HKStockIds_ = new HashSet();//HK
    private static Vector<String> HKSelectedStockItems_ = new Vector();

    private final static int BackgroundColor_ = Color.WHITE;
    private final static int HighlightColor_ = Color.rgb(210, 233, 255);
    private final static String ShIndex = "sh000001";
    private final static String SzIndex = "sz399001";
    private final static String AGEIndex = "sz399006";
    private final static String AStockIdsKey_ = "StockIds";

    private final static String HSIIndex = "int_hangseng";
    private final static String DjiIndex = "int_dji";
    private final static String NasdaqIndex = "int_nasdaq";
    private final static String HKStockIdsKey_ = "HKStockIds";
    private final static String YAHOOF = "https://hk.finance.yahoo.com/quote/";


    private TreeMap<String, AStock> aspeakMap;
    private TreeMap<String, HKStock> hkspeakMap;
    private EditText cinput_ashares;//voicce
    private SpeechSynthesizer mSpeechSynthesizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        //----------------View Page Function on create---------------------------
        cinput_ashares = (EditText) this.findViewById(R.id.input_ashares);//voice

        viewPager = (ViewPager) findViewById(R.id.stockcontent);
        //import other view pages layout: LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        //the name of xml.
        View view0 = inflater.inflate(R.layout.asharesviewpage, null);
        View view1 = inflater.inflate(R.layout.hksharesviewpage, null);
        View view2 = inflater.inflate(R.layout.homeviewpage, null);
        View view3 = inflater.inflate(R.layout.indicatorviewpage, null);
        View view4 = inflater.inflate(R.layout.newsviewpage, null);

        //import the bottom menu.
        casharesLayout = (TextView) findViewById(R.id.asharesLayout);
        chksharesLayout = (TextView) findViewById(R.id.hksharesLayout);
        chomeLayout = (TextView) findViewById(R.id.homeLayout);
        cindicatorLayout = (TextView) findViewById(R.id.indicatorLayout);
        cnewsLayout = (TextView) findViewById(R.id.newsLayout);
        Homett = (TextView) view2.findViewById(R.id.Homett);
        Homenum = (TextView) view2.findViewById(R.id.Homenum);
        IDtt = (TextView) view3.findViewById(R.id.idtt);
        IDnum = (TextView) view3.findViewById(R.id.idnum);

        scrollbar = (ImageView) findViewById(R.id.scrollbar);

        Homeimg = (ImageView) view2.findViewById(R.id.Homeimg);
        Idimg = (ImageView) view3.findViewById(R.id.idimg);


        pageview = new ArrayList<View>();
        //add view page
        pageview.add(view0);
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);
        pageview.add(view4);

        /*---------------------news_begin--------------------------*/
        listNews = (ListView) view4.findViewById(R.id.listNews);
        loader = (ProgressBar) view4.findViewById(R.id.loader);
        listNews.setEmptyView(loader);

        if (isNetworkAvailable(getApplicationContext())) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
//-------------------------news_end-----------------------------
        //when you click the bottom menu, it will trigger below function.
        casharesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter SH and SZ Stock Page when you click the button
                viewPager.setCurrentItem(0);

            }
        });

        chksharesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter HK Stock Page when you click the button
                viewPager.setCurrentItem(1);

            }
        });

        chomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter Home Page when you click the button
                viewPager.setCurrentItem(2);

            }
        });

        cindicatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter Indicator Page when you click the button
                viewPager.setCurrentItem(3);

            }
        });

        cnewsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter News Page when you click the button
                viewPager.setCurrentItem(4);

            }
        });

        //view page function, get/set/remove view page
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            //get the size of windows
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            //remove view
            public void destroyItem(View arg0, int arg1, Object arg2) {

                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //judge which page will be shown on current windows
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        viewPager.setAdapter(mPagerAdapter);
        //set init page, mian page when you open APP.
        viewPager.setCurrentItem(2);
        //Listener for view page changing
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // get the width of scrollbar
        scrollbarwidth = BitmapFactory.decodeResource(getResources(), R.drawable.scrollbar).getWidth();

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //get the width of screen
        int screenwidth = displayMetrics.widthPixels;
        //calculate the init offset of scrollbar
        scrollbaroffset = (int) (screenwidth / 5 - scrollbarwidth) / 2;
        //scrollbar move one page width when changing the page.
        onepagewidth = (int) (scrollbaroffset * 2 + scrollbarwidth);
        Matrix matrix = new Matrix();
        matrix.postTranslate(scrollbaroffset + onepagewidth * 2, 0);
        //init scrollbar location.
        scrollbar.setImageMatrix(matrix);
        //----------------End View Page Function on create---------------------------

        //----------------- A/HK Stock Function on create------------------------
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String AidsStr = sharedPref.getString(AStockIdsKey_, ShIndex + "," + SzIndex + "," + AGEIndex);
        String HKidsStr = sharedPref.getString(HKStockIdsKey_, HSIIndex + "," + DjiIndex + "," + NasdaqIndex);

        String[] Aids = AidsStr.split(",");
        AStockIds_.clear();
        for (String id : Aids) {
            AStockIds_.add(id);
        }

        String[] HKids = HKidsStr.split(",");
        HKStockIds_.clear();
        for (String id : HKids) {
            HKStockIds_.add(id);
        }

        //-----------------End A/HK Stock Function on create------------------------

        Home_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                Bundle bundle = msg.getData();
                String homett = "";//bundle.getString(homedate);
                String homenum = bundle.getString(HOMEINDEX);
                String bgid = bundle.getString(HOMEWEATHER);
                String bg = "";
                cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

                year = String.valueOf(cal.get(Calendar.YEAR));
                month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                day = String.valueOf(cal.get(Calendar.DATE));
                if (cal.get(Calendar.AM_PM) == 0)
                    hour = String.format("%02d", Integer.valueOf(cal.get(Calendar.HOUR)));
                else
                    hour = String.format("%02d", Integer.valueOf(cal.get(Calendar.HOUR) + 12));
                minute = String.format("%02d", Integer.valueOf(cal.get(Calendar.MINUTE)));
                ;
                homett = year + "-" + month + "-" + day + "\n" + hour + ":" + minute;
                boolean txtcolor = true;

                switch (bgid) {
                    case "s":
                        bg = "snowy";
                        txtcolor = true;
                        break;
                    case "c":
                        bg = "cloudy";
                        txtcolor = true;
                        break;
                    case "r":
                        bg = "rainy";
                        txtcolor = false;
                        break;
                    case "o":
                        bg = "sunny";
                        txtcolor = true;
                        break;
                    case "t":
                        bg = "tornado";
                        txtcolor = true;
                        break;

                }

                int id = getResources().getIdentifier(bg, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(id);
                Homett.setText(homett);
                Homenum.setText(bg + ":" + homenum);
                if (txtcolor == true) {
                    Homett.setTextColor(Color.BLACK);
                    Homenum.setTextColor(Color.BLACK);

                } else {
                    Homett.setTextColor(Color.WHITE);
                    Homenum.setTextColor(Color.WHITE);

                }

                Homeimg.setBackground(drawable);
            }
        };


        Id_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                Bundle bundle = msg.getData();
                String Idtt = bundle.getString(IDTT);
                String Idnum = bundle.getString(IDNUM);
                String Idtxt = bundle.getString(IDBG);
                String StockMarket = "";
                String bg = "";
                Boolean txtcolor = true;
                String indicator;
                String volatility;


                switch (Idtt) {
                    case "1":
                        StockMarket = "A Share";
                        bg = "astock";
                        txtcolor = true;
                        //Stockname=AStockname;

                        break;
                    case "2":
                        StockMarket = "HK Share";
                        bg = "hkstock";
                        txtcolor = false;
                        //Stockname=HStockname;
                        break;
                }



                int id = getResources().getIdentifier(bg, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(id);

                /*if (txtcolor == true)
                    IDnum.setTextColor(Color.LTGRAY);
                else
                    IDnum.setTextColor(Color.BLACK);*/
                if (Integer.parseInt(Idnum) == 0) {
                    IDnum.setVisibility(View.VISIBLE);
                    IDnum.setText(Idtxt);
                    IDtt.setText(StockMarket);
                    Idimg.setBackground(drawable);

                } else {
                    IDnum.setVisibility(View.INVISIBLE);
                    IDtt.setText(StockMarket);
                    Idimg.setBackground(drawable);

                }


            }
        };
        //-----------------Timer for updating data on create--------------------------
        //if you want to set something update periodically, it can add function here..
        Timer timer = new Timer("RefreshStocks");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //view 0; A-Shares
                if (viewPager.getCurrentItem() == 0) {
                    refreshAStocks();
                }
                //view 1: HK-Shares
                if (viewPager.getCurrentItem() == 1) {
                    refreshHKStocks();
                }
                //view 2: home page
                if (viewPager.getCurrentItem() == 2) {
                    //Add function
                    refreshHome();
                }
                //view 3: indicator page
                if (viewPager.getCurrentItem() == 3) {
                    //Add function
                    //System.out.println("tack");
                    refreshId();
                    //System.out.println("tick");
                }
                //view 4: news page
                if (viewPager.getCurrentItem() == 4) {
                    //Add function
                }
                // refreshAStocks();
                // refreshHKStocks();

            }
        }, 0, 5000); // 5 seconds

        //-----------------End Timer for updating data-------------------------------


    }
    //End on create


    //save the data before destroying this activity
    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        this.mSpeechSynthesizer.release();
        saveStocksToPreferences();
    }

    //save data function, do not change
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveStocksToPreferences();

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    //------------------Save data when you quit app----------------
    //save and show
    private void saveStocksToPreferences() {
        String Aids = "";
        for (String id : AStockIds_) {
            Aids += id;
            Aids += ",";
        }

        String HKids = "";
        for (String id : HKStockIds_) {
            HKids += id;
            HKids += ",";
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //add what you want to save
        //editor.putString(key, value);
        editor.putString(AStockIdsKey_, Aids);
        editor.putString(HKStockIdsKey_, HKids);
        editor.commit();
    }

    //Function: data format
    //var hq_str_sh601988="中国银行,3.620,3.610,3.610,3.620,3.590,3.610,3.620,48039504,173197119.000,1141700,3.610,8306730,3.600,
    //6706200,3.590,4834500,3.580,1580400,3.570,5177927,3.620,6635250,3.630,6068400,3.640,3731900,3.650,1920540,3.660,2018-11-22,15:00:00,00";
    public class AStock {
        public String aid_, aname_;
        public String aopen_, ayesterday_, anow_, ahigh_, alow_;
        public String ab1_, ab2_, ab3_, ab4_, ab5_;
        public String abp1_, abp2_, abp3_, abp4_, abp5_;
        public String as1_, as2_, as3_, as4_, as5_;
        public String asp1_, asp2_, asp3_, asp4_, asp5_;
        public String atime_;
    }

    //var hq_str_hk00001="CHEUNG KONG,长和,81.100,80.850,81.300,80.350,81.200,0.350,0.433,81.150,81.200,210217944,2594663,8.417,3.510,107.000,77.800,2018/11/22,16:08";
    public class HKStock {
        public String hkid_, hkEngname_, hkCNname_;
        public String hkopen_, hkyesterday_, hkhigh_, hklow_, hknow_;
        public String hkChangeprice_, hkChangeper_;
        public String hkbp1_, hksp1_, hkVolPri_, hkVol_;
        public String hkPE_, hkdivid_;    //dividend yield
        public String hkHigh52_, hkLow52_;
        public String hktime_;
    }

    //analyze data got from url. A shares
    public TreeMap<String, AStock> sinaResponseToAStocks(String response) {
        response = response.replaceAll("\n", "");
        String[] stocks = response.split(";");

        TreeMap<String, AStock> astockMap = new TreeMap();
        for (String stock : stocks) {
            String[] leftRight = stock.split("=");
            if (leftRight.length < 2)
                continue;

            String right = leftRight[1].replaceAll("\"", "");
            if (right.isEmpty())
                continue;

            String left = leftRight[0];
            if (left.isEmpty())
                continue;

            AStock stockNow = new AStock();
            stockNow.aid_ = left.split("_")[2];

            String[] values = right.split(",");
            stockNow.aname_ = values[0];
            stockNow.aopen_ = values[1];
            stockNow.ayesterday_ = values[2];
            stockNow.anow_ = values[3];
            stockNow.ahigh_ = values[4];
            stockNow.alow_ = values[5];
            stockNow.ab1_ = values[10];
            stockNow.ab2_ = values[12];
            stockNow.ab3_ = values[14];
            stockNow.ab4_ = values[16];
            stockNow.ab5_ = values[18];
            stockNow.abp1_ = values[11];
            stockNow.abp2_ = values[13];
            stockNow.abp3_ = values[15];
            stockNow.abp4_ = values[17];
            stockNow.abp5_ = values[19];
            stockNow.as1_ = values[20];
            stockNow.as2_ = values[22];
            stockNow.as3_ = values[24];
            stockNow.as4_ = values[26];
            stockNow.as5_ = values[28];
            stockNow.asp1_ = values[21];
            stockNow.asp2_ = values[23];
            stockNow.asp3_ = values[25];
            stockNow.asp4_ = values[27];
            stockNow.asp5_ = values[29];
            stockNow.atime_ = values[values.length - 3] + "_" + values[values.length - 2];
            astockMap.put(stockNow.aid_, stockNow);
        }
        aspeakMap = astockMap;
        return astockMap;
    }


    //analyze data got from url. HK shares
    //var hq_str_hk00700="TENCENT,腾讯控股,293.000,295.000,294.600,287.400,291.200,-3.800,-1.288,291.000,291.200,3488588962,12015790,32.504,0.302,476.600,251.400,2018/11/23,16:08";
    //var hq_str_int_hangseng="恒生指数,25927.68,-91.73,-0.35";
    //HK
    public TreeMap<String, HKStock> sinaResponseToHKStocks(String response) {
        response = response.replaceAll("\n", "");
        String[] stocks = response.split(";");

        TreeMap<String, HKStock> hkstockMap = new TreeMap();
        for (String stock : stocks) {
            String[] leftRight = stock.split("=");
            if (leftRight.length < 2)
                continue;

            String right = leftRight[1].replaceAll("\"", "");
            if (right.isEmpty())
                continue;

            String left = leftRight[0];
            if (left.isEmpty())
                continue;


            HKStock stockNow = new HKStock();
            String[] leftnum = left.split("_");
            //HK Stock data
            if (leftnum.length == 3) {
                //stock
                stockNow.hkid_ = left.split("_")[2];
                String[] values = right.split(",");
                stockNow.hkEngname_ = values[0];
                stockNow.hkCNname_ = values[1];

                stockNow.hkopen_ = values[2];
                stockNow.hkyesterday_ = values[3];
                stockNow.hkhigh_ = values[4];
                stockNow.hklow_ = values[5];
                stockNow.hknow_ = values[6];

                stockNow.hkChangeprice_ = values[7];
                stockNow.hkChangeper_ = values[8];

                stockNow.hkbp1_ = values[9];
                stockNow.hksp1_ = values[10];
                stockNow.hkVolPri_ = values[11];
                stockNow.hkVol_ = values[12];

                stockNow.hkPE_ = values[13];
                stockNow.hkdivid_ = values[14];

                stockNow.hkHigh52_ = values[15];
                stockNow.hkLow52_ = values[16];
                stockNow.hktime_ = values[values.length - 2] + "_" + values[values.length - 1];
            } else if (leftnum.length == 4) {
                //HK-index, USA-index
                //var hq_str_int_hangseng="恒生指数,25927.68,-91.73,-0.35";
                stockNow.hkid_ = (left.split("_")[2]) + "_" + (left.split("_")[3]);
                String[] values = right.split(",");

                stockNow.hkCNname_ = values[0];
                stockNow.hknow_ = values[1];
                stockNow.hkChangeprice_ = values[2];
                //stockNow.hkChangeper_ = values[3];
                //if open: var hq_str_int_hangseng="恒生指数,25927.68,-91.73,-0.35%";
                String hkchangeindex = values[3].replaceAll("%","");
                stockNow.hkChangeper_ = hkchangeindex;



            } else
                continue;

            //stockNow.hkid_ = left.split("_")[2];
            //-----------------------------------------------------------------------------
            hkstockMap.put(stockNow.hkid_, stockNow);
        }

        hkspeakMap = hkstockMap;
        return hkstockMap;
    }

    // request data from sina; A Shares
    public void querySinaAStocks(String list) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://hq.sinajs.cn/list=" + list;
        //http://hq.sinajs.cn/list=sh600000,sh600536


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //put data into atable view
                        //update data on UI;
                        updateAStockListView(sinaResponseToAStocks(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(stringRequest);
    }

    //HK
    // request data from sina; HK Shares
    public void querySinaHKStocks(String list) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://hq.sinajs.cn/list=" + list;

        //http://hq.sinajs.cn/list=hk00700

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //put data into atable view
                        //update data on UI
                        updateHKStockListView(sinaResponseToHKStocks(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(stringRequest);
    }

    //update data; A Shares
    private void refreshAStocks() {
        String Aids = "";
        for (String id : AStockIds_) {
            Aids += id;
            Aids += ",";
        }

        querySinaAStocks(Aids);

    }

    //HK
    //update data; HK Shares
    private void refreshHKStocks() {

        //HK
        String HKids = "";
        for (String id : HKStockIds_) {
            HKids += id;
            HKids += ",";
        }

        querySinaHKStocks(HKids);

    }

    //update Home page
    private void refreshHome() {
        //Homett.setText(" na");
        ArrayList<String> Stockname = new ArrayList<String>();
        ArrayList<StockEntry> entrylist = new ArrayList<>();


        Double forecast = 0.0;

        String weatherchar = "tsrco";

        //Stockname.add("%5EHSI");
        Stockname.add("%5EGSPC");
        Stockname.add("%5EIXIC");
        Stockname.add("%5EN225");
        Stockname.add("%5EHSIL");
        Stockname.add("%5EVIX");
        Stockname.add("%5EVXN");

        while (Cal_Flag < 36) {



            for (int i = 0; i < Stockname.size(); i++) {

                try {

                    entrylist = htmlrequest(Stockname.get(i));
                    //if(entrylist.size()!=0)
                    forecast += AssetRate(entrylist.get(0));
                    entrylist.get(0).EntryPrint();
                    System.out.println(forecast);
                    Cal_Flag=10000;

                } catch (Exception e) {

                    forecast = 1.23 * Stockname.size() / 2;
                    Cal_Flag++;
                    //System.out.println(Cal_Flag);


                } finally {
                    //conn_status.disconnect();
                }
            }

            //Integer random = (int) (Math.random() * 9 + 1);
            String weather;
            Message msg = Home_handler.obtainMessage();
            Bundle bundle = new Bundle();
            forecast = (forecast * 2 / (1.0 * Stockname.size()));
            weather = String.valueOf((weatherchar.charAt((int) Math.round(forecast + 2))));

            //System.out.println(forecast.toString());
            bundle.putString(HOMEINDEX, String.valueOf(Math.round(forecast*100)/100.0));
            bundle.putString(HOMEWEATHER, weather);
            msg.setData(bundle);
            Home_handler.sendMessage(msg);
        }


    }

    private ArrayList<StockEntry> htmlrequest(String AssetName) {

        final int HTML_BUFFER_SIZE = 64 * 1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];
        HttpURLConnection conn_status = null;
        StockEntry entry = new StockEntry();
        ArrayList<StockEntry> entrylist = new ArrayList<StockEntry>();
        int j=0;
        while(j<6) {
            try {

                URL url_status = new URL(YAHOOF + AssetName + "/history");
               // System.out.println(YAHOOF + AssetName + "/history");

                conn_status = (HttpURLConnection) url_status.openConnection();

                conn_status.setInstanceFollowRedirects(true);

                BufferedReader reader_status = new BufferedReader(new InputStreamReader(conn_status.getInputStream()));

                // System.out.println("exception");
                String HTMLSource = ReadBufferedHTML(reader_status, htmlBuffer, HTML_BUFFER_SIZE);

                //System.out.println(HTMLSource.length());
                entrylist = parse_HTML_Source(HTMLSource, 1);
                for (int i = 0; i < entrylist.size(); i++) {
                    Double weight = 1.0;

                    entrylist.get(i).assetname = AssetName;

                    switch (AssetName) {
                        case "%5EHSIL5":
                            weight = -1.0;
                            break;
                        case "%5EVIX":
                            weight = -1.0;
                            break;
                        case "%5EVXN":
                            weight = -1.0;
                            break;

                    }
                    entrylist.get(i).weight = weight;

                }

                j = 100;
                //System.out.println(j);
            } catch (Exception e) {
                j++;
                //System.out.println("exception");
            }
            finally{
                conn_status.disconnect();

            }
        }

        //System.out.println(forecast);
        return entrylist;


    }

    private void refreshId() {
        String Idindex = "";
        long timercount=0;
        timercount=(timercount++)%10000;
        String taptt="\t\t\t\t";
        String taptxt="\t\t";
        ArrayList<String> Stockname = new ArrayList<String>();
        ArrayList<String> AStockname = new ArrayList<String>();
        ArrayList<String> HStockname = new ArrayList<String>();
        ArrayList<StockEntry> entrylist = new ArrayList<>();
        Integer j=0;


        AStockname.add("000001.SS");
        AStockname.add("000300.SS");
        AStockname.add("399001.SZ");

        HStockname.add("%5EHSI");
        HStockname.add("%5EHSIL");
        HStockname.add("%5EGSPC");
        HStockname.add("%5VIX");
        //HStockname.add("%5EHSCE");
        //HStockname.add("%5EHSIL");

        String Stockmarket = "1";
        if (Idcount < 2) {
            Stockmarket = "1";
            Stockname=AStockname;
            Idindex=ida;
            Idcount++;

        } else if (Idcount < 4) {
            Stockmarket = "2";
            Idcount = ++Idcount % 4;
            Idindex=idh;
            Stockname=HStockname;
        }

        Idindex = String.format("\t\t\t\t%1$-18s%2$-16s%3$-16s%4$-12s\n","Date","Index","Open","Latest");
        if(timercount<4) {

            while (j < 6) {
                for (int i = 0; i < Stockname.size(); i++) {

                    try {

                        entrylist = htmlrequest(Stockname.get(i));
                        //if(entrylist.size()!=0)
                        //entrylist.get(0).EntryPrint();//"%1$-16s\t%2$-14s\t%3$-16s\t\t%4$-16s\t\t\n",
                        Idindex = Idindex + String.format("\t\t\t\t%1$-12s%2$-12s%3$-12s%4$-12s\n",
                                entrylist.get(0).date, entrylist.get(0).assetname.replace("%5",""),
                                entrylist.get(0).price.get(0).toString(), entrylist.get(0).price.get(4).toString());
                        //entrylist.get(0).date + taptxt + entrylist.get(0).assetname + taptxt +
                        //entrylist.get(0).price.get(0).toString() + taptxt + entrylist.get(0).price.get(4).toString()+"\n";
                        //System.out.println(forecast);
                        j = 100;

                    } catch (Exception e) {
                        j++;
                        //System.out.println(j);


                    } finally {
                        //conn_status.disconnect();
                    }
                }
            }
            if(Stockmarket=="1"){
                ida=Idindex;

            }else {
                idh=Idindex;
            }
        }

        //System.out.println(Idcount);

        //System.out.println(Idcount.toString());

        Message msg = Id_handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(IDTT, Stockmarket);
        bundle.putString(IDNUM, "0");
        bundle.putString(IDBG, Idindex);
        msg.setData(bundle); /**/
        Id_handler.sendMessage(msg);

    }

    private Double AssetRate(StockEntry stockEntry) {
        Double percentage, threshold = 0.01;
        Double result;

        percentage = stockEntry.price.get(4) / (1.0 * stockEntry.price.get(0));
        if (percentage > 1+threshold)
            result = 1.0;
        else if (percentage < 1-threshold)
            result = -1.0;
        else result = 0.0;

        result = result*stockEntry.weight;

        //System.out.println(percentage.toString());
        //System.out.println(stockEntry.weight.toString());

        return result;
    }

    public String ReadBufferedHTML(BufferedReader reader, char[] htmlBuffer, int bufSz) throws java.io.IOException {
        htmlBuffer[0] = '\0';
        int offset = 0;
        //System.out.println("hellow");
        do {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0) {
                offset += cnt;
            } else {
                break;
            }
        } while (true);


        return new String(htmlBuffer);
    }

    private class StockEntry {
        public String assetname;
        public Double threshold;
        public Double weight;
        public String date;
        public ArrayList<Double> price = new ArrayList<Double>();


        public void EntryPrint() {

            System.out.println(assetname);

            System.out.println(date);

            String pricelist = "";
            for (int i = 0; i < price.size(); i++) {
                pricelist = pricelist + "__" + String.valueOf(price.get(i));
            }
            System.out.println(pricelist);
        }
    }

    private String Com_data(int id) {

        return ".{1,70}?<span data-reactid=\"" + String.valueOf(id) + "\">(.*?)</span>";
    }

    public ArrayList<StockEntry> parse_HTML_Source(String HTMLsource, Integer num) {
        // System.out.println("this is /n" + HTMLsource);
        String Com_date = "<span data-reactid=\"(\\d+)\">(201\\d)年(\\d{2})月(\\d{2})日</span>";
        String Alt_data = ".{1,70}?.</td></tr>";
        Pattern P_date = Pattern.compile(Com_date);
        Matcher m_date = P_date.matcher(HTMLsource);
        ArrayList<StockEntry> EntryResult = new ArrayList<StockEntry>();


        int pnum = num;
        while (m_date.find() && pnum > 0) {
            pnum--;
            StockEntry stockentry = new StockEntry();
            int id = Integer.parseInt(m_date.group(1));
            String Date = m_date.group(2) + m_date.group(3) + m_date.group(4);
            stockentry.date = Date;
            //System.out.println(stockentry.date);

            Pattern P_Entry = Pattern.compile(Com_data(id + 2) + Com_data(id + 4) + Com_data(id + 6)
                    + Com_data(id + 8) + Com_data(id + 10));
            Matcher m_Entry = P_Entry.matcher(HTMLsource);

            if (m_Entry.find()) {
                for (int i = 1; i <= 5; i++) {
                    //System.out.println("hi"+m_Entry.group(i));
                    Double Price = 0.0;
                    if (m_Entry.group(i) != "-")
                        Price = Double.parseDouble(m_Entry.group(i).replace(",", ""));
                    stockentry.price.add(Price);
                    // System.out.println(Price.toString());
                }
                EntryResult.add(stockentry);
            }


        }
        return EntryResult;
    }


    //Add function: A shares
    public void addAStock(View view) {

        //view1
        EditText editText = (EditText) findViewById(R.id.input_ashares);
        String astockId = editText.getText().toString();
        if (astockId.length() != 6)
            return;


        if (astockId.startsWith("6")) {
            astockId = "sh" + astockId;
        } else if (astockId.startsWith("0") || astockId.startsWith("3")) {
            astockId = "sz" + astockId;
        } else
            return;

        AStockIds_.add(astockId);
        refreshAStocks();//
    }

    //HK
    //Add function: HK shares
    public void addHKStock(View view) {

        //view1
        EditText editText = (EditText) findViewById(R.id.input_hkshares);
        String hkstockId = editText.getText().toString();
        if (hkstockId.length() != 5)
            return;

        hkstockId = "hk" + hkstockId;
        HKStockIds_.add(hkstockId);

        refreshHKStocks();//
    }

    //Delete function: A shares
    public void deleteAStock(View view) {
        if (ASelectedStockItems_.isEmpty())
            return;//

        for (String selectedId : ASelectedStockItems_) {
            AStockIds_.remove(selectedId);
            //view1
            TableLayout atable = (TableLayout) findViewById(R.id.stock_atable);
            int count = atable.getChildCount();
            for (int i = 1; i < count; i++) {
                TableRow row = (TableRow) atable.getChildAt(i);
                LinearLayout nameId = (LinearLayout) row.getChildAt(0);
                TextView idText = (TextView) nameId.getChildAt(1);
                if (idText != null && idText.getText().toString() == selectedId) {
                    atable.removeView(row);
                    break;
                }
            }
        }
        refreshAStocks();//
    }

    //HK
    //Delete function: HK shares
    public void deleteHKStock(View view) {
        if (HKSelectedStockItems_.isEmpty())
            return;//

        for (String selectedId : HKSelectedStockItems_) {
            HKStockIds_.remove(selectedId);
            //view1
            TableLayout hktable = (TableLayout) findViewById(R.id.stock_hktable);
            int count = hktable.getChildCount();
            for (int i = 1; i < count; i++) {
                TableRow row = (TableRow) hktable.getChildAt(i);
                LinearLayout nameId = (LinearLayout) row.getChildAt(0);
                TextView idText = (TextView) nameId.getChildAt(1);
                if (idText != null && idText.getText().toString() == selectedId) {
                    hktable.removeView(row);
                    break;
                }
            }
        }
        refreshHKStocks();//
    }


    //Update UI; A shares
    public void updateAStockListView(TreeMap<String, AStock> astockMap) {

        // Table
        //view1
        TableLayout atable = (TableLayout) findViewById(R.id.stock_atable);//view
        atable.setStretchAllColumns(true);
        atable.setShrinkAllColumns(true);
        atable.removeAllViews();

        // Title
        TableRow rowTitle = new TableRow(this);
        TextView nameTitle = new TextView(this);
        nameTitle.setTextColor(Color.WHITE);
        nameTitle.setTextSize(titletextsize);
        nameTitle.setText(getResources().getString(R.string.stock_name_title));
        rowTitle.addView(nameTitle);

        TextView nowTitle = new TextView(this);
        nowTitle.setGravity(Gravity.CENTER);
        nowTitle.setTextColor(Color.WHITE);
        nowTitle.setTextSize(titletextsize);
        nowTitle.setText(getResources().getString(R.string.stock_now_title));
        rowTitle.addView(nowTitle);

        TextView percentTitle = new TextView(this);
        percentTitle.setGravity(Gravity.CENTER);
        percentTitle.setTextColor(Color.WHITE);
        percentTitle.setTextSize(titletextsize);
        percentTitle.setText(getResources().getString(R.string.stock_increase_percent_title));
        rowTitle.addView(percentTitle);

        TextView increaseTitle = new TextView(this);
        increaseTitle.setGravity(Gravity.CENTER);
        increaseTitle.setTextColor(Color.WHITE);
        increaseTitle.setTextSize(titletextsize);//17
        increaseTitle.setText(getResources().getString(R.string.stock_increase_title));
        rowTitle.addView(increaseTitle);

        atable.addView(rowTitle);

        Collection<AStock> astocks = astockMap.values();
        for (AStock stock : astocks) {
            if (stock.aid_.equals(ShIndex) || stock.aid_.equals(SzIndex) || stock.aid_.equals(AGEIndex)) {
                Double dNow = Double.parseDouble(stock.anow_);
                Double dYesterday = Double.parseDouble(stock.ayesterday_);
                Double dIncrease = dNow - dYesterday;
                Double dPercent = dIncrease / dYesterday * 100;
                String change = String.format("%.2f", dPercent) + "% ";

                int indexId;
                int changeId;
                if (stock.aid_.equals(ShIndex)) {
                    indexId = R.id.stock_sh_index;
                    changeId = R.id.stock_sh_change;


                } else if (stock.aid_.equals(SzIndex)) {
                    indexId = R.id.stock_sz_index;
                    changeId = R.id.stock_sz_change;
                } else {
                    indexId = R.id.stock_ge_index;
                    changeId = R.id.stock_ge_change;
                }

                TextView indexText = (TextView) findViewById(indexId);
                indexText.setText(stock.anow_);
                int color = Color.WHITE;
                if (dIncrease > 0) {
                    color = Color.RED;
                } else if (dIncrease < 0) {
                    color = Color.GREEN;
                }
                indexText.setTextColor(color);

                TextView changeText = (TextView) findViewById(changeId);
                changeText.setTextColor(color);
                changeText.setText(change);

                continue;
            }

            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            if (ASelectedStockItems_.contains(stock.aid_)) {
                row.setBackgroundColor(HighlightColor_);
            }

            LinearLayout nameId = new LinearLayout(this);
            nameId.setOrientation(LinearLayout.VERTICAL);
            TextView name = new TextView(this);
            name.setTextColor(Color.WHITE);
            name.setTextSize(stocknametextsize);
            name.setText(stock.aname_);
            nameId.addView(name);

            TextView id = new TextView(this);
            id.setTextSize(stocknametextsize);
            id.setTextColor(Color.WHITE);
            id.setText(stock.aid_);
            nameId.addView(id);
            row.addView(nameId);

            TextView now = new TextView(this);
            now.setGravity(Gravity.RIGHT);
            now.setText(stock.anow_);
            row.addView(now);

            TextView percent = new TextView(this);
            percent.setGravity(Gravity.RIGHT);
            TextView increaseValue = new TextView(this);
            increaseValue.setGravity(Gravity.RIGHT);
            Double dOpen = Double.parseDouble(stock.aopen_);
            Double dB1 = Double.parseDouble(stock.abp1_);
            Double dS1 = Double.parseDouble(stock.asp1_);
            if (dOpen == 0 && dB1 == 0 && dS1 == 0) {
                percent.setText("--");
                increaseValue.setText("--");
            } else {
                Double dNow = Double.parseDouble(stock.anow_);
                if (dNow == 0) {// before open
                    if (dS1 == 0) {
                        dNow = dB1;
                        now.setText(stock.abp1_);
                    } else {
                        dNow = dS1;
                        now.setText(stock.asp1_);
                    }
                }
                Double dYesterday = Double.parseDouble(stock.ayesterday_);
                Double dIncrease = dNow - dYesterday;
                Double dPercent = dIncrease / dYesterday * 100;
                percent.setText(String.format("%.2f", dPercent) + "%");
                increaseValue.setText(String.format("%.2f", dIncrease));
                int color = Color.WHITE;
                if (dIncrease > 0) {
                    color = Color.RED;
                } else if (dIncrease < 0) {
                    color = Color.GREEN;
                }

                now.setTextColor(color);
                now.setTextSize(stockvaluetextsize);
                percent.setTextColor(color);
                percent.setTextSize(stockvaluetextsize);
                increaseValue.setTextColor(color);
                increaseValue.setTextSize(stockvaluetextsize);
            }
            row.addView(percent);
            row.addView(increaseValue);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup group = (ViewGroup) v;
                    ViewGroup nameId = (ViewGroup) group.getChildAt(0);
                    TextView idText = (TextView) nameId.getChildAt(1);
                    if (ASelectedStockItems_.contains(idText.getText().toString())) {
                        v.setBackgroundColor(BackgroundColor_);
                        ASelectedStockItems_.remove(idText.getText().toString());
                    } else {
                        v.setBackgroundColor(HighlightColor_);
                        ASelectedStockItems_.add(idText.getText().toString());
                    }
                }
            });

            atable.addView(row);


        }
    }

    //HK
    //Update UI; HK shares
    public void updateHKStockListView(TreeMap<String, HKStock> hkstockMap) {

        // Table
        //view1
        TableLayout hktable = (TableLayout) findViewById(R.id.stock_hktable);//view
        hktable.setStretchAllColumns(true);
        hktable.setShrinkAllColumns(true);
        hktable.removeAllViews();

        // Title
        TableRow rowTitle = new TableRow(this);
        TextView nameTitle = new TextView(this);
        nameTitle.setTextColor(Color.WHITE);
        nameTitle.setTextSize(titletextsize);
        nameTitle.setText(getResources().getString(R.string.stock_name_title));
        rowTitle.addView(nameTitle);

        TextView nowTitle = new TextView(this);
        nowTitle.setGravity(Gravity.CENTER);
        nowTitle.setTextColor(Color.WHITE);
        nowTitle.setTextSize(titletextsize);
        nowTitle.setText(getResources().getString(R.string.stock_now_title));
        rowTitle.addView(nowTitle);

        TextView percentTitle = new TextView(this);
        percentTitle.setGravity(Gravity.CENTER);
        percentTitle.setTextColor(Color.WHITE);
        percentTitle.setTextSize(titletextsize);
        percentTitle.setText(getResources().getString(R.string.stock_increase_percent_title));
        rowTitle.addView(percentTitle);

        TextView increaseTitle = new TextView(this);
        increaseTitle.setGravity(Gravity.CENTER);
        increaseTitle.setTextColor(Color.WHITE);
        increaseTitle.setTextSize(titletextsize);
        increaseTitle.setText(getResources().getString(R.string.stock_increase_title));
        rowTitle.addView(increaseTitle);

        hktable.addView(rowTitle);


        Collection<HKStock> hkstocks = hkstockMap.values();
        for (HKStock stock : hkstocks) {
            if (stock.hkid_.equals(HSIIndex) || stock.hkid_.equals(DjiIndex) || stock.hkid_.equals(NasdaqIndex)) {

                Double dIncrease = Double.parseDouble(stock.hkChangeprice_);
                Double dPercent = Double.parseDouble(stock.hkChangeper_);
                String change = String.format("%.2f", dPercent) + "% ";

                int indexId;
                int changeId;
                if (stock.hkid_.equals(HSIIndex)) {
                    indexId = R.id.stock_hsi_index;
                    changeId = R.id.stock_hsi_change;


                } else if (stock.hkid_.equals(DjiIndex)) {
                    indexId = R.id.stock_dji_index;
                    changeId = R.id.stock_dji_change;
                } else {
                    indexId = R.id.stock_nasdaq_index;
                    changeId = R.id.stock_nasdaq_change;
                }

                TextView indexText = (TextView) findViewById(indexId);
                indexText.setText(stock.hknow_);
                int color = Color.WHITE;
                if (dIncrease > 0) {
                    color = Color.RED;
                } else if (dIncrease < 0) {
                    color = Color.GREEN;
                }
                indexText.setTextColor(color);

                TextView changeText = (TextView) findViewById(changeId);
                changeText.setTextColor(color);
                changeText.setText(change);

                continue;
            }

            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            if (HKSelectedStockItems_.contains(stock.hkid_)) {
                row.setBackgroundColor(HighlightColor_);
            }

            LinearLayout nameId = new LinearLayout(this);
            nameId.setOrientation(LinearLayout.VERTICAL);
            TextView name = new TextView(this);
            name.setTextColor(Color.WHITE);
            name.setTextSize(stocknametextsize);
            name.setText(stock.hkCNname_);
            nameId.addView(name);

            TextView id = new TextView(this);
            id.setTextSize(stocknametextsize);
            id.setTextColor(Color.WHITE);
            id.setText(stock.hkid_);
            nameId.addView(id);
            row.addView(nameId);

            TextView now = new TextView(this);
            now.setGravity(Gravity.RIGHT);
            now.setText(stock.hknow_);
            row.addView(now);

            TextView percent = new TextView(this);
            percent.setGravity(Gravity.RIGHT);
            TextView increaseValue = new TextView(this);
            increaseValue.setGravity(Gravity.RIGHT);
            Double dOpen = Double.parseDouble(stock.hkopen_);
            Double dB1 = Double.parseDouble(stock.hkbp1_);
            Double dS1 = Double.parseDouble(stock.hksp1_);
            if (dOpen == 0 && dB1 == 0 && dS1 == 0) {
                percent.setText("--");
                increaseValue.setText("--");
            } else {
                Double dNow = Double.parseDouble(stock.hknow_);
                if (dNow == 0) {// before open
                    if (dS1 == 0) {
                        dNow = dB1;
                        now.setText(stock.hkbp1_);
                    } else {
                        dNow = dS1;
                        now.setText(stock.hksp1_);
                    }
                }
                Double dYesterday = Double.parseDouble(stock.hkyesterday_);
                Double dIncrease = dNow - dYesterday;
                Double dPercent = dIncrease / dYesterday * 100;
                percent.setText(String.format("%.2f", dPercent) + "%");
                increaseValue.setText(String.format("%.2f", dIncrease));
                int color = Color.WHITE;
                if (dIncrease > 0) {
                    color = Color.RED;
                } else if (dIncrease < 0) {
                    color = Color.GREEN;
                }

                now.setTextColor(color);
                now.setTextSize(stockvaluetextsize);
                percent.setTextColor(color);
                percent.setTextSize(stockvaluetextsize);
                increaseValue.setTextColor(color);
                increaseValue.setTextSize(stockvaluetextsize);
            }
            row.addView(percent);
            row.addView(increaseValue);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup group = (ViewGroup) v;
                    ViewGroup nameId = (ViewGroup) group.getChildAt(0);
                    TextView idText = (TextView) nameId.getChildAt(1);
                    if (HKSelectedStockItems_.contains(idText.getText().toString())) {
                        v.setBackgroundColor(BackgroundColor_);
                        HKSelectedStockItems_.remove(idText.getText().toString());
                    } else {
                        v.setBackgroundColor(HighlightColor_);
                        HKSelectedStockItems_.add(idText.getText().toString());
                    }
                }
            });

            hktable.addView(row);


        }
    }


    //-------------View page seeting----------------------------

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    //A shares;
                    animation = new TranslateAnimation(Math.round((currIndex - 2) / 2.0) * scrollbaroffset + (currIndex - 2) * onepagewidth, -scrollbaroffset - onepagewidth * 2, 0, 0);
                    refreshAStocks();
                    //Add function

                    break;
                case 1:
                    //HK Shares;
                    animation = new TranslateAnimation(Math.round((currIndex - 2) / 2.0) * scrollbaroffset + (currIndex - 2) * onepagewidth, -scrollbaroffset - onepagewidth, 0, 0);
                    refreshHKStocks();
                    break;
                case 2:
                    //home page, add function
                    animation = new TranslateAnimation(Math.round((currIndex - 2) / 2.0) * scrollbaroffset + (currIndex - 2) * onepagewidth, 0, 0, 0);
                    Cal_Flag = 0;
                    refreshHome();
                    //Add function

                    break;
                case 3:
                    //indicator page
                    animation = new TranslateAnimation(Math.round((currIndex - 2) / 2.0) * scrollbaroffset + (currIndex - 2) * onepagewidth, scrollbaroffset + onepagewidth, 0, 0);
                    refreshId();
                    //Add function

                    break;
                case 4:
                    //news page
                    animation = new TranslateAnimation(Math.round((currIndex - 2) / 2.0) * scrollbaroffset + (currIndex - 2) * onepagewidth, scrollbaroffset + onepagewidth * 2, 0, 0);
                    //Add function

                    break;

            }
            //current page number
            currIndex = arg0;
            //seeting for scrollbar;
            animation.setFillAfter(true);
            animation.setDuration(200);
            scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    }
    //-------------End View page seeting----------------------------


    public void avoiceAKey(View view) {

        aspeak(0);

    }

    public void voiceHKKey(View view) {

        hkspeak(0);

    }

    public void aspeak(int speaker) {

        VoiceUtils utils = new VoiceUtils();
        utils.init(this, speaker);
        mSpeechSynthesizer = utils.getSyntheszer();

        Collection<AStock> aspeak = aspeakMap.values();
        for (AStock apstock : aspeak) {
            Double dNow = Double.parseDouble(apstock.anow_);
            Double dYesterday = Double.parseDouble(apstock.ayesterday_);
            Double dIncrease = dNow - dYesterday;
            Double dPercent = dIncrease / dYesterday * 100;
            String dPercentf = String.format("%.2f", dPercent);
            String atext = apstock.aname_ + "现价    " + apstock.anow_ + "   涨跌幅为百分之 " + dPercentf + "   ";
            this.mSpeechSynthesizer.speak(atext);
        }
        //cinput_ashares.setText(text);
        //this.mSpeechSynthesizer.speak(text);
    }

    public void hkspeak(int speaker) {

        VoiceUtils utils = new VoiceUtils();
        utils.init(this, speaker);
        mSpeechSynthesizer = utils.getSyntheszer();


        Collection<HKStock> hkspeak = hkspeakMap.values();
        for (HKStock hkstock : hkspeak) {
            Double dPercentt = Double.parseDouble(hkstock.hkChangeper_);
            String dPercenthk = String.format("%.2f", dPercentt);
            String hktext = hkstock.hkCNname_ + "现价    " + hkstock.hknow_ + "   涨跌幅为百分之 " + dPercenthk + "   ";
            this.mSpeechSynthesizer.speak(hktext);
        }

    }

    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String xml = "";
            String urlParameters = "";
//            xml = Function.excuteGet("https://newsapi.org/v2/top-headlines?country="+COUNTRY+"&apiKey="+API_KEY, urlParameters);
//            xml = Function.excuteGet(" https://newsapi.org/v2/top-headlines？sources = "+ SOURCE + "＆apiKey = "+ API_KEY, urlParameters);
            xml = excuteGet("https://newsapi.org/v2/everything?sources=" + SOURCE + "&apiKey=" + API_KEY, urlParameters);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            if (xml.length() > 10) { // Just checking if not empty
                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }

                ListNewsAdapter adapter = new ListNewsAdapter(StockActivity.this, dataList);
                listNews.setAdapter(adapter);

                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(StockActivity.this, DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        startActivity(i);
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
