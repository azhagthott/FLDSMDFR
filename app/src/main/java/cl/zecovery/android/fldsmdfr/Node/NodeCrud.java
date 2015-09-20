package cl.zecovery.android.fldsmdfr.Node;

import java.util.List;

/**
 * Created by fran on 18-09-15.
 */
public interface NodeCrud {

    void addNode(Node node);
    Node getNode(int nodeId);
    List<Node> getAllNodes();
    int getNodeCount(int result);
    int updateNode(Node node);
    void deleteNode(Node node);
}
