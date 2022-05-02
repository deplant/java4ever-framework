package tech.deplant.java4ever.framework;

import lombok.Getter;

public enum Network {

    MAIN_NET(new String[]{
            "https://eri01.main.everos.dev/",
            "https://gra01.main.everos.dev/",
            "https://gra02.main.everos.dev/",
            "https://lim01.main.everos.dev/",
            "https://rbx01.main.everos.dev/"
    }),

    DEV_NET(new String[]{
            "https://eri01.net.everos.dev/",
            "https://rbx01.net.everos.dev/",
            "https://gra01.net.everos.dev/"
    }),
    LOCALHOST(new String[]{
            "http://0.0.0.0/",
            "http://127.0.0.1/",
            "http://localhost/"
    });

    @Getter
    String[] endpoints;

    Network(String[] endpoints) {
        this.endpoints = endpoints;
    }
}
