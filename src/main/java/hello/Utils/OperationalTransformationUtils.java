package hello.Utils;

public class OperationalTransformationUtils {

    /**
     * insert insert
     * @param local local op, has already been applied
     * @param recv remote op
     * @return transformation of remote op
     */
    public static Message insertInsert(Message local, Message recv) {
        System.out.println(local.getText());
        System.out.println(recv.getText());

        int localInsertPosition = local.getInsertPosition();
        int recvInsertPosition = recv.getInsertPosition();

        if (local.getText().equals(recv.getText())) {
            return null;
        }

        boolean sameInsertionPositionSmallerId = localInsertPosition == recvInsertPosition && local.getUrlId().compareTo(recv.getUrlId()) < 0;

        if (localInsertPosition < recvInsertPosition || sameInsertionPositionSmallerId) {
            recv.setInsertPosition(recvInsertPosition + local.getText().length());
        }

        return recv;
    }

    /**
     * insert b into a at position x
     * @param a original string
     * @param b insert string
     * @param x insert point
     * @return inserted results
     */
    public static String insertString(String a, String b, int x) {
        System.out.println("merge: " + a + "|" + b + "|" + x);
        if (x >= a.length()) {
            return a + b;
        }
        return a.substring(0, x) + b + a.substring(x);
    }
}
