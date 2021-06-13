package sr.grpc.server;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;


public class grpcServerAdvancedCalc
{
    private final int port;
    private Server server;

    public grpcServerAdvancedCalc(int port) {
        this.port = port;
    }

    public void start() throws IOException
    {

        server = ServerBuilder/*NettyServerBuilder*/.forPort(port).executor(Executors.newFixedThreadPool(16))
                .addService(new AdvancedCalculatorImpl())
                .addService(new StreamTesterImpl())
                .build()
                .start();
        System.out.println("Server advanced calc start started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                grpcServerAdvancedCalc.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final grpcServerAdvancedCalc server = new grpcServerAdvancedCalc(Integer.parseInt(args[0]));
        server.start();
        server.blockUntilShutdown();
    }

}
