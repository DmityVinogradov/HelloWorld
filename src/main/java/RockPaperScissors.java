import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Start Game: Press Enter");
        System.out.println("Game over: press Q");
        RPS[] rps = RPS.values();
        int Vasya, Petya;
        boolean flag = true;
        while (flag) {
            Vasya = random.nextInt(rps.length);
            Petya = random.nextInt(rps.length);
            if (!scanner.nextLine().equals("q")) {
                System.out.println("Вася показывает: " + rps[Vasya]);
                System.out.println("Петя показывает: " + rps[Petya]);
                if ((Vasya == 0 && Petya == 1) || (Vasya == 1 && Petya == 2)
                        || (Vasya == 2 && Petya == 0) ){
                    System.out.println("Вася выйграл!");
                } else if (Vasya == Petya){
                    System.out.println("Ничья!");
                }else {
                    System.out.println("Петя выйграл!");
                }
            } else {
                flag = false;
            }
        }
    }
    public static enum RPS {Камень,
        Ножницы,
        Бумага}
}
