import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Zadacha_2 {

    // Константы для удобства изменения условий задачи
    private static final int MAX_VALUE = 10000;
    private static final int TARGET_DIVISOR = 14;
    private static final String INPUT_FILE_NAME = "C:\\Users\\Acer\\IdeaProjects\\first\\src\\PoslPoisk.txt";

    public static void main(String[] args) {
        // Массив частот: frequencies[i] — сколько раз число i встретилось в файле
        int[] frequencies = new int[MAX_VALUE + 1];
        int totalRead = 0;

        System.out.println("Начало обработки файла: " + INPUT_FILE_NAME);

        // 1. Однопроходное чтение файла и подсчёт частот
        try (Scanner scanner = new Scanner(new File(INPUT_FILE_NAME))) {
            while (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                // Защита от чисел вне допустимого диапазона
                if (number >= 0 && number <= MAX_VALUE) {
                    frequencies[number]++;
                    totalRead++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл '" + INPUT_FILE_NAME + "' не найден.");
            return;
        }

        System.out.println("Успешно прочитано элементов: " + totalRead);

        int result = -1;

        // 2. Идём от 10000 вниз — первый подходящий кандидат и будет максимальным
        for (int candidate = MAX_VALUE; candidate >= 0; candidate--) {

            // Кандидат должен присутствовать в файле и быть кратен 14
            if (frequencies[candidate] > 0 && candidate % TARGET_DIVISOR == 0) {

                // Временно "изымаем" кандидата, чтобы он не мог быть множителем самого себя
                frequencies[candidate]--;

                if (canBeFormed(candidate, frequencies)) {
                    result = candidate;
                    break;
                }

                // Возвращаем кандидата обратно, если он не подошёл
                frequencies[candidate]++;
            }
        }

        // 3. Вывод результата
        if (result != -1) {
            System.out.println("Максимальный подходящий элемент R: " + result);
        } else {
            System.out.println("Подходящий элемент не найден. Результат: " + result);
        }
    }

    /**
     * Проверяет, можно ли представить target как произведение двух ДРУГИХ
     * чисел, присутствующих в массиве (с учётом их количества).
     */
    private static boolean canBeFormed(int target, int[] frequencies) {

        // Особый случай для 0: нужно минимум два нуля в исходных данных
        if (target == 0) {
            return frequencies[0] >= 1;
        }

        // Перебираем делители до квадратного корня из target
        for (int divisor = 1; divisor * divisor <= target; divisor++) {
            if (target % divisor == 0) {
                int factor1 = divisor;
                int factor2 = target / divisor;

                if (factor1 > MAX_VALUE || factor2 > MAX_VALUE) continue;

                // Множители равны — нужно минимум 2 вхождения
                if (factor1 == factor2) {
                    if (frequencies[factor1] >= 2) return true;
                }
                // Множители разные — достаточно по одному вхождению каждого
                else {
                    if (frequencies[factor1] >= 1 && frequencies[factor2] >= 1) return true;
                }
            }
        }

        return false;
    }
}