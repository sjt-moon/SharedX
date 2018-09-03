package hello.Utils;

import com.google.gson.Gson;

/**
 * As we apply DelimiterBasedFrameDecoder, when we
 * - send: append '\n' at end
 * - receive: automatically '\n' is removed
 */
public class Message {
    private final String urlId;
    private final String text;

    private static final Gson gson = new Gson();

    public Message(String urlId, String text) {
        this.urlId = urlId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getUrlId() {
        return urlId;
    }

    @Override
    public String toString() {
        return gson.toJson(this) + MetaUtils.LINE_SEPARATOR;
    }

    /**
     * try to extract Message from a json string
     * @param json json string
     */
    public static Message getMessage(String json) {

        try {
            return gson.fromJson(json, Message.class);
        }
        catch (Exception e) {
            return null;
        }
    }
}
