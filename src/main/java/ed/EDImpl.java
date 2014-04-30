package ed;

/**
 * Created by ZWH on 4/25/2014.
 */
public class EDImpl {
    final private String OW = "this is a test message";
    final private String G = "FFFFFFFF";

    private byte[] EW;
    private byte[] te, ta, tc;

    public EDImpl() {
        byte[] k = gen(G);
//        System.out.println("genK>>" +
//                Integer.toHexString(
//                        Integer.parseInt(
//                                ((String)(G+"")),16
//                        )
//                )
//        );
//        Integer.toBinaryString(int i);
        System.out.println("genK>>" +te.toString());
//        byte[] o = tob(OW);
//        System.out.println("OW2B>>" + o);
//        byte[] es = ec(k, o);//en
//        System.out.println("EC>>" + es);
//        byte[] ds = dc(k, es);//de
//        System.out.println("DC>>" + ds);
//        String OT = tos(ds);
//        System.out.println("B2TW>>" + OT);


//        int kae=128;
//        (double)(kae>>4)+ (byte)0
//        System.out.println( (('好') << 0)|(('沙') << 0));
//        System.out.println( ("好wertj").getBytes().length << 0);
        System.out.println(G);
    }


    private byte[] gen(String  ge) {
        int size = 64;//genLength
        te = new byte[size];
        int i;
        for (i=0;i<size;i++){te[i]='1';}
//        byte[] st = ge.getBytes();
//        int lg=st.length;
//        int tr = size*16/lg+1;
//        for(i=0;i<tr)
//        int[] p = new int[st.length];
//        for (i=0;i<st.length;i++){p[i] =  Integer.parseInt(Integer.toHexString(st[i]),16);}
//        for (i=0;i<p.length;i++){gen[i%size]=(byte)p[i];}
        return te;
    }

    private byte[] tob(String ow) {
        return ow.getBytes();
    }

    private byte[] ec(byte[] k, byte[] o) {
        return new byte[6];
    }


    private byte[] dc(byte[] k, byte[] es) {
        return new byte[6];
    }


    private String tos(byte[] ds) {
        return new String(ds);
    }


    public static void main(String[] args) throws Exception {
        EDImpl test = new EDImpl();
    }

}
