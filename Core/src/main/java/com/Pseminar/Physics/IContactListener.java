package com.Pseminar.Physics;

import org.jbox2d.dynamics.contacts.Contact;

public interface IContactListener {
    public void OnContactBegin(Contact contact);

    public void OnContactEnd(Contact contact);
} 
