
#ifndef OFFICE_ICE
#define OFFICE_ICE

module OfficeData
{
  enum BuildingOptions {NewBuilding, BuildingExpansion, BuildingDemolition };
  enum Gender {woman, man};
  enum IDReason {firstID, dataChange, imageChange, idDamage, idLost};

  struct Person{
    string name;
    string surname;
    string pesel;
  }

  class Request
  {
    Person person;
  }

  class BuildingPermission extends Request
  {
    string city;
    string street;
    short number;
    BuildingOptions option;
  }

  class VehicleRegistration extends Request
  {
    string carBrand;
    string chassisNumber;
    short productionYear;
    short seatsNumber;
  }

  class IDCardIssuing extends Request
  {
    string phoneNumber;
    string familyName;
    Gender gender;
    IDReason reason;
  }

  struct Result
  {
    int id;
    string requestType;
    string message;
  }

  interface Citizen
  {
    void handleResponse(Result result);
  }

  interface Office
  {
    int requestBuildingIssue(BuildingPermission issue); //returns expected time int czy short?
    int requestVehicleIssue(VehicleRegistration issue);
    int requestIDCardIssue(IDCardIssuing issue);
    void connect(string pesel, Citizen* proxy);
  };

};

#endif
