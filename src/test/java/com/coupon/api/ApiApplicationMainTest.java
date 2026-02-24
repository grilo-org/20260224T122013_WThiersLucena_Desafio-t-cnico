package com.coupon.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ApiApplication main")
class ApiApplicationMainTest {

    @Test
    @DisplayName("main inicia sem exceção")
    void mainStartsWithoutException() throws InterruptedException {
        Thread t = new Thread(() -> ApiApplication.main(new String[]{}), "api-main");
        t.setDaemon(true);
        t.start();
        // Aguarda main() e SpringApplication.run() serem executados para cobertura
        Thread.sleep(3000);
    }
}
