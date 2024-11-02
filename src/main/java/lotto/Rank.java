package lotto;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Rank {

    ONE(6, 6, 2_000_000_000, false),
    TWO(5.5, 5, 30_000_000, true),
    THREE(5, 5, 1_500_000, false),
    FOUR(4, 4, 50_000, false),
    FIVE(3, 3, 5_000, false),
    ;

    final double score;

    final double count;

    final long amount;

    final boolean drawBonus;

    Rank(double score, double count, long amount, boolean drawBonus) {
        this.score = score;
        this.count = count;
        this.amount = amount;
        this.drawBonus = drawBonus;
    }

    public double getCount() {
        return count;
    }

    public long getAmount() {
        return amount;
    }

    public static Rank findByScore(double score) {
        Stream<Rank> rankStream = Arrays.stream(Rank.values());
        return rankStream.filter(rank -> rank.score == score).findFirst()
                .orElse(null);

    }

}
