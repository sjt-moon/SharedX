package hello.UI;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hello.Socket.Client;
import hello.Utils.SocketThreadFactory;
import org.apache.logging.log4j.util.Strings;

import java.util.concurrent.*;

@SpringUI(path = "/text")
public class TextUI extends UI {

    private final Client client = new Client();

    private VerticalLayout layout;

    private TextArea textArea = new TextArea();

    private final static long SLEEP_DURATION = 2L;

    private final SocketThreadFactory threadFactory = new SocketThreadFactory();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Thread clientThread = threadFactory.newThread(client);
        clientThread.start();

        startTextSender();

        setupLayout();
        addHeader();
        addTextField();
        addSendButton();
    }

    private void setupLayout() {
        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private void addHeader() {
        Label header = new Label("sharedX");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);
    }

    private void addTextField() {
        textArea.setWordWrap(true);
        layout.addComponent(textArea);
    }

    private void addSendButton() {
        Button sendButton = new Button();
        sendButton.setIcon(FontAwesome.SEND);
        sendButton.setVisible(true);
        sendButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        layout.addComponent(sendButton);

        sendButton.addClickListener(event -> {
            if (Strings.isNotEmpty(textArea.getValue())) {
                client.sendMessage(textArea.getValue());
                Notification.show("Sent");
            }
        });
    }

    /**
     * heartbeat, periodically get text field data & send to the server
     */
    private void startTextSender() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            try {
                client.sendMessage(textArea.getValue());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        scheduledExecutorService.scheduleWithFixedDelay(task, SLEEP_DURATION, SLEEP_DURATION, TimeUnit.SECONDS);
    }
}
