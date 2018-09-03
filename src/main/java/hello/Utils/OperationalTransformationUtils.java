package hello.Utils;

public class OperationalTransformationUtils {

    /**
     * insert insert
     * @param local local op, has already been applied
     * @param recv remote op
     * @return transformation of remote op
     */
    public static Message insertInsert(Message local, Message recv) {
        int localInsertPosition = local.getInsertPosition();
        int recvInsertPosition = recv.getInsertPosition();

        boolean sameInsertionPositionSmallerId = localInsertPosition == recvInsertPosition && local.getUrlId().compareTo(recv.getUrlId()) < 0;

        if (localInsertPosition < recvInsertPosition || sameInsertionPositionSmallerId) {
            recv.setInsertPosition(recvInsertPosition + local.getText().length());
        }

        return recv;
    }
}
