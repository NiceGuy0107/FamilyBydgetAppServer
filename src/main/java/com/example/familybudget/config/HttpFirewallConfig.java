package com.example.familybudget.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class HttpFirewallConfig {

    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true); // Разрешить символы URL-кодирования
        firewall.setAllowSemicolon(true); // Разрешить точку с запятой
        firewall.setAllowBackSlash(true); // Разрешить обратную косую черту
        firewall.setAllowUrlEncodedSlash(true); // Разрешить закодированные слеши
        firewall.setAllowUrlEncodedPeriod(true); // Разрешить закодированные точки
        firewall.setAllowUrlEncodedCarriageReturn(true); // Разрешить закодированные символы возврата каретки
        firewall.setAllowUrlEncodedLineFeed(true); // Разрешить закодированные символы новой строки
        return firewall;
    }
}