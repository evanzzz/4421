package ed;

/**
 * Created by ZWH on 4/29/2014.
 */


/**
 * #分析｛｝
 * >>>横式
 * 横式报表主要说明需求和供给以及库存量的计算过程。表头部分信息来自物料主文件，除了现有库存量随时间变动外，其余信息属于静态信息。
 * b[][]    // 总数据表
 * [a]>>0～n  PS 当期+1~n时段
 * N>时段
 * I>预测量 合同量 计划接收量    PAB初值     现有库存量 已分配量
 * C>毛需求  预计库存量 净需求
 * C>计划产出量 计划投入量
 * C>可供销售量ATP  ATP调整后
 *
 * tz[4] 时区标记  当前、需求、计划、预测时区
 * ifo信息组
 * ｛物料号String 名称String 计划时间（dt当前） 计划员String
 * >num>  现有库存  安全库存 批量  批量增量vr 提前期     需求时界 计划时界｝
 *
 * 算法：p35 36
 *
 * >>>竖式
 * 竖式报表对照供给（订单下达情况）和需求（任务来源）的来源和处理状况。
 * ifo同上
 *
 * I>【需求】毛需求num 需求日期dt 需求追溯(合同号)
 * C>库存结余
 * C>【供给】 加工单号 产出量 投入日期 产出日期
 * N>措施（下达 确认 计划）
 *
 * >>综合报表   p34
 * 对于有多种产品的主生产计划，将横式报表成为综合报表。
 */

public class MP {   //对应单一一个BOM对象  //横式报表

    final private String[] tzh={"当期","需求时区","计划时区","预测时区","时区"};
    final private String[] head={"时段(周)","预测量" ,"合同量","毛需求", "计划接收量",
            "PAB初值", "预计库存量",// 5-0现有库存量 6-0已分配量
            "净需求NR","计划产出量","计划投入量","可供销售量ATP","ATP调整后"};
    /**
     * 总数据表[a][b]  a>>
     * 0时段 1预测量 2合同量
     * 3毛需求 4计划接收量
     * 5PAB初值 6预计库存量 4-0现有库存量 5-0已分配量
     * 7净需求NR
     * 8计划产出量 9计划投入量
     * 10可供销售量ATP  11ATP调整后
     */
    private int[][] b; //11 * (n+1)
    /**
     * 时区指针[4] (当前、需求、计划、预测时区)
     */
    private int[] tz = new int[4];
    //时段计数
    private int tf;
    //表头信息
    //0现有库存量v 1已分配量v 2安全库存s 3批量s 4批量增量s 5提前期s 6需求时界s 7计划时界s
    //不实现s类的动态变化
    private int[] info;
    //表格ID
    //0物料号 1物料名称 2计划时间 3计划员
    private String[] id;

    //--------------------------------------------------------------------
    /**外部调用new
     * @param tf 总时段计数
     * @param method 毛需求计算方式 int
     * @param yc 预测量 int[]
     * @param ht 合同量 int[]
     * @param jhjs 计划接收量 int[]
     * @param info 表头信息 0现有库存量v 1已分配量v 2安全库存s 3批量s 4批量增量s 5提前期s 6需求时界s 7计划时界s
     * @param autoPrint 是否自动输出表格 0不输出 1无表头 2有表头

     // method
        0-毛需求量＝预测量（不考虑合同量，适合于MTS）
        1-毛需求量＝合同量（不考虑预测量，适合于MTO）
        2-毛需求量＝MAX(预测量，合同量)（适合于既有预测又有合同的企业）
        3-毛需求量＝预测量＋合同量
        4-毛需求量＝合同量（在需求时区内）  毛需求量＝预测量（在需求时区外）
    `   5-毛需求量＝合同量（在需求时区内）  毛需求量＝MAX(预测量，合同量)（在需求时区外）
        6-毛需求量＝合同量（在需求时区）   毛需求量＝预测量（在预测时区） 毛需求量＝MAX(预测量，合同量)（在计划时区）
    */
    public MP(int tf, int method, int[] yc, int[] ht,int[] jhjs, int[] info, int autoPrint){
        this.tf=tf;
        this.tz = new int[]{0,info[6],info[7],tf};
        this.b=new int[12][tf+1];
        int i,j;
        for(i=0;i<11;i++){for(j=0;j<tf+1;j++){b[i][j]=0;}}
        for(i=0;i<tf;i++){b[0][i]=i;b[1][i+1]=yc[i];b[2][i+1]=ht[i];b[4][i+1]=jhjs[i];}
        b[4][0]=info[0];b[5][0]=info[1];
        this.info=info;

        this.weave(method);
        if(autoPrint!=0)this.printTable();
    }
    public MP(int[][] b, int method, int[] info, int autoPrint){
        this.tz = new int[]{0,info[6],info[7],b[0].length-1};
        this.b=b;
        b[4][0]=info[0];b[5][0]=info[1];
        this.info=info;

        this.weave(method);
        if(autoPrint!=0){this.printTable();}
    }

    //--------------------------------------------------------------------
    /**
     * 模块测试区
     */
    public static void main(String[] args) throws Exception {
        MP test = new MP();
        test.writeData();
        test.weave(5);
        test.printTable();
    }
    public MP(){//
    }
    //产生数据
    private void writeData(){
        tf=11;//change me  时段数
        tz=new int[]{0,3,8,11};//change me  时区
        int[] yc={60, 60,60,60,60,60,60, 60, 60,60,60};//change me
        int[] ht={110,80,50,70,50,60,110,150,50,0 ,20};//change me
        info = new int[]{100,20,50,100,100,1,3,8};

        b=new int[12][tf+1];
        int i,j;
        for(i=0;i<11;i++){for(j=0;j<tf+1;j++){b[i][j]=0;}}
        for(i=0;i<tf;i++){b[0][i]=i;b[1][i+1]=yc[i];b[2][i+1]=ht[i];}

        b[4][0]=info[0];b[5][0]=info[1];//change me   b[4][0]存放现有库存量  b[5][0]存放已分配量
        tz[0]=0;//时段归零
        b[4][1]=100;
    }

    //--------------------------------------------------------------------
    //输出数据
    //terminal方式
    public void printTable(){
        String nl="\t\t";//数据区长度间隔  \t 或 \t\t
        //生成表头
        int i=0;
        String l1="",l2="",l3="";
        l1+="|"+tzh[1];l1+="-"+tz[1];for(i=0;i<tz[1]-3+nl.length();i++){l1+=nl;}
        l1+="|"+tzh[2];l1+="-"+tz[2];for(i=tz[1];i<tz[2]-3+nl.length();i++){l1+=nl;}
        l1+="|"+tzh[3];l1+="-"+tz[3];for(i=tz[2];i<tz[3]-4+nl.length();i++){l1+=nl;}l1+="|";
        for(i=1;i<b[0].length;i++){l2+=((i)+nl);}
        System.out.println(tzh[4]+ "\t\t\t\t"+ l1);
        System.out.println(head[0]+"\t"+tzh[0]+nl.substring(nl.length()-1)+l2);
        //加入数据
        l3="";l3+=head[1]+"\t\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[1][i])+nl);}System.out.println(l3);
        l3="";l3+=head[2]+"\t\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[2][i])+nl);}System.out.println(l3);
        l3="";l3+=head[3]+"\t\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[3][i])+nl);}System.out.println(l3);
        l3="";l3+=head[4]+"\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[4][i])+nl);}System.out.println(l3);
        l3="";l3+=head[5]+"\t\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[5][i])+nl);}System.out.println(l3);
        l3="";l3+=head[6]+"\t"+b[6][0]+nl;for(i=1;i<b[0].length;i++){l3+=((b[6][i])+nl);}System.out.println(l3);
        l3="";l3+=head[7]+"\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[7][i])+nl);}System.out.println(l3);
        l3="";l3+=head[8]+"\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[8][i])+nl);}System.out.println(l3);
        l3="";l3+=head[9]+"\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[9][i])+nl);}System.out.println(l3);
        l3="";l3+=head[10]+""+nl;for(i=1;i<b[0].length;i++){l3+=((b[10][i])+nl);}System.out.println(l3);
        l3="";l3+=head[11]+"\t"+nl;for(i=1;i<b[0].length;i++){l3+=((b[11][i])+nl);}System.out.println(l3);//可不输出

    }

    //getter & setter
    public int[][] getData(){   //unsafe
        return b;
    }
    public void setData(int[][] in){   //unsafe
        this.b=in;
    }
    public int setData(int head, int tf, int value){   //unsafe
        if(head<b.length&&head>=0&&tf<b[0].length&&tf>=0){
            b[head][tf]=value;return 0;
        }else{return 1;}
    }

    //--------------------------------------------------------------------
    //实现算法
    public void weave(int method){
        //如果修改了表格数据需重新计算 则调用对应的 weaveX(,[tzc])
        //外部调用只能全盘重算//
        //所有方法对this.b操作无返回值
        weave1(method);//change me
    }
    /**
     * 推算毛需求  b3
     * @param method 计算方式
     * use 需求预测量,合同订货量
     * return 毛需求量
     */
    private void weave1(int method){
    /*  method
        0-毛需求量＝预测量（不考虑合同量，适合于MTS）
        1-毛需求量＝合同量（不考虑预测量，适合于MTO）
        2-毛需求量＝MAX(预测量，合同量)（适合于既有预测又有合同的企业）
        3-毛需求量＝预测量＋合同量
        4-毛需求量＝合同量（在需求时区内）  毛需求量＝预测量（在需求时区外）
    `   5-毛需求量＝合同量（在需求时区内）  毛需求量＝MAX(预测量，合同量)（在需求时区外）
        6-毛需求量＝合同量（在需求时区）   毛需求量＝预测量（在预测时区） 毛需求量＝MAX(预测量，合同量)（在计划时区）
    */
        switch(method){
            case 0:for(int i=1;i<b[0].length;i++) {b[3][i] = b[1][i];}break;
            case 1:for(int i=1;i<b[0].length;i++) {b[3][i] = b[2][i];}break;
            case 2:for(int i=1;i<b[0].length;i++) {b[3][i] = Math.max(b[1][i], b[2][i]);}break;
            case 3:for(int i=1;i<b[0].length;i++) {b[3][i] = b[1][i]+b[2][i];}break;
            case 4:for(int i=1;i<b[0].length;i++) {if(i<=tz[1]){b[3][i] = b[2][i];}else{b[3][i] = b[1][i];}}break;
            case 5:for(int i=1;i<b[0].length;i++) {if(i<=tz[1]){b[3][i] = b[2][i];}else{b[3][i] = Math.max(b[1][i], b[2][i]);}}break;
            case 6:for(int i=1;i<b[0].length;i++) {if(i<=tz[1]){b[3][i] = b[2][i];}else{if(i<=tz[2]){b[3][i] = b[1][i];}else{Math.max(b[1][i], b[2][i]);}}}break;
        }
        weave2();
    }
    /**
     * 计算当期预计可用库存量
     * 当期预计可用库存量＝现有库存量－已分配量- - **安全库存
     * use b[4][0]存放现有库存量  b[5][0]存放已分配量
     * return b[6][0]
     */
    private void weave2(){
        b[6][0]=b[4][0]-b[5][0];
        weave3(++tz[0]);
    }
    /**
     * 推算PAB初值
     * PAB初值5＝上期末预计可用库存量6＋计划接收量4－毛需求量3
     */
    private void weave3(int tzc){
        if(tzc>b[0].length-1){weave8();}else{
        b[5][tzc]=b[6][tzc-1]+b[4][tzc]-b[3][tzc];
        weave4(tzc);}
    }
    /**
     * 推算净需求
     * 当PAB初值5 ≥ 安全库存时info2，净需求7＝0；
     * 当PAB初值 < 安全库存时，净需求＝安全库存－PAB初值（上期末预计可用库存量＋计划接收量－毛需求量）
     */
    private void weave4(int tzc){
        if(b[5][tzc]>=info[2]){b[7][tzc]=0;}else{b[7][tzc]=info[2]-b[5][tzc];}
        weave5(tzc);
    }
    /**
     * 推算计划产出量
     * 当(use)净需求b7大于零时，按照(use)批量规则info3 info4确定return计划产出量b8。
     */
    private void weave5(int tzc){
        b[8][tzc]=0;
        if(b[7][tzc]>0){
            b[8][tzc]+=info[3];
            while(b[8][tzc]<b[7][tzc]){
                b[8][tzc]+=info[4];
            }
        }
        weave6(tzc);
    }
    /**
     * 推算预计可用库存量
     * 预计可用库存量6＝计划产出量8＋PAB初值5
     */
    private void weave6(int tzc){
        b[6][tzc]=b[8][tzc]+b[5][tzc];
        weave3(++tzc);//loop till table end
    }
    /**
     * 根据(use)提前期info5，return推算计划投入量b9   (use)计划产出量8
     */
    private void weave8(){
        for(int i=1;i<b[0].length;i++){
            if(b[8][i]!=0){if(i-info[5]>=0){b[9][i-info[5]]=b[8][i];}else{b[9][0]=b[8][i];}}
        }
        weave9();
    }
    /**
     * 推算可供销售量b10 b11
     * return 可供销售量＝(use)本时段计划产出量b8＋本时段计划接收量b4－下一次出现计划产出量之前各时段合同量b2之和
     */
    private void weave9(){
        int j,k=0,l=0;
        for(int i=b[0].length-1;i>=1;i--) {
            if(b[8][i]>0||i==1){  // 仅修改有计划产出量的时段
//                j=i;k=l+b[2][j];while(b[8][j]==0){k+=b[2][j];j++;}//优化算法?
//                b[10][i]=b[8][i]+b[4][i]-k;l+=k-b[2][i];
                j=i;k=0;do{k+=b[2][j];j++;}while((j<b[0].length-1)&&(b[8][j]==0||j==i));//课件算法
                b[10][i]=b[8][i]+b[4][i]-k;
                if(i==1){b[10][1]+=info[0]-info[1];}    //针对时段1的调整 添加当期预计可用库存量
            }
            if(b[10][i]<0){b[11][i]=0;}else{b[11][i]=b[10][i];}//转换为 ATP(调整后)   可不输出
        }
        //weave end
    }
}
