package sr.ice;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import sr.ice.office.OfficeC;

public class IceServer
{
	public static void main(String[] args)
	{
		String[] connArgs = {"--Ice.ThreadPool.Client.Size=2", "--Ice.ThreadPool.Server.Size=2"};
		Communicator communicator = Util.initialize(connArgs);

		ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("office", "default -p 10000");
		OfficeC officeServant = new OfficeC();

		adapter.add(officeServant, new Identity("office", "office"));
		adapter.activate();

		System.out.println("Server started");

		communicator.waitForShutdown();

		communicator.destroy();
	}
}
