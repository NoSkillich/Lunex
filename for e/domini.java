import java.util.Scanner;
public class domini {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int totalArea = n * n;
        int cutArea = m * m;
        int remainingArea = totalArea - cutArea;
        if (remainingArea % 2 != 0) {
            System.out.println("NO");
        } else {
            int numDominos = remainingArea / 2;
            System.out.println("YES");
            System.out.println(numDominos);
        }

        scanner.close();
    }
}
