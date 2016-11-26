package com.example.jiarou.sharelove;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private Spinner spinner;
    Spinner city;
    //Spinner vendor_type;
    /*spinner 連動*/
    private String[] type = new String[] {"基隆市", "新北市","臺北市","宜蘭縣","新竹縣","桃園縣"
            ,"苗栗縣","臺中市","彰化縣","南投縣","嘉義縣","雲林縣","臺南市","高雄市","澎湖縣","金門縣"
            ,"屏東縣","台東縣","花蓮縣"};
    private String[] address = new String[]{"(200)仁愛區","(201)信義區","(202)中正區","(204)安樂區","(206)七堵區","(203)中山區","(205)暖暖區"};
    private String[][] type2 = new String[][]{{"(200)仁愛區","(201)信義區","(202)中正區","(204)安樂區","(206)七堵區","(203)中山區","(205)暖暖區"},{"(207)萬里區","(220)板橋區","(222)深坑區","(224)瑞芳區","(226)平溪區","(228)貢寮區","(232)坪林區","(234)永和區","(236)土城區","(238)樹林區","(241)三重區","(243)泰山區","(247)蘆洲區","(249)八里區","(252)三芝區","(208)金山區","(221)汐止區","(223)石碇區","(227)雙溪區","(231)新店區","(233)烏來區","(235)中和區","(237)三峽區","(239)鶯歌區","(242)新莊區","(244)林口區","(248)五股區","(251)淡水區","(253)石門區"},
            {"(100)中正區","(103)大同區","(104)中山區","(106)大安區","(108)萬華區","(110)信義區","(112)北投區","(114)內湖區","(116)文山區","(111)士林區","(105)松山區","(115)南港區"},{"(260)宜蘭市","(262)礁溪鄉","(264)員山鄉","(266)三星鄉","(268)五結鄉","(270)蘇澳鎮","(261)頭城鎮","(263)壯圍鄉","(265)羅東鎮","(267)大同鄉","(269)冬山鄉","(272)南澳鄉","(290)釣魚臺列嶼"},{ "(300)新竹市","(302)竹北市","(304)新豐鄉","(306)關西鎮","(308)寶山鄉","(310)竹東鎮","(312)橫山鄉","(314)北埔鄉","(303)湖口鄉","(305)新埔鎮","(307)芎林鄉","(311)五峰鄉","(313)尖石鄉","(315)峨眉鄉"},{"(320)中壢市","(325)龍潭鄉","(324)平鎮市-","(326)楊梅市","(328)觀音鄉","(333)龜山鄉","(335)大溪鎮","(337)大園鄉-","(327)新屋鄉","(330)桃園市","(334)八德市","(336)復興鄉","(338)蘆竹鄉"},{"(350)竹南鎮","(352)三灣鄉","(354)獅潭鄉","(357)通霄鎮","(360)苗栗市","(362)頭屋鄉","(364)大湖鄉","(366)銅鑼鄉","(368)西湖鄉","(351)頭份鎮","(353)南庄鄉","(356)後龍鎮","(358)苑裡鎮","(361)造橋鄉","(363)公館鄉","(365)泰安鄉","(367)三義鄉","(369)卓蘭鎮"},
            {"(400) 中區","(402) 南區","(404) 北區","(407)西屯區","(411)太平區","(413)霧峰區","(420)豐原區","(422)石岡區","(424)和平區","(427)潭子區","(429)神岡區","(433)沙鹿區","(435)梧棲區","(437)大甲區","(439)大安區","(401)東區","(403)西區","(406)北屯區","(408)南屯區","(412)大里區","(414)烏日區","(421)后里區","(423)東勢區","(426)新社區","(428)大雅區","(432)大肚區","(434)龍井區","(436)清水區","(438)外埔區"}
            ,{"(500)彰化市","(503)花壇鄉","(505)鹿港鎮","(507)線西鄉","(509)伸港鄉","(511)社頭鄉","(513)埔心鄉","(515)大村鄉","(520)田中鎮","(522)田尾鄉","(524))溪州鄉","(526)二林鎮","(528)芳苑鄉","(502)芬園鄉","(504)秀水鄉","(506)福興鄉","(508)和美鎮","(510)員林鎮","(512)永靖鄉","(514)溪湖鎮","(516)埔鹽鄉","(521)北斗鎮","(523)埤頭鄉","(525)竹塘鄉","(527)大城鄉","(530)二水鄉"}
            , {"(540)南投市","(542)草屯鎮","(544)國姓鄉","(546)仁愛鄉","(551)名間鄉","(533)水里鄉","(555)魚池鄉","(557)竹山鎮","(541)中寮鄉","(556)信義鄉","(545)埔里鎮","(558)鹿谷鄉","(552)集集鎮"},
            { "(600)嘉義市","(602)番路鄉","(604)竹崎鄉","(606)中埔鄉","(608)水上鄉","(612)太保市","(614)東石鄉","(621)民雄鄉","(623)溪口鄉","(625)布袋鎮","(603)梅山鄉","(605)阿里山鄉","(607)大埔鄉","(611)鹿草鄉","(613)朴子市","(615)六腳鄉","(616)新港鄉","(622)大林鎮","(624)義竹鄉"} ,
            {"(630)斗南鎮","(632)虎尾鎮","(634)褒忠鄉","(636)臺西鄉","(638)麥寮鄉","(643)林內鄉","(647)莿桐鄉","(649)二崙鄉","(652)水林鄉","(654)四湖鄉","(631)大埤鄉","(633)土庫鎮","(635)東勢鄉","(637)崙背鄉","(640)斗六市","(646)古坑鄉","(648)西螺鎮","(651)北港鎮","(653)口湖鄉","(655)元長鄉"},
            {"(700)中西區","(702)南區","(704)北區","(701)東區","(709)安南區","(711)歸仁區","(713)左鎮區","(715)楠西區","(717)仁德區","(719)龍崎區","(722)佳里區","(724)七股區","(726)學甲區","(730)新營區","(732)白河區","(734)六甲區","(736)柳營區","(741)善化區","(743)山上區","(745)安定區","(708)安平區","(710)永康區","(712)新化區","(714)玉井區","(716)南化區","(718)關廟區","(720)官田區","(721)麻豆區","(723)西港區","(725)將軍區","(727)北門區","(731)後壁區","(733)東山區","(735)下營區","(737)鹽水區","(742)大內區","(744)新市區"},
            {"(801)前金區","(800)新興區","(802)苓雅區","(804)鼓山區","(806)前鎮區","(811)楠梓區","(813)左營區","(815)大社區","(817)東沙群島","(819)南沙群島","(821)路竹區","(823)田寮區","(825)橋頭區","(827)彌陀區","(829)湖內區","(831)大寮區","(833)鳥松區","(840)大樹區","(843)美濃區","(845)內門區","(847)甲仙區","(849)那瑪夏區","(852)茄萣區","(842)旗山區","(844)六龜區","(846)杉林區","(848)桃源區","(851)茂林區","(822)阿蓮區","(824)燕巢區","(826)梓官區","(828)永安區","(830)鳳山區","(832)林園區","(803)鹽埕區","(805)旗津區","(807)三民區","(812)小港區","(814)仁武區","(820)岡山區"},
            {"(890)金沙鎮","(892)金寧鄉","(894)烈嶼鄉","(891)金湖鎮","(893)金城鎮","(896)烏坵鄉"},
            {"(900)屏東市","(902)霧台鄉-","(904)九如鄉","(906)高樹鄉","(908)長治鄉","(911)竹田鄉","(913)萬丹鄉","(921)泰武鄉","(923)萬巒鄉","(925)新埤鄉","(927)林邊鄉","(929)琉球鄉","(932)新園鄉","(941)枋山鄉","(943)獅子鄉","(945)牡丹鄉","(947)滿州鄉","(901)三地門鄉","(903)瑪家鄉","(905)里港鄉","(907)鹽埔鄉","(909)麟洛鄉","(912)內埔鄉","(920)潮州鎮","(922)來義鄉","(924)崁頂鄉","(926)南州鄉","(928)東港鎮","(931)佳冬鄉","(940)枋寮鄉","(942)春日鄉","(944)車城鄉","(946)恆春鎮"},
            {"(950)台東市","(952)蘭嶼鄉","(954)卑南鄉","(956)關山鎮","(958)池上鄉","(961)成功鎮","(963)太麻里鄉","(965)大武鄉","(951)綠島鄉","(953)延平鄉","(955)鹿野鄉","(957)海端鄉","(959)東河鄉","(962)長濱鄉","(964)金峰鄉","(966)達仁鄉"},
            { "(970)花蓮市","(971)新城鄉","(973)吉安鄉","(975)鳳林鎮","(977)豐濱鄉","(979)萬榮鄉","(982)卓溪鄉","(972)秀林鄉","(974)壽豐鄉","(976)光復鄉","(978)瑞穗鄉","(981)玉里鎮","(983)富里鄉"},
            {"(880)馬公市","(881)西嶼鄉","(882)望安鄉","(883)七美鄉","(884)白沙鄉","(885)湖西鄉"}};
     Spinner sp;//第一個下拉選單
     Spinner zips;//第二個下拉選單
     private Context context;
     String zip_number;
    private Button search;
    String zip_area;
    String zip_areas;
    TextView tx1;
    ImageView face;

    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle=new Bundle();
        bundle.putString(zip_areas, "From Activity");
        //set Fragmentclass Arguments
        VenderListFragment fragobj=new  VenderListFragment();
        fragobj.setArguments(bundle);

//        Toolbar my_toolbar= (Toolbar)findViewById(R.id.my_toolbar);
//         setSupportActionBar(my_toolbar);
//        getSupportActionBar().setTitle("搜尋店家");



        //跳頁


        /** sent hi to mapActivity
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("message","HI");
                intent.putExtras(bundle);
                intent.setClass(SearchActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        */

        tx1 =(TextView) findViewById(R.id.tx1);
        tx1.setText("抱歉！此區尚無攤販資料喔！");
        face =(ImageView) findViewById(R.id.face);
        face.setBackgroundResource(R.drawable.unhappy);

        //tx1.setVisibility(View.GONE);
      //  vendor_type=(Spinner) findViewById(R.id.spinner2);
         /*spinner 連動*/
        context = this;
        //程式剛啟始時載入第一個下拉選單
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.spinner);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(selectListener);
        //因為下拉選單第一個為地址，所以先載入地址群組進第二個下拉選單
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, address);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zips = (Spinner) findViewById(R.id.spinner3);
        zips.setAdapter(adapter2);
        zips.setOnItemSelectedListener(zipListener);
    }


    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){
            //讀取第一個下拉選單是選擇第幾個
            int pos = sp.getSelectedItemPosition();
            //重新產生新的Adapter，用的是二維陣列type2[pos]
            adapter2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, type2[pos]);
            //載入第二個下拉選單Spinner
            zips.setAdapter(adapter2);
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };

    //選區域將攤販資料印在listview上面
    private  AdapterView.OnItemSelectedListener zipListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //存選取的資料
            zip_area = zips.getSelectedItem().toString();
            zip_areas = zip_area.substring(0,8);
            //去掉中文字
            zip_number= zip_area.substring(1, 4);

            tx1.setText("抱歉！此區尚無攤販資料喔！");
            tx1.setVisibility(View.VISIBLE);
            face.setBackgroundResource(R.drawable.unhappy);


            //將firebase取到的資料印在上面
            /**
            ListView areas = (ListView) findViewById(R.id.areaView2);
            final ArrayAdapter<String> arealist =
                    new ArrayAdapter<String>(SearchActivity.this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1);

            areas.setAdapter(arealist);
**/
            final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");
            ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.child("Location/ZIP").getValue().toString().equals(zip_number)) {
                       // arealist.add((String) dataSnapshot.child("Information/Name").getValue());
                       final String shop_name= (String) dataSnapshot.child("Information/Name").getValue();
                        final ArrayList<String> vendorTitleList = new ArrayList<>();
                        vendorTitleList.add((String) dataSnapshot.child("Information/Name").getValue());
                        int a =0;


                        search = (Button) findViewById(R.id.search);
                        Log.d("hell", "no"+vendorTitleList.size());


                            tx1.setText("此區域有愛心攤販喔～");
                            tx1.setVisibility(View.VISIBLE);
                           face.setBackgroundResource(R.drawable.happy);



                        search.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                    //與maps相通
                                Intent intent01 = getIntent();
                                Bundle bundle = new Bundle();
                                bundle.putString("name", zip_areas);
                                intent01.putExtras(bundle);
                                setResult(2, intent01);

                                    /** 與fragment相通
                                     Bundle bundle=new Bundle();
                                     bundle.putString(zip_areas, "From Activity");
                                     //set Fragmentclass Arguments
                                     VenderListFragment fragobj=new  VenderListFragment();
                                     fragobj.setArguments(bundle); **/
                                    SearchActivity.this.finish();


                                //requestCode需跟A.class的一樣


                            }

                        });

                        /**  int store_number;
                        store_number=((String) dataSnapshot.child("Information/Name").getValue()).length();
                       if(store_number>0) {
                            Toast.makeText(SearchActivity.this, "目前有"+ zip_areas+"筆", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SearchActivity.this, "您沒有選擇任何項目"+store_number, Toast.LENGTH_LONG).show();
                        } **/
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    public void order(View v){

      //  String []  vendor_types=getResources().getStringArray(R.array.vendor_types);
    }









}
