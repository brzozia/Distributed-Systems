package sr.ice.office;

import OfficeData.*;
import com.zeroc.Ice.ConnectionLostException;
import sr.ice.data.ClientData;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfficeC implements OfficeData.Office {
    private final ConcurrentMap<String, ClientData> map;
    private final Random rand;
    private final int minTime; //millis
    private final int maxTime;
    private final ExecutorService executor;
    private int requestCounter;

    public OfficeC() {
        this.minTime = 1000;
        this.maxTime = 10*1000;
        this.map = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
        this.requestCounter = 0;
        this.rand = new Random();
    }

    private int prepareReceipt(){
        requestCounter += 1;
        return rand.nextInt(maxTime + minTime);
    }

    @Override
    public int requestBuildingIssue(BuildingPermission issue, com.zeroc.Ice.Current current) {
        int time = prepareReceipt();

        executor.execute(new Worker(issue, map, time, requestCounter, "BuildingIssue"));
        return time/1000;
    }

    @Override
    public int requestVehicleIssue(VehicleRegistration issue, com.zeroc.Ice.Current current) {
        int time = prepareReceipt();

        executor.execute(new Worker(issue, map, time, requestCounter, "VehicleIssue"));
        return time/1000;
    }

    @Override
    public int requestIDCardIssue(IDCardIssuing issue, com.zeroc.Ice.Current current) {
        int time = prepareReceipt();

        executor.execute(new Worker(issue, map, time, requestCounter, "IDCardIssue"));
        return time/1000;
    }

    @Override
    public void connect(String pesel, CitizenPrx proxy, com.zeroc.Ice.Current current) {

        if(!map.containsKey(pesel)) {
            map.put(pesel, new ClientData(proxy.ice_fixed(current.con)));
            System.out.println("Citizen " + pesel + " callback address added");
        }
        else {
            synchronized (map) {
                map.get(pesel).setProxy(proxy.ice_fixed(current.con));
            }
            System.out.println("Citizen " + pesel + " callback address updated");

            if (map.get(pesel).getResponses() != null) {
                while (map.get(pesel).getResponses().size()>0) {
                    try {
                        synchronized (map) {
                            map.get(pesel).getProxy().handleResponse(map.get(pesel).getResponses().get(0));
                            map.get(pesel).getResponses().remove(0);
                        }
                        System.out.println("Overdue response send to " + pesel);
                    } catch (ConnectionLostException e) {
                        System.out.println("Connection Lost Exception occurred while sending again to " + pesel);
                        break;
                    }
                }

            }
        }
    }
}
