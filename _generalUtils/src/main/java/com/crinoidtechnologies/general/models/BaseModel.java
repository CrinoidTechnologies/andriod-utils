package com.crinoidtechnologies.general.models;

import java.io.Serializable;

/**
 * Created by ${Vivek} on 5/4/2016 for CrinoidTechnologies.
 */
public interface BaseModel extends Serializable, Cloneable {
    String getUniqueId();

    String getJsonString();

    boolean containSearchString(String searchString);
}
