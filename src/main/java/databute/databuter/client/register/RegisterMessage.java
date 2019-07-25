package databute.databuter.client.register;

import com.google.common.base.MoreObjects;
import databute.databuter.client.network.ClientMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

public class RegisterMessage implements Message {

    @Override
    public MessageCode messageCode() {
        return ClientMessageCode.REGISTER;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .toString();
    }
}