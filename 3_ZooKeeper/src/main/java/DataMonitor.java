import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.*;
import static org.apache.zookeeper.AddWatchMode.PERSISTENT_RECURSIVE;


public class DataMonitor implements Watcher {
    private final ZooKeeper zooKeeper;
    private final String znode;
    private boolean isDead;
    private final Connector listener;

    public DataMonitor(ZooKeeper zooKeeper, String znode, Connector listener) {
        this.zooKeeper = zooKeeper;
        this.znode = znode;
        this.listener = listener;
        this.isDead = false;

        try {
            zooKeeper.addWatch(znode, PERSISTENT_RECURSIVE);
            zooKeeper.getChildren(znode, true);
        } catch (KeeperException | InterruptedException ignored) {
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired, Closed:
                    isDead = true;
                    listener.closing(KeeperException.Code.SessionExpired);
                    break;
            }
        }else if(path.equals(znode) && event.getType() == Event.EventType.NodeCreated){
            try {
                zooKeeper.addWatch(znode, PERSISTENT_RECURSIVE);
                zooKeeper.getChildren(znode, true);
                listener.runApp();
            } catch (IOException | InterruptedException | KeeperException e) {
                e.printStackTrace();
            }

        }else if(path.equals(znode) && event.getType() == Event.EventType.NodeDeleted){
            isDead = true;
            listener.closeApp();
        }
        else if(event.getType() == Event.EventType.NodeChildrenChanged){
            try {
                if(zooKeeper.exists(path,true)!=null){
                    int list= zooKeeper.getAllChildrenNumber(znode);
                    watchChildren(znode);
                    listener.childrenNo(list);
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void watchChildren(String node)  {
        List<String> list = null;
        try {
            list = zooKeeper.getChildren(node, true);
            if(list.size()>0){
                for(String child : list){
                    watchChildren(node+"/"+child);
                }
            }
        } catch (KeeperException | InterruptedException ignored) {
        }
    }

    public boolean exists() throws KeeperException, InterruptedException {
        if(zooKeeper.exists(znode,true)!=null)
            return true;
        else
            return false;
    }


    public List<String> getChildrenList(String node) throws KeeperException, InterruptedException {
        if(zooKeeper.exists(node,true)!=null)
            return zooKeeper.getChildren(node, true);
        else
            return new ArrayList<>();
    }
}

