package ed;

/**
 * Created by ZWH on 4/29/2014.
 */
public class AF { //合同或预测-数据源

    public AF(String mn, int nr, long nd, String ns) {
        this.mn = mn;//物料号
        this.nr = nr;//毛需求
        this.nd = nd;//需求日期
        this.ns = ns;//需求追溯
//        this.id = id++;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public long getNd() {
        return nd;
    }

    public void setNd(long nd) {
        this.nd = nd;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    private String mn;//物料号
    private int nr;//毛需求
    private long nd;//需求日期
    private String ns;//需求追溯  C???合同 / P预测

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

//    static private int id; //条目ID
}
