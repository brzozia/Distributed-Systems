package sr.ice.office;

import OfficeData.Request;
import OfficeData.Result;
import com.zeroc.Ice.ConnectionLostException;
import sr.ice.data.ClientData;

import java.util.Map;

public class Worker implements Runnable {
    private final Request permission;
    private final Map<String, ClientData> map;
    private final int time;
    private final int requestId;
    private final String requestType;

    public Worker(Request permission, Map<String, ClientData> map, int time, int requestId, String requestType) {
        this.map = map;
        this.time = time;
        this.requestId = requestId;
        this.permission = permission;
        this.requestType = requestType;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Result result = new Result(requestId, requestType, "Permission granted.");

        try {
            map.get(permission.person.pesel).getProxy().handleResponse(result);
            System.out.println("["+requestType+"] "+"Response send to "+permission.person.pesel);
        }
        catch(ConnectionLostException e){
            map.get(permission.person.pesel).addResponse(result);
            System.out.println("["+requestType+"] "+"Connection Lost Exception occurred while sending to "+permission.person.pesel);
        }
    }

}
