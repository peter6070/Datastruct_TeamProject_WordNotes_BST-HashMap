import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

class wordMean {
	String mean;
	String level;
}

public class wordFind {

	public static void main(String[] args) {

		wordMean[] wordmean1 = new wordMean[9];
		wordmean1[0] = new wordMean();
		wordmean1[1] = new wordMean();
		wordmean1[2] = new wordMean();
		wordmean1[3] = new wordMean();
		wordmean1[4] = new wordMean();
		wordmean1[5] = new wordMean();
		wordmean1[6] = new wordMean();
		wordmean1[7] = new wordMean();
		wordmean1[8] = new wordMean();

		wordmean1[0].mean = "요구하다";
		wordmean1[0].level = "상";
		wordmean1[1].mean = "기술";
		wordmean1[1].level = "상";
		wordmean1[2].mean = "후회하다";
		wordmean1[2].level = "상";
		wordmean1[3].mean = "제공하다";
		wordmean1[3].level = "상";
		wordmean1[4].mean = "제공하다";
		wordmean1[4].level = "중";
		wordmean1[5].mean = "치료하다";
		wordmean1[5].level = "중";
		wordmean1[6].mean = "패션";
		wordmean1[6].level = "중";
		wordmean1[7].mean = "게임";
		wordmean1[7].level = "하";
		wordmean1[8].mean = "발생하다";
		wordmean1[8].level = "하";

		HashMap<String, wordMean> map = new HashMap<String, wordMean>();

		map.put("require", wordmean1[0]);
		map.put("tech", wordmean1[1]);
		map.put("regret", wordmean1[2]);
		map.put("provide", wordmean1[3]);
		map.put("offer", wordmean1[4]);
		map.put("heal", wordmean1[5]);
		map.put("fashion", wordmean1[6]);
		map.put("game", wordmean1[7]);
		map.put("occur", wordmean1[8]);

		for (;;) {

			int n = 0;
			String word;
			String mean;
			String level;
			Scanner sc = new Scanner(System.in, "euc-kr");
			menuView();// 메뉴 보기

			n = sc.nextInt();
			sc.nextLine();
			if (n == 1) {
				if (map.isEmpty()) {
					System.out.println("단어장이 비어있습니다.");
				} else {
					Iterator<Entry<String, wordMean>> entries = map.entrySet().iterator();

					while (entries.hasNext()) {
						Map.Entry<String, wordMean> entry = entries.next();
						System.out
								.println("[Key]:" + entry.getKey() + "\t[value]:" + entry.getValue().mean + "\t[난이도]:"
										+ entry.getValue().level);
					}
				}

			} else if (n == 2) {
				wordMean temp = new wordMean();

				System.out.println("추가할 단어를 입력하세요");
				word = sc.nextLine();
				if (map.containsKey(word)) {
					System.out.println("단어는 중복될 수 없습니다.");
				} else {
					System.out.println("추가할 뜻을 입력하세요");
					mean = sc.nextLine();
					System.out.println("중요도를 입력하세요");
					level = sc.nextLine();

					temp.mean = mean;
					temp.level = level;

					map.put(word, temp);
				}

			} else if (n == 3) {
				System.out.println("삭제할 단어를 입력하세요");
				word = sc.nextLine();

				if (map.containsKey(word)) {
					map.remove(word);
					System.out.println("[" + word + "]를 삭제했습니다.");
				} else
					System.out.println("[" + word + "]단어가 없습니다.");

			} else if (n == 4) {
				System.out.println("수정할 단어를 입력하세요");
				word = sc.nextLine();

				if (map.containsKey(word)) {
					wordMean temp = new wordMean();
					map.remove(word);
					System.out.println("바꿀 단어를 입력하세요");
					word = sc.nextLine();
					System.out.println("바꿀 뜻을 입력하세요");
					mean = sc.nextLine();
					System.out.println("중요도를 입력하세요");
					level = sc.nextLine();
					temp.mean = mean;
					temp.level = level;

					map.put(word, temp);

					System.out.println("[" + word + "]로 수정했습니다.");
				} else
					System.out.println("[" + word + "]단어가 없습니다.");
			} else if (n == 5) {
				System.out.println("검색할 단어를 입력하세요");
				word = sc.nextLine();

				if (map.containsKey(word)) {
					System.out.println("[" + word + "]의 뜻은 " + map.get(word));
				} else {
					System.out.println("[" + word + "]단어가 없습니다.");
				}

			} else if (n == 6) {
				String YN = "";
				System.out.println("진짜 지우시려면 Y 아니면 N (Y/N)");
				YN = sc.nextLine();

				if (YN.equalsIgnoreCase("Y")) {
					map.clear();
					System.out.println("단어장을 모두 지웠습니다.");
				} else {
					System.out.println("전부지우기를 취소합니다.");
				}

			} else {
				System.out.println("종료합니다.");
				break;
			}
		}
	}

	public static void menuView() {
		System.out.println("메뉴를 선택하세요");
		System.out.println("1.전체 보기");
		System.out.println("2.단어 추가");
		System.out.println("3.단어 삭제");
		System.out.println("4.단어 수정");
		System.out.println("5.단어 검색");
		System.out.println("6.전부지우기");
		System.out.println("0. 끝내기");
	}

}
