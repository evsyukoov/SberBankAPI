package com.evsyukoov.project;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

//класс для запуска мавеном
//поскольку все тесты запускаются сразу при сборке, БД надо поднять 1 раз
public class TestExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
            context.getRoot().getStore(GLOBAL).put("AnyUniqueName", this);
            H2Server.run();
        }
    }

    @Override
    public void close() throws Exception {
        H2Server.stop();
    }
}
