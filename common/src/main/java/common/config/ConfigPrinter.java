package common.config;

public class ConfigPrinter {

    /**
     * Выводит все параметры конфигурации в консоль, КРОМЕ логина и пароля.
     */
    public static void printConfig() {
        System.out.println("========== КОНФИГУРАЦИЯ ТЕСТОВ ==========");
        System.out.println("URL стенда:        " + ConfigProvider.getBaseUrl());
        System.out.println("URL API:           " + ConfigProvider.getApiUrl());
        System.out.println("Таймаут (мс):      " + ConfigProvider.getTimeout());
        System.out.println("Режим логирования: " + ConfigProvider.getLogMode());
        System.out.println("Имя товара:        " + ConfigProvider.getProductName());
        System.out.println("Цена товара:       " + ConfigProvider.getProductPrice());
        System.out.println("Цена (дорогой):    " + ConfigProvider.getProductBigPrice());
        System.out.println("Credentials:       [СКРЫТО]");
        System.out.println("=========================================");
    }
}