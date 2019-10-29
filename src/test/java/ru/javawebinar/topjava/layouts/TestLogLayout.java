package ru.javawebinar.topjava.layouts;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class TestLogLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {
        return event.getFormattedMessage() + "\n";
    }
}
