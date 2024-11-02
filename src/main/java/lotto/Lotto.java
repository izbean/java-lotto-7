package lotto;

import java.util.List;

public class Lotto {

    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }

        for (int number : numbers) {
            validateNumberRange(number);
        }
    }

    public static void validateNumberRange(int number) {
        if (number > 45 || number < 1) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
    }

    public static void validateBonusNumber(List<Integer> drawLottoNumbers, String bonusNumber) {
        String[] bonusNumbers = bonusNumber.split(",");
        if (bonusNumbers.length > 1) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1개만 입력가능합니다.");
        }

        for (String number : bonusNumbers) {
            try {
                validateNumberRange(Integer.parseInt(number));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("[ERROR] 보너스 번호는 숫자여야 합니다.");
            }
        }

        if (drawLottoNumbers.contains(Integer.parseInt(bonusNumber))) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호에 포함 될 수 없습니다.");
        }
    }

    public List<Integer> getSortedNumbers() {
        return numbers.stream()
                .sorted()
                .toList();
    }

    public Rank getDrawScore(Lotto drawLotto, int bonusNumber) {
        List<Integer> drawLottoNumber = drawLotto.getSortedNumbers();
        List<Integer> lottoNumber = this.numbers;

        int count = 0;
        for (Integer drawNumber : drawLottoNumber) {
            if (lottoNumber.contains(drawNumber)) {
                count++;
            }
        }

        double score = count;
        if (lottoNumber.contains(bonusNumber)) {
            score += 0.5;
        }

        return Rank.findByScore(score);
    }

    @Override
    public String toString() {
        return getSortedNumbers().toString();
    }
}
