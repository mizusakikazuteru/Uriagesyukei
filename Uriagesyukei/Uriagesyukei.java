package Uriagesyukei;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			BufferedReader br = new BufferedReader(new FileReader(new File(args[0], "branch.lst")));
			String line;

			// 一行ずつファイルを読み込む。
			while ((line = br.readLine()) != null) {

				String[] code = line.split(","); // splitメソッドでlineをカンマで分割

				if (!code[0].matches("^\\d{3}")) {
					System.out.println("支店ファイルのフォーマットが不正です");
					// 支店コード3桁ならtrue

				}
				if (!code[1].matches(".+支店")) {
					System.out.println("支店ファイルのフォーマットが不正です");
					// 支店名の末尾が[支店」ならtrue

				}
				branchlist.put(code[0], code[1]);// キーと値を追加する(mapに作る）
				// System.out.println(code[0]);
				// System.out.println(code[1]);
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
			BufferedReader br = new BufferedReader(new FileReader(new File(args[0], "commodity.lst")));
			String line;

			// 一行ずつファイルを読み込む。
			while ((line = br.readLine()) != null) {

				String[] cord = line.split(","); // splitメソッドでlineをカンマで分割

				// 商品コードアルファベットと数字。8桁固定ならtrue
				if (!cord[0].matches("[a-zA-Z0-9]{8}")) {
					System.out.println("商品ファイルのフォーマットが不正です");

				}
				// 商品名がアルファベットならtrue
				if (!cord[1].matches("[a-zA-Zａ-ｚＡ-Ｚ]+")) {
					System.out.println("商品ファイル商品名のフォーマットが不正です");

				}
				commoditylist.put(cord[0], cord[1]);// リストに商品コードと商品名を追加
				// System.out.println(cord[0]);
				// System.out.println(cord[1]);
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
		// 連番リストを作成
		ArrayList<Integer> serial = new ArrayList<Integer>();
		// 売上支店リストを作る
		ArrayList<String> earningsFileNameList = new ArrayList<String>();
		// 売上商品リストを作る
		ArrayList<Integer> earningsFileProductcodeList = new ArrayList<Integer>();
		// キーと値を生成(売上支店、売上商品）
		HashMap<String, Long> earningsbranch = new HashMap<String, Long>();
		HashMap<String, Long> earningscommodity = new HashMap<String, Long>();
		// 抽出したファイル⇒ファイル名と拡張子をわけ、ファイル名と拡張子をそれぞれ保持
		// System.out.println(filelist[1].getName());

		// ファイルの長さをlengthで検索
		for (int i = 0; i < filelist.length; i++) {
			// listFileNameにファイル名を格納する。
			String listFileName = filelist[i].getName();
			// System.out.println(listFileName);
			// ファイル名を.(ドット)の手前までの文字数を検索する
			int index = listFileName.lastIndexOf(".");
			// System.out.println(index);
			// listFileNameからindex分（文字列の長さ）を取り出しfileNameに代入。
			String fileName = listFileName.substring(0, index);// 0始まり8まで(拡張子手前まで）
			// String fileName = "00000001.rcd".substring(8 + 1);
			// System.out.println(fileName);
			// listFileNameからドット以降の文字列をextensionに代入
			String extension = listFileName.substring(index + 1);// 8+1(.ドット以降）
			// 拡張子が[rcd]且つ(AND演算)ファイル名が8桁の数字かどうか
			if (index == 8 && "rcd".equals(extension)) {
				// 連番チェック用リストに入れる
				serial.add(Integer.parseInt(fileName));
				// 売上ファイル用にrcd拡張子ファイルを入れる。
				earningsFileNameList.add(listFileName);
			}
		}
		// 連番チェック
		if (serial.size() >= 2) {
			// 昇順にする
			Collections.sort(serial);
			// 連番かどうか確認
			for (int j = 1; j < serial.size(); j++) {
				if (serial.get(j) - serial.get(j - 1) != 1) {
					System.out.println("売上ファイルが連番になっていません");
					return;
				}
				// System.out.println(serial.get(j) - serial.get(j - 1));

			}
		}

		ArrayList<String> earningsList = new ArrayList<String>();
		// 売上ファイル用の要素数分処理を繰り返す
		for (int i = 0; i < earningsFileNameList.size(); i++) {
			earningsList = new ArrayList<String>();
			// コマンドライン引数に集計リストのファイル名を文字列連結し、ファイルを開く

			BufferedReader br = null;// ここで宣言、変数のスコープ（範囲）
			try {
				br = new BufferedReader(new FileReader(new File(args[0], earningsFileNameList.get(i))));
				// 売上ファイルを一行ずつ読み込む
				String line = null;
				while ((line = br.readLine()) != null) {
					// 読込んだデータを集計リストに追加する
					earningsList.add(line);
				}

				// 集計リストの要素数が3かどうか
				if (earningsList.size() != 3) {
					System.out.println("<" + earningsFileNameList.get(i) + ">のフォーマットが不正です");
					return;
				}
				// 支店定義用HashMapキーの有無。
				if (!branchlist.containsKey(earningsList.get(0))) {
					System.out.println("<" + earningsFileNameList.get(i) + ">の支店コードが不正です");
					return;
				}
				// 商品定義用HashMapのキーの有無
				if (!commoditylist.containsKey(earningsList.get(1))) {
					System.out.println("<" + earningsFileNameList.get(i) + ">の商品コードが不正です");
					return;
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

			//long salebranch = 0L;
			//long salecommodity = 0L;
			String branchCode = earningsList.get(0);
			String commodityCode = earningsList.get(1);
			// 売上額を数値型に変換し保持する
			//salebranch = Long.parseLong(earningsList.get(2));
			Long salebranch = Long.parseLong(earningsList.get(2));
			//salecommodity = Long.parseLong(earningsList.get(2));
			Long salecommodity = Long.parseLong(earningsList.get(2));

			// 支店別合計売上額HashMapの該当する支店に売上額を加算する
			if (earningsbranch.containsKey(branchCode)) {
				salebranch = earningsbranch.get(branchCode) + salebranch;
			}
			earningsbranch.put(branchCode, salebranch);

			// 商品別合計売上額HashMapの該当する商品に売上額を加算する
			if (earningscommodity.containsKey(commodityCode)) {
				salecommodity = earningscommodity.get(commodityCode) + salecommodity;
			}
			earningscommodity.put(commodityCode, salecommodity);

			// 加算した合計金額が10桁超えていないかどうか
			if (salebranch >= 10000000000L) {
				System.out.println("合計金額が10桁を超えました");
				return;
			}
			if (salecommodity >= 10000000000L) {
				System.out.println("合計金額が10桁を超えました");
				return;
			}
		}

		// System.out.println(Earningsbranch);
		// System.out.println(Earningscommodity);

		// ***************集計結果出力*********************
		// 支店集計結果出力メソッドの呼び出し
		if (!branchOutput(earningsbranch, args[0], "branch.out", branchlist)) {
			System.out.println("支店別集計結果ファイルが書き込み時にエラーが発生しました");
			return;
		}
		// System.out.println();

		// 商品集計結果出力メソッドの呼び出し
		if (!commodityOutput(earningscommodity, args[0], "commodity.out", commoditylist)) {
			System.out.println("商品別集計結果ファイルが書き込み時にエラーが発生しました");
			return;
		}
		// System.out.println();
	}

	// 支店集計結果出力メソッド
	public static boolean branchOutput(HashMap<String, Long> branchMap, String filepath, String newFileName,
			HashMap<String, String> branch) {
		// 支店(商品)別合計金額用HashMapの合計金額を降順にしたコレクション(合計額降順)を生成
		List<Map.Entry<String, Long>> descendingOrder = new ArrayList<Map.Entry<String, Long>>(branchMap.entrySet());
		Collections.sort(descendingOrder, new Comparator<Map.Entry<String, Long>>() {

			@Override
			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return (entry2.getValue()).compareTo(entry1.getValue());
			}
		});

		// ファイルオブジェクトを生成し、ファイルを生成する
		BufferedWriter bw = null;
		OutputStreamWriter os = null;
		try {
			File fileOut = new File("filepath\\" + newFileName);
			fileOut.createNewFile();
			// 文字、配列を出力また、OutStreamWriterで文字からバイトへ橋渡し
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut, false), "UTF-8"));
			// 合計額降順コレクションを抽出していく
			for (Entry<String, Long> descendingorderMap : descendingOrder) {
				// 合計額降順コレクションのキーの支店コードを用いて支店名用HashMapから支店名を取り出す
				// [コード][名前][合計売上額]のカンマで区切ったものを文字列連結し、末尾に改行したものを書き込む
				bw.write(descendingorderMap.getKey() + ",");
				bw.write(branch.get(descendingorderMap.getKey()) + ",");
				bw.write(descendingorderMap.getValue().toString());
				bw.newLine();
				// 支店集計出力
				System.out.print(descendingorderMap.getKey() + ",");
				System.out.print(branch.get(descendingorderMap.getKey()) + ",");
				System.out.println(descendingorderMap.getValue().toString());
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

	// 商品集計結果出力メソッド
	public static boolean commodityOutput(HashMap<String, Long> commodityMap, String filepath, String newFileName,
			HashMap<String, String> commodity) {
		// 支店(商品)別合計金額用HashMapの合計金額を降順にしたコレクション(合計額降順)を生成
		List<Map.Entry<String, Long>> descendingOrder = new ArrayList<Map.Entry<String, Long>>(commodityMap.entrySet());
		Collections.sort(descendingOrder, new Comparator<Map.Entry<String, Long>>() {

			@Override
			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return (entry2.getValue()).compareTo(entry1.getValue());
			}
		});

		// ファイルオブジェクトを生成し、ファイルを生成する
		BufferedWriter bw = null;
		OutputStreamWriter os = null;
		try {
			File fileOut = new File("filepath\\" + newFileName);
			fileOut.createNewFile();
			// 文字、配列を出力また、OutStreamWriterで文字からバイトへ橋渡し
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut, false), "UTF-8"));
			// 合計額降順コレクションを抽出していく
			for (Entry<String, Long> descendingorderMap : descendingOrder) {
				// 合計額降順コレクションのキーの支店コードを用いて支店名用HashMapから支店名を取り出す
				// [コード][名前][合計売上額]のカンマで区切ったものを文字列連結し、末尾に改行したものを書き込む
				bw.write(descendingorderMap.getKey() + ",");
				bw.write(commodity.get(descendingorderMap.getKey()) + ",");
				bw.write(descendingorderMap.getValue().toString());
				bw.newLine();
				// 商品集計出力
				System.out.print(descendingorderMap.getKey() + ",");
				System.out.print(commodity.get(descendingorderMap.getKey()) + ",");
				System.out.println(descendingorderMap.getValue().toString());
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