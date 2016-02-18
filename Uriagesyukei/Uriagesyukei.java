package Uriagesyukei;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
				branchlist.put(code[0], code[1]);// キーと値を追加する(mapに作る）

				if (!code[0].matches("^\\d{3}")) {
					System.out.println("支店ファイルのフォーマットが不正です");
					// 支店コード3桁ならtrue
				}
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
				commoditylist.put(cord[0], cord[1]);// リストに商品コードと商品名を追加
				// 商品コードアルファベットと数字。8桁固定ならtrue
				if (!cord[0].matches("[a-zA-Z0-9]{8}")) {
					System.out.println("商品ファイルのフォーマットが不正です");

				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		// System.out.println(commoditylist);
		// System.out.println(branchlist);

		// ***********集計（売上ファイル関連）**********
		//連番リストを作成
		ArrayList<Integer> serial= new ArrayList<Integer>();
		// 売上支店リストを作る
		ArrayList<String> EarningsFileNameList = new ArrayList<String>();
		// 売上商品リストを作る
		ArrayList<Integer> EarningsFileProductcodeList = new ArrayList<Integer>();
		// キーと値を生成(売上支店、売上商品）
		HashMap<String, String> Earningsbranch = new HashMap<String, String>();
		HashMap<Long, Long> Earningscommodity = new HashMap<Long, Long>();
		// 抽出したファイル⇒ファイル名と拡張子をわけ、ファイル名と拡張子をそれぞれ保持
		// System.out.println(filelist[1].getName());
		//ファイルの長さをlengthで検索

		for (int i = 0; i < filelist.length; i++) {
			// listFileNameにファイル名を格納する。
			String listFileName = filelist[i].getName();
			// System.out.println(listFileName);
			//ファイル名を.(ドット)の手前までの文字数を検索する
			int index = listFileName.lastIndexOf(".");
			// System.out.println(index);
			//listFileNameからindex分（文字列の長さ）を取り出しfileNameに代入。
			String fileName = listFileName.substring(0, index);//0始まり8まで(拡張子手前まで）
			// System.out.println(fileName);
			//listFileNameからドット以降の文字列をextensionに代入
			String extension = listFileName.substring(index + 1);//8+1(.ドット以降）
			// 拡張子が[rcd]且つ(AND演算)ファイル名が8桁の数字かどうか
			if (index == 8 && "rcd".equals(extension)) {
		    // 連番チェック用リストに入れる
				serial.add(Integer.parseInt(fileName));
			}
		}
			//連番チェックで昇順にする
	}
}
