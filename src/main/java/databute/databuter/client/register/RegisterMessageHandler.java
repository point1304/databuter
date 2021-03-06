package databute.databuter.client.register;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketGroup;
import databute.databuter.bucket.notification.BucketNotificationMessage;
import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
import databute.databuter.cluster.ClusterNode;
import databute.databuter.cluster.local.LocalClusterNode;
import databute.databuter.cluster.notification.ClusterNodeNotificationMessage;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.cluster.remote.RemoteClusterNodeGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterMessageHandler extends ClientMessageHandler<RegisterMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterMessageHandler.class);

    public RegisterMessageHandler(ClientSession session) {
        super(session);
    }

    @Override
    public void handle(RegisterMessage register) {
        session().startListen();
        logger.debug("Session {} is listening update.", session());

        Databuter.instance().clientSessionGroup().addListeningSession(session());

        sendLocalNode();
        sendRemoteNodes();

        sendBuckets();
    }

    private void sendLocalNode() {
        final LocalClusterNode localNode = Databuter.instance().clusterCoordinator().localNode();
        sendAddClusterNodeMessage(localNode);
    }

    private void sendRemoteNodes() {
        final RemoteClusterNodeGroup remoteNodeGroup = Databuter.instance().clusterCoordinator().remoteNodeGroup();
        for (RemoteClusterNode remoteNode : remoteNodeGroup) {
            sendAddClusterNodeMessage(remoteNode);
        }
    }

    private void sendAddClusterNodeMessage(ClusterNode node) {
        session().send(ClusterNodeNotificationMessage.added()
                .id(node.id())
                .endpoint(node.outboundEndpoint())
                .build());
    }

    private void sendBuckets() {
        final BucketGroup bucketGroup = Databuter.instance().bucketGroup();
        for (Bucket bucket : bucketGroup) {
            sendBucketAddedNotificationMessage(bucket);
        }
    }

    private void sendBucketAddedNotificationMessage(Bucket bucket) {
        session().send(BucketNotificationMessage.added()
                .id(bucket.id())
                .keyFactor(bucket.keyFactor())
                .activeNodeId(bucket.activeNodeId())
                .standbyNodeId(bucket.standbyNodeId())
                .build());
    }
}
