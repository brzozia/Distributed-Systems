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

public class BuildingPermission extends Request
{
    public BuildingPermission()
    {
        super();
        this.city = "";
        this.street = "";
        this.option = BuildingOptions.NewBuilding;
    }

    public BuildingPermission(Person person, String city, String street, short number, BuildingOptions option)
    {
        super(person);
        this.city = city;
        this.street = street;
        this.number = number;
        this.option = option;
    }

    public String city;

    public String street;

    public short number;

    public BuildingOptions option;

    public BuildingPermission clone()
    {
        return (BuildingPermission)super.clone();
    }

    public static String ice_staticId()
    {
        return "::OfficeData::BuildingPermission";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = 1049825307L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, false);
        ostr_.writeString(city);
        ostr_.writeString(street);
        ostr_.writeShort(number);
        BuildingOptions.ice_write(ostr_, option);
        ostr_.endSlice();
        super._iceWriteImpl(ostr_);
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        city = istr_.readString();
        street = istr_.readString();
        number = istr_.readShort();
        option = BuildingOptions.ice_read(istr_);
        istr_.endSlice();
        super._iceReadImpl(istr_);
    }
}
