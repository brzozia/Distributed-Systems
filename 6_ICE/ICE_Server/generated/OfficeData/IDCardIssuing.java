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

public class IDCardIssuing extends Request
{
    public IDCardIssuing()
    {
        super();
        this.phoneNumber = "";
        this.familyName = "";
        this.gender = Gender.woman;
        this.reason = IDReason.firstID;
    }

    public IDCardIssuing(Person person, String phoneNumber, String familyName, Gender gender, IDReason reason)
    {
        super(person);
        this.phoneNumber = phoneNumber;
        this.familyName = familyName;
        this.gender = gender;
        this.reason = reason;
    }

    public String phoneNumber;

    public String familyName;

    public Gender gender;

    public IDReason reason;

    public IDCardIssuing clone()
    {
        return (IDCardIssuing)super.clone();
    }

    public static String ice_staticId()
    {
        return "::OfficeData::IDCardIssuing";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = 390027504L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, false);
        ostr_.writeString(phoneNumber);
        ostr_.writeString(familyName);
        Gender.ice_write(ostr_, gender);
        IDReason.ice_write(ostr_, reason);
        ostr_.endSlice();
        super._iceWriteImpl(ostr_);
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        phoneNumber = istr_.readString();
        familyName = istr_.readString();
        gender = Gender.ice_read(istr_);
        reason = IDReason.ice_read(istr_);
        istr_.endSlice();
        super._iceReadImpl(istr_);
    }
}
