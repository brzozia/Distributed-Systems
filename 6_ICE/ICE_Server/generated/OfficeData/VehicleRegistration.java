//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.5
//
// <auto-generated>
//
// Generated from file `office.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package OfficeData;

public class VehicleRegistration extends Request
{
    public VehicleRegistration()
    {
        super();
        this.carBrand = "";
        this.chassisNumber = "";
    }

    public VehicleRegistration(Person person, String carBrand, String chassisNumber, short productionYear, short seatsNumber)
    {
        super(person);
        this.carBrand = carBrand;
        this.chassisNumber = chassisNumber;
        this.productionYear = productionYear;
        this.seatsNumber = seatsNumber;
    }

    public String carBrand;

    public String chassisNumber;

    public short productionYear;

    public short seatsNumber;

    public VehicleRegistration clone()
    {
        return (VehicleRegistration)super.clone();
    }

    public static String ice_staticId()
    {
        return "::OfficeData::VehicleRegistration";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = 2076537102L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, false);
        ostr_.writeString(carBrand);
        ostr_.writeString(chassisNumber);
        ostr_.writeShort(productionYear);
        ostr_.writeShort(seatsNumber);
        ostr_.endSlice();
        super._iceWriteImpl(ostr_);
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        carBrand = istr_.readString();
        chassisNumber = istr_.readString();
        productionYear = istr_.readShort();
        seatsNumber = istr_.readShort();
        istr_.endSlice();
        super._iceReadImpl(istr_);
    }
}