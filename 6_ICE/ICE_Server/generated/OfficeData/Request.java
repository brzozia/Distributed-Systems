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

public class Request extends com.zeroc.Ice.Value
{
    public Request()
    {
        this.person = new Person();
    }

    public Request(Person person)
    {
        this.person = person;
    }

    public Person person;

    public Request clone()
    {
        return (Request)super.clone();
    }

    public static String ice_staticId()
    {
        return "::OfficeData::Request";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = 205360290L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, true);
        Person.ice_write(ostr_, person);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        person = Person.ice_read(istr_);
        istr_.endSlice();
    }
}