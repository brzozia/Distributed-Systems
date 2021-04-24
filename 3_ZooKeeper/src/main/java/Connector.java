import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Connector implements  Runnable,Watcher
{
    private final DataMonitor dataMonitor;
    private final ZooKeeper zooKeeper;
    private final String exec;
    private Process child;
    private int childrenNumber;
    private final String znode;

    public Connector(String hostPort, String znodeName, String exec) throws IOException,KeeperException {
        this.exec = exec;
        System.out.println(hostPort);
        child = null;
        childrenNumber = 0;
        znode = znodeName;
        zooKeeper = new ZooKeeper(hostPort, 3000, this);
        dataMonitor = new DataMonitor(zooKeeper, znodeName, this);
        runThread();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Wrong number of arguments");
            return;
        }
        String hostPort = args[0];
        String znodeName = args[1];
        String exec = args[2];
        try {
            new Connector(hostPort, znodeName, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    public void run() {
        try {
            synchronized (this) {
                while (!dataMonitor.isDead()) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Exception occured:"+e);
        }
    }

    private void runThread(){
        Runnable fun = () ->{
            try {
                threadFun();
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread readInput = new Thread(fun);
        readInput.start();
    }

    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    public void runApp() throws IOException {
        child = Runtime.getRuntime().exec(exec);
    }

    public void closeApp(){
        if (child != null) {
            child.destroyForcibly();
            try {
                child.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void childrenNo(int number){
        if(childrenNumber<number){
            System.out.println("Current number of children: "+number);
        }
        childrenNumber = number;
    }

    public void showChildren(String node, String prefix) throws KeeperException, InterruptedException {
      List<String> children= dataMonitor.getChildrenList(node);
        if(children.size()!=0){
            for(String child: children){
                System.out.println(prefix+"|--"+child);
                showChildren(node+'/'+child, prefix+"   ");
            }
        }
    }

    private  void showStructure() throws KeeperException, InterruptedException {
        if(dataMonitor.exists()){
            System.out.println("z");
            showChildren(znode, " ");
        }else{
            System.out.println("no nodes");
        }

    }

    private void threadFun() throws KeeperException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Type 'tree' to see znode tree structure");
        String input = "input";

        while(!input.equals("exit")){
            input = scan.nextLine();
            if(input.equals("tree")){
                showStructure();
            }
        }
        scan.close();
    }
}