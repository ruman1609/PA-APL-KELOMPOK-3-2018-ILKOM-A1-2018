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
    
    public static Connection getConnection(){//connect kan ke database
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
    static void updateDriver(int number,String nama[],double rating[], int narik[], double total[]){// mengupdate data driver
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
    static void readDriver(int number,String login[],double rating[],int narik[],double total[]){// masukin data driver ke variabel
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
            loading(1);
            register(user,pass,name);
        }
        else{
            System.out.println("\n\n\n     Registrasi anda berhasil "+fname+"\n     Kembali ke menu utama");
            loading(1);
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
            System.out.println("\n\n\n   Selamat datang kembali "+name[dapat]);
            return nama;
        }
        else{
            System.out.println("  Username atau password tidak sesuai");
            loading(1);
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
                   System.out.println("Tidak valid, hanya boleh "+REGEX);
               }
           }
           else{
               try{
                   for(i=0;i<simbol.length;i++){
                       if(huruf.contains(simbol[i])){
                           System.out.println("Tidak valid, tidak boleh ada "+simbol[i]);
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
        String numbah=null;
        do{
            BufferedReader angka = new BufferedReader(isr);
            try{
                numbah=angka.readLine();
                n=Integer.valueOf(numbah);
                if(n>batas){
                    run = false;
                }
                else{
                    System.out.println("INPUTAN TIDAK BOLEH KURAND DARI "+batas);
                }
            }
            catch(Exception e){
                System.out.println("INPUTAN SALAH HANYA BOLEH ANGKA!");
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
                System.out.println("INPUTAN SALAH HANYA BOLEH ANGKA!");
                run = true;
            }
        }while(run);
        return n;
    }
    
    private static void sortRating(String array[],double rating[],int narik[],double total[]){// sorting
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
    static int searchName(String array[],double rating[], int narik[],double total[], String key){//binary search
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
            Thread.sleep(second*1000);// loading berdasarkan detik
        }catch(Exception e){
            
        }
    }
    
    static double tempat(int tj,int tw){// lokasi berdasar kan di notepad urutan nya
        double total=0;
        double dis[][]=
        {{0, 0.35, 0.09, 0.35, 0.65, 0.7, 0.7, 0.45, 1.1, 1.7, 1.2, 1.1, 1.7, 0.3, 0.7, 0.65, 0.7, 0.4, 0.55, 1.1},  // FKTI
         {0.35, 0, 0.35, 0.1, 0.35, 0.75, 0.5, 0.3, 1.2, 1.8, 1.3, 1.1, 1.8, 0.4, 0.6, 0.75, 0.7, 0.35, 0.65, 1.2},
         {0.09, 0.35, 0, 0.7, 0.6, 0.65, 0.7, 0.4, 1.1, 1.6, 1.2, 1, 1.6, 0.27, 0.45, 0.6, 0.65, 0.35, 0.5, 1},
         {0.35, 0.1, 0.7, 0, 0.65, 1, 0.8, 0.9, 1.5, 2, 1.6, 1.4, 2, 0.75, 0.6, 1.1, 1, 0.65, 1, 1.4},
         {0.65, 0.35, 0.6, 0.65, 0, 0.4, 0.13, 0.45, 0.8, 1.4, 0.9, 0.75, 1.4, 0.29, 0.2, 0.45, 0.35, 0.23, 0.5, 0.75},
         {0.7, 0.75, 0.65, 1, 0.4, 0, 0.13, 0.3, 0.4, 1, 0.5, 0.35, 0.95, 0.4, 0.5, 0.13, 0.043, 0.5, 0.22, 0.4},
         {0.7, 0.5, 0.7, 0.8, 0.13, 0.13, 0, 0.6, 0.7, 1.3, 0.8, 0.7, 1.2, 0.45, 0.23, 0.35, 0.29, 0.35, 0.45, 0.7},
         {0.45, 0.6, 0.4, 0.9, 0.45, 0.3, 0.6, 0, 0.75, 1.3, 0.85, 0.7, 1.2, 0.14, 0.65, 0.22, 0.35, 0.22, 0.14, 0.7},
         {0.75, 0.85, 0.65, 1.2, 0.8, 0.4, 0.7, 0.75, 1.4, 0.95, 0.8, 1.3, 0.45, 0.85, 0.35, 0.4, 0.27, 0.5, 0.27, 0.8},
         {1.1, 1.2, 1.5, 1.1, 0.65, 1, 1.3, 1.3, 1.4, 0, 0.18, 0.7, 0.5, 0.8, 1.2, 0.7, 0.75, 0.6, 0.85, 0.6, 0.55},
         {1.2, 1.3, 1.2, 1.6, 0.9, 0.5, 0.8, 0.85, 0.95, 0.5, 0, 0.55, 0.45, 0.65, 1.1, 0.55, 0.6, 0.75, 0.5, 0.45},
         {1.1, 1.1, 1, 1.4, 0.75, 0.35, 0.7, 0.7, 0.8, 1, 0.55, 0, 1, 0.65, 1.1, 0.5, 0.65, 0.75, 0.5, 0.45},
         {1.7, 1.8, 1.6, 2, 1.4, 0.95, 1.2, 1.2, 1.3, 0.15, 0.35, 0.85, 0, 1, 1.4, 0.85, 0.9, 1, 0.8, 0.75},
         {0.3, 0.4, 0.27, 0.75, 0.29, 0.4, 0.45, 0.14, 0.45, 1.4, 0.9, 0.75, 1.3, 0, 0.5, 0.3, 0.35, 0.067, 0.21, 0.8},
         {0.7, 0.3, 0.45, 0.6, 0.2, 0.5, 0.23, 0.65, 1.3, 1.5, 1, 0.85, 1.4, 0.5, 0.5, 0, 0.45, 0.4, 0.65, 0.95},
         {0.65, 0.75, 0.6, 1.1, 0.45, 0.13, 0.35, 0.22, 0.5, 1.1, 0.6, 0.5, 1, 0.3, 0.5, 0, 0.071, 0.4, 0.12, 0.5},
         {0.7, 0.7, 0.65, 1, 0.35, 0.043, 0.29, 0.35, 0.75, 1.3, 0.85, 0.7, 1.2, 0.5, 0.75, 0.24, 0, 0.45, 0.35, 0.7},
         {0.4, 0.35, 0.35, 0.65, 0.23, 0.5, 0.35, 0.22, 0.5, 0.9, 1, 0.85, 1.4, 0.067, 0.4, 0.4, 0.6, 0, 0.3, 0.85},
         {0.55, 0.65, 0.5, 1, 0.5, 0.22, 0.45, 0.14, 0.65, 1.2, 0.75, 0.6, 1.1, 0.21, 0.65, 0.12, 0.19, 0.3, 0, 0.6},
         {1.1, 1.2, 1, 1.4, 0.75, 0.44, 0.7, 0.7, 0.35, 0.9, 0.45, 0.45, 0.85, 0.55, 0.95, 0.45, 0.5, 0.6, 0.3, 0} // GOR 27 SEPTEMBER
        };
        total = dis[tj][tw];
        return total;
    }
    static void namaTempat(){
        System.out.println("\n\n******************************************************************************************************************************");
        System.out.println("*     1. Kampus FKTI           |     6.  Kampus FEB          |     11. Kampus F.Teknik         |    16. Perpustakaan         *");
        System.out.println("|                              *                             *                                 *                             |");
        System.out.println("*     2. Pusat Komputer FKTI   |     7.  Kampus FAPERTA      |     12. Kampus F.Kedokteran     |    17. Gedung Rektorat      *");
        System.out.println("|                              *                             *                                 *                             |");
        System.out.println("*     3. Kampus FAHUTAN        |     8.  Kampus FKIP         |     13. Kampus F.KESMAS         |    18. Student Center       *");
        System.out.println("|                              *                             *                                 *                             |");
        System.out.println("*     4. Kampus FMIPA          |     9.  Kampus FPIK         |     14. Kampus F.Farmasi        |    19. Gedung MPK           *");
        System.out.println("|                              *                             *                                 *                             |");
        System.out.println("*     5. Kampus FISIPOL        |     10. Kampus F.Hukum      |     15. Kampus FIB              |    20. GOR 27 September     *");
        System.out.print("******************************************************************************************************************************");
    }
    
    public static void main(String[] args)throws IOException{
        Scanner pil = new Scanner(System.in); // main variabel
        String us=null,pw=null,nm=null,star=null,key=null;
        int pilih[]=new int[3];
        int batas = total(1);
        int narik[]=new int[batas];
        double rating[]=new double[batas];
        String driver[]=new String[batas];
        double all[]=new double[batas];//total
        //akhir main variabel
        do{
            System.out.println("\n\n\n     Selamat datang di Bon-Ceng Universitas Mulawarman");
            System.out.println("    ===================================================");
            System.out.println("\n\t           * Menu *");
            System.out.println("\t        ***************");
            System.out.println("\t        *  1. Daftar  *");
            System.out.println("\t        *  2. Login   *");
            System.out.println("\t        *  3. Exit    *");
            System.out.println("\t        ***************");
            System.out.print("        Silahkan pilih menu: ");
            pilih[0]=inputNum(0);
            switch(pilih[0]){
                case 1:
                    register(us,pw,nm);
                    System.out.print("   Anda akan dikembalikan ke menu utama\n Tunggu sebentar..");
                    loading(1);
                    break;
                case 2:
                    nm=login(nm);
                    System.out.print(" Tunggu sebentar..");
                    loading(2);
                    do{
                        System.out.println("\n\n\n\t                  * Menu *");
                        System.out.println("\t        ****************************");
                        System.out.println("\t        *  1. A/Je(Antar/Jemput)   *");
                        System.out.println("\t        *  2. Driver Hall of Fame  *");
                        System.out.println("\t        *  3. Exit                 *");
                        System.out.println("\t        ****************************");
                        System.out.print("        Silahkan pilih menu: ");
                        pilih[1]=inputNum(0);
                        switch(pilih[1]){
                            case 1://menu bonceng
                                do{
                                    boolean aotp=false;
                                    int tj,tw,out;
                                    int kerja=0;
                                    double total=0,bayar=0,bintang=0,jumlah;
                                    String place[]={"Kampus FKTI","Pusat Komputer FKTI","Kampus FAHUTAN","Kampus FMIPA","Kampus FISIPOL","Kampus FEB","Kampus FAPERTA","Kampus FKIP","Kampus FPIK","Kampus F.Hukum"
                                            ,"Kampus F.Teknik","Kampus F.Kedokteran","Kampus F.KESMAS","Kampus F.Farmasi","Kampus FIB","Perpustakaan","Gedung Rektorat","Student Center","Gedung MPK","GOR 27 September"};
                                    do{
                                        aotp=false;
                                        namaTempat();
                                        System.out.println("\n   Silahkan tentukan titik jemput");
                                        System.out.print("  Pilih: ");
                                        tj=inputNum(0)-1;
                                        namaTempat();
                                        System.out.println("\n   Silahkan tentukan tujuan");
                                        System.out.print("  Pilih: ");
                                        tw=inputNum(0)-1;
                                        if((tj>=0&&tj<=19)&&(tw>=0&&tw<=19)){
                                            if(tj==tw){
                                                System.out.println(" Anda sudah berada di lokasi");
                                                aotp=true;
                                                break;
                                            }
                                            else{
                                                total=tempat(tj,tw)*1000;// harga nya 1000 per km.
                                                System.out.println(" Titik jemput:\n"+place[tj]);
                                                System.out.println(" Tujuan:\n"+place[tw]);
                                                System.out.println(" Biaya: Rp."+total);
                                                System.out.println(" Estimasi jarak "+tempat(tj,tw)+"-an Km");
                                                System.out.println(" Dengan biaya Rp.1000 per Km");
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
                                        loading(2);
                                        int ojek=findDriver(driver,rating,narik,all);
                                        if(ojek==5){
                                            System.out.println("Mohon maaf driver kami sedang sibuk");
                                            loading(2);
                                            System.exit(0);
                                        }
                                        kerja  = narik[ojek] + 1;
                                        System.out.println("\n  "+driver[ojek]+" sedang menuju ke tempat mu");
                                        loading(1);
                                        System.out.println("  Driver sudah di tempat mu");
                                        loading(1);
                                        System.out.println("  Meluncur ke lokasi\n  Pegang yang erat-erat yaa");
                                        loading(3);
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
                                        loading(1);
                                        do{
                                            System.out.println("\n\n\n Mohon berikan bintang untuk driver kami");
                                            System.out.println(" Nama driver: "+driver[ojek]);
                                            System.out.print(" Bintang(*)(1-5): ");
                                            star=disableAlp(star,true,"[*]+",null,false,0);
                                            if(star.length()>=6){
                                                System.out.println("Mohon maaf bintang hanya 5 digit saja");
                                            }
                                            else{
                                                bintang=Integer.valueOf(star.length());
                                                System.out.println(" Terima kasih atas ulasan nya\n Semoga hari anda menyenangkan "+nm);
                                                jumlah = bintang;//proses penghitungan biar update
                                                all[ojek]+=jumlah;
                                                bintang = all[ojek] / kerja;
                                                rating[ojek]=bintang;
                                                narik[ojek]=kerja;
                                                sortRating(driver,rating,narik,all);
                                                updating(driver,rating,narik,all);
                                                //selesai
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
                                    System.out.print(" Pilih: ");
                                    int search=inputNum(-1);
                                    if(search==0){
                                        break;
                                    }
                                    else if(search==1){
                                        System.out.println("\n\n\n Silahkan input nama driver yang ingin di cari");
                                        key=disableAlp(key,true,"[A-Za-z]+",null,false,0);
                                        int nemu=searchName(driver,rating,narik,all,key);
                                        if(foundAlp){
                                            System.out.println("\n\n Nama  : "+driver[nemu]);
                                            System.out.println(" Rating: "+rating[nemu]);
                                            loading(1);
                                        }
                                        else{
                                            System.out.println("\n Nama driver tak terdaftar");
                                        }
                                        if(foundBoss){
                                            System.out.println("\n\n Haifa sang CEO dari Bon-Ceng ini\n dan juga merupakan driver pertama dari Bon-Ceng\n Namun sekarang tidak aktif lagi.");
                                        }
                                    }
                                    else System.out.println("Tidak terdaftar di pilihan");
                                }while(true);
                                break;
                            case 3:
                                do{
                                    System.out.println("\n\n  Apakah anda ingin keluar dari program");
                                    System.out.println("  Tekan 1 untuk keluar / 0 untuk tidak");
                                    int pilihanOUT=inputNum(-1);
                                    if(pilihanOUT==1){
                                        System.out.println("\n\nTerima kasih telah menggunakan aplikasi kami :)");
                                        System.exit(0);
                                    }
                                    else if(pilihanOUT==0) break;
                                    else System.out.println("Tidak ada di daftar");
                                }while(true);
                                break;
                            default:
                                System.out.println("Tidak ada dalam daftar");
                                break;
                        }
                    }while(true);
                case 3:
                    do{
                        System.out.println("\n\n  Apakah anda ingin keluar dari program");
                        System.out.println("  Tekan 1 untuk keluar / 0 untuk tidak");
                        int pilihanOUT=inputNum(-1);
                        if(pilihanOUT==1){
                            System.out.println("\n\nTerima kasih telah menggunakan aplikasi kami :)");
                            System.exit(0);
                        }
                        else if(pilihanOUT==0) break;
                        else System.out.println("Tidak ada di daftar");
                    }while(true);
                    break;
                default:
                    System.out.println("Tidak ada dalam daftar");
                    break;
            }
        }while(true);
    }
}
