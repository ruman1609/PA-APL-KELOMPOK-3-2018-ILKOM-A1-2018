package project.akhir;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
//@author Kelompok 3 2019 (Rudy, Fian, Puji, Fina, Ayu, Haifa)
public class ProjectAkhir {
    static InputStreamReader isr=new InputStreamReader(System.in);
    static BufferedReader input = new BufferedReader(isr);
    
    public static Connection getConnection(){
        Connection ConnectDB = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            ConnectDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/paapl?useTimezone=true&serverTimezone=UTC","root","");
        }
        catch(Exception e){
            
        }
        return ConnectDB;
    }
    public static boolean error=false;
    public static void insert(int number,String user,String pass,String name){
        Connection connect = getConnection();
        PreparedStatement ps = null;
        try{
            String isi = "insert into relog(number,user,pass,name) values(?,?,?,?)";
            ps=connect.prepareStatement(isi);
            ps.setInt(1, number);
            ps.setString(2, user);
            ps.setString(3, pass);
            ps.setString(4, name);
            ps.executeUpdate();
        }catch(Exception e){
            error = true;
            System.out.println(" INVALID DATA!");
        }
    }
    static String[] read(int number,String login[]){
        Connection connect = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String  isi =  "select * from relog where number=?";
            ps=connect.prepareStatement(isi);
            ps.setInt(1, number);
            rs=ps.executeQuery();
            while(rs.next()){
                login[0]=rs.getString("user");
                login[1]=rs.getString("pass");
                login[2]=rs.getString("name");
            }
        }catch(Exception e){
            
        }
        return login;
    }
    static void updateDriver(int number,String nama[],double rating[], int narik[], double total[]){
        Connection connect = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String  isi =  "update driver set nama=?,rating=?,narik=?,total=? where number=?";
            ps=connect.prepareStatement(isi);
            ps.setString(1, nama[number]);
            ps.setDouble(2, rating[number]);
            ps.setInt(3, narik[number]);
            ps.setDouble(4, total[number]);
            ps.setInt(5, number);
            ps.executeUpdate();
        }catch(Exception e){
            
        }
    }
    static void readDriver(int number,String login[],double rating[],int narik[],double total[]){
        Connection connect = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String  isi =  "select * from driver where number=?";
            ps=connect.prepareStatement(isi);
            ps.setInt(1, number);
            rs=ps.executeQuery();
            while(rs.next()){
                login[number]=rs.getString("nama");
                rating[number]=rs.getDouble("rating");
                narik[number]=rs.getInt("narik");
                total[number]=rs.getDouble("total");
            }
        }catch(Exception e){
            
        }
    }
    public static int total(int pilih){
        Connection connect = getConnection();
        Statement s = null;
        ResultSet rs = null;
        int batas=-1;
        String  isi=null;
        try{
            if(pilih==0){
                isi =  "select * from relog";
            }
            else if(pilih==1){
                isi =  "select * from driver";
            }
            s=connect.prepareStatement(isi);
            rs=s.executeQuery(isi);
            while(rs.next()){
                batas = rs.getInt("number");
            }
        }catch(Exception e){
            
        }
        return batas;
    }
    
    static void register(String user, String pass, String name)throws IOException{
        int i=total(0)+1;
        error=false;
        String args[]=null;
        String fname=null,lname=null,space[]={" "};
        System.out.println("\n\n\n\t\t**********************");
        System.out.println("\t\t*     Registrasi     *");
        System.out.println("\t\t**********************");
        System.out.println("****************************************************************");
        System.out.println("*  Ketik keluar di username jika ingin keluar dari registrasi  *");
        System.out.println("****************************************************************");
        System.out.print("\n   Input Username: ");
        user=disableAlp(user,false,null,space,true,5);
        if(user.equalsIgnoreCase("keluar"))main(args);
        System.out.print("   Input Password: ");
        pass=disableAlp(pass,false,null,null,true,5);
        System.out.print("   Input nama depan: ");
        fname=disableAlp(fname,true,"[a-zA-Z]+",space,false,0);
        System.out.print("   Input nama belakang: ");
        lname=disableAlp(lname,true,"[a-zA-Z]*",null,false,0);
        name=fname + " " + lname;
        insert(i,user,pass,name);
        if(error){
            System.out.println("  Username sudah ada yang pakai");
            loading(2);
            register(user,pass,name);
        }
        else{
            System.out.println("\n\n     Selamat datang "+fname);
            loading(3);
            main(args);
        }
    }
    static String login(String nama)throws IOException{
        int dapat=0;
        int limit=total(0)+1;
        String[] args=null;
        String user[]=new String[limit],pass[]=new String[limit],name[]=new String[limit],login[]=new String[3];
        String space[]={" "};
        String us=null,pw=null;
        boolean found = false;
        System.out.println("\n\n\n\t\t*******************");
        System.out.println("\t\t*      LOGIN      *");
        System.out.println("\t\t*******************");
        System.out.println("*****************************************************************");
        System.out.println("*     Ketik keluar di username jika ingin keluar dari login     *");
        System.out.println("*          Atau ketik register untuk ke menu Registrasi         *");
        System.out.println("*****************************************************************");
        System.out.println("\n  Silahkan masukkan");
        System.out.print("   User Name: ");
        us=disableAlp(us,false,null,null,false,0);
        if(us.equals("keluar")) main(args);
        if(us.equals("register")) register(us,pw,nama);
        System.out.print("   Password: ");
        pw=disableAlp(pw,false,null,null,false,0);
        for(int i=0;i<limit;i++){
            read(i,login);
            user[i]=login[0];
            pass[i]=login[1];
            name[i]=login[2];
        }
        for(dapat=0;dapat<limit;dapat++){
            if(us.equals(user[dapat])&&pw.equals(pass[dapat])){
                found=true;
                break;
            }
        }
        if(found){
            nama = name[dapat];
            System.out.println("   Selamat datang kembali "+name[dapat]);
            return nama;
        }
        else{
            System.out.println("  Username atau password tidak sesuai");
            loading(2);
            return login(nama);
        }
    }
    static int findDriver(String login[],double rating[],int narik[],double total[]){
        int name=0;
        for(int i=0;i<6;i++) readDriver(i,login,rating,narik,total);
        name= (int) (Math.random()*6);
        return name;
    }
    static void updating(String login[],double rating[],int narik[],double total[]){
        int batas = total(1)+1;
        for(int i=0;i<batas;i++) updateDriver(i,login,rating,narik,total);
    }
    
    static String disableAlp(String huruf, boolean regex,String REGEX, String simbol[], boolean limit, int batas) throws IOException{
        int i;
        boolean run = true;
        do{
           boolean error = false;
           while(true){
               huruf=input.readLine();
               if (limit==true){
                   if(huruf.length()>=batas){
                   break;
                   }
                   else System.out.println("Tidak Valid Min."+batas);
               }
               else break;
           }
           if(regex==true){
               if(huruf.matches(REGEX)){
                   return huruf;
               }
               else{
                   System.out.println("Tidak valid");
               }
           }
           else{
               try{
                   for(i=0;i<simbol.length;i++){
                       if(huruf.contains(simbol[i])){
                           System.out.println("Tidak Valid");
                           break;
                       }
                       else return huruf;
                   }
               }
               catch(NullPointerException e){
                   return huruf;
               }
           }
        }while(run);
        return null;
    }
    static int inputNum(int batas){
        boolean run = true;
        int n=0;
        do{
            Scanner angka=new Scanner(System.in);
            try{
                n=angka.nextInt();
                if(n>batas){
                    run = false;
                }
                else{
                    System.out.println("INPUTAN TIDAK BOLEH KURAND DARI "+batas);
                }
            }
            catch(Exception e){
                System.out.println("INPUTAN SALAH");
                run = true;
            }
        }while(run);
        return n;
    }
    static double Distance(int batas){
        boolean run = true;
        double n=0;
        do{
            Scanner angka=new Scanner(System.in);
            try{
                n=angka.nextFloat();
                if(n>batas){
                    run = false;
                }
                else{
                    System.out.println("INPUTAN TIDAK BOLEH KURAND DARI "+batas);
                }
            }
            catch(Exception e){
                System.out.println("INPUTAN SALAH");
                run = true;
            }
        }while(run);
        return n;
    }
    
    private static void sortRating(String array[],double rating[],int narik[],double total[]){
        for(int i=0;i<array.length;i++){
            for(int j=0;j<array.length;j++){
                if(rating[i]>rating[j]){// < buat ascending && > buat descending
                    String temp = array[i];
                    double sementara=rating[i];
                    int bentar=narik[i];
                    double stumat=total[i];
                    array[i]=array[j];
                    rating[i]=rating[j];
                    narik[i]=narik[j];
                    total[i]=total[j];
                    array[j]=temp;
                    rating[j]=sementara;
                    narik[j]=bentar;
                    total[j]=stumat;
                }
            }
        }
    }
    private static void sortName(String array[],double rating[],int narik[],double total[]){
        for(int i=0;i<array.length;i++){
            for(int j=0;j<array.length;j++){
                if(array[i].compareTo(array[j])<0){// < buat ascending && > buat descending
                    String temp = array[i];
                    double sementara=rating[i];
                    int bentar=narik[i];
                    double stumat=total[i];
                    array[i]=array[j];
                    rating[i]=rating[j];
                    narik[i]=narik[j];
                    total[i]=total[j];
                    array[j]=temp;
                    rating[j]=sementara;
                    narik[j]=bentar;
                    total[j]=stumat;
                }
            }
        }
    }
    static boolean foundAlp = false;
    static boolean foundBoss = false;
    static int searchName(String array[],double rating[], int narik[],double total[], String key){
        int a=0, o=array.length, m;
        foundAlp=false;
        foundBoss=false;
        sortName(array,rating,narik,total);
        if(key.equalsIgnoreCase("Haifa")){
            foundBoss=true;
        }
        while(a<=o){
            m = (a+o) / 2;
            int res = key.compareToIgnoreCase(array[m]);
            if(res==0){
                foundAlp=true;
                return m;
            }
            else if(res>0){
                a=m+1;
            }
            else{
                o=m-1;
            }
        }
        return 0;
    }
    
    static void loading(int second){
        try{
            Thread.sleep(second*1000);
        }catch(Exception e){
            
        }
    }
    
    static double tempat(int tj,int tw){
        double total=0;
        double dis[][]=
        {{0, 0.65, 0.65, 4.3, 0.85},
         {0.65, 0, 1.1, 4.5, 1.4},
         {0.65, 1.1, 0, 4.8, 0.55},
         {4.3, 4.5, 4.8, 0, 3.6},
         {0.85, 1.4, 0.55, 3.6, 0}
        };
        total = dis[tj][tw];
        return total;
    }
    
    public static void main(String[] args)throws IOException{
        Scanner pil = new Scanner(System.in);
        String us=null,pw=null,nm=null,star=null,key=null;
        int pilih[]=new int[3],biaya=1500;
        int batas = total(1);
        int narik[]=new int[batas];
        double rating[]=new double[batas];
        String driver[]=new String[batas];
        double all[]=new double[batas];//total
        do{
            System.out.println("\n\n\n     Selamat datang di Bon-Ceng");
            System.out.println("    =============================");
            System.out.println("\n           * Menu *");
            System.out.println("        ***************");
            System.out.println("        *  1. Daftar  *");
            System.out.println("        *  2. Login   *");
            System.out.println("        *  3. Exit    *");
            System.out.println("        ***************");
            System.out.print(" Silahkan pilih menu: ");
            pilih[0]=inputNum(0);
            switch(pilih[0]){
                case 1:
                    register(us,pw,nm);
                    System.out.print("   Anda akan dikembalikan ke menu utama\n Tunggu sebentar..");
                    loading(3);
                    break;
                case 2:
                    nm=login(nm);
                    System.out.print(" Tunggu sebentar..");
                    loading(3);
                    do{
                        System.out.println("\n\n\n                  * Menu *");
                        System.out.println("         ****************************");
                        System.out.println("         *  1. A/Je(Antar/Jemput)   *");
                        System.out.println("         *  2. Driver Hall of Fame  *");
                        System.out.println("         *  3. Exit                 *");
                        System.out.println("         ****************************");
                        System.out.print("Silahkan pilih menu: ");
                        pilih[1]=inputNum(0);
                        switch(pilih[1]){
                            case 1://menu bonceng
                                do{
                                    boolean aotp=false;
                                    int tj,tw,out;
                                    int kerja=0;
                                    double dis=0,total=0,bayar=0,bintang=0,jumlah;
                                    String place[]={"Kampus FKTI","Pusat Komputer Un-Mul","Perpustakaan Un-Mul","Perpustakaan Daerah","Gelora 27 September Un-Mul"};
                                    String nt=null,ntt=null;
                                    do{
                                        aotp=false;
                                        System.out.println("\nSilahkan tentukan titik jemput");
                                        System.out.print("1. Kampus FKTI\t\t\t4. Perpustakaan Daerah\n2. Pusat Komputer Un-Mul\t5. Gelora 27 September Un-Mul\n3. Perpustakaan Un-Mul\t\t6. Lainnya\nPilih: ");
                                        tj=inputNum(0)-1;
                                        if(tj==5){
                                            System.out.println("Masukkan nama tempat anda akan dijemput");
                                            nt=disableAlp(nt,false,null,null,false,0);
                                        }
                                        System.out.println("Silahkan tentukan tujuan");
                                        System.out.print("1. Kampus FKTI\t\t\t4. Perpustakaan Daerah\n2. Pusat Komputer Un-Mul\t5. Gelora 27 September Un-Mul\n3. Perpustakaan Un-Mul\t\t6. Lainnya\nPilih: ");
                                        tw=inputNum(0)-1;
                                        if(tw==5){
                                            System.out.println(" Masukkan nama tempat anda akan dijemput");
                                            ntt=disableAlp(nt,false,null,null,false,0);
                                            System.out.print(" Masukkan jarak nya (dalam Km): ");
                                            dis=Distance(0);
                                        }
                                        if((tj>=0&&tj<=5)&&(tw>=0&&tw<=5)){
                                            if((tj>=0&&tj<=4)&&(tw>=0&&tw<=4)){
                                                if(tj==tw){
                                                    System.out.println(" Anda sudah berada di lokasi");
                                                    aotp=true;
                                                    break;
                                                }
                                                else{
                                                    total=tempat(tj,tw)*1500;
                                                    System.out.println(" Titik jemput:\n"+place[tj]);
                                                    System.out.println(" Tujuan:\n"+place[tw]);
                                                    System.out.println(" Biaya: Rp."+total);
                                                    break;
                                                }
                                            }
                                            else if(tj==5&&tw==5){
                                                total=dis*1500;
                                                System.out.println("Titik jemput:\n"+" "+nt);
                                                System.out.println("Tujuan:\n"+" "+ntt);
                                                System.out.println("Biaya: Rp."+total);
                                                break;
                                            }
                                            else if((tj>=0&&tj<=4)&&tw==5){
                                                total=dis*1500;
                                                System.out.println(" Titik jemput:\n"+" "+place[tj]);
                                                System.out.println(" Tujuan:\n"+" "+ntt);
                                                System.out.println(" Biaya: Rp."+" "+total);
                                                break;
                                            }
                                            else if(tj==5&&(tw>=0&&tw<=4)){
                                                System.out.print(" Masukkan jarak nya dari "+nt+" ke "+place[tw]+"(dalam Km): ");
                                                dis=Distance(0);
                                                total=dis*1500;
                                                System.out.println(" Titik jemput:\n"+nt);
                                                System.out.println(" Tujuan:\n"+place[tw]);
                                                System.out.println(" Biaya: Rp."+total);
                                                break;
                                            }
                                        }
                                        else{
                                            System.out.println(" Pilihan mu ada yang tak terdaftar di pilihan\n Coba lagi");
                                        }
                                    }while(true);
                                    if(aotp==true){
                                        do{
                                            System.out.println(" Apakah anda ingin  memilih tujuan lagi\n 1 untuk lanjut\n 0 untuk keluar");
                                            out=inputNum(-1);
                                            if(out==1){
                                                break;
                                            }
                                            else if(out==0){
                                                System.out.println(" Terima kasih telah menggunakan aplikasi kami :)");
                                                System.exit(0);
                                            }
                                            else System.out.println(" Pilihan hanya 1/0");
                                        }while(true);
                                    }
                                    else if(aotp==false){
                                        System.out.println("Sedang mencari driver..");
                                        loading(3);
                                        int ojek=findDriver(driver,rating,narik,all);
                                        if(ojek==5){
                                            System.out.println("Mohon maaf driver kami sedang sibuk");
                                            loading(3);
                                            System.exit(0);
                                        }
                                        kerja  = narik[ojek] + 1;
                                        System.out.println("\n  "+driver[ojek]+" sedang menuju ke tempat mu");
                                        loading(2);
                                        System.out.println("  Driver sudah di tempat mu");
                                        loading(1);
                                        System.out.println("  Meluncur ke lokasi\n  Pegang yang erat-erat yaa");
                                        loading(5);
                                        do{
                                            System.out.println(" Sudah sampai tujuan silahkan bayar ke driver nya sebesar\n Rp."+total);
                                            System.out.print("Bayar Rp.");
                                            bayar=inputNum(0);
                                            if(bayar<total){
                                                System.out.println("Maaf bayaran anda kurang");
                                            }
                                            else break;
                                        }while(true);
                                        double kembalian = bayar - total;
                                        System.out.println(" Kembalian nya Rp."+kembalian);
                                        System.out.println(" Terima kasih telah menggunakan aplikasi kami :)");
                                        loading(3);
                                        do{
                                            System.out.println(" Mohon berikan bintang untuk driver kami");
                                            System.out.println(" Nama driver: "+driver[ojek]);
                                            System.out.print(" Bintang: ");
                                            star=disableAlp(star,true,"[*]+",null,false,0);
                                            if(star.length()>=6){
                                                System.out.println("Mohon maaf bintang hanya 5 digit saja");
                                            }
                                            else{
                                                bintang=Integer.valueOf(star.length());
                                                System.out.println(" Terima kasih atas ulasan nya\n Semoga hari anda menyenangkan "+nm);
                                                jumlah = bintang;
                                                all[ojek]+=jumlah;
                                                bintang = all[ojek] / kerja;
                                                rating[ojek]=bintang;
                                                narik[ojek]=kerja;
                                                sortRating(driver,rating,narik,all);
                                                updating(driver,rating,narik,all);
                                                System.exit(0);
                                                break;
                                            }
                                        }while(true);
                                        break;
                                    }
                                }while(true);
                                break;
                            case 2:
                                for(int i=0;i<6;i++) readDriver(i,driver,rating,narik,all);
                                sortRating(driver,rating,narik,all);
                                System.out.println("Best of 3 driver Bon-Ceng");
                                for(int i=0;i<3;i++){
                                    System.out.println((i+1)+". "+driver[i]+" dengan rating "+rating[i]);
                                }
                                loading(3);
                                do{
                                    System.out.println("\n Ingin mencari driver andalan mu?\n (input 1 untuk iya atau 0 untuk tidak");
                                    System.out.print(" Pilih:");
                                    int search=inputNum(-1);
                                    if(search==0){
                                        break;
                                    }
                                    else if(search==1){
                                        System.out.println("\n\n\n Silahkan input nama driver yang ingin di cari");
                                        key=disableAlp(key,true,"[A-Za-z]+",null,false,0);
                                        int nemu=searchName(driver,rating,narik,all,key);
                                        if(foundAlp){
                                            System.out.println("\n Nama  : "+driver[nemu]);
                                            System.out.println(" Rating: "+rating[nemu]);
                                            loading(3);
                                        }
                                        else{
                                            System.out.println("\n Nama driver tak terdaftar");
                                        }
                                        if(foundBoss){
                                            System.out.println("\n Haifa sang CEO dari Bon-Ceng ini\n dan juga merupakan driver pertama dari Bon-Ceng\n Namun sekarang tidak aktif lagi.");
                                        }
                                    }
                                    else System.out.println("Tidak terdaftar di pilihan");
                                }while(true);
                                break;
                            case 3:
                                System.out.println("Terima kasih telah menggunakan aplikasi kami :)");
                                System.exit(0);
                            default:
                                System.out.println("Tidak ada dalam daftar");
                                break;
                        }
                    }while(true);
                case 3:
                    System.out.println("Terima kasih telah menggunakan aplikasi kami :)");
                    System.exit(0);
                default:
                    System.out.println("Tidak ada dalam daftar");
                    break;
            }
        }while(true);
    }
}