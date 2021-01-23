package com.entity.model;


import org.springframework.cglib.core.internal.Function;

public enum JurisdictionPredicateAttributes {

    name(Jurisdiction::getName),
    type(Jurisdiction::getType),
    code(Jurisdiction::getCode),
    country(Jurisdiction::getCountry),
    loation(Jurisdiction::getLocation),
    associatedApps(Jurisdiction::getAssociatedApps);
    final public Function<Jurisdiction, String> getter;

    JurisdictionPredicateAttributes(Function<Jurisdiction, String> getter){
        this.getter = getter;
    }
}
