package ed;

import java.util.LinkedList;

/**
 * Created by ZWH on 4/29/2014.
 */
public class ML {

    final private String[] tzh={"下达","确认","计划"};
    final private String[] head1={"供给","需求","库存"};
    final private String[] head2={"措施","加工单号","产出量","投入日期","产出日期","毛需求","需求日期","需求追溯","结余"};

    //最小分段周期 （天）
    final private int mp=7;
    //需求数据链表AF类
    private LinkedList<AF> ct;
    //措施  0基准 1下达 2确认 3计划
    private long[] st;
    //表头信息
    //0现有库存量v 1已分配量v 2安全库存s 3批量s 4批量增量s 5提前期s 6需求时界s 7计划时界s
    //不实现s类的动态变化
    private int[] info;
    /**
     * 总数据表[a][b]
     */
    private int[][] b; //11 * (n+1)
    //时段计数
    private int tf;
    //表格ID
    //0物料号 1物料名称 2计划时间 3计划员
    private String[] id;
//    //表格AF-ID序列
//    private int[] od;
    //竖式表格输出缓冲
    //"措施"
    String[][] b1;//"加工单号 "需求追溯",
    long[][] b2;//,"投入日期","产出日期""需求日期"
    int[][] b3;//"产出量"毛需求",","结余"


    /**
     * 竖式表格
     * @param ct 合同和预测数据源 AF类
     * @param tf 时段数
     * @param info 表头信息
     * @param autoPrint 自动表格输出
     */
    public ML(LinkedList<AF> ct, int tf, int[] info, int autoPrint){
        this.ct = ct;
        this.tf=tf;
        this.info = info;
        st= new long[]{0,info[6],info[7],tf};
        b[4][0]=info[0];b[5][0]=info[1];

        this.arrangeData();
        MP c = new MP(b,5,info,0);
        this.b = c.getData();
        orderData();
        if(autoPrint!=0)this.printTable();
    }

    //--------------------------------------------------------------------
    /**
     * 模块测试区
     */
    public static void main(String[] args) throws Exception {
        new ML();
    }
    public ML(){
        this.writeData();
        this.arrangeData();
        MP c = new MP(b,5,this.info,0);
        this.b = c.getData();
        this.orderData();
        c.printTable();
        this.printTable();
    }

    //产生数据
    public void writeData(){
        ct = new LinkedList<AF>();
        ct.add(new AF("1001",110,7*1 ,"c511"));
        ct.add(new AF("1001",80, 7*2 ,"c513"));
        ct.add(new AF("1001",50, 7*3 ,"c524"));
        ct.add(new AF("1001",70, 7*4 ,"c533"));
        ct.add(new AF("1001",50, 7*5 ,"c535"));
        ct.add(new AF("1001",10, 7*5 ,"p"));
        ct.add(new AF("1001",60, 7*6 ,"c546"));
        ct.add(new AF("1001",110,7*7 ,"c549"));
        ct.add(new AF("1001",150,7*8 ,"c552"));
        ct.add(new AF("1001",50, 7*9 ,"c560"));
        ct.add(new AF("1001",10, 7*9 ,"p"));
        ct.add(new AF("1001",60, 7*10,"p"));
        ct.add(new AF("1001",20, 7*11,"c566"));
        ct.add(new AF("1001",40, 7*11,"p"));
        tf=11;
        st=new long[]{0,7*3,7*8,7*11};
        info = new int[]{100,20,50,100,100,1,3,8};
    }

    //整理数据
    public  void arrangeData() {
        this.b=new int[12][tf+1];
//        this.od = new int[ct.size()];
        int i,j;
        for(i=0;i<11;i++){for(j=0;j<tf+1;j++){b[i][j]=0;}}
        for(i=0;i<tf;i++){b[0][i]=i;}
        for(i=0;i<ct.size();i++){
            if(ct.get(i).getNs().charAt(0)=='p'){
                b[1][(int)(ct.get(i).getNd()/mp)]+=ct.get(i).getNr();
            }else {
                b[2][(int)(ct.get(i).getNd()/mp)]+=ct.get(i).getNr();
            }
//            od[i]=ct.get(i).getId();
        }
        //od对需求日期升序和追溯A.P 省略
        b1 = new String[2][ct.size()] ;//"加工单号 "需求追溯",
        b2 = new long[3][ct.size()];//,"投入日期","产出日期""需求日期"
        b3 = new int[3][ct.size()];//产出量","毛需求"","结余"
        for(i=0;i<ct.size();i++){//init
            b1[0][i]="-";b1[1][i]="";
            b2[0][i]=0;b2[1][i]=0;b2[2][i]=0;
            b3[0][i]=0;b3[1][i]=0;
        }
//            b1"加工单号 "需求追溯",
//            b2,"投入日期","产出日期""需求日期"
//            b3产出量","毛需求"","结余"
        for(i=0;i<ct.size();i++){
            b1[1][i]=ct.get(i).getNs();//需求追溯
            b2[2][i]=ct.get(i).getNd();//需求日期
            b3[1][i]=ct.get(i).getNr();//毛需求
        }
    }

    private void orderData() {
        int i,j;
        for(i=0;i<b[0].length;i++){
            if(b[8][i]>=0){
                System.out.print(i+"\t");
                for(j=0;j<ct.size();j++){
                    if(((long)(b[0][i]*mp)==b2[2][j])&&(!b1[1][j].equals("p"))){//匹配b时段和需求日期 且不是预测行
                        b2[1][j]=b[0][i]*mp;
                        b2[0][j]=(   ((b[0][i]-info[5]>=0)?(b[0][i]-info[5]):0)  )*mp;
                        b3[0][j]=b[8][i];//产出量
                    }
                }
            }
        }
//            b1"加工单号 "需求追溯",
//            b2,"投入日期","产出日期""需求日期"
//            b3产出量","毛需求"","结余"
//          b[][]
//          0时段 1预测量 2合同量
//          3毛需求 4计划接收量
//          5PAB初值 6预计库存量 4-0现有库存量 5-0已分配量
//          7净需求NR
//          8计划产出量 9计划投入量
//          10可供销售量ATP  11ATP调整后

        b3[2][0]=70;
        for(j=1;j<ct.size();j++){//结余
            b3[2][j]=b3[2][j-1]+b3[0][j]-b3[1][j];
        }
    }

    //--------------------------------------------------------------------
    //输出数据
    //terminal方式
    public void printTable() {
        String nl="\t\t";//数据区长度间隔  \t 或 \t\t
        //生成表头
        int i=0;
        String l1="",l2="",l3="";
        l1+="|"+head1[0]+nl+nl+nl+nl+nl+nl;l1+="|"+head1[1]+nl+nl+nl+"\t";l1+="|"+head1[2]+nl;
        l2+="|"+head2[0]+"\t";l2+="|"+head2[1]+"\t";l2+="|"+head2[2]+"\t";
        l2+="|"+head2[3]+"\t";l2+="|"+head2[4]+"\t";l2+="|"+head2[5]+"\t";
        l2+="|"+head2[6]+"\t";l2+="|"+head2[7]+"\t";l2+="|"+head2[8]+"\t";
//        for(i=1;i<b[0].length;i++){l2+=((i)+nl);}
        System.out.println(l1);
        System.out.println(l2);
        //加入数据
        i=0;
        while(b2[2][i]<=st[1]){
            l3="";l3+=tzh[0]+nl;
            //"措施","加工单号","产出量","投入日期","产出日期","毛需求","需求日期","需求追溯","结余"};
//            l3+="-"+nl;l3+=b[8][(int)st[0]+1]+nl;
//            l3+=st[0]-mp*info[5]+nl+nl;l3+=st[0];
//            l3+=ct.get(i).getNr()+nl;l3+=ct.get(i).getNd()+nl; l3+=ct.get(i).getNs()+nl;
//            if(ct.size()>=(i+1)&&ct.get(i).getNd()!=ct.get(i+1).getNd()){
//                l3+=b[6][(int)st[0]]+nl;
//                st[0]++;
//            }
            l3+=b1[0][i]+nl+b3[0][i]+nl+"\t"+b2[0][i]+nl+"\t"+b2[1][i]+nl+"\t"
                    +b3[1][i]+nl  +b2[2][i]+nl+"\t"   +b1[1][i]+nl;
            if(b1[1][i].equals("p")){l3+=""+"\t";}else{l3+="";}
            l3+=b3[2][i]+nl;
//            b1"加工单号 "需求追溯",
//            b2,"投入日期","产出日期""需求日期"
//            b3产出量","毛需求"","结余"
            System.out.println(l3);
            i++;
        }
        while(b2[2][i]<=st[2]){
            l3="";l3+=tzh[1]+nl;
            l3+=b1[0][i]+nl+b3[0][i]+nl+"\t"+b2[0][i]+nl+"\t"+b2[1][i]+nl+"\t"
                    +b3[1][i]+nl  +b2[2][i]+nl+"\t"   +b1[1][i]+nl;
            if(b1[1][i].equals("p")){l3+=""+"\t";}else{l3+="";}
            l3+=b3[2][i]+nl;
            System.out.println(l3);
            i++;
        }
        while((i<b2[2].length)&&b2[2][i]<=st[3]){
            l3="";l3+=tzh[2]+nl;
            l3+=b1[0][i]+nl+b3[0][i]+nl+"\t"+b2[0][i]+nl+"\t"+b2[1][i]+nl+"\t"
                    +b3[1][i]+nl  +b2[2][i]+nl+"\t"   +b1[1][i]+nl;
            if(b1[1][i].equals("p")){l3+=""+"\t";}else{l3+="";}
            l3+=b3[2][i]+nl;
            System.out.println(l3);
            i++;
        }



    }

}
