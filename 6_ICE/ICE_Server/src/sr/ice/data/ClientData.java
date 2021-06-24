package sr.ice.data;

import OfficeData.CitizenPrx;
import OfficeData.Result;

import java.util.ArrayList;
import java.util.List;

public class ClientData {
    private final List<Result> responses;

    private CitizenPrx proxy;

    public List<Result> getResponses() {
        return responses;
    }

    public void addResponse(Result responses) {
        this.responses.add(responses);
    }

    public CitizenPrx getProxy() {
        return proxy;
    }

    public void setProxy(CitizenPrx proxy) {
        this.proxy = proxy;
    }

    public ClientData(CitizenPrx proxy) {
        this.responses = new ArrayList<>();
        this.proxy = proxy;
    }

}
