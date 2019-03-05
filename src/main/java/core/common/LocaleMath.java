package core.common;

/**
 * Local Math
 * <br>
 * Подстроенные под специфические нужды класс, для работы с методами {@link java.lang.Math}
 * @author Arthur Kupriyanov
 */
public class LocaleMath {

    /**
     * Возвращает случайное число в диапозоне от <code>minValue</code> до <code>maxValue</code>
     * @param minValue нижняя граница (включительно)
     * @param maxValue верхняя граница (включительно)
     * @return рандомное число в указанном диапозоне
     */
    public static int randInt(int minValue, int maxValue){
        return (int)(java.lang.Math.random()*maxValue) + minValue;
    }
}
