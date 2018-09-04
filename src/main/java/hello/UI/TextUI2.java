package hello.UI;

import com.vaadin.annotations.Push;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hello.Socket.Client;
import hello.Utils.Message;
import hello.Utils.OperationalTransformationUtils;
import hello.Utils.SocketThreadFactory;
import hello.Utils.ThreadSafeQueue;
import org.apache.logging.log4j.util.Strings;
import java.util.concurrent.*;

import static hello.UI.TextUI2.urlId;

@Push
@SpringUI(path = urlId)
public class TextUI2 extends UI {

    static final String urlId = "/text2";

    private VerticalLayout layout;

    private TextArea textArea = new TextArea();

    private final Client client = new Client(urlId, textArea);

    private final static long SLEEP_DURATION = 2L;

    private final SocketThreadFactory threadFactory = new SocketThreadFactory();

    /**
     * local operations
     */
    private final ThreadSafeQueue<Message> localOperationQueue = new ThreadSafeQueue<>();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Thread clientThread = threadFactory.newThread(client);
        clientThread.start();

        startTextSender();

        startValueChangeMonitor();

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
                client.sendMessage(new Message(urlId, -1, textArea.getValue()));
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

            /*
             * update text captured from editor */
            client.setPrevText(client.getCurrText());
            client.setCurrText(textArea.getValue());

            /*
             * todo: only insertion is considered currently */
            StringBuilder insertedText = new StringBuilder();
            int insertPosition = Message.getDiff(client.getPrevText(), client.getCurrText(), insertedText);
            String text = insertedText.toString();

            if (text.length() > 0) {
                try {
                    Message localOp = new Message(urlId, insertPosition, text);
                    client.sendMessage(localOp);
                    localOperationQueue.add(localOp);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scheduledExecutorService.scheduleWithFixedDelay(task, SLEEP_DURATION, SLEEP_DURATION, TimeUnit.SECONDS);
    }

    /**
     * monitor on ClientHandler, if received ops from server, send it here to push up to browser
     */
    private void startValueChangeMonitor() {
        ThreadSafeQueue<Message> receivedMessageQueue = client.getClientHandler().receivedMessageQueue;

        Runnable task = () -> {
            while (true) {
                Message recv = receivedMessageQueue.poll();
                System.out.println("here: " + (recv == null ? "null" : recv.getText()));

                /*
                 * apply operational transformation */
                if (recv != null && !localOperationQueue.isEmpty()) {
                    recv = OperationalTransformationUtils.insertInsert(localOperationQueue.poll(), recv);
                }

                if (recv != null) {
                    String consistentText = OperationalTransformationUtils.insertString(client.getCurrText(),
                            recv.getText(), recv.getInsertPosition());

                    access(() -> textArea.setValue(consistentText));
                    client.setPrevText(client.getCurrText());
                    client.setCurrText(consistentText);
                }
            }
        };

        Thread thread = new SocketThreadFactory().newThread(task);
        thread.start();
    }
}
