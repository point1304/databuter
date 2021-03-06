package databute.databuter.entry.result.fail;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryOperationFailMessageSerializer implements MessageSerializer<EntryOperationFailMessage> {

    @Override
    public Packet serialize(EntryOperationFailMessage entryOperationFailMessage) {
        checkNotNull(entryOperationFailMessage, "entryOperationFailMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(entryOperationFailMessage.id());
        packet.writeString(entryOperationFailMessage.key());
        packet.writeString(entryOperationFailMessage.errorCode().name());
        return packet;
    }
}
