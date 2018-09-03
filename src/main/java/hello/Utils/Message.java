package hello.Utils;

import com.google.gson.Gson;

/**
 * As we apply DelimiterBasedFrameDecoder, when we
 * - send: append '\n' at end manually
 * - receive: automatically '\n' is removed
 */
public class Message {
    private final String urlId;
    private int insertPosition;
    private final String text;

    private static final Gson gson = new Gson();

    public Message(String urlId, int insertPosition, String text) {
        this.urlId = urlId;
        this.insertPosition = insertPosition;
        this.text = text;
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

    /**
     * INSERT(xStr, pos) into prev & get curr
     * find where (pos) & what (xStr) do we insert 'prev' to get 'curr'
     * @param prev previous text
     * @param curr current text
     * @param xStr inserted text
     * @return pos
     */
    public static int getDiff(String prev, String curr, StringBuilder xStr) {
        if (prev.length() >= curr.length()) {
            return -1;
        }
        int i = 0, j = prev.length() - 1, k = 0;
        while (i <= j && prev.charAt(i) == curr.charAt(k)) {
            i++;
            k++;
        }
        int endPoint = curr.length() - prev.length() + k;
        xStr.setLength(0);
        if (endPoint < k) {
            return k;
        }
        xStr.append(curr.substring(k, endPoint));
        return k;
    }

    public String getText() {
        return text;
    }

    public String getUrlId() {
        return urlId;
    }

    public int getInsertPosition() {
        return insertPosition;
    }

    public void setInsertPosition(int insertPosition) {
        this.insertPosition = insertPosition;
    }
}
