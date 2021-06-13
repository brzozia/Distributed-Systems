package sr.grpc.server;

import sr.grpc.gen.ComplexArithmeticOpResult;
import sr.grpc.gen.AdvancedCalculatorGrpc.AdvancedCalculatorImplBase;

public class AdvancedCalculatorImpl extends AdvancedCalculatorImplBase 
{
	@Override
	public void complexOperation(sr.grpc.gen.ComplexArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<sr.grpc.gen.ComplexArithmeticOpResult> responseObserver) 
	{
		System.out.println("multipleArgumentsRequest (" + request.getOptypeValue() + ", #" + request.getArgsCount() +")");

		double res = 0;

		if(request.getArgsCount() == 0) {
			System.out.println("No arguments");
		}
		else {
			switch (request.getOptype()) {
				case SUM:
					for (Double d : request.getArgsList()) res += d;
					break;
				case AVG:
					for (Double d : request.getArgsList()) res += d;
					res /= request.getArgsCount();
					break;
				case MIN:
					Double m = request.getArgsList().get(0);
					for (Double d : request.getArgsList()) {
						if (d < m)
							m = d;
					}
					res = m;
					break;
				case MAX:
					Double max = request.getArgsList().get(0);
					for (Double d : request.getArgsList()) {
						if (d > max)
							max = d;
					}
					res = max;
					break;
				case UNRECOGNIZED:
					break;
			}
		}
		
		ComplexArithmeticOpResult result = ComplexArithmeticOpResult.newBuilder().setRes(res).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}
}
