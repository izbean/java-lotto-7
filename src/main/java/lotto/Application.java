package lotto;

import camp.nextstep.edu.missionutils.Console;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        LottoMachine lottoMachine = new LottoMachine();
        int totalProgressLevel = lottoMachine.getTotalProgressLevel();
        int level = 0;
        while (level < totalProgressLevel) {
            try {
                lottoMachine.progress(level);
                level++;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        Console.close();
    }
}
