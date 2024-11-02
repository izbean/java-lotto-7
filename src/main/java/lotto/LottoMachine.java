package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.text.NumberFormat;
import java.util.*;

public class LottoMachine {

    private final List<Lotto> lottoList = new ArrayList<>();

    private int balance;

    private Lotto drawLotto;

    private Integer bonusNumber;

    private final int totalProgressLevel = 6;

    private final Map<Rank, Integer> drawCountMap = new HashMap<>();

    public LottoMachine() {
        for (Rank rank : Rank.values()) {
            drawCountMap.put(rank, 0);
        }
    }

    public int getTotalProgressLevel() {
        return totalProgressLevel;
    }

    public void progress(int progressLevel) {
        if (progressLevel == 0) {
            inputAmount();
            return;
        }
        if (progressLevel == 1) {
            buy();
            return;
        }
        if (progressLevel == 2) {
            inputDrawNumber();
            return;
        }
        if (progressLevel == 3) {
            inputBonusNumber();
            return;
        }
        if (progressLevel == 4) {
            draw();
            return;
        }
        if (progressLevel == 5) {
            showDrawStatistics();
        }
    }

    private void inputAmount() {
        System.out.println("구입금액을 입력해 주세요.");
        String amountString = Console.readLine();
        if (amountString == null) {
            throw new IllegalArgumentException("[ERROR] 구입금액은 비어있을 수 없습니다.");
        }

        try {
            Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 구입금액은 숫자여야합니다.");
        }

        if (Integer.parseInt(amountString) % 1_000 != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위로 입력해주세요.");
        }

        this.balance = Integer.parseInt(amountString);
    }

    private void buy() {
        long buyCount = this.balance / 1000;

        System.out.printf("%s개를 구매했습니다.%n", buyCount);
        for (int i = 0; i < buyCount; i++) {
            List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            lottoList.add(new Lotto(numbers));
            System.out.println(numbers);
        }
    }

    private void inputDrawNumber() {
        System.out.println("당첨 번호를 입력해주세요.");
        String drawNumberString = Console.readLine();
        if (drawNumberString == null) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 비어있을 수 없습니다.");
        }

        String[] drawNumberStringArray = drawNumberString.split(",");
        List<Integer> numbers = new ArrayList<>();
        for (String drawNumber : drawNumberStringArray) {
            try {
                numbers.add(Integer.parseInt(drawNumber));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("[ERROR] 당첨 번호는 숫자여야합니다.");
            }
        }

        this.drawLotto = new Lotto(numbers);
    }

    private void inputBonusNumber() {
        System.out.println("보너스 번호를 입력해주세요.");
        String bonusNumber = Console.readLine();
        if (bonusNumber == null) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 비어있을 수 없습니다.");
        }

        Lotto.validateBonusNumber(drawLotto.getSortedNumbers(), bonusNumber);
        this.bonusNumber = Integer.parseInt(bonusNumber);
    }

    private void draw() {
        for (Lotto lotto : lottoList) {
            Rank drawScore = lotto.getDrawScore(drawLotto, bonusNumber);
            if (drawScore == null) {
                continue;
            }

            drawCountMap.put(drawScore, drawCountMap.get(drawScore) + 1);
        }
    }

    private void showDrawStatistics() {
        List<Rank> drawOrder = List.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO, Rank.ONE);

        long totalAmount = 0;
        for (Rank rank : drawOrder) {
            int count = (int) rank.count;
            String amount = NumberFormat.getInstance(Locale.KOREA).format(rank.amount);
            totalAmount += rank.amount * drawCountMap.get(rank);
            if (rank.drawBonus) {
                System.out.printf("%s개 일치, 보너스 볼 일치 (%s원) - %s개%n", count, amount, drawCountMap.get(rank));
                continue;
            }

            System.out.printf("%s개 일치 (%s원) - %s개%n", count, amount, drawCountMap.get(rank));
        }

        System.out.printf("총 수익률은 %.1f%%입니다.%n", (double) ((totalAmount / balance) * 100));
    }

}
