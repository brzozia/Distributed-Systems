import sys
import Ice
import OfficeData as office


class CitizenC(office.Citizen):
    def handleResponse(self, result, context):
        print("========= Response issue "+str(result.id)+" =========")
        print("Type of issue: "+str(result.requestType))
        print("Message: "+str(result.message)+"\n")


if __name__ == "__main__":

    communicator = Ice.initialize(sys.argv)
    server = communicator.stringToProxy("office/office:default -p 10000")
    try:
        office_proxy = office.OfficePrx.checkedCast(server)
    except ConnectionRefusedError:
        print("Exception occurred during connection. Server refused connection.")

    if not office_proxy:
        raise RuntimeError("Invalid proxy")

    adapter = communicator.createObjectAdapter("")
    citizen = office.CitizenPrx.uncheckedCast(adapter.addWithUUID(CitizenC()))

    office_proxy.ice_getCachedConnection().setAdapter(adapter)

    print("enter your pesel to log in")
    pesel = input()
    office_proxy.connect(pesel, citizen)

    person = office.Person("Jan", "Kowalski", pesel)

    command = ""
    while command != "exit":
        command = input("==>")
        if command == "building":
            request = office.BuildingPermission(person, "Krakow", "Poziomkowa", 128, office.BuildingOptions.NewBuilding)
            time = office_proxy.requestBuildingIssue(request)
            print("Request sent. It will take aprox. "+str(time)+" seconds")
        elif command == "idcard":
            request = office.IDCardIssuing(person, "123943172", "Kowalski", office.Gender.man, office.IDReason.idLost)
            time = office_proxy.requestIDCardIssue(request)
            print("Request sent. It will take aprox. " + str(time) + " seconds")
        elif command == "car":
            request = office.VehicleRegistration(person, "Kia", "JMZBM548352103252", 2018, 5)
            time = office_proxy.requestVehicleIssue(request)
            print("Request sent. It will take aprox. " + str(time) + " seconds")

    communicator.waitForShutdown()


