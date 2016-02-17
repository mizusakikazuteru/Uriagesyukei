package Uriagesyukei;

ああああああああああ
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.security.KeyStore.Entry;

public class Uriagesyukei {
    // ******支店定義ファイル*********//


    public static void main(String[] args) {
        // コマンドライン引数でパスを受ける
        File filepath = new File(args[0]);
        // 指定したディレクトリ・ファイル一覧をFile型の配列で返す。
        File[] filelist = filepath.listFiles();
        // ファイルの存在確認
        File branch = new File(args[0], "branch.lst");
        if (!branch.isFile()) {
            System.out.println("支店定義ファイルが存在しません");
        }

        // キーと値を生成
        HashMap<String, String> branchlist = new HashMap<String, String>();

        try {
            // ファイル読み込み
            BufferedReader br = new BufferedReader(new FileReader(new File(
                    args[0], "branch.lst")));
            String line;

            // 一行ずつファイルを読み込む。
            while ((line = br.readLine()) != null) {

                String[] code = line.split(","); // splitメソッドでlineをカンマで分割
                branchlist.put(code[0], code[1]);//キーと値を追加する(mapに作る）

                // System.out.println("Map中身"+branchlst);

                if (!code[0].matches("^\\d{3}")) {
                    System.out.println("支店ファイルのフォーマットが不正です");
                    // 支店コード3桁ならtrue
                } else {

                    branchlist.get(code[0]);// getメソッドで追加する。(取得する）
                    System.out.print(code[0]);
                }
                branchlist.get(code[1]);// getメソッドで追加する。(取得する）
                System.out.println(code[1]);// 支店名出力

                //System.out.println(branchlist.entrySet());
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        // *******商品定義ファイル**********//
        // コマンドライン引数でパスを受ける
        // File filepath = new File(args[0]);
        // 指定したディレクトリ・ファイル一覧をFile型の配列で返す。
        // File[] filelist = filepath.listFiles();

        File commodity = new File(args[0], "commodity.lst");
        if (!commodity.isFile()) {
            System.out.println("商品定義ファイルが存在しません");
        }


        // キーと値の生成
        HashMap<String, String> commoditylist = new HashMap<String, String>();
        try {
            // ファイル読み込み
            BufferedReader br = new BufferedReader(new FileReader(new File(
                    args[0], "commodity.lst")));
            String line;

            // 一行ずつファイルを読み込む。
            while ((line = br.readLine()) != null) {

                String[] cord = line.split(","); // splitメソッドでlineをカンマで分割
                commoditylist.put(cord[0], cord[1]);// キーと値を追加
                // 商品コード,商品名を検索
                // 商品コードアルファベットと数字。8桁固定ならtrue
                if (!cord[0].matches("[a-zA-Z0-9]{8}")) {
                    System.out.println("商品ファイルのフォーマットが不正です");

                } else {

                    commoditylist.get(cord[0]);// getメソッドで商品コード追加する。(取得する）
                    System.out.print(cord[0]);// 商品コード出力
                }

                commoditylist.get(cord[1]);// getメソッドで商品コード追加する。(取得する）
                System.out.println(cord[1]);// 商品名出力
            }
            //System.out.println(commoditylist.entrySet());
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }


// ***********集計（売上ファイル関連）**********
        //フォルダ・ファイルをリスト化
        File fileArgs = new File(args[0]);
        // 売上支店コードを配列(リスト）に入れる(作る）
        ArrayList<String> EarningsFileNameList = new ArrayList<String>();
        // 売上商品コードを配列(リスト）に入れる(作る）
        ArrayList<Integer> EarningsFileProductcodeList = new ArrayList<Integer>();
        // キーと値を生成(売上支店、売上商品）
        HashMap<String, String> Earningsbranch = new HashMap<String, String>();
        HashMap<Long, Long> Earningscommodity = new HashMap<Long, Long>();
        // 抽出したファイル⇒ファイル名と拡張子をわけ、ファイル名と拡張子をそれぞれ保持
        for (int i = 0; Earningsbranch.size() > i; i++) {
            // 後列から部分文字列を検索する
            String listFileName = Earningsbranch.get(".rcd");
            int index = listFileName.lastIndexOf(".");
            String fileName = listFileName.substring(0, index);
            String extension = listFileName.substring(index + 1);

            // 拡張子が[rcd]且つ(AND演算)ファイル名が8桁の数字かどうか
            if (args[0].matches("\\d{8}") && "rcd".equals(extension)
                    && listFileName.length() == 12) {
                // 売上ファイル用リストに入れる（作る）
                EarningsFileNameList.add(listFileName);
                Integer fileNameNumber = Integer.parseInt(listFileName);
                // 連番チェック用リストに入れる（作る）
                EarningsFileProductcodeList.add(fileNameNumber);
            }
        }
        // 売上ファイル用リストの要素数が2つ以上あるか確認
        if (EarningsFileNameList.size() >= 2) {
            // 売上ファイル用リストの要素数分処理を繰り返す
            for (int j = 1; EarningsFileNameList.size() > j; j++) {
                if (EarningsFileProductcodeList.get(j)
                        - EarningsFileProductcodeList.get(j - 1) != 1) {
                    System.out.println("売上ファイルが連番になっていません");
                    return;
                }
            }

            // 売上ファイル用の要素数分処理を繰り返す
            ArrayList<String> EarningsList = new ArrayList<String>();
            for (int i = 0; EarningsFileNameList.size() > i; i++) {
                 EarningsList = new ArrayList<String>();
// コマンドライン引数に集計リストのファイル名を文字列連結し、ファイルを開く

                String Aggregate = null;
                BufferedReader br = null;// ここで宣言、変数のスコープ（範囲）
                try {

                    br = new BufferedReader(new FileReader(new File(args[0],
                            EarningsFileNameList.get(i))));
                    // 売上ファイルを一行ずつ読み込む
                    while ((Aggregate = br.readLine()) != null) {
                        // 読込んだデータを集計リストに追加する
                        EarningsList.add(Aggregate);
                        // 集計リストの要素数が3かどうか
                        if (EarningsList.size() != 3) {
                            System.out.println("<"
                                    + EarningsFileNameList.get(i)
                                    + ">のフォーマットが不正です");
                            return;
                        }
                        // 支店定義用HashMapキーの有無。
                        if (!branchlist.containsKey(EarningsList.get(0))) {
                            System.out.println("<"
                                    + EarningsFileNameList.get(i)
                                    + ">の支店コードが不正です");
                            return;
                        }
                        // 商品定義用HashMapのキーの有無
                        if (!commoditylist.containsKey(EarningsList.get(1))) {
                            System.out.println("<"
                                    + EarningsFileNameList.get(i)
                                    + ">の商品コードが不正です");
                            return;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("ファイル入出力の際にエラーが発生しました。");
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.out.println("ファイル入出力の際にエラーが発生しました。");

                    }
                }

        //System.out.println(branchlst);
        //System.out.println(commoditylst);
        //Arraylist<String>
        //getメソッドで支店コードを追加する。(取得する)
       String branchCode = EarningsFileNameList.get(0);
     //getメソッドで商品コードを追加する。(取得する)
       String CommodityCord = EarningsFileNameList.get(0);


       long beforeSale = 0L;
       //集計ファイルから支店コードと商品コードを取り出す
        String BranchCode = EarningsList.get(0);
        String CommodityCode = EarningsList.get(1);
        // 売上額を数値型に変換し保持する
        beforeSale = Long.parseLong(EarningsList.get(2));
        // 支店別合計売上額HashMapの該当する支店に売上額を加算する
        EarningsList.add(BranchCode);
        //支店別合計売上額をString型からint型に変換。
        Integer intbranch = Integer.parseInt(BranchCode);
        //long Branchresult = EarningsList.get(Integer a) + beforeSale;
        int intcode =   Integer.parseInt(EarningsList.get(intbranch));
        //支店別合計合計売上を売上額
        long Branchresult = intcode + beforeSale;
        //long型をString型へ変換
        String Branchresu = "Branchresult";


        Earningsbranch.put(BranchCode, Branchresu);

        // 商品別合計売上額HashMapの該当する商品に売上額を加算する
        long afterCommoditySale = Earningscommodity.get(CommodityCode) + beforeSale;
        //Earningscommodity.put(CommodityCode, Earningscommodity);
        // 加算した合計金額が10桁超えていないかどうか
        if (Branchresult >= 10000000000L) {
            System.out.println("合計金額が10桁を超えました");
            return;
        }
        if (Branchresult >= 10000000000L) {
            System.out.println("合計金額が10桁を超えました");
            return;
        }
    }

// **************集計結果出力***********
            // 集計結果(支店)ファイル確認,(商品)ファイル確認
            File BranchresultOutput = new File(args[0],"branch.out");
            File CommodityresultOutput = new File(args[0],"branch.out");

            if (!BranchresultOutput.isFile()) {
                System.out.println("支店別集計結果ファイルが書き込み時にエラーが発生しました");
            }
            if (!CommodityresultOutput.isFile()) {
                System.out.println("商品別集計結果ファイルが書き込み時にエラーが発生しました");
            }


        }
        BufferedReader br = null;// ここで宣言、変数のスコープ（範囲）
            try {
                String purpose;
                br = new BufferedReader(new FileReader(new File(filepath, purpose)));
                String line;
                // ファイルを一行ずつ読込む
                while ((line = br.readLine()) != null) {
                    // 読み込んだデータをカンマで分割
                    String[] splitreadLine = line.split(",", -1);
                    if (splitreadLine.length != 2) {
                        System.out.println("定義ファイルのフォーマットが不正です");

                    }
                    // 分割したデータを、それぞれ「コード」「名前」のデータを保持する
                    String thiscode = splitreadLine[0];
                    String thisname = splitreadLine[1];
                    //キーと値を生成
                    HashMap<String, String> thislist ;
                    HashMap<String, Long> resultmap;
                    // コードをキーとした名前を定義用HashMapに追加
                    thislist .put(thiscode, thisname);

                    // コードをキーとした売上金額(初期値として0)を合計売上額HashMapに追加
                    resultmap.put(thiscode, 0L);
                }
            } catch (NullPointerException e) {
                System.out.println("ファイル入出力の際にエラーが発生しました。");

            } catch (Exception e) {
                System.out.println("ファイル入出力の際にエラーが発生しました。");
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("ファイル入出力の際にエラーが発生しました。");
                }
            }

        }

        // 集計結果出力メソッド
        public static boolean Aggregate(HashMap<String, Long> salesMap,
                String filepath, String newFileName,HashMap<String, String> useListMap) {
            // 支店(商品)別合計金額用HashMapの合計金額を降順にしたコレクション(合計額降順)を生成
        	Set<Entry<String, Long>> descendingOrder = (salesMap.entrySet());
            Collections.sort(descendingOrder,new Comparator<Entry<String, Long>>() {
                        @Override
                        public int compare(Entry<String, Long> entry1,Entry<String, Long> entry2) {
                            return (entry2.getValue()).compareTo(entry1.getValue());
                        }
                    });
            // ファイルオブジェクトを生成し、ファイルを生成する
            BufferedWriter bw = null;
            OutputStreamWriter os = null;
            try {
                File fileOut = new File(filepath, newFileName);
                fileOut.createNewFile();
                //文字、配列を出力また、OutStreamWriterで文字からバイトへ橋渡し
           bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut, false), "UTF-8"));
                //bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut, false), "UTF-8"));
                // 合計額降順コレクションを抽出していく
                for (Entry <String, Long> descendingorderMap : descendingOrder) {
                    // 合計額降順コレクションのキーの支店コードを用いて支店名用HashMapから支店名を取り出す
                    // [コード][名前][合計売上額]のカンマで区切ったものを文字列連結し、末尾に改行したものを書き込む
                    bw.write(descendingorderMap.getKey() + ",");
                    bw.write(useListMap.get(descendingorderMap.getKey()) + ",");
                    bw.write(descendingorderMap.getValue().toString());
                    bw.newLine();
                }
            } catch (IOException e) {
                return false;
            } finally {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("ファイル入出力の際にエラーが発生しました。");
                    return false;
                }
            }
            return true;
        }
}
}
}
